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
    id = "ZendeskInputTicketFields",
    image = "org/pentaho/di/trans/steps/zendesk/zendesk.png",
    i18nPackageName = "org.pentaho.di.trans.steps.zendesk",
    name = "ZendeskInputTicketFields.Name",
    description = "ZendeskInputTicketFields.TooltipDesc",
    categoryDescription = "i18n:org.pentaho.di.trans.step:BaseStep.Category.Input"
  )
public class ZendeskInputTicketFieldsMeta extends ZendeskInputMeta {

  private String ticketFieldIdFieldname;
  private String ticketFieldUrlFieldname;
  private String ticketFieldTypeFieldname;
  private String ticketFieldTitleFieldname;
  private String ticketFieldActiveFieldname;
  private String ticketFieldRequiredFieldname;
  private String ticketFieldVisibleEndUsersFieldname;
  private String createdAtFieldname;
  private String updatedAtFieldname;

  public String getCreatedAtFieldname() {
    return createdAtFieldname;
  }

  @Override
  public void getFields( RowMetaInterface inputRowMeta, String name, RowMetaInterface[] info, StepMeta nextStep,
      VariableSpace space, Repository repository, IMetaStore metaStore ) throws KettleStepException {
    super.getFields( inputRowMeta, name, info, nextStep, space, repository, metaStore );
    addFieldToRow( inputRowMeta, space.environmentSubstitute( getTicketFieldIdFieldname() ),
      ValueMetaInterface.TYPE_INTEGER );
    addFieldToRow( inputRowMeta, space.environmentSubstitute( getTicketFieldUrlFieldname() ),
      ValueMetaInterface.TYPE_STRING );
    addFieldToRow( inputRowMeta, space.environmentSubstitute( getTicketFieldTypeFieldname() ),
      ValueMetaInterface.TYPE_STRING );
    addFieldToRow( inputRowMeta, space.environmentSubstitute( getTicketFieldTitleFieldname() ),
      ValueMetaInterface.TYPE_STRING );
    addFieldToRow( inputRowMeta, space.environmentSubstitute( getTicketFieldActiveFieldname() ),
      ValueMetaInterface.TYPE_BOOLEAN );
    addFieldToRow( inputRowMeta, space.environmentSubstitute( getTicketFieldRequiredFieldname() ),
      ValueMetaInterface.TYPE_BOOLEAN );
    addFieldToRow( inputRowMeta, space.environmentSubstitute( getTicketFieldVisibleEndUsersFieldname() ),
      ValueMetaInterface.TYPE_BOOLEAN );
    addFieldToRow( inputRowMeta, space.environmentSubstitute( getCreatedAtFieldname() ), ValueMetaInterface.TYPE_DATE );
    addFieldToRow( inputRowMeta, space.environmentSubstitute( getUpdatedAtFieldname() ), ValueMetaInterface.TYPE_DATE );
  }

  @Override
  public StepInterface getStep( StepMeta stepMeta, StepDataInterface stepDataInterface, int copyNr,
      TransMeta transMeta, Trans trans ) {
    return new ZendeskInputTicketFields( stepMeta, stepDataInterface, copyNr, transMeta, trans );
  }

  @Override
  public StepDataInterface getStepData() {
    return new ZendeskInputTicketFieldsData();
  }

  public String getUpdatedAtFieldname() {
    return updatedAtFieldname;
  }

