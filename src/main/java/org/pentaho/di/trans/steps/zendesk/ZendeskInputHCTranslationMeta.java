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
    id = "ZendeskInputHCTranslations",
    image = "org/pentaho/di/trans/steps/zendesk/zendesk.png",
    i18nPackageName="org.pentaho.di.trans.steps.zendesk",
    name="ZendeskInputHCTranslations.Name",
    description = "ZendeskInputHCTranslations.TooltipDesc",
    categoryDescription="i18n:org.pentaho.di.trans.step:BaseStep.Category.Input"
)
public class ZendeskInputHCTranslationMeta extends ZendeskInputMeta {

  private String translationIdFieldname;
  private String translationUrlFieldname;
  private String translationTitleFieldname;
  private String translationBodyFieldname;
  private String localeFieldname;
  private String sourceIdFieldname;
  private String sourceTypeFieldname;
  private String outdatedFieldname;
  private String draftFieldname;
  private String createdAtFieldname;
  private String createdByFieldname;
  private String updatedAtFieldname;
  private String updatedByFieldname;

  @Override
  public void getFields( RowMetaInterface inputRowMeta, String name, RowMetaInterface[] info, StepMeta nextStep,
      VariableSpace space, Repository repository, IMetaStore metaStore ) throws KettleStepException {
    super.getFields( inputRowMeta, name, info, nextStep, space, repository, metaStore );
    addFieldToRow( inputRowMeta, space.environmentSubstitute( getTranslationIdFieldname() ), ValueMetaInterface.TYPE_INTEGER );
    addFieldToRow( inputRowMeta, space.environmentSubstitute( getTranslationUrlFieldname() ), ValueMetaInterface.TYPE_STRING );
    addFieldToRow( inputRowMeta, space.environmentSubstitute( getTranslationTitleFieldname() ), ValueMetaInterface.TYPE_STRING );
    addFieldToRow( inputRowMeta, space.environmentSubstitute( getTranslationBodyFieldname() ), ValueMetaInterface.TYPE_STRING );
    addFieldToRow( inputRowMeta, space.environmentSubstitute( getLocaleFieldname() ), ValueMetaInterface.TYPE_STRING );
    addFieldToRow( inputRowMeta, space.environmentSubstitute( getSourceIdFieldname() ), ValueMetaInterface.TYPE_INTEGER );
    addFieldToRow( inputRowMeta, space.environmentSubstitute( getSourceTypeFieldname() ), ValueMetaInterface.TYPE_STRING );
    addFieldToRow( inputRowMeta, space.environmentSubstitute( getOutdatedFieldname() ), ValueMetaInterface.TYPE_BOOLEAN );
    addFieldToRow( inputRowMeta, space.environmentSubstitute( getDraftFieldname() ), ValueMetaInterface.TYPE_BOOLEAN );
    addFieldToRow( inputRowMeta, space.environmentSubstitute( getCreatedAtFieldname() ), ValueMetaInterface.TYPE_DATE );
    addFieldToRow( inputRowMeta, space.environmentSubstitute( getCreatedByFieldname() ), ValueMetaInterface.TYPE_INTEGER );
    addFieldToRow( inputRowMeta, space.environmentSubstitute( getUpdatedAtFieldname() ), ValueMetaInterface.TYPE_DATE );
    addFieldToRow( inputRowMeta, space.environmentSubstitute( getUpdatedByFieldname() ), ValueMetaInterface.TYPE_INTEGER );
  }

  @Override
  public StepInterface getStep( StepMeta stepMeta, StepDataInterface stepDataInterface, int copyNr,
      TransMeta transMeta, Trans trans ) {
    return new ZendeskInputHCTranslation( stepMeta, stepDataInterface, copyNr, transMeta, trans );
  }

  @Override
  public StepDataInterface getStepData() {
    return new ZendeskInputHCTranslationData();
  }

  @Override
  public String getXML() throws KettleException {
    StringBuilder xml = new StringBuilder();
    xml.append( super.getXML() );
    xml.append( "    " ).append( XMLHandler.addTagValue( "translationIdFieldname", getTranslationIdFieldname() ) );
    xml.append( "    " ).append( XMLHandler.addTagValue( "translationUrlFieldname", getTranslationUrlFieldname() ) );
    xml.append( "    " ).append( XMLHandler.addTagValue( "translationTitleFieldname", getTranslationTitleFieldname() ) );
    xml.append( "    " ).append( XMLHandler.addTagValue( "translationBodyFieldname", getTranslationBodyFieldname() ) );
    xml.append( "    " ).append( XMLHandler.addTagValue( "localeFieldname", getLocaleFieldname() ) );
    xml.append( "    " ).append( XMLHandler.addTagValue( "sourceIdFieldname", getSourceIdFieldname() ) );
    xml.append( "    " ).append( XMLHandler.addTagValue( "sourceTypeFieldname", getSourceTypeFieldname() ) );
    xml.append( "    " ).append( XMLHandler.addTagValue( "outdatedFieldname", getOutdatedFieldname() ) );
    xml.append( "    " ).append( XMLHandler.addTagValue( "draftFieldname", getDraftFieldname() ) );
    xml.append( "    " ).append( XMLHandler.addTagValue( "createdAtFieldname", getCreatedAtFieldname() ) );
    xml.append( "    " ).append( XMLHandler.addTagValue( "createdByFieldname", getCreatedByFieldname() ) );
    xml.append( "    " ).append( XMLHandler.addTagValue( "updatedAtFieldname", getUpdatedAtFieldname() ) );
    xml.append( "    " ).append( XMLHandler.addTagValue( "updatedByFieldname", getUpdatedByFieldname() ) );
    return xml.toString();
  }

