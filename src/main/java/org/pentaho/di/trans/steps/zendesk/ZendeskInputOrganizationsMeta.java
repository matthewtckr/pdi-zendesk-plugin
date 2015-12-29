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
import org.pentaho.di.trans.step.errorhandling.Stream;
import org.pentaho.di.trans.step.errorhandling.StreamIcon;
import org.pentaho.di.trans.step.errorhandling.StreamInterface;
import org.pentaho.di.trans.step.errorhandling.StreamInterface.StreamType;
import org.pentaho.metastore.api.IMetaStore;
import org.w3c.dom.Node;

@Step(  
    id = "ZendeskInputOrganizations",
    image = "org/pentaho/di/trans/steps/zendesk/zendesk.png",
    i18nPackageName="org.pentaho.di.trans.steps.zendesk",
    name="ZendeskInputOrganizations.Name",
    description = "ZendeskInputOrganizations.TooltipDesc",
    categoryDescription="i18n:org.pentaho.di.trans.step:BaseStep.Category.Input"
)
public class ZendeskInputOrganizationsMeta extends ZendeskInputMeta {

  private static final Class<?> PKG = ZendeskInputOrganizationsMeta.class;

  private String incomingFieldname;

  private String organizationIdFieldname;
  private String urlFieldname;
  private String externalIdFieldname;
  private String nameFieldname;
  private String createdAtFieldname;
  private String updatedAtFieldname;
  private String detailsFieldname;
  private String notesFieldname;
  private String groupIdFieldname;
  private String sharedTicketsFieldname;
  private String sharedCommentsFieldname;

  private String tagFieldname;

  private String domainNameFieldname;

  private String orgFieldNameFieldname;
  private String orgFieldValueFieldname;

  private StepIOMetaInterface ioMeta;
  private String organizationStepName;
  private String organizationTagStepName;
  private String organizationFieldStepName;
  private String organizationDomainStepName; 

  private StepMeta organizationStepMeta;
  private StepMeta organizationTagStepMeta;
  private StepMeta organizationFieldStepMeta;
  private StepMeta organizationDomainStepMeta;

  @Override
  public void getFields( RowMetaInterface inputRowMeta, String name, RowMetaInterface[] info, StepMeta nextStep,
      VariableSpace space, Repository repository, IMetaStore metaStore ) throws KettleStepException {
    super.getFields( inputRowMeta, name, info, nextStep, space, repository, metaStore );
    if ( nextStep != null ) {
      if ( nextStep.equals( organizationStepMeta ) ) {
        prepareExecutionResultsOrganization( inputRowMeta, space );
      } else if ( nextStep.equals( organizationTagStepMeta ) ) {
        prepareExecutionResultsOrganizationTag( inputRowMeta, space );
      } else if ( nextStep.equals( organizationFieldStepMeta ) ) {
        prepareExecutionResultsOrganizationField( inputRowMeta, space );
      } else if ( nextStep.equals( organizationDomainStepMeta ) ) {
        prepareExecutionResultsOrganizationDomain( inputRowMeta, space );
      }
    }
  }

  private void prepareExecutionResultsOrganization( RowMetaInterface inputRowMeta, VariableSpace space ) throws KettleStepException {
    inputRowMeta.clear();
    addFieldToRow( inputRowMeta, space.environmentSubstitute( getOrganizationIdFieldname() ), ValueMetaInterface.TYPE_INTEGER );
    addFieldToRow( inputRowMeta, space.environmentSubstitute( getUrlFieldname() ), ValueMetaInterface.TYPE_STRING );
    addFieldToRow( inputRowMeta, space.environmentSubstitute( getExternalIdFieldname() ), ValueMetaInterface.TYPE_STRING );
    addFieldToRow( inputRowMeta, space.environmentSubstitute( getNameFieldname() ), ValueMetaInterface.TYPE_STRING );
    addFieldToRow( inputRowMeta, space.environmentSubstitute( getDetailsFieldname() ), ValueMetaInterface.TYPE_STRING );
    addFieldToRow( inputRowMeta, space.environmentSubstitute( getNotesFieldname() ), ValueMetaInterface.TYPE_STRING );
    addFieldToRow( inputRowMeta, space.environmentSubstitute( getGroupIdFieldname() ), ValueMetaInterface.TYPE_INTEGER );
    addFieldToRow( inputRowMeta, space.environmentSubstitute( getSharedTicketsFieldname() ), ValueMetaInterface.TYPE_BOOLEAN );
    addFieldToRow( inputRowMeta, space.environmentSubstitute( getSharedCommentsFieldname() ), ValueMetaInterface.TYPE_BOOLEAN );
    addFieldToRow( inputRowMeta, space.environmentSubstitute( getCreatedAtFieldname() ), ValueMetaInterface.TYPE_DATE );
    addFieldToRow( inputRowMeta, space.environmentSubstitute( getUpdatedAtFieldname() ), ValueMetaInterface.TYPE_DATE );
  }

