/*! ******************************************************************************
 *
 * Pentaho Data Integration
 *
 * Copyright (C) 2002-2018 by Pentaho : http://www.pentaho.com
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
import org.pentaho.di.trans.steps.zendesk.ZendeskInputUsersMeta.IdentityField;
import org.pentaho.di.trans.steps.zendesk.ZendeskInputUsersMeta.UserField;
import org.zendesk.client.v2.model.Identity;
import org.zendesk.client.v2.model.User;

public class ZendeskInputUsersData extends ZendeskInputData {

  RowMetaInterface userRowMeta;
  RowMetaInterface userIdentityRowMeta;

  RowSet userOutputRowSet;
  RowSet userIdentityOutputRowSet;

  boolean isReceivingInput;
  public int incomingIndex;

  public Object getValue( User user, UserField.Attribute attribute ) {
	if ( user == null || attribute == null ) {
      return null;
	}
	switch ( attribute ) {
      case USERID:
        return user.getId();
      case URL:
        return user.getUrl();
      case EXTERNALID:
        return user.getExternalId();
      case NAME:
        return user.getName();
      case EMAIL:
        return user.getEmail();
      case ALIAS:
        return user.getAlias();
      case CREATED_AT:
        return user.getCreatedAt();
      case UPDATED_AT:
        return user.getUpdatedAt();
      case ACTIVE:
        return user.getActive();
      case VERIFIED:
        return user.getVerified();
      case SHARED:
        return user.getShared();
      case LOCALE_ID:
        return user.getLocaleId();
      case TIMEZONE:
        return user.getTimeZone();
      case LAST_LOGIN_AT:
        return user.getLastLoginAt();
      case PHONE:
        return user.getPhone();
      case SIGNATURE:
        return user.getSignature();
      case DETAILS:
        return user.getDetails();
      case NOTES:
        return user.getNotes();
      case ORGANIZATION_ID:
        return user.getOrganizationId();
      case ROLE:
        return user.getRole() == null ? null : user.getRole();
      case CUSTOM_ROLE_ID:
        return user.getCustomRoleId();
      case MODERATOR:
        return user.getModerator();
      case TICKET_RESTRICTION:
        return user.getTicketRestriction() == null ? null : user.getTicketRestriction().name();
      case ONLY_PRIVATE_COMMENTS:
        return user.getOnlyPrivateComments();
      case TAGS:
        return user.getTags().toString();
      case SUSPENDED:
        return user.getSuspended();
      case REMOTE_PHOTO_URL:
        return user.getRemotePhotoUrl();
      case USER_FIELDS:
        return user.getUserFields() == null ? null : user.getUserFields().toString();
      default:
    	return null;
	}
  }
  public Object getValue( Identity identity, IdentityField.Attribute attribute ) {
    if ( identity == null || attribute == null ) {
      return null;
    }
    switch ( attribute ) {
      case ID:
        return identity.getId();
      case USERID:
        return identity.getUserId();
      case URL:
        return identity.getUrl();
      case TYPE:
        return identity.getType();
      case VALUE:
        return identity.getValue();
      case VERIFIED:
        return identity.getVerified();
      case PRIMARY:
        return identity.getPrimary();
      case CREATED_AT:
        return identity.getCreatedAt();
      case UPDATED_AT:
        return identity.getUpdatedAt();
      default:
        return null;
    }
  }
}
