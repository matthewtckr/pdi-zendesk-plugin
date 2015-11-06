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
    id = "ZendeskInputHCArticles",
    image = "org/pentaho/di/trans/steps/zendesk/zendesk.png",
    i18nPackageName="org.pentaho.di.trans.steps.zendesk",
    name="ZendeskInputHCArticles.Name",
    description = "ZendeskInputHCArticles.TooltipDesc",
    categoryDescription="i18n:org.pentaho.di.trans.step:BaseStep.Category.Input"
)
public class ZendeskInputHCArticleMeta extends ZendeskInputMeta {

  private String articleIdFieldname;
  private String articleUrlFieldname;
  private String articleTitleFieldname;
  private String articleBodyFieldname;
  private String localeFieldname;
  private String sourceLocaleFieldname;
  private String authorIdFieldname;
  private String commentsDisabledFieldname;
  private String outdatedFieldname;
  private String labelsFieldname;
  private String draftFieldname;
  private String promotedFieldname;
  private String positionFieldname;
  private String voteSumFieldname;
  private String voteCountFieldname;
  private String sectionIdFieldname;
  private String createdAtFieldname;
  private String updatedAtFieldname;

  public String getCreatedAtFieldname() {
    return createdAtFieldname;
  }

  @Override
  public void getFields( RowMetaInterface inputRowMeta, String name, RowMetaInterface[] info, StepMeta nextStep,
      VariableSpace space, Repository repository, IMetaStore metaStore ) throws KettleStepException {
    super.getFields( inputRowMeta, name, info, nextStep, space, repository, metaStore );
    addFieldToRow( inputRowMeta, space.environmentSubstitute( getArticleIdFieldname() ), ValueMetaInterface.TYPE_INTEGER );
    addFieldToRow( inputRowMeta, space.environmentSubstitute( getArticleUrlFieldname() ), ValueMetaInterface.TYPE_STRING );
    addFieldToRow( inputRowMeta, space.environmentSubstitute( getArticleTitleFieldname() ), ValueMetaInterface.TYPE_STRING );
    addFieldToRow( inputRowMeta, space.environmentSubstitute( getArticleBodyFieldname() ), ValueMetaInterface.TYPE_STRING );
    addFieldToRow( inputRowMeta, space.environmentSubstitute( getLocaleFieldname() ), ValueMetaInterface.TYPE_STRING );
    addFieldToRow( inputRowMeta, space.environmentSubstitute( getSourceLocaleFieldname() ), ValueMetaInterface.TYPE_STRING );
    addFieldToRow( inputRowMeta, space.environmentSubstitute( getAuthorIdFieldname() ), ValueMetaInterface.TYPE_INTEGER );
    addFieldToRow( inputRowMeta, space.environmentSubstitute( getCommentsDisabledFieldname() ), ValueMetaInterface.TYPE_BOOLEAN );
    addFieldToRow( inputRowMeta, space.environmentSubstitute( getOutdatedFieldname() ), ValueMetaInterface.TYPE_BOOLEAN );
    addFieldToRow( inputRowMeta, space.environmentSubstitute( getLabelsFieldname() ), ValueMetaInterface.TYPE_STRING );
    addFieldToRow( inputRowMeta, space.environmentSubstitute( getDraftFieldname() ), ValueMetaInterface.TYPE_BOOLEAN );
    addFieldToRow( inputRowMeta, space.environmentSubstitute( getPromotedFieldname() ), ValueMetaInterface.TYPE_BOOLEAN );
    addFieldToRow( inputRowMeta, space.environmentSubstitute( getPositionFieldname() ), ValueMetaInterface.TYPE_INTEGER );
    addFieldToRow( inputRowMeta, space.environmentSubstitute( getVoteSumFieldname() ), ValueMetaInterface.TYPE_INTEGER );
    addFieldToRow( inputRowMeta, space.environmentSubstitute( getVoteCountFieldname() ), ValueMetaInterface.TYPE_INTEGER );
    addFieldToRow( inputRowMeta, space.environmentSubstitute( getSectionIdFieldname() ), ValueMetaInterface.TYPE_INTEGER );
    addFieldToRow( inputRowMeta, space.environmentSubstitute( getCreatedAtFieldname() ), ValueMetaInterface.TYPE_DATE );
    addFieldToRow( inputRowMeta, space.environmentSubstitute( getUpdatedAtFieldname() ), ValueMetaInterface.TYPE_DATE );
  }

