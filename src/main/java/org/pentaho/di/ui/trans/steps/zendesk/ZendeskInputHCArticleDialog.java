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

package org.pentaho.di.ui.trans.steps.zendesk;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.ShellAdapter;
import org.eclipse.swt.events.ShellEvent;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.pentaho.di.core.Const;
import org.pentaho.di.i18n.BaseMessages;
import org.pentaho.di.trans.TransMeta;
import org.pentaho.di.trans.step.BaseStepMeta;
import org.pentaho.di.trans.step.StepDialogInterface;
import org.pentaho.di.trans.steps.zendesk.ZendeskInputHCArticleMeta;
import org.pentaho.di.ui.core.widget.LabelTextVar;
import org.pentaho.di.ui.core.widget.PasswordTextVar;
import org.pentaho.di.ui.trans.step.BaseStepDialog;

public class ZendeskInputHCArticleDialog extends BaseStepDialog implements StepDialogInterface {

 private static Class<?> PKG = ZendeskInputHCArticleMeta.class; // for i18n purposes, needed by Translator2!!
 private ZendeskInputHCArticleMeta input;

 private LabelTextVar wSubDomain, wUsername;
 private Label wlPassword, wlToken;
 private PasswordTextVar wPassword;
 private Button wToken;

 private LabelTextVar wArticleIdFieldname;
 private LabelTextVar wArticleUrlFieldname;
 private LabelTextVar wArticleTitleFieldname;
 private LabelTextVar wArticleBodyFieldname;
 private LabelTextVar wLocaleFieldname;
 private LabelTextVar wSourceLocaleFieldname;
 private LabelTextVar wAuthorIdFieldname;
 private LabelTextVar wCommentsDisabledFieldname;
 private LabelTextVar wOutdatedFieldname;
 private LabelTextVar wLabelsFieldname;
 private LabelTextVar wDraftFieldname;
 private LabelTextVar wPromotedFieldname;
 private LabelTextVar wPositionFieldname;
 private LabelTextVar wVoteSumFieldname;
 private LabelTextVar wVoteCountFieldname;
 private LabelTextVar wSectionIdFieldname;
 private LabelTextVar wCreatedAtFieldname;
 private LabelTextVar wUpdatedAtFieldname;
 public ZendeskInputHCArticleDialog( Shell parent, Object in, TransMeta tr, String sname ) {
   super( parent, (BaseStepMeta) in, tr, sname );
   input = (ZendeskInputHCArticleMeta) in;
 }

