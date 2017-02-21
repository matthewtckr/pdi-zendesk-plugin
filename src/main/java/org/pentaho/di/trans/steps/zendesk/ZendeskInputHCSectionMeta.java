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
    id = "ZendeskInputHCSections",
    image = "org/pentaho/di/trans/steps/zendesk/zendesk.png",
    i18nPackageName = "org.pentaho.di.trans.steps.zendesk",
    name = "ZendeskInputHCSections.Name",
    description = "ZendeskInputHCSections.TooltipDesc",
    categoryDescription = "i18n:org.pentaho.di.trans.step:BaseStep.Category.Input"
  )
public class ZendeskInputHCSectionMeta extends ZendeskInputMeta {

  private String sectionIdFieldname;
  private String sectionUrlFieldname;
  private String sectionNameFieldname;
  private String categoryIdFieldname;
  private String localeFieldname;
  private String sourceLocaleFieldname;
  private String sectionHtmlUrlFieldname;
  private String outdatedFieldname;
  private String positionFieldname;
  private String createdAtFieldname;
  private String updatedAtFieldname;

  public String getCreatedAtFieldname() {
    return createdAtFieldname;
  }

  @Override
  public void getFields( RowMetaInterface inputRowMeta, String name, RowMetaInterface[] info, StepMeta nextStep,
      VariableSpace space, Repository repository, IMetaStore metaStore ) throws KettleStepException {
    super.getFields( inputRowMeta, name, info, nextStep, space, repository, metaStore );
    addFieldToRow( inputRowMeta, space.environmentSubstitute( getSectionIdFieldname() ), ValueMetaInterface.TYPE_INTEGER );
    addFieldToRow( inputRowMeta, space.environmentSubstitute( getSectionUrlFieldname() ), ValueMetaInterface.TYPE_STRING );
    addFieldToRow( inputRowMeta, space.environmentSubstitute( getSectionNameFieldname() ), ValueMetaInterface.TYPE_STRING );
    addFieldToRow( inputRowMeta, space.environmentSubstitute( getCategoryIdFieldname() ), ValueMetaInterface.TYPE_INTEGER );
    addFieldToRow( inputRowMeta, space.environmentSubstitute( getLocaleFieldname() ), ValueMetaInterface.TYPE_STRING );
    addFieldToRow( inputRowMeta, space.environmentSubstitute( getSourceLocaleFieldname() ), ValueMetaInterface.TYPE_STRING );
    addFieldToRow( inputRowMeta, space.environmentSubstitute( getSectionHtmlUrlFieldname() ), ValueMetaInterface.TYPE_STRING );
    addFieldToRow( inputRowMeta, space.environmentSubstitute( getOutdatedFieldname() ), ValueMetaInterface.TYPE_BOOLEAN );
    addFieldToRow( inputRowMeta, space.environmentSubstitute( getPositionFieldname() ), ValueMetaInterface.TYPE_INTEGER );
    addFieldToRow( inputRowMeta, space.environmentSubstitute( getCreatedAtFieldname() ), ValueMetaInterface.TYPE_DATE );
    addFieldToRow( inputRowMeta, space.environmentSubstitute( getUpdatedAtFieldname() ), ValueMetaInterface.TYPE_DATE );
  }

  @Override
  public StepInterface getStep( StepMeta stepMeta, StepDataInterface stepDataInterface, int copyNr,
      TransMeta transMeta, Trans trans ) {
    return new ZendeskInputHCSection( stepMeta, stepDataInterface, copyNr, transMeta, trans );
  }

  @Override
  public StepDataInterface getStepData() {
    return new ZendeskInputHCSectionData();
  }

  public String getUpdatedAtFieldname() {
    return updatedAtFieldname;
  }

