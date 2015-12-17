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
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
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
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.pentaho.di.core.Const;
import org.pentaho.di.core.Props;
import org.pentaho.di.i18n.BaseMessages;
import org.pentaho.di.trans.TransMeta;
import org.pentaho.di.trans.step.BaseStepMeta;
import org.pentaho.di.trans.step.StepDialogInterface;
import org.pentaho.di.trans.steps.zendesk.ZendeskInputHCTranslationMeta;
import org.pentaho.di.ui.core.widget.LabelTextVar;
import org.pentaho.di.ui.core.widget.PasswordTextVar;
import org.pentaho.di.ui.trans.step.BaseStepDialog;

public class ZendeskInputHCTranslationDialog extends BaseStepDialog implements StepDialogInterface {

 private static Class<?> PKG = ZendeskInputHCTranslationMeta.class; // for i18n purposes, needed by Translator2!!
 private ZendeskInputHCTranslationMeta input;

 private CTabFolder wTabFolder;
 private CTabItem wGeneralTab, wTranslationTab;
 private Composite wGeneralComp, wTranslationComp;

 private LabelTextVar wSubDomain, wUsername;
 private Label wlPassword, wlToken;
 private PasswordTextVar wPassword;
 private Button wToken;

 private LabelTextVar wTranslationIdFieldname;
 private LabelTextVar wTranslationUrlFieldname;
 private LabelTextVar wTranslationTitleFieldname;
 private LabelTextVar wTranslationBodyFieldname;
 private LabelTextVar wLocaleFieldname;
 private LabelTextVar wSourceIdFieldname;
 private LabelTextVar wSourceTypeFieldname;
 private LabelTextVar wOutdatedFieldname;
 private LabelTextVar wDraftFieldname;
 private LabelTextVar wCreatedAtFieldname;
 private LabelTextVar wCreatedByFieldname;
 private LabelTextVar wUpdatedAtFieldname;
 private LabelTextVar wUpdatedByFieldname;