 public String open() {
   Shell parent = getParent();
   Display display = parent.getDisplay();

   shell = new Shell( parent, SWT.DIALOG_TRIM | SWT.RESIZE | SWT.MIN | SWT.MAX );
   props.setLook( shell );
   setShellImage( shell, input );

   ModifyListener lsMod = new ModifyListener() {
     public void modifyText( ModifyEvent e ) {
       input.setChanged();
     }
   };
   changed = input.hasChanged();

   FormLayout formLayout = new FormLayout();
   formLayout.marginWidth = Const.FORM_MARGIN;
   formLayout.marginHeight = Const.FORM_MARGIN;

   shell.setLayout( formLayout );
   shell.setText( BaseMessages.getString( PKG, "ZendeskInputHCArticles.Shell.Title" ) );

   int middle = props.getMiddlePct();
   int margin = Const.MARGIN;

   // Stepname line
   wlStepname = new Label( shell, SWT.RIGHT );
   wlStepname.setText( BaseMessages.getString( PKG, "ZendeskInputDialog.Stepname.Label" ) );
   props.setLook( wlStepname );
   fdlStepname = new FormData();
   fdlStepname.left = new FormAttachment( 0, 0 );
   fdlStepname.right = new FormAttachment( middle, -margin );
   fdlStepname.top = new FormAttachment( 0, margin );
   wlStepname.setLayoutData( fdlStepname );
   wStepname = new Text( shell, SWT.SINGLE | SWT.LEFT | SWT.BORDER );
   wStepname.setText( stepname );
   props.setLook( wStepname );
   wStepname.addModifyListener( lsMod );
   fdStepname = new FormData();
   fdStepname.left = new FormAttachment( middle, 0 );
   fdStepname.top = new FormAttachment( 0, margin );
   fdStepname.right = new FormAttachment( 100, 0 );
   wStepname.setLayoutData( fdStepname );

   // Subdomain
   wSubDomain = new LabelTextVar( transMeta, shell,
     BaseMessages.getString( PKG, "ZendeskInputDialog.SubDomain.Label" ),
     BaseMessages.getString( PKG, "ZendeskInputDialog.SubDomain.Tooltip" ) );
   props.setLook( wSubDomain );
   wSubDomain.addModifyListener( lsMod );
   FormData fdSubDomain = new FormData();
   fdSubDomain.left = new FormAttachment( 0, -margin );
   fdSubDomain.top = new FormAttachment( wStepname, 2 * margin );
   fdSubDomain.right = new FormAttachment( 100, -margin );
   wSubDomain.setLayoutData( fdSubDomain );

   // Username
   wUsername =
     new LabelTextVar(
       transMeta, shell, BaseMessages.getString( PKG, "ZendeskInputDialog.Username.Label" ),
       BaseMessages.getString( PKG, "ZendeskInputDialog.Username.Tooltip" ) );
   props.setLook( wUsername );
   wUsername.addModifyListener( lsMod );
   FormData fdUsername = new FormData();
   fdUsername.left = new FormAttachment( 0, -margin );
   fdUsername.top = new FormAttachment( wSubDomain, 2 * margin );
   fdUsername.right = new FormAttachment( 100, -margin );
   wUsername.setLayoutData( fdUsername );

   // Password
   wlPassword = new Label( shell, SWT.RIGHT );
   wlPassword.setText( BaseMessages.getString( PKG, "ZendeskInputDialog.Password.Label" ) );
   props.setLook( wlPassword );
   FormData fdlPassword = new FormData();
   fdlPassword.left = new FormAttachment( 0, 0 );
   fdlPassword.top = new FormAttachment( wUsername, 2 * margin );
   fdlPassword.right = new FormAttachment( middle, -margin );
   wlPassword.setLayoutData( fdlPassword );
   
   wPassword = new PasswordTextVar( transMeta, shell, SWT.SINGLE | SWT.LEFT | SWT.BORDER,
     BaseMessages.getString( PKG, "ZendeskInputDialog.Password.Tooltip" ) );
   props.setLook( wPassword );
   wPassword.addModifyListener( lsMod );
   FormData fdPassword = new FormData();
   fdPassword.left = new FormAttachment( middle, 0 );
   fdPassword.top = new FormAttachment( wUsername, margin );
   fdPassword.right = new FormAttachment( 100, -margin );
   wPassword.setLayoutData( fdPassword );

   // Token
   wlToken = new Label( shell, SWT.RIGHT );
   wlToken.setText( BaseMessages.getString( PKG, "ZendeskInputDialog.Token.Label" ) );
   props.setLook( wlToken );
   FormData fdlToken = new FormData();
   fdlToken.left = new FormAttachment( 0, 0 );
   fdlToken.top = new FormAttachment( wlPassword, 2 * margin );
   fdlToken.right = new FormAttachment( middle, -margin );
   wlToken.setLayoutData( fdlToken );

   wToken = new Button( shell, SWT.CHECK );
   props.setLook( wToken );
   wToken.setToolTipText( BaseMessages.getString( PKG, "ZendeskInputDialog.Token.Tooltip" ) );
   FormData fdToken = new FormData();
   fdToken.left = new FormAttachment( middle, 0 );
   fdToken.top = new FormAttachment( wPassword, margin );
   fdToken.right = new FormAttachment( 100, -margin );
   wToken.setLayoutData( fdToken );
   wToken.addSelectionListener( new SelectionAdapter() {
     public void widgetSelected(SelectionEvent e) {
       input.setChanged();
     }
   } );

   // articleIdFieldname
   wArticleIdFieldname =
     new LabelTextVar(
       transMeta, shell, BaseMessages.getString( PKG, "ZendeskInputHCArticlesDialog.ArticleIdFieldname.Label" ),
       BaseMessages.getString( PKG, "ZendeskInputHCArticlesDialog.ArticleIdFieldname.Tooltip" ) );
   props.setLook( wArticleIdFieldname );
   wArticleIdFieldname.addModifyListener( lsMod );
   FormData fdArticleIdFieldname = new FormData();
   fdArticleIdFieldname.left = new FormAttachment( 0, -margin );
   fdArticleIdFieldname.top = new FormAttachment( wToken, 2 * margin );
   fdArticleIdFieldname.right = new FormAttachment( 100, -margin );
   wArticleIdFieldname.setLayoutData( fdArticleIdFieldname );

   // articleUrlFieldname
   wArticleUrlFieldname =
     new LabelTextVar(
       transMeta, shell, BaseMessages.getString( PKG, "ZendeskInputHCArticlesDialog.ArticleURLFieldname.Label" ),
       BaseMessages.getString( PKG, "ZendeskInputHCArticlesDialog.ArticleURLFieldname.Tooltip" ) );
   props.setLook( wArticleUrlFieldname );
   wArticleUrlFieldname.addModifyListener( lsMod );
   FormData fdArticleUrlFieldname = new FormData();
   fdArticleUrlFieldname.left = new FormAttachment( 0, -margin );
   fdArticleUrlFieldname.top = new FormAttachment( wArticleIdFieldname, 2 * margin );
   fdArticleUrlFieldname.right = new FormAttachment( 100, -margin );
   wArticleUrlFieldname.setLayoutData( fdArticleUrlFieldname );

   // articleTitleFieldname
   wArticleTitleFieldname =
     new LabelTextVar(
       transMeta, shell, BaseMessages.getString( PKG, "ZendeskInputHCArticlesDialog.ArticleTitleFieldname.Label" ),
       BaseMessages.getString( PKG, "ZendeskInputHCArticlesDialog.ArticleTitleFieldname.Tooltip" ) );
   props.setLook( wArticleTitleFieldname );
   wArticleTitleFieldname.addModifyListener( lsMod );
   FormData fdArticleTitleFieldname = new FormData();
   fdArticleTitleFieldname.left = new FormAttachment( 0, -margin );
   fdArticleTitleFieldname.top = new FormAttachment( wArticleUrlFieldname, 2 * margin );
   fdArticleTitleFieldname.right = new FormAttachment( 100, -margin );
   wArticleTitleFieldname.setLayoutData( fdArticleTitleFieldname );

   // articleBodyFieldname
   wArticleBodyFieldname =
     new LabelTextVar(
       transMeta, shell, BaseMessages.getString( PKG, "ZendeskInputHCArticlesDialog.ArticleBodyFieldname.Label" ),
       BaseMessages.getString( PKG, "ZendeskInputHCArticlesDialog.ArticleBodyFieldname.Tooltip" ) );
   props.setLook( wArticleBodyFieldname );
   wArticleBodyFieldname.addModifyListener( lsMod );
   FormData fdArticleBodyFieldname = new FormData();
   fdArticleBodyFieldname.left = new FormAttachment( 0, -margin );
   fdArticleBodyFieldname.top = new FormAttachment( wArticleTitleFieldname, 2 * margin );
   fdArticleBodyFieldname.right = new FormAttachment( 100, -margin );
   wArticleBodyFieldname.setLayoutData( fdArticleBodyFieldname );

   // localeFieldname
   wLocaleFieldname =
     new LabelTextVar(
       transMeta, shell, BaseMessages.getString( PKG, "ZendeskInputHCArticlesDialog.LocaleFieldname.Label" ),
       BaseMessages.getString( PKG, "ZendeskInputHCArticlesDialog.LocaleFieldname.Tooltip" ) );
   props.setLook( wLocaleFieldname );
   wLocaleFieldname.addModifyListener( lsMod );
   FormData fdLocaleFieldname = new FormData();
   fdLocaleFieldname.left = new FormAttachment( 0, -margin );
   fdLocaleFieldname.top = new FormAttachment( wArticleBodyFieldname, 2 * margin );
   fdLocaleFieldname.right = new FormAttachment( 100, -margin );
   wLocaleFieldname.setLayoutData( fdLocaleFieldname );

   // sourceLocaleFieldname
   wSourceLocaleFieldname =
     new LabelTextVar(
       transMeta, shell, BaseMessages.getString( PKG, "ZendeskInputHCArticlesDialog.SourceLocaleFieldname.Label" ),
       BaseMessages.getString( PKG, "ZendeskInputHCArticlesDialog.SourceLocaleFieldname.Tooltip" ) );
   props.setLook( wSourceLocaleFieldname );
   wSourceLocaleFieldname.addModifyListener( lsMod );
   FormData fdSourceLocaleFieldname = new FormData();
   fdSourceLocaleFieldname.left = new FormAttachment( 0, -margin );
   fdSourceLocaleFieldname.top = new FormAttachment( wLocaleFieldname, 2 * margin );
   fdSourceLocaleFieldname.right = new FormAttachment( 100, -margin );
   wSourceLocaleFieldname.setLayoutData( fdSourceLocaleFieldname );

   // authorIdFieldname
   wAuthorIdFieldname =
     new LabelTextVar(
       transMeta, shell, BaseMessages.getString( PKG, "ZendeskInputHCArticlesDialog.AuthorIDFieldname.Label" ),
       BaseMessages.getString( PKG, "ZendeskInputHCArticlesDialog.AuthorIDFieldname.Tooltip" ) );
   props.setLook( wAuthorIdFieldname );
   wAuthorIdFieldname.addModifyListener( lsMod );
   FormData fdAuthorIdFieldname = new FormData();
   fdAuthorIdFieldname.left = new FormAttachment( 0, -margin );
   fdAuthorIdFieldname.top = new FormAttachment( wSourceLocaleFieldname, 2 * margin );
   fdAuthorIdFieldname.right = new FormAttachment( 100, -margin );
   wAuthorIdFieldname.setLayoutData( fdAuthorIdFieldname );

   // commentsDisabledFieldname
   wCommentsDisabledFieldname =
     new LabelTextVar(
       transMeta, shell, BaseMessages.getString( PKG, "ZendeskInputHCArticlesDialog.CommentsDisabledFieldname.Label" ),
       BaseMessages.getString( PKG, "ZendeskInputHCArticlesDialog.CommentsDisabledFieldname.Tooltip" ) );
   props.setLook( wCommentsDisabledFieldname );
   wCommentsDisabledFieldname.addModifyListener( lsMod );
   FormData fdCommentsDisabledFieldname = new FormData();
   fdCommentsDisabledFieldname.left = new FormAttachment( 0, -margin );
   fdCommentsDisabledFieldname.top = new FormAttachment( wAuthorIdFieldname, 2 * margin );
   fdCommentsDisabledFieldname.right = new FormAttachment( 100, -margin );
   wCommentsDisabledFieldname.setLayoutData( fdCommentsDisabledFieldname );

   // outdatedFieldname
   wOutdatedFieldname =
     new LabelTextVar(
       transMeta, shell, BaseMessages.getString( PKG, "ZendeskInputHCArticlesDialog.OutdatedFieldname.Label" ),
       BaseMessages.getString( PKG, "ZendeskInputHCArticlesDialog.OutdatedFieldname.Tooltip" ) );
   props.setLook( wOutdatedFieldname );
   wOutdatedFieldname.addModifyListener( lsMod );
   FormData fdOutdatedFieldname = new FormData();
   fdOutdatedFieldname.left = new FormAttachment( 0, -margin );
   fdOutdatedFieldname.top = new FormAttachment( wCommentsDisabledFieldname, 2 * margin );
   fdOutdatedFieldname.right = new FormAttachment( 100, -margin );
   wOutdatedFieldname.setLayoutData( fdOutdatedFieldname );

   // labelsFieldname
   wLabelsFieldname =
     new LabelTextVar(
       transMeta, shell, BaseMessages.getString( PKG, "ZendeskInputHCArticlesDialog.LabelsFieldname.Label" ),
       BaseMessages.getString( PKG, "ZendeskInputHCArticlesDialog.LabelsFieldname.Tooltip" ) );
   props.setLook( wLabelsFieldname );
   wLabelsFieldname.addModifyListener( lsMod );
   FormData fdLabelsFieldname = new FormData();
   fdLabelsFieldname.left = new FormAttachment( 0, -margin );
   fdLabelsFieldname.top = new FormAttachment( wOutdatedFieldname, 2 * margin );
   fdLabelsFieldname.right = new FormAttachment( 100, -margin );
   wLabelsFieldname.setLayoutData( fdLabelsFieldname );

   // draftFieldname
   wDraftFieldname =
     new LabelTextVar(
       transMeta, shell, BaseMessages.getString( PKG, "ZendeskInputHCArticlesDialog.DraftFieldname.Label" ),
       BaseMessages.getString( PKG, "ZendeskInputHCArticlesDialog.DraftFieldname.Tooltip" ) );
   props.setLook( wDraftFieldname );
   wDraftFieldname.addModifyListener( lsMod );
   FormData fdDraftFieldname = new FormData();
   fdDraftFieldname.left = new FormAttachment( 0, -margin );
   fdDraftFieldname.top = new FormAttachment( wLabelsFieldname, 2 * margin );
   fdDraftFieldname.right = new FormAttachment( 100, -margin );
   wDraftFieldname.setLayoutData( fdDraftFieldname );

   // promotedFieldname
   wPromotedFieldname =
     new LabelTextVar(
       transMeta, shell, BaseMessages.getString( PKG, "ZendeskInputHCArticlesDialog.PromotedFieldname.Label" ),
       BaseMessages.getString( PKG, "ZendeskInputHCArticlesDialog.PromotedFieldname.Tooltip" ) );
   props.setLook( wPromotedFieldname );
   wPromotedFieldname.addModifyListener( lsMod );
   FormData fdPromotedFieldname = new FormData();
   fdPromotedFieldname.left = new FormAttachment( 0, -margin );
   fdPromotedFieldname.top = new FormAttachment( wDraftFieldname, 2 * margin );
   fdPromotedFieldname.right = new FormAttachment( 100, -margin );
   wPromotedFieldname.setLayoutData( fdPromotedFieldname );

   // positionFieldname
   wPositionFieldname =
     new LabelTextVar(
       transMeta, shell, BaseMessages.getString( PKG, "ZendeskInputHCArticlesDialog.PositionFieldname.Label" ),
       BaseMessages.getString( PKG, "ZendeskInputHCArticlesDialog.PositionFieldname.Tooltip" ) );
   props.setLook( wPositionFieldname );
   wPositionFieldname.addModifyListener( lsMod );
   FormData fdPositionFieldname = new FormData();
   fdPositionFieldname.left = new FormAttachment( 0, -margin );
   fdPositionFieldname.top = new FormAttachment( wPromotedFieldname, 2 * margin );
   fdPositionFieldname.right = new FormAttachment( 100, -margin );
   wPositionFieldname.setLayoutData( fdPositionFieldname );

   // voteSumFieldname
   wVoteSumFieldname =
     new LabelTextVar(
       transMeta, shell, BaseMessages.getString( PKG, "ZendeskInputHCArticlesDialog.VoteSumFieldname.Label" ),
       BaseMessages.getString( PKG, "ZendeskInputHCArticlesDialog.VoteSumFieldname.Tooltip" ) );
   props.setLook( wVoteSumFieldname );
   wVoteSumFieldname.addModifyListener( lsMod );
   FormData fdVoteSumFieldname = new FormData();
   fdVoteSumFieldname.left = new FormAttachment( 0, -margin );
   fdVoteSumFieldname.top = new FormAttachment( wPositionFieldname, 2 * margin );
   fdVoteSumFieldname.right = new FormAttachment( 100, -margin );
   wVoteSumFieldname.setLayoutData( fdVoteSumFieldname );

   // voteCountFieldname
   wVoteCountFieldname =
     new LabelTextVar(
       transMeta, shell, BaseMessages.getString( PKG, "ZendeskInputHCArticlesDialog.VoteCountFieldname.Label" ),
       BaseMessages.getString( PKG, "ZendeskInputHCArticlesDialog.VoteCountFieldname.Tooltip" ) );
   props.setLook( wVoteCountFieldname );
   wVoteCountFieldname.addModifyListener( lsMod );
   FormData fdVoteCountFieldname = new FormData();
   fdVoteCountFieldname.left = new FormAttachment( 0, -margin );
   fdVoteCountFieldname.top = new FormAttachment( wVoteSumFieldname, 2 * margin );
   fdVoteCountFieldname.right = new FormAttachment( 100, -margin );
   wVoteCountFieldname.setLayoutData( fdVoteCountFieldname );

   // sectionIdFieldname
   wSectionIdFieldname =
     new LabelTextVar(
       transMeta, shell, BaseMessages.getString( PKG, "ZendeskInputHCArticlesDialog.SectionIDFieldname.Label" ),
       BaseMessages.getString( PKG, "ZendeskInputHCArticlesDialog.SectionIDFieldname.Tooltip" ) );
   props.setLook( wSectionIdFieldname );
   wSectionIdFieldname.addModifyListener( lsMod );
   FormData fdSectionIdFieldname = new FormData();
   fdSectionIdFieldname.left = new FormAttachment( 0, -margin );
   fdSectionIdFieldname.top = new FormAttachment( wVoteCountFieldname, 2 * margin );
   fdSectionIdFieldname.right = new FormAttachment( 100, -margin );
   wSectionIdFieldname.setLayoutData( fdSectionIdFieldname );

   // createdAtFieldname
   wCreatedAtFieldname =
     new LabelTextVar(
       transMeta, shell, BaseMessages.getString( PKG, "ZendeskInputHCArticlesDialog.CreatedAtFieldname.Label" ),
       BaseMessages.getString( PKG, "ZendeskInputHCArticlesDialog.CreatedAtFieldname.Tooltip" ) );
   props.setLook( wCreatedAtFieldname );
   wCreatedAtFieldname.addModifyListener( lsMod );
   FormData fdCreatedAtFieldname = new FormData();
   fdCreatedAtFieldname.left = new FormAttachment( 0, -margin );
   fdCreatedAtFieldname.top = new FormAttachment( wSectionIdFieldname, 2 * margin );
   fdCreatedAtFieldname.right = new FormAttachment( 100, -margin );
   wCreatedAtFieldname.setLayoutData( fdCreatedAtFieldname );

   // updatedAtFieldname
   wUpdatedAtFieldname =
     new LabelTextVar(
       transMeta, shell, BaseMessages.getString( PKG, "ZendeskInputHCArticlesDialog.UpdatedAtFieldname.Label" ),
       BaseMessages.getString( PKG, "ZendeskInputHCArticlesDialog.UpdatedAtFieldname.Tooltip" ) );
   props.setLook( wUpdatedAtFieldname );
   wUpdatedAtFieldname.addModifyListener( lsMod );
   FormData fdUpdatedAtFieldname = new FormData();
   fdUpdatedAtFieldname.left = new FormAttachment( 0, -margin );
   fdUpdatedAtFieldname.top = new FormAttachment( wCreatedAtFieldname, 2 * margin );
   fdUpdatedAtFieldname.right = new FormAttachment( 100, -margin );
   wUpdatedAtFieldname.setLayoutData( fdUpdatedAtFieldname );

   // Some buttons
   wOK = new Button( shell, SWT.PUSH );
   wOK.setText( BaseMessages.getString( PKG, "System.Button.OK" ) );
   wCancel = new Button( shell, SWT.PUSH );
   wCancel.setText( BaseMessages.getString( PKG, "System.Button.Cancel" ) );

   setButtonPositions( new Button[] { wOK, wCancel }, margin, wUpdatedAtFieldname );

   // Add listeners
   lsCancel = new Listener() {
     public void handleEvent( Event e ) {
       cancel();
     }
   };
   lsOK = new Listener() {
     public void handleEvent( Event e ) {
       ok();
     }
   };

   wCancel.addListener( SWT.Selection, lsCancel );
   wOK.addListener( SWT.Selection, lsOK );

   lsDef = new SelectionAdapter() {
     public void widgetDefaultSelected( SelectionEvent e ) {
       ok();
     }
   };

   wStepname.addSelectionListener( lsDef );

   // Detect X or ALT-F4 or something that kills this window...
   shell.addShellListener( new ShellAdapter() {
     public void shellClosed( ShellEvent e ) {
       cancel();
     }
   } );

   // Set the shell size, based upon previous time...
   setSize();

   getData();
   input.setChanged( changed );

   shell.open();
   while ( !shell.isDisposed() ) {
     if ( !display.readAndDispatch() ) {
       display.sleep();
     }
   }
   return stepname;
 }

