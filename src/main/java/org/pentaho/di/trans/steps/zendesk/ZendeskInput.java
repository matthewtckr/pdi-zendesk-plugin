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

import org.pentaho.di.core.Const;
import org.pentaho.di.core.encryption.Encr;
import org.pentaho.di.i18n.BaseMessages;
import org.pentaho.di.trans.Trans;
import org.pentaho.di.trans.TransMeta;
import org.pentaho.di.trans.step.BaseStep;
import org.pentaho.di.trans.step.StepDataInterface;
import org.pentaho.di.trans.step.StepInterface;
import org.pentaho.di.trans.step.StepMeta;
import org.pentaho.di.trans.step.StepMetaInterface;
import org.zendesk.client.v2.Zendesk;


public abstract class ZendeskInput extends BaseStep implements StepInterface {

  static Class<?> PKG = ZendeskInput.class;

  ZendeskInputMeta meta;
  ZendeskInputData data;

  public ZendeskInput( StepMeta stepMeta, StepDataInterface stepDataInterface, int copyNr, TransMeta transMeta,
      Trans trans ) {
    super( stepMeta, stepDataInterface, copyNr, transMeta, trans );
  }

  @Override
  public boolean init( StepMetaInterface smi, StepDataInterface sdi ) {
    if ( !super.init( smi, sdi ) ) {
      return false;
    }
    meta = (ZendeskInputMeta) smi;
    data = (ZendeskInputData) sdi;

    String subDomain = environmentSubstitute( meta.getSubDomain() );
    String username = environmentSubstitute( meta.getUsername() );
    String password = Encr.decryptPasswordOptionallyEncrypted( environmentSubstitute( meta.getPassword() ) );

    if ( Const.isEmpty( subDomain ) || Const.isEmpty( username ) || Const.isEmpty( password ) ) {
      logError( BaseMessages.getString( PKG, "ZendeskInput.Error.MissingCredentials" ) );
      return false;
    }

    data.conn = createConnection( subDomain, username, password, meta.isToken() );
    if ( data.conn == null || data.conn.isClosed() ) {
      return false;
    }
    return true;
  }

  Zendesk createConnection( String subDomain, String username, String password, boolean token ) {
    Zendesk.Builder builder = new Zendesk.Builder( String.format( "https://%s.zendesk.com", subDomain ) );

    if ( username.contains( "/token" ) ) {
      token = true;
      username = username.replaceAll( "/token", "" );
      logDetailed( BaseMessages.getString( PKG, "ZendeskInput.UsernameContainsToken.Warning" ) );
    }
    builder.setUsername( username );

    if ( token ) {
      builder.setToken( password );
    } else {
      builder.setPassword( password );
    }
    return builder.build();
  }

  @Override
  public void dispose( StepMetaInterface smi, StepDataInterface sdi ) {
    data.conn.close();
    super.dispose( smi, sdi );
  }
}
