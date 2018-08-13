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
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

import org.junit.BeforeClass;
import org.junit.Test;
import org.pentaho.di.core.KettleEnvironment;
import org.pentaho.di.core.exception.KettleException;
import org.pentaho.di.trans.steps.loadsave.LoadSaveTester;
import org.pentaho.di.trans.steps.loadsave.validator.ArrayLoadSaveValidator;
import org.pentaho.di.trans.steps.loadsave.validator.FieldLoadSaveValidator;
import org.pentaho.di.trans.steps.zendesk.ZendeskInputTicketFieldsMeta.TicketField;
import org.pentaho.di.trans.steps.zendesk.ZendeskInputTicketFieldsMeta.TicketField.Attribute;

public class ZendeskInputTicketFieldsMetaTest {

  @BeforeClass
  public static void setUpBeforeClass() throws KettleException {
    KettleEnvironment.init( false );
  }

  @Test
  public void testRoundTrip() throws KettleException {
    List<String> attributes =
      Arrays.asList( "TicketFields" );

    Map<String, String> getters = Collections.emptyMap();
    Map<String, String> setters = Collections.emptyMap();
    Map<String, FieldLoadSaveValidator<?>> attributeValidators = new HashMap<>();
    Map<String, FieldLoadSaveValidator<?>> typeValidators = new HashMap<>();
    attributeValidators.put( "TicketFields",
      new ArrayLoadSaveValidator<TicketField>( new TicketFieldLoadSaveValidator(), 25 ) );

    LoadSaveTester loadSaveTester =
      new LoadSaveTester( ZendeskInputTicketFieldsMeta.class, attributes, getters, setters,
        attributeValidators, typeValidators );

    loadSaveTester.testXmlRoundTrip();
    loadSaveTester.testRepoRoundTrip();
  }

  @Test
  public void testDefault() {
    ZendeskInputTicketFieldsMeta meta = new ZendeskInputTicketFieldsMeta();
    meta.setDefault();
    assertNotNull( meta.getTicketFields() );
    assertTrue( meta.getTicketFields().length > 0 );
  }

  public static class TicketFieldLoadSaveValidator implements FieldLoadSaveValidator<TicketField>{

    @Override
    public TicketField getTestObject() {
      Attribute[] values = Attribute.values();

      return new TicketField(
        UUID.randomUUID().toString(),
        values[new Random().nextInt( values.length )] );
    }

    @Override
    public boolean validateTestObject(TicketField testObject, Object actual) {
      if ( !( actual instanceof TicketField ) ) {
        return false;
      }
      TicketField actualObject = (TicketField) actual;
      return testObject.getName().equals( actualObject.getName() ) &&
        testObject.getType() == actualObject.getType();
    }
  }
}