  private void prepareExecutionResultsOrganizationTag( RowMetaInterface inputRowMeta, VariableSpace space ) throws KettleStepException {
    inputRowMeta.clear();
    addFieldToRow( inputRowMeta, space.environmentSubstitute( getOrganizationIdFieldname() ), ValueMetaInterface.TYPE_INTEGER );
    addFieldToRow( inputRowMeta, space.environmentSubstitute( getTagFieldname() ), ValueMetaInterface.TYPE_STRING );
  }

  private void prepareExecutionResultsOrganizationField( RowMetaInterface inputRowMeta, VariableSpace space ) throws KettleStepException {
    inputRowMeta.clear();
    addFieldToRow( inputRowMeta, space.environmentSubstitute( getOrganizationIdFieldname() ), ValueMetaInterface.TYPE_INTEGER );
    addFieldToRow( inputRowMeta, space.environmentSubstitute( getOrgFieldNameFieldname() ), ValueMetaInterface.TYPE_STRING );
    addFieldToRow( inputRowMeta, space.environmentSubstitute( getOrgFieldValueFieldname() ), ValueMetaInterface.TYPE_STRING );
  }

  private void prepareExecutionResultsOrganizationDomain( RowMetaInterface inputRowMeta, VariableSpace space ) throws KettleStepException {
    inputRowMeta.clear();
    addFieldToRow( inputRowMeta, space.environmentSubstitute( getOrganizationIdFieldname() ), ValueMetaInterface.TYPE_INTEGER );
    addFieldToRow( inputRowMeta, space.environmentSubstitute( getDomainNameFieldname() ), ValueMetaInterface.TYPE_STRING );
  }

  @Override
  public StepIOMetaInterface getStepIOMeta() {
    if ( ioMeta == null ) {
      ioMeta = new StepIOMeta( true, true, false, false, true, false );

      ioMeta.addStream( new Stream( StreamType.TARGET, organizationStepMeta, BaseMessages.getString(
        PKG, "ZendeskInputOrganizationsMeta.OrganizationOverviewStream.Description" ), StreamIcon.TARGET, null ) );
      ioMeta.addStream( new Stream( StreamType.TARGET, organizationTagStepMeta, BaseMessages.getString(
        PKG, "ZendeskInputOrganizationsMeta.OrganizationTagStream.Description" ), StreamIcon.TARGET, null ) );
      ioMeta.addStream( new Stream( StreamType.TARGET, organizationFieldStepMeta, BaseMessages.getString(
        PKG, "ZendeskInputOrganizationsMeta.OrganizationFieldStream.Description" ), StreamIcon.TARGET, null ) );
      ioMeta.addStream( new Stream( StreamType.TARGET, organizationDomainStepMeta, BaseMessages.getString(
        PKG, "ZendeskInputOrganizationsMeta.OrganizationDomainStream.Description" ), StreamIcon.TARGET, null ) );
    }
    return ioMeta;
  }

  @Override
  public void searchInfoAndTargetSteps( List<StepMeta> steps ) {
    organizationStepMeta = StepMeta.findStep( steps, organizationStepName );
    organizationTagStepMeta = StepMeta.findStep( steps, organizationTagStepName );
    organizationFieldStepMeta = StepMeta.findStep( steps, organizationFieldStepName );
    organizationDomainStepMeta = StepMeta.findStep( steps, organizationDomainStepName );
  }

  @Override
  public void handleStreamSelection( StreamInterface stream ) {
    List<StreamInterface> targets = getStepIOMeta().getTargetStreams();
    int index = targets.indexOf( stream );
    StepMeta step = targets.get( index ).getStepMeta();
    switch ( index ) {
      case 0:
        setOrganizationStepMeta( step );
        break;
      case 1:
        setOrganizationTagStepMeta( step );
        break;
      case 2:
        setOrganizationFieldStepMeta( step );
        break;
      case 3:
        setOrganizationDomainStepMeta( step );
        break;
      default:
        break;
    }
  }

