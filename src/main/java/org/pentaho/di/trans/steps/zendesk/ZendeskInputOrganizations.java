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

import java.util.Map;

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
import org.zendesk.client.v2.model.Organization;

public class ZendeskInputOrganizations extends ZendeskInput {

  ZendeskInputOrganizationsMeta meta;
  ZendeskInputOrganizationsData data;

  @Override
  public boolean init( StepMetaInterface smi, StepDataInterface sdi ) {
    if ( !super.init( smi, sdi ) ) {
      return false;
    }
    meta = (ZendeskInputOrganizationsMeta) smi;
    data = (ZendeskInputOrganizationsData) sdi;
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

      if ( meta.getOrganizationStepMeta() != null ) {
        data.organizationRowMeta = new RowMeta();
        meta.getFields( data.organizationRowMeta, getStepname(), null, meta.getOrganizationStepMeta(), this, repository, metaStore );
        data.organizationOutputRowSet = findOutputRowSet( meta.getOrganizationStepMeta().getName() );
        data.organizationIdIndex = data.organizationRowMeta.indexOfValue( environmentSubstitute( meta.getOrganizationIdFieldname() ) );
        data.urlIndex = data.organizationRowMeta.indexOfValue( environmentSubstitute( meta.getUrlFieldname() ) );
        data.externalIdIndex = data.organizationRowMeta.indexOfValue( environmentSubstitute( meta.getExternalIdFieldname() ) );
        data.nameIndex = data.organizationRowMeta.indexOfValue( environmentSubstitute( meta.getNameFieldname() ) );
        data.createdAtIndex = data.organizationRowMeta.indexOfValue( environmentSubstitute( meta.getCreatedAtFieldname() ) );
        data.updatedAtIndex = data.organizationRowMeta.indexOfValue( environmentSubstitute( meta.getUpdatedAtFieldname() ) );
        data.detailsIndex = data.organizationRowMeta.indexOfValue( environmentSubstitute( meta.getDetailsFieldname() ) );
        data.notesIndex = data.organizationRowMeta.indexOfValue( environmentSubstitute( meta.getNotesFieldname() ) );
        data.groupIdIndex = data.organizationRowMeta.indexOfValue( environmentSubstitute( meta.getGroupIdFieldname() ) );
        data.sharedTicketsIndex = data.organizationRowMeta.indexOfValue( environmentSubstitute( meta.getSharedTicketsFieldname() ) );
        data.sharedCommentsIndex = data.organizationRowMeta.indexOfValue( environmentSubstitute( meta.getSharedCommentsFieldname() ) );
      }

      if ( meta.getOrganizationTagStepMeta() != null ) {
        data.organizationTagRowMeta = new RowMeta();
        meta.getFields( data.organizationTagRowMeta, getStepname(), null, meta.getOrganizationTagStepMeta(), this, repository, metaStore );
        data.organizationTagOutputRowSet = findOutputRowSet( meta.getOrganizationTagStepMeta().getName() );
        data.tagOrganizationIdIndex = data.organizationTagRowMeta.indexOfValue( environmentSubstitute( meta.getOrganizationIdFieldname() ) );
        data.tagValueIndex = data.organizationTagRowMeta.indexOfValue( environmentSubstitute( meta.getTagFieldname() ) );
      }

      if ( meta.getOrganizationFieldStepMeta() != null ) {
        data.organizationFieldRowMeta = new RowMeta();
        meta.getFields( data.organizationFieldRowMeta, getStepname(), null, meta.getOrganizationFieldStepMeta(), this, repository, metaStore );
        data.organizationFieldOutputRowSet = findOutputRowSet( meta.getOrganizationFieldStepMeta().getName() );
        data.fieldOrganizationIdIndex = data.organizationFieldRowMeta.indexOfValue( environmentSubstitute( meta.getOrganizationIdFieldname() ) );
        data.fieldNameIndex = data.organizationFieldRowMeta.indexOfValue( environmentSubstitute( meta.getOrgFieldNameFieldname() ) );
        data.fieldValueIndex = data.organizationFieldRowMeta.indexOfValue( environmentSubstitute( meta.getOrgFieldValueFieldname() ) );
      }

      if ( meta.getOrganizationDomainStepMeta() != null ) {
        data.organizationDomainRowMeta = new RowMeta();
        meta.getFields( data.organizationDomainRowMeta, getStepname(), null, meta.getOrganizationDomainStepMeta(), this, repository, metaStore );
        data.organizationDomainOutputRowSet = findOutputRowSet( meta.getOrganizationDomainStepMeta().getName() );
        data.domainOrganizationIdIndex = data.organizationDomainRowMeta.indexOfValue( environmentSubstitute( meta.getOrganizationIdFieldname() ) );
        data.domainNameIndex = data.organizationDomainRowMeta.indexOfValue( environmentSubstitute( meta.getDomainNameFieldname() ) );
      }
    }

