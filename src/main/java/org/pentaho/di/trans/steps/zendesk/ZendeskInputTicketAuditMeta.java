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
import org.pentaho.di.core.annotations.Step;
import org.pentaho.di.core.database.DatabaseMeta;
import org.pentaho.di.core.exception.KettleException;
import org.pentaho.di.core.exception.KettlePluginException;
import org.pentaho.di.core.exception.KettleStepException;
import org.pentaho.di.core.exception.KettleXMLException;
import org.pentaho.di.core.row.RowMetaInterface;
import org.pentaho.di.core.row.ValueMetaInterface;
import org.pentaho.di.core.row.value.ValueMetaFactory;
import org.pentaho.di.core.variables.VariableSpace;
import org.pentaho.di.core.xml.XMLHandler;
import org.pentaho.di.i18n.BaseMessages;
import org.pentaho.di.repository.ObjectId;
import org.pentaho.di.repository.Repository;
import org.pentaho.di.trans.Trans;
import org.pentaho.di.trans.TransMeta;
import org.pentaho.di.trans.step.StepDataInterface;
import org.pentaho.di.trans.step.StepIOMeta;
import org.pentaho.di.trans.step.StepIOMetaInterface;
import org.pentaho.di.trans.step.StepInterface;
import org.pentaho.di.trans.step.StepMeta;
import org.pentaho.di.trans.step.StepMetaInterface;
import org.pentaho.di.trans.step.errorhandling.Stream;
import org.pentaho.di.trans.step.errorhandling.StreamIcon;
import org.pentaho.di.trans.step.errorhandling.StreamInterface;
import org.pentaho.di.trans.step.errorhandling.StreamInterface.StreamType;
import org.pentaho.metastore.api.IMetaStore;
import org.w3c.dom.Node;

@Step(  
    id = "ZendeskInputTicketAudit",
    image = "org/pentaho/di/trans/steps/zendesk/zendesk.png",
    i18nPackageName="org.pentaho.di.trans.steps.zendesk",
    name="ZendeskInputTicketAudit.Name",
    description = "ZendeskInputTicketAudit.TooltipDesc",
    categoryDescription="i18n:org.pentaho.di.trans.step:BaseStep.Category.Input"
)
public class ZendeskInputTicketAuditMeta extends ZendeskInputMeta implements StepMetaInterface {

  private static final Class<?> PKG = ZendeskInputTicketAuditMeta.class;

  private String ticketIdFieldname;

  private String auditIdFieldname;
  private String createdTimeFieldname;
  private String organizationIdFieldname;
  private String requesterIdFieldname;
  private String assigneeIdFieldname;
  private String groupIdFieldname;
  private String subjectFieldname;
  private String tagsFieldname;
  private String statusFieldname;
  private String priorityFieldname;
  private String channelFieldname;
  private String typeFieldname;
  private String satisfactionFieldname;

  private String commentIdFieldname;
  private String authorIdFieldname;
  private String publicCommentFieldname;
  private String commentBodyFieldname;
  private String commentHTMLBodyFieldname;
  private String changedToPrivateFieldname;

  private String customFieldFieldname;
  private String customFieldValueFieldname;

  private StepIOMetaInterface ioMeta;
  private String ticketOverviewStepName;
  private String ticketCommentsStepName;
  private String ticketCustomFieldsStepName;
  private StepMeta ticketOverviewStepMeta;
  private StepMeta ticketCustomFieldsStepMeta;
  private StepMeta ticketCommentsStepMeta;
  
  @Override
  public StepInterface getStep( StepMeta stepMeta, StepDataInterface stepDataInterface, int copyNr,
      TransMeta transMeta, Trans trans ) {
    return new ZendeskInputTicketAudit( stepMeta, stepDataInterface, copyNr, transMeta, trans );
  }

  @Override
  public StepDataInterface getStepData() {
    return new ZendeskInputTicketAuditData();
  }

