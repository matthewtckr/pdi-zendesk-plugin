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
    id = "ZendeskInputUsers",
    image = "org/pentaho/di/trans/steps/zendesk/zendesk.png",
    i18nPackageName = "org.pentaho.di.trans.steps.zendesk",
    name = "ZendeskInputUsers.Name",
    description = "ZendeskInputUsers.TooltipDesc",
    categoryDescription = "i18n:org.pentaho.di.trans.step:BaseStep.Category.Input"
  )
public class ZendeskInputUsersMeta extends ZendeskInputMeta {

  private static final Class<?> PKG = ZendeskInputUsersMeta.class;

  private String incomingFieldname;

  private String userIdFieldname;
  private String urlFieldname;
  private String externalIdFieldname;
  private String nameFieldname;
  private String emailFieldname;
  private String aliasFieldname;
  private String createdAtFieldname;
  private String updatedAtFieldname;
  private String activeFieldname;
  private String verifiedFieldname;
  private String sharedFieldname;
  private String localeIdFieldname;
  private String timeZoneFieldname;
  private String lastLoginAtFieldname;
  private String phoneFieldname;
  private String signatureFieldname;
  private String detailsFieldname;
  private String notesFieldname;
  private String organizationIdFieldname;
  private String roleFieldname;
  private String customRoleIdFieldname;
  private String moderatorFieldname;
  private String ticketRestrictionFieldname;
  private String onlyPrivateCommentsFieldname;
  private String tagsFieldname;
  private String suspendedFieldname;
  private String remotePhotoUrlFieldname;
  private String userFieldsFieldname;
  private String identityIdFieldname;
  private String identityUrlFieldname;
  private String identityTypeFieldname;
  private String identityValueFieldname;
  private String identityVerifiedFieldname;
  private String identityPrimaryFieldname;
  private String identityCreatedAtFieldname;
  private String identityUpdatedAtFieldname;

  private StepIOMetaInterface ioMeta;
  private String userStepName;
  private String userIdentityStepName;

  private StepMeta userStepMeta;
  private StepMeta userIdentityStepMeta;

  @Override
  public void getFields( RowMetaInterface inputRowMeta, String name, RowMetaInterface[] info, StepMeta nextStep,
      VariableSpace space, Repository repository, IMetaStore metaStore ) throws KettleStepException {
    super.getFields( inputRowMeta, name, info, nextStep, space, repository, metaStore );
    if ( nextStep != null ) {
      if ( nextStep.equals( userStepMeta ) ) {
        prepareExecutionResultsUser( inputRowMeta, space );
      } else if ( nextStep.equals( userIdentityStepMeta ) ) {
        prepareExecutionResultsUserIdentity( inputRowMeta, space );
      }
    }
  }

  private void prepareExecutionResultsUser( RowMetaInterface inputRowMeta, VariableSpace space ) throws KettleStepException {
    inputRowMeta.clear();
    addFieldToRow( inputRowMeta, space.environmentSubstitute( getUserIdFieldname() ), ValueMetaInterface.TYPE_INTEGER );
    addFieldToRow( inputRowMeta, space.environmentSubstitute( getUrlFieldname() ), ValueMetaInterface.TYPE_STRING );
    addFieldToRow( inputRowMeta, space.environmentSubstitute( getExternalIdFieldname() ), ValueMetaInterface.TYPE_STRING );
    addFieldToRow( inputRowMeta, space.environmentSubstitute( getNameFieldname() ), ValueMetaInterface.TYPE_STRING );
    addFieldToRow( inputRowMeta, space.environmentSubstitute( getEmailFieldname() ), ValueMetaInterface.TYPE_STRING );
    addFieldToRow( inputRowMeta, space.environmentSubstitute( getAliasFieldname() ), ValueMetaInterface.TYPE_STRING );
    addFieldToRow( inputRowMeta, space.environmentSubstitute( getCreatedAtFieldname() ), ValueMetaInterface.TYPE_DATE );
    addFieldToRow( inputRowMeta, space.environmentSubstitute( getUpdatedAtFieldname() ), ValueMetaInterface.TYPE_DATE );
    addFieldToRow( inputRowMeta, space.environmentSubstitute( getActiveFieldname() ), ValueMetaInterface.TYPE_BOOLEAN );
    addFieldToRow( inputRowMeta, space.environmentSubstitute( getVerifiedFieldname() ), ValueMetaInterface.TYPE_BOOLEAN );
    addFieldToRow( inputRowMeta, space.environmentSubstitute( getSharedFieldname() ), ValueMetaInterface.TYPE_BOOLEAN );
    addFieldToRow( inputRowMeta, space.environmentSubstitute( getLocaleIdFieldname() ), ValueMetaInterface.TYPE_INTEGER );
    addFieldToRow( inputRowMeta, space.environmentSubstitute( getTimeZoneFieldname() ), ValueMetaInterface.TYPE_STRING );
    addFieldToRow( inputRowMeta, space.environmentSubstitute( getLastLoginAtFieldname() ), ValueMetaInterface.TYPE_DATE );
    addFieldToRow( inputRowMeta, space.environmentSubstitute( getPhoneFieldname() ), ValueMetaInterface.TYPE_STRING );
    addFieldToRow( inputRowMeta, space.environmentSubstitute( getSignatureFieldname() ), ValueMetaInterface.TYPE_STRING );
    addFieldToRow( inputRowMeta, space.environmentSubstitute( getDetailsFieldname() ), ValueMetaInterface.TYPE_STRING );
    addFieldToRow( inputRowMeta, space.environmentSubstitute( getNotesFieldname() ), ValueMetaInterface.TYPE_STRING );
    addFieldToRow( inputRowMeta, space.environmentSubstitute( getOrganizationIdFieldname() ), ValueMetaInterface.TYPE_INTEGER );
    addFieldToRow( inputRowMeta, space.environmentSubstitute( getRoleFieldname() ), ValueMetaInterface.TYPE_STRING );
    addFieldToRow( inputRowMeta, space.environmentSubstitute( getCustomRoleIdFieldname() ), ValueMetaInterface.TYPE_INTEGER );
    addFieldToRow( inputRowMeta, space.environmentSubstitute( getModeratorFieldname() ), ValueMetaInterface.TYPE_BOOLEAN );
    addFieldToRow( inputRowMeta, space.environmentSubstitute( getTicketRestrictionFieldname() ), ValueMetaInterface.TYPE_STRING );
    addFieldToRow( inputRowMeta, space.environmentSubstitute( getOnlyPrivateCommentsFieldname() ), ValueMetaInterface.TYPE_BOOLEAN );
    addFieldToRow( inputRowMeta, space.environmentSubstitute( getTagsFieldname() ), ValueMetaInterface.TYPE_STRING );
    addFieldToRow( inputRowMeta, space.environmentSubstitute( getSuspendedFieldname() ), ValueMetaInterface.TYPE_BOOLEAN );
    addFieldToRow( inputRowMeta, space.environmentSubstitute( getRemotePhotoUrlFieldname() ), ValueMetaInterface.TYPE_STRING );
    addFieldToRow( inputRowMeta, space.environmentSubstitute( getUserFieldsFieldname() ), ValueMetaInterface.TYPE_STRING );
  }

