/*! ******************************************************************************
 *
 * Pentaho Data Integration
 *
 * Copyright (C) 2002-2017 by Pentaho : http://www.pentaho.com
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

import static org.junit.Assert.assertNull;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.junit.BeforeClass;
import org.junit.Test;
import org.pentaho.di.core.KettleEnvironment;
import org.pentaho.di.core.exception.KettleException;
import org.pentaho.di.trans.steps.loadsave.LoadSaveTester;
import org.pentaho.di.trans.steps.loadsave.validator.FieldLoadSaveValidator;
import org.pentaho.di.trans.steps.zendesk.ZendeskOutputSuspendedTicketsMeta.SuspendedTicketAction;

public class ZendeskOutputSuspendedTicketsMetaTest {

  @BeforeClass
  public static void setUpBeforeClass() throws KettleException {
    KettleEnvironment.init( false );
  }

  @Test
  public void testRoundTrip() throws KettleException {
    List<String> attributes =
      Arrays.asList( "subDomain", "username", "password", "token",
        "action", "ticketFieldName" );

    Map<String, FieldLoadSaveValidator<?>> attributeMap = new HashMap<String, FieldLoadSaveValidator<?>>();
    attributeMap.put( "action", new SuspendedTicketActionFieldLoadSaveValidator() );

    LoadSaveTester loadSaveTester =
      new LoadSaveTester( ZendeskOutputSuspendedTicketsMeta.class, attributes, new HashMap<String, String>(),
        new HashMap<String, String>(), attributeMap, new HashMap<String, FieldLoadSaveValidator<?>>() );

    loadSaveTester.testRepoRoundTrip();
    loadSaveTester.testXmlRoundTrip();
  }

  public class SuspendedTicketActionFieldLoadSaveValidator implements FieldLoadSaveValidator<SuspendedTicketAction> {
    @Override
    public SuspendedTicketAction getTestObject() {
      int index = new Random().nextInt( SuspendedTicketAction.values().length + 1 );
      if ( 0 == index ) {
        return null;
      } else {
        return SuspendedTicketAction.values()[( index - 1 )];
      }
    }

    @Override
    public boolean validateTestObject( SuspendedTicketAction testObject, Object actual ) {
      if ( testObject == null && actual == null ) {
        return true;
      }
      if ( testObject != null ) {
        return testObject.equals( actual );
      }
      return false;
    }
  }

  @Test
  public void testDefault() {
	  ZendeskOutputSuspendedTicketsMeta meta = new ZendeskOutputSuspendedTicketsMeta();
    meta.setDefault();
    assertNull( meta.getAction() );
    assertNull( meta.getTicketFieldName() );
  }

}
