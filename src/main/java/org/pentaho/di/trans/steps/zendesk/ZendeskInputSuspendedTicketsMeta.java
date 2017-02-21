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
    id = "ZendeskInputSuspendedTickets",
    image = "org/pentaho/di/trans/steps/zendesk/zendesk.png",
    i18nPackageName = "org.pentaho.di.trans.steps.zendesk",
    name = "ZendeskInputSuspendedTickets.Name",
    description = "ZendeskInputSuspendedTickets.TooltipDesc",
    categoryDescription = "i18n:org.pentaho.di.trans.step:BaseStep.Category.Input"
  )
public class ZendeskInputSuspendedTicketsMeta extends ZendeskInputMeta {

  private String suspendedTicketIdFieldname;
  private String suspendedTicketUrlFieldname;
  private String authorFieldname;
  private String subjectFieldname;
  private String contentFieldname;
  private String causeFieldname;
  private String messageIdFieldname;
  private String ticketIdFieldname;
  private String recipientFieldname;
  private String createdAtFieldname;
  private String updatedAtFieldname;
  private String viaFieldname;
  private String brandIdFieldname;

  @Override
  public void getFields( RowMetaInterface inputRowMeta, String name, RowMetaInterface[] info, StepMeta nextStep,
      VariableSpace space, Repository repository, IMetaStore metaStore ) throws KettleStepException {
    super.getFields( inputRowMeta, name, info, nextStep, space, repository, metaStore );
    addFieldToRow( inputRowMeta, space.environmentSubstitute( getSuspendedTicketIdFieldname() ), ValueMetaInterface.TYPE_INTEGER );
    addFieldToRow( inputRowMeta, space.environmentSubstitute( getSuspendedTicketUrlFieldname() ), ValueMetaInterface.TYPE_STRING );
    addFieldToRow( inputRowMeta, space.environmentSubstitute( getAuthorFieldname() ), ValueMetaInterface.TYPE_STRING );
    addFieldToRow( inputRowMeta, space.environmentSubstitute( getSubjectFieldname() ), ValueMetaInterface.TYPE_STRING );
    addFieldToRow( inputRowMeta, space.environmentSubstitute( getContentFieldname() ), ValueMetaInterface.TYPE_STRING );
    addFieldToRow( inputRowMeta, space.environmentSubstitute( getCauseFieldname() ), ValueMetaInterface.TYPE_STRING );
    addFieldToRow( inputRowMeta, space.environmentSubstitute( getMessageIdFieldname() ), ValueMetaInterface.TYPE_STRING );
    addFieldToRow( inputRowMeta, space.environmentSubstitute( getTicketIdFieldname() ), ValueMetaInterface.TYPE_INTEGER );
    addFieldToRow( inputRowMeta, space.environmentSubstitute( getRecipientFieldname() ), ValueMetaInterface.TYPE_STRING );
    addFieldToRow( inputRowMeta, space.environmentSubstitute( getCreatedAtFieldname() ), ValueMetaInterface.TYPE_DATE );
    addFieldToRow( inputRowMeta, space.environmentSubstitute( getUpdatedAtFieldname() ), ValueMetaInterface.TYPE_DATE );
    addFieldToRow( inputRowMeta, space.environmentSubstitute( getViaFieldname() ), ValueMetaInterface.TYPE_STRING );
    addFieldToRow( inputRowMeta, space.environmentSubstitute( getBrandIdFieldname() ), ValueMetaInterface.TYPE_INTEGER );
  }

  @Override
  public StepInterface getStep( StepMeta stepMeta, StepDataInterface stepDataInterface, int copyNr,
      TransMeta transMeta, Trans trans ) {
    return new ZendeskInputSuspendedTickets( stepMeta, stepDataInterface, copyNr, transMeta, trans );
  }

  @Override
  public StepDataInterface getStepData() {
    return new ZendeskInputSuspendedTicketsData();
  }

