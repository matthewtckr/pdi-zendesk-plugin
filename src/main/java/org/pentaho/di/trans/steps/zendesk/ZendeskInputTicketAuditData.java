/*! ******************************************************************************
 *
 * Pentaho Data Integration
 *
 * Copyright (C) 2002-2015 by Pentaho : http://www.pentaho.com
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

import org.apache.commons.collections4.map.AbstractLinkedMap;
import org.apache.commons.collections4.map.LinkedMap;
import org.pentaho.di.core.RowSet;
import org.pentaho.di.core.exception.KettleValueException;
import org.pentaho.di.core.row.RowMetaInterface;
import org.zendesk.client.v2.model.Audit;

public class ZendeskInputTicketAuditData extends ZendeskInputData {

  AbstractLinkedMap<Long, ZendeskTicketAuditHistory> auditSummaries;

  RowMetaInterface ticketOverviewOutputRowMeta;
  RowMetaInterface ticketCommentsOutputRowMeta;
  RowMetaInterface ticketCustomFieldsOutputRowMeta;
  RowMetaInterface ticketTagsOutputRowMeta;
  RowMetaInterface ticketCollaboratorsOutputRowMeta;

  RowSet ticketOverviewOutputRowSet;
  RowSet ticketCommentsOutputRowSet;
  RowSet ticketCustomFieldsOutputRowSet;
  RowSet ticketTagsOutputRowSet;
  RowSet ticketCollaboratorsOutputRowSet;

  void newTicket() {
    auditSummaries = null;
  }

  void addAudit( Audit audit ) throws KettleValueException {
    if ( auditSummaries == null ) {
      auditSummaries = new LinkedMap<Long, ZendeskTicketAuditHistory>();
    }
    if ( auditSummaries.size() <= 0 ) {
      auditSummaries.put( audit.getId(), new ZendeskTicketAuditHistory( audit ) );
    } else {
      try {
        ZendeskTicketAuditHistory newAudit =
          auditSummaries.get( auditSummaries.lastKey() ).createNextAudit( audit, auditSummaries );
        auditSummaries.put( audit.getId(), newAudit );
      } catch ( CloneNotSupportedException e ) {
        e.printStackTrace();
      }
    }
  }


}
