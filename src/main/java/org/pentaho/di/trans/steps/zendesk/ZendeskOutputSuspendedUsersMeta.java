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

import java.util.List;

import org.pentaho.di.core.annotations.Step;
import org.pentaho.di.core.database.DatabaseMeta;
import org.pentaho.di.core.exception.KettleException;
import org.pentaho.di.core.exception.KettleStepException;
import org.pentaho.di.core.exception.KettleXMLException;
import org.pentaho.di.core.row.RowMetaInterface;
import org.pentaho.di.core.row.ValueMetaInterface;
import org.pentaho.di.core.variables.VariableSpace;
import org.pentaho.di.core.xml.XMLHandler;
import org.pentaho.di.repository.ObjectId;
import org.pentaho.di.repository.Repository;
import org.pentaho.di.trans.Trans;
import org.pentaho.di.trans.TransMeta;
import org.pentaho.di.trans.step.StepDataInterface;
import org.pentaho.di.trans.step.StepInterface;
import org.pentaho.di.trans.step.StepMeta;
import org.pentaho.metastore.api.IMetaStore;
import org.w3c.dom.Node;

@Step(
    id = "ZendeskOutputSuspendedUsers",
    image = "org/pentaho/di/trans/steps/zendesk/zendesk.png",
    i18nPackageName = "org.pentaho.di.trans.steps.zendesk",
    name = "ZendeskOutputSuspendedUsers.Name",
    description = "ZendeskOutputSuspendedUsers.TooltipDesc",
    categoryDescription = "i18n:org.pentaho.di.trans.step:BaseStep.Category.Output"
  )
public class ZendeskOutputSuspendedUsersMeta extends ZendeskInputMeta {

  private static final Class<?> PKG = ZendeskOutputSuspendedUsersMeta.class;

  private String userFieldName;
  private String actionFieldName;
  private String resultFieldName;

  public String getUserFieldName() {
    return userFieldName;
  }

  public void setUserFieldName( String userFieldName ) {
    this.userFieldName = userFieldName;
  }

  public String getActionFieldName() {
    return actionFieldName;
  }

  public void setActionFieldName( String actionFieldName ) {
    this.actionFieldName = actionFieldName;
  }

  public String getResultFieldName() {
    return resultFieldName;
  }

  public void setResultFieldName( String resultFieldName ) {
    this.resultFieldName = resultFieldName;
  }

  @Override
  public void getFields( RowMetaInterface inputRowMeta, String name, RowMetaInterface[] info, StepMeta nextStep,
      VariableSpace space, Repository repository, IMetaStore metaStore ) throws KettleStepException {
    super.getFields( inputRowMeta, name, info, nextStep, space, repository, metaStore );
    addFieldToRow( inputRowMeta, space.environmentSubstitute( getResultFieldName() ), ValueMetaInterface.TYPE_BOOLEAN );
  }

  @Override
  public String getXML() throws KettleException {
    StringBuilder builder = new StringBuilder();
    builder.append( super.getXML() );
    builder.append( "    " ).append( XMLHandler.addTagValue( "userFieldName", getUserFieldName() ) );
    builder.append( "    " ).append( XMLHandler.addTagValue( "actionFieldName", getActionFieldName() ) );
    builder.append( "    " ).append( XMLHandler.addTagValue( "resultFieldName", getResultFieldName() ) );
    return builder.toString();
  }

  @Override
  public void loadXML( Node stepnode, List<DatabaseMeta> databases, IMetaStore metaStore ) throws KettleXMLException {
    super.loadXML( stepnode, databases, metaStore );
    setUserFieldName( XMLHandler.getTagValue( stepnode, "userFieldName" ) );
    setActionFieldName( XMLHandler.getTagValue( stepnode, "actionFieldName" ) );
    setResultFieldName( XMLHandler.getTagValue( stepnode, "resultFieldName" ) );
  }

  @Override
  public void readRep( Repository rep, IMetaStore metaStore, ObjectId id_step, List<DatabaseMeta> databases )
      throws KettleException {
    super.readRep( rep, metaStore, id_step, databases );
    setUserFieldName( rep.getStepAttributeString( id_step, "userFieldName" ) );
    setActionFieldName( rep.getStepAttributeString( id_step, "actionFieldName" ) );
    setResultFieldName( rep.getStepAttributeString( id_step, "resultFieldName" ) );
  }

  @Override
  public void saveRep( Repository rep, IMetaStore metaStore, ObjectId id_transformation, ObjectId id_step )
      throws KettleException {
    super.saveRep( rep, metaStore, id_transformation, id_step );
    rep.saveStepAttribute( id_transformation, id_step, "userFieldName", getUserFieldName() );
    rep.saveStepAttribute( id_transformation, id_step, "actionFieldName", getActionFieldName() );
    rep.saveStepAttribute( id_transformation, id_step, "resultFieldName", getResultFieldName() );
  }

  @Override
  public StepInterface getStep( StepMeta stepMeta, StepDataInterface stepDataInterface, int copyNr,
      TransMeta transMeta, Trans trans ) {
    return new ZendeskOutputSuspendedUsers( stepMeta, stepDataInterface, copyNr, transMeta, trans );
  }

  @Override
  public StepDataInterface getStepData() {
    return new ZendeskOutputSuspendedUsersData();
  }
}
