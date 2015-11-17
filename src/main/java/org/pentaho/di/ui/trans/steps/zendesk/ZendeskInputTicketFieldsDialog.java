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
import org.pentaho.di.trans.steps.zendesk.ZendeskInputTicketFieldsMeta;
import org.pentaho.di.ui.core.widget.LabelTextVar;
import org.pentaho.di.ui.core.widget.PasswordTextVar;
import org.pentaho.di.ui.trans.step.BaseStepDialog;

public class ZendeskInputTicketFieldsDialog extends BaseStepDialog implements StepDialogInterface {

 private static Class<?> PKG = ZendeskInputTicketFieldsMeta.class; // for i18n purposes, needed by Translator2!!
 private ZendeskInputTicketFieldsMeta input;

 private LabelTextVar wSubDomain, wUsername;
 private Label wlPassword, wlToken;
 private PasswordTextVar wPassword;
 private Button wToken;
 private FormData fdSubDomain, fdUsername, fdPassword, fdlToken, fdToken;

 private LabelTextVar wTicketFieldIdFieldname;
 private LabelTextVar wTicketFieldUrlFieldname;
 private LabelTextVar wTicketFieldTypeFieldname;
 private LabelTextVar wTicketFieldTitleFieldname;
 private LabelTextVar wTicketFieldActiveFieldname;
 private LabelTextVar wTicketFieldRequiredFieldname;
 private LabelTextVar wTicketFieldVisibleEndUsersFieldname;
 private LabelTextVar wCreatedAtFieldname;
 private LabelTextVar wUpdatedAtFieldname;

 private FormData fdTicketFieldIdFieldname;
 private FormData fdTicketFieldUrlFieldname;
 private FormData fdTicketFieldTypeFieldname;
 private FormData fdTicketFieldTitleFieldname;
 private FormData fdTicketFieldActiveFieldname;
 private FormData fdTicketFieldRequiredFieldname;
 private FormData fdTicketFieldVisibleEndUsersFieldname;
 private FormData fdCreatedAtFieldname;
 private FormData fdUpdatedAtFieldname;