    if ( !data.isReceivingInput ) {
      Iterable<Organization> organizations = null;
      try {
        organizations = data.conn.getOrganizations();
      } catch ( ZendeskResponseException zre ) {
        logError( BaseMessages.getString( PKG, "ZendeskInput.Error.Generic", zre ) );
        setErrors( 1L );
        setOutputDone();
        return false;
      }

      int i = 0;
      for ( Organization organization : organizations ) {
        if ( organization != null ) {
          i++;
          outputOrganizationRow( organization );
          outputOrganizationTagRow( organization );
          outputOrganizationFieldRow( organization );
          outputOrganizationDomainRow( organization );
          incrementLinesOutput();
        }
      }
      logBasic( "Total Organizations: " + i );
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

      Long orgId = getInputRowMeta().getValueMeta( data.incomingIndex ).getInteger( row[data.incomingIndex] );
      Organization organization = null;
      try {
        organization = data.conn.getOrganization( orgId );
      } catch ( ZendeskResponseException zre ) {
        logError( BaseMessages.getString( PKG, "ZendeskInput.Error.Generic", zre ) );
        setErrors( 1L );
        setOutputDone();
        return false;
      }
      if ( organization != null ) {
        outputOrganizationRow( organization );
        outputOrganizationTagRow( organization );
        outputOrganizationFieldRow( organization );
        outputOrganizationDomainRow( organization );
        incrementLinesOutput();
      }
      return true;
    }
  }

  public ZendeskInputOrganizations( StepMeta stepMeta, StepDataInterface stepDataInterface, int copyNr,
      TransMeta transMeta, Trans trans ) {
    super( stepMeta, stepDataInterface, copyNr, transMeta, trans );
  }

  private void outputOrganizationRow( Organization org ) throws KettleStepException {
    if ( data.organizationRowMeta == null || data.organizationRowMeta.isEmpty() ) {
      return;
    }

    Object[] outputRow = RowDataUtil.allocateRowData( data.organizationRowMeta.size() );
    if ( data.organizationIdIndex >= 0 ) {
      outputRow[data.organizationIdIndex] = org.getId();
    }
    if ( data.urlIndex >= 0 ) {
      // TODO
      outputRow[data.urlIndex] = "";
    }
    if ( data.externalIdIndex >= 0 ) {
      outputRow[data.externalIdIndex] = org.getExternalId();
    }
    if ( data.nameIndex >= 0 ) {
      outputRow[data.nameIndex] = org.getName();
    }
    if ( data.createdAtIndex >= 0 ) {
      outputRow[data.createdAtIndex] = org.getCreatedAt();
    }
    if ( data.updatedAtIndex >= 0 ) {
      outputRow[data.updatedAtIndex] = org.getUpdatedAt();
    }
    if ( data.detailsIndex >= 0 ) {
      outputRow[data.detailsIndex] = org.getDetails();
    }
    if ( data.notesIndex >= 0 ) {
      outputRow[data.notesIndex] = org.getNotes();
    }
    if ( data.groupIdIndex >= 0 ) {
      outputRow[data.groupIdIndex] = org.getGroupId();
    }
    if ( data.sharedTicketsIndex >= 0 ) {
      outputRow[data.sharedTicketsIndex] = org.getSharedTickets();
    }
    if ( data.sharedCommentsIndex >= 0 ) {
      outputRow[data.sharedCommentsIndex] = org.getSharedComments();
    }
    putRowTo( data.organizationRowMeta, outputRow, data.organizationOutputRowSet );
  }

  private void outputOrganizationTagRow( Organization org ) throws KettleStepException {
    if ( data.organizationTagRowMeta == null || data.organizationTagRowMeta.isEmpty() ) {
      return;
    }

    for ( String tag : org.getTags() ) {
      Object[] outputRow = RowDataUtil.allocateRowData( data.organizationTagRowMeta.size() );

      if ( data.tagOrganizationIdIndex >= 0 ) {
        outputRow[data.tagOrganizationIdIndex] = org.getId();
      }
      if ( data.tagValueIndex >= 0 ) {
        outputRow[data.tagValueIndex] = tag;
      }
      putRowTo( data.organizationTagRowMeta, outputRow, data.organizationTagOutputRowSet );
    }
  }

  private void outputOrganizationFieldRow( Organization org ) throws KettleStepException {
    if ( data.organizationFieldRowMeta == null || data.organizationFieldRowMeta.isEmpty() ) {
      return;
    }

    for ( Map.Entry<String, Object> field : org.getOrganizationFields().entrySet() ) {
      Object[] outputRow = RowDataUtil.allocateRowData( data.organizationTagRowMeta.size() );

      if ( data.fieldOrganizationIdIndex >= 0 ) {
        outputRow[data.fieldOrganizationIdIndex] = org.getId();
      }
      if ( data.fieldNameIndex >= 0 ) {
        outputRow[data.fieldNameIndex] = field.getKey();
      }
      if ( data.fieldValueIndex >= 0 ) {
        outputRow[data.fieldValueIndex] = String.valueOf( field.getValue() );
      }
      putRowTo( data.organizationFieldRowMeta, outputRow, data.organizationFieldOutputRowSet );
    }
  }

  private void outputOrganizationDomainRow( Organization org ) throws KettleStepException {
    if ( data.organizationDomainRowMeta == null || data.organizationDomainRowMeta.isEmpty() ) {
      return;
    }

    for ( String domain : org.getDomainNames() ) {
      if ( !Const.isEmpty( domain ) ) {
        Object[] outputRow = RowDataUtil.allocateRowData( data.organizationDomainRowMeta.size() );

        if ( data.domainOrganizationIdIndex >= 0 ) {
          outputRow[data.domainOrganizationIdIndex] = org.getId();
        }
        if ( data.domainNameIndex >= 0 ) {
          outputRow[data.domainNameIndex] = domain;
        }
        putRowTo( data.organizationDomainRowMeta, outputRow, data.organizationDomainOutputRowSet );
      }
    }
  }
}
