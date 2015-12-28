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
    id = "ZendeskInputHCArticles",
    image = "org/pentaho/di/trans/steps/zendesk/zendesk.png",
    i18nPackageName="org.pentaho.di.trans.steps.zendesk",
    name="ZendeskInputHCArticles.Name",
    description = "ZendeskInputHCArticles.TooltipDesc",
    categoryDescription="i18n:org.pentaho.di.trans.step:BaseStep.Category.Input"
)
public class ZendeskInputHCArticleMeta extends ZendeskInputMeta {

  private static final Class<?> PKG = ZendeskInputHCArticleMeta.class;

  private String incomingFieldname;

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

  private String translationIdFieldname;
  private String translationUrlFieldname;
  private String translationHtmlUrlFieldname;
  private String translationSourceIdFieldname;
  private String translationSourceTypeFieldname;
  private String translationLocaleFieldname;
  private String translationTitleFieldname;
  private String translationBodyFieldname;
  private String translationOutdatedFieldname;
  private String translationDraftFieldname;
  private String translationCreatedAtFieldname;
  private String translationUpdatedAtFieldname;
  private String translationUpdatedByIdFieldname;
  private String translationCreatedByIdFieldname;

  private StepIOMetaInterface ioMeta;
  private String articleStepName;
  private String translationStepName;

  private StepMeta articleStepMeta;
  private StepMeta translationStepMeta;

  public String getCreatedAtFieldname() {
    return createdAtFieldname;
  }

  @Override
  public void getFields( RowMetaInterface inputRowMeta, String name, RowMetaInterface[] info, StepMeta nextStep,
      VariableSpace space, Repository repository, IMetaStore metaStore ) throws KettleStepException {
    super.getFields( inputRowMeta, name, info, nextStep, space, repository, metaStore );
    if ( nextStep != null ) {
      if ( nextStep.equals( articleStepMeta ) ) {
        prepareExecutionResultsArticle( inputRowMeta, space );
      } else if ( nextStep.equals( translationStepMeta ) ) {
        prepareExecutionResultsTranslation( inputRowMeta, space );
      }
    }
  }

