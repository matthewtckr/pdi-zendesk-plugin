/*! ******************************************************************************
 *
 * Pentaho Data Integration
 *
 * Copyright (C) 2002-2018 by Pentaho : http://www.pentaho.com
 *
 *******************************************************************************
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 ******************************************************************************/

package org.pentaho.di.trans.steps.zendesk;

import java.util.ArrayList;
import java.util.List;

import org.pentaho.di.core.Const;
import org.pentaho.di.core.exception.KettleException;
import org.pentaho.di.core.exception.KettleStepException;
import org.pentaho.di.core.row.RowDataUtil;
import org.pentaho.di.core.row.RowMeta;
import org.pentaho.di.i18n.BaseMessages;
import org.pentaho.di.trans.Trans;
import org.pentaho.di.trans.TransMeta;
import org.pentaho.di.trans.step.StepDataInterface;
import org.pentaho.di.trans.step.StepMeta;
import org.pentaho.di.trans.step.StepMetaInterface;
import org.zendesk.client.v2.ZendeskResponseException;
import org.zendesk.client.v2.model.Identity;
import org.zendesk.client.v2.model.User;

public class ZendeskInputUsers extends ZendeskInput {

  ZendeskInputUsersMeta meta;
  ZendeskInputUsersData data;

  @Override
  public boolean init( StepMetaInterface smi, StepDataInterface sdi ) {
    if ( !super.init( smi, sdi ) ) {
      return false;
    }
    meta = (ZendeskInputUsersMeta) smi;
    data = (ZendeskInputUsersData) sdi;
    return true;
  }

  @Override
  public boolean processRow( StepMetaInterface smi, StepDataInterface sdi ) throws KettleException {
    Object[] row = getRow();

    if ( first ) {
      first = false;

      if ( getInputRowMeta() != null ) {
        data.isReceivingInput = true;
        data.incomingIndex = getInputRowMeta().indexOfValue( environmentSubstitute( meta.getIncomingFieldname() ) );
      } else {
        data.isReceivingInput = !Const.isEmpty( environmentSubstitute( meta.getIncomingFieldname() ) );
        data.incomingIndex = -1;
      }
      if ( meta.getUserStepMeta() != null ) {
        data.userRowMeta = new RowMeta();
        meta.getFields( data.userRowMeta, getStepname(), null, meta.getUserStepMeta(), this, repository, metaStore );
        data.userOutputRowSet = findOutputRowSet( meta.getUserStepMeta().getName() );
      }

      if ( meta.getUserIdentityStepMeta() != null ) {
        data.userIdentityRowMeta = new RowMeta();
        meta.getFields( data.userIdentityRowMeta, getStepname(), null, meta.getUserIdentityStepMeta(), this, repository, metaStore );
        data.userIdentityOutputRowSet = findOutputRowSet( meta.getUserIdentityStepMeta().getName() );
      }
    }

    if ( !data.isReceivingInput ) {
      Iterable<User> users = null;
      try {
        users = data.conn.getUsers();
      } catch ( ZendeskResponseException zre ) {
        logError( BaseMessages.getString( PKG, "ZendeskInput.Error.Generic", zre ) );
        setErrors( 1L );
        setOutputDone();
        return false;
      }
      int i = 0;
      for ( User user : users ) {
        if ( isStopped() ) {
          break;
        }
        if ( user != null ) {
          i++;
          List<Identity> identities = new ArrayList<Identity>();
          try {
            if ( meta.getUserIdentityStepMeta() != null && data.userIdentityOutputRowSet != null ) {
              for ( Identity identity : data.conn.getUserIdentities( user.getId() ) ) {
                identities.add( identity );
              }
            }
          } catch ( ZendeskResponseException zre ) {
            logError( BaseMessages.getString( PKG, "ZendeskInput.Error.Generic", zre ) );
            setErrors( 1L );
            setOutputDone();
            return false;
          }
          outputUserRow( user );
          outputUserIdentityRow( identities );
          incrementLinesOutput();
        }
      }
      if ( !isStopped() ) {
        logBasic( "Total Users: " + i );
      }
      setOutputDone();
      return false;
    } else if ( row == null ) {
      setOutputDone();
      return false;
    } else {
      if ( data.incomingIndex < 0 ) {
        logError( BaseMessages.getString( PKG, "ZendeskInput.Error.MissingField",
          environmentSubstitute( meta.getIncomingFieldname() ) ) );
        setErrors( 1L );
        setOutputDone();
        return false;
      }

      Long userId = getInputRowMeta().getValueMeta( data.incomingIndex ).getInteger( row[data.incomingIndex] );
      User user = null;
      try {
        user = data.conn.getUser( userId );
      } catch ( ZendeskResponseException zre ) {
        if ( 404 == zre.getStatusCode() && getStepMeta().isDoingErrorHandling() ) {
          putError( getInputRowMeta(), row, 1L, zre.toString(),
            getInputRowMeta().getValueMeta( data.incomingIndex ).getName(), zre.getStatusText() );
        } else {
          logError( BaseMessages.getString( PKG, "ZendeskInput.Error.Generic", zre ) );
          setErrors( 1L );
          setOutputDone();
          return false; // die unknown error
        }
      }
      if ( user != null ) {
        List<Identity> identities = new ArrayList<Identity>();
        try {
          if ( meta.getUserIdentityStepMeta() != null && data.userIdentityOutputRowSet != null ) {
            for ( Identity identity : data.conn.getUserIdentities( user.getId() ) ) {
              identities.add( identity );
            }
          }
        } catch ( ZendeskResponseException zre ) {
          if ( 404 == zre.getStatusCode() && getStepMeta().isDoingErrorHandling() ) {
            putError( getInputRowMeta(), row, 1L, zre.toString(),
              getInputRowMeta().getValueMeta( data.incomingIndex ).getName(), zre.getStatusText() );
          } else {
            logError( BaseMessages.getString( PKG, "ZendeskInput.Error.Generic", zre ) );
            setErrors( 1L );
            setOutputDone();
            return false;
          }
        }
        outputUserRow( user );
        outputUserIdentityRow( identities );
        incrementLinesOutput();
      }
      return true;
    }
  }

  public ZendeskInputUsers( StepMeta stepMeta, StepDataInterface stepDataInterface, int copyNr,
      TransMeta transMeta, Trans trans ) {
    super( stepMeta, stepDataInterface, copyNr, transMeta, trans );
  }

  private void outputUserRow( User user ) throws KettleStepException {
    if ( data.userRowMeta == null || data.userRowMeta.isEmpty() ) {
      return;
    }

    Object[] outputRow = RowDataUtil.allocateRowData( data.userRowMeta.size() );
    for ( int i = 0; i < meta.getUserFields().length; i++ ) {
    	outputRow[i] = data.getValue( user, meta.getUserFields()[i].getType() );
    }
    putRowTo( data.userRowMeta, outputRow, data.userOutputRowSet );
  }

  private void outputUserIdentityRow( List<Identity> identities ) throws KettleStepException {
    if ( data.userIdentityRowMeta == null || data.userIdentityRowMeta.isEmpty()
        || identities == null || identities.isEmpty() || data.userIdentityOutputRowSet == null ) {
      return;
    }

    for ( Identity ident : identities ) {
      Object[] outputRow = RowDataUtil.allocateRowData( data.userIdentityRowMeta.size() );
      for ( int i = 0; i < meta.getIdentityFields().length; i++ ) {
        outputRow[i] = data.getValue( ident, meta.getIdentityFields()[i].getType() );
      }
      putRowTo( data.userIdentityRowMeta, outputRow, data.userIdentityOutputRowSet );
    }
  }
}