  @Override
  public void setDefault() {
    ticketIdFieldname = null;

    auditIdFieldname = "Audit ID";
    createdTimeFieldname = "Created Time";
    organizationIdFieldname = "Organization ID";
    requesterIdFieldname = "Requester ID";
    assigneeIdFieldname = "Assignee ID";
    groupIdFieldname = "Group ID";
    subjectFieldname = "Subject";
    tagsFieldname = "Tags";
    statusFieldname = "Status";
    priorityFieldname = "Priority";
    channelFieldname = "Channel";
    typeFieldname = "Type";
    satisfactionFieldname = "Satisfaction";

    commentIdFieldname = "Comment ID";
    authorIdFieldname = "Author ID";
    publicCommentFieldname = "Is Public Comment";
    commentBodyFieldname = "Comment Body";
    commentHTMLBodyFieldname = "Comment HTML Body";
    changedToPrivateFieldname = "Was Changed to Private";

    customFieldFieldname = "Custom Field Name";
    customFieldValueFieldname = "Custom Field Value";
  }

  @Override
  public String getXML() throws KettleException {
    StringBuilder builder = new StringBuilder();
    builder.append( super.getXML() );
    builder.append( "    " ).append( XMLHandler.addTagValue( "ticketIdFieldname", getTicketIdFieldname() ) );
    builder.append( "    " ).append( XMLHandler.addTagValue( "auditIdFieldname", getAuditIdFieldname() ) );
    builder.append( "    " ).append( XMLHandler.addTagValue( "createdTimeFieldname", getCreatedTimeFieldname() ) );
    builder.append( "    " ).append( XMLHandler.addTagValue( "organizationIdFieldname", getOrganizationIdFieldname() ) );
    builder.append( "    " ).append( XMLHandler.addTagValue( "requesterIdFieldname", getRequesterIdFieldname() ) );
    builder.append( "    " ).append( XMLHandler.addTagValue( "assigneeIdFieldname", getAssigneeIdFieldname() ) );
    builder.append( "    " ).append( XMLHandler.addTagValue( "groupIdFieldname", getGroupIdFieldname() ) );
    builder.append( "    " ).append( XMLHandler.addTagValue( "subjectFieldname", getSubjectFieldname() ) );
    builder.append( "    " ).append( XMLHandler.addTagValue( "tagsFieldname", getTagsFieldname() ) );
    builder.append( "    " ).append( XMLHandler.addTagValue( "statusFieldname", getStatusFieldname() ) );
    builder.append( "    " ).append( XMLHandler.addTagValue( "priorityFieldname", getPriorityFieldname() ) );
    builder.append( "    " ).append( XMLHandler.addTagValue( "channelFieldname", getChannelFieldname() ) );
    builder.append( "    " ).append( XMLHandler.addTagValue( "typeFieldname", getTypeFieldname() ) );
    builder.append( "    " ).append( XMLHandler.addTagValue( "satisfactionFieldname", getSatisfactionFieldname() ) );
    builder.append( "    " ).append( XMLHandler.addTagValue( "commentIdFieldname", getCommentIdFieldname() ) );
    builder.append( "    " ).append( XMLHandler.addTagValue( "authorIdFieldname", getAuthorIdFieldname() ) );
    builder.append( "    " ).append( XMLHandler.addTagValue( "publicCommentFieldname", getPublicCommentFieldname() ) );
    builder.append( "    " ).append( XMLHandler.addTagValue( "commentBodyFieldname", getCommentBodyFieldname() ) );
    builder.append( "    " ).append( XMLHandler.addTagValue( "commentHTMLBodyFieldname", getCommentHTMLBodyFieldname() ) );
    builder.append( "    " ).append( XMLHandler.addTagValue( "changedToPrivateFieldname", getChangedToPrivateFieldname() ) );
    builder.append( "    " ).append( XMLHandler.addTagValue( "customFieldFieldname", getCustomFieldFieldname() ) );
    builder.append( "    " ).append( XMLHandler.addTagValue( "customFieldValueFieldname", getCustomFieldValueFieldname() ) );
    builder.append( "    " ).append( XMLHandler.addTagValue( "ticketOverviewStepName",
      getTicketOverviewStepMeta() != null ? getTicketOverviewStepMeta().getName() : getTicketOverviewStepName() ) );
    builder.append( "    " ).append( XMLHandler.addTagValue( "ticketCustomFieldsStepName",
      getTicketCustomFieldsStepMeta() != null ? getTicketCustomFieldsStepMeta().getName() : getTicketCustomFieldsStepName() ) );
    builder.append( "    " ).append( XMLHandler.addTagValue( "ticketCommentsStepName",
      getTicketCommentsStepMeta() != null ? getTicketCommentsStepMeta().getName() : getTicketCommentsStepName() ) );
    return builder.toString();
  }

