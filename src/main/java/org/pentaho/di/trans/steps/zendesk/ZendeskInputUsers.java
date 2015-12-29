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

import java.util.ArrayList;
import java.util.List;

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
import org.zendesk.client.v2.model.Identity;
import org.zendesk.client.v2.model.User;

public class ZendeskInputUsers extends ZendeskInput {

  ZendeskInputUsersMeta meta;
  ZendeskInputUsersData data;

  @Override
  public boolean init( StepMetaInterface smi, StepDataInterface sdi ) {
    if ( !super.init( smi, sdi ) ) {
      return false;
    }
    meta = (ZendeskInputUsersMeta) smi;
    data = (ZendeskInputUsersData) sdi;
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
      if ( meta.getUserStepMeta() != null ) {
        data.userRowMeta = new RowMeta();
        meta.getFields( data.userRowMeta, getStepname(), null, meta.getUserStepMeta(), this, repository, metaStore );
        data.userOutputRowSet = findOutputRowSet( meta.getUserStepMeta().getName() );
        data.userIdIndex = data.userRowMeta.indexOfValue( environmentSubstitute( meta.getUserIdFieldname() ) );
        data.urlIndex = data.userRowMeta.indexOfValue( environmentSubstitute( meta.getUrlFieldname() ) );
        data.externalIdIndex = data.userRowMeta.indexOfValue( environmentSubstitute( meta.getExternalIdFieldname() ) );
        data.nameIndex = data.userRowMeta.indexOfValue( environmentSubstitute( meta.getNameFieldname() ) );
        data.emailIndex = data.userRowMeta.indexOfValue( environmentSubstitute( meta.getEmailFieldname() ) );
        data.aliasIndex = data.userRowMeta.indexOfValue( environmentSubstitute( meta.getAliasFieldname() ) );
        data.createdAtIndex = data.userRowMeta.indexOfValue( environmentSubstitute( meta.getCreatedAtFieldname() ) );
        data.updatedAtIndex = data.userRowMeta.indexOfValue( environmentSubstitute( meta.getUpdatedAtFieldname() ) );
        data.activeIndex = data.userRowMeta.indexOfValue( environmentSubstitute( meta.getActiveFieldname() ) );
        data.verifiedIndex = data.userRowMeta.indexOfValue( environmentSubstitute( meta.getVerifiedFieldname() ) );
        data.sharedIndex = data.userRowMeta.indexOfValue( environmentSubstitute( meta.getSharedFieldname() ) );
        data.localeIdIndex = data.userRowMeta.indexOfValue( environmentSubstitute( meta.getLocaleIdFieldname() ) );
        data.timeZoneIndex = data.userRowMeta.indexOfValue( environmentSubstitute( meta.getTimeZoneFieldname() ) );
        data.lastLoginAtIndex = data.userRowMeta.indexOfValue( environmentSubstitute( meta.getLastLoginAtFieldname() ) );
        data.phoneIndex = data.userRowMeta.indexOfValue( environmentSubstitute( meta.getPhoneFieldname() ) );
        data.signatureIndex = data.userRowMeta.indexOfValue( environmentSubstitute( meta.getSignatureFieldname() ) );
        data.detailsIndex = data.userRowMeta.indexOfValue( environmentSubstitute( meta.getDetailsFieldname() ) );
        data.notesIndex = data.userRowMeta.indexOfValue( environmentSubstitute( meta.getNotesFieldname() ) );
        data.organizationIdIndex = data.userRowMeta.indexOfValue( environmentSubstitute( meta.getOrganizationIdFieldname() ) );
        data.roleIndex = data.userRowMeta.indexOfValue( environmentSubstitute( meta.getRoleFieldname() ) );
        data.customRoleIdIndex = data.userRowMeta.indexOfValue( environmentSubstitute( meta.getCustomRoleIdFieldname() ) );
        data.moderatorIndex = data.userRowMeta.indexOfValue( environmentSubstitute( meta.getModeratorFieldname() ) );
        data.ticketRestrictionIndex = data.userRowMeta.indexOfValue( environmentSubstitute( meta.getTicketRestrictionFieldname() ) );
        data.onlyPrivateCommentsIndex = data.userRowMeta.indexOfValue( environmentSubstitute( meta.getOnlyPrivateCommentsFieldname() ) );
        data.tagsIndex = data.userRowMeta.indexOfValue( environmentSubstitute( meta.getTagsFieldname() ) );
        data.suspendedIndex = data.userRowMeta.indexOfValue( environmentSubstitute( meta.getSuspendedFieldname() ) );
        data.remotePhotoUrlIndex = data.userRowMeta.indexOfValue( environmentSubstitute( meta.getRemotePhotoUrlFieldname() ) );
        data.userFieldsIndex = data.userRowMeta.indexOfValue( environmentSubstitute( meta.getUserFieldsFieldname() ) );
      }

      if ( meta.getUserIdentityStepMeta() != null ) {
        data.userIdentityRowMeta = new RowMeta();
        meta.getFields( data.userIdentityRowMeta, getStepname(), null, meta.getUserIdentityStepMeta(), this, repository, metaStore );
        data.userIdentityOutputRowSet = findOutputRowSet( meta.getUserIdentityStepMeta().getName() );
        data.identityUserIdIndex = data.userIdentityRowMeta.indexOfValue( environmentSubstitute( meta.getUserIdFieldname() ) );
        data.identityIdIndex = data.userIdentityRowMeta.indexOfValue( environmentSubstitute( meta.getIdentityIdFieldname() ) );
        data.identityUrlIndex = data.userIdentityRowMeta.indexOfValue( environmentSubstitute( meta.getIdentityUrlFieldname() ) );
        data.identityTypeIndex = data.userIdentityRowMeta.indexOfValue( environmentSubstitute( meta.getIdentityTypeFieldname() ) );
        data.identityValueIndex = data.userIdentityRowMeta.indexOfValue( environmentSubstitute( meta.getIdentityValueFieldname() ) );
        data.identityVerifiedIndex = data.userIdentityRowMeta.indexOfValue( environmentSubstitute( meta.getIdentityVerifiedFieldname() ) );
        data.identityPrimaryIndex = data.userIdentityRowMeta.indexOfValue( environmentSubstitute( meta.getIdentityPrimaryFieldname() ) );
        data.identityCreatedAtIndex = data.userIdentityRowMeta.indexOfValue( environmentSubstitute( meta.getIdentityCreatedAtFieldname() ) );
        data.identityUpdatedAtIndex = data.userIdentityRowMeta.indexOfValue( environmentSubstitute( meta.getIdentityUpdatedAtFieldname() ) );
      }
    }