  @Override
  public StepInterface getStep( StepMeta stepMeta, StepDataInterface stepDataInterface, int copyNr,
      TransMeta transMeta, Trans trans ) {
    return new ZendeskInputHCArticle( stepMeta, stepDataInterface, copyNr, transMeta, trans );
  }

  @Override
  public StepDataInterface getStepData() {
    return new ZendeskInputHCArticleData();
  }

  public String getUpdatedAtFieldname() {
    return updatedAtFieldname;
  }

  @Override
  public String getXML() throws KettleException {
    StringBuilder xml = new StringBuilder();
    xml.append( super.getXML() );
    xml.append( "    " ).append( XMLHandler.addTagValue( "articleIdFieldname", getArticleIdFieldname() ) );
    xml.append( "    " ).append( XMLHandler.addTagValue( "articleUrlFieldname", getArticleUrlFieldname() ) );
    xml.append( "    " ).append( XMLHandler.addTagValue( "articleTitleFieldname", getArticleTitleFieldname() ) );
    xml.append( "    " ).append( XMLHandler.addTagValue( "articleBodyFieldname", getArticleBodyFieldname() ) );
    xml.append( "    " ).append( XMLHandler.addTagValue( "localeFieldname", getLocaleFieldname() ) );
    xml.append( "    " ).append( XMLHandler.addTagValue( "sourceLocaleFieldname", getSourceLocaleFieldname() ) );
    xml.append( "    " ).append( XMLHandler.addTagValue( "authorIdFieldname", getAuthorIdFieldname() ) );
    xml.append( "    " ).append( XMLHandler.addTagValue( "commentsDisabledFieldname", getCommentsDisabledFieldname() ) );
    xml.append( "    " ).append( XMLHandler.addTagValue( "outdatedFieldname", getOutdatedFieldname() ) );
    xml.append( "    " ).append( XMLHandler.addTagValue( "labelsFieldname", getLabelsFieldname() ) );
    xml.append( "    " ).append( XMLHandler.addTagValue( "draftFieldname", getDraftFieldname() ) );
    xml.append( "    " ).append( XMLHandler.addTagValue( "promotedFieldname", getPromotedFieldname() ) );
    xml.append( "    " ).append( XMLHandler.addTagValue( "positionFieldname", getPositionFieldname() ) );
    xml.append( "    " ).append( XMLHandler.addTagValue( "voteSumFieldname", getVoteSumFieldname() ) );
    xml.append( "    " ).append( XMLHandler.addTagValue( "voteCountFieldname", getVoteCountFieldname() ) );
    xml.append( "    " ).append( XMLHandler.addTagValue( "sectionIdFieldname", getSectionIdFieldname() ) );
    xml.append( "    " ).append( XMLHandler.addTagValue( "createdAtFieldname", getCreatedAtFieldname() ) );
    xml.append( "    " ).append( XMLHandler.addTagValue( "updatedAtFieldname", getUpdatedAtFieldname() ) );
    return xml.toString();
  }

  @Override
  public void loadXML( Node stepnode, List<DatabaseMeta> databases, IMetaStore metaStore ) throws KettleXMLException {
    super.loadXML( stepnode, databases, metaStore );
    setArticleIdFieldname( XMLHandler.getTagValue( stepnode, "articleIdFieldname") );
    setArticleUrlFieldname( XMLHandler.getTagValue( stepnode, "articleUrlFieldname") );
    setArticleTitleFieldname( XMLHandler.getTagValue( stepnode, "articleTitleFieldname") );
    setArticleBodyFieldname( XMLHandler.getTagValue( stepnode, "articleBodyFieldname") );
    setLocaleFieldname( XMLHandler.getTagValue( stepnode, "localeFieldname") );
    setSourceLocaleFieldname( XMLHandler.getTagValue( stepnode, "sourceLocaleFieldname") );
    setAuthorIdFieldname( XMLHandler.getTagValue( stepnode, "authorIdFieldname") );
    setCommentsDisabledFieldname( XMLHandler.getTagValue( stepnode, "commentsDisabledFieldname") );
    setOutdatedFieldname( XMLHandler.getTagValue( stepnode, "outdatedFieldname") );
    setLabelsFieldname( XMLHandler.getTagValue( stepnode, "labelsFieldname") );
    setDraftFieldname( XMLHandler.getTagValue( stepnode, "draftFieldname") );
    setPromotedFieldname( XMLHandler.getTagValue( stepnode, "promotedFieldname") );
    setPositionFieldname( XMLHandler.getTagValue( stepnode, "positionFieldname") );
    setVoteSumFieldname( XMLHandler.getTagValue( stepnode, "voteSumFieldname") );
    setVoteCountFieldname( XMLHandler.getTagValue( stepnode, "voteCountFieldname") );
    setSectionIdFieldname( XMLHandler.getTagValue( stepnode, "sectionIdFieldname") );
    setCreatedAtFieldname( XMLHandler.getTagValue( stepnode, "createdAtFieldname") );
    setUpdatedAtFieldname( XMLHandler.getTagValue( stepnode, "updatedAtFieldname") );

  }