  @Override
  public void loadXML( Node stepnode, List<DatabaseMeta> databases, IMetaStore metaStore ) throws KettleXMLException {
    super.loadXML( stepnode, databases, metaStore );
    setTicketIdFieldname( XMLHandler.getTagValue( stepnode, "ticketIdFieldname" ) );
    setAuditIdFieldname( XMLHandler.getTagValue( stepnode, "auditIdFieldname" ) );
    setCreatedTimeFieldname( XMLHandler.getTagValue( stepnode, "createdTimeFieldname" ) );
    setOrganizationIdFieldname( XMLHandler.getTagValue( stepnode, "organizationIdFieldname" ) );
    setRequesterIdFieldname( XMLHandler.getTagValue( stepnode, "requesterIdFieldname" ) );
    setAssigneeIdFieldname( XMLHandler.getTagValue( stepnode, "assigneeIdFieldname" ) );
    setGroupIdFieldname( XMLHandler.getTagValue( stepnode, "groupIdFieldname" ) );
    setSubjectFieldname( XMLHandler.getTagValue( stepnode, "subjectFieldname" ) );
    setTagsFieldname( XMLHandler.getTagValue( stepnode, "tagsFieldname" ) );
    setStatusFieldname( XMLHandler.getTagValue( stepnode, "statusFieldname" ) );
    setPriorityFieldname( XMLHandler.getTagValue( stepnode, "priorityFieldname" ) );
    setChannelFieldname( XMLHandler.getTagValue( stepnode, "channelFieldname" ) );
    setTypeFieldname( XMLHandler.getTagValue( stepnode, "typeFieldname" ) );
    setSatisfactionFieldname( XMLHandler.getTagValue( stepnode, "satisfactionFieldname" ) );
    setCommentIdFieldname( XMLHandler.getTagValue( stepnode, "commentIdFieldname" ) );
    setAuthorIdFieldname( XMLHandler.getTagValue( stepnode, "authorIdFieldname" ) );
    setPublicCommentFieldname( XMLHandler.getTagValue( stepnode, "publicCommentFieldname" ) );
    setCommentBodyFieldname( XMLHandler.getTagValue( stepnode, "commentBodyFieldname" ) );
    setCommentHTMLBodyFieldname( XMLHandler.getTagValue( stepnode, "commentHTMLBodyFieldname" ) );
    setChangedToPrivateFieldname( XMLHandler.getTagValue( stepnode, "changedToPrivateFieldname" ) );
    setCustomFieldFieldname( XMLHandler.getTagValue( stepnode, "customFieldFieldname" ) );
    setCustomFieldValueFieldname( XMLHandler.getTagValue( stepnode, "customFieldValueFieldname" ) );
    setTicketOverviewStepName( XMLHandler.getTagValue( stepnode, "ticketOverviewStepName" ) );
    setTicketCustomFieldsStepName( XMLHandler.getTagValue( stepnode, "ticketCustomFieldsStepName" ) );
    setTicketCommentsStepName( XMLHandler.getTagValue( stepnode, "ticketCommentsStepName" ) );
  }

