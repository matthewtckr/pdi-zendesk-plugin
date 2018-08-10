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

import java.util.Date;

import org.pentaho.di.core.Const;
import org.pentaho.di.core.exception.KettleException;
import org.pentaho.di.core.exception.KettleStepException;
import org.pentaho.di.core.row.RowMeta;
import org.pentaho.di.core.row.RowMetaInterface;
import org.pentaho.di.core.row.ValueMetaInterface;
import org.pentaho.di.core.row.value.ValueMetaDate;
import org.pentaho.di.core.row.value.ValueMetaFactory;
import org.pentaho.di.i18n.BaseMessages;
import org.pentaho.di.trans.Trans;
import org.pentaho.di.trans.TransMeta;
import org.pentaho.di.trans.step.StepDataInterface;
import org.pentaho.di.trans.step.StepMeta;
import org.pentaho.di.trans.step.StepMetaInterface;
import org.zendesk.client.v2.model.Organization;
import org.zendesk.client.v2.model.Ticket;
import org.zendesk.client.v2.model.User;
import org.zendesk.client.v2.model.hc.Article;
import org.zendesk.client.v2.ZendeskResponseException;
import java.util.Iterator;

public class ZendeskInputIncremental extends ZendeskInput {

  ZendeskInputIncrementalMeta meta;
  ZendeskInputIncrementalData data;

  public ZendeskInputIncremental( StepMeta stepMeta, StepDataInterface stepDataInterface, int copyNr,
      TransMeta transMeta, Trans trans ) {
    super( stepMeta, stepDataInterface, copyNr, transMeta, trans );
  }

  @Override
  public boolean init( StepMetaInterface smi, StepDataInterface sdi ) {
    if ( super.init( smi, sdi ) ) {
      meta = (ZendeskInputIncrementalMeta) super.meta;
      data = (ZendeskInputIncrementalData) super.data;
      return true;
    }
    return false;
  }

  @Override
  public boolean processRow( StepMetaInterface smi, StepDataInterface sdi ) throws KettleException {
    Date startDate = new Date( 0L );
    if ( first ) {
      first = false;
      try {
        startDate = getIncrementalFieldValue();
        if ( startDate == null ) {
          setOutputDone();
          return false;
        }
      } catch ( KettleStepException e ) {
        setErrors( 1L );
        logError( e.getMessage() );
        setOutputDone();
        return false;
      }
      if ( data.rowMeta == null ) {
        data.rowMeta = new RowMeta();
        meta.getFields( data.rowMeta, getStepname(), null, null, this, getRepository(), getMetaStore() );
      }
    }

    Iterator<?> serviceIterator = null;

    switch ( meta.getDownloadType() ) {
      case TICKETS:
        serviceIterator = data.conn.getTicketsIncrementally( startDate ).iterator();
        break;
      case USERS:
        serviceIterator = data.conn.getUsersIncrementally( startDate ).iterator();
        break;
      case ORGANIZATIONS:
        serviceIterator = data.conn.getOrganizationsIncrementally( startDate ).iterator();
        break;
      case HELPCENTER_ARTICLES:
        serviceIterator = data.conn.getArticlesIncrementally( startDate ).iterator();
        break;
      default:
        break;
    }

    try {
      while ( !isStopped() && serviceIterator.hasNext() ) {
        switch ( meta.getDownloadType() ) {
          case TICKETS:
            Ticket ticket = (Ticket) ( serviceIterator.next() );
            putRow( data.rowMeta, processTicket( ticket ) );
            break;
          case USERS:
            User user = (User) ( serviceIterator.next() );
            putRow( data.rowMeta, processUser( user ) );
            break;
          case ORGANIZATIONS:
            Organization org = (Organization) ( serviceIterator.next() );
            putRow( data.rowMeta, processOrganization( org ) );
            break;
          case HELPCENTER_ARTICLES:
            Article article = (Article) ( serviceIterator.next() );
            putRow( data.rowMeta, processArticle( article ) );
            break;
        }
        incrementLinesOutput();
      }
    } catch ( ZendeskResponseException zre ) {
      logError( BaseMessages.getString( PKG, "ZendeskInput.Error.Generic", zre ) );
    }
    setOutputDone();
    return false;
  }

  private Date getIncrementalFieldValue() throws KettleException {
    Date result = null;
    boolean firstRow = true;
    Object[] row;
    RowMetaInterface inputRowMeta;

    while ( ( row = getRow() ) != null ) {
      if ( firstRow ) {
        firstRow = false;
        inputRowMeta = getInputRowMeta();

        if ( inputRowMeta == null || inputRowMeta.size() <= 0 ) {
          if ( log.isBasic() ) {
            logBasic( BaseMessages.getString( PKG, "ZendeskInput.Error.NoIncomingRows" ) );
          }
          return null;
        }

        String filenameField = environmentSubstitute( meta.getTimestampFieldName() );
        int fieldIndex = inputRowMeta.indexOfValue( filenameField );
        if ( fieldIndex < 0 ) {
          throw new KettleStepException( BaseMessages.getString(
            PKG, "ZendeskInputIncremental.Exception.StartDateFieldNotFound", filenameField ) );
        }
        ValueMetaInterface fieldValueMeta = inputRowMeta.getValueMeta( fieldIndex );
        if ( !( fieldValueMeta instanceof ValueMetaDate ) ) {
          throw new KettleStepException( BaseMessages.getString( PKG, "ZendeskInput.Error.WrongFieldType",
            ValueMetaFactory.getValueMetaName( fieldValueMeta.getType() ) ) );
        } else {
          result = fieldValueMeta.getDate( row[fieldIndex] );
        }
      } else {
        if ( log.isDetailed() ) {
          logDetailed( BaseMessages.getString( PKG, "ZendeskInput.Warning.IgnoringAdditionalInputRows" ) );
        }
      }
    }

    if ( firstRow ) {
      if ( log.isBasic() ) {
        logBasic( BaseMessages.getString( PKG, "ZendeskInput.Error.NoIncomingRows" ) );
      }
    }

    return result;
  }

  Object[] processTicket( Ticket ticket ) {
    if ( !Const.isEmpty( environmentSubstitute( meta.getStatusFieldName() ) ) ) { 
      return new Object[]{ ticket.getId(), ticket.getStatus() };
    } else {
      return new Object[] { ticket.getId() };
    }
  }

  Object[] processUser( User user ) {
    return new Object[]{ user.getId() };
  }

  Object[] processOrganization( Organization org ) {
    return new Object[]{ org.getId() };
  }

  Object[] processArticle( Article article ) {
    return new Object[]{ article.getId() };
  }
}