  private void prepareExecutionResultsUserIdentity( RowMetaInterface inputRowMeta, VariableSpace space ) throws KettleStepException {
    inputRowMeta.clear();
    addFieldToRow( inputRowMeta, space.environmentSubstitute( getUserIdFieldname() ), ValueMetaInterface.TYPE_INTEGER );
    addFieldToRow( inputRowMeta, space.environmentSubstitute( getIdentityIdFieldname() ), ValueMetaInterface.TYPE_INTEGER );
    addFieldToRow( inputRowMeta, space.environmentSubstitute( getIdentityUrlFieldname() ), ValueMetaInterface.TYPE_STRING );
    addFieldToRow( inputRowMeta, space.environmentSubstitute( getIdentityTypeFieldname() ), ValueMetaInterface.TYPE_STRING );
    addFieldToRow( inputRowMeta, space.environmentSubstitute( getIdentityValueFieldname() ), ValueMetaInterface.TYPE_STRING );
    addFieldToRow( inputRowMeta, space.environmentSubstitute( getIdentityVerifiedFieldname() ), ValueMetaInterface.TYPE_BOOLEAN );
    addFieldToRow( inputRowMeta, space.environmentSubstitute( getIdentityPrimaryFieldname() ), ValueMetaInterface.TYPE_BOOLEAN );
    addFieldToRow( inputRowMeta, space.environmentSubstitute( getIdentityCreatedAtFieldname() ), ValueMetaInterface.TYPE_DATE );
    addFieldToRow( inputRowMeta, space.environmentSubstitute( getIdentityUpdatedAtFieldname() ), ValueMetaInterface.TYPE_DATE );
  }

  @Override
  public StepIOMetaInterface getStepIOMeta() {
    if ( ioMeta == null ) {
      ioMeta = new StepIOMeta( true, true, false, false, true, false );

      ioMeta.addStream( new Stream( StreamType.TARGET, userStepMeta, BaseMessages.getString(
        PKG, "ZendeskInputUsersMeta.UserOverviewStream.Description" ), StreamIcon.TARGET, null ) );
      ioMeta.addStream( new Stream( StreamType.TARGET, userIdentityStepMeta, BaseMessages.getString(
        PKG, "ZendeskInputUsersMeta.UserIdentityStream.Description" ), StreamIcon.TARGET, null ) );
    }
    return ioMeta;
  }

  @Override
  public void searchInfoAndTargetSteps( List<StepMeta> steps ) {
    userStepMeta = StepMeta.findStep( steps, userStepName );
    userIdentityStepMeta = StepMeta.findStep( steps, userIdentityStepName );
  }

  @Override
  public void handleStreamSelection( StreamInterface stream ) {
    List<StreamInterface> targets = getStepIOMeta().getTargetStreams();
    int index = targets.indexOf( stream );
    StepMeta step = targets.get( index ).getStepMeta();
    switch ( index ) {
      case 0:
        setUserStepMeta( step );
        break;
      case 1:
        setUserIdentityStepMeta( step );
        break;
      default:
        break;
    }
  }