  @Override
  public void readRep( Repository rep, IMetaStore metaStore, ObjectId id_step, List<DatabaseMeta> databases )
    throws KettleException {
    super.readRep( rep, metaStore, id_step, databases );
    setTicketIdFieldname( rep.getStepAttributeString( id_step, "ticketIdFieldname" ) );
    setAuditIdFieldname( rep.getStepAttributeString( id_step, "auditIdFieldname" ) );
    setCreatedTimeFieldname( rep.getStepAttributeString( id_step, "createdTimeFieldname" ) );
    setOrganizationIdFieldname( rep.getStepAttributeString( id_step, "organizationIdFieldname" ) );
    setRequesterIdFieldname( rep.getStepAttributeString( id_step, "requesterIdFieldname" ) );
    setAssigneeIdFieldname( rep.getStepAttributeString( id_step, "assigneeIdFieldname" ) );
    setGroupIdFieldname( rep.getStepAttributeString( id_step, "groupIdFieldname" ) );
    setSubjectFieldname( rep.getStepAttributeString( id_step, "subjectFieldname" ) );
    setTagsFieldname( rep.getStepAttributeString( id_step, "tagsFieldname" ) );
    setStatusFieldname( rep.getStepAttributeString( id_step, "statusFieldname" ) );
    setPriorityFieldname( rep.getStepAttributeString( id_step, "priorityFieldname" ) );
    setChannelFieldname( rep.getStepAttributeString( id_step, "channelFieldname" ) );
    setTypeFieldname( rep.getStepAttributeString( id_step, "typeFieldname" ) );
    setSatisfactionFieldname( rep.getStepAttributeString( id_step, "satisfactionFieldname" ) );
    setCommentIdFieldname( rep.getStepAttributeString( id_step, "commentIdFieldname" ) );
    setAuthorIdFieldname( rep.getStepAttributeString( id_step, "authorIdFieldname" ) );
    setPublicCommentFieldname( rep.getStepAttributeString( id_step, "publicCommentFieldname" ) );
    setCommentBodyFieldname( rep.getStepAttributeString( id_step, "commentBodyFieldname" ) );
    setCommentHTMLBodyFieldname( rep.getStepAttributeString( id_step, "commentHTMLBodyFieldname" ) );
    setChangedToPrivateFieldname( rep.getStepAttributeString( id_step, "changedToPrivateFieldname" ) );
    setCustomFieldFieldname( rep.getStepAttributeString( id_step, "customFieldFieldname" ) );
    setCustomFieldValueFieldname( rep.getStepAttributeString( id_step, "customFieldValueFieldname" ) );
    setTicketOverviewStepName( rep.getStepAttributeString( id_step, "ticketOverviewStepName" ) );
    setTicketCustomFieldsStepName( rep.getStepAttributeString( id_step, "ticketCustomFieldsStepName" ) );
    setTicketCommentsStepName( rep.getStepAttributeString( id_step, "ticketCommentsStepName" ) );
  }

