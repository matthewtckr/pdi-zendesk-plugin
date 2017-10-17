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
import org.pentaho.di.core.row.RowMeta;
import org.pentaho.di.i18n.BaseMessages;
import org.pentaho.di.trans.Trans;
import org.pentaho.di.trans.TransMeta;
import org.pentaho.di.trans.step.StepDataInterface;
import org.pentaho.di.trans.step.StepMeta;
import org.pentaho.di.trans.step.StepMetaInterface;
import org.zendesk.client.v2.ZendeskResponseException;
import org.zendesk.client.v2.model.SuspendedTicket;

public class ZendeskInputSuspendedTickets extends ZendeskInput {

  ZendeskInputSuspendedTicketsMeta meta;
  ZendeskInputSuspendedTicketsData data;

  @Override
  public boolean init( StepMetaInterface smi, StepDataInterface sdi ) {
    if ( !super.init( smi, sdi ) ) {
      return false;
    }
    meta = (ZendeskInputSuspendedTicketsMeta) smi;
    data = (ZendeskInputSuspendedTicketsData) sdi;
    return true;
  }

  @Override
  public boolean processRow( StepMetaInterface smi, StepDataInterface sdi ) throws KettleException {
    if ( first ) {
      data.rowMeta = new RowMeta();
      meta.getFields( data.rowMeta, getStepname(), null, null, this, repository, metaStore );
      data.suspendedTicketIdIndex = data.rowMeta.indexOfValue( environmentSubstitute( meta.getSuspendedTicketIdFieldname() ) );
      data.suspendedTicketUrlIndex = data.rowMeta.indexOfValue( environmentSubstitute( meta.getSuspendedTicketUrlFieldname() ) );
      data.authorIndex = data.rowMeta.indexOfValue( environmentSubstitute( meta.getAuthorFieldname() ) );
      data.subjectIndex = data.rowMeta.indexOfValue( environmentSubstitute( meta.getSubjectFieldname() ) );
      data.contentIndex = data.rowMeta.indexOfValue( environmentSubstitute( meta.getContentFieldname() ) );
      data.causeIndex = data.rowMeta.indexOfValue( environmentSubstitute( meta.getCauseFieldname() ) );
      data.messageIdIndex = data.rowMeta.indexOfValue( environmentSubstitute( meta.getMessageIdFieldname() ) );
      data.ticketIdIndex = data.rowMeta.indexOfValue( environmentSubstitute( meta.getTicketIdFieldname() ) );
      data.recipientIndex = data.rowMeta.indexOfValue( environmentSubstitute( meta.getRecipientFieldname() ) );
      data.createdAtIndex = data.rowMeta.indexOfValue( environmentSubstitute( meta.getCreatedAtFieldname() ) );
      data.updatedAtIndex = data.rowMeta.indexOfValue( environmentSubstitute( meta.getUpdatedAtFieldname() ) );
      data.viaIndex = data.rowMeta.indexOfValue( environmentSubstitute( meta.getViaFieldname() ) );
      data.brandIdIndex = data.rowMeta.indexOfValue( environmentSubstitute( meta.getBrandIdFieldname() ) );
    }
    Iterable<SuspendedTicket> tickets = null;
    try {
      tickets = data.conn.getSuspendedTickets();
    } catch ( ZendeskResponseException zre ) {
      logError( BaseMessages.getString( PKG, "ZendeskInput.Error.Generic", zre ) );
      setErrors( 1L );
      setOutputDone();
      return false;
    }

    for ( SuspendedTicket ticket : tickets ) {
      Object[] outputRow = RowDataUtil.allocateRowData( data.rowMeta.size() );
      if ( data.suspendedTicketIdIndex >= 0 ) {
        outputRow[data.suspendedTicketIdIndex] = ticket.getId();
      }
      if ( data.suspendedTicketUrlIndex >= 0 ) {
        outputRow[data.suspendedTicketUrlIndex] = ticket.getUrl();
      }
      if ( data.authorIndex >= 0 ) {
        outputRow[data.authorIndex] = ticket.getAuthor();
      }
      if ( data.subjectIndex >= 0 ) {
        outputRow[data.subjectIndex] = ticket.getSubject();
      }
      if ( data.contentIndex >= 0 ) {
        outputRow[data.contentIndex] = ticket.getContent();
      }
      if ( data.causeIndex >= 0 ) {
        outputRow[data.causeIndex] = ticket.getCause();
      }
      if ( data.messageIdIndex >= 0 ) {
        outputRow[data.messageIdIndex] = ticket.getMessageId();
      }
      if ( data.ticketIdIndex >= 0 ) {
        outputRow[data.ticketIdIndex] = ticket.getTicketId();
      }
      if ( data.recipientIndex >= 0 ) {
        outputRow[data.recipientIndex] = ticket.getRecipient();
      }
      if ( data.createdAtIndex >= 0 ) {
        outputRow[data.createdAtIndex] = ticket.getCreatedAt();
      }
      if ( data.updatedAtIndex >= 0 ) {
        outputRow[data.updatedAtIndex] = ticket.getUpdatedAt();
      }
      if ( data.viaIndex >= 0 ) {
        outputRow[data.viaIndex] = ticket.getVia().getChannel();
      }
      if ( data.brandIdIndex >= 0 ) {
        outputRow[data.brandIdIndex] = ticket.getBrandId();
      }
      putRow( data.rowMeta, outputRow );
      incrementLinesOutput();
      if ( isStopped() ) {
        break;
      }
    }

    setOutputDone();
    return false;
  }

  public ZendeskInputSuspendedTickets( StepMeta stepMeta, StepDataInterface stepDataInterface, int copyNr,
      TransMeta transMeta, Trans trans ) {
    super( stepMeta, stepDataInterface, copyNr, transMeta, trans );
  }
}
