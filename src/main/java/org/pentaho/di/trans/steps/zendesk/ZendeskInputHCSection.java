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
import org.zendesk.client.v2.model.hc.Section;

public class ZendeskInputHCSection extends ZendeskInput {

  ZendeskInputHCSectionMeta meta;
  ZendeskInputHCSectionData data;

  @Override
  public boolean init( StepMetaInterface smi, StepDataInterface sdi ) {
    if ( !super.init( smi, sdi ) ) {
      return false;
    }
    meta = (ZendeskInputHCSectionMeta) smi;
    data = (ZendeskInputHCSectionData) sdi;
    return true;
  }

  @Override
  public boolean processRow( StepMetaInterface smi, StepDataInterface sdi ) throws KettleException {
    if ( first ) {
      data.rowMeta = new RowMeta();
      meta.getFields( data.rowMeta, getStepname(), null, null, this, repository, metaStore );
      data.sectionIdIndex = data.rowMeta.indexOfValue( environmentSubstitute( meta.getSectionIdFieldname() ) );
      data.sectionUrlIndex = data.rowMeta.indexOfValue( environmentSubstitute( meta.getSectionUrlFieldname() ) );
      data.sectionNameIndex = data.rowMeta.indexOfValue( environmentSubstitute( meta.getSectionNameFieldname() ) );
      data.categoryIdIndex = data.rowMeta.indexOfValue( environmentSubstitute( meta.getCategoryIdFieldname() ) );
      data.localeIndex = data.rowMeta.indexOfValue( environmentSubstitute( meta.getLocaleFieldname() ) );
      data.sourceLocaleIndex = data.rowMeta.indexOfValue( environmentSubstitute( meta.getSourceLocaleFieldname() ) );
      data.htmlURL = data.rowMeta.indexOfValue( environmentSubstitute( meta.getSectionHtmlUrlFieldname() ) );
      data.outdatedIndex = data.rowMeta.indexOfValue( environmentSubstitute( meta.getOutdatedFieldname() ) );
      data.positionIndex = data.rowMeta.indexOfValue( environmentSubstitute( meta.getPositionFieldname() ) );
      data.createdAtIndex = data.rowMeta.indexOfValue( environmentSubstitute( meta.getCreatedAtFieldname() ) );
      data.updatedAtIndex = data.rowMeta.indexOfValue( environmentSubstitute( meta.getUpdatedAtFieldname() ) );
    }
    Iterable<Section> sections = null;
    try {
      sections = data.conn.getSections();
    } catch ( ZendeskResponseException zre ) {
      logError( BaseMessages.getString( PKG, "ZendeskInput.Error.Generic", zre ) );
      setErrors( 1L );
      setOutputDone();
      return false;
    }

    for ( Section section : sections ) {
      Object[] outputRow = RowDataUtil.allocateRowData( data.rowMeta.size() );
      if ( data.sectionIdIndex >= 0 ) {
        outputRow[data.sectionIdIndex] = section.getId();
      }
      if ( data.sectionUrlIndex >= 0 ) {
        outputRow[data.sectionUrlIndex] = section.getUrl();
      }
      if ( data.sectionNameIndex >= 0 ) {
        outputRow[data.sectionNameIndex] = section.getName();
      }
      if ( data.categoryIdIndex >= 0 ) {
        outputRow[data.categoryIdIndex] = Long.valueOf( section.getCategoryId() );
      }
      if ( data.localeIndex >= 0 ) {
        outputRow[data.localeIndex] = section.getLocale();
      }
      if ( data.sourceLocaleIndex >= 0 ) {
        outputRow[data.sourceLocaleIndex] = section.getSourceLocale();
      }
      if ( data.htmlURL >= 0 ) {
        outputRow[data.htmlURL] = section.getHtmlUrl();
      }
      if ( data.outdatedIndex >= 0 ) {
        outputRow[data.outdatedIndex] = section.getOutdated();
      }
      if ( data.positionIndex >= 0 ) {
        outputRow[data.positionIndex] = section.getPosition();
      }
      if ( data.createdAtIndex >= 0 ) {
        outputRow[data.createdAtIndex] = section.getCreatedAt();
      }
      if ( data.updatedAtIndex >= 0 ) {
        outputRow[data.updatedAtIndex] = section.getUpdatedAt();
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

  public ZendeskInputHCSection( StepMeta stepMeta, StepDataInterface stepDataInterface, int copyNr,
      TransMeta transMeta, Trans trans ) {
    super( stepMeta, stepDataInterface, copyNr, transMeta, trans );
  }
}