  @Override
  public void saveRep( Repository rep, IMetaStore metaStore, ObjectId id_transformation, ObjectId id_step )
    throws KettleException {
    super.saveRep( rep, metaStore, id_transformation, id_step );
    rep.saveStepAttribute( id_transformation, id_step, "ticketIdFieldname", getTicketIdFieldname() );
    rep.saveStepAttribute( id_transformation, id_step, "auditIdFieldname", getAuditIdFieldname() );
    rep.saveStepAttribute( id_transformation, id_step, "createdTimeFieldname", getCreatedTimeFieldname() );
    rep.saveStepAttribute( id_transformation, id_step, "organizationIdFieldname", getOrganizationIdFieldname() );
    rep.saveStepAttribute( id_transformation, id_step, "requesterIdFieldname", getRequesterIdFieldname() );
    rep.saveStepAttribute( id_transformation, id_step, "assigneeIdFieldname", getAssigneeIdFieldname() );
    rep.saveStepAttribute( id_transformation, id_step, "groupIdFieldname", getGroupIdFieldname() );
    rep.saveStepAttribute( id_transformation, id_step, "subjectFieldname", getSubjectFieldname() );
    rep.saveStepAttribute( id_transformation, id_step, "tagsFieldname", getTagsFieldname() );
    rep.saveStepAttribute( id_transformation, id_step, "statusFieldname", getStatusFieldname() );
    rep.saveStepAttribute( id_transformation, id_step, "priorityFieldname", getPriorityFieldname() );
    rep.saveStepAttribute( id_transformation, id_step, "channelFieldname", getChannelFieldname() );
    rep.saveStepAttribute( id_transformation, id_step, "typeFieldname", getTypeFieldname() );
    rep.saveStepAttribute( id_transformation, id_step, "satisfactionFieldname", getSatisfactionFieldname() );
    rep.saveStepAttribute( id_transformation, id_step, "commentIdFieldname", getCommentIdFieldname() );
    rep.saveStepAttribute( id_transformation, id_step, "authorIdFieldname", getAuthorIdFieldname() );
    rep.saveStepAttribute( id_transformation, id_step, "publicCommentFieldname", getPublicCommentFieldname() );
    rep.saveStepAttribute( id_transformation, id_step, "commentBodyFieldname", getCommentBodyFieldname() );
    rep.saveStepAttribute( id_transformation, id_step, "commentHTMLBodyFieldname", getCommentHTMLBodyFieldname() );
    rep.saveStepAttribute( id_transformation, id_step, "changedToPrivateFieldname", getChangedToPrivateFieldname() );
    rep.saveStepAttribute( id_transformation, id_step, "customFieldFieldname", getCustomFieldFieldname() );
    rep.saveStepAttribute( id_transformation, id_step, "customFieldValueFieldname", getCustomFieldValueFieldname() );
    rep.saveStepAttribute( id_transformation, id_step, "ticketOverviewStepName",
      getTicketOverviewStepMeta() != null ? getTicketOverviewStepMeta().getName() : getTicketOverviewStepName() );
    rep.saveStepAttribute( id_transformation, id_step, "ticketCustomFieldsStepName",
      getTicketCustomFieldsStepMeta() != null ? getTicketCustomFieldsStepMeta().getName() : getTicketCustomFieldsStepName() );
    rep.saveStepAttribute( id_transformation, id_step, "ticketCommentsStepName",
      getTicketCommentsStepMeta() != null ? getTicketCommentsStepMeta().getName() : getTicketCommentsStepName() );
  }

  @Override
  public void getFields( RowMetaInterface inputRowMeta, String name, RowMetaInterface[] info, StepMeta nextStep,
      VariableSpace space, Repository repository, IMetaStore metaStore ) throws KettleStepException {
    if ( nextStep != null ) {
      if ( nextStep.equals( ticketOverviewStepMeta ) ) {
        prepareExecutionResultsTicketOverview( inputRowMeta, space );
      } else if ( nextStep.equals( ticketCommentsStepMeta ) ) {
        prepareExecutionResultsTicketComments( inputRowMeta, space );
      } else if ( nextStep.equals( ticketCustomFieldsStepMeta ) ) {
        prepareExecutionResultsTicketCustomFields( inputRowMeta, space );
      }
    }
  }