  @Override
  public StepInterface getStep( StepMeta stepMeta, StepDataInterface stepDataInterface, int copyNr,
      TransMeta transMeta, Trans trans ) {
    return new ZendeskInputOrganizations( stepMeta, stepDataInterface, copyNr, transMeta, trans );
  }

  @Override
  public StepDataInterface getStepData() {
    return new ZendeskInputOrganizationsData();
  }

  @Override
  public String getXML() throws KettleException {
    StringBuilder xml = new StringBuilder();
    xml.append( super.getXML() );
    xml.append( "    " ).append( XMLHandler.addTagValue( "incomingFieldname", getIncomingFieldname() ) );
    xml.append( "    " ).append( XMLHandler.addTagValue( "organizationIdFieldname", getOrganizationIdFieldname() ) );
    xml.append( "    " ).append( XMLHandler.addTagValue( "urlFieldname", getUrlFieldname() ) );
    xml.append( "    " ).append( XMLHandler.addTagValue( "externalIdFieldname", getExternalIdFieldname() ) );
    xml.append( "    " ).append( XMLHandler.addTagValue( "nameFieldname", getNameFieldname() ) );
    xml.append( "    " ).append( XMLHandler.addTagValue( "createdAtFieldname", getCreatedAtFieldname() ) );
    xml.append( "    " ).append( XMLHandler.addTagValue( "updatedAtFieldname", getUpdatedAtFieldname() ) );
    xml.append( "    " ).append( XMLHandler.addTagValue( "detailsFieldname", getDetailsFieldname() ) );
    xml.append( "    " ).append( XMLHandler.addTagValue( "notesFieldname", getNotesFieldname() ) );
    xml.append( "    " ).append( XMLHandler.addTagValue( "groupIdFieldname", getGroupIdFieldname() ) );
    xml.append( "    " ).append( XMLHandler.addTagValue( "sharedTicketsFieldname", getSharedTicketsFieldname() ) );
    xml.append( "    " ).append( XMLHandler.addTagValue( "sharedCommentsFieldname", getSharedCommentsFieldname() ) );
    xml.append( "    " ).append( XMLHandler.addTagValue( "tagFieldname", getTagFieldname() ) );
    xml.append( "    " ).append( XMLHandler.addTagValue( "domainNameFieldname", getDomainNameFieldname() ) );
    xml.append( "    " ).append( XMLHandler.addTagValue( "orgFieldNameFieldname", getOrgFieldNameFieldname() ) );
    xml.append( "    " ).append( XMLHandler.addTagValue( "orgFieldValueFieldname", getOrgFieldValueFieldname() ) );
    xml.append( "    " ).append( XMLHandler.addTagValue( "organizationStepName",
      getOrganizationStepMeta() != null ? getOrganizationStepMeta().getName() : getOrganizationStepName() ) );
    xml.append( "    " ).append( XMLHandler.addTagValue( "organizationTagStepName",
      getOrganizationTagStepMeta() != null ? getOrganizationTagStepMeta().getName() : getOrganizationTagStepName() ) );
    xml.append( "    " ).append( XMLHandler.addTagValue( "organizationFieldStepName",
      getOrganizationFieldStepMeta() != null ? getOrganizationFieldStepMeta().getName() : getOrganizationFieldStepName() ) );
    xml.append( "    " ).append( XMLHandler.addTagValue( "organizationDomainStepName",
      getOrganizationDomainStepMeta() != null ? getOrganizationDomainStepMeta().getName() : getOrganizationDomainStepName() ) );
    return xml.toString();
  }

