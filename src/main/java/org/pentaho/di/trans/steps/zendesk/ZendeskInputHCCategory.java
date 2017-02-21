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
import org.zendesk.client.v2.model.hc.Category;

public class ZendeskInputHCCategory extends ZendeskInput {

  ZendeskInputHCCategoryMeta meta;
  ZendeskInputHCCategoryData data;

  @Override
  public boolean init( StepMetaInterface smi, StepDataInterface sdi ) {
    if ( !super.init( smi, sdi ) ) {
      return false;
    }
    meta = (ZendeskInputHCCategoryMeta) smi;
    data = (ZendeskInputHCCategoryData) sdi;
    return true;
  }

  @Override
  public boolean processRow( StepMetaInterface smi, StepDataInterface sdi ) throws KettleException {
    if ( first ) {
      data.rowMeta = new RowMeta();
      meta.getFields( data.rowMeta, getStepname(), null, null, this, repository, metaStore );
      data.categoryIdIndex = data.rowMeta.indexOfValue( environmentSubstitute( meta.getCategoryIdFieldname() ) );
      data.categoryUrlIndex = data.rowMeta.indexOfValue( environmentSubstitute( meta.getCategoryUrlFieldname() ) );
      data.categoryNameIndex = data.rowMeta.indexOfValue( environmentSubstitute( meta.getCategoryNameFieldname() ) );
      data.descriptionIndex = data.rowMeta.indexOfValue( environmentSubstitute( meta.getDescriptionFieldname() ) );
      data.localeIndex = data.rowMeta.indexOfValue( environmentSubstitute( meta.getLocaleFieldname() ) );
      data.sourceLocaleIndex = data.rowMeta.indexOfValue( environmentSubstitute( meta.getSourceLocaleFieldname() ) );
      data.htmlURL = data.rowMeta.indexOfValue( environmentSubstitute( meta.getCategoryHtmlUrlFieldname() ) );
      data.outdatedIndex = data.rowMeta.indexOfValue( environmentSubstitute( meta.getOutdatedFieldname() ) );
      data.positionIndex = data.rowMeta.indexOfValue( environmentSubstitute( meta.getPositionFieldname() ) );
      data.createdAtIndex = data.rowMeta.indexOfValue( environmentSubstitute( meta.getCreatedAtFieldname() ) );
      data.updatedAtIndex = data.rowMeta.indexOfValue( environmentSubstitute( meta.getUpdatedAtFieldname() ) );
    }
    Iterable<Category> categories = null;
    try {
      categories = data.conn.getCategories();
    } catch ( ZendeskResponseException zre ) {
      logError( BaseMessages.getString( PKG, "ZendeskInput.Error.Generic", zre ) );
      setErrors( 1L );
      setOutputDone();
      return false;
    }

    for ( Category cat : categories ) {
      Object[] outputRow = RowDataUtil.allocateRowData( data.rowMeta.size() );
      if ( data.categoryIdIndex >= 0 ) {
        outputRow[data.categoryIdIndex] = cat.getId();
      }
      if ( data.categoryUrlIndex >= 0 ) {
        outputRow[data.categoryUrlIndex] = cat.getUrl();
      }
      if ( data.categoryNameIndex >= 0 ) {
        outputRow[data.categoryNameIndex] = cat.getName();
      }
      if ( data.descriptionIndex >= 0 ) {
        outputRow[data.descriptionIndex] = cat.getDescription();
      }
      if ( data.localeIndex >= 0 ) {
        outputRow[data.localeIndex] = cat.getLocale();
      }
      if ( data.sourceLocaleIndex >= 0 ) {
        outputRow[data.sourceLocaleIndex] = cat.getSourceLocale();
      }
      if ( data.htmlURL >= 0 ) {
        outputRow[data.htmlURL] = cat.getHtmlUrl();
      }
      if ( data.outdatedIndex >= 0 ) {
        outputRow[data.outdatedIndex] = cat.getOutdated();
      }
      if ( data.positionIndex >= 0 ) {
        outputRow[data.positionIndex] = cat.getPosition();
      }
      if ( data.createdAtIndex >= 0 ) {
        outputRow[data.createdAtIndex] = cat.getCreatedAt();
      }
      if ( data.updatedAtIndex >= 0 ) {
        outputRow[data.updatedAtIndex] = cat.getUpdatedAt();
      }
      putRow( data.rowMeta, outputRow );
      incrementLinesOutput();
    }

    setOutputDone();
    return false;
  }

  public ZendeskInputHCCategory( StepMeta stepMeta, StepDataInterface stepDataInterface, int copyNr,
      TransMeta transMeta, Trans trans ) {
    super( stepMeta, stepDataInterface, copyNr, transMeta, trans );
  }
}
