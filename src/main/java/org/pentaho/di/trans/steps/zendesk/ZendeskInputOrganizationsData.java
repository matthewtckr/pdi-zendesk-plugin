/*! ******************************************************************************
 *
 * Pentaho Data Integration
 *
 * Copyright (C) 2002-2015 by Pentaho : http://www.pentaho.com
 *
 *******************************************************************************
 *
 * Licensed under the Apache License, Version 2.0 (the "License")Index;
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

public class ZendeskInputOrganizationsData extends ZendeskInputData {

  RowMetaInterface organizationRowMeta;
  RowMetaInterface organizationTagRowMeta;
  RowMetaInterface organizationFieldRowMeta;
  RowMetaInterface organizationDomainRowMeta;

  RowSet organizationOutputRowSet;
  RowSet organizationTagOutputRowSet;
  RowSet organizationFieldOutputRowSet;
  RowSet organizationDomainOutputRowSet;

  boolean isReceivingInput;

  int incomingIndex;
  int organizationIdIndex;
  int urlIndex;
  int externalIdIndex;
  int nameIndex;
  int createdAtIndex;
  int updatedAtIndex;
  int detailsIndex;
  int notesIndex;
  int groupIdIndex;
  int sharedTicketsIndex;
  int sharedCommentsIndex;

  int tagOrganizationIdIndex;
  int tagValueIndex;

  int domainOrganizationIdIndex;
  int domainNameIndex;

  int fieldOrganizationIdIndex;
  int fieldNameIndex;
  int fieldValueIndex;
  
}
