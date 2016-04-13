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

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;
import org.pentaho.di.core.KettleEnvironment;
import org.pentaho.di.core.exception.KettleException;
import org.pentaho.di.trans.steps.loadsave.LoadSaveTester;

public class ZendeskInputHCCategoryMetaTest {

  @BeforeClass
  public static void setUpBeforeClass() throws KettleException {
    KettleEnvironment.init( false );
  }

  @Test
  public void testRoundTrip() throws KettleException {
    List<String> attributes =
      Arrays.asList( "subDomain", "username", "password", "token", "categoryIdFieldname", "categoryUrlFieldname",
        "categoryNameFieldname", "descriptionFieldname", "localeFieldname", "sourceLocaleFieldname",
        "categoryHtmlUrlFieldname", "outdatedFieldname", "positionFieldname", "createdAtFieldname",
        "updatedAtFieldname" );

    LoadSaveTester loadSaveTester =
      new LoadSaveTester( ZendeskInputHCCategoryMeta.class, attributes, new HashMap<String, String>(),
        new HashMap<String, String>() );

    loadSaveTester.testRepoRoundTrip();
    loadSaveTester.testXmlRoundTrip();
  }

  @Test
  public void testDefault() {
    ZendeskInputHCCategoryMeta meta = new ZendeskInputHCCategoryMeta();
    meta.setDefault();
    assertNotNull( meta.getCategoryIdFieldname() );
    assertNotNull( meta.getCategoryUrlFieldname() );
    assertNotNull( meta.getCategoryNameFieldname() );
    assertNotNull( meta.getDescriptionFieldname() );
    assertNotNull( meta.getLocaleFieldname() );
    assertNotNull( meta.getSourceLocaleFieldname() );
    assertNotNull( meta.getCategoryHtmlUrlFieldname() );
    assertNotNull( meta.getOutdatedFieldname() );
    assertNotNull( meta.getPositionFieldname() );
    assertNotNull( meta.getCreatedAtFieldname() );
    assertNotNull( meta.getUpdatedAtFieldname() );
  }
}
