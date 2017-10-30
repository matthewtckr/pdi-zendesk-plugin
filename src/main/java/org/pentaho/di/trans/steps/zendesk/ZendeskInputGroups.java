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
import org.zendesk.client.v2.model.Group;

public class ZendeskInputGroups extends ZendeskInput {

  ZendeskInputGroupsMeta meta;
  ZendeskInputGroupsData data;

  @Override
  public boolean init( StepMetaInterface smi, StepDataInterface sdi ) {
    if ( !super.init( smi, sdi ) ) {
      return false;
    }
    meta = (ZendeskInputGroupsMeta) smi;
    data = (ZendeskInputGroupsData) sdi;
    return true;
  }

  @Override
  public boolean processRow( StepMetaInterface smi, StepDataInterface sdi ) throws KettleException {
    if ( first ) {
      data.rowMeta = new RowMeta();
      meta.getFields( data.rowMeta, getStepname(), null, null, this, repository, metaStore );
      data.groupIdIndex = data.rowMeta.indexOfValue( environmentSubstitute( meta.getGroupIdFieldname() ) );
      data.groupUrlIndex = data.rowMeta.indexOfValue( environmentSubstitute( meta.getGroupUrlFieldname() ) );
      data.groupNameIndex = data.rowMeta.indexOfValue( environmentSubstitute( meta.getGroupNameFieldname() ) );
      data.deletedIndex = data.rowMeta.indexOfValue( environmentSubstitute( meta.getDeletedFieldname() ) );
      data.createdAtIndex = data.rowMeta.indexOfValue( environmentSubstitute( meta.getCreatedAtFieldname() ) );
      data.updatedAtIndex = data.rowMeta.indexOfValue( environmentSubstitute( meta.getUpdatedAtFieldname() ) );
    }
    Iterable<Group> groups = null;
    try {
      groups = data.conn.getGroups();
    } catch ( ZendeskResponseException zre ) {
      logError( BaseMessages.getString( PKG, "ZendeskInput.Error.Generic", zre ) );
      setErrors( 1L );
      setOutputDone();
      return false;
    }

    for ( Group group : groups ) {
      Object[] outputRow = RowDataUtil.allocateRowData( data.rowMeta.size() );
      if ( data.groupIdIndex >= 0 ) {
        outputRow[data.groupIdIndex] = group.getId();
      }
      if ( data.groupUrlIndex >= 0 ) {
        outputRow[data.groupUrlIndex] = group.getUrl();
      }
      if ( data.groupNameIndex >= 0 ) {
        outputRow[data.groupNameIndex] = group.getName();
      }
      if ( data.deletedIndex >= 0 ) {
        outputRow[data.deletedIndex] = group.getDeleted();
      }
      if ( data.createdAtIndex >= 0 ) {
        outputRow[data.createdAtIndex] = group.getCreatedAt();
      }
      if ( data.updatedAtIndex >= 0 ) {
        outputRow[data.updatedAtIndex] = group.getUpdatedAt();
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

  public ZendeskInputGroups( StepMeta stepMeta, StepDataInterface stepDataInterface, int copyNr,
      TransMeta transMeta, Trans trans ) {
    super( stepMeta, stepDataInterface, copyNr, transMeta, trans );
  }
}
