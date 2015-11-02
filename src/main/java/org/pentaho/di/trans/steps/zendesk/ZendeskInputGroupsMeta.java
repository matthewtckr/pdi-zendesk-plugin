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
    id = "ZendeskInputGroups",
    image = "org/pentaho/di/trans/steps/zendesk/zendesk.png",
    i18nPackageName="org.pentaho.di.trans.steps.zendesk",
    name="ZendeskInputGroups.Name",
    description = "ZendeskInputGroups.TooltipDesc",
    categoryDescription="i18n:org.pentaho.di.trans.step:BaseStep.Category.Input"
)
public class ZendeskInputGroupsMeta extends ZendeskInputMeta {

  private String groupIdFieldname;
  private String groupUrlFieldname;
  private String groupNameFieldname;
  private String deletedFieldname;
  private String createdAtFieldname;
  private String updatedAtFieldname;

  public String getCreatedAtFieldname() {
    return createdAtFieldname;
  }

  @Override
  public void getFields( RowMetaInterface inputRowMeta, String name, RowMetaInterface[] info, StepMeta nextStep,
      VariableSpace space, Repository repository, IMetaStore metaStore ) throws KettleStepException {
    super.getFields( inputRowMeta, name, info, nextStep, space, repository, metaStore );
    addFieldToRow( inputRowMeta, space.environmentSubstitute( getGroupIdFieldname() ), ValueMetaInterface.TYPE_INTEGER );
    addFieldToRow( inputRowMeta, space.environmentSubstitute( getGroupUrlFieldname() ), ValueMetaInterface.TYPE_STRING );
    addFieldToRow( inputRowMeta, space.environmentSubstitute( getGroupNameFieldname() ), ValueMetaInterface.TYPE_STRING );
    addFieldToRow( inputRowMeta, space.environmentSubstitute( getDeletedFieldname() ), ValueMetaInterface.TYPE_BOOLEAN );
    addFieldToRow( inputRowMeta, space.environmentSubstitute( getCreatedAtFieldname() ), ValueMetaInterface.TYPE_DATE );
    addFieldToRow( inputRowMeta, space.environmentSubstitute( getUpdatedAtFieldname() ), ValueMetaInterface.TYPE_DATE );
  }

  public String getGroupIdFieldname() {
    return groupIdFieldname;
  }

  @Override
  public StepInterface getStep( StepMeta stepMeta, StepDataInterface stepDataInterface, int copyNr,
      TransMeta transMeta, Trans trans ) {
    return new ZendeskInputGroups( stepMeta, stepDataInterface, copyNr, transMeta, trans );
  }

  @Override
  public StepDataInterface getStepData() {
    return new ZendeskInputGroupsData();
  }

  public String getUpdatedAtFieldname() {
    return updatedAtFieldname;
  }

  @Override
  public String getXML() throws KettleException {
    StringBuilder xml = new StringBuilder();
    xml.append( super.getXML() );
    xml.append( "    " ).append( XMLHandler.addTagValue( "groupIdFieldname", getGroupIdFieldname() ) );
    xml.append( "    " ).append( XMLHandler.addTagValue( "groupUrlFieldname", getGroupUrlFieldname() ) );
    xml.append( "    " ).append( XMLHandler.addTagValue( "groupNameFieldname", getGroupNameFieldname() ) );
    xml.append( "    " ).append( XMLHandler.addTagValue( "deletedFieldname", getDeletedFieldname() ) );
    xml.append( "    " ).append( XMLHandler.addTagValue( "createdAtFieldname", getCreatedAtFieldname() ) );
    xml.append( "    " ).append( XMLHandler.addTagValue( "updatedAtFieldname", getUpdatedAtFieldname() ) );
    return xml.toString();
  }

