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

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;
import org.pentaho.di.core.Const;
import org.pentaho.di.core.KettleEnvironment;
import org.pentaho.di.core.exception.KettleException;
import org.pentaho.di.trans.steps.loadsave.LoadSaveTester;

public class ZendeskInputUsersMetaTest {

  @BeforeClass
  public static void setUpBeforeClass() throws KettleException {
    KettleEnvironment.init( false );
  }

  @Test
  public void testRoundTrip() throws KettleException {
    List<String> attributes =
      Arrays.asList( "subDomain", "username", "password", "token", "incomingFieldname", "userIdFieldname",
        "urlFieldname", "externalIdFieldname", "nameFieldname", "emailFieldname", "aliasFieldname",
        "createdAtFieldname", "updatedAtFieldname", "activeFieldname", "verifiedFieldname", "sharedFieldname",
        "localeIdFieldname", "timeZoneFieldname", "lastLoginAtFieldname", "phoneFieldname", "signatureFieldname",
        "detailsFieldname", "notesFieldname", "organizationIdFieldname", "roleFieldname", "customRoleIdFieldname",
        "moderatorFieldname", "ticketRestrictionFieldname", "onlyPrivateCommentsFieldname", "tagsFieldname",
        "suspendedFieldname", "remotePhotoUrlFieldname", "userFieldsFieldname", "identityIdFieldname",
        "identityUrlFieldname", "identityTypeFieldname", "identityValueFieldname", "identityVerifiedFieldname",
        "identityPrimaryFieldname", "identityCreatedAtFieldname", "identityUpdatedAtFieldname", "userStepName",
        "userIdentityStepName" );

    LoadSaveTester loadSaveTester =
      new LoadSaveTester( ZendeskInputUsersMeta.class, attributes, new HashMap<String, String>(),
        new HashMap<String, String>() );

    loadSaveTester.testRepoRoundTrip();
    loadSaveTester.testXmlRoundTrip();
  }

  @Test
  public void testDefault() {
    ZendeskInputUsersMeta meta = new ZendeskInputUsersMeta();
    meta.setDefault();
    assertTrue( Const.isEmpty( meta.getIncomingFieldname() ) );
    assertNotNull( meta.getUserIdFieldname() );
    assertNotNull( meta.getUrlFieldname() );
    assertNotNull( meta.getExternalIdFieldname() );
    assertNotNull( meta.getNameFieldname() );
    assertNotNull( meta.getEmailFieldname() );
    assertNotNull( meta.getAliasFieldname() );
    assertNotNull( meta.getCreatedAtFieldname() );
    assertNotNull( meta.getUpdatedAtFieldname() );
    assertNotNull( meta.getActiveFieldname() );
    assertNotNull( meta.getVerifiedFieldname() );
    assertNotNull( meta.getSharedFieldname() );
    assertNotNull( meta.getLocaleIdFieldname() );
    assertNotNull( meta.getTimeZoneFieldname() );
    assertNotNull( meta.getLastLoginAtFieldname() );
    assertNotNull( meta.getPhoneFieldname() );
    assertNotNull( meta.getSignatureFieldname() );
    assertNotNull( meta.getDetailsFieldname() );
    assertNotNull( meta.getNotesFieldname() );
    assertNotNull( meta.getOrganizationIdFieldname() );
    assertNotNull( meta.getRoleFieldname() );
    assertNotNull( meta.getCustomRoleIdFieldname() );
    assertNotNull( meta.getModeratorFieldname() );
    assertNotNull( meta.getTicketRestrictionFieldname() );
    assertNotNull( meta.getOnlyPrivateCommentsFieldname() );
    assertNotNull( meta.getTagsFieldname() );
    assertNotNull( meta.getSuspendedFieldname() );
    assertNotNull( meta.getRemotePhotoUrlFieldname() );
    assertNotNull( meta.getUserFieldsFieldname() );
    assertNotNull( meta.getIdentityIdFieldname() );
    assertNotNull( meta.getIdentityUrlFieldname() );
    assertNotNull( meta.getIdentityTypeFieldname() );
    assertNotNull( meta.getIdentityValueFieldname() );
    assertNotNull( meta.getIdentityVerifiedFieldname() );
    assertNotNull( meta.getIdentityPrimaryFieldname() );
    assertNotNull( meta.getIdentityCreatedAtFieldname() );
    assertNotNull( meta.getIdentityUpdatedAtFieldname() );
  }
}