  private void prepareExecutionResultsArticle( RowMetaInterface inputRowMeta, VariableSpace space ) throws KettleStepException {
    inputRowMeta.clear();
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

  private void prepareExecutionResultsTranslation( RowMetaInterface inputRowMeta, VariableSpace space ) throws KettleStepException {
    inputRowMeta.clear();
    addFieldToRow( inputRowMeta, space.environmentSubstitute( getArticleIdFieldname() ), ValueMetaInterface.TYPE_INTEGER );
    addFieldToRow( inputRowMeta, space.environmentSubstitute( getTranslationIdFieldname() ), ValueMetaInterface.TYPE_INTEGER );
    addFieldToRow( inputRowMeta, space.environmentSubstitute( getTranslationUrlFieldname() ), ValueMetaInterface.TYPE_STRING );
    addFieldToRow( inputRowMeta, space.environmentSubstitute( getTranslationHtmlUrlFieldname() ), ValueMetaInterface.TYPE_STRING );
    addFieldToRow( inputRowMeta, space.environmentSubstitute( getTranslationSourceIdFieldname() ), ValueMetaInterface.TYPE_INTEGER );
    addFieldToRow( inputRowMeta, space.environmentSubstitute( getTranslationSourceTypeFieldname() ), ValueMetaInterface.TYPE_STRING );
    addFieldToRow( inputRowMeta, space.environmentSubstitute( getTranslationLocaleFieldname() ), ValueMetaInterface.TYPE_STRING );
    addFieldToRow( inputRowMeta, space.environmentSubstitute( getTranslationTitleFieldname() ), ValueMetaInterface.TYPE_STRING );
    addFieldToRow( inputRowMeta, space.environmentSubstitute( getTranslationBodyFieldname() ), ValueMetaInterface.TYPE_STRING );
    addFieldToRow( inputRowMeta, space.environmentSubstitute( getTranslationOutdatedFieldname() ), ValueMetaInterface.TYPE_BOOLEAN );
    addFieldToRow( inputRowMeta, space.environmentSubstitute( getTranslationDraftFieldname() ), ValueMetaInterface.TYPE_BOOLEAN );
    addFieldToRow( inputRowMeta, space.environmentSubstitute( getTranslationCreatedAtFieldname() ), ValueMetaInterface.TYPE_DATE );
    addFieldToRow( inputRowMeta, space.environmentSubstitute( getTranslationUpdatedAtFieldname() ), ValueMetaInterface.TYPE_DATE );
    addFieldToRow( inputRowMeta, space.environmentSubstitute( getTranslationUpdatedByIdFieldname() ), ValueMetaInterface.TYPE_INTEGER );
    addFieldToRow( inputRowMeta, space.environmentSubstitute( getTranslationCreatedByIdFieldname() ), ValueMetaInterface.TYPE_INTEGER );
  }

  @Override
  public StepInterface getStep( StepMeta stepMeta, StepDataInterface stepDataInterface, int copyNr,
      TransMeta transMeta, Trans trans ) {
    return new ZendeskInputHCArticle( stepMeta, stepDataInterface, copyNr, transMeta, trans );
  }

  @Override
  public StepIOMetaInterface getStepIOMeta() {
    if ( ioMeta == null ) {
      ioMeta = new StepIOMeta( true, true, false, false, true, false );

      ioMeta.addStream( new Stream( StreamType.TARGET, articleStepMeta, BaseMessages.getString(
        PKG, "ZendeskInputHCArticlesMeta.ArticleStream.Description" ), StreamIcon.TARGET, null ) );
      ioMeta.addStream( new Stream( StreamType.TARGET, translationStepMeta, BaseMessages.getString(
        PKG, "ZendeskInputHCArticlesMeta.TranslationStream.Description" ), StreamIcon.TARGET, null ) );
    }
    return ioMeta;
  }

  @Override
  public void searchInfoAndTargetSteps( List<StepMeta> steps ) {
    articleStepMeta = StepMeta.findStep( steps, articleStepName );
    translationStepMeta = StepMeta.findStep( steps, translationStepName );
  }

  @Override
  public void handleStreamSelection( StreamInterface stream ) {
    List<StreamInterface> targets = getStepIOMeta().getTargetStreams();
    int index = targets.indexOf( stream );
    StepMeta step = targets.get( index ).getStepMeta();
    switch ( index ) {
      case 0:
        setArticleStepMeta( step );
        break;
      case 1:
        setTranslationStepMeta( step );
        break;
      default:
        break;
    }
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
    xml.append( "    " ).append( XMLHandler.addTagValue( "incomingFieldname", getIncomingFieldname() ) );
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
    xml.append( "    " ).append( XMLHandler.addTagValue( "translationIdFieldname", getTranslationIdFieldname() ) );
    xml.append( "    " ).append( XMLHandler.addTagValue( "translationUrlFieldname", getTranslationUrlFieldname() ) );
    xml.append( "    " ).append( XMLHandler.addTagValue( "translationHtmlUrlFieldname", getTranslationHtmlUrlFieldname() ) );
    xml.append( "    " ).append( XMLHandler.addTagValue( "translationSourceIdFieldname", getTranslationSourceIdFieldname() ) );
    xml.append( "    " ).append( XMLHandler.addTagValue( "translationSourceTypeFieldname", getTranslationSourceTypeFieldname() ) );
    xml.append( "    " ).append( XMLHandler.addTagValue( "translationLocaleFieldname", getTranslationLocaleFieldname() ) );
    xml.append( "    " ).append( XMLHandler.addTagValue( "translationTitleFieldname", getTranslationTitleFieldname() ) );
    xml.append( "    " ).append( XMLHandler.addTagValue( "translationBodyFieldname", getTranslationBodyFieldname() ) );
    xml.append( "    " ).append( XMLHandler.addTagValue( "translationOutdatedFieldname", getTranslationOutdatedFieldname() ) );
    xml.append( "    " ).append( XMLHandler.addTagValue( "translationDraftFieldname", getTranslationDraftFieldname() ) );
    xml.append( "    " ).append( XMLHandler.addTagValue( "translationCreatedAtFieldname", getTranslationCreatedAtFieldname() ) );
    xml.append( "    " ).append( XMLHandler.addTagValue( "translationUpdatedAtFieldname", getTranslationUpdatedAtFieldname() ) );
    xml.append( "    " ).append( XMLHandler.addTagValue( "translationUpdatedByIdFieldname", getTranslationUpdatedByIdFieldname() ) );
    xml.append( "    " ).append( XMLHandler.addTagValue( "translationCreatedByIdFieldname", getTranslationCreatedByIdFieldname() ) );
    xml.append( "    " ).append( XMLHandler.addTagValue( "articleStepName",
      getArticleStepMeta() != null ? getArticleStepMeta().getName() : getArticleStepName() ) );
    xml.append( "    " ).append( XMLHandler.addTagValue( "translationStepName",
      getTranslationStepMeta() != null ? getTranslationStepMeta().getName() : getTranslationStepName() ) );
    return xml.toString();
  }

  @Override
  public void loadXML( Node stepnode, List<DatabaseMeta> databases, IMetaStore metaStore ) throws KettleXMLException {
    super.loadXML( stepnode, databases, metaStore );
    setIncomingFieldname( XMLHandler.getTagValue( stepnode, "incomingFieldname" ) );
    setArticleIdFieldname( XMLHandler.getTagValue( stepnode, "articleIdFieldname" ) );
    setArticleUrlFieldname( XMLHandler.getTagValue( stepnode, "articleUrlFieldname" ) );
    setArticleTitleFieldname( XMLHandler.getTagValue( stepnode, "articleTitleFieldname" ) );
    setArticleBodyFieldname( XMLHandler.getTagValue( stepnode, "articleBodyFieldname" ) );
    setLocaleFieldname( XMLHandler.getTagValue( stepnode, "localeFieldname" ) );
    setSourceLocaleFieldname( XMLHandler.getTagValue( stepnode, "sourceLocaleFieldname" ) );
    setAuthorIdFieldname( XMLHandler.getTagValue( stepnode, "authorIdFieldname" ) );
    setCommentsDisabledFieldname( XMLHandler.getTagValue( stepnode, "commentsDisabledFieldname" ) );
    setOutdatedFieldname( XMLHandler.getTagValue( stepnode, "outdatedFieldname" ) );
    setLabelsFieldname( XMLHandler.getTagValue( stepnode, "labelsFieldname" ) );
    setDraftFieldname( XMLHandler.getTagValue( stepnode, "draftFieldname" ) );
    setPromotedFieldname( XMLHandler.getTagValue( stepnode, "promotedFieldname" ) );
    setPositionFieldname( XMLHandler.getTagValue( stepnode, "positionFieldname" ) );
    setVoteSumFieldname( XMLHandler.getTagValue( stepnode, "voteSumFieldname" ) );
    setVoteCountFieldname( XMLHandler.getTagValue( stepnode, "voteCountFieldname" ) );
    setSectionIdFieldname( XMLHandler.getTagValue( stepnode, "sectionIdFieldname" ) );
    setCreatedAtFieldname( XMLHandler.getTagValue( stepnode, "createdAtFieldname" ) );
    setUpdatedAtFieldname( XMLHandler.getTagValue( stepnode, "updatedAtFieldname" ) );
    setTranslationIdFieldname( XMLHandler.getTagValue( stepnode, "translationIdFieldname" ) );
    setTranslationUrlFieldname( XMLHandler.getTagValue( stepnode, "translationUrlFieldname" ) );
    setTranslationHtmlUrlFieldname( XMLHandler.getTagValue( stepnode, "translationHtmlUrlFieldname" ) );
    setTranslationSourceIdFieldname( XMLHandler.getTagValue( stepnode, "translationSourceIdFieldname" ) );
    setTranslationSourceTypeFieldname( XMLHandler.getTagValue( stepnode, "translationSourceTypeFieldname" ) );
    setTranslationLocaleFieldname( XMLHandler.getTagValue( stepnode, "translationLocaleFieldname" ) );
    setTranslationTitleFieldname( XMLHandler.getTagValue( stepnode, "translationTitleFieldname" ) );
    setTranslationBodyFieldname( XMLHandler.getTagValue( stepnode, "translationBodyFieldname" ) );
    setTranslationOutdatedFieldname( XMLHandler.getTagValue( stepnode, "translationOutdatedFieldname" ) );
    setTranslationDraftFieldname( XMLHandler.getTagValue( stepnode, "translationDraftFieldname" ) );
    setTranslationCreatedAtFieldname( XMLHandler.getTagValue( stepnode, "translationCreatedAtFieldname" ) );
    setTranslationUpdatedAtFieldname( XMLHandler.getTagValue( stepnode, "translationUpdatedAtFieldname" ) );
    setTranslationUpdatedByIdFieldname( XMLHandler.getTagValue( stepnode, "translationUpdatedByIdFieldname" ) );
    setTranslationCreatedByIdFieldname( XMLHandler.getTagValue( stepnode, "translationCreatedByIdFieldname" ) );
    setArticleStepName( XMLHandler.getTagValue( stepnode, "articleStepName" ) );
    setTranslationStepName( XMLHandler.getTagValue( stepnode, "translationStepName" ) );
  }

  @Override
  public void readRep( Repository rep, IMetaStore metaStore, ObjectId id_step, List<DatabaseMeta> databases )
    throws KettleException {
    super.readRep( rep, metaStore, id_step, databases );
    setIncomingFieldname( rep.getStepAttributeString( id_step, "incomingFieldname" ) );
    setArticleIdFieldname( rep.getStepAttributeString( id_step, "articleIdFieldname" ) );
    setArticleUrlFieldname( rep.getStepAttributeString( id_step, "articleUrlFieldname" ) );
    setArticleTitleFieldname( rep.getStepAttributeString( id_step, "articleTitleFieldname" ) );
    setArticleBodyFieldname( rep.getStepAttributeString( id_step, "articleBodyFieldname" ) );
    setLocaleFieldname( rep.getStepAttributeString( id_step, "localeFieldname" ) );
    setSourceLocaleFieldname( rep.getStepAttributeString( id_step, "sourceLocaleFieldname" ) );
    setAuthorIdFieldname( rep.getStepAttributeString( id_step, "authorIdFieldname" ) );
    setCommentsDisabledFieldname( rep.getStepAttributeString( id_step, "commentsDisabledFieldname" ) );
    setOutdatedFieldname( rep.getStepAttributeString( id_step, "outdatedFieldname" ) );
    setLabelsFieldname( rep.getStepAttributeString( id_step, "labelsFieldname" ) );
    setDraftFieldname( rep.getStepAttributeString( id_step, "draftFieldname" ) );
    setPromotedFieldname( rep.getStepAttributeString( id_step, "promotedFieldname" ) );
    setPositionFieldname( rep.getStepAttributeString( id_step, "positionFieldname" ) );
    setVoteSumFieldname( rep.getStepAttributeString( id_step, "voteSumFieldname" ) );
    setVoteCountFieldname( rep.getStepAttributeString( id_step, "voteCountFieldname" ) );
    setSectionIdFieldname( rep.getStepAttributeString( id_step, "sectionIdFieldname" ) );
    setCreatedAtFieldname( rep.getStepAttributeString( id_step, "createdAtFieldname" ) );
    setUpdatedAtFieldname( rep.getStepAttributeString( id_step, "updatedAtFieldname" ) );
    setTranslationIdFieldname( rep.getStepAttributeString( id_step, "translationIdFieldname" ) );
    setTranslationUrlFieldname( rep.getStepAttributeString( id_step, "translationUrlFieldname" ) );
    setTranslationHtmlUrlFieldname( rep.getStepAttributeString( id_step, "translationHtmlUrlFieldname" ) );
    setTranslationSourceIdFieldname( rep.getStepAttributeString( id_step, "translationSourceIdFieldname" ) );
    setTranslationSourceTypeFieldname( rep.getStepAttributeString( id_step, "translationSourceTypeFieldname" ) );
    setTranslationLocaleFieldname( rep.getStepAttributeString( id_step, "translationLocaleFieldname" ) );
    setTranslationTitleFieldname( rep.getStepAttributeString( id_step, "translationTitleFieldname" ) );
    setTranslationBodyFieldname( rep.getStepAttributeString( id_step, "translationBodyFieldname" ) );
    setTranslationOutdatedFieldname( rep.getStepAttributeString( id_step, "translationOutdatedFieldname" ) );
    setTranslationDraftFieldname( rep.getStepAttributeString( id_step, "translationDraftFieldname" ) );
    setTranslationCreatedAtFieldname( rep.getStepAttributeString( id_step, "translationCreatedAtFieldname" ) );
    setTranslationUpdatedAtFieldname( rep.getStepAttributeString( id_step, "translationUpdatedAtFieldname" ) );
    setTranslationUpdatedByIdFieldname( rep.getStepAttributeString( id_step, "translationUpdatedByIdFieldname" ) );
    setTranslationCreatedByIdFieldname( rep.getStepAttributeString( id_step, "translationCreatedByIdFieldname" ) );
    setArticleStepName( rep.getStepAttributeString( id_step, "articleStepName" ) );
    setTranslationStepName( rep.getStepAttributeString( id_step, "translationStepName" ) );
  }

  @Override
  public void saveRep( Repository rep, IMetaStore metaStore, ObjectId id_transformation, ObjectId id_step )
    throws KettleException {
    super.saveRep( rep, metaStore, id_transformation, id_step );
    rep.saveStepAttribute( id_transformation, id_step, "incomingFieldname", getIncomingFieldname() );
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
    rep.saveStepAttribute( id_transformation, id_step, "translationIdFieldname", getTranslationIdFieldname() );
    rep.saveStepAttribute( id_transformation, id_step, "translationUrlFieldname", getTranslationUrlFieldname() );
    rep.saveStepAttribute( id_transformation, id_step, "translationHtmlUrlFieldname", getTranslationHtmlUrlFieldname() );
    rep.saveStepAttribute( id_transformation, id_step, "translationSourceIdFieldname", getTranslationSourceIdFieldname() );
    rep.saveStepAttribute( id_transformation, id_step, "translationSourceTypeFieldname", getTranslationSourceTypeFieldname() );
    rep.saveStepAttribute( id_transformation, id_step, "translationLocaleFieldname", getTranslationLocaleFieldname() );
    rep.saveStepAttribute( id_transformation, id_step, "translationTitleFieldname", getTranslationTitleFieldname() );
    rep.saveStepAttribute( id_transformation, id_step, "translationBodyFieldname", getTranslationBodyFieldname() );
    rep.saveStepAttribute( id_transformation, id_step, "translationOutdatedFieldname", getTranslationOutdatedFieldname() );
    rep.saveStepAttribute( id_transformation, id_step, "translationDraftFieldname", getTranslationDraftFieldname() );
    rep.saveStepAttribute( id_transformation, id_step, "translationCreatedAtFieldname", getTranslationCreatedAtFieldname() );
    rep.saveStepAttribute( id_transformation, id_step, "translationUpdatedAtFieldname", getTranslationUpdatedAtFieldname() );
    rep.saveStepAttribute( id_transformation, id_step, "translationUpdatedByIdFieldname", getTranslationUpdatedByIdFieldname() );
    rep.saveStepAttribute( id_transformation, id_step, "translationCreatedByIdFieldname", getTranslationCreatedByIdFieldname() );
    rep.saveStepAttribute( id_transformation, id_step, "articleStepName",
      getArticleStepMeta() != null ? getArticleStepMeta().getName() : getArticleStepName() );
    rep.saveStepAttribute( id_transformation, id_step, "translationStepName",
      getTranslationStepMeta() != null ? getTranslationStepMeta().getName() : getTranslationStepName() );
  }

  public void setCreatedAtFieldname( String createdAtFieldname ) {
    this.createdAtFieldname = createdAtFieldname;
  }

  @Override
  public void setDefault() {
    super.setDefault();
    setIncomingFieldname( "" );
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
    setTranslationIdFieldname( "Translation_ID" );
    setTranslationUrlFieldname( "Translation_URL" );
    setTranslationHtmlUrlFieldname( "Translation_HTML_URL" );
    setTranslationSourceIdFieldname( "Translation_Source_ID" );
    setTranslationSourceTypeFieldname( "Translation_Source_Type" );
    setTranslationLocaleFieldname( "Translation_Locale" );
    setTranslationTitleFieldname( "Translation_Title" );
    setTranslationBodyFieldname( "Translation_Body" );
    setTranslationOutdatedFieldname( "Translation_Outdated" );
    setTranslationDraftFieldname( "Translation_Draft" );
    setTranslationCreatedAtFieldname( "Translation_Created_At" );
    setTranslationUpdatedAtFieldname( "Translation_Updated_At" );
    setTranslationUpdatedByIdFieldname( "Translation_Updated_By" );
    setTranslationCreatedByIdFieldname( "Translation_Created_By" );
  }

  public void setUpdatedAtFieldname( String updatedAtFieldname ) {
    this.updatedAtFieldname = updatedAtFieldname;
  }

  public String getIncomingFieldname() {
    return incomingFieldname;
  }

  public void setIncomingFieldname( String incomingFieldname ) {
    this.incomingFieldname = incomingFieldname;
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

  public String getTranslationHtmlUrlFieldname() {
    return translationHtmlUrlFieldname;
  }

  public void setTranslationHtmlUrlFieldname( String translationHtmlUrlFieldname ) {
    this.translationHtmlUrlFieldname = translationHtmlUrlFieldname;
  }

  public String getTranslationSourceIdFieldname() {
    return translationSourceIdFieldname;
  }

  public void setTranslationSourceIdFieldname( String translationSourceIdFieldname ) {
    this.translationSourceIdFieldname = translationSourceIdFieldname;
  }

  public String getTranslationSourceTypeFieldname() {
    return translationSourceTypeFieldname;
  }

  public void setTranslationSourceTypeFieldname( String translationSourceTypeFieldname ) {
    this.translationSourceTypeFieldname = translationSourceTypeFieldname;
  }

  public String getTranslationLocaleFieldname() {
    return translationLocaleFieldname;
  }

  public void setTranslationLocaleFieldname( String translationLocaleFieldname ) {
    this.translationLocaleFieldname = translationLocaleFieldname;
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

  public String getTranslationOutdatedFieldname() {
    return translationOutdatedFieldname;
  }

  public void setTranslationOutdatedFieldname( String translationOutdatedFieldname ) {
    this.translationOutdatedFieldname = translationOutdatedFieldname;
  }

  public String getTranslationDraftFieldname() {
    return translationDraftFieldname;
  }

  public void setTranslationDraftFieldname( String translationDraftFieldname ) {
    this.translationDraftFieldname = translationDraftFieldname;
  }

  public String getTranslationCreatedAtFieldname() {
    return translationCreatedAtFieldname;
  }

  public void setTranslationCreatedAtFieldname( String translationCreatedAtFieldname ) {
    this.translationCreatedAtFieldname = translationCreatedAtFieldname;
  }

  public String getTranslationUpdatedAtFieldname() {
    return translationUpdatedAtFieldname;
  }

  public void setTranslationUpdatedAtFieldname( String translationUpdatedAtFieldname ) {
    this.translationUpdatedAtFieldname = translationUpdatedAtFieldname;
  }

  public String getTranslationUpdatedByIdFieldname() {
    return translationUpdatedByIdFieldname;
  }

  public void setTranslationUpdatedByIdFieldname( String translationUpdatedByIdFieldname ) {
    this.translationUpdatedByIdFieldname = translationUpdatedByIdFieldname;
  }

  public String getTranslationCreatedByIdFieldname() {
    return translationCreatedByIdFieldname;
  }

  public void setTranslationCreatedByIdFieldname( String translationCreatedByIdFieldname ) {
    this.translationCreatedByIdFieldname = translationCreatedByIdFieldname;
  }

  public String getArticleStepName() {
    return articleStepName;
  }

  public void setArticleStepName( String articleStepName ) {
    this.articleStepName = articleStepName;
  }

  public String getTranslationStepName() {
    return translationStepName;
  }

  public void setTranslationStepName( String translationStepName ) {
    this.translationStepName = translationStepName;
  }

  public StepMeta getArticleStepMeta() {
    return articleStepMeta;
  }

  public void setArticleStepMeta( StepMeta articleStepMeta ) {
    this.articleStepMeta = articleStepMeta;
  }

  public StepMeta getTranslationStepMeta() {
    return translationStepMeta;
  }

  public void setTranslationStepMeta( StepMeta translationStepMeta ) {
    this.translationStepMeta = translationStepMeta;
  }
}