  private void prepareExecutionResultsTicketOverview( RowMetaInterface inputRowMeta, VariableSpace space ) throws KettleStepException {
    addFieldToRow( inputRowMeta, getTicketIdFieldname(), ValueMetaInterface.TYPE_INTEGER );
    addFieldToRow( inputRowMeta, space.environmentSubstitute( getAuditIdFieldname() ), ValueMetaInterface.TYPE_INTEGER );
    addFieldToRow( inputRowMeta, space.environmentSubstitute( getCreatedTimeFieldname() ), ValueMetaInterface.TYPE_DATE );
    addFieldToRow( inputRowMeta, space.environmentSubstitute( getOrganizationIdFieldname() ), ValueMetaInterface.TYPE_INTEGER );
    addFieldToRow( inputRowMeta, space.environmentSubstitute( getRequesterIdFieldname() ), ValueMetaInterface.TYPE_INTEGER );
    addFieldToRow( inputRowMeta, space.environmentSubstitute( getAssigneeIdFieldname() ), ValueMetaInterface.TYPE_INTEGER );
    addFieldToRow( inputRowMeta, space.environmentSubstitute( getGroupIdFieldname() ), ValueMetaInterface.TYPE_INTEGER );
    addFieldToRow( inputRowMeta, space.environmentSubstitute( getSubjectFieldname() ), ValueMetaInterface.TYPE_STRING );
    addFieldToRow( inputRowMeta, space.environmentSubstitute( getTagsFieldname() ), ValueMetaInterface.TYPE_STRING );
    addFieldToRow( inputRowMeta, space.environmentSubstitute( getStatusFieldname() ), ValueMetaInterface.TYPE_STRING );
    addFieldToRow( inputRowMeta, space.environmentSubstitute( getPriorityFieldname() ), ValueMetaInterface.TYPE_STRING );
    addFieldToRow( inputRowMeta, space.environmentSubstitute( getChannelFieldname() ), ValueMetaInterface.TYPE_STRING );
    addFieldToRow( inputRowMeta, space.environmentSubstitute( getTypeFieldname() ), ValueMetaInterface.TYPE_STRING );
    addFieldToRow( inputRowMeta, space.environmentSubstitute( getSatisfactionFieldname() ), ValueMetaInterface.TYPE_STRING );
  }

  private void prepareExecutionResultsTicketComments( RowMetaInterface inputRowMeta, VariableSpace space ) throws KettleStepException {
    addFieldToRow( inputRowMeta, getTicketIdFieldname(), ValueMetaInterface.TYPE_INTEGER );
    addFieldToRow( inputRowMeta, space.environmentSubstitute( getAuditIdFieldname() ), ValueMetaInterface.TYPE_INTEGER );
    addFieldToRow( inputRowMeta, space.environmentSubstitute( getCommentIdFieldname() ), ValueMetaInterface.TYPE_INTEGER );
    addFieldToRow( inputRowMeta, space.environmentSubstitute( getAuthorIdFieldname() ), ValueMetaInterface.TYPE_INTEGER );
    addFieldToRow( inputRowMeta, space.environmentSubstitute( getPublicCommentFieldname() ), ValueMetaInterface.TYPE_BOOLEAN );
    addFieldToRow( inputRowMeta, space.environmentSubstitute( getCommentBodyFieldname() ), ValueMetaInterface.TYPE_STRING );
    addFieldToRow( inputRowMeta, space.environmentSubstitute( getCommentHTMLBodyFieldname() ), ValueMetaInterface.TYPE_STRING );
    addFieldToRow( inputRowMeta, space.environmentSubstitute( getChangedToPrivateFieldname() ), ValueMetaInterface.TYPE_DATE );
  }

  private void prepareExecutionResultsTicketCustomFields( RowMetaInterface inputRowMeta, VariableSpace space ) throws KettleStepException {
    addFieldToRow( inputRowMeta, getTicketIdFieldname(), ValueMetaInterface.TYPE_INTEGER );
    addFieldToRow( inputRowMeta, space.environmentSubstitute( getAuditIdFieldname() ), ValueMetaInterface.TYPE_INTEGER );
    addFieldToRow( inputRowMeta, space.environmentSubstitute( getCustomFieldFieldname() ), ValueMetaInterface.TYPE_STRING );
    addFieldToRow( inputRowMeta, space.environmentSubstitute( getCustomFieldValueFieldname() ), ValueMetaInterface.TYPE_STRING );
  }