 /**
  * Copy information from the meta-data input to the dialog fields.
  */
 public void getData() {
   wSubDomain.setText( Const.NVL( input.getSubDomain(), "" ) );
   wUsername.setText( Const.NVL( input.getUsername(), "" ) );
   wPassword.setText( Const.NVL( input.getPassword(), "" ) );
   wToken.setSelection( input.isToken() );
   wArticleIdFieldname.setText( Const.NVL( input.getArticleIdFieldname(), "" ) );
   wArticleUrlFieldname.setText( Const.NVL( input.getArticleUrlFieldname(), "" ) );
   wArticleTitleFieldname.setText( Const.NVL( input.getArticleTitleFieldname(), "" ) );
   wArticleBodyFieldname.setText( Const.NVL( input.getArticleBodyFieldname(), "" ) );
   wLocaleFieldname.setText( Const.NVL( input.getLocaleFieldname(), "" ) );
   wSourceLocaleFieldname.setText( Const.NVL( input.getSourceLocaleFieldname(), "" ) );
   wAuthorIdFieldname.setText( Const.NVL( input.getAuthorIdFieldname(), "" ) );
   wCommentsDisabledFieldname.setText( Const.NVL( input.getCommentsDisabledFieldname(), "" ) );
   wOutdatedFieldname.setText( Const.NVL( input.getOutdatedFieldname(), "" ) );
   wLabelsFieldname.setText( Const.NVL( input.getLabelsFieldname(), "" ) );
   wDraftFieldname.setText( Const.NVL( input.getDraftFieldname(), "" ) );
   wPromotedFieldname.setText( Const.NVL( input.getPromotedFieldname(), "" ) );
   wPositionFieldname.setText( Const.NVL( input.getPositionFieldname(), "" ) );
   wVoteSumFieldname.setText( Const.NVL( input.getVoteSumFieldname(), "" ) );
   wVoteCountFieldname.setText( Const.NVL( input.getVoteCountFieldname(), "" ) );
   wSectionIdFieldname.setText( Const.NVL( input.getSectionIdFieldname(), "" ) );
   wCreatedAtFieldname.setText( Const.NVL( input.getCreatedAtFieldname(), "" ) );
   wUpdatedAtFieldname.setText( Const.NVL( input.getUpdatedAtFieldname(), "" ) );

   wStepname.selectAll();
   wStepname.setFocus();
 }

