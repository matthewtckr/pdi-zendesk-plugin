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
    id = "ZendeskInputGroupMemberships",
    image = "org/pentaho/di/trans/steps/zendesk/zendesk.png",
    i18nPackageName = "org.pentaho.di.trans.steps.zendesk",
    name = "ZendeskInputGroupMemberships.Name",
    description = "ZendeskInputGroupMemberships.TooltipDesc",
    categoryDescription = "i18n:org.pentaho.di.trans.step:BaseStep.Category.Input"
  )
public class ZendeskInputGroupMembershipsMeta extends ZendeskInputMeta {

  private String groupMembershipIdFieldname;
  private String groupMembershipUrlFieldname;
  private String userIdFieldname;
  private String groupIdFieldname;
  private String createdAtFieldname;
  private String updatedAtFieldname;
  private String defaultGroupFieldname;

  public String getCreatedAtFieldname() {
    return createdAtFieldname;
  }

  public String getDefaultGroupFieldname() {
    return defaultGroupFieldname;
  }

  @Override
  public void getFields( RowMetaInterface inputRowMeta, String name, RowMetaInterface[] info, StepMeta nextStep,
      VariableSpace space, Repository repository, IMetaStore metaStore ) throws KettleStepException {
    super.getFields( inputRowMeta, name, info, nextStep, space, repository, metaStore );
    addFieldToRow( inputRowMeta, space.environmentSubstitute( getGroupMembershipIdFieldname() ), ValueMetaInterface.TYPE_INTEGER );
    addFieldToRow( inputRowMeta, space.environmentSubstitute( getGroupMembershipUrlFieldname() ), ValueMetaInterface.TYPE_STRING );
    addFieldToRow( inputRowMeta, space.environmentSubstitute( getUserIdFieldname() ), ValueMetaInterface.TYPE_INTEGER );
    addFieldToRow( inputRowMeta, space.environmentSubstitute( getGroupIdFieldname() ), ValueMetaInterface.TYPE_INTEGER );
    addFieldToRow( inputRowMeta, space.environmentSubstitute( getCreatedAtFieldname() ), ValueMetaInterface.TYPE_DATE );
    addFieldToRow( inputRowMeta, space.environmentSubstitute( getUpdatedAtFieldname() ), ValueMetaInterface.TYPE_DATE );
    addFieldToRow( inputRowMeta, space.environmentSubstitute( getDefaultGroupFieldname() ), ValueMetaInterface.TYPE_BOOLEAN );
  }

  public String getGroupIdFieldname() {
    return groupIdFieldname;
  }

  public String getGroupMembershipIdFieldname() {
    return groupMembershipIdFieldname;
  }

  public String getGroupMembershipUrlFieldname() {
    return groupMembershipUrlFieldname;
  }

  @Override
  public StepInterface getStep( StepMeta stepMeta, StepDataInterface stepDataInterface, int copyNr,
      TransMeta transMeta, Trans trans ) {
    return new ZendeskInputGroupMemberships( stepMeta, stepDataInterface, copyNr, transMeta, trans );
  }

  @Override
  public StepDataInterface getStepData() {
    return new ZendeskInputGroupMembershipsData();
  }

  public String getUpdatedAtFieldname() {
    return updatedAtFieldname;
  }

  public String getUserIdFieldname() {
    return userIdFieldname;
  }

  @Override
  public String getXML() throws KettleException {
    StringBuilder xml = new StringBuilder();
    xml.append( super.getXML() );
    xml.append( "    " ).append( XMLHandler.addTagValue( "groupMembershipIdFieldname", getGroupMembershipIdFieldname() ) );
    xml.append( "    " ).append( XMLHandler.addTagValue( "groupMembershipUrlFieldname", getGroupMembershipUrlFieldname() ) );
    xml.append( "    " ).append( XMLHandler.addTagValue( "userIdFieldname", getUserIdFieldname() ) );
    xml.append( "    " ).append( XMLHandler.addTagValue( "groupIdFieldname", getGroupIdFieldname() ) );
    xml.append( "    " ).append( XMLHandler.addTagValue( "createdAtFieldname", getCreatedAtFieldname() ) );
    xml.append( "    " ).append( XMLHandler.addTagValue( "updatedAtFieldname", getUpdatedAtFieldname() ) );
    xml.append( "    " ).append( XMLHandler.addTagValue( "defaultGroupFieldname", getDefaultGroupFieldname() ) );
    return xml.toString();
  }