  @Override
  public void loadXML( Node stepnode, List<DatabaseMeta> databases, IMetaStore metaStore ) throws KettleXMLException {
    super.loadXML( stepnode, databases, metaStore );
    setTranslationIdFieldname( XMLHandler.getTagValue( stepnode, "translationIdFieldname" ) );
    setTranslationUrlFieldname( XMLHandler.getTagValue( stepnode, "translationUrlFieldname" ) );
    setTranslationTitleFieldname( XMLHandler.getTagValue( stepnode, "translationTitleFieldname" ) );
    setTranslationBodyFieldname( XMLHandler.getTagValue( stepnode, "translationBodyFieldname" ) );
    setLocaleFieldname( XMLHandler.getTagValue( stepnode, "localeFieldname" ) );
    setSourceIdFieldname( XMLHandler.getTagValue( stepnode, "sourceIdFieldname" ) );
    setSourceTypeFieldname( XMLHandler.getTagValue( stepnode, "sourceTypeFieldname" ) );
    setOutdatedFieldname( XMLHandler.getTagValue( stepnode, "outdatedFieldname" ) );
    setDraftFieldname( XMLHandler.getTagValue( stepnode, "draftFieldname" ) );
    setCreatedAtFieldname( XMLHandler.getTagValue( stepnode, "createdAtFieldname" ) );
    setCreatedByFieldname( XMLHandler.getTagValue( stepnode, "createdByFieldname" ) );
    setUpdatedAtFieldname( XMLHandler.getTagValue( stepnode, "updatedAtFieldname" ) );
    setUpdatedByFieldname( XMLHandler.getTagValue( stepnode, "updatedByFieldname" ) );
  }

  @Override
  public void readRep( Repository rep, IMetaStore metaStore, ObjectId id_step, List<DatabaseMeta> databases )
    throws KettleException {
    super.readRep( rep, metaStore, id_step, databases );
    setTranslationIdFieldname( rep.getStepAttributeString( id_step, "translationIdFieldname" ) );
    setTranslationUrlFieldname( rep.getStepAttributeString( id_step, "translationUrlFieldname" ) );
    setTranslationTitleFieldname( rep.getStepAttributeString( id_step, "translationTitleFieldname" ) );
    setTranslationBodyFieldname( rep.getStepAttributeString( id_step, "translationBodyFieldname" ) );
    setLocaleFieldname( rep.getStepAttributeString( id_step, "localeFieldname" ) );
    setSourceIdFieldname( rep.getStepAttributeString( id_step, "sourceIdFieldname" ) );
    setSourceTypeFieldname( rep.getStepAttributeString( id_step, "sourceTypeFieldname" ) );
    setOutdatedFieldname( rep.getStepAttributeString( id_step, "outdatedFieldname" ) );
    setDraftFieldname( rep.getStepAttributeString( id_step, "draftFieldname" ) );
    setCreatedAtFieldname( rep.getStepAttributeString( id_step, "createdAtFieldname" ) );
    setCreatedByFieldname( rep.getStepAttributeString( id_step, "createdByFieldname" ) );
    setUpdatedAtFieldname( rep.getStepAttributeString( id_step, "updatedAtFieldname" ) );
    setUpdatedByFieldname( rep.getStepAttributeString( id_step, "updatedByFieldname" ) );

  }