  @Override
  public void loadXML( Node stepnode, List<DatabaseMeta> databases, IMetaStore metaStore ) throws KettleXMLException {
    super.loadXML( stepnode, databases, metaStore );
    setIncomingFieldname( XMLHandler.getTagValue( stepnode, "incomingFieldname" ) );
    setOrganizationIdFieldname( XMLHandler.getTagValue( stepnode, "organizationIdFieldname" ) );
    setUrlFieldname( XMLHandler.getTagValue( stepnode, "urlFieldname" ) );
    setExternalIdFieldname( XMLHandler.getTagValue( stepnode, "externalIdFieldname" ) );
    setNameFieldname( XMLHandler.getTagValue( stepnode, "nameFieldname" ) );
    setCreatedAtFieldname( XMLHandler.getTagValue( stepnode, "createdAtFieldname" ) );
    setUpdatedAtFieldname( XMLHandler.getTagValue( stepnode, "updatedAtFieldname" ) );
    setDetailsFieldname( XMLHandler.getTagValue( stepnode, "detailsFieldname" ) );
    setNotesFieldname( XMLHandler.getTagValue( stepnode, "notesFieldname" ) );
    setGroupIdFieldname( XMLHandler.getTagValue( stepnode, "groupIdFieldname" ) );
    setSharedTicketsFieldname( XMLHandler.getTagValue( stepnode, "sharedTicketsFieldname" ) );
    setSharedCommentsFieldname( XMLHandler.getTagValue( stepnode, "sharedCommentsFieldname" ) );
    setTagFieldname( XMLHandler.getTagValue( stepnode, "tagFieldname" ) );
    setDomainNameFieldname( XMLHandler.getTagValue( stepnode, "domainNameFieldname" ) );
    setOrgFieldNameFieldname( XMLHandler.getTagValue( stepnode, "orgFieldNameFieldname" ) );
    setOrgFieldValueFieldname( XMLHandler.getTagValue( stepnode, "orgFieldValueFieldname" ) );
    setOrganizationStepName( XMLHandler.getTagValue( stepnode, "organizationStepName" ) );
    setOrganizationTagStepName( XMLHandler.getTagValue( stepnode, "organizationTagStepName" ) );
    setOrganizationFieldStepName( XMLHandler.getTagValue( stepnode, "organizationFieldStepName" ) );
    setOrganizationDomainStepName( XMLHandler.getTagValue( stepnode, "organizationDomainStepName" ) );
  }

  @Override
  public void readRep( Repository rep, IMetaStore metaStore, ObjectId id_step, List<DatabaseMeta> databases )
    throws KettleException {
    super.readRep( rep, metaStore, id_step, databases );
    setIncomingFieldname( rep.getStepAttributeString( id_step, "incomingFieldname" ) );
    setOrganizationIdFieldname( rep.getStepAttributeString( id_step, "organizationIdFieldname" ) );
    setUrlFieldname( rep.getStepAttributeString( id_step, "urlFieldname" ) );
    setExternalIdFieldname( rep.getStepAttributeString( id_step, "externalIdFieldname" ) );
    setNameFieldname( rep.getStepAttributeString( id_step, "nameFieldname" ) );
    setCreatedAtFieldname( rep.getStepAttributeString( id_step, "createdAtFieldname" ) );
    setUpdatedAtFieldname( rep.getStepAttributeString( id_step, "updatedAtFieldname" ) );
    setDetailsFieldname( rep.getStepAttributeString( id_step, "detailsFieldname" ) );
    setNotesFieldname( rep.getStepAttributeString( id_step, "notesFieldname" ) );
    setGroupIdFieldname( rep.getStepAttributeString( id_step, "groupIdFieldname" ) );
    setSharedTicketsFieldname( rep.getStepAttributeString( id_step, "sharedTicketsFieldname" ) );
    setSharedCommentsFieldname( rep.getStepAttributeString( id_step, "sharedCommentsFieldname" ) );
    setTagFieldname( rep.getStepAttributeString( id_step, "tagFieldname" ) );
    setDomainNameFieldname( rep.getStepAttributeString( id_step, "domainNameFieldname" ) );
    setOrgFieldNameFieldname( rep.getStepAttributeString( id_step, "orgFieldNameFieldname" ) );
    setOrgFieldValueFieldname( rep.getStepAttributeString( id_step, "orgFieldValueFieldname" ) );
    setOrganizationStepName( rep.getStepAttributeString( id_step, "organizationStepName" ) );
    setOrganizationTagStepName( rep.getStepAttributeString( id_step, "organizationTagStepName" ) );
    setOrganizationFieldStepName( rep.getStepAttributeString( id_step, "organizationFieldStepName" ) );
    setOrganizationDomainStepName( rep.getStepAttributeString( id_step, "organizationDomainStepName" ) );
  }

