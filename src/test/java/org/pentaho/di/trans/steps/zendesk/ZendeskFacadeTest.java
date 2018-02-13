package org.pentaho.di.trans.steps.zendesk;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.pentaho.di.core.KettleEnvironment;
import org.pentaho.di.core.exception.KettleException;
import org.pentaho.di.core.logging.LogChannel;
import org.pentaho.di.core.logging.LogChannelInterface;
import org.pentaho.di.trans.step.BaseStep;
import org.zendesk.client.v2.Zendesk;

public class ZendeskFacadeTest {

  private BaseStep parentStep;
  private LogChannelInterface parentLog;
  private ZendeskFacade zendesk;
  private Zendesk.Builder builder;

  @BeforeClass
  public static void setUpBeforeClass() throws KettleException {
    KettleEnvironment.init( false );
  }

  @Before
  public void setUp() {
    builder = mock( Zendesk.Builder.class );
    parentStep = mock( BaseStep.class );
    parentLog = mock( LogChannel.class );
    when( parentStep.getLogChannel() ).thenReturn( parentLog );
    zendesk = spy( new ZendeskFacade( parentStep ) );
  }

  @Test
  public void testConnect() {
    ArgumentCaptor<String> subDomainCaptor = ArgumentCaptor.forClass( String.class );
    ArgumentCaptor<String> userCaptor = ArgumentCaptor.forClass( String.class );
    ArgumentCaptor<String> passCaptor = ArgumentCaptor.forClass( String.class );
    ArgumentCaptor<String> tokenCaptor = ArgumentCaptor.forClass( String.class );
    when( zendesk.getBuilder( subDomainCaptor.capture() ) ).thenReturn( builder );
    when( builder.setUsername( userCaptor.capture() ) ).thenReturn( builder );
    when( builder.setPassword( passCaptor.capture() ) ).thenReturn( builder );
    when( builder.setToken( tokenCaptor.capture() ) ).thenReturn( builder );
    when( builder.build() ).thenReturn( mock( Zendesk.class ) );
    
    zendesk.connect( "PdiZendeskPluginDomain", "PdiZendeskPluginUser", "PdiZendeskPluginPass", false );
    assertEquals( "https://PdiZendeskPluginDomain.zendesk.com", subDomainCaptor.getValue() );
    assertEquals( "PdiZendeskPluginUser", userCaptor.getValue() );
    assertEquals( "PdiZendeskPluginPass", passCaptor.getValue() );
    
    zendesk.connect( "PdiZendeskPluginDomain", "PdiZendeskPluginUser", "PdiZendeskPluginPass", true );
    assertEquals( "https://PdiZendeskPluginDomain.zendesk.com", subDomainCaptor.getValue() );
    assertEquals( "PdiZendeskPluginUser", userCaptor.getValue() );
    assertEquals( "PdiZendeskPluginPass", tokenCaptor.getValue() );
  }
}