    if ( !data.isReceivingInput ) {
      Iterable<User> users = null;
      try {
        users = data.conn.getUsers();
      } catch ( ZendeskResponseException zre ) {
        logError( BaseMessages.getString( PKG, "ZendeskInput.Error.Generic", zre ) );
        setErrors( 1L );
        setOutputDone();
        return false;
      }

      int i = 0;
      for ( User user : users ) {
        i++;
        List<Identity> identities = new ArrayList<Identity>();
        try {
          if ( meta.getUserIdentityStepMeta() != null ) {
            identities.addAll( data.conn.getUserIdentities( user ) );
          }
        } catch ( ZendeskResponseException zre ) {
          logError( BaseMessages.getString( PKG, "ZendeskInput.Error.Generic", zre ) );
          setErrors( 1L );
          setOutputDone();
          return false;
        }
        outputUserRow( user );
        outputUserIdentityRow( identities );
        incrementLinesOutput();
      }
      logBasic("Total Users: " + i );
      setOutputDone();
      return false;
    } else if ( row == null ) {
      setOutputDone();
      return false;
    } else {
      if ( data.incomingIndex < 0 ) {
        logError( BaseMessages.getString( PKG, "ZendeskInput.Error.MissingField" ) );
        setErrors( 1L );
        setOutputDone();
        return false;
      }

      Long userId = getInputRowMeta().getValueMeta( data.incomingIndex ).getInteger( row[data.incomingIndex] );
      User user = null;
      try {
        user = data.conn.getUser( userId );
      } catch ( ZendeskResponseException zre ) {
        logError( BaseMessages.getString( PKG, "ZendeskInput.Error.Generic", zre ) );
        setErrors( 1L );
        setOutputDone();
        return false;
      }
      List<Identity> identities = new ArrayList<Identity>();
      try {
        if ( meta.getUserIdentityStepMeta() != null ) {
          identities.addAll( data.conn.getUserIdentities( user ) );
        }
      } catch ( ZendeskResponseException zre ) {
        logError( BaseMessages.getString( PKG, "ZendeskInput.Error.Generic", zre ) );
        setErrors( 1L );
        setOutputDone();
        return false;
      }
      outputUserRow( user );
      outputUserIdentityRow( identities );
      incrementLinesOutput();
      return true;
    }
  }

  public ZendeskInputUsers( StepMeta stepMeta, StepDataInterface stepDataInterface, int copyNr,
      TransMeta transMeta, Trans trans ) {
    super( stepMeta, stepDataInterface, copyNr, transMeta, trans );
  }

  private void outputUserRow( User user ) throws KettleStepException {
    if ( data.userRowMeta == null || data.userRowMeta.isEmpty() ) {
      return;
    }

    Object[] outputRow = RowDataUtil.allocateRowData( data.userRowMeta.size() );
    if ( data.userIdIndex >= 0 ) {
      outputRow[data.userIdIndex] = user.getId();
    }
    if ( data.urlIndex >= 0 ) {
      outputRow[data.urlIndex] = user.getUrl();
    }
    if ( data.externalIdIndex >= 0 ) {
      outputRow[data.externalIdIndex] = user.getExternalId();
    }
    if ( data.nameIndex >= 0 ) {
      outputRow[data.nameIndex] = user.getName();
    }
    if ( data.emailIndex >= 0 ) {
      outputRow[data.emailIndex] = user.getEmail();
    }
    if ( data.aliasIndex >= 0 ) {
      outputRow[data.aliasIndex] = user.getAlias();
    }
    if ( data.createdAtIndex >= 0 ) {
      outputRow[data.createdAtIndex] = user.getCreatedAt();
    }
    if ( data.updatedAtIndex >= 0 ) {
      outputRow[data.updatedAtIndex] = user.getUpdatedAt();
    }
    if ( data.activeIndex >= 0 ) {
      outputRow[data.activeIndex] = user.getActive();
    }
    if ( data.verifiedIndex >= 0 ) {
      outputRow[data.verifiedIndex] = user.getVerified();
    }
    if ( data.sharedIndex >= 0 ) {
      outputRow[data.sharedIndex] = user.getShared();
    }
    if ( data.localeIdIndex >= 0 ) {
      outputRow[data.localeIdIndex] = user.getLocaleId();
    }
    if ( data.timeZoneIndex >= 0 ) {
      outputRow[data.timeZoneIndex] = user.getTimeZone();
    }
    if ( data.lastLoginAtIndex >= 0 ) {
      outputRow[data.lastLoginAtIndex] = user.getLastLoginAt();
    }
    if ( data.phoneIndex >= 0 ) {
      outputRow[data.phoneIndex] = user.getPhone();
    }
    if ( data.signatureIndex >= 0 ) {
      outputRow[data.signatureIndex] = user.getSignature();
    }
    if ( data.detailsIndex >= 0 ) {
      outputRow[data.detailsIndex] = user.getDetails();
    }
    if ( data.notesIndex >= 0 ) {
      outputRow[data.notesIndex] = user.getNotes();
    }
    if ( data.organizationIdIndex >= 0 ) {
      outputRow[data.organizationIdIndex] = user.getOrganizationId();
    }
    if ( data.roleIndex >= 0 ) {
      outputRow[data.roleIndex] = user.getRole() == null ? null : user.getRole().name();
    }
    if ( data.customRoleIdIndex >= 0 ) {
      outputRow[data.customRoleIdIndex] = user.getCustomRoleId();
    }
    if ( data.moderatorIndex >= 0 ) {
      outputRow[data.moderatorIndex] = user.getModerator();
    }
    if ( data.ticketRestrictionIndex >= 0 ) {
      outputRow[data.ticketRestrictionIndex] = user.getTicketRestriction() == null ? null : user.getTicketRestriction().name();
    }
    if ( data.onlyPrivateCommentsIndex >= 0 ) {
      outputRow[data.onlyPrivateCommentsIndex] = user.getOnlyPrivateComments();
    }
    if ( data.tagsIndex >= 0 ) {
      outputRow[data.tagsIndex] = user.getTags().toString();
    }
    if ( data.suspendedIndex >= 0 ) {
      outputRow[data.suspendedIndex] = user.getSuspended();
    }
    if ( data.remotePhotoUrlIndex >= 0 ) {
      outputRow[data.remotePhotoUrlIndex] = user.getRemotePhotoUrl();
    }
    if ( data.userFieldsIndex >= 0 ) {
      outputRow[data.userFieldsIndex] = user.getUserFields() == null ? null : user.getUserFields().toString();
    }
    putRowTo( data.userRowMeta, outputRow, data.userOutputRowSet );
  }

  private void outputUserIdentityRow( List<Identity> identities ) throws KettleStepException {
    if ( data.userIdentityRowMeta == null || data.userIdentityRowMeta.isEmpty() ||
        identities == null || identities.isEmpty() ) {
      return;
    }

    for ( Identity ident : identities ) {
      Object[] outputRow = RowDataUtil.allocateRowData( data.userIdentityRowMeta.size() );

      if ( data.identityUserIdIndex >= 0 ) {
      outputRow[data.identityUserIdIndex] = Long.valueOf( ident.getUserId() );
      }
      if ( data.identityIdIndex >= 0 ) {
        outputRow[data.identityIdIndex] = ident.getId();
      }
      if ( data.identityUrlIndex >= 0 ) {
        outputRow[data.identityUrlIndex] = ident.getUrl();
      }
      if ( data.identityTypeIndex >= 0 ) {
        outputRow[data.identityTypeIndex] = ident.getType();
      }
      if ( data.identityValueIndex >= 0 ) {
        outputRow[data.identityValueIndex] = ident.getValue();
      }
      if ( data.identityVerifiedIndex >= 0 ) {
        outputRow[data.identityVerifiedIndex] = ident.getVerified();
      }
      if ( data.identityPrimaryIndex >= 0 ) {
        outputRow[data.identityPrimaryIndex] = ident.getPrimary();
      }
      if ( data.identityCreatedAtIndex >= 0 ) {
        outputRow[data.identityCreatedAtIndex] = ident.getCreatedAt();
      }
      if ( data.identityUpdatedAtIndex >= 0 ) {
        outputRow[data.identityUpdatedAtIndex] = ident.getUpdatedAt();
      }
      putRowTo( data.userIdentityRowMeta, outputRow, data.userIdentityOutputRowSet );
    }
  }
}