  @Override
  public void saveRep( Repository rep, IMetaStore metaStore, ObjectId id_transformation, ObjectId id_step )
    throws KettleException {
    super.saveRep( rep, metaStore, id_transformation, id_step );
    rep.saveStepAttribute( id_transformation, id_step, "translationIdFieldname", getTranslationIdFieldname() );
    rep.saveStepAttribute( id_transformation, id_step, "translationUrlFieldname", getTranslationUrlFieldname() );
    rep.saveStepAttribute( id_transformation, id_step, "translationTitleFieldname", getTranslationTitleFieldname() );
    rep.saveStepAttribute( id_transformation, id_step, "translationBodyFieldname", getTranslationBodyFieldname() );
    rep.saveStepAttribute( id_transformation, id_step, "localeFieldname", getLocaleFieldname() );
    rep.saveStepAttribute( id_transformation, id_step, "sourceIdFieldname", getSourceIdFieldname() );
    rep.saveStepAttribute( id_transformation, id_step, "sourceTypeFieldname", getSourceTypeFieldname() );
    rep.saveStepAttribute( id_transformation, id_step, "outdatedFieldname", getOutdatedFieldname() );
    rep.saveStepAttribute( id_transformation, id_step, "draftFieldname", getDraftFieldname() );
    rep.saveStepAttribute( id_transformation, id_step, "createdAtFieldname", getCreatedAtFieldname() );
    rep.saveStepAttribute( id_transformation, id_step, "createdByFieldname", getCreatedByFieldname() );
    rep.saveStepAttribute( id_transformation, id_step, "updatedAtFieldname", getUpdatedAtFieldname() );
    rep.saveStepAttribute( id_transformation, id_step, "updatedByFieldname", getUpdatedByFieldname() );
  }

  @Override
  public void setDefault() {
    super.setDefault();
    setTranslationIdFieldname( "Translation_Id" );
    setTranslationUrlFieldname( "Translation_URL" );
    setTranslationTitleFieldname( "Translation_Title" );
    setTranslationBodyFieldname( "Translation_Body" );
    setLocaleFieldname( "Locale" );
    setSourceIdFieldname( "Source_Id" );
    setSourceTypeFieldname( "Source_Type" );
    setOutdatedFieldname( "Is_Outdated" );
    setDraftFieldname( "Is_Draft" );
    setCreatedAtFieldname( "Created_Time" );
    setCreatedByFieldname( "Created_By" );
    setUpdatedAtFieldname( "Updated_Time" );
    setUpdatedByFieldname( "Updated_By" );
  }

  public String getTranslationIdFieldname() {
    return translationIdFieldname;
  }

  public void setTranslationIdFieldname( String translationIdFieldname ) {
    this.translationIdFieldname = translationIdFieldname;
  }

  public String getTranslationUrlFieldname() {
    return translationUrlFieldname;
  }

  public void setTranslationUrlFieldname( String translationUrlFieldname ) {
    this.translationUrlFieldname = translationUrlFieldname;
  }

  public String getTranslationTitleFieldname() {
    return translationTitleFieldname;
  }

  public void setTranslationTitleFieldname( String translationTitleFieldname ) {
    this.translationTitleFieldname = translationTitleFieldname;
  }

  public String getTranslationBodyFieldname() {
    return translationBodyFieldname;
  }

  public void setTranslationBodyFieldname( String translationBodyFieldname ) {
    this.translationBodyFieldname = translationBodyFieldname;
  }

  public String getLocaleFieldname() {
    return localeFieldname;
  }

  public void setLocaleFieldname( String localeFieldname ) {
    this.localeFieldname = localeFieldname;
  }

  public String getSourceIdFieldname() {
    return sourceIdFieldname;
  }

  public void setSourceIdFieldname( String sourceIdFieldname ) {
    this.sourceIdFieldname = sourceIdFieldname;
  }

  public String getSourceTypeFieldname() {
    return sourceTypeFieldname;
  }

  public void setSourceTypeFieldname( String sourceTypeFieldname ) {
    this.sourceTypeFieldname = sourceTypeFieldname;
  }

  public String getOutdatedFieldname() {
    return outdatedFieldname;
  }

  public void setOutdatedFieldname( String outdatedFieldname ) {
    this.outdatedFieldname = outdatedFieldname;
  }

  public String getDraftFieldname() {
    return draftFieldname;
  }

  public void setDraftFieldname( String draftFieldname ) {
    this.draftFieldname = draftFieldname;
  }

  public String getCreatedAtFieldname() {
    return createdAtFieldname;
  }

  public void setCreatedAtFieldname( String createdAtFieldname ) {
    this.createdAtFieldname = createdAtFieldname;
  }

  public String getCreatedByFieldname() {
    return createdByFieldname;
  }

  public void setCreatedByFieldname( String createdByFieldname ) {
    this.createdByFieldname = createdByFieldname;
  }

  public String getUpdatedAtFieldname() {
    return updatedAtFieldname;
  }

  public void setUpdatedAtFieldname( String updatedAtFieldname ) {
    this.updatedAtFieldname = updatedAtFieldname;
  }

  public String getUpdatedByFieldname() {
    return updatedByFieldname;
  }

  public void setUpdatedByFieldname( String updatedByFieldname ) {
    this.updatedByFieldname = updatedByFieldname;
  }
}