  @Override
  public String getXML() throws KettleException {
    StringBuilder xml = new StringBuilder();
    xml.append( super.getXML() );
    xml.append( "    " ).append( XMLHandler.addTagValue( "ticketFieldIdFieldname", getTicketFieldIdFieldname() ) );
    xml.append( "    " ).append( XMLHandler.addTagValue( "ticketFieldUrlFieldname", getTicketFieldUrlFieldname() ) );
    xml.append( "    " ).append( XMLHandler.addTagValue( "ticketFieldTypeFieldname", getTicketFieldTypeFieldname() ) );
    xml.append( "    " ).append( XMLHandler.addTagValue( "ticketFieldTitleFieldname", getTicketFieldTitleFieldname() ) );
    xml.append( "    " ).append( XMLHandler.addTagValue( "ticketFieldActiveFieldname",
      getTicketFieldActiveFieldname() ) );
    xml.append( "    " ).append( XMLHandler.addTagValue( "ticketFieldRequiredFieldname",
      getTicketFieldRequiredFieldname() ) );
    xml.append( "    " ).append( XMLHandler.addTagValue( "ticketFieldVisibleEndUsersFieldname",
      getTicketFieldVisibleEndUsersFieldname() ) );
    xml.append( "    " ).append( XMLHandler.addTagValue( "createdAtFieldname", getCreatedAtFieldname() ) );
    xml.append( "    " ).append( XMLHandler.addTagValue( "updatedAtFieldname", getUpdatedAtFieldname() ) );
    return xml.toString();
  }

  @Override
  public void loadXML( Node stepnode, List<DatabaseMeta> databases, IMetaStore metaStore ) throws KettleXMLException {
    super.loadXML( stepnode, databases, metaStore );
    setTicketFieldIdFieldname( XMLHandler.getTagValue( stepnode, "ticketFieldIdFieldname" ) );
    setTicketFieldUrlFieldname( XMLHandler.getTagValue( stepnode, "ticketFieldUrlFieldname" ) );
    setTicketFieldTypeFieldname( XMLHandler.getTagValue( stepnode, "ticketFieldTypeFieldname" ) );
    setTicketFieldTitleFieldname( XMLHandler.getTagValue( stepnode, "ticketFieldTitleFieldname" ) );
    setTicketFieldActiveFieldname( XMLHandler.getTagValue( stepnode, "ticketFieldActiveFieldname" ) );
    setTicketFieldRequiredFieldname( XMLHandler.getTagValue( stepnode, "ticketFieldRequiredFieldname" ) );
    setTicketFieldVisibleEndUsersFieldname( XMLHandler.getTagValue( stepnode,
      "ticketFieldVisibleEndUsersFieldname" ) );
    setCreatedAtFieldname( XMLHandler.getTagValue( stepnode, "createdAtFieldname" ) );
    setUpdatedAtFieldname( XMLHandler.getTagValue( stepnode, "updatedAtFieldname" ) );
  }

  @Override
  public void readRep( Repository rep, IMetaStore metaStore, ObjectId id_step, List<DatabaseMeta> databases )
    throws KettleException {
    super.readRep( rep, metaStore, id_step, databases );
    setTicketFieldIdFieldname( rep.getStepAttributeString( id_step, "ticketFieldIdFieldname" ) );
    setTicketFieldUrlFieldname( rep.getStepAttributeString( id_step, "ticketFieldUrlFieldname" ) );
    setTicketFieldTypeFieldname( rep.getStepAttributeString( id_step, "ticketFieldTypeFieldname" ) );
    setTicketFieldTitleFieldname( rep.getStepAttributeString( id_step, "ticketFieldTitleFieldname" ) );
    setTicketFieldActiveFieldname( rep.getStepAttributeString( id_step, "ticketFieldActiveFieldname" ) );
    setTicketFieldRequiredFieldname( rep.getStepAttributeString( id_step, "ticketFieldRequiredFieldname" ) );
    setTicketFieldVisibleEndUsersFieldname( rep.getStepAttributeString( id_step,
      "ticketFieldVisibleEndUsersFieldname" ) );
    setCreatedAtFieldname( rep.getStepAttributeString( id_step, "createdAtFieldname" ) );
    setUpdatedAtFieldname( rep.getStepAttributeString( id_step, "updatedAtFieldname" ) );
  }

