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

import org.pentaho.di.core.RowSet;
import org.pentaho.di.core.row.RowMetaInterface;

public class ZendeskInputHCArticleData extends ZendeskInputData {

  RowMetaInterface articleRowMeta;
  RowMetaInterface translationRowMeta;

  RowSet articleOutputRowSet;
  RowSet translationOutputRowSet;

  boolean isReceivingInput;

  int incomingIndex;

  int articleIdIndex;
  int articleUrlIndex;
  int articleTitleIndex;
  int articleBodyIndex;
  int localeIndex;
  int sourceLocaleIndex;
  int authorIdIndex;
  int commentsDisabledIndex;
  int outdatedIndex;
  int labelsIndex;
  int draftIndex;
  int promotedIndex;
  int positionIndex;
  int voteSumIndex;
  int voteCountIndex;
  int sectionIdIndex;
  int createdAtIndex;
  int updatedAtIndex;

  int translationArticleIdIndex;
  int translationIdIndex;
  int translationUrlIndex;
  int translationHtmlUrlIndex;
  int translationSourceIdIndex;
  int translationSourceTypeIndex;
  int translationLocaleIndex;
  int translationTitleIndex;
  int translationBodyIndex;
  int translationOutdatedIndex;
  int translationDraftIndex;
  int translationCreatedAtIndex;
  int translationUpdatedAtIndex;
  int translationUpdatedByIdIndex;
  int translationCreatedByIdIndex;
}
