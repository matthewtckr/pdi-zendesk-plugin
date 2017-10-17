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
import org.zendesk.client.v2.model.Field;

public class ZendeskInputTicketFields extends ZendeskInput {

  ZendeskInputTicketFieldsMeta meta;
  ZendeskInputTicketFieldsData data;

  @Override
  public boolean init( StepMetaInterface smi, StepDataInterface sdi ) {
    if ( !super.init( smi, sdi ) ) {
      return false;
    }
    meta = (ZendeskInputTicketFieldsMeta) smi;
    data = (ZendeskInputTicketFieldsData) sdi;
    return true;
  }

  @Override
  public boolean processRow( StepMetaInterface smi, StepDataInterface sdi ) throws KettleException {
    if ( first ) {
      data.rowMeta = new RowMeta();
      meta.getFields( data.rowMeta, getStepname(), null, null, this, repository, metaStore );
      data.ticketFieldIdIndex = data.rowMeta.indexOfValue( environmentSubstitute( meta.getTicketFieldIdFieldname() ) );
      data.ticketFieldUrlIndex = data.rowMeta.indexOfValue(
        environmentSubstitute( meta.getTicketFieldUrlFieldname() ) );
      data.ticketFieldTypeIndex = data.rowMeta.indexOfValue(
        environmentSubstitute( meta.getTicketFieldTypeFieldname() ) );
      data.ticketFieldTitleIndex = data.rowMeta.indexOfValue(
        environmentSubstitute( meta.getTicketFieldTitleFieldname() ) );
      data.ticketFieldActiveIndex = data.rowMeta.indexOfValue(
        environmentSubstitute( meta.getTicketFieldActiveFieldname() ) );
      data.ticketFieldRequiredIndex = data.rowMeta.indexOfValue(
        environmentSubstitute( meta.getTicketFieldRequiredFieldname() ) );
      data.ticketFieldVisibleEndUsersIndex = data.rowMeta.indexOfValue(
        environmentSubstitute( meta.getTicketFieldVisibleEndUsersFieldname() ) );
      data.createdAtIndex = data.rowMeta.indexOfValue( environmentSubstitute( meta.getCreatedAtFieldname() ) );
      data.updatedAtIndex = data.rowMeta.indexOfValue( environmentSubstitute( meta.getUpdatedAtFieldname() ) );
    }
    Iterable<Field> fields = null;
    try {
      fields = data.conn.getTicketFields();
    } catch ( ZendeskResponseException zre ) {
      logError( BaseMessages.getString( PKG, "ZendeskInput.Error.Generic", zre ) );
      setErrors( 1L );
      setOutputDone();
      return false;
    }

    for ( Field field : fields ) {
      Object[] outputRow = RowDataUtil.allocateRowData( data.rowMeta.size() );
      if ( data.ticketFieldIdIndex >= 0 ) {
        outputRow[data.ticketFieldIdIndex] = field.getId();
      }
      if ( data.ticketFieldUrlIndex >= 0 ) {
        outputRow[data.ticketFieldUrlIndex] = field.getUrl();
      }
      if ( data.ticketFieldTypeIndex >= 0 ) {
        outputRow[data.ticketFieldTypeIndex] = field.getType();
      }
      if ( data.ticketFieldTitleIndex >= 0 ) {
        outputRow[data.ticketFieldTitleIndex] = field.getTitle();
      }
      if ( data.ticketFieldActiveIndex >= 0 ) {
        outputRow[data.ticketFieldActiveIndex] = field.getActive();
      }
      if ( data.ticketFieldRequiredIndex >= 0 ) {
        outputRow[data.ticketFieldRequiredIndex] = field.getRequired();
      }
      if ( data.ticketFieldVisibleEndUsersIndex >= 0 ) {
        outputRow[data.ticketFieldVisibleEndUsersIndex] = field.getVisibleInPortal();
      }
      if ( data.createdAtIndex >= 0 ) {
        outputRow[data.createdAtIndex] = field.getCreatedAt();
      }
      if ( data.updatedAtIndex >= 0 ) {
        outputRow[data.updatedAtIndex] = field.getUpdatedAt();
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

  public ZendeskInputTicketFields( StepMeta stepMeta, StepDataInterface stepDataInterface, int copyNr,
      TransMeta transMeta, Trans trans ) {
    super( stepMeta, stepDataInterface, copyNr, transMeta, trans );
  }
}
