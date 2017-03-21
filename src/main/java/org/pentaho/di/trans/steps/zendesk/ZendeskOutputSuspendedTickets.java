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

public class ZendeskOutputSuspendedTickets extends ZendeskInput {

  ZendeskOutputSuspendedTicketsMeta meta;
  ZendeskOutputSuspendedTicketsData data;

  @Override
  public boolean init( StepMetaInterface smi, StepDataInterface sdi ) {
    if ( !super.init( smi, sdi ) ) {
      return false;
    }
    meta = (ZendeskOutputSuspendedTicketsMeta) smi;
    data = (ZendeskOutputSuspendedTicketsData) sdi;
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
      data.ticketFieldNameIndex = getInputRowMeta().indexOfValue( meta.getTicketFieldName() );
      first = false;
      data.outputRowMeta = getInputRowMeta().clone();
      meta.getFields( data.outputRowMeta, getStepname(), null, null, this, repository, metaStore );
      data.resultFieldNameIndex = data.outputRowMeta.indexOfValue( meta.getResultFieldName() );
    }

    Object[] outputRow = RowDataUtil.resizeArray( r, data.outputRowMeta.size() );

    Long ticketId = (Long) r[data.ticketFieldNameIndex];
    Boolean result = false;

    switch ( meta.getAction() ) {
      case DELETE:
        try {
          data.conn.deleteSuspendedTicket( ticketId );
          result = true;
        } catch ( ZendeskResponseException zre ) {
          // If Suspended Ticket ID not found, catch the error, and mark Result as false
          // Otherwise, if different error, continue throwing the error
          if ( 404 != zre.getStatusCode() ) {
            throw zre;
          }
        }
        break;
    }

    if ( data.resultFieldNameIndex >= 0 ) {
      outputRow[data.resultFieldNameIndex] = result;
    }
    putRow( data.outputRowMeta, outputRow );
    return true;
  }

  public ZendeskOutputSuspendedTickets( StepMeta stepMeta, StepDataInterface stepDataInterface, int copyNr,
      TransMeta transMeta, Trans trans ) {
    super( stepMeta, stepDataInterface, copyNr, transMeta, trans );
  }

}
