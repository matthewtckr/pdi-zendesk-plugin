/*! ******************************************************************************
 *
 * Pentaho Data Integration
 *
 * Copyright (C) 2002-2018 by Pentaho : http://www.pentaho.com
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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.InputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

import org.junit.BeforeClass;
import org.junit.Test;
import org.pentaho.di.core.Const;
import org.pentaho.di.core.KettleEnvironment;
import org.pentaho.di.core.database.DatabaseMeta;
import org.pentaho.di.core.exception.KettleException;
import org.pentaho.di.core.exception.KettleXMLException;
import org.pentaho.di.core.xml.XMLHandler;
import org.pentaho.di.trans.steps.loadsave.LoadSaveTester;
import org.pentaho.di.trans.steps.loadsave.validator.ArrayLoadSaveValidator;
import org.pentaho.di.trans.steps.loadsave.validator.FieldLoadSaveValidator;
import org.pentaho.di.trans.steps.zendesk.ZendeskInputUsersMeta.IdentityField;
import org.pentaho.di.trans.steps.zendesk.ZendeskInputUsersMeta.UserField;
import org.pentaho.metastore.api.IMetaStore;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

public class ZendeskInputUsersMetaTest {

  @BeforeClass
  public static void setUpBeforeClass() throws KettleException {
    KettleEnvironment.init( false );
  }

  @Test
  public void testRoundTrip() throws KettleException {
    List<String> attributes =
      Arrays.asList( "subDomain", "username", "password", "token", "incomingFieldname", "userFields", "identityFields" );
    Map<String, FieldLoadSaveValidator<?>> attrValidators = new HashMap<>();
    attrValidators.put( "userFields",
      new ArrayLoadSaveValidator<UserField>( new UserFieldLoadSaveValidator(), 10 ) );
    attrValidators.put( "identityFields",
      new ArrayLoadSaveValidator<IdentityField>( new IdentityFieldLoadSaveValidator(), 5 ) );

    LoadSaveTester loadSaveTester =
      new LoadSaveTester( ZendeskInputUsersMeta.class, attributes, new HashMap<String, String>(),
        new HashMap<String, String>(), attrValidators, new HashMap<String, FieldLoadSaveValidator<?>>() );

    loadSaveTester.testRepoRoundTrip();
    loadSaveTester.testXmlRoundTrip();
  }

  @Test
  public void testDefault() {
    ZendeskInputUsersMeta meta = new ZendeskInputUsersMeta();
    meta.setDefault();
    assertTrue( Const.isEmpty( meta.getIncomingFieldname() ) );
    assertNotNull( meta.getUserFields() );
    assertNotNull( meta.getIdentityFields() );
  }

  @Test
  public void testLegacyXML() throws KettleXMLException {
    ZendeskInputUsersMeta meta = new ZendeskInputUsersMeta();
    InputStream legacyXMLStream = this.getClass().getResourceAsStream( "ZendeskInputUsers_LegacyStep.xml" );
    Document doc = XMLHandler.loadXMLFile( legacyXMLStream );
    Node transformationSteps = XMLHandler.getSubNode( doc, "transformation-steps" );
    Node steps = XMLHandler.getSubNode( transformationSteps, "steps" );
    List<Node> stepList = XMLHandler.getNodes( steps, "step" );
    Node usersStep = null;
    for ( Node step : stepList ) {
      if ( "ZendeskInputUsers".equals( XMLHandler.getTagValue( step, "type" ) ) ) {
        usersStep = step;
        break;
      }
    }
    assertNotNull( usersStep );
    meta.loadXML( usersStep, (List<DatabaseMeta>) null, (IMetaStore) null );

    assertEquals( "${ZENDESK_APITOKEN_SUBDOMAIN}", meta.getSubDomain() );
    assertEquals( "${ZENDESK_APITOKEN_USER}", meta.getUsername() );
    assertEquals( "${ZENDESK_APITOKEN_PASS}", meta.getPassword() );
    assertEquals( true, meta.isToken() );
    assertEquals( "User_ID", meta.getIncomingFieldname() );
    assertEquals( 27, meta.getUserFields().length );
    assertEquals( 9, meta.getIdentityFields().length );
  }

  public static class UserFieldLoadSaveValidator implements FieldLoadSaveValidator<UserField> {

    @Override
    public UserField getTestObject() {
      UserField.Attribute[] values = UserField.Attribute.values();
		
      return new UserField(
        UUID.randomUUID().toString(),
        values[new Random().nextInt( values.length )] );
	}

    @Override
    public boolean validateTestObject( UserField testObject, Object actual ) {
      if ( !( actual instanceof UserField ) ) {
        return false;
      }
      UserField actualObject = (UserField) actual;
      return testObject.getName().equals( actualObject.getName() ) &&
        testObject.getType() == actualObject.getType();
    }
  }

  public static class IdentityFieldLoadSaveValidator implements FieldLoadSaveValidator<IdentityField> {

    @Override
    public IdentityField getTestObject() {
      IdentityField.Attribute[] values = IdentityField.Attribute.values();

      return new IdentityField(
        UUID.randomUUID().toString(),
        values[new Random().nextInt( values.length )] );
    }

    @Override
    public boolean validateTestObject( IdentityField testObject, Object actual ) {
      if ( !(actual instanceof IdentityField ) ) {
        return false;
      }
      IdentityField actualObject = (IdentityField) actual;
      return testObject.getName().equals( actualObject.getName() ) &&
        testObject.getType() == actualObject.getType();
    }
  }
}
