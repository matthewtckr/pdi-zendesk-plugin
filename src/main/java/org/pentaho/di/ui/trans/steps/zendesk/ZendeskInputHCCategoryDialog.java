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
import org.pentaho.di.trans.steps.zendesk.ZendeskInputHCCategoryMeta;
import org.pentaho.di.ui.core.widget.LabelTextVar;
import org.pentaho.di.ui.core.widget.PasswordTextVar;
import org.pentaho.di.ui.trans.step.BaseStepDialog;

public class ZendeskInputHCCategoryDialog extends BaseStepDialog implements StepDialogInterface {

 private static Class<?> PKG = ZendeskInputHCCategoryMeta.class; // for i18n purposes, needed by Translator2!!
 private ZendeskInputHCCategoryMeta input;

 private LabelTextVar wSubDomain, wUsername;
 private Label wlPassword, wlToken;
 private PasswordTextVar wPassword;
 private Button wToken;
 private FormData fdSubDomain, fdUsername, fdPassword, fdlToken, fdToken;

 private LabelTextVar wCategoryIdFieldname;
 private LabelTextVar wCategoryUrlFieldname;
 private LabelTextVar wCategoryNameFieldname;
 private LabelTextVar wDescriptionFieldname;
 private LabelTextVar wLocaleFieldname;
 private LabelTextVar wSourceLocaleFieldname;
 private LabelTextVar wCategoryHtmlUrlFieldname;
 private LabelTextVar wOutdatedFieldname;
 private LabelTextVar wPositionFieldname;
 private LabelTextVar wCreatedAtFieldname;
 private LabelTextVar wUpdatedAtFieldname;

 private FormData fdCategoryIdFieldname;
 private FormData fdCategoryUrlFieldname;
 private FormData fdCategoryNameFieldname;
 private FormData fdDescriptionFieldname;
 private FormData fdLocaleFieldname;
 private FormData fdSourceLocaleFieldname;
 private FormData fdCategoryHtmlUrlFieldname;
 private FormData fdOutdatedFieldname;
 private FormData fdPositionFieldname;
 private FormData fdCreatedAtFieldname;
 private FormData fdUpdatedAtFieldname;