  @Override
  public void readRep( Repository rep, IMetaStore metaStore, ObjectId id_step, List<DatabaseMeta> databases )
    throws KettleException {
    super.readRep( rep, metaStore, id_step, databases );
    setArticleIdFieldname( rep.getStepAttributeString( id_step, "articleIdFieldname") );
    setArticleUrlFieldname( rep.getStepAttributeString( id_step, "articleUrlFieldname") );
    setArticleTitleFieldname( rep.getStepAttributeString( id_step, "articleTitleFieldname") );
    setArticleBodyFieldname( rep.getStepAttributeString( id_step, "articleBodyFieldname") );
    setLocaleFieldname( rep.getStepAttributeString( id_step, "localeFieldname") );
    setSourceLocaleFieldname( rep.getStepAttributeString( id_step, "sourceLocaleFieldname") );
    setAuthorIdFieldname( rep.getStepAttributeString( id_step, "authorIdFieldname") );
    setCommentsDisabledFieldname( rep.getStepAttributeString( id_step, "commentsDisabledFieldname") );
    setOutdatedFieldname( rep.getStepAttributeString( id_step, "outdatedFieldname") );
    setLabelsFieldname( rep.getStepAttributeString( id_step, "labelsFieldname") );
    setDraftFieldname( rep.getStepAttributeString( id_step, "draftFieldname") );
    setPromotedFieldname( rep.getStepAttributeString( id_step, "promotedFieldname") );
    setPositionFieldname( rep.getStepAttributeString( id_step, "positionFieldname") );
    setVoteSumFieldname( rep.getStepAttributeString( id_step, "voteSumFieldname") );
    setVoteCountFieldname( rep.getStepAttributeString( id_step, "voteCountFieldname") );
    setSectionIdFieldname( rep.getStepAttributeString( id_step, "sectionIdFieldname") );
    setCreatedAtFieldname( rep.getStepAttributeString( id_step, "createdAtFieldname") );
    setUpdatedAtFieldname( rep.getStepAttributeString( id_step, "updatedAtFieldname") );

  }

  @Override
  public void saveRep( Repository rep, IMetaStore metaStore, ObjectId id_transformation, ObjectId id_step )
    throws KettleException {
    super.saveRep( rep, metaStore, id_transformation, id_step );
    rep.saveStepAttribute( id_transformation, id_step, "articleIdFieldname", getArticleIdFieldname() );
    rep.saveStepAttribute( id_transformation, id_step, "articleUrlFieldname", getArticleUrlFieldname() );
    rep.saveStepAttribute( id_transformation, id_step, "articleTitleFieldname", getArticleTitleFieldname() );
    rep.saveStepAttribute( id_transformation, id_step, "articleBodyFieldname", getArticleBodyFieldname() );
    rep.saveStepAttribute( id_transformation, id_step, "localeFieldname", getLocaleFieldname() );
    rep.saveStepAttribute( id_transformation, id_step, "sourceLocaleFieldname", getSourceLocaleFieldname() );
    rep.saveStepAttribute( id_transformation, id_step, "authorIdFieldname", getAuthorIdFieldname() );
    rep.saveStepAttribute( id_transformation, id_step, "commentsDisabledFieldname", getCommentsDisabledFieldname() );
    rep.saveStepAttribute( id_transformation, id_step, "outdatedFieldname", getOutdatedFieldname() );
    rep.saveStepAttribute( id_transformation, id_step, "labelsFieldname", getLabelsFieldname() );
    rep.saveStepAttribute( id_transformation, id_step, "draftFieldname", getDraftFieldname() );
    rep.saveStepAttribute( id_transformation, id_step, "promotedFieldname", getPromotedFieldname() );
    rep.saveStepAttribute( id_transformation, id_step, "positionFieldname", getPositionFieldname() );
    rep.saveStepAttribute( id_transformation, id_step, "voteSumFieldname", getVoteSumFieldname() );
    rep.saveStepAttribute( id_transformation, id_step, "voteCountFieldname", getVoteCountFieldname() );
    rep.saveStepAttribute( id_transformation, id_step, "sectionIdFieldname", getSectionIdFieldname() );
    rep.saveStepAttribute( id_transformation, id_step, "createdAtFieldname", getCreatedAtFieldname() );
    rep.saveStepAttribute( id_transformation, id_step, "updatedAtFieldname", getUpdatedAtFieldname() );
    
  }