  @Override
  public String getXML() throws KettleException {
    StringBuilder xml = new StringBuilder();
    xml.append( super.getXML() );
    xml.append( "    " ).append( XMLHandler.addTagValue( "suspendedTicketIdFieldname", getSuspendedTicketIdFieldname() ) );
    xml.append( "    " ).append( XMLHandler.addTagValue( "suspendedTicketUrlFieldname", getSuspendedTicketUrlFieldname() ) );
    xml.append( "    " ).append( XMLHandler.addTagValue( "authorFieldname", getAuthorFieldname() ) );
    xml.append( "    " ).append( XMLHandler.addTagValue( "subjectFieldname", getSubjectFieldname() ) );
    xml.append( "    " ).append( XMLHandler.addTagValue( "contentFieldname", getContentFieldname() ) );
    xml.append( "    " ).append( XMLHandler.addTagValue( "causeFieldname", getCauseFieldname() ) );
    xml.append( "    " ).append( XMLHandler.addTagValue( "messageIdFieldname", getMessageIdFieldname() ) );
    xml.append( "    " ).append( XMLHandler.addTagValue( "ticketIdFieldname", getTicketIdFieldname() ) );
    xml.append( "    " ).append( XMLHandler.addTagValue( "recipientFieldname", getRecipientFieldname() ) );
    xml.append( "    " ).append( XMLHandler.addTagValue( "createdAtFieldname", getCreatedAtFieldname() ) );
    xml.append( "    " ).append( XMLHandler.addTagValue( "updatedAtFieldname", getUpdatedAtFieldname() ) );
    xml.append( "    " ).append( XMLHandler.addTagValue( "viaFieldname", getViaFieldname() ) );
    xml.append( "    " ).append( XMLHandler.addTagValue( "brandIdFieldname", getBrandIdFieldname() ) );
    return xml.toString();
  }

  @Override
  public void loadXML( Node stepnode, List<DatabaseMeta> databases, IMetaStore metaStore ) throws KettleXMLException {
    super.loadXML( stepnode, databases, metaStore );
    setSuspendedTicketIdFieldname( XMLHandler.getTagValue( stepnode, "suspendedTicketIdFieldname" ) );
    setSuspendedTicketUrlFieldname( XMLHandler.getTagValue( stepnode, "suspendedTicketUrlFieldname" ) );
    setAuthorFieldname( XMLHandler.getTagValue( stepnode, "authorFieldname" ) );
    setSubjectFieldname( XMLHandler.getTagValue( stepnode, "subjectFieldname" ) );
    setContentFieldname( XMLHandler.getTagValue( stepnode, "contentFieldname" ) );
    setCauseFieldname( XMLHandler.getTagValue( stepnode, "causeFieldname" ) );
    setMessageIdFieldname( XMLHandler.getTagValue( stepnode, "messageIdFieldname" ) );
    setTicketIdFieldname( XMLHandler.getTagValue( stepnode, "ticketIdFieldname" ) );
    setRecipientFieldname( XMLHandler.getTagValue( stepnode, "recipientFieldname" ) );
    setCreatedAtFieldname( XMLHandler.getTagValue( stepnode, "createdAtFieldname" ) );
    setUpdatedAtFieldname( XMLHandler.getTagValue( stepnode, "updatedAtFieldname" ) );
    setViaFieldname( XMLHandler.getTagValue( stepnode, "viaFieldname" ) );
    setBrandIdFieldname( XMLHandler.getTagValue( stepnode, "brandIdFieldname" ) );
  }

  @Override
  public void readRep( Repository rep, IMetaStore metaStore, ObjectId id_step, List<DatabaseMeta> databases )
    throws KettleException {
    super.readRep( rep, metaStore, id_step, databases );
    setSuspendedTicketIdFieldname( rep.getStepAttributeString( id_step, "suspendedTicketIdFieldname" ) );
    setSuspendedTicketUrlFieldname( rep.getStepAttributeString( id_step, "suspendedTicketUrlFieldname" ) );
    setAuthorFieldname( rep.getStepAttributeString( id_step, "authorFieldname" ) );
    setSubjectFieldname( rep.getStepAttributeString( id_step, "subjectFieldname" ) );
    setContentFieldname( rep.getStepAttributeString( id_step, "contentFieldname" ) );
    setCauseFieldname( rep.getStepAttributeString( id_step, "causeFieldname" ) );
    setMessageIdFieldname( rep.getStepAttributeString( id_step, "messageIdFieldname" ) );
    setTicketIdFieldname( rep.getStepAttributeString( id_step, "ticketIdFieldname" ) );
    setRecipientFieldname( rep.getStepAttributeString( id_step, "recipientFieldname" ) );
    setCreatedAtFieldname( rep.getStepAttributeString( id_step, "createdAtFieldname" ) );
    setUpdatedAtFieldname( rep.getStepAttributeString( id_step, "updatedAtFieldname" ) );
    setViaFieldname( rep.getStepAttributeString( id_step, "viaFieldname" ) );
    setBrandIdFieldname( rep.getStepAttributeString( id_step, "brandIdFieldname" ) );
  }

