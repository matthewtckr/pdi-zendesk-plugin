/*! ******************************************************************************
 *
 * Pentaho Data Integration
 *
 * Copyright (C) 2002-2016 by Pentaho : http://www.pentaho.com
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
import org.zendesk.client.v2.model.hc.Article;
import org.zendesk.client.v2.model.hc.Translation;

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

      if ( meta.getArticleStepMeta() != null ) {
        data.articleRowMeta = new RowMeta();
        meta.getFields( data.articleRowMeta, getStepname(), null, meta.getArticleStepMeta(), this, repository, metaStore );
        data.articleOutputRowSet = findOutputRowSet( meta.getArticleStepMeta().getName() );
        data.articleIdIndex = data.articleRowMeta.indexOfValue( environmentSubstitute( meta.getArticleIdFieldname() ) );
        data.articleUrlIndex = data.articleRowMeta.indexOfValue( environmentSubstitute( meta.getArticleUrlFieldname() ) );
        data.articleTitleIndex = data.articleRowMeta.indexOfValue( environmentSubstitute( meta.getArticleTitleFieldname() ) );
        data.articleBodyIndex = data.articleRowMeta.indexOfValue( environmentSubstitute( meta.getArticleBodyFieldname() ) );
        data.localeIndex = data.articleRowMeta.indexOfValue( environmentSubstitute( meta.getLocaleFieldname() ) );
        data.sourceLocaleIndex = data.articleRowMeta.indexOfValue( environmentSubstitute( meta.getSourceLocaleFieldname() ) );
        data.authorIdIndex = data.articleRowMeta.indexOfValue( environmentSubstitute( meta.getAuthorIdFieldname() ) );
        data.commentsDisabledIndex = data.articleRowMeta.indexOfValue( environmentSubstitute( meta.getCommentsDisabledFieldname() ) );
        data.outdatedIndex = data.articleRowMeta.indexOfValue( environmentSubstitute( meta.getOutdatedFieldname() ) );
        data.labelsIndex = data.articleRowMeta.indexOfValue( environmentSubstitute( meta.getLabelsFieldname() ) );
        data.draftIndex = data.articleRowMeta.indexOfValue( environmentSubstitute( meta.getDraftFieldname() ) );
        data.promotedIndex = data.articleRowMeta.indexOfValue( environmentSubstitute( meta.getPromotedFieldname() ) );
        data.positionIndex = data.articleRowMeta.indexOfValue( environmentSubstitute( meta.getPositionFieldname() ) );
        data.voteSumIndex = data.articleRowMeta.indexOfValue( environmentSubstitute( meta.getVoteSumFieldname() ) );
        data.voteCountIndex = data.articleRowMeta.indexOfValue( environmentSubstitute( meta.getVoteCountFieldname() ) );
        data.sectionIdIndex = data.articleRowMeta.indexOfValue( environmentSubstitute( meta.getSectionIdFieldname() ) );
        data.createdAtIndex = data.articleRowMeta.indexOfValue( environmentSubstitute( meta.getCreatedAtFieldname() ) );
        data.updatedAtIndex = data.articleRowMeta.indexOfValue( environmentSubstitute( meta.getUpdatedAtFieldname() ) );
      }
      if ( meta.getTranslationStepMeta() != null ) {
        data.translationRowMeta = new RowMeta();
        meta.getFields( data.translationRowMeta,  getStepname(), null, meta.getTranslationStepMeta(), this, repository, metaStore );
        data.translationOutputRowSet = findOutputRowSet( meta.getTranslationStepMeta().getName() );
        data.translationArticleIdIndex = data.translationRowMeta.indexOfValue( environmentSubstitute( meta.getArticleIdFieldname() ) );
        data.translationIdIndex = data.translationRowMeta.indexOfValue( environmentSubstitute( meta.getTranslationIdFieldname() ) );
        data.translationUrlIndex = data.translationRowMeta.indexOfValue( environmentSubstitute( meta.getTranslationUrlFieldname() ) );
        data.translationHtmlUrlIndex = data.translationRowMeta.indexOfValue( environmentSubstitute( meta.getTranslationHtmlUrlFieldname() ) );
        data.translationSourceIdIndex = data.translationRowMeta.indexOfValue( environmentSubstitute( meta.getTranslationSourceIdFieldname() ) );
        data.translationSourceTypeIndex = data.translationRowMeta.indexOfValue( environmentSubstitute( meta.getTranslationSourceTypeFieldname() ) );
        data.translationLocaleIndex = data.translationRowMeta.indexOfValue( environmentSubstitute( meta.getTranslationLocaleFieldname() ) );
        data.translationTitleIndex = data.translationRowMeta.indexOfValue( environmentSubstitute( meta.getTranslationTitleFieldname() ) );
        data.translationBodyIndex = data.translationRowMeta.indexOfValue( environmentSubstitute( meta.getTranslationBodyFieldname() ) );
        data.translationOutdatedIndex = data.translationRowMeta.indexOfValue( environmentSubstitute( meta.getTranslationOutdatedFieldname() ) );
        data.translationDraftIndex = data.translationRowMeta.indexOfValue( environmentSubstitute( meta.getTranslationDraftFieldname() ) );
        data.translationCreatedAtIndex = data.translationRowMeta.indexOfValue( environmentSubstitute( meta.getTranslationCreatedAtFieldname() ) );
        data.translationUpdatedAtIndex = data.translationRowMeta.indexOfValue( environmentSubstitute( meta.getTranslationUpdatedAtFieldname() ) );
        data.translationUpdatedByIdIndex = data.translationRowMeta.indexOfValue( environmentSubstitute( meta.getTranslationUpdatedByIdFieldname() ) );
        data.translationCreatedByIdIndex = data.translationRowMeta.indexOfValue( environmentSubstitute( meta.getTranslationCreatedByIdFieldname() ) );
      }
    }

    if ( !data.isReceivingInput ) {
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
        outputArticleRow( article );
        Iterable<Translation> translations = null;      
        if ( meta.getTranslationStepMeta() != null ) {
          try {
            translations = data.conn.getArticleTranslations( article.getId() );
          } catch ( ZendeskResponseException zre ) {
            logError( BaseMessages.getString( PKG, "ZendeskInput.Error.Generic", zre ) );
            setErrors( 1L );
            setOutputDone();
            return false;
          }
          for ( Translation translation : translations ) {
            outputTranslationRow( article.getId(), translation );
          }
        }
        incrementLinesOutput();
      }

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
      Long articleId = getInputRowMeta().getValueMeta( data.incomingIndex ).getInteger( row[data.incomingIndex] );
      Article article = null;
      try {
        article = data.conn.getArticle( articleId.intValue() );
      } catch ( ZendeskResponseException zre ) {
        logError( BaseMessages.getString( PKG, "ZendeskInput.Error.Generic", zre ) );
        setErrors( 1L );
        setOutputDone();
        return false;
      }
      if ( article != null ) {
        outputArticleRow( article );
        Iterable<Translation> translations = null;      
        if ( meta.getTranslationStepMeta() != null ) {
          try {
            translations = data.conn.getArticleTranslations( article.getId() );
          } catch ( ZendeskResponseException zre ) {
            logError( BaseMessages.getString( PKG, "ZendeskInput.Error.Generic", zre ) );
            setErrors( 1L );
            setOutputDone();
            return false;
          }
          for ( Translation translation : translations ) {
            outputTranslationRow( article.getId(), translation );
          }
        }
      }
      return true;
    }
  }

  public ZendeskInputHCArticle( StepMeta stepMeta, StepDataInterface stepDataInterface, int copyNr,
      TransMeta transMeta, Trans trans ) {
    super( stepMeta, stepDataInterface, copyNr, transMeta, trans );
  }

  private void outputArticleRow( Article article ) throws KettleStepException {
    Object[] outputRow = RowDataUtil.allocateRowData( data.articleRowMeta.size() );
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
    putRowTo( data.articleRowMeta, outputRow, data.articleOutputRowSet );
  }

  private void outputTranslationRow( Long articleId, Translation translation ) throws KettleStepException {
    Object[] outputRow = RowDataUtil.allocateRowData( data.translationRowMeta.size() );
    if ( data.translationArticleIdIndex >= 0 ) {
      outputRow[data.translationArticleIdIndex] = articleId;
    }
    if ( data.translationIdIndex >= 0 ) {
      outputRow[data.translationIdIndex] = translation.getId();
    }
    if ( data.translationUrlIndex >= 0 ) {
      outputRow[data.translationUrlIndex] = translation.getUrl();
      
    }
    if ( data.translationHtmlUrlIndex >= 0 ) {
      outputRow[data.translationHtmlUrlIndex] = translation.getHtmlUrl();
    }
    if ( data.translationSourceIdIndex >= 0 ) {
      outputRow[data.translationSourceIdIndex] = translation.getSourceId();
    }
    if ( data.translationSourceTypeIndex >= 0 ) {
      outputRow[data.translationSourceTypeIndex] = translation.getSourceType();
    }
    if ( data.translationLocaleIndex >= 0 ) {
      outputRow[data.translationLocaleIndex] = translation.getLocale();
    }
    if ( data.translationTitleIndex >= 0 ) {
      outputRow[data.translationTitleIndex] = translation.getTitle();
    }
    if ( data.translationBodyIndex >= 0 ) {
      outputRow[data.translationBodyIndex] = translation.getBody();
    }
    if ( data.translationOutdatedIndex >= 0 ) {
      outputRow[data.translationOutdatedIndex] = translation.isOutdated();
    }
    if ( data.translationDraftIndex >= 0 ) {
      outputRow[data.translationDraftIndex] = translation.isDraft();
    }
    if ( data.translationCreatedAtIndex >= 0 ) {
      outputRow[data.translationCreatedAtIndex] = translation.getCreatedAt();
    }
    if ( data.translationUpdatedAtIndex >= 0 ) {
      outputRow[data.translationUpdatedAtIndex] = translation.getUpdatedAt();
    }
    if ( data.translationUpdatedByIdIndex >= 0 ) {
      outputRow[data.translationUpdatedByIdIndex] = translation.getUpdatedById();
    }
    if ( data.translationCreatedByIdIndex >= 0 ) {
      outputRow[data.translationCreatedByIdIndex] = translation.getCreatedById();
    }
    putRowTo( data.translationRowMeta, outputRow, data.translationOutputRowSet );
  }
}