  public boolean supportsErrorHandling() {
    return true;
  }

  @Override
  public StepInterface getStep( StepMeta stepMeta, StepDataInterface stepDataInterface, int copyNr,
      TransMeta transMeta, Trans trans ) {
    return new ZendeskInputUsers( stepMeta, stepDataInterface, copyNr, transMeta, trans );
  }

  @Override
  public StepDataInterface getStepData() {
    return new ZendeskInputUsersData();
  }

  @Override
  public String getXML() throws KettleException {
    StringBuilder xml = new StringBuilder();
    xml.append( super.getXML() );
    xml.append( "    " ).append( XMLHandler.addTagValue( "incomingFieldname", getIncomingFieldname() ) );
    xml.append( "    " ).append( XMLHandler.addTagValue( "userIdFieldname", getUserIdFieldname() ) );
    xml.append( "    " ).append( XMLHandler.addTagValue( "urlFieldname", getUrlFieldname() ) );
    xml.append( "    " ).append( XMLHandler.addTagValue( "externalIdFieldname", getExternalIdFieldname() ) );
    xml.append( "    " ).append( XMLHandler.addTagValue( "nameFieldname", getNameFieldname() ) );
    xml.append( "    " ).append( XMLHandler.addTagValue( "emailFieldname", getEmailFieldname() ) );
    xml.append( "    " ).append( XMLHandler.addTagValue( "aliasFieldname", getAliasFieldname() ) );
    xml.append( "    " ).append( XMLHandler.addTagValue( "createdAtFieldname", getCreatedAtFieldname() ) );
    xml.append( "    " ).append( XMLHandler.addTagValue( "updatedAtFieldname", getUpdatedAtFieldname() ) );
    xml.append( "    " ).append( XMLHandler.addTagValue( "activeFieldname", getActiveFieldname() ) );
    xml.append( "    " ).append( XMLHandler.addTagValue( "verifiedFieldname", getVerifiedFieldname() ) );
    xml.append( "    " ).append( XMLHandler.addTagValue( "sharedFieldname", getSharedFieldname() ) );
    xml.append( "    " ).append( XMLHandler.addTagValue( "localeIdFieldname", getLocaleIdFieldname() ) );
    xml.append( "    " ).append( XMLHandler.addTagValue( "timeZoneFieldname", getTimeZoneFieldname() ) );
    xml.append( "    " ).append( XMLHandler.addTagValue( "lastLoginAtFieldname", getLastLoginAtFieldname() ) );
    xml.append( "    " ).append( XMLHandler.addTagValue( "phoneFieldname", getPhoneFieldname() ) );
    xml.append( "    " ).append( XMLHandler.addTagValue( "signatureFieldname", getSignatureFieldname() ) );
    xml.append( "    " ).append( XMLHandler.addTagValue( "detailsFieldname", getDetailsFieldname() ) );
    xml.append( "    " ).append( XMLHandler.addTagValue( "notesFieldname", getNotesFieldname() ) );
    xml.append( "    " ).append( XMLHandler.addTagValue( "organizationIdFieldname", getOrganizationIdFieldname() ) );
    xml.append( "    " ).append( XMLHandler.addTagValue( "roleFieldname", getRoleFieldname() ) );
    xml.append( "    " ).append( XMLHandler.addTagValue( "customRoleIdFieldname", getCustomRoleIdFieldname() ) );
    xml.append( "    " ).append( XMLHandler.addTagValue( "moderatorFieldname", getModeratorFieldname() ) );
    xml.append( "    " ).append( XMLHandler.addTagValue( "ticketRestrictionFieldname", getTicketRestrictionFieldname() ) );
    xml.append( "    " ).append( XMLHandler.addTagValue( "onlyPrivateCommentsFieldname", getOnlyPrivateCommentsFieldname() ) );
    xml.append( "    " ).append( XMLHandler.addTagValue( "tagsFieldname", getTagsFieldname() ) );
    xml.append( "    " ).append( XMLHandler.addTagValue( "suspendedFieldname", getSuspendedFieldname() ) );
    xml.append( "    " ).append( XMLHandler.addTagValue( "remotePhotoUrlFieldname", getRemotePhotoUrlFieldname() ) );
    xml.append( "    " ).append( XMLHandler.addTagValue( "userFieldsFieldname", getUserFieldsFieldname() ) );
    xml.append( "    " ).append( XMLHandler.addTagValue( "identityIdFieldname", getIdentityIdFieldname() ) );
    xml.append( "    " ).append( XMLHandler.addTagValue( "identityUrlFieldname", getIdentityUrlFieldname() ) );
    xml.append( "    " ).append( XMLHandler.addTagValue( "identityTypeFieldname", getIdentityTypeFieldname() ) );
    xml.append( "    " ).append( XMLHandler.addTagValue( "identityValueFieldname", getIdentityValueFieldname() ) );
    xml.append( "    " ).append( XMLHandler.addTagValue( "identityVerifiedFieldname", getIdentityVerifiedFieldname() ) );
    xml.append( "    " ).append( XMLHandler.addTagValue( "identityPrimaryFieldname", getIdentityPrimaryFieldname() ) );
    xml.append( "    " ).append( XMLHandler.addTagValue( "identityCreatedAtFieldname", getIdentityCreatedAtFieldname() ) );
    xml.append( "    " ).append( XMLHandler.addTagValue( "identityUpdatedAtFieldname", getIdentityUpdatedAtFieldname() ) );
    xml.append( "    " ).append( XMLHandler.addTagValue( "userStepName",
      getUserStepMeta() != null ? getUserStepMeta().getName() : getUserStepName() ) );
    xml.append( "    " ).append( XMLHandler.addTagValue( "userIdentityStepName",
      getUserIdentityStepMeta() != null ? getUserIdentityStepMeta().getName() : getUserIdentityStepName() ) );
    return xml.toString();
  }