  @Override
  public String getXML() throws KettleException {
    StringBuilder xml = new StringBuilder();
    xml.append( super.getXML() );
    xml.append( "    " ).append( XMLHandler.addTagValue( "sectionIdFieldname", getSectionIdFieldname() ) );
    xml.append( "    " ).append( XMLHandler.addTagValue( "sectionUrlFieldname", getSectionUrlFieldname() ) );
    xml.append( "    " ).append( XMLHandler.addTagValue( "sectionNameFieldname", getSectionNameFieldname() ) );
    xml.append( "    " ).append( XMLHandler.addTagValue( "categoryIdFieldname", getCategoryIdFieldname() ) );
    xml.append( "    " ).append( XMLHandler.addTagValue( "localeFieldname", getLocaleFieldname() ) );
    xml.append( "    " ).append( XMLHandler.addTagValue( "sourceLocaleFieldname", getSourceLocaleFieldname() ) );
    xml.append( "    " ).append( XMLHandler.addTagValue( "sectionHtmlUrlFieldname", getSectionHtmlUrlFieldname() ) );
    xml.append( "    " ).append( XMLHandler.addTagValue( "outdatedFieldname", getOutdatedFieldname() ) );
    xml.append( "    " ).append( XMLHandler.addTagValue( "positionFieldname", getPositionFieldname() ) );
    xml.append( "    " ).append( XMLHandler.addTagValue( "createdAtFieldname", getCreatedAtFieldname() ) );
    xml.append( "    " ).append( XMLHandler.addTagValue( "updatedAtFieldname", getUpdatedAtFieldname() ) );
    return xml.toString();
  }

  @Override
  public void loadXML( Node stepnode, List<DatabaseMeta> databases, IMetaStore metaStore ) throws KettleXMLException {
    super.loadXML( stepnode, databases, metaStore );
    setSectionIdFieldname( XMLHandler.getTagValue( stepnode, "sectionIdFieldname" ) );
    setSectionUrlFieldname( XMLHandler.getTagValue( stepnode, "sectionUrlFieldname" ) );
    setSectionNameFieldname( XMLHandler.getTagValue( stepnode, "sectionNameFieldname" ) );
    setCategoryIdFieldname( XMLHandler.getTagValue( stepnode, "categoryIdFieldname" ) );
    setLocaleFieldname( XMLHandler.getTagValue( stepnode, "localeFieldname" ) );
    setSourceLocaleFieldname( XMLHandler.getTagValue( stepnode, "sourceLocaleFieldname" ) );
    setSectionHtmlUrlFieldname( XMLHandler.getTagValue( stepnode, "sectionHtmlUrlFieldname" ) );
    setOutdatedFieldname( XMLHandler.getTagValue( stepnode, "outdatedFieldname" ) );
    setPositionFieldname( XMLHandler.getTagValue( stepnode, "positionFieldname" ) );
    setCreatedAtFieldname( XMLHandler.getTagValue( stepnode, "createdAtFieldname" ) );
    setUpdatedAtFieldname( XMLHandler.getTagValue( stepnode, "updatedAtFieldname" ) );
  }

  @Override
  public void readRep( Repository rep, IMetaStore metaStore, ObjectId id_step, List<DatabaseMeta> databases )
    throws KettleException {
    super.readRep( rep, metaStore, id_step, databases );
    setSectionIdFieldname( rep.getStepAttributeString( id_step, "sectionIdFieldname" ) );
    setSectionUrlFieldname( rep.getStepAttributeString( id_step, "sectionUrlFieldname" ) );
    setSectionNameFieldname( rep.getStepAttributeString( id_step, "sectionNameFieldname" ) );
    setCategoryIdFieldname( rep.getStepAttributeString( id_step, "categoryIdFieldname" ) );
    setLocaleFieldname( rep.getStepAttributeString( id_step, "localeFieldname" ) );
    setSourceLocaleFieldname( rep.getStepAttributeString( id_step, "sourceLocaleFieldname" ) );
    setSectionHtmlUrlFieldname( rep.getStepAttributeString( id_step, "sectionHtmlUrlFieldname" ) );
    setOutdatedFieldname( rep.getStepAttributeString( id_step, "outdatedFieldname" ) );
    setPositionFieldname( rep.getStepAttributeString( id_step, "positionFieldname" ) );
    setCreatedAtFieldname( rep.getStepAttributeString( id_step, "createdAtFieldname" ) );
    setUpdatedAtFieldname( rep.getStepAttributeString( id_step, "updatedAtFieldname" ) );
  }