 public ZendeskInputTicketFieldsDialog( Shell parent, Object in, TransMeta tr, String sname ) {
   super( parent, (BaseStepMeta) in, tr, sname );
   input = (ZendeskInputTicketFieldsMeta) in;
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
   shell.setText( BaseMessages.getString( PKG, "ZendeskInputTicketFieldsDialog.Shell.Title" ) );

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

   // ticketFieldIdFieldname
   wTicketFieldIdFieldname =
     new LabelTextVar(
       transMeta, shell, BaseMessages.getString( PKG, "ZendeskInputTicketFieldsDialog.TicketFieldIdFieldname.Label" ),
       BaseMessages.getString( PKG, "ZendeskInputTicketFieldsDialog.TicketFieldIdFieldname.Tooltip" ) );
   props.setLook( wTicketFieldIdFieldname );
   wTicketFieldIdFieldname.addModifyListener( lsMod );
   fdTicketFieldIdFieldname = new FormData();
   fdTicketFieldIdFieldname.left = new FormAttachment( 0, -margin );
   fdTicketFieldIdFieldname.top = new FormAttachment( wToken, 2 * margin );
   fdTicketFieldIdFieldname.right = new FormAttachment( 100, -margin );
   wTicketFieldIdFieldname.setLayoutData( fdTicketFieldIdFieldname );

   // ticketFieldUrlFieldname
   wTicketFieldUrlFieldname =
     new LabelTextVar(
       transMeta, shell, BaseMessages.getString( PKG, "ZendeskInputTicketFieldsDialog.TicketFieldUrlFieldname.Label" ),
       BaseMessages.getString( PKG, "ZendeskInputTicketFieldsDialog.TicketFieldUrlFieldname.Tooltip" ) );
   props.setLook( wTicketFieldUrlFieldname );
   wTicketFieldUrlFieldname.addModifyListener( lsMod );
   fdTicketFieldUrlFieldname = new FormData();
   fdTicketFieldUrlFieldname.left = new FormAttachment( 0, -margin );
   fdTicketFieldUrlFieldname.top = new FormAttachment( wTicketFieldIdFieldname, 2 * margin );
   fdTicketFieldUrlFieldname.right = new FormAttachment( 100, -margin );
   wTicketFieldUrlFieldname.setLayoutData( fdTicketFieldUrlFieldname );

   // ticketFieldTypeFieldname
   wTicketFieldTypeFieldname =
     new LabelTextVar(
       transMeta, shell, BaseMessages.getString( PKG, "ZendeskInputTicketFieldsDialog.TicketFieldTypeFieldname.Label" ),
       BaseMessages.getString( PKG, "ZendeskInputTicketFieldsDialog.TicketFieldTypeFieldname.Tooltip" ) );
   props.setLook( wTicketFieldTypeFieldname );
   wTicketFieldTypeFieldname.addModifyListener( lsMod );
   fdTicketFieldTypeFieldname = new FormData();
   fdTicketFieldTypeFieldname.left = new FormAttachment( 0, -margin );
   fdTicketFieldTypeFieldname.top = new FormAttachment( wTicketFieldUrlFieldname, 2 * margin );
   fdTicketFieldTypeFieldname.right = new FormAttachment( 100, -margin );
   wTicketFieldTypeFieldname.setLayoutData( fdTicketFieldTypeFieldname );

   // ticketFieldTitleFieldname
   wTicketFieldTitleFieldname =
     new LabelTextVar(
       transMeta, shell, BaseMessages.getString( PKG, "ZendeskInputTicketFieldsDialog.TicketFieldTitleFieldname.Label" ),
       BaseMessages.getString( PKG, "ZendeskInputTicketFieldsDialog.TicketFieldTitleFieldname.Tooltip" ) );
   props.setLook( wTicketFieldTitleFieldname );
   wTicketFieldTitleFieldname.addModifyListener( lsMod );
   fdTicketFieldTitleFieldname = new FormData();
   fdTicketFieldTitleFieldname.left = new FormAttachment( 0, -margin );
   fdTicketFieldTitleFieldname.top = new FormAttachment( wTicketFieldTypeFieldname, 2 * margin );
   fdTicketFieldTitleFieldname.right = new FormAttachment( 100, -margin );
   wTicketFieldTitleFieldname.setLayoutData( fdTicketFieldTitleFieldname );

   // ticketFieldActiveFieldname
   wTicketFieldActiveFieldname =
     new LabelTextVar(
       transMeta, shell, BaseMessages.getString( PKG, "ZendeskInputTicketFieldsDialog.TicketFieldActiveFieldname.Label" ),
       BaseMessages.getString( PKG, "ZendeskInputTicketFieldsDialog.TicketFieldActiveFieldname.Tooltip" ) );
   props.setLook( wTicketFieldActiveFieldname );
   wTicketFieldActiveFieldname.addModifyListener( lsMod );
   fdTicketFieldActiveFieldname = new FormData();
   fdTicketFieldActiveFieldname.left = new FormAttachment( 0, -margin );
   fdTicketFieldActiveFieldname.top = new FormAttachment( wTicketFieldTitleFieldname, 2 * margin );
   fdTicketFieldActiveFieldname.right = new FormAttachment( 100, -margin );
   wTicketFieldActiveFieldname.setLayoutData( fdTicketFieldActiveFieldname );

   // ticketFieldRequiredFieldname
   wTicketFieldRequiredFieldname =
     new LabelTextVar(
       transMeta, shell, BaseMessages.getString( PKG, "ZendeskInputTicketFieldsDialog.TicketFieldRequiredFieldname.Label" ),
       BaseMessages.getString( PKG, "ZendeskInputTicketFieldsDialog.TicketFieldRequiredFieldname.Tooltip" ) );
   props.setLook( wTicketFieldRequiredFieldname );
   wTicketFieldRequiredFieldname.addModifyListener( lsMod );
   fdTicketFieldRequiredFieldname = new FormData();
   fdTicketFieldRequiredFieldname.left = new FormAttachment( 0, -margin );
   fdTicketFieldRequiredFieldname.top = new FormAttachment( wTicketFieldActiveFieldname, 2 * margin );
   fdTicketFieldRequiredFieldname.right = new FormAttachment( 100, -margin );
   wTicketFieldRequiredFieldname.setLayoutData( fdTicketFieldRequiredFieldname );

   // ticketFieldVisibleEndUsersFieldname
   wTicketFieldVisibleEndUsersFieldname =
     new LabelTextVar(
       transMeta, shell, BaseMessages.getString( PKG, "ZendeskInputTicketFieldsDialog.TicketFieldVisibleEndUsersFieldname.Label" ),
       BaseMessages.getString( PKG, "ZendeskInputTicketFieldsDialog.TicketFieldVisibleEndUsersFieldname.Tooltip" ) );
   props.setLook( wTicketFieldVisibleEndUsersFieldname );
   wTicketFieldVisibleEndUsersFieldname.addModifyListener( lsMod );
   fdTicketFieldVisibleEndUsersFieldname = new FormData();
   fdTicketFieldVisibleEndUsersFieldname.left = new FormAttachment( 0, -margin );
   fdTicketFieldVisibleEndUsersFieldname.top = new FormAttachment( wTicketFieldRequiredFieldname, 2 * margin );
   fdTicketFieldVisibleEndUsersFieldname.right = new FormAttachment( 100, -margin );
   wTicketFieldVisibleEndUsersFieldname.setLayoutData( fdTicketFieldVisibleEndUsersFieldname );

   // createdAtFieldname
   wCreatedAtFieldname =
     new LabelTextVar(
       transMeta, shell, BaseMessages.getString( PKG, "ZendeskInputTicketFieldsDialog.CreatedAtFieldname.Label" ),
       BaseMessages.getString( PKG, "ZendeskInputTicketFieldsDialog.CreatedAtFieldname.Tooltip" ) );
   props.setLook( wCreatedAtFieldname );
   wCreatedAtFieldname.addModifyListener( lsMod );
   fdCreatedAtFieldname = new FormData();
   fdCreatedAtFieldname.left = new FormAttachment( 0, -margin );
   fdCreatedAtFieldname.top = new FormAttachment( wTicketFieldVisibleEndUsersFieldname, 2 * margin );
   fdCreatedAtFieldname.right = new FormAttachment( 100, -margin );
   wCreatedAtFieldname.setLayoutData( fdCreatedAtFieldname );

   // updatedAtFieldname
   wUpdatedAtFieldname =
     new LabelTextVar(
       transMeta, shell, BaseMessages.getString( PKG, "ZendeskInputTicketFieldsDialog.UpdatedAtFieldname.Label" ),
       BaseMessages.getString( PKG, "ZendeskInputTicketFieldsDialog.UpdatedAtFieldname.Tooltip" ) );
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
   wTicketFieldIdFieldname.setText( Const.NVL( input.getTicketFieldIdFieldname(), "" ) );
   wTicketFieldUrlFieldname.setText( Const.NVL( input.getTicketFieldUrlFieldname(), "" ) );
   wTicketFieldTypeFieldname.setText( Const.NVL( input.getTicketFieldTypeFieldname(), "" ) );
   wTicketFieldTitleFieldname.setText( Const.NVL( input.getTicketFieldTitleFieldname(), "" ) );
   wTicketFieldActiveFieldname.setText( Const.NVL( input.getTicketFieldActiveFieldname(), "" ) );
   wTicketFieldRequiredFieldname.setText( Const.NVL( input.getTicketFieldRequiredFieldname(), "" ) );
   wTicketFieldVisibleEndUsersFieldname.setText( Const.NVL( input.getTicketFieldVisibleEndUsersFieldname(), "" ) );
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

 private void getInfo( ZendeskInputTicketFieldsMeta inf ) {
   inf.setSubDomain( wSubDomain.getText() );
   inf.setUsername( wUsername.getText() );
   inf.setPassword( wPassword.getText() );
   inf.setToken( wToken.getSelection() );
   inf.setTicketFieldIdFieldname( wTicketFieldIdFieldname.getText() );
   inf.setTicketFieldUrlFieldname( wTicketFieldUrlFieldname.getText() );
   inf.setTicketFieldTypeFieldname( wTicketFieldTypeFieldname.getText() );
   inf.setTicketFieldTitleFieldname( wTicketFieldTitleFieldname.getText() );
   inf.setTicketFieldActiveFieldname( wTicketFieldActiveFieldname.getText() );
   inf.setTicketFieldRequiredFieldname( wTicketFieldRequiredFieldname.getText() );
   inf.setTicketFieldVisibleEndUsersFieldname( wTicketFieldVisibleEndUsersFieldname.getText() );
   inf.setCreatedAtFieldname( wCreatedAtFieldname.getText() );
   inf.setUpdatedAtFieldname( wUpdatedAtFieldname.getText() );

   stepname = wStepname.getText(); // return value
 }
}