  @Override
  public void loadXML( Node stepnode, List<DatabaseMeta> databases, IMetaStore metaStore ) throws KettleXMLException {
    super.loadXML( stepnode, databases, metaStore );
    setIncomingFieldname( XMLHandler.getTagValue( stepnode, "incomingFieldname" ) );
    setUserIdFieldname( XMLHandler.getTagValue( stepnode, "userIdFieldname" ) );
    setUrlFieldname( XMLHandler.getTagValue( stepnode, "urlFieldname" ) );
    setExternalIdFieldname( XMLHandler.getTagValue( stepnode, "externalIdFieldname" ) );
    setNameFieldname( XMLHandler.getTagValue( stepnode, "nameFieldname" ) );
    setEmailFieldname( XMLHandler.getTagValue( stepnode, "emailFieldname" ) );
    setAliasFieldname( XMLHandler.getTagValue( stepnode, "aliasFieldname" ) );
    setCreatedAtFieldname( XMLHandler.getTagValue( stepnode, "createdAtFieldname" ) );
    setUpdatedAtFieldname( XMLHandler.getTagValue( stepnode, "updatedAtFieldname" ) );
    setActiveFieldname( XMLHandler.getTagValue( stepnode, "activeFieldname" ) );
    setVerifiedFieldname( XMLHandler.getTagValue( stepnode, "verifiedFieldname" ) );
    setSharedFieldname( XMLHandler.getTagValue( stepnode, "sharedFieldname" ) );
    setLocaleIdFieldname( XMLHandler.getTagValue( stepnode, "localeIdFieldname" ) );
    setTimeZoneFieldname( XMLHandler.getTagValue( stepnode, "timeZoneFieldname" ) );
    setLastLoginAtFieldname( XMLHandler.getTagValue( stepnode, "lastLoginAtFieldname" ) );
    setPhoneFieldname( XMLHandler.getTagValue( stepnode, "phoneFieldname" ) );
    setSignatureFieldname( XMLHandler.getTagValue( stepnode, "signatureFieldname" ) );
    setDetailsFieldname( XMLHandler.getTagValue( stepnode, "detailsFieldname" ) );
    setNotesFieldname( XMLHandler.getTagValue( stepnode, "notesFieldname" ) );
    setOrganizationIdFieldname( XMLHandler.getTagValue( stepnode, "organizationIdFieldname" ) );
    setRoleFieldname( XMLHandler.getTagValue( stepnode, "roleFieldname" ) );
    setCustomRoleIdFieldname( XMLHandler.getTagValue( stepnode, "customRoleIdFieldname" ) );
    setModeratorFieldname( XMLHandler.getTagValue( stepnode, "moderatorFieldname" ) );
    setTicketRestrictionFieldname( XMLHandler.getTagValue( stepnode, "ticketRestrictionFieldname" ) );
    setOnlyPrivateCommentsFieldname( XMLHandler.getTagValue( stepnode, "onlyPrivateCommentsFieldname" ) );
    setTagsFieldname( XMLHandler.getTagValue( stepnode, "tagsFieldname" ) );
    setSuspendedFieldname( XMLHandler.getTagValue( stepnode, "suspendedFieldname" ) );
    setRemotePhotoUrlFieldname( XMLHandler.getTagValue( stepnode, "remotePhotoUrlFieldname" ) );
    setUserFieldsFieldname( XMLHandler.getTagValue( stepnode, "userFieldsFieldname" ) );
    setIdentityIdFieldname( XMLHandler.getTagValue( stepnode, "identityIdFieldname" ) );
    setIdentityUrlFieldname( XMLHandler.getTagValue( stepnode, "identityUrlFieldname" ) );
    setIdentityTypeFieldname( XMLHandler.getTagValue( stepnode, "identityTypeFieldname" ) );
    setIdentityValueFieldname( XMLHandler.getTagValue( stepnode, "identityValueFieldname" ) );
    setIdentityVerifiedFieldname( XMLHandler.getTagValue( stepnode, "identityVerifiedFieldname" ) );
    setIdentityPrimaryFieldname( XMLHandler.getTagValue( stepnode, "identityPrimaryFieldname" ) );
    setIdentityCreatedAtFieldname( XMLHandler.getTagValue( stepnode, "identityCreatedAtFieldname" ) );
    setIdentityUpdatedAtFieldname( XMLHandler.getTagValue( stepnode, "identityUpdatedAtFieldname" ) );
    setUserStepName( XMLHandler.getTagValue( stepnode, "userStepName" ) );
    setUserIdentityStepName( XMLHandler.getTagValue( stepnode, "userIdentityStepName" ) );
  }

