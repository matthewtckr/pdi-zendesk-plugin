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

import java.util.List;

import org.pentaho.di.core.Const;
import org.pentaho.di.core.database.DatabaseMeta;
import org.pentaho.di.core.encryption.Encr;
import org.pentaho.di.core.exception.KettleException;
import org.pentaho.di.core.exception.KettlePluginException;
import org.pentaho.di.core.exception.KettleStepException;
import org.pentaho.di.core.exception.KettleXMLException;
import org.pentaho.di.core.row.RowMetaInterface;
import org.pentaho.di.core.row.ValueMetaInterface;
import org.pentaho.di.core.row.value.ValueMetaFactory;
import org.pentaho.di.core.xml.XMLHandler;
import org.pentaho.di.i18n.BaseMessages;
import org.pentaho.di.repository.ObjectId;
import org.pentaho.di.repository.Repository;
import org.pentaho.di.trans.step.BaseStepMeta;
import org.pentaho.di.trans.step.StepMetaInterface;
import org.pentaho.metastore.api.IMetaStore;
import org.w3c.dom.Node;

public abstract class ZendeskInputMeta extends BaseStepMeta implements StepMetaInterface {

  private static final Class<?> PKG = ZendeskInputMeta.class;

  private String subDomain;
  private String username;
  private String password;
  private boolean isToken;

  public ZendeskInputMeta() {
    super();
  }

  @Override
  public void setDefault() {
    subDomain = "";
    username = "";
    password = "";
    isToken = false;
  }

  @Override
  public String getXML() throws KettleException {
    StringBuilder builder = new StringBuilder();
    builder.append( "    " ).append( XMLHandler.addTagValue( "subdomain", getSubDomain() ) );
    builder.append( "    " ).append( XMLHandler.addTagValue( "username", getUsername() ) );
    builder.append( "    " ).append( XMLHandler.addTagValue( "password", Encr.encryptPasswordIfNotUsingVariables( getPassword() ) ) );
    builder.append( "    " ).append( XMLHandler.addTagValue( "isToken", isToken() ) );
    return builder.toString();
  }

  @Override
  public void loadXML( Node stepnode, List<DatabaseMeta> databases, IMetaStore metaStore ) throws KettleXMLException {
    setSubDomain( XMLHandler.getTagValue( stepnode, "subdomain" ) );
    setUsername( XMLHandler.getTagValue( stepnode, "username" ) );
    setPassword( Encr.decryptPasswordOptionallyEncrypted( XMLHandler.getTagValue( stepnode, "password" ) ) );
    setToken( "Y".equalsIgnoreCase( XMLHandler.getTagValue( stepnode, "isToken" ) ) );
  }

  @Override
  public void readRep( Repository rep, IMetaStore metaStore, ObjectId id_step, List<DatabaseMeta> databases )
    throws KettleException {
    setSubDomain( rep.getStepAttributeString( id_step, "subdomain" ) );
    setUsername( rep.getStepAttributeString( id_step, "username" ) );
    setPassword( Encr.decryptPasswordOptionallyEncrypted( rep.getStepAttributeString( id_step, "password" ) ) );
    setToken( rep.getStepAttributeBoolean( id_step, "isToken" ) );
  }

  @Override
  public void saveRep( Repository rep, IMetaStore metaStore, ObjectId id_transformation, ObjectId id_step )
    throws KettleException {
    rep.saveStepAttribute( id_transformation, id_step, "subdomain", getSubDomain() );
    rep.saveStepAttribute( id_transformation, id_step, "username", getUsername() );
    rep.saveStepAttribute( id_transformation, id_step, "password", Encr.encryptPasswordIfNotUsingVariables( getPassword() ) );
    rep.saveStepAttribute( id_transformation, id_step, "isToken", isToken() );
  }

  public String getSubDomain() {
    return subDomain;
  }

  public void setSubDomain( String subDomain ) {
    this.subDomain = subDomain;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername( String username ) {
    this.username = username;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword( String password ) {
    this.password = password;
  }

  public boolean isToken() {
    return isToken;
  }

  public void setToken( boolean isToken ) {
    this.isToken = isToken;
  }

  protected void addFieldToRow( RowMetaInterface row, String fieldName, int type ) throws KettleStepException {
    if ( !Const.isEmpty( fieldName ) ) {
      try {
        ValueMetaInterface value = ValueMetaFactory.createValueMeta( fieldName, type );
        value.setOrigin( getName() );
        row.addValueMeta( value );
      } catch ( KettlePluginException e ) {
        throw new KettleStepException( BaseMessages.getString( PKG,
          "TransExecutorMeta.ValueMetaInterfaceCreation", fieldName ), e );
      }
    }
  }
}