  @Override
  public void saveRep( Repository rep, IMetaStore metaStore, ObjectId id_transformation, ObjectId id_step )
    throws KettleException {
    super.saveRep( rep, metaStore, id_transformation, id_step );
    rep.saveStepAttribute( id_transformation, id_step, "suspendedTicketIdFieldname", getSuspendedTicketIdFieldname() );
    rep.saveStepAttribute( id_transformation, id_step, "suspendedTicketUrlFieldname", getSuspendedTicketUrlFieldname() );
    rep.saveStepAttribute( id_transformation, id_step, "authorFieldname", getAuthorFieldname() );
    rep.saveStepAttribute( id_transformation, id_step, "subjectFieldname", getSubjectFieldname() );
    rep.saveStepAttribute( id_transformation, id_step, "contentFieldname", getContentFieldname() );
    rep.saveStepAttribute( id_transformation, id_step, "causeFieldname", getCauseFieldname() );
    rep.saveStepAttribute( id_transformation, id_step, "messageIdFieldname", getMessageIdFieldname() );
    rep.saveStepAttribute( id_transformation, id_step, "ticketIdFieldname", getTicketIdFieldname() );
    rep.saveStepAttribute( id_transformation, id_step, "recipientFieldname", getRecipientFieldname() );
    rep.saveStepAttribute( id_transformation, id_step, "createdAtFieldname", getCreatedAtFieldname() );
    rep.saveStepAttribute( id_transformation, id_step, "updatedAtFieldname", getUpdatedAtFieldname() );
    rep.saveStepAttribute( id_transformation, id_step, "viaFieldname", getViaFieldname() );
    rep.saveStepAttribute( id_transformation, id_step, "brandIdFieldname", getBrandIdFieldname() );
  }

  @Override
  public void setDefault() {
    super.setDefault();
    setSuspendedTicketIdFieldname( "Suspended_Ticket_ID" );
    setSuspendedTicketUrlFieldname( "Suspended_Ticket_URL" );
    setAuthorFieldname( "Author" );
    setSubjectFieldname( "Subject" );
    setContentFieldname( "Content" );
    setCauseFieldname( "Suspension_Cause" );
    setMessageIdFieldname( "Message_ID" );
    setTicketIdFieldname( "Ticket_ID" );
    setRecipientFieldname( "Recipient" );
    setCreatedAtFieldname( "Created_Time" );
    setUpdatedAtFieldname( "Updated_Time" );
    setViaFieldname( "Channel" );
    setBrandIdFieldname( "Brand_ID" );
  }

  public String getSuspendedTicketIdFieldname() {
    return suspendedTicketIdFieldname;
  }

  public void setSuspendedTicketIdFieldname( String suspendedTicketIdFieldname ) {
    this.suspendedTicketIdFieldname = suspendedTicketIdFieldname;
  }

  public String getSuspendedTicketUrlFieldname() {
    return suspendedTicketUrlFieldname;
  }

  public void setSuspendedTicketUrlFieldname( String suspendedTicketUrlFieldname ) {
    this.suspendedTicketUrlFieldname = suspendedTicketUrlFieldname;
  }

  public String getAuthorFieldname() {
    return authorFieldname;
  }

  public void setAuthorFieldname( String authorFieldname ) {
    this.authorFieldname = authorFieldname;
  }

  public String getSubjectFieldname() {
    return subjectFieldname;
  }

  public void setSubjectFieldname( String subjectFieldname ) {
    this.subjectFieldname = subjectFieldname;
  }

  public String getContentFieldname() {
    return contentFieldname;
  }

  public void setContentFieldname( String contentFieldname ) {
    this.contentFieldname = contentFieldname;
  }

  public String getCauseFieldname() {
    return causeFieldname;
  }

  public void setCauseFieldname( String causeFieldname ) {
    this.causeFieldname = causeFieldname;
  }

  public String getMessageIdFieldname() {
    return messageIdFieldname;
  }

  public void setMessageIdFieldname( String messageIdFieldname ) {
    this.messageIdFieldname = messageIdFieldname;
  }

  public String getTicketIdFieldname() {
    return ticketIdFieldname;
  }

  public void setTicketIdFieldname( String ticketIdFieldname ) {
    this.ticketIdFieldname = ticketIdFieldname;
  }

  public String getRecipientFieldname() {
    return recipientFieldname;
  }

  public void setRecipientFieldname( String recipientFieldname ) {
    this.recipientFieldname = recipientFieldname;
  }

  public String getCreatedAtFieldname() {
    return createdAtFieldname;
  }

  public void setCreatedAtFieldname( String createdAtFieldname ) {
    this.createdAtFieldname = createdAtFieldname;
  }

  public String getUpdatedAtFieldname() {
    return updatedAtFieldname;
  }

  public void setUpdatedAtFieldname( String updatedAtFieldname ) {
    this.updatedAtFieldname = updatedAtFieldname;
  }

  public String getViaFieldname() {
    return viaFieldname;
  }

  public void setViaFieldname( String viaFieldname ) {
    this.viaFieldname = viaFieldname;
  }

  public String getBrandIdFieldname() {
    return brandIdFieldname;
  }

  public void setBrandIdFieldname( String brandIdFieldname ) {
    this.brandIdFieldname = brandIdFieldname;
  }
}