  @Override
  public void saveRep( Repository rep, IMetaStore metaStore, ObjectId id_transformation, ObjectId id_step )
    throws KettleException {
    super.saveRep( rep, metaStore, id_transformation, id_step );
    rep.saveStepAttribute( id_transformation, id_step, "sectionIdFieldname", getSectionIdFieldname() );
    rep.saveStepAttribute( id_transformation, id_step, "sectionUrlFieldname", getSectionUrlFieldname() );
    rep.saveStepAttribute( id_transformation, id_step, "sectionNameFieldname", getSectionNameFieldname() );
    rep.saveStepAttribute( id_transformation, id_step, "categoryIdFieldname", getCategoryIdFieldname() );
    rep.saveStepAttribute( id_transformation, id_step, "localeFieldname", getLocaleFieldname() );
    rep.saveStepAttribute( id_transformation, id_step, "sourceLocaleFieldname", getSourceLocaleFieldname() );
    rep.saveStepAttribute( id_transformation, id_step, "sectionHtmlUrlFieldname", getSectionHtmlUrlFieldname() );
    rep.saveStepAttribute( id_transformation, id_step, "outdatedFieldname", getOutdatedFieldname() );
    rep.saveStepAttribute( id_transformation, id_step, "positionFieldname", getPositionFieldname() );
    rep.saveStepAttribute( id_transformation, id_step, "createdAtFieldname", getCreatedAtFieldname() );
    rep.saveStepAttribute( id_transformation, id_step, "updatedAtFieldname", getUpdatedAtFieldname() );
  }

  public void setCreatedAtFieldname( String createdAtFieldname ) {
    this.createdAtFieldname = createdAtFieldname;
  }

  @Override
  public void setDefault() {
    super.setDefault();
    setSectionIdFieldname( "Section_ID" );
    setSectionUrlFieldname( "Section_URL" );
    setSectionNameFieldname( "Section_Name" );
    setCategoryIdFieldname( "Category_ID" );
    setLocaleFieldname( "Section_Locale" );
    setSourceLocaleFieldname( "Section_SourceLocale" );
    setSectionHtmlUrlFieldname( "Section_HTML_URL" );
    setOutdatedFieldname( "Section_IsOutdated" );
    setPositionFieldname( "Section_Position" );
    setCreatedAtFieldname( "Created_Time" );
    setUpdatedAtFieldname( "Updated_Time" );
  }

  public void setUpdatedAtFieldname( String updatedAtFieldname ) {
    this.updatedAtFieldname = updatedAtFieldname;
  }

  public String getSectionIdFieldname() {
    return sectionIdFieldname;
  }

  public void setSectionIdFieldname( String categoryIdFieldname ) {
    this.sectionIdFieldname = categoryIdFieldname;
  }

  public String getSectionUrlFieldname() {
    return sectionUrlFieldname;
  }

  public void setSectionUrlFieldname( String categoryUrlFieldname ) {
    this.sectionUrlFieldname = categoryUrlFieldname;
  }

  public String getSectionNameFieldname() {
    return sectionNameFieldname;
  }

  public void setSectionNameFieldname( String categoryNameFieldname ) {
    this.sectionNameFieldname = categoryNameFieldname;
  }

  public String getLocaleFieldname() {
    return localeFieldname;
  }

  public void setLocaleFieldname( String localeFieldname ) {
    this.localeFieldname = localeFieldname;
  }

  public String getSourceLocaleFieldname() {
    return sourceLocaleFieldname;
  }

  public void setSourceLocaleFieldname( String sourceLocaleFieldname ) {
    this.sourceLocaleFieldname = sourceLocaleFieldname;
  }

  public String getSectionHtmlUrlFieldname() {
    return sectionHtmlUrlFieldname;
  }

  public void setSectionHtmlUrlFieldname( String categoryHtmlUrlFieldname ) {
    this.sectionHtmlUrlFieldname = categoryHtmlUrlFieldname;
  }

  public String getOutdatedFieldname() {
    return outdatedFieldname;
  }

  public void setOutdatedFieldname( String outdatedFieldname ) {
    this.outdatedFieldname = outdatedFieldname;
  }

  public String getPositionFieldname() {
    return positionFieldname;
  }

  public void setPositionFieldname( String positionFieldname ) {
    this.positionFieldname = positionFieldname;
  }

  public String getCategoryIdFieldname() {
    return categoryIdFieldname;
  }

  public void setCategoryIdFieldname( String categoryIdFieldname ) {
    this.categoryIdFieldname = categoryIdFieldname;
  }

}