  public void setCreatedAtFieldname( String createdAtFieldname ) {
    this.createdAtFieldname = createdAtFieldname;
  }

  @Override
  public void setDefault() {
    super.setDefault();
    setArticleIdFieldname( "Article_ID" );
    setArticleUrlFieldname( "Article_URL" );
    setArticleTitleFieldname( "Article_Title" );
    setArticleBodyFieldname( "Article_Body" );
    setLocaleFieldname( "Article_Locale" );
    setSourceLocaleFieldname( "Article_Source_Locale" );
    setAuthorIdFieldname( "Article_Author_ID" );
    setCommentsDisabledFieldname( "Comments_Disabled" );
    setOutdatedFieldname( "Is_Outdated" );
    setLabelsFieldname( "Article_Labels" );
    setDraftFieldname( "Is_Draft" );
    setPromotedFieldname( "Is_Promoted" );
    setPositionFieldname( "Position" );
    setVoteSumFieldname( "Vote_Sum" );
    setVoteCountFieldname( "Vote_Count" );
    setSectionIdFieldname( "Section_ID" );
    setCreatedAtFieldname( "Created_Time" );
    setUpdatedAtFieldname( "Updated_Time");
  }

  public void setUpdatedAtFieldname( String updatedAtFieldname ) {
    this.updatedAtFieldname = updatedAtFieldname;
  }

  public String getArticleIdFieldname() {
    return articleIdFieldname;
  }

  public void setArticleIdFieldname( String articleIdFieldname ) {
    this.articleIdFieldname = articleIdFieldname;
  }

  public String getArticleUrlFieldname() {
    return articleUrlFieldname;
  }

  public void setArticleUrlFieldname( String articleUrlFieldname ) {
    this.articleUrlFieldname = articleUrlFieldname;
  }

  public String getArticleTitleFieldname() {
    return articleTitleFieldname;
  }

  public void setArticleTitleFieldname( String articleTitleFieldname ) {
    this.articleTitleFieldname = articleTitleFieldname;
  }

  public String getArticleBodyFieldname() {
    return articleBodyFieldname;
  }

  public void setArticleBodyFieldname( String articleBodyFieldname ) {
    this.articleBodyFieldname = articleBodyFieldname;
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

  public String getAuthorIdFieldname() {
    return authorIdFieldname;
  }

  public void setAuthorIdFieldname( String authorIdFieldname ) {
    this.authorIdFieldname = authorIdFieldname;
  }

  public String getCommentsDisabledFieldname() {
    return commentsDisabledFieldname;
  }

  public void setCommentsDisabledFieldname( String commentsDisabledFieldname ) {
    this.commentsDisabledFieldname = commentsDisabledFieldname;
  }

  public String getOutdatedFieldname() {
    return outdatedFieldname;
  }

  public void setOutdatedFieldname( String outdatedFieldname ) {
    this.outdatedFieldname = outdatedFieldname;
  }

  public String getLabelsFieldname() {
    return labelsFieldname;
  }

  public void setLabelsFieldname( String labelsFieldname ) {
    this.labelsFieldname = labelsFieldname;
  }

  public String getDraftFieldname() {
    return draftFieldname;
  }

  public void setDraftFieldname( String draftFieldname ) {
    this.draftFieldname = draftFieldname;
  }

  public String getPromotedFieldname() {
    return promotedFieldname;
  }

  public void setPromotedFieldname( String promotedFieldname ) {
    this.promotedFieldname = promotedFieldname;
  }

  public String getPositionFieldname() {
    return positionFieldname;
  }

  public void setPositionFieldname( String positionFieldname ) {
    this.positionFieldname = positionFieldname;
  }

  public String getVoteSumFieldname() {
    return voteSumFieldname;
  }

  public void setVoteSumFieldname( String voteSumFieldname ) {
    this.voteSumFieldname = voteSumFieldname;
  }

  public String getVoteCountFieldname() {
    return voteCountFieldname;
  }

  public void setVoteCountFieldname( String voteCountFieldname ) {
    this.voteCountFieldname = voteCountFieldname;
  }

  public String getSectionIdFieldname() {
    return sectionIdFieldname;
  }

  public void setSectionIdFieldname( String sectionIdFieldname ) {
    this.sectionIdFieldname = sectionIdFieldname;
  }
}
