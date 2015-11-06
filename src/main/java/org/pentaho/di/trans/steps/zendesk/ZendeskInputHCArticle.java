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

import org.apache.commons.lang.StringUtils;
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

public class ZendeskInputHCArticle extends ZendeskInput {

  ZendeskInputHCArticleMeta meta;
  ZendeskInputHCArticleData data;

  @Override
  public boolean init( StepMetaInterface smi, StepDataInterface sdi ) {
    if ( !super.init( smi, sdi ) ) {
      return false;
    }
    meta = (ZendeskInputHCArticleMeta) smi;
    data = (ZendeskInputHCArticleData) sdi;
    return true;
  }

  @Override
  public boolean processRow( StepMetaInterface smi, StepDataInterface sdi ) throws KettleException {
    if ( first ) {
      data.rowMeta = new RowMeta();
      meta.getFields( data.rowMeta, getStepname(), null, null, this, repository, metaStore );
      data.articleIdIndex = data.rowMeta.indexOfValue( environmentSubstitute( meta.getArticleIdFieldname() ) );
      data.articleUrlIndex = data.rowMeta.indexOfValue( environmentSubstitute( meta.getArticleUrlFieldname() ) );
      data.articleTitleIndex = data.rowMeta.indexOfValue( environmentSubstitute( meta.getArticleTitleFieldname() ) );
      data.articleBodyIndex = data.rowMeta.indexOfValue( environmentSubstitute( meta.getArticleBodyFieldname() ) );
      data.localeIndex = data.rowMeta.indexOfValue( environmentSubstitute( meta.getLocaleFieldname() ) );
      data.sourceLocaleIndex = data.rowMeta.indexOfValue( environmentSubstitute( meta.getSourceLocaleFieldname() ) );
      data.authorIdIndex = data.rowMeta.indexOfValue( environmentSubstitute( meta.getAuthorIdFieldname() ) );
      data.commentsDisabledIndex = data.rowMeta.indexOfValue( environmentSubstitute( meta.getCommentsDisabledFieldname() ) );
      data.outdatedIndex = data.rowMeta.indexOfValue( environmentSubstitute( meta.getOutdatedFieldname() ) );
      data.labelsIndex = data.rowMeta.indexOfValue( environmentSubstitute( meta.getLabelsFieldname() ) );
      data.draftIndex = data.rowMeta.indexOfValue( environmentSubstitute( meta.getDraftFieldname() ) );
      data.promotedIndex = data.rowMeta.indexOfValue( environmentSubstitute( meta.getPromotedFieldname() ) );
      data.positionIndex = data.rowMeta.indexOfValue( environmentSubstitute( meta.getPositionFieldname() ) );
      data.voteSumIndex = data.rowMeta.indexOfValue( environmentSubstitute( meta.getVoteSumFieldname() ) );
      data.voteCountIndex = data.rowMeta.indexOfValue( environmentSubstitute( meta.getVoteCountFieldname() ) );
      data.sectionIdIndex = data.rowMeta.indexOfValue( environmentSubstitute( meta.getSectionIdFieldname() ) );
      data.createdAtIndex = data.rowMeta.indexOfValue( environmentSubstitute( meta.getCreatedAtFieldname() ) );
      data.updatedAtIndex = data.rowMeta.indexOfValue( environmentSubstitute( meta.getUpdatedAtFieldname() ) );
    }
    Iterable<Article> articles = null;
    try {
      articles = data.conn.getArticlesFromPage( 1 );
    } catch ( ZendeskResponseException zre ) {
      logError( BaseMessages.getString( PKG, "ZendeskInput.Error.Generic", zre ) );
      setErrors( 1L );
      setOutputDone();
      return false;
    }

    for (Article article : articles ) {
      Object[] outputRow = RowDataUtil.allocateRowData( data.rowMeta.size() );
      if ( data.articleIdIndex >= 0 ) {
        outputRow[data.articleIdIndex] = article.getId();
      }
      if ( data.articleUrlIndex >= 0 ) {
        outputRow[data.articleUrlIndex] = article.getUrl();
      }
      if ( data.articleTitleIndex >= 0 ) {
        outputRow[data.articleTitleIndex] = article.getTitle();
      }
      if ( data.articleBodyIndex >= 0 ) {
        outputRow[data.articleBodyIndex] = article.getBody();
      }
      if ( data.localeIndex >= 0 ) {
        outputRow[data.localeIndex] = article.getLocale();
      }
      if ( data.sourceLocaleIndex >= 0 ) {
        outputRow[data.sourceLocaleIndex] = article.getSourceLocale();
      }
      if ( data.authorIdIndex >= 0 ) {
        outputRow[data.authorIdIndex] = article.getAuthorId();
      }
      if ( data.commentsDisabledIndex >= 0 ) {
        outputRow[data.commentsDisabledIndex] = article.getCommentsDisabled();
      }
      if ( data.outdatedIndex >= 0 ) {
        outputRow[data.outdatedIndex] = article.getOutdated();
      }
      if ( data.labelsIndex >= 0 ) {
        outputRow[data.labelsIndex] = StringUtils.join( article.getLabelNames(), "," );
      }
      if ( data.draftIndex >= 0 ) {
        outputRow[data.draftIndex] = article.getDraft();
      }
      if ( data.promotedIndex >= 0 ) {
        outputRow[data.promotedIndex] = article.getPromoted();
      }
      if ( data.positionIndex >= 0 ) {
        outputRow[data.positionIndex] = article.getPosition();
      }
      if ( data.voteSumIndex >= 0 ) {
        outputRow[data.voteSumIndex] = article.getVoteSum();
      }
      if ( data.voteCountIndex >= 0 ) {
        outputRow[data.voteCountIndex] = article.getVoteCount();
      }
      if ( data.sectionIdIndex >= 0 ) {
        outputRow[data.sectionIdIndex] = article.getSectionId();
      }
      if ( data.createdAtIndex >= 0 ) {
        outputRow[data.createdAtIndex] = article.getCreatedAt();
      }
      if ( data.updatedAtIndex >= 0 ) {
        outputRow[data.updatedAtIndex] = article.getUpdatedAt();
      }
      putRow( data.rowMeta, outputRow );
      incrementLinesOutput();
    }

    setOutputDone();
    return false;
  }

  public ZendeskInputHCArticle( StepMeta stepMeta, StepDataInterface stepDataInterface, int copyNr,
      TransMeta transMeta, Trans trans ) {
    super( stepMeta, stepDataInterface, copyNr, transMeta, trans );
  }
}