  @Override
  public void readRep( Repository rep, IMetaStore metaStore, ObjectId id_step, List<DatabaseMeta> databases )
    throws KettleException {
    super.readRep( rep, metaStore, id_step, databases );
    setIncomingFieldname( rep.getStepAttributeString( id_step, "incomingFieldname" ) );
    setUserIdFieldname( rep.getStepAttributeString( id_step, "userIdFieldname" ) );
    setUrlFieldname( rep.getStepAttributeString( id_step, "urlFieldname" ) );
    setExternalIdFieldname( rep.getStepAttributeString( id_step, "externalIdFieldname" ) );
    setNameFieldname( rep.getStepAttributeString( id_step, "nameFieldname" ) );
    setEmailFieldname( rep.getStepAttributeString( id_step, "emailFieldname" ) );
    setAliasFieldname( rep.getStepAttributeString( id_step, "aliasFieldname" ) );
    setCreatedAtFieldname( rep.getStepAttributeString( id_step, "createdAtFieldname" ) );
    setUpdatedAtFieldname( rep.getStepAttributeString( id_step, "updatedAtFieldname" ) );
    setActiveFieldname( rep.getStepAttributeString( id_step, "activeFieldname" ) );
    setVerifiedFieldname( rep.getStepAttributeString( id_step, "verifiedFieldname" ) );
    setSharedFieldname( rep.getStepAttributeString( id_step, "sharedFieldname" ) );
    setLocaleIdFieldname( rep.getStepAttributeString( id_step, "localeIdFieldname" ) );
    setTimeZoneFieldname( rep.getStepAttributeString( id_step, "timeZoneFieldname" ) );
    setLastLoginAtFieldname( rep.getStepAttributeString( id_step, "lastLoginAtFieldname" ) );
    setPhoneFieldname( rep.getStepAttributeString( id_step, "phoneFieldname" ) );
    setSignatureFieldname( rep.getStepAttributeString( id_step, "signatureFieldname" ) );
    setDetailsFieldname( rep.getStepAttributeString( id_step, "detailsFieldname" ) );
    setNotesFieldname( rep.getStepAttributeString( id_step, "notesFieldname" ) );
    setOrganizationIdFieldname( rep.getStepAttributeString( id_step, "organizationIdFieldname" ) );
    setRoleFieldname( rep.getStepAttributeString( id_step, "roleFieldname" ) );
    setCustomRoleIdFieldname( rep.getStepAttributeString( id_step, "customRoleIdFieldname" ) );
    setModeratorFieldname( rep.getStepAttributeString( id_step, "moderatorFieldname" ) );
    setTicketRestrictionFieldname( rep.getStepAttributeString( id_step, "ticketRestrictionFieldname" ) );
    setOnlyPrivateCommentsFieldname( rep.getStepAttributeString( id_step, "onlyPrivateCommentsFieldname" ) );
    setTagsFieldname( rep.getStepAttributeString( id_step, "tagsFieldname" ) );
    setSuspendedFieldname( rep.getStepAttributeString( id_step, "suspendedFieldname" ) );
    setRemotePhotoUrlFieldname( rep.getStepAttributeString( id_step, "remotePhotoUrlFieldname" ) );
    setUserFieldsFieldname( rep.getStepAttributeString( id_step, "userFieldsFieldname" ) );
    setIdentityIdFieldname( rep.getStepAttributeString( id_step, "identityIdFieldname" ) );
    setIdentityUrlFieldname( rep.getStepAttributeString( id_step, "identityUrlFieldname" ) );
    setIdentityTypeFieldname( rep.getStepAttributeString( id_step, "identityTypeFieldname" ) );
    setIdentityValueFieldname( rep.getStepAttributeString( id_step, "identityValueFieldname" ) );
    setIdentityVerifiedFieldname( rep.getStepAttributeString( id_step, "identityVerifiedFieldname" ) );
    setIdentityPrimaryFieldname( rep.getStepAttributeString( id_step, "identityPrimaryFieldname" ) );
    setIdentityCreatedAtFieldname( rep.getStepAttributeString( id_step, "identityCreatedAtFieldname" ) );
    setIdentityUpdatedAtFieldname( rep.getStepAttributeString( id_step, "identityUpdatedAtFieldname" ) );
    setUserStepName( rep.getStepAttributeString( id_step, "userStepName" ) );
    setUserIdentityStepName( rep.getStepAttributeString( id_step, "userIdentityStepName" ) );
  }

