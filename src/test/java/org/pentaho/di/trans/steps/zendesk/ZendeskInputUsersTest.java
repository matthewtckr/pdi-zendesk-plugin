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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyBoolean;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.pentaho.di.core.KettleEnvironment;
import org.pentaho.di.core.exception.KettleException;
import org.pentaho.di.core.logging.LoggingObjectInterface;
import org.pentaho.di.trans.steps.mock.StepMockHelper;
import org.zendesk.client.v2.Zendesk;

public class ZendeskInputUsersTest {

  private StepMockHelper<ZendeskInputUsersMeta, ZendeskInputUsersData> smh;
  private Zendesk conn;

  @BeforeClass
  public static void setUpBeforeClass() throws KettleException {
    KettleEnvironment.init( false );
  }

  @Before
  public void setUp() throws KettleException {
    smh = new StepMockHelper<ZendeskInputUsersMeta, ZendeskInputUsersData>( "UsersMeta",
      ZendeskInputUsersMeta.class, ZendeskInputUsersData.class );
    conn = mock( Zendesk.class );
    when( conn.isClosed() ).thenReturn( false );
    when( smh.logChannelInterfaceFactory.create( any(), any( LoggingObjectInterface.class ) ) ).thenReturn(
      smh.logChannelInterface );
    when( smh.trans.isRunning() ).thenReturn( true );
  }

  @Test
  public void testZendeskInputInit() {
    ArgumentCaptor<String> subDomainCaptor = ArgumentCaptor.forClass( String.class );
    ArgumentCaptor<String> userCaptor = ArgumentCaptor.forClass( String.class );
    ArgumentCaptor<String> passCaptor = ArgumentCaptor.forClass( String.class );
    ArgumentCaptor<Boolean> tokenCaptor = ArgumentCaptor.forClass( Boolean.class );

    ZendeskInputUsers step =
      spy( new ZendeskInputUsers( smh.stepMeta, smh.stepDataInterface, 0, smh.transMeta, smh.trans ) );
    doReturn( conn ).when( step ).createConnection( subDomainCaptor.capture(), userCaptor.capture(),
      passCaptor.capture(), tokenCaptor.capture() );
    /*when( step.createConnection( subDomainCaptor.capture(), userCaptor.capture(), passCaptor.capture(),
      tokenCaptor.capture() ) ).thenReturn( conn );*/
    ZendeskInputUsersMeta meta = new ZendeskInputUsersMeta();

    meta.setDefault();
    assertFalse( step.init( meta, smh.stepDataInterface ) );

    step.setVariable( "ZENDESK_DOMAIN", "fakeSubdomain" );
    meta.setSubDomain( "${ZENDESK_DOMAIN}" );
    assertFalse( step.init( meta, smh.stepDataInterface ) );

    step.setVariable( "ZENDESK_USER", "unit_test_user" );
    meta.setUsername( "${ZENDESK_USER}" );
    assertFalse( step.init( meta, smh.stepDataInterface ) );

    step.setVariable( "ZENDESK_PASS", "randomPassword" );
    meta.setPassword( "${ZENDESK_PASS}" );
    assertTrue( step.init( meta, smh.stepDataInterface ) );

    assertEquals( "fakeSubdomain", subDomainCaptor.getValue() );
    assertEquals( "unit_test_user", userCaptor.getValue() );
    assertEquals( "randomPassword", passCaptor.getValue() );
    assertEquals( false, tokenCaptor.getValue() );

    meta.setToken( true );
    assertTrue( step.init( meta, smh.stepDataInterface ) );
    assertEquals( true, tokenCaptor.getValue() );

    step.dispose( meta, smh.stepDataInterface );
    verify( conn, times( 1 ) ).close();

    // Init should fail if a connection is not created
    doReturn( null ).when( step ).createConnection( anyString(), anyString(), anyString(), anyBoolean() );
    assertFalse( step.init( meta, smh.stepDataInterface ) );

    
  }
}
