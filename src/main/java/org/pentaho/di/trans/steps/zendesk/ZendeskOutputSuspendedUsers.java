/*! ******************************************************************************
 *
 * Pentaho Data Integration
 *
 * Copyright (C) 2002-2017 by Pentaho : http://www.pentaho.com
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

import org.pentaho.di.core.exception.KettleException;
import org.pentaho.di.core.row.RowDataUtil;
import org.pentaho.di.trans.Trans;
import org.pentaho.di.trans.TransMeta;
import org.pentaho.di.trans.step.StepDataInterface;
import org.pentaho.di.trans.step.StepMeta;
import org.pentaho.di.trans.step.StepMetaInterface;
import org.zendesk.client.v2.ZendeskResponseException;

public class ZendeskOutputSuspendedUsers extends ZendeskInput {

  private ZendeskOutputSuspendedUsersMeta meta;
  private ZendeskOutputSuspendedUsersData data;

  @Override
  public boolean init( StepMetaInterface smi, StepDataInterface sdi ) {
    if ( !super.init( smi, sdi ) ) {
      return false;
    }
    meta = (ZendeskOutputSuspendedUsersMeta) smi;
    data = (ZendeskOutputSuspendedUsersData) sdi;
    return true;
  }

  @Override
  public boolean processRow( StepMetaInterface smi, StepDataInterface sdi ) throws KettleException {

    Object[] r = getRow();

    if ( r == null ) {
      setOutputDone();
      return false;
    }

    if ( first ) {
      data.userFieldNameIndex = getInputRowMeta().indexOfValue( meta.getUserFieldName() );
      data.actionFieldNameIndex = getInputRowMeta().indexOfValue( meta.getActionFieldName() );
      first = false;
      data.outputRowMeta = getInputRowMeta().clone();
      meta.getFields( data.outputRowMeta, getStepname(), null, null, this, repository, metaStore );
      data.resultFieldNameIndex = data.outputRowMeta.indexOfValue( meta.getResultFieldName() );
    }

    Object[] outputRow = RowDataUtil.resizeArray( r, data.outputRowMeta.size() );

    Long userId = (Long) r[data.userFieldNameIndex];
    Boolean suspend = (Boolean) r[data.actionFieldNameIndex];
    Boolean result = false;

    try {
      result = data.suspendUser( userId, suspend );
    } catch ( ZendeskResponseException zre ) {
      if ( 404 == zre.getStatusCode() ) {
        // User does not exist, pass row but mark as failure
        result = false;
      } else {
        // Some other error (e.g. bad network?)
        throw zre;
      }
    }

    if ( data.resultFieldNameIndex >= 0 ) {
      outputRow[data.resultFieldNameIndex] = result;
    }
    putRow( data.outputRowMeta, outputRow );
    return true;
  }

  public ZendeskOutputSuspendedUsers( StepMeta stepMeta, StepDataInterface stepDataInterface, int copyNr,
      TransMeta transMeta, Trans trans ) {
    super( stepMeta, stepDataInterface, copyNr, transMeta, trans );
  }

}