  @Override
  public void saveRep( Repository rep, IMetaStore metaStore, ObjectId id_transformation, ObjectId id_step )
    throws KettleException {
    super.saveRep( rep, metaStore, id_transformation, id_step );
    rep.saveStepAttribute( id_transformation, id_step, "ticketFieldIdFieldname", getTicketFieldIdFieldname() );
    rep.saveStepAttribute( id_transformation, id_step, "ticketFieldUrlFieldname", getTicketFieldUrlFieldname() );
    rep.saveStepAttribute( id_transformation, id_step, "ticketFieldTypeFieldname", getTicketFieldTypeFieldname() );
    rep.saveStepAttribute( id_transformation, id_step, "ticketFieldTitleFieldname", getTicketFieldTitleFieldname() );
    rep.saveStepAttribute( id_transformation, id_step, "ticketFieldActiveFieldname", getTicketFieldActiveFieldname() );
    rep.saveStepAttribute( id_transformation, id_step, "ticketFieldRequiredFieldname",
      getTicketFieldRequiredFieldname() );
    rep.saveStepAttribute( id_transformation, id_step, "ticketFieldVisibleEndUsersFieldname",
      getTicketFieldVisibleEndUsersFieldname() );
    rep.saveStepAttribute( id_transformation, id_step, "createdAtFieldname", getCreatedAtFieldname() );
    rep.saveStepAttribute( id_transformation, id_step, "updatedAtFieldname", getUpdatedAtFieldname() );
  }

  public void setCreatedAtFieldname( String createdAtFieldname ) {
    this.createdAtFieldname = createdAtFieldname;
  }

  @Override
  public void setDefault() {
    super.setDefault();
    setTicketFieldIdFieldname( "field_id" );
    setTicketFieldUrlFieldname( "field_url" );
    setTicketFieldTypeFieldname( "field_type" );
    setTicketFieldTitleFieldname( "field_title" );
    setTicketFieldActiveFieldname( "field_active" );
    setTicketFieldRequiredFieldname( "field_required" );
    setTicketFieldVisibleEndUsersFieldname( "field_visible_endusers" );
    setCreatedAtFieldname( "Created_Time" );
    setUpdatedAtFieldname( "Updated_Time" );
  }

  public void setUpdatedAtFieldname( String updatedAtFieldname ) {
    this.updatedAtFieldname = updatedAtFieldname;
  }

  public String getTicketFieldIdFieldname() {
    return ticketFieldIdFieldname;
  }

  public void setTicketFieldIdFieldname( String ticketFieldIdFieldname ) {
    this.ticketFieldIdFieldname = ticketFieldIdFieldname;
  }

  public String getTicketFieldUrlFieldname() {
    return ticketFieldUrlFieldname;
  }

  public void setTicketFieldUrlFieldname( String ticketFieldUrlFieldname ) {
    this.ticketFieldUrlFieldname = ticketFieldUrlFieldname;
  }

  public String getTicketFieldTypeFieldname() {
    return ticketFieldTypeFieldname;
  }

  public void setTicketFieldTypeFieldname( String ticketFieldTypeFieldname ) {
    this.ticketFieldTypeFieldname = ticketFieldTypeFieldname;
  }

  public String getTicketFieldTitleFieldname() {
    return ticketFieldTitleFieldname;
  }

  public void setTicketFieldTitleFieldname( String ticketFieldTitleFieldname ) {
    this.ticketFieldTitleFieldname = ticketFieldTitleFieldname;
  }

  public String getTicketFieldActiveFieldname() {
    return ticketFieldActiveFieldname;
  }

  public void setTicketFieldActiveFieldname( String ticketFieldActiveFieldname ) {
    this.ticketFieldActiveFieldname = ticketFieldActiveFieldname;
  }

  public String getTicketFieldRequiredFieldname() {
    return ticketFieldRequiredFieldname;
  }

  public void setTicketFieldRequiredFieldname( String ticketFieldRequiredFieldname ) {
    this.ticketFieldRequiredFieldname = ticketFieldRequiredFieldname;
  }

  public String getTicketFieldVisibleEndUsersFieldname() {
    return ticketFieldVisibleEndUsersFieldname;
  }

  public void setTicketFieldVisibleEndUsersFieldname( String ticketFieldVisibleEndUsersFieldname ) {
    this.ticketFieldVisibleEndUsersFieldname = ticketFieldVisibleEndUsersFieldname;
  }

}