  @Override
  public void saveRep( Repository rep, IMetaStore metaStore, ObjectId id_transformation, ObjectId id_step )
    throws KettleException {
    super.saveRep( rep, metaStore, id_transformation, id_step );
    rep.saveStepAttribute( id_transformation, id_step, "incomingFieldname", getIncomingFieldname() );
    rep.saveStepAttribute( id_transformation, id_step, "userIdFieldname", getUserIdFieldname() );
    rep.saveStepAttribute( id_transformation, id_step, "urlFieldname", getUrlFieldname() );
    rep.saveStepAttribute( id_transformation, id_step, "externalIdFieldname", getExternalIdFieldname() );
    rep.saveStepAttribute( id_transformation, id_step, "nameFieldname", getNameFieldname() );
    rep.saveStepAttribute( id_transformation, id_step, "emailFieldname", getEmailFieldname() );
    rep.saveStepAttribute( id_transformation, id_step, "aliasFieldname", getAliasFieldname() );
    rep.saveStepAttribute( id_transformation, id_step, "createdAtFieldname", getCreatedAtFieldname() );
    rep.saveStepAttribute( id_transformation, id_step, "updatedAtFieldname", getUpdatedAtFieldname() );
    rep.saveStepAttribute( id_transformation, id_step, "activeFieldname", getActiveFieldname() );
    rep.saveStepAttribute( id_transformation, id_step, "verifiedFieldname", getVerifiedFieldname() );
    rep.saveStepAttribute( id_transformation, id_step, "sharedFieldname", getSharedFieldname() );
    rep.saveStepAttribute( id_transformation, id_step, "localeIdFieldname", getLocaleIdFieldname() );
    rep.saveStepAttribute( id_transformation, id_step, "timeZoneFieldname", getTimeZoneFieldname() );
    rep.saveStepAttribute( id_transformation, id_step, "lastLoginAtFieldname", getLastLoginAtFieldname() );
    rep.saveStepAttribute( id_transformation, id_step, "phoneFieldname", getPhoneFieldname() );
    rep.saveStepAttribute( id_transformation, id_step, "signatureFieldname", getSignatureFieldname() );
    rep.saveStepAttribute( id_transformation, id_step, "detailsFieldname", getDetailsFieldname() );
    rep.saveStepAttribute( id_transformation, id_step, "notesFieldname", getNotesFieldname() );
    rep.saveStepAttribute( id_transformation, id_step, "organizationIdFieldname", getOrganizationIdFieldname() );
    rep.saveStepAttribute( id_transformation, id_step, "roleFieldname", getRoleFieldname() );
    rep.saveStepAttribute( id_transformation, id_step, "customRoleIdFieldname", getCustomRoleIdFieldname() );
    rep.saveStepAttribute( id_transformation, id_step, "moderatorFieldname", getModeratorFieldname() );
    rep.saveStepAttribute( id_transformation, id_step, "ticketRestrictionFieldname", getTicketRestrictionFieldname() );
    rep.saveStepAttribute( id_transformation, id_step, "onlyPrivateCommentsFieldname", getOnlyPrivateCommentsFieldname() );
    rep.saveStepAttribute( id_transformation, id_step, "tagsFieldname", getTagsFieldname() );
    rep.saveStepAttribute( id_transformation, id_step, "suspendedFieldname", getSuspendedFieldname() );
    rep.saveStepAttribute( id_transformation, id_step, "remotePhotoUrlFieldname", getRemotePhotoUrlFieldname() );
    rep.saveStepAttribute( id_transformation, id_step, "userFieldsFieldname", getUserFieldsFieldname() );
    rep.saveStepAttribute( id_transformation, id_step, "identityIdFieldname", getIdentityIdFieldname() );
    rep.saveStepAttribute( id_transformation, id_step, "identityUrlFieldname", getIdentityUrlFieldname() );
    rep.saveStepAttribute( id_transformation, id_step, "identityTypeFieldname", getIdentityTypeFieldname() );
    rep.saveStepAttribute( id_transformation, id_step, "identityValueFieldname", getIdentityValueFieldname() );
    rep.saveStepAttribute( id_transformation, id_step, "identityVerifiedFieldname", getIdentityVerifiedFieldname() );
    rep.saveStepAttribute( id_transformation, id_step, "identityPrimaryFieldname", getIdentityPrimaryFieldname() );
    rep.saveStepAttribute( id_transformation, id_step, "identityCreatedAtFieldname", getIdentityCreatedAtFieldname() );
    rep.saveStepAttribute( id_transformation, id_step, "identityUpdatedAtFieldname", getIdentityUpdatedAtFieldname() );
    rep.saveStepAttribute( id_transformation, id_step, "userStepName",
      getUserStepMeta() != null ? getUserStepMeta().getName() : getUserStepName() );
    rep.saveStepAttribute( id_transformation, id_step, "userIdentityStepName",
      getUserIdentityStepMeta() != null ? getUserIdentityStepMeta().getName() : getUserIdentityStepName() );
  }