  @Override
  public void saveRep( Repository rep, IMetaStore metaStore, ObjectId id_transformation, ObjectId id_step )
    throws KettleException {
    super.saveRep( rep, metaStore, id_transformation, id_step );
    rep.saveStepAttribute( id_transformation, id_step, "incomingFieldname", getIncomingFieldname() );
    rep.saveStepAttribute( id_transformation, id_step, "organizationIdFieldname", getOrganizationIdFieldname() );
    rep.saveStepAttribute( id_transformation, id_step, "urlFieldname", getUrlFieldname() );
    rep.saveStepAttribute( id_transformation, id_step, "externalIdFieldname", getExternalIdFieldname() );
    rep.saveStepAttribute( id_transformation, id_step, "nameFieldname", getNameFieldname() );
    rep.saveStepAttribute( id_transformation, id_step, "createdAtFieldname", getCreatedAtFieldname() );
    rep.saveStepAttribute( id_transformation, id_step, "updatedAtFieldname", getUpdatedAtFieldname() );
    rep.saveStepAttribute( id_transformation, id_step, "detailsFieldname", getDetailsFieldname() );
    rep.saveStepAttribute( id_transformation, id_step, "notesFieldname", getNotesFieldname() );
    rep.saveStepAttribute( id_transformation, id_step, "groupIdFieldname", getGroupIdFieldname() );
    rep.saveStepAttribute( id_transformation, id_step, "sharedTicketsFieldname", getSharedTicketsFieldname() );
    rep.saveStepAttribute( id_transformation, id_step, "sharedCommentsFieldname", getSharedCommentsFieldname() );
    rep.saveStepAttribute( id_transformation, id_step, "tagFieldname", getTagFieldname() );
    rep.saveStepAttribute( id_transformation, id_step, "domainNameFieldname", getDomainNameFieldname() );
    rep.saveStepAttribute( id_transformation, id_step, "orgFieldNameFieldname", getOrgFieldNameFieldname() );
    rep.saveStepAttribute( id_transformation, id_step, "orgFieldValueFieldname", getOrgFieldValueFieldname() );
    rep.saveStepAttribute( id_transformation, id_step, "organizationStepName",
      getOrganizationStepMeta() != null ? getOrganizationStepMeta().getName() : getOrganizationStepName() );
    rep.saveStepAttribute( id_transformation, id_step, "organizationTagStepName",
      getOrganizationTagStepMeta() != null ? getOrganizationTagStepMeta().getName() : getOrganizationTagStepName() );
    rep.saveStepAttribute( id_transformation, id_step, "organizationFieldStepName",
      getOrganizationFieldStepMeta() != null ? getOrganizationFieldStepMeta().getName() : getOrganizationFieldStepName() );
    rep.saveStepAttribute( id_transformation, id_step, "organizationDomainStepName",
      getOrganizationDomainStepMeta() != null ? getOrganizationDomainStepMeta().getName() : getOrganizationDomainStepName() );
  }

  @Override
  public void setDefault() {
    super.setDefault();
    setIncomingFieldname( "" );
    setOrganizationIdFieldname( "Organization_ID" );
    setUrlFieldname( "Organization_URL" );
    setExternalIdFieldname( "Organization_External_ID" );
    setNameFieldname( "Organization_Name" );
    setCreatedAtFieldname( "Created_Time" );
    setUpdatedAtFieldname( "Updated_Time" );
    setDetailsFieldname( "Details" );
    setNotesFieldname( "Notes" );
    setGroupIdFieldname( "Group_ID" );
    setSharedTicketsFieldname( "Is_Shared_Tickets" );
    setSharedCommentsFieldname( "Is_Shared_Comments" );
    setTagFieldname( "Organization_Tag" );
    setDomainNameFieldname( "Organization_Domain" );
    setOrgFieldNameFieldname( "Organization_Field_Name" );
    setOrgFieldValueFieldname( "Organization_Field_Value" );
  }

  public String getIncomingFieldname() {
    return incomingFieldname;
  }

  public void setIncomingFieldname( String incomingFieldname ) {
    this.incomingFieldname = incomingFieldname;
  }

  public String getOrganizationIdFieldname() {
    return organizationIdFieldname;
  }

  public void setOrganizationIdFieldname( String organizationIdFieldname ) {
    this.organizationIdFieldname = organizationIdFieldname;
  }

  public String getUrlFieldname() {
    return urlFieldname;
  }