  protected void addFieldToRow( RowMetaInterface row, String fieldName, int type )
      throws KettleStepException {
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

  @Override
  public StepIOMetaInterface getStepIOMeta() {
    if ( ioMeta == null ) {
      ioMeta = new StepIOMeta( true, true, false, false, true, false );

      ioMeta.addStream( new Stream( StreamType.TARGET, ticketOverviewStepMeta, BaseMessages.getString(
        PKG, "ZendeskInputTicketAuditMeta.TicketOverviewStream.Description" ), StreamIcon.TARGET, null ) );
      ioMeta.addStream( new Stream( StreamType.TARGET, ticketCommentsStepMeta, BaseMessages.getString(
        PKG, "ZendeskInputTicketAuditMeta.TicketCommentStream.Description" ), StreamIcon.TARGET, null ) );
      ioMeta.addStream( new Stream( StreamType.TARGET, ticketCustomFieldsStepMeta, BaseMessages.getString(
        PKG, "ZendeskInputTicketAuditMeta.TicketCustomFieldsStream.Description" ), StreamIcon.TARGET, null ) );
    }
    return ioMeta;
  }

  @Override
  public void searchInfoAndTargetSteps( List<StepMeta> steps ) {
    ticketOverviewStepMeta = StepMeta.findStep( steps, ticketOverviewStepName );
    ticketCommentsStepMeta = StepMeta.findStep( steps, ticketCommentsStepName );
    ticketCustomFieldsStepMeta = StepMeta.findStep( steps, ticketCustomFieldsStepName );
  }

  @Override
  public void handleStreamSelection( StreamInterface stream ) {
    List<StreamInterface> targets = getStepIOMeta().getTargetStreams();
    int index = targets.indexOf( stream );
    StepMeta step = targets.get( index ).getStepMeta();
    switch ( index ) {
      case 0:
        setTicketOverviewStepMeta( step );
        break;
      case 1:
        setTicketCommentsStepMeta( step );
        break;
      case 2:
        setTicketCustomFieldsStepMeta( step );
        break;
      default:
        break;
    }
  }

  public String getTicketIdFieldname() {
    return ticketIdFieldname;
  }

  public void setTicketIdFieldname( String ticketIdFieldname ) {
    this.ticketIdFieldname = ticketIdFieldname;
  }

  public String getAuditIdFieldname() {
    return auditIdFieldname;
  }

  public void setAuditIdFieldname( String auditIdFieldname ) {
    this.auditIdFieldname = auditIdFieldname;
  }

  public String getCreatedTimeFieldname() {
    return createdTimeFieldname;
  }

  public void setCreatedTimeFieldname( String createdTimeFieldname ) {
    this.createdTimeFieldname = createdTimeFieldname;
  }

  public String getOrganizationIdFieldname() {
    return organizationIdFieldname;
  }

  public void setOrganizationIdFieldname( String organizationIdFieldname ) {
    this.organizationIdFieldname = organizationIdFieldname;
  }

  public String getRequesterIdFieldname() {
    return requesterIdFieldname;
  }

  public void setRequesterIdFieldname( String requesterIdFieldname ) {
    this.requesterIdFieldname = requesterIdFieldname;
  }

  public String getAssigneeIdFieldname() {
    return assigneeIdFieldname;
  }

  public void setAssigneeIdFieldname( String assigneeIdFieldname ) {
    this.assigneeIdFieldname = assigneeIdFieldname;
  }

  public String getGroupIdFieldname() {
    return groupIdFieldname;
  }

  public void setGroupIdFieldname( String groupIdFieldname ) {
    this.groupIdFieldname = groupIdFieldname;
  }

  public String getSubjectFieldname() {
    return subjectFieldname;
  }

  public void setSubjectFieldname( String subjectFieldname ) {
    this.subjectFieldname = subjectFieldname;
  }

  public String getTagsFieldname() {
    return tagsFieldname;
  }

  public void setTagsFieldname( String tagsFieldname ) {
    this.tagsFieldname = tagsFieldname;
  }

  public String getStatusFieldname() {
    return statusFieldname;
  }

  public void setStatusFieldname( String statusFieldname ) {
    this.statusFieldname = statusFieldname;
  }

  public String getPriorityFieldname() {
    return priorityFieldname;
  }

  public void setPriorityFieldname( String priorityFieldname ) {
    this.priorityFieldname = priorityFieldname;
  }

  public String getChannelFieldname() {
    return channelFieldname;
  }

  public void setChannelFieldname( String channelFieldname ) {
    this.channelFieldname = channelFieldname;
  }

  public String getTypeFieldname() {
    return typeFieldname;
  }

  public void setTypeFieldname( String typeFieldname ) {
    this.typeFieldname = typeFieldname;
  }

  public String getSatisfactionFieldname() {
    return satisfactionFieldname;
  }

  public void setSatisfactionFieldname( String satisfactionFieldname ) {
    this.satisfactionFieldname = satisfactionFieldname;
  }

  public String getCommentIdFieldname() {
    return commentIdFieldname;
  }

  public void setCommentIdFieldname( String commentIdFieldname ) {
    this.commentIdFieldname = commentIdFieldname;
  }

  public String getAuthorIdFieldname() {
    return authorIdFieldname;
  }

  public void setAuthorIdFieldname( String authorIdFieldname ) {
    this.authorIdFieldname = authorIdFieldname;
  }

  public String getPublicCommentFieldname() {
    return publicCommentFieldname;
  }

  public void setPublicCommentFieldname( String publicCommentFieldname ) {
    this.publicCommentFieldname = publicCommentFieldname;
  }

  public String getCommentBodyFieldname() {
    return commentBodyFieldname;
  }

  public void setCommentBodyFieldname( String commentBodyFieldname ) {
    this.commentBodyFieldname = commentBodyFieldname;
  }

  public String getCommentHTMLBodyFieldname() {
    return commentHTMLBodyFieldname;
  }

  public void setCommentHTMLBodyFieldname( String commentHTMLBodyFieldname ) {
    this.commentHTMLBodyFieldname = commentHTMLBodyFieldname;
  }

  public String getChangedToPrivateFieldname() {
    return changedToPrivateFieldname;
  }

  public void setChangedToPrivateFieldname( String changedToPrivateFieldname ) {
    this.changedToPrivateFieldname = changedToPrivateFieldname;
  }

  public String getCustomFieldFieldname() {
    return customFieldFieldname;
  }

  public void setCustomFieldFieldname( String customFieldFieldname ) {
    this.customFieldFieldname = customFieldFieldname;
  }

  public String getCustomFieldValueFieldname() {
    return customFieldValueFieldname;
  }

  public void setCustomFieldValueFieldname( String customFieldValueFieldname ) {
    this.customFieldValueFieldname = customFieldValueFieldname;
  }

  public String getTicketOverviewStepName() {
    return ticketOverviewStepName;
  }

  public void setTicketOverviewStepName( String ticketOverviewStepName ) {
    this.ticketOverviewStepName = ticketOverviewStepName;
  }

  public String getTicketCommentsStepName() {
    return ticketCommentsStepName;
  }

  public void setTicketCommentsStepName( String ticketCommentsStepName ) {
    this.ticketCommentsStepName = ticketCommentsStepName;
  }

  public String getTicketCustomFieldsStepName() {
    return ticketCustomFieldsStepName;
  }

  public void setTicketCustomFieldsStepName( String ticketCustomFieldsStepName ) {
    this.ticketCustomFieldsStepName = ticketCustomFieldsStepName;
  }

  public StepMeta getTicketOverviewStepMeta() {
    return ticketOverviewStepMeta;
  }

  public void setTicketOverviewStepMeta( StepMeta ticketOverviewStepMeta ) {
    this.ticketOverviewStepMeta = ticketOverviewStepMeta;
  }

  public StepMeta getTicketCustomFieldsStepMeta() {
    return ticketCustomFieldsStepMeta;
  }

  public void setTicketCustomFieldsStepMeta( StepMeta ticketCustomFieldsStepMeta ) {
    this.ticketCustomFieldsStepMeta = ticketCustomFieldsStepMeta;
  }

  public StepMeta getTicketCommentsStepMeta() {
    return ticketCommentsStepMeta;
  }

  public void setTicketCommentsStepMeta( StepMeta ticketCommentsStepMeta ) {
    this.ticketCommentsStepMeta = ticketCommentsStepMeta;
  }

}