  @Override
  public void loadXML( Node stepnode, List<DatabaseMeta> databases, IMetaStore metaStore ) throws KettleXMLException {
    super.loadXML( stepnode, databases, metaStore );
    setGroupIdFieldname( XMLHandler.getTagValue( stepnode, "groupIdFieldname" ) );
    setGroupUrlFieldname( XMLHandler.getTagValue( stepnode, "groupUrlFieldname" ) );
    setGroupNameFieldname( XMLHandler.getTagValue( stepnode, "groupNameFieldname" ) );
    setDeletedFieldname( XMLHandler.getTagValue( stepnode, "deletedFieldname" ) );
    setCreatedAtFieldname( XMLHandler.getTagValue( stepnode, "createdAtFieldname" ) );
    setUpdatedAtFieldname( XMLHandler.getTagValue( stepnode, "updatedAtFieldname" ) );
  }

  @Override
  public void readRep( Repository rep, IMetaStore metaStore, ObjectId id_step, List<DatabaseMeta> databases )
    throws KettleException {
    super.readRep( rep, metaStore, id_step, databases );
    setGroupIdFieldname( rep.getStepAttributeString( id_step, "groupIdFieldname" ) );
    setGroupUrlFieldname( rep.getStepAttributeString( id_step, "groupUrlFieldname" ) );
    setGroupNameFieldname( rep.getStepAttributeString( id_step, "groupNameFieldname" ) );
    setDeletedFieldname( rep.getStepAttributeString( id_step, "deletedFieldname" ) );
    setCreatedAtFieldname( rep.getStepAttributeString( id_step, "createdAtFieldname" ) );
    setUpdatedAtFieldname( rep.getStepAttributeString( id_step, "updatedAtFieldname" ) );
  }

  @Override
  public void saveRep( Repository rep, IMetaStore metaStore, ObjectId id_transformation, ObjectId id_step )
    throws KettleException {
    super.saveRep( rep, metaStore, id_transformation, id_step );
    rep.saveStepAttribute( id_transformation, id_step, "groupIdFieldname", getGroupIdFieldname() );
    rep.saveStepAttribute( id_transformation, id_step, "groupUrlFieldname", getGroupUrlFieldname() );
    rep.saveStepAttribute( id_transformation, id_step, "groupNameFieldname", getGroupNameFieldname() );
    rep.saveStepAttribute( id_transformation, id_step, "deletedFieldname", getDeletedFieldname() );
    rep.saveStepAttribute( id_transformation, id_step, "createdAtFieldname", getCreatedAtFieldname() );
    rep.saveStepAttribute( id_transformation, id_step, "updatedAtFieldname", getUpdatedAtFieldname() );
    
  }

  public void setCreatedAtFieldname( String createdAtFieldname ) {
    this.createdAtFieldname = createdAtFieldname;
  }

  @Override
  public void setDefault() {
    super.setDefault();
    setGroupIdFieldname( "Group_ID" );
    setGroupUrlFieldname( "Group_URL" );
    setGroupNameFieldname( "Group_Name" );
    setDeletedFieldname( "Is_Deleted" );
    setCreatedAtFieldname( "Created_Time" );
    setUpdatedAtFieldname( "Updated_Time");
  }

  public void setGroupIdFieldname( String groupIdFieldname ) {
    this.groupIdFieldname = groupIdFieldname;
  }

  public void setUpdatedAtFieldname( String updatedAtFieldname ) {
    this.updatedAtFieldname = updatedAtFieldname;
  }

  public String getGroupUrlFieldname() {
    return groupUrlFieldname;
  }

  public void setGroupUrlFieldname( String groupUrlFieldname ) {
    this.groupUrlFieldname = groupUrlFieldname;
  }

  public String getGroupNameFieldname() {
    return groupNameFieldname;
  }

  public void setGroupNameFieldname( String groupNameFieldname ) {
    this.groupNameFieldname = groupNameFieldname;
  }

  public String getDeletedFieldname() {
    return deletedFieldname;
  }

  public void setDeletedFieldname( String deletedFieldname ) {
    this.deletedFieldname = deletedFieldname;
  }

}