 public ZendeskInputHCCategoryDialog( Shell parent, Object in, TransMeta tr, String sname ) {
   super( parent, (BaseStepMeta) in, tr, sname );
   input = (ZendeskInputHCCategoryMeta) in;
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
   shell.setText( BaseMessages.getString( PKG, "ZendeskInputHCCategories.Shell.Title" ) );

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
   fdSubDomain = new FormData();
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
   fdUsername = new FormData();
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
   fdPassword = new FormData();
   fdPassword.left = new FormAttachment( middle, 0 );
   fdPassword.top = new FormAttachment( wUsername, margin );
   fdPassword.right = new FormAttachment( 100, -margin );
   wPassword.setLayoutData( fdPassword );

   // Token
   wlToken = new Label( shell, SWT.RIGHT );
   wlToken.setText( BaseMessages.getString( PKG, "ZendeskInputDialog.Token.Label" ) );
   props.setLook( wlToken );
   fdlToken = new FormData();
   fdlToken.left = new FormAttachment( 0, 0 );
   fdlToken.top = new FormAttachment( wlPassword, 2 * margin );
   fdlToken.right = new FormAttachment( middle, -margin );
   wlToken.setLayoutData( fdlToken );

   wToken = new Button( shell, SWT.CHECK );
   props.setLook( wToken );
   wToken.setToolTipText( BaseMessages.getString( PKG, "ZendeskInputDialog.Token.Tooltip" ) );
   fdToken = new FormData();
   fdToken.left = new FormAttachment( middle, 0 );
   fdToken.top = new FormAttachment( wPassword, margin );
   fdToken.right = new FormAttachment( 100, -margin );
   wToken.setLayoutData( fdToken );
   wToken.addSelectionListener( new SelectionAdapter() {
     public void widgetSelected(SelectionEvent e) {
       input.setChanged();
     }
   } );

   // categoryIdFieldname
   wCategoryIdFieldname =
     new LabelTextVar(
       transMeta, shell, BaseMessages.getString( PKG, "ZendeskInputHCCategoryDialog.CategoryIdFieldname.Label" ),
       BaseMessages.getString( PKG, "ZendeskInputHCCategoryDialog.CategoryIdFieldname.Tooltip" ) );
   props.setLook( wCategoryIdFieldname );
   wCategoryIdFieldname.addModifyListener( lsMod );
   fdCategoryIdFieldname = new FormData();
   fdCategoryIdFieldname.left = new FormAttachment( 0, -margin );
   fdCategoryIdFieldname.top = new FormAttachment( wToken, 2 * margin );
   fdCategoryIdFieldname.right = new FormAttachment( 100, -margin );
   wCategoryIdFieldname.setLayoutData( fdCategoryIdFieldname );

   // categoryUrlFieldname
   wCategoryUrlFieldname =
     new LabelTextVar(
       transMeta, shell, BaseMessages.getString( PKG, "ZendeskInputHCCategoryDialog.CategoryURLFieldname.Label" ),
       BaseMessages.getString( PKG, "ZendeskInputHCCategoryDialog.CategoryURLFieldname.Tooltip" ) );
   props.setLook( wCategoryUrlFieldname );
   wCategoryUrlFieldname.addModifyListener( lsMod );
   fdCategoryUrlFieldname = new FormData();
   fdCategoryUrlFieldname.left = new FormAttachment( 0, -margin );
   fdCategoryUrlFieldname.top = new FormAttachment( wCategoryIdFieldname, 2 * margin );
   fdCategoryUrlFieldname.right = new FormAttachment( 100, -margin );
   wCategoryUrlFieldname.setLayoutData( fdCategoryUrlFieldname );

   // categoryNameFieldname
   wCategoryNameFieldname =
     new LabelTextVar(
       transMeta, shell, BaseMessages.getString( PKG, "ZendeskInputHCCategoryDialog.CategoryNameFieldname.Label" ),
       BaseMessages.getString( PKG, "ZendeskInputHCCategoryDialog.CategoryNameFieldname.Tooltip" ) );
   props.setLook( wCategoryNameFieldname );
   wCategoryNameFieldname.addModifyListener( lsMod );
   fdCategoryNameFieldname = new FormData();
   fdCategoryNameFieldname.left = new FormAttachment( 0, -margin );
   fdCategoryNameFieldname.top = new FormAttachment( wCategoryUrlFieldname, 2 * margin );
   fdCategoryNameFieldname.right = new FormAttachment( 100, -margin );
   wCategoryNameFieldname.setLayoutData( fdCategoryNameFieldname );

   // descriptionFieldname
   wDescriptionFieldname =
     new LabelTextVar(
       transMeta, shell, BaseMessages.getString( PKG, "ZendeskInputHCCategoryDialog.DescriptionFieldname.Label" ),
       BaseMessages.getString( PKG, "ZendeskInputHCCategoryDialog.DescriptionFieldname.Tooltip" ) );
   props.setLook( wDescriptionFieldname );
   wDescriptionFieldname.addModifyListener( lsMod );
   fdDescriptionFieldname = new FormData();
   fdDescriptionFieldname.left = new FormAttachment( 0, -margin );
   fdDescriptionFieldname.top = new FormAttachment( wCategoryNameFieldname, 2 * margin );
   fdDescriptionFieldname.right = new FormAttachment( 100, -margin );
   wDescriptionFieldname.setLayoutData( fdDescriptionFieldname );

   // localeFieldname
   wLocaleFieldname =
     new LabelTextVar(
       transMeta, shell, BaseMessages.getString( PKG, "ZendeskInputHCCategoryDialog.LocaleFieldname.Label" ),
       BaseMessages.getString( PKG, "ZendeskInputHCCategoryDialog.LocaleFieldname.Tooltip" ) );
   props.setLook( wLocaleFieldname );
   wLocaleFieldname.addModifyListener( lsMod );
   fdLocaleFieldname = new FormData();
   fdLocaleFieldname.left = new FormAttachment( 0, -margin );
   fdLocaleFieldname.top = new FormAttachment( wDescriptionFieldname, 2 * margin );
   fdLocaleFieldname.right = new FormAttachment( 100, -margin );
   wLocaleFieldname.setLayoutData( fdLocaleFieldname );

   // sourceLocaleFieldname
   wSourceLocaleFieldname =
     new LabelTextVar(
       transMeta, shell, BaseMessages.getString( PKG, "ZendeskInputHCCategoryDialog.SourceLocaleFieldname.Label" ),
       BaseMessages.getString( PKG, "ZendeskInputHCCategoryDialog.SourceLocaleFieldname.Tooltip" ) );
   props.setLook( wSourceLocaleFieldname );
   wSourceLocaleFieldname.addModifyListener( lsMod );
   fdSourceLocaleFieldname = new FormData();
   fdSourceLocaleFieldname.left = new FormAttachment( 0, -margin );
   fdSourceLocaleFieldname.top = new FormAttachment( wLocaleFieldname, 2 * margin );
   fdSourceLocaleFieldname.right = new FormAttachment( 100, -margin );
   wSourceLocaleFieldname.setLayoutData( fdSourceLocaleFieldname );

   // categoryHtmlUrlFieldname
   wCategoryHtmlUrlFieldname =
     new LabelTextVar(
       transMeta, shell, BaseMessages.getString( PKG, "ZendeskInputHCCategoryDialog.CategoryHTMLURLFieldname.Label" ),
       BaseMessages.getString( PKG, "ZendeskInputHCCategoryDialog.CategoryHTMLURLFieldname.Tooltip" ) );
   props.setLook( wCategoryHtmlUrlFieldname );
   wCategoryHtmlUrlFieldname.addModifyListener( lsMod );
   fdCategoryHtmlUrlFieldname = new FormData();
   fdCategoryHtmlUrlFieldname.left = new FormAttachment( 0, -margin );
   fdCategoryHtmlUrlFieldname.top = new FormAttachment( wSourceLocaleFieldname, 2 * margin );
   fdCategoryHtmlUrlFieldname.right = new FormAttachment( 100, -margin );
   wCategoryHtmlUrlFieldname.setLayoutData( fdCategoryHtmlUrlFieldname );

   // outdatedFieldname
   wOutdatedFieldname =
     new LabelTextVar(
       transMeta, shell, BaseMessages.getString( PKG, "ZendeskInputHCCategoryDialog.OutdatedFieldname.Label" ),
       BaseMessages.getString( PKG, "ZendeskInputHCCategoryDialog.OutdatedFieldname.Tooltip" ) );
   props.setLook( wOutdatedFieldname );
   wOutdatedFieldname.addModifyListener( lsMod );
   fdOutdatedFieldname = new FormData();
   fdOutdatedFieldname.left = new FormAttachment( 0, -margin );
   fdOutdatedFieldname.top = new FormAttachment( wCategoryHtmlUrlFieldname, 2 * margin );
   fdOutdatedFieldname.right = new FormAttachment( 100, -margin );
   wOutdatedFieldname.setLayoutData( fdOutdatedFieldname );

   // positionFieldname
   wPositionFieldname =
     new LabelTextVar(
       transMeta, shell, BaseMessages.getString( PKG, "ZendeskInputHCCategoryDialog.PositionFieldname.Label" ),
       BaseMessages.getString( PKG, "ZendeskInputHCCategoryDialog.PositionFieldname.Tooltip" ) );
   props.setLook( wPositionFieldname );
   wPositionFieldname.addModifyListener( lsMod );
   fdPositionFieldname = new FormData();
   fdPositionFieldname.left = new FormAttachment( 0, -margin );
   fdPositionFieldname.top = new FormAttachment( wOutdatedFieldname, 2 * margin );
   fdPositionFieldname.right = new FormAttachment( 100, -margin );
   wPositionFieldname.setLayoutData( fdPositionFieldname );

   // createdAtFieldname
   wCreatedAtFieldname =
     new LabelTextVar(
       transMeta, shell, BaseMessages.getString( PKG, "ZendeskInputHCCategoryDialog.CreatedAtFieldname.Label" ),
       BaseMessages.getString( PKG, "ZendeskInputHCCategoryDialog.CreatedAtFieldname.Tooltip" ) );
   props.setLook( wCreatedAtFieldname );
   wCreatedAtFieldname.addModifyListener( lsMod );
   fdCreatedAtFieldname = new FormData();
   fdCreatedAtFieldname.left = new FormAttachment( 0, -margin );
   fdCreatedAtFieldname.top = new FormAttachment( wPositionFieldname, 2 * margin );
   fdCreatedAtFieldname.right = new FormAttachment( 100, -margin );
   wCreatedAtFieldname.setLayoutData( fdCreatedAtFieldname );

   // updatedAtFieldname
   wUpdatedAtFieldname =
     new LabelTextVar(
       transMeta, shell, BaseMessages.getString( PKG, "ZendeskInputHCCategoryDialog.UpdatedAtFieldname.Label" ),
       BaseMessages.getString( PKG, "ZendeskInputHCCategoryDialog.UpdatedAtFieldname.Tooltip" ) );
   props.setLook( wUpdatedAtFieldname );
   wUpdatedAtFieldname.addModifyListener( lsMod );
   fdUpdatedAtFieldname = new FormData();
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
   wCategoryIdFieldname.setText( Const.NVL( input.getCategoryIdFieldname(), "" ) );
   wCategoryUrlFieldname.setText( Const.NVL( input.getCategoryUrlFieldname(), "" ) );
   wCategoryNameFieldname.setText( Const.NVL( input.getCategoryNameFieldname(), "" ) );
   wDescriptionFieldname.setText( Const.NVL( input.getDescriptionFieldname(), "" ) );
   wLocaleFieldname.setText( Const.NVL( input.getLocaleFieldname(), "" ) );
   wSourceLocaleFieldname.setText( Const.NVL( input.getSourceLocaleFieldname(), "" ) );
   wCategoryHtmlUrlFieldname.setText( Const.NVL( input.getCategoryHtmlUrlFieldname(), "" ) );
   wOutdatedFieldname.setText( Const.NVL( input.getOutdatedFieldname(), "" ) );
   wPositionFieldname.setText( Const.NVL( input.getPositionFieldname(), "" ) );
   wCreatedAtFieldname.setText( Const.NVL( input.getCreatedAtFieldname() , "" ) );
   wUpdatedAtFieldname.setText( Const.NVL( input.getUpdatedAtFieldname() , "" ) );

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

 private void getInfo( ZendeskInputHCCategoryMeta inf ) {
   inf.setSubDomain( wSubDomain.getText() );
   inf.setUsername( wUsername.getText() );
   inf.setPassword( wPassword.getText() );
   inf.setToken( wToken.getSelection() );
   inf.setCategoryIdFieldname( wCategoryIdFieldname.getText() );
   inf.setCategoryUrlFieldname( wCategoryUrlFieldname.getText() );
   inf.setCategoryNameFieldname( wCategoryNameFieldname.getText() );
   inf.setDescriptionFieldname( wDescriptionFieldname.getText() );
   inf.setLocaleFieldname( wLocaleFieldname.getText() );
   inf.setSourceLocaleFieldname( wSourceLocaleFieldname.getText() );
   inf.setCategoryHtmlUrlFieldname( wCategoryHtmlUrlFieldname.getText() );
   inf.setOutdatedFieldname( wOutdatedFieldname.getText() );
   inf.setPositionFieldname( wPositionFieldname.getText() );
   inf.setCreatedAtFieldname( wCreatedAtFieldname.getText() );
   inf.setUpdatedAtFieldname( wUpdatedAtFieldname.getText() );

   stepname = wStepname.getText(); // return value
 }
}
