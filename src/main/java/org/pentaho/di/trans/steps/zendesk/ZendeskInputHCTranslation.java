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

import java.util.Date;

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
import org.zendesk.client.v2.model.hc.Article;

public class ZendeskInputHCTranslation extends ZendeskInput {

  ZendeskInputHCTranslationMeta meta;
  ZendeskInputHCTranslationData data;

  @Override
  public boolean init( StepMetaInterface smi, StepDataInterface sdi ) {
    if ( !super.init( smi, sdi ) ) {
      return false;
    }
    meta = (ZendeskInputHCTranslationMeta) smi;
    data = (ZendeskInputHCTranslationData) sdi;
    return true;
  }

  @Override
  public boolean processRow( StepMetaInterface smi, StepDataInterface sdi ) throws KettleException {
    if ( first ) {
      data.rowMeta = new RowMeta();
      meta.getFields( data.rowMeta, getStepname(), null, null, this, repository, metaStore );
      data.translationIdIndex = data.rowMeta.indexOfValue( environmentSubstitute( meta.getTranslationIdFieldname() ) );
      data.translationUrlIndex = data.rowMeta.indexOfValue( environmentSubstitute( meta.getTranslationUrlFieldname() ) );
      data.translationTitleIndex = data.rowMeta.indexOfValue( environmentSubstitute( meta.getTranslationTitleFieldname() ) );
      data.translationBodyIndex = data.rowMeta.indexOfValue( environmentSubstitute( meta.getTranslationBodyFieldname() ) );
      data.localeIndex = data.rowMeta.indexOfValue( environmentSubstitute( meta.getLocaleFieldname() ) );
      data.sourceIdIndex = data.rowMeta.indexOfValue( environmentSubstitute( meta.getSourceIdFieldname() ) );
      data.sourceTypeIndex = data.rowMeta.indexOfValue( environmentSubstitute( meta.getSourceTypeFieldname() ) );
      data.outdatedIndex = data.rowMeta.indexOfValue( environmentSubstitute( meta.getOutdatedFieldname() ) );
      data.draftIndex = data.rowMeta.indexOfValue( environmentSubstitute( meta.getDraftFieldname() ) );
      data.createdAtIndex = data.rowMeta.indexOfValue( environmentSubstitute( meta.getCreatedAtFieldname() ) );
      data.createdByIndex = data.rowMeta.indexOfValue( environmentSubstitute( meta.getCreatedByFieldname() ) );
      data.updatedAtIndex = data.rowMeta.indexOfValue( environmentSubstitute( meta.getUpdatedAtFieldname() ) );
      data.updatedByIndex = data.rowMeta.indexOfValue( environmentSubstitute( meta.getUpdatedByFieldname() ) );
    }
    Iterable<Article> articles = null;
    try {
      articles = data.conn.getArticles();
    } catch ( ZendeskResponseException zre ) {
      logError( BaseMessages.getString( PKG, "ZendeskInput.Error.Generic", zre ) );
      setErrors( 1L );
      setOutputDone();
      return false;
    }

    for (Article article : articles ) {
      Object[] outputRow = RowDataUtil.allocateRowData( data.rowMeta.size() );
      if ( data.translationIdIndex >= 0 ) {
        outputRow[data.translationIdIndex] = 0L;
      }
      if ( data.translationUrlIndex >= 0 ) {
        outputRow[data.translationUrlIndex] = "";
      }
      if ( data.translationTitleIndex >= 0 ) {
        outputRow[data.translationTitleIndex] = "";
      }
      if ( data.translationBodyIndex >= 0 ) {
        outputRow[data.translationBodyIndex] = "";
      }
      if ( data.localeIndex >= 0 ) {
        outputRow[data.localeIndex] = "";
      }
      if ( data.sourceIdIndex >= 0 ) {
        outputRow[data.sourceIdIndex] = 0L;
      }
      if ( data.sourceTypeIndex >= 0 ) {
        outputRow[data.sourceTypeIndex] = "";
      }
      if ( data.outdatedIndex >= 0 ) {
        outputRow[data.outdatedIndex] = false;
      }
      if ( data.draftIndex >= 0 ) {
        outputRow[data.draftIndex] = false;
      }
      if ( data.createdAtIndex >= 0 ) {
        outputRow[data.createdAtIndex] = new Date(0L);
      }
      if ( data.createdByIndex >= 0 ) {
        outputRow[data.createdByIndex] = 0L;
      }
      if ( data.updatedAtIndex >= 0 ) {
        outputRow[data.updatedAtIndex] = new Date(0L);
      }
      if ( data.updatedByIndex >= 0 ) {
        outputRow[data.updatedByIndex] = 0L;
      }
      putRow( data.rowMeta, outputRow );
      incrementLinesOutput();
    }

    setOutputDone();
    return false;
  }

  public ZendeskInputHCTranslation( StepMeta stepMeta, StepDataInterface stepDataInterface, int copyNr,
      TransMeta transMeta, Trans trans ) {
    super( stepMeta, stepDataInterface, copyNr, transMeta, trans );
  }
}