 private void cancel() {
   stepname = null;
   input.setChanged( changed );
   dispose();
 }

 private void ok() {

   if ( Const.isEmpty( wStepname.getText() ) ) {
     return;
   }

   // Get the information for the dialog into the input structure.
   getInfo( input );

   dispose();
 }

 private void getInfo( ZendeskInputHCArticleMeta inf ) {
   inf.setSubDomain( wSubDomain.getText() );
   inf.setUsername( wUsername.getText() );
   inf.setPassword( wPassword.getText() );
   inf.setToken( wToken.getSelection() );
   inf.setArticleIdFieldname( wArticleIdFieldname.getText() );
   inf.setArticleUrlFieldname( wArticleUrlFieldname.getText() );
   inf.setArticleTitleFieldname( wArticleTitleFieldname.getText() );
   inf.setArticleBodyFieldname( wArticleBodyFieldname.getText() );
   inf.setLocaleFieldname( wLocaleFieldname.getText() );
   inf.setSourceLocaleFieldname( wSourceLocaleFieldname.getText() );
   inf.setAuthorIdFieldname( wAuthorIdFieldname.getText() );
   inf.setCommentsDisabledFieldname( wCommentsDisabledFieldname.getText() );
   inf.setOutdatedFieldname( wOutdatedFieldname.getText() );
   inf.setLabelsFieldname( wLabelsFieldname.getText() );
   inf.setDraftFieldname( wDraftFieldname.getText() );
   inf.setPromotedFieldname( wPromotedFieldname.getText() );
   inf.setPositionFieldname( wPositionFieldname.getText() );
   inf.setVoteSumFieldname( wVoteSumFieldname.getText() );
   inf.setVoteCountFieldname( wVoteCountFieldname.getText() );
   inf.setSectionIdFieldname( wSectionIdFieldname.getText() );
   inf.setCreatedAtFieldname( wCreatedAtFieldname.getText() );
   inf.setUpdatedAtFieldname( wUpdatedAtFieldname.getText() );

   stepname = wStepname.getText(); // return value
 }
}
