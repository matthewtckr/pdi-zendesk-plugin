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

public class ZendeskInputUsersData extends ZendeskInputData {

  RowMetaInterface userRowMeta;
  RowMetaInterface userIdentityRowMeta;

  RowSet userOutputRowSet;
  RowSet userIdentityOutputRowSet;

  int incomingIndex;
  int userIdIndex;
  int urlIndex;
  int externalIdIndex;
  int nameIndex;
  int emailIndex;
  int aliasIndex;
  int createdAtIndex;
  int updatedAtIndex;
  int activeIndex;
  int verifiedIndex;
  int sharedIndex;
  int localeIdIndex;
  int timeZoneIndex;
  int lastLoginAtIndex;
  int phoneIndex;
  int signatureIndex;
  int detailsIndex;
  int notesIndex;
  int organizationIdIndex;
  int roleIndex;
  int customRoleIdIndex;
  int moderatorIndex;
  int ticketRestrictionIndex;
  int onlyPrivateCommentsIndex;
  int tagsIndex;
  int suspendedIndex;
  int photoIndex;
  int identitiesIndex;
  int remotePhotoUrlIndex;
  int userFieldsIndex;
  int identityUserIdIndex;
  int identityIdIndex;
  int identityUrlIndex;
  int identityTypeIndex;
  int identityValueIndex;
  int identityVerifiedIndex;
  int identityPrimaryIndex;
  int identityCreatedAtIndex;
  int identityUpdatedAtIndex;
}