 public ZendeskInputHCTranslationDialog( Shell parent, Object in, TransMeta tr, String sname ) {
   super( parent, (BaseStepMeta) in, tr, sname );
   input = (ZendeskInputHCTranslationMeta) in;
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
   shell.setText( BaseMessages.getString( PKG, "ZendeskInputHCTranslations.Shell.Title" ) );

   int middle = props.getMiddlePct();
   int margin = Const.MARGIN;

   // Stepname line
   wlStepname = new Label( shell, SWT.RIGHT );
   wlStepname.setText( BaseMessages.getString( PKG, "ZendeskInputDialog.Stepname.Label" ) );
   props.setLook( wlStepname );
   FormData fdlStepname = new FormData();
   fdlStepname.left = new FormAttachment( 0, 0 );
   fdlStepname.right = new FormAttachment( middle, -margin );
   fdlStepname.top = new FormAttachment( 0, margin );
   wlStepname.setLayoutData( fdlStepname );
   wStepname = new Text( shell, SWT.SINGLE | SWT.LEFT | SWT.BORDER );
   wStepname.setText( stepname );
   props.setLook( wStepname );
   wStepname.addModifyListener( lsMod );
   FormData fdStepname = new FormData();
   fdStepname.left = new FormAttachment( middle, 0 );
   fdStepname.top = new FormAttachment( 0, margin );
   fdStepname.right = new FormAttachment( 100, 0 );
   wStepname.setLayoutData( fdStepname );

   // The Tab Folders
   wTabFolder = new CTabFolder( shell, SWT.BORDER );
   props.setLook(  wTabFolder, Props.WIDGET_STYLE_TAB );

   // ///////////////////////
   // START OF GENERAL TAB //
   // ///////////////////////

   wGeneralTab = new CTabItem( wTabFolder, SWT.NONE );
   wGeneralTab.setText( BaseMessages.getString( PKG, "ZendeskInputDialog.GeneralTab.TabItem" ) );

   wGeneralComp = new Composite( wTabFolder, SWT.NONE );
   props.setLook( wGeneralComp );

   FormLayout generalLayout = new FormLayout();
   generalLayout.marginWidth = margin;
   generalLayout.marginHeight = margin;
   wGeneralComp.setLayout( generalLayout );
   
   // Subdomain
   wSubDomain = new LabelTextVar( transMeta, wGeneralComp,
     BaseMessages.getString( PKG, "ZendeskInputDialog.SubDomain.Label" ),
     BaseMessages.getString( PKG, "ZendeskInputDialog.SubDomain.Tooltip" ) );
   props.setLook( wSubDomain );
   wSubDomain.addModifyListener( lsMod );
   FormData fdSubDomain = new FormData();
   fdSubDomain.left = new FormAttachment( 0, -margin );
   fdSubDomain.top = new FormAttachment( 0, 2 * margin );
   fdSubDomain.right = new FormAttachment( 100, -margin );
   wSubDomain.setLayoutData( fdSubDomain );

   // Username
   wUsername =
     new LabelTextVar(
       transMeta, wGeneralComp, BaseMessages.getString( PKG, "ZendeskInputDialog.Username.Label" ),
       BaseMessages.getString( PKG, "ZendeskInputDialog.Username.Tooltip" ) );
   props.setLook( wUsername );
   wUsername.addModifyListener( lsMod );
   FormData fdUsername = new FormData();
   fdUsername.left = new FormAttachment( 0, -margin );
   fdUsername.top = new FormAttachment( wSubDomain, 2 * margin );
   fdUsername.right = new FormAttachment( 100, -margin );
   wUsername.setLayoutData( fdUsername );

   // Password
   wlPassword = new Label( wGeneralComp, SWT.RIGHT );
   wlPassword.setText( BaseMessages.getString( PKG, "ZendeskInputDialog.Password.Label" ) );
   props.setLook( wlPassword );
   FormData fdlPassword = new FormData();
   fdlPassword.left = new FormAttachment( 0, 0 );
   fdlPassword.top = new FormAttachment( wUsername, 2 * margin );
   fdlPassword.right = new FormAttachment( middle, -margin );
   wlPassword.setLayoutData( fdlPassword );
   
   wPassword = new PasswordTextVar( transMeta, wGeneralComp, SWT.SINGLE | SWT.LEFT | SWT.BORDER,
     BaseMessages.getString( PKG, "ZendeskInputDialog.Password.Tooltip" ) );
   props.setLook( wPassword );
   wPassword.addModifyListener( lsMod );
   FormData fdPassword = new FormData();
   fdPassword.left = new FormAttachment( middle, 0 );
   fdPassword.top = new FormAttachment( wUsername, margin );
   fdPassword.right = new FormAttachment( 100, -margin );
   wPassword.setLayoutData( fdPassword );

   // Token
   wlToken = new Label( wGeneralComp, SWT.RIGHT );
   wlToken.setText( BaseMessages.getString( PKG, "ZendeskInputDialog.Token.Label" ) );
   props.setLook( wlToken );
   FormData fdlToken = new FormData();
   fdlToken.left = new FormAttachment( 0, 0 );
   fdlToken.top = new FormAttachment( wlPassword, 2 * margin );
   fdlToken.right = new FormAttachment( middle, -margin );
   wlToken.setLayoutData( fdlToken );

   wToken = new Button( wGeneralComp, SWT.CHECK );
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

   FormData fdGeneralComp = new FormData();
   fdGeneralComp.left = new FormAttachment( 0, 0 );
   fdGeneralComp.top = new FormAttachment( 0, 0 );
   fdGeneralComp.right = new FormAttachment( 100, 0 );
   fdGeneralComp.bottom = new FormAttachment( 100, 0 );
   wGeneralComp.setLayoutData( fdGeneralComp );

   wGeneralComp.layout();
   wGeneralTab.setControl( wGeneralComp );

   // /////////////////////
   // END OF GENERAL TAB //
   // /////////////////////

   // ///////////////////////////
   // START OF TRANSLATION TAB //
   // ///////////////////////////

   wTranslationTab = new CTabItem( wTabFolder, SWT.NONE );
   wTranslationTab.setText( BaseMessages.getString( PKG, "ZendeskInputHCTranslationsDialog.TranslationTab.TabItem" ) );

   wTranslationComp = new Composite( wTabFolder, SWT.NONE );
   props.setLook( wTranslationComp );

   FormLayout userLayout = new FormLayout();
   userLayout.marginWidth = margin;
   userLayout.marginHeight = margin;
   wTranslationComp.setLayout( userLayout );

   // translationIdFieldname
   wTranslationIdFieldname =
     new LabelTextVar(
       transMeta, wTranslationComp,
       BaseMessages.getString( PKG, "ZendeskInputHCTranslationsDialog.TranslationIdFieldname.Label" ),
       BaseMessages.getString( PKG, "ZendeskInputHCTranslationsDialog.TranslationIdFieldname.Tooltip" ) );
   props.setLook( wTranslationIdFieldname );
   wTranslationIdFieldname.addModifyListener( lsMod );
   FormData fdTranslationIdFieldname = new FormData();
   fdTranslationIdFieldname.left = new FormAttachment( 0, -margin );
   fdTranslationIdFieldname.top = new FormAttachment( 0, 2 * margin );
   fdTranslationIdFieldname.right = new FormAttachment( 100, -margin );
   wTranslationIdFieldname.setLayoutData( fdTranslationIdFieldname );

   // translationUrlFieldname
   wTranslationUrlFieldname =
     new LabelTextVar(
       transMeta, wTranslationComp,
       BaseMessages.getString( PKG, "ZendeskInputHCTranslationsDialog.TranslationURLFieldname.Label" ),
       BaseMessages.getString( PKG, "ZendeskInputHCTranslationsDialog.TranslationURLFieldname.Tooltip" ) );
   props.setLook( wTranslationUrlFieldname );
   wTranslationUrlFieldname.addModifyListener( lsMod );
   FormData fdTranslationUrlFieldname = new FormData();
   fdTranslationUrlFieldname.left = new FormAttachment( 0, -margin );
   fdTranslationUrlFieldname.top = new FormAttachment( wTranslationIdFieldname, 2 * margin );
   fdTranslationUrlFieldname.right = new FormAttachment( 100, -margin );
   wTranslationUrlFieldname.setLayoutData( fdTranslationUrlFieldname );

   // translationTitleFieldname
   wTranslationTitleFieldname =
     new LabelTextVar(
       transMeta, wTranslationComp,
       BaseMessages.getString( PKG, "ZendeskInputHCTranslationsDialog.TranslationTitleFieldname.Label" ),
       BaseMessages.getString( PKG, "ZendeskInputHCTranslationsDialog.TranslationTitleFieldname.Tooltip" ) );
   props.setLook( wTranslationTitleFieldname );
   wTranslationTitleFieldname.addModifyListener( lsMod );
   FormData fdTranslationTitleFieldname = new FormData();
   fdTranslationTitleFieldname.left = new FormAttachment( 0, -margin );
   fdTranslationTitleFieldname.top = new FormAttachment( wTranslationUrlFieldname, 2 * margin );
   fdTranslationTitleFieldname.right = new FormAttachment( 100, -margin );
   wTranslationTitleFieldname.setLayoutData( fdTranslationTitleFieldname );

   // translationBodyFieldname
   wTranslationBodyFieldname =
     new LabelTextVar(
       transMeta, wTranslationComp,
       BaseMessages.getString( PKG, "ZendeskInputHCTranslationsDialog.TranslationBodyFieldname.Label" ),
       BaseMessages.getString( PKG, "ZendeskInputHCTranslationsDialog.TranslationBodyFieldname.Tooltip" ) );
   props.setLook( wTranslationBodyFieldname );
   wTranslationBodyFieldname.addModifyListener( lsMod );
   FormData fdTranslationBodyFieldname = new FormData();
   fdTranslationBodyFieldname.left = new FormAttachment( 0, -margin );
   fdTranslationBodyFieldname.top = new FormAttachment( wTranslationTitleFieldname, 2 * margin );
   fdTranslationBodyFieldname.right = new FormAttachment( 100, -margin );
   wTranslationBodyFieldname.setLayoutData( fdTranslationBodyFieldname );

   // localeFieldname
   wLocaleFieldname =
     new LabelTextVar(
       transMeta, wTranslationComp,
       BaseMessages.getString( PKG, "ZendeskInputHCTranslationsDialog.LocaleFieldname.Label" ),
       BaseMessages.getString( PKG, "ZendeskInputHCTranslationsDialog.LocaleFieldname.Tooltip" ) );
   props.setLook( wLocaleFieldname );
   wLocaleFieldname.addModifyListener( lsMod );
   FormData fdLocaleFieldname = new FormData();
   fdLocaleFieldname.left = new FormAttachment( 0, -margin );
   fdLocaleFieldname.top = new FormAttachment( wTranslationBodyFieldname, 2 * margin );
   fdLocaleFieldname.right = new FormAttachment( 100, -margin );
   wLocaleFieldname.setLayoutData( fdLocaleFieldname );

   // sourceIdFieldname
   wSourceIdFieldname =
     new LabelTextVar(
       transMeta, wTranslationComp,
       BaseMessages.getString( PKG, "ZendeskInputHCTranslationsDialog.SourceIdFieldname.Label" ),
       BaseMessages.getString( PKG, "ZendeskInputHCTranslationsDialog.SourceIdFieldname.Tooltip" ) );
   props.setLook( wSourceIdFieldname );
   wSourceIdFieldname.addModifyListener( lsMod );
   FormData fdSourceIdFieldname = new FormData();
   fdSourceIdFieldname.left = new FormAttachment( 0, -margin );
   fdSourceIdFieldname.top = new FormAttachment( wLocaleFieldname, 2 * margin );
   fdSourceIdFieldname.right = new FormAttachment( 100, -margin );
   wSourceIdFieldname.setLayoutData( fdSourceIdFieldname );

   // sourceTypeFieldname
   wSourceTypeFieldname =
     new LabelTextVar(
       transMeta, wTranslationComp,
       BaseMessages.getString( PKG, "ZendeskInputHCTranslationsDialog.SourceTypeFieldname.Label" ),
       BaseMessages.getString( PKG, "ZendeskInputHCTranslationsDialog.SourceTypeFieldname.Tooltip" ) );
   props.setLook( wSourceTypeFieldname );
   wSourceTypeFieldname.addModifyListener( lsMod );
   FormData fdSourceTypeFieldname = new FormData();
   fdSourceTypeFieldname.left = new FormAttachment( 0, -margin );
   fdSourceTypeFieldname.top = new FormAttachment( wSourceIdFieldname, 2 * margin );
   fdSourceTypeFieldname.right = new FormAttachment( 100, -margin );
   wSourceTypeFieldname.setLayoutData( fdSourceTypeFieldname );

   // outdatedFieldname
   wOutdatedFieldname =
     new LabelTextVar(
       transMeta, wTranslationComp,
       BaseMessages.getString( PKG, "ZendeskInputHCTranslationsDialog.OutdatedFieldname.Label" ),
       BaseMessages.getString( PKG, "ZendeskInputHCTranslationsDialog.OutdatedFieldname.Tooltip" ) );
   props.setLook( wOutdatedFieldname );
   wOutdatedFieldname.addModifyListener( lsMod );
   FormData fdOutdatedFieldname = new FormData();
   fdOutdatedFieldname.left = new FormAttachment( 0, -margin );
   fdOutdatedFieldname.top = new FormAttachment( wSourceTypeFieldname, 2 * margin );
   fdOutdatedFieldname.right = new FormAttachment( 100, -margin );
   wOutdatedFieldname.setLayoutData( fdOutdatedFieldname );

   // draftFieldname
   wDraftFieldname =
     new LabelTextVar(
       transMeta, wTranslationComp,
       BaseMessages.getString( PKG, "ZendeskInputHCTranslationsDialog.DraftFieldname.Label" ),
       BaseMessages.getString( PKG, "ZendeskInputHCTranslationsDialog.DraftFieldname.Tooltip" ) );
   props.setLook( wDraftFieldname );
   wDraftFieldname.addModifyListener( lsMod );
   FormData fdDraftFieldname = new FormData();
   fdDraftFieldname.left = new FormAttachment( 0, -margin );
   fdDraftFieldname.top = new FormAttachment( wOutdatedFieldname, 2 * margin );
   fdDraftFieldname.right = new FormAttachment( 100, -margin );
   wDraftFieldname.setLayoutData( fdDraftFieldname );

   // createdAtFieldname
   wCreatedAtFieldname =
     new LabelTextVar(
       transMeta, wTranslationComp,
       BaseMessages.getString( PKG, "ZendeskInputHCTranslationsDialog.CreatedAtFieldname.Label" ),
       BaseMessages.getString( PKG, "ZendeskInputHCTranslationsDialog.CreatedAtFieldname.Tooltip" ) );
   props.setLook( wCreatedAtFieldname );
   wCreatedAtFieldname.addModifyListener( lsMod );
   FormData fdCreatedAtFieldname = new FormData();
   fdCreatedAtFieldname.left = new FormAttachment( 0, -margin );
   fdCreatedAtFieldname.top = new FormAttachment( wDraftFieldname, 2 * margin );
   fdCreatedAtFieldname.right = new FormAttachment( 100, -margin );
   wCreatedAtFieldname.setLayoutData( fdCreatedAtFieldname );

   // createdByFieldname
   wCreatedByFieldname =
     new LabelTextVar(
       transMeta, wTranslationComp,
       BaseMessages.getString( PKG, "ZendeskInputHCTranslationsDialog.CreatedByFieldname.Label" ),
       BaseMessages.getString( PKG, "ZendeskInputHCTranslationsDialog.CreatedByFieldname.Tooltip" ) );
   props.setLook( wCreatedByFieldname );
   wCreatedByFieldname.addModifyListener( lsMod );
   FormData fdCreatedByFieldname = new FormData();
   fdCreatedByFieldname.left = new FormAttachment( 0, -margin );
   fdCreatedByFieldname.top = new FormAttachment( wDraftFieldname, 2 * margin );
   fdCreatedByFieldname.right = new FormAttachment( 100, -margin );
   wCreatedByFieldname.setLayoutData( fdCreatedByFieldname );

   // updatedAtFieldname
   wUpdatedAtFieldname =
     new LabelTextVar(
       transMeta, wTranslationComp,
       BaseMessages.getString( PKG, "ZendeskInputHCTranslationsDialog.UpdatedAtFieldname.Label" ),
       BaseMessages.getString( PKG, "ZendeskInputHCTranslationsDialog.UpdatedAtFieldname.Tooltip" ) );
   props.setLook( wUpdatedAtFieldname );
   wUpdatedAtFieldname.addModifyListener( lsMod );
   FormData fdUpdatedAtFieldname = new FormData();
   fdUpdatedAtFieldname.left = new FormAttachment( 0, -margin );
   fdUpdatedAtFieldname.top = new FormAttachment( wCreatedByFieldname, 2 * margin );
   fdUpdatedAtFieldname.right = new FormAttachment( 100, -margin );
   wUpdatedAtFieldname.setLayoutData( fdUpdatedAtFieldname );

   // updatedByFieldname
   wUpdatedByFieldname =
     new LabelTextVar(
       transMeta, wTranslationComp,
       BaseMessages.getString( PKG, "ZendeskInputHCTranslationsDialog.UpdatedByFieldname.Label" ),
       BaseMessages.getString( PKG, "ZendeskInputHCTranslationsDialog.UpdatedByFieldname.Tooltip" ) );
   props.setLook( wUpdatedByFieldname );
   wUpdatedByFieldname.addModifyListener( lsMod );
   FormData fdUpdatedByFieldname = new FormData();
   fdUpdatedByFieldname.left = new FormAttachment( 0, -margin );
   fdUpdatedByFieldname.top = new FormAttachment( wUpdatedAtFieldname, 2 * margin );
   fdUpdatedByFieldname.right = new FormAttachment( 100, -margin );
   wUpdatedByFieldname.setLayoutData( fdUpdatedByFieldname );

   FormData fdUserComp = new FormData();
   fdUserComp.left = new FormAttachment( 0, 0 );
   fdUserComp.top = new FormAttachment( 0, 0 );
   fdUserComp.right = new FormAttachment( 100, 0 );
   fdUserComp.bottom = new FormAttachment( 100, 0 );
   wTranslationComp.setLayoutData( fdUserComp );

   wTranslationComp.layout();
   wTranslationTab.setControl( wTranslationComp );

   // /////////////////////////
   // END OF TRANSLATION TAB //
   // /////////////////////////

   FormData fdTabFolder = new FormData();
   fdTabFolder.left = new FormAttachment( 0, 0 );
   fdTabFolder.top = new FormAttachment( wStepname, margin );
   fdTabFolder.right = new FormAttachment( 100, 0 );
   fdTabFolder.bottom = new FormAttachment( 100, -50 );
   wTabFolder.setLayoutData( fdTabFolder );

   wTabFolder.setSelection( 0 );

   // ////////////////////
   // END OF TAB FOLDER //
   // ////////////////////

   // Some buttons
   wOK = new Button( shell, SWT.PUSH );
   wOK.setText( BaseMessages.getString( PKG, "System.Button.OK" ) );
   wCancel = new Button( shell, SWT.PUSH );
   wCancel.setText( BaseMessages.getString( PKG, "System.Button.Cancel" ) );

   setButtonPositions( new Button[] { wOK, wCancel }, margin, wTabFolder );

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
   wTranslationIdFieldname.setText( Const.NVL( input.getTranslationIdFieldname(), "" ) );
   wTranslationUrlFieldname.setText( Const.NVL( input.getTranslationUrlFieldname(), "" ) );
   wTranslationTitleFieldname.setText( Const.NVL( input.getTranslationTitleFieldname(), "" ) );
   wTranslationBodyFieldname.setText( Const.NVL( input.getTranslationBodyFieldname(), "" ) );
   wLocaleFieldname.setText( Const.NVL( input.getLocaleFieldname(), "" ) );
   wSourceIdFieldname.setText( Const.NVL( input.getSourceIdFieldname(), "" ) );
   wSourceTypeFieldname.setText( Const.NVL( input.getSourceTypeFieldname(), "" ) );
   wOutdatedFieldname.setText( Const.NVL( input.getOutdatedFieldname(), "" ) );
   wDraftFieldname.setText( Const.NVL( input.getDraftFieldname(), "" ) );
   wCreatedAtFieldname.setText( Const.NVL( input.getCreatedAtFieldname(), "" ) );
   wCreatedByFieldname.setText( Const.NVL( input.getCreatedByFieldname(), "" ) );
   wUpdatedAtFieldname.setText( Const.NVL( input.getUpdatedAtFieldname(), "" ) );
   wUpdatedByFieldname.setText( Const.NVL( input.getUpdatedByFieldname(), "" ) );

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

 private void getInfo( ZendeskInputHCTranslationMeta inf ) {
   inf.setSubDomain( wSubDomain.getText() );
   inf.setUsername( wUsername.getText() );
   inf.setPassword( wPassword.getText() );
   inf.setToken( wToken.getSelection() );
   inf.setTranslationIdFieldname(wTranslationIdFieldname.getText() );
   inf.setTranslationUrlFieldname(wTranslationUrlFieldname.getText() );
   inf.setTranslationTitleFieldname(wTranslationTitleFieldname.getText() );
   inf.setTranslationBodyFieldname(wTranslationBodyFieldname.getText() );
   inf.setLocaleFieldname(wLocaleFieldname.getText() );
   inf.setSourceIdFieldname(wSourceIdFieldname.getText() );
   inf.setSourceTypeFieldname(wSourceTypeFieldname.getText() );
   inf.setOutdatedFieldname(wOutdatedFieldname.getText() );
   inf.setDraftFieldname(wDraftFieldname.getText() );
   inf.setCreatedAtFieldname(wCreatedAtFieldname.getText() );
   inf.setCreatedByFieldname(wCreatedByFieldname.getText() );
   inf.setUpdatedAtFieldname(wUpdatedAtFieldname.getText() );
   inf.setUpdatedByFieldname(wUpdatedByFieldname.getText() );

   stepname = wStepname.getText(); // return value
 }
}