  public void setUrlFieldname( String urlFieldname ) {
    this.urlFieldname = urlFieldname;
  }

  public String getExternalIdFieldname() {
    return externalIdFieldname;
  }

  public void setExternalIdFieldname( String externalIdFieldname ) {
    this.externalIdFieldname = externalIdFieldname;
  }

  public String getNameFieldname() {
    return nameFieldname;
  }

  public void setNameFieldname( String nameFieldname ) {
    this.nameFieldname = nameFieldname;
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

  public String getDetailsFieldname() {
    return detailsFieldname;
  }

  public void setDetailsFieldname( String detailsFieldname ) {
    this.detailsFieldname = detailsFieldname;
  }

  public String getNotesFieldname() {
    return notesFieldname;
  }

  public void setNotesFieldname( String notesFieldname ) {
    this.notesFieldname = notesFieldname;
  }

  public String getGroupIdFieldname() {
    return groupIdFieldname;
  }

  public void setGroupIdFieldname( String groupIdFieldname ) {
    this.groupIdFieldname = groupIdFieldname;
  }

  public String getSharedTicketsFieldname() {
    return sharedTicketsFieldname;
  }

  public void setSharedTicketsFieldname( String sharedTicketsFieldname ) {
    this.sharedTicketsFieldname = sharedTicketsFieldname;
  }

  public String getSharedCommentsFieldname() {
    return sharedCommentsFieldname;
  }

  public void setSharedCommentsFieldname( String sharedCommentsFieldname ) {
    this.sharedCommentsFieldname = sharedCommentsFieldname;
  }

  public String getTagFieldname() {
    return tagFieldname;
  }

  public void setTagFieldname( String tagFieldname ) {
    this.tagFieldname = tagFieldname;
  }

  public String getDomainNameFieldname() {
    return domainNameFieldname;
  }

  public void setDomainNameFieldname( String domainNameFieldname ) {
    this.domainNameFieldname = domainNameFieldname;
  }

  public String getOrgFieldNameFieldname() {
    return orgFieldNameFieldname;
  }

  public void setOrgFieldNameFieldname( String orgFieldNameFieldname ) {
    this.orgFieldNameFieldname = orgFieldNameFieldname;
  }

  public String getOrgFieldValueFieldname() {
    return orgFieldValueFieldname;
  }

  public void setOrgFieldValueFieldname( String orgFieldValueFieldname ) {
    this.orgFieldValueFieldname = orgFieldValueFieldname;
  }

  public String getOrganizationStepName() {
    return organizationStepName;
  }

  public void setOrganizationStepName( String organizationStepName ) {
    this.organizationStepName = organizationStepName;
  }

  public String getOrganizationTagStepName() {
    return organizationTagStepName;
  }

  public void setOrganizationTagStepName( String organizationTagStepName ) {
    this.organizationTagStepName = organizationTagStepName;
  }

  public String getOrganizationFieldStepName() {
    return organizationFieldStepName;
  }

  public void setOrganizationFieldStepName( String organizationFieldStepName ) {
    this.organizationFieldStepName = organizationFieldStepName;
  }

  public StepMeta getOrganizationStepMeta() {
    return organizationStepMeta;
  }

  public void setOrganizationStepMeta( StepMeta organizationStepMeta ) {
    this.organizationStepMeta = organizationStepMeta;
  }

  public StepMeta getOrganizationTagStepMeta() {
    return organizationTagStepMeta;
  }

  public void setOrganizationTagStepMeta( StepMeta organizationTagStepMeta ) {
    this.organizationTagStepMeta = organizationTagStepMeta;
  }

  public StepMeta getOrganizationFieldStepMeta() {
    return organizationFieldStepMeta;
  }

  public void setOrganizationFieldStepMeta( StepMeta organizationFieldStepMeta ) {
    this.organizationFieldStepMeta = organizationFieldStepMeta;
  }

  public String getOrganizationDomainStepName() {
    return organizationDomainStepName;
  }

  public void setOrganizationDomainStepName( String organizationDomainStepName ) {
    this.organizationDomainStepName = organizationDomainStepName;
  }

  public StepMeta getOrganizationDomainStepMeta() {
    return organizationDomainStepMeta;
  }

  public void setOrganizationDomainStepMeta( StepMeta organizationDomainStepMeta ) {
    this.organizationDomainStepMeta = organizationDomainStepMeta;
  }
}
