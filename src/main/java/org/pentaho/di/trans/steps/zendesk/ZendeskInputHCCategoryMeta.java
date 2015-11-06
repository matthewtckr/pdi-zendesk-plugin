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
    id = "ZendeskInputHCCategories",
    image = "org/pentaho/di/trans/steps/zendesk/zendesk.png",
    i18nPackageName="org.pentaho.di.trans.steps.zendesk",
    name="ZendeskInputHCCategories.Name",
    description = "ZendeskInputHCCategories.TooltipDesc",
    categoryDescription="i18n:org.pentaho.di.trans.step:BaseStep.Category.Input"
)
public class ZendeskInputHCCategoryMeta extends ZendeskInputMeta {

  private String categoryIdFieldname;
  private String categoryUrlFieldname;
  private String categoryNameFieldname;
  private String descriptionFieldname;
  private String localeFieldname;
  private String sourceLocaleFieldname;
  private String categoryHtmlUrlFieldname;
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
    addFieldToRow( inputRowMeta, space.environmentSubstitute( getCategoryIdFieldname() ), ValueMetaInterface.TYPE_INTEGER );
    addFieldToRow( inputRowMeta, space.environmentSubstitute( getCategoryUrlFieldname() ), ValueMetaInterface.TYPE_STRING );
    addFieldToRow( inputRowMeta, space.environmentSubstitute( getCategoryNameFieldname() ), ValueMetaInterface.TYPE_STRING );
    addFieldToRow( inputRowMeta, space.environmentSubstitute( getDescriptionFieldname() ), ValueMetaInterface.TYPE_STRING );
    addFieldToRow( inputRowMeta, space.environmentSubstitute( getLocaleFieldname() ), ValueMetaInterface.TYPE_STRING );
    addFieldToRow( inputRowMeta, space.environmentSubstitute( getSourceLocaleFieldname() ), ValueMetaInterface.TYPE_STRING );
    addFieldToRow( inputRowMeta, space.environmentSubstitute( getCategoryHtmlUrlFieldname() ), ValueMetaInterface.TYPE_STRING );
    addFieldToRow( inputRowMeta, space.environmentSubstitute( getOutdatedFieldname() ), ValueMetaInterface.TYPE_BOOLEAN );
    addFieldToRow( inputRowMeta, space.environmentSubstitute( getPositionFieldname() ), ValueMetaInterface.TYPE_INTEGER );
    addFieldToRow( inputRowMeta, space.environmentSubstitute( getCreatedAtFieldname() ), ValueMetaInterface.TYPE_DATE );
    addFieldToRow( inputRowMeta, space.environmentSubstitute( getUpdatedAtFieldname() ), ValueMetaInterface.TYPE_DATE );
  }

  @Override
  public StepInterface getStep( StepMeta stepMeta, StepDataInterface stepDataInterface, int copyNr,
      TransMeta transMeta, Trans trans ) {
    return new ZendeskInputHCCategory( stepMeta, stepDataInterface, copyNr, transMeta, trans );
  }

  @Override
  public StepDataInterface getStepData() {
    return new ZendeskInputHCCategoryData();
  }

  public String getUpdatedAtFieldname() {
    return updatedAtFieldname;
  }

  @Override
  public String getXML() throws KettleException {
    StringBuilder xml = new StringBuilder();
    xml.append( super.getXML() );
    xml.append( "    " ).append( XMLHandler.addTagValue( "categoryIdFieldname", getCategoryIdFieldname() ) );
    xml.append( "    " ).append( XMLHandler.addTagValue( "categoryUrlFieldname", getCategoryUrlFieldname() ) );
    xml.append( "    " ).append( XMLHandler.addTagValue( "categoryNameFieldname", getCategoryNameFieldname() ) );
    xml.append( "    " ).append( XMLHandler.addTagValue( "descriptionFieldname", getDescriptionFieldname() ) );
    xml.append( "    " ).append( XMLHandler.addTagValue( "localeFieldname", getLocaleFieldname() ) );
    xml.append( "    " ).append( XMLHandler.addTagValue( "sourceLocaleFieldname", getSourceLocaleFieldname() ) );
    xml.append( "    " ).append( XMLHandler.addTagValue( "categoryHtmlUrlFieldname", getCategoryHtmlUrlFieldname() ) );
    xml.append( "    " ).append( XMLHandler.addTagValue( "outdatedFieldname", getOutdatedFieldname() ) );
    xml.append( "    " ).append( XMLHandler.addTagValue( "positionFieldname", getPositionFieldname() ) );
    xml.append( "    " ).append( XMLHandler.addTagValue( "createdAtFieldname", getCreatedAtFieldname() ) );
    xml.append( "    " ).append( XMLHandler.addTagValue( "updatedAtFieldname", getUpdatedAtFieldname() ) );
    return xml.toString();
  }

  @Override
  public void loadXML( Node stepnode, List<DatabaseMeta> databases, IMetaStore metaStore ) throws KettleXMLException {
    super.loadXML( stepnode, databases, metaStore );
    setCategoryIdFieldname( XMLHandler.getTagValue( stepnode, "categoryIdFieldname" ) );
    setCategoryUrlFieldname( XMLHandler.getTagValue( stepnode, "categoryUrlFieldname" ) );
    setCategoryNameFieldname( XMLHandler.getTagValue( stepnode, "categoryNameFieldname" ) );
    setDescriptionFieldname( XMLHandler.getTagValue( stepnode, "descriptionFieldname" ) );
    setLocaleFieldname( XMLHandler.getTagValue( stepnode, "localeFieldname" ) );
    setSourceLocaleFieldname( XMLHandler.getTagValue( stepnode, "sourceLocaleFieldname" ) );
    setCategoryHtmlUrlFieldname( XMLHandler.getTagValue( stepnode, "categoryHtmlUrlFieldname" ) );
    setOutdatedFieldname( XMLHandler.getTagValue( stepnode, "outdatedFieldname" ) );
    setPositionFieldname( XMLHandler.getTagValue( stepnode, "positionFieldname" ) );
    setCreatedAtFieldname( XMLHandler.getTagValue( stepnode, "createdAtFieldname" ) );
    setUpdatedAtFieldname( XMLHandler.getTagValue( stepnode, "updatedAtFieldname" ) );
  }

  @Override
  public void readRep( Repository rep, IMetaStore metaStore, ObjectId id_step, List<DatabaseMeta> databases )
    throws KettleException {
    super.readRep( rep, metaStore, id_step, databases );
    setCategoryIdFieldname( rep.getStepAttributeString( id_step, "categoryIdFieldname" ) );
    setCategoryUrlFieldname( rep.getStepAttributeString( id_step, "categoryUrlFieldname" ) );
    setCategoryNameFieldname( rep.getStepAttributeString( id_step, "categoryNameFieldname" ) );
    setDescriptionFieldname( rep.getStepAttributeString( id_step, "descriptionFieldname" ) );
    setLocaleFieldname( rep.getStepAttributeString( id_step, "localeFieldname" ) );
    setSourceLocaleFieldname( rep.getStepAttributeString( id_step, "sourceLocaleFieldname" ) );
    setCategoryHtmlUrlFieldname( rep.getStepAttributeString( id_step, "categoryHtmlUrlFieldname" ) );
    setOutdatedFieldname( rep.getStepAttributeString( id_step, "outdatedFieldname" ) );
    setPositionFieldname( rep.getStepAttributeString( id_step, "positionFieldname" ) );
    setCreatedAtFieldname( rep.getStepAttributeString( id_step, "createdAtFieldname" ) );
    setUpdatedAtFieldname( rep.getStepAttributeString( id_step, "updatedAtFieldname" ) );
  }

  @Override
  public void saveRep( Repository rep, IMetaStore metaStore, ObjectId id_transformation, ObjectId id_step )
    throws KettleException {
    super.saveRep( rep, metaStore, id_transformation, id_step );
    rep.saveStepAttribute( id_transformation, id_step, "categoryIdFieldname", getCategoryIdFieldname() );
    rep.saveStepAttribute( id_transformation, id_step, "categoryUrlFieldname", getCategoryUrlFieldname() );
    rep.saveStepAttribute( id_transformation, id_step, "categoryNameFieldname", getCategoryNameFieldname() );
    rep.saveStepAttribute( id_transformation, id_step, "descriptionFieldname", getDescriptionFieldname() );
    rep.saveStepAttribute( id_transformation, id_step, "localeFieldname", getLocaleFieldname() );
    rep.saveStepAttribute( id_transformation, id_step, "sourceLocaleFieldname", getSourceLocaleFieldname() );
    rep.saveStepAttribute( id_transformation, id_step, "categoryHtmlUrlFieldname", getCategoryHtmlUrlFieldname() );
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
    setCategoryIdFieldname( "Category_ID" );
    setCategoryUrlFieldname( "Category_URL" );
    setCategoryNameFieldname( "Category_Name" );
    setDescriptionFieldname( "Category_Description" );
    setLocaleFieldname( "Category_Locale" );
    setSourceLocaleFieldname( "Category_SourceLocale" );
    setCategoryHtmlUrlFieldname( "Category_HTML_URL" );
    setOutdatedFieldname( "Category_IsOutdated" );
    setPositionFieldname( "Category_Position" );
    setCreatedAtFieldname( "Created_Time" );
    setUpdatedAtFieldname( "Updated_Time");
  }

  public void setUpdatedAtFieldname( String updatedAtFieldname ) {
    this.updatedAtFieldname = updatedAtFieldname;
  }

  public String getCategoryIdFieldname() {
    return categoryIdFieldname;
  }

  public void setCategoryIdFieldname( String categoryIdFieldname ) {
    this.categoryIdFieldname = categoryIdFieldname;
  }

  public String getCategoryUrlFieldname() {
    return categoryUrlFieldname;
  }

  public void setCategoryUrlFieldname( String categoryUrlFieldname ) {
    this.categoryUrlFieldname = categoryUrlFieldname;
  }

  public String getCategoryNameFieldname() {
    return categoryNameFieldname;
  }

  public void setCategoryNameFieldname( String categoryNameFieldname ) {
    this.categoryNameFieldname = categoryNameFieldname;
  }

  public String getDescriptionFieldname() {
    return descriptionFieldname;
  }

  public void setDescriptionFieldname( String descriptionFieldname ) {
    this.descriptionFieldname = descriptionFieldname;
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

  public String getCategoryHtmlUrlFieldname() {
    return categoryHtmlUrlFieldname;
  }

  public void setCategoryHtmlUrlFieldname( String categoryHtmlUrlFieldname ) {
    this.categoryHtmlUrlFieldname = categoryHtmlUrlFieldname;
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

}