  @Override
  public void setDefault() {
    super.setDefault();
    setIncomingFieldname( "" );
    setUserIdFieldname( "User_ID" );
    setUrlFieldname( "User_URL" );
    setExternalIdFieldname( "User_External_ID" );
    setNameFieldname( "User_Name" );
    setEmailFieldname( "User_Email" );
    setAliasFieldname( "User_Alias" );
    setCreatedAtFieldname( "Created_Time" );
    setUpdatedAtFieldname( "Updated_Time" );
    setActiveFieldname( "Is_Active" );
    setVerifiedFieldname( "Is_Verified" );
    setSharedFieldname( "Is_Shared" );
    setLocaleIdFieldname( "Locale_ID" );
    setTimeZoneFieldname( "Timezone" );
    setLastLoginAtFieldname( "Last_Login_Time" );
    setPhoneFieldname( "Phone" );
    setSignatureFieldname( "Signature" );
    setDetailsFieldname( "Details" );
    setNotesFieldname( "Notes" );
    setOrganizationIdFieldname( "Organization_ID" );
    setRoleFieldname( "Role" );
    setCustomRoleIdFieldname( "Custom_Role_ID" );
    setModeratorFieldname( "Is_Moderator" );
    setTicketRestrictionFieldname( "Ticket_Restriction" );
    setOnlyPrivateCommentsFieldname( "Is_Only_Private_Comments" );
    setTagsFieldname( "User_Tags" );
    setSuspendedFieldname( "Is_Suspended" );
    setRemotePhotoUrlFieldname( "Photo_URL" );
    setUserFieldsFieldname( "User_Fields" );
    setIdentityIdFieldname( "Identity_ID" );
    setIdentityUrlFieldname( "Identity_URL" );
    setIdentityTypeFieldname( "Identity_Type" );
    setIdentityValueFieldname( "Identity_Value" );
    setIdentityVerifiedFieldname( "Identity_Verified" );
    setIdentityPrimaryFieldname( "Identity_Primary" );
    setIdentityCreatedAtFieldname( "Identity_Created_Time" );
    setIdentityUpdatedAtFieldname( "Identity_Updated_Time" );
  }

  public String getIncomingFieldname() {
    return incomingFieldname;
  }

  public void setIncomingFieldname( String incomingFieldname ) {
    this.incomingFieldname = incomingFieldname;
  }

  public String getUserIdFieldname() {
    return userIdFieldname;
  }