  @Override
  public void loadXML( Node stepnode, List<DatabaseMeta> databases, IMetaStore metaStore ) throws KettleXMLException {
    super.loadXML( stepnode, databases, metaStore );
    setGroupMembershipIdFieldname( XMLHandler.getTagValue( stepnode, "groupMembershipIdFieldname" ) );
    setGroupMembershipUrlFieldname( XMLHandler.getTagValue( stepnode, "groupMembershipUrlFieldname" ) );
    setUserIdFieldname( XMLHandler.getTagValue( stepnode, "userIdFieldname" ) );
    setGroupIdFieldname( XMLHandler.getTagValue( stepnode, "groupIdFieldname" ) );
    setCreatedAtFieldname( XMLHandler.getTagValue( stepnode, "createdAtFieldname" ) );
    setUpdatedAtFieldname( XMLHandler.getTagValue( stepnode, "updatedAtFieldname" ) );
    setDefaultGroupFieldname( XMLHandler.getTagValue( stepnode, "defaultGroupFieldname" ) );
  }

  @Override
  public void readRep( Repository rep, IMetaStore metaStore, ObjectId id_step, List<DatabaseMeta> databases )
    throws KettleException {
    super.readRep( rep, metaStore, id_step, databases );
    setGroupMembershipIdFieldname( rep.getStepAttributeString( id_step, "groupMembershipIdFieldname" ) );
    setGroupMembershipUrlFieldname( rep.getStepAttributeString( id_step, "groupMembershipUrlFieldname" ) );
    setUserIdFieldname( rep.getStepAttributeString( id_step, "userIdFieldname" ) );
    setGroupIdFieldname( rep.getStepAttributeString( id_step, "groupIdFieldname" ) );
    setCreatedAtFieldname( rep.getStepAttributeString( id_step, "createdAtFieldname" ) );
    setUpdatedAtFieldname( rep.getStepAttributeString( id_step, "updatedAtFieldname" ) );
    setDefaultGroupFieldname( rep.getStepAttributeString( id_step, "defaultGroupFieldname" ) );
  }

  @Override
  public void saveRep( Repository rep, IMetaStore metaStore, ObjectId id_transformation, ObjectId id_step )
    throws KettleException {
    // TODO Auto-generated method stub
    super.saveRep( rep, metaStore, id_transformation, id_step );
    rep.saveStepAttribute( id_transformation, id_step, "groupMembershipIdFieldname", getGroupMembershipIdFieldname() );
    rep.saveStepAttribute( id_transformation, id_step, "groupMembershipUrlFieldname", getGroupMembershipUrlFieldname() );
    rep.saveStepAttribute( id_transformation, id_step, "userIdFieldname", getUserIdFieldname() );
    rep.saveStepAttribute( id_transformation, id_step, "groupIdFieldname", getGroupIdFieldname() );
    rep.saveStepAttribute( id_transformation, id_step, "createdAtFieldname", getCreatedAtFieldname() );
    rep.saveStepAttribute( id_transformation, id_step, "updatedAtFieldname", getUpdatedAtFieldname() );
    rep.saveStepAttribute( id_transformation, id_step, "defaultGroupFieldname", getDefaultGroupFieldname() );
  }

  public void setCreatedAtFieldname( String createdAtFieldname ) {
    this.createdAtFieldname = createdAtFieldname;
  }

  @Override
  public void setDefault() {
    super.setDefault();
    setGroupMembershipIdFieldname( "Group_Membership_ID" );
    setGroupMembershipUrlFieldname( "Group_Membership_URL" );
    setUserIdFieldname( "User_ID" );
    setGroupIdFieldname( "Group_ID" );
    setCreatedAtFieldname( "Created_Time" );
    setUpdatedAtFieldname( "Updated_Time" );
    setDefaultGroupFieldname( "Is_Default_Group" );
  }

  public void setDefaultGroupFieldname( String defaultGroupFieldname ) {
    this.defaultGroupFieldname = defaultGroupFieldname;
  }

  public void setGroupIdFieldname( String groupIdFieldname ) {
    this.groupIdFieldname = groupIdFieldname;
  }

  public void setGroupMembershipIdFieldname( String groupMembershipIdFieldname ) {
    this.groupMembershipIdFieldname = groupMembershipIdFieldname;
  }

  public void setGroupMembershipUrlFieldname( String groupMembershipUrlFieldname ) {
    this.groupMembershipUrlFieldname = groupMembershipUrlFieldname;
  }

  public void setUpdatedAtFieldname( String updatedAtFieldname ) {
    this.updatedAtFieldname = updatedAtFieldname;
  }

  public void setUserIdFieldname( String userIdFieldname ) {
    this.userIdFieldname = userIdFieldname;
  }

}