  public void setUserIdFieldname( String userIdFieldname ) {
    this.userIdFieldname = userIdFieldname;
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

  public String getEmailFieldname() {
    return emailFieldname;
  }

  public void setEmailFieldname( String emailFieldname ) {
    this.emailFieldname = emailFieldname;
  }

  public String getAliasFieldname() {
    return aliasFieldname;
  }

  public void setAliasFieldname( String aliasFieldname ) {
    this.aliasFieldname = aliasFieldname;
  }

  public String getUpdatedAtFieldname() {
    return updatedAtFieldname;
  }

  public void setUpdatedAtFieldname( String updatedAtFieldname ) {
    this.updatedAtFieldname = updatedAtFieldname;
  }

  public String getActiveFieldname() {
    return activeFieldname;
  }

  public void setActiveFieldname( String activeFieldname ) {
    this.activeFieldname = activeFieldname;
  }

  public String getVerifiedFieldname() {
    return verifiedFieldname;
  }

  public void setVerifiedFieldname( String verifiedFieldname ) {
    this.verifiedFieldname = verifiedFieldname;
  }

  public String getSharedFieldname() {
    return sharedFieldname;
  }

  public void setSharedFieldname( String sharedFieldname ) {
    this.sharedFieldname = sharedFieldname;
  }

  public String getLocaleIdFieldname() {
    return localeIdFieldname;
  }

  public void setLocaleIdFieldname( String localeIdFieldname ) {
    this.localeIdFieldname = localeIdFieldname;
  }

  public String getTimeZoneFieldname() {
    return timeZoneFieldname;
  }

  public void setTimeZoneFieldname( String timeZoneFieldname ) {
    this.timeZoneFieldname = timeZoneFieldname;
  }

  public String getLastLoginAtFieldname() {
    return lastLoginAtFieldname;
  }

  public void setLastLoginAtFieldname( String lastLoginAtFieldname ) {
    this.lastLoginAtFieldname = lastLoginAtFieldname;
  }

  public String getPhoneFieldname() {
    return phoneFieldname;
  }

  public void setPhoneFieldname( String phoneFieldname ) {
    this.phoneFieldname = phoneFieldname;
  }

  public String getSignatureFieldname() {
    return signatureFieldname;
  }

  public void setSignatureFieldname( String signatureFieldname ) {
    this.signatureFieldname = signatureFieldname;
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

  public String getOrganizationIdFieldname() {
    return organizationIdFieldname;
  }

  public void setOrganizationIdFieldname( String organizationIdFieldname ) {
    this.organizationIdFieldname = organizationIdFieldname;
  }

  public String getRoleFieldname() {
    return roleFieldname;
  }

  public void setRoleFieldname( String roleFieldname ) {
    this.roleFieldname = roleFieldname;
  }

  public String getCustomRoleIdFieldname() {
    return customRoleIdFieldname;
  }

  public void setCustomRoleIdFieldname( String customRoleIdFieldname ) {
    this.customRoleIdFieldname = customRoleIdFieldname;
  }

  public String getModeratorFieldname() {
    return moderatorFieldname;
  }

  public void setModeratorFieldname( String moderatorFieldname ) {
    this.moderatorFieldname = moderatorFieldname;
  }

  public String getTicketRestrictionFieldname() {
    return ticketRestrictionFieldname;
  }

  public void setTicketRestrictionFieldname( String ticketRestrictionFieldname ) {
    this.ticketRestrictionFieldname = ticketRestrictionFieldname;
  }

  public String getOnlyPrivateCommentsFieldname() {
    return onlyPrivateCommentsFieldname;
  }

  public void setOnlyPrivateCommentsFieldname( String onlyPrivateCommentsFieldname ) {
    this.onlyPrivateCommentsFieldname = onlyPrivateCommentsFieldname;
  }

  public String getTagsFieldname() {
    return tagsFieldname;
  }

  public void setTagsFieldname( String tagsFieldname ) {
    this.tagsFieldname = tagsFieldname;
  }

  public String getSuspendedFieldname() {
    return suspendedFieldname;
  }

  public void setSuspendedFieldname( String suspendedFieldname ) {
    this.suspendedFieldname = suspendedFieldname;
  }

  public String getRemotePhotoUrlFieldname() {
    return remotePhotoUrlFieldname;
  }

  public void setRemotePhotoUrlFieldname( String remotePhotoUrlFieldname ) {
    this.remotePhotoUrlFieldname = remotePhotoUrlFieldname;
  }

  public String getUserFieldsFieldname() {
    return userFieldsFieldname;
  }

  public void setUserFieldsFieldname( String userFieldsFieldname ) {
    this.userFieldsFieldname = userFieldsFieldname;
  }

  public String getCreatedAtFieldname() {
    return createdAtFieldname;
  }

  public void setCreatedAtFieldname( String createdAtFieldname ) {
    this.createdAtFieldname = createdAtFieldname;
  }


  public String getIdentityIdFieldname() {
    return identityIdFieldname;
  }

  public void setIdentityIdFieldname( String identityIdFieldname ) {
    this.identityIdFieldname = identityIdFieldname;
  }

  public String getIdentityUrlFieldname() {
    return identityUrlFieldname;
  }

  public void setIdentityUrlFieldname( String identityUrlFieldname ) {
    this.identityUrlFieldname = identityUrlFieldname;
  }

  public String getIdentityTypeFieldname() {
    return identityTypeFieldname;
  }

  public void setIdentityTypeFieldname( String identityTypeFieldname ) {
    this.identityTypeFieldname = identityTypeFieldname;
  }

  public String getIdentityValueFieldname() {
    return identityValueFieldname;
  }

  public void setIdentityValueFieldname( String identityValueFieldname ) {
    this.identityValueFieldname = identityValueFieldname;
  }

  public String getIdentityVerifiedFieldname() {
    return identityVerifiedFieldname;
  }

  public void setIdentityVerifiedFieldname( String identityVerifiedFieldname ) {
    this.identityVerifiedFieldname = identityVerifiedFieldname;
  }

  public String getIdentityPrimaryFieldname() {
    return identityPrimaryFieldname;
  }

  public void setIdentityPrimaryFieldname( String identityPrimaryFieldname ) {
    this.identityPrimaryFieldname = identityPrimaryFieldname;
  }

  public String getIdentityCreatedAtFieldname() {
    return identityCreatedAtFieldname;
  }

  public void setIdentityCreatedAtFieldname( String identityCreatedAtFieldname ) {
    this.identityCreatedAtFieldname = identityCreatedAtFieldname;
  }

  public String getIdentityUpdatedAtFieldname() {
    return identityUpdatedAtFieldname;
  }

  public void setIdentityUpdatedAtFieldname( String identityUpdatedAtFieldname ) {
    this.identityUpdatedAtFieldname = identityUpdatedAtFieldname;
  }

  public StepIOMetaInterface getIoMeta() {
    return ioMeta;
  }

  public void setIoMeta( StepIOMetaInterface ioMeta ) {
    this.ioMeta = ioMeta;
  }

  public String getUserStepName() {
    return userStepName;
  }

  public void setUserStepName( String userStepName ) {
    this.userStepName = userStepName;
  }

  public String getUserIdentityStepName() {
    return userIdentityStepName;
  }

  public void setUserIdentityStepName( String userIdentityStepName ) {
    this.userIdentityStepName = userIdentityStepName;
  }

  public StepMeta getUserStepMeta() {
    return userStepMeta;
  }

  public void setUserStepMeta( StepMeta userStepMeta ) {
    this.userStepMeta = userStepMeta;
  }

  public StepMeta getUserIdentityStepMeta() {
    return userIdentityStepMeta;
  }

  public void setUserIdentityStepMeta( StepMeta userIdentityStepMeta ) {
    this.userIdentityStepMeta = userIdentityStepMeta;
  }
}
