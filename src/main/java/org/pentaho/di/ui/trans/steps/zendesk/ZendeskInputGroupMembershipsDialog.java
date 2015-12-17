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
import org.pentaho.di.trans.steps.zendesk.ZendeskInputGroupMembershipsMeta;
import org.pentaho.di.ui.core.widget.LabelTextVar;
import org.pentaho.di.ui.core.widget.PasswordTextVar;
import org.pentaho.di.ui.trans.step.BaseStepDialog;

public class ZendeskInputGroupMembershipsDialog extends BaseStepDialog implements StepDialogInterface {

 private static Class<?> PKG = ZendeskInputGroupMembershipsMeta.class; // for i18n purposes, needed by Translator2!!
 private ZendeskInputGroupMembershipsMeta input;

 private LabelTextVar wSubDomain, wUsername;
 private Label wlPassword, wlToken;
 private PasswordTextVar wPassword;
 private Button wToken;

 private LabelTextVar wGroupMembershipIdFieldname;
 private LabelTextVar wGroupMembershipUrlFieldname;
 private LabelTextVar wUserIdFieldname;
 private LabelTextVar wGroupIdFieldname;
 private LabelTextVar wCreatedAtFieldname;
 private LabelTextVar wUpdatedAtFieldname;
 private LabelTextVar wDefaultGroupFieldname;

 public ZendeskInputGroupMembershipsDialog( Shell parent, Object in, TransMeta tr, String sname ) {
   super( parent, (BaseStepMeta) in, tr, sname );
   input = (ZendeskInputGroupMembershipsMeta) in;
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
   shell.setText( BaseMessages.getString( PKG, "ZendeskInputGroupMemberships.Shell.Title" ) );

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

   // groupMembershipIdFieldname
   wGroupMembershipIdFieldname =
     new LabelTextVar(
       transMeta, shell, BaseMessages.getString( PKG, "ZendeskInputGroupMembershipsDialog.GroupMembershipIdFieldname.Label" ),
       BaseMessages.getString( PKG, "ZendeskInputGroupMembershipsDialog.GroupMembershipIdFieldname.Tooltip" ) );
   props.setLook( wGroupMembershipIdFieldname );
   wGroupMembershipIdFieldname.addModifyListener( lsMod );
   FormData fdGroupMembershipIdFieldname = new FormData();
   fdGroupMembershipIdFieldname.left = new FormAttachment( 0, -margin );
   fdGroupMembershipIdFieldname.top = new FormAttachment( wToken, 2 * margin );
   fdGroupMembershipIdFieldname.right = new FormAttachment( 100, -margin );
   wGroupMembershipIdFieldname.setLayoutData( fdGroupMembershipIdFieldname );

   // groupMembershipUrlFieldname
   wGroupMembershipUrlFieldname =
     new LabelTextVar(
       transMeta, shell, BaseMessages.getString( PKG, "ZendeskInputGroupMembershipsDialog.GroupMembershipURLFieldname.Label" ),
       BaseMessages.getString( PKG, "ZendeskInputGroupMembershipsDialog.GroupMembershipURLFieldname.Tooltip" ) );
   props.setLook( wGroupMembershipUrlFieldname );
   wGroupMembershipUrlFieldname.addModifyListener( lsMod );
   FormData fdGroupMembershipUrlFieldname = new FormData();
   fdGroupMembershipUrlFieldname.left = new FormAttachment( 0, -margin );
   fdGroupMembershipUrlFieldname.top = new FormAttachment( wGroupMembershipIdFieldname, 2 * margin );
   fdGroupMembershipUrlFieldname.right = new FormAttachment( 100, -margin );
   wGroupMembershipUrlFieldname.setLayoutData( fdGroupMembershipUrlFieldname );

   // userIdFieldname
   wUserIdFieldname =
     new LabelTextVar(
       transMeta, shell, BaseMessages.getString( PKG, "ZendeskInputGroupMembershipsDialog.UserIDFieldname.Label" ),
       BaseMessages.getString( PKG, "ZendeskInputGroupMembershipsDialog.UserIDFieldname.Tooltip" ) );
   props.setLook( wUserIdFieldname );
   wUserIdFieldname.addModifyListener( lsMod );
   FormData fdUserIdFieldname = new FormData();
   fdUserIdFieldname.left = new FormAttachment( 0, -margin );
   fdUserIdFieldname.top = new FormAttachment( wGroupMembershipUrlFieldname, 2 * margin );
   fdUserIdFieldname.right = new FormAttachment( 100, -margin );
   wUserIdFieldname.setLayoutData( fdUserIdFieldname );

   // groupIdFieldname
   wGroupIdFieldname =
     new LabelTextVar(
       transMeta, shell, BaseMessages.getString( PKG, "ZendeskInputGroupMembershipsDialog.GroupIDFieldname.Label" ),
       BaseMessages.getString( PKG, "ZendeskInputGroupMembershipsDialog.GroupIDFieldname.Tooltip" ) );
   props.setLook( wGroupIdFieldname );
   wGroupIdFieldname.addModifyListener( lsMod );
   FormData fdGroupIdFieldname = new FormData();
   fdGroupIdFieldname.left = new FormAttachment( 0, -margin );
   fdGroupIdFieldname.top = new FormAttachment( wUserIdFieldname, 2 * margin );
   fdGroupIdFieldname.right = new FormAttachment( 100, -margin );
   wGroupIdFieldname.setLayoutData( fdGroupIdFieldname );

   // createdAtFieldname
   wCreatedAtFieldname =
     new LabelTextVar(
       transMeta, shell, BaseMessages.getString( PKG, "ZendeskInputGroupMembershipsDialog.CreatedAtFieldname.Label" ),
       BaseMessages.getString( PKG, "ZendeskInputGroupMembershipsDialog.CreatedAtFieldname.Tooltip" ) );
   props.setLook( wCreatedAtFieldname );
   wCreatedAtFieldname.addModifyListener( lsMod );
   FormData fdCreatedAtFieldname = new FormData();
   fdCreatedAtFieldname.left = new FormAttachment( 0, -margin );
   fdCreatedAtFieldname.top = new FormAttachment( wGroupIdFieldname, 2 * margin );
   fdCreatedAtFieldname.right = new FormAttachment( 100, -margin );
   wCreatedAtFieldname.setLayoutData( fdCreatedAtFieldname );

   // updatedAtFieldname
   wUpdatedAtFieldname =
     new LabelTextVar(
       transMeta, shell, BaseMessages.getString( PKG, "ZendeskInputGroupMembershipsDialog.UpdatedAtFieldname.Label" ),
       BaseMessages.getString( PKG, "ZendeskInputGroupMembershipsDialog.UpdatedAtFieldname.Tooltip" ) );
   props.setLook( wUpdatedAtFieldname );
   wUpdatedAtFieldname.addModifyListener( lsMod );
   FormData fdUpdatedAtFieldname = new FormData();
   fdUpdatedAtFieldname.left = new FormAttachment( 0, -margin );
   fdUpdatedAtFieldname.top = new FormAttachment( wCreatedAtFieldname, 2 * margin );
   fdUpdatedAtFieldname.right = new FormAttachment( 100, -margin );
   wUpdatedAtFieldname.setLayoutData( fdUpdatedAtFieldname );

   // defaultGroupFieldname
   wDefaultGroupFieldname =
     new LabelTextVar(
       transMeta, shell, BaseMessages.getString( PKG, "ZendeskInputGroupMembershipsDialog.DefaultGroupFieldname.Label" ),
       BaseMessages.getString( PKG, "ZendeskInputGroupMembershipsDialog.DefaultGroupFieldname.Tooltip" ) );
   props.setLook( wGroupIdFieldname );
   wDefaultGroupFieldname.addModifyListener( lsMod );
   FormData fdDefaultGroupFieldname = new FormData();
   fdDefaultGroupFieldname.left = new FormAttachment( 0, -margin );
   fdDefaultGroupFieldname.top = new FormAttachment( wUpdatedAtFieldname, 2 * margin );
   fdDefaultGroupFieldname.right = new FormAttachment( 100, -margin );
   wDefaultGroupFieldname.setLayoutData( fdDefaultGroupFieldname );

   // Some buttons
   wOK = new Button( shell, SWT.PUSH );
   wOK.setText( BaseMessages.getString( PKG, "System.Button.OK" ) );
   wCancel = new Button( shell, SWT.PUSH );
   wCancel.setText( BaseMessages.getString( PKG, "System.Button.Cancel" ) );

   setButtonPositions( new Button[] { wOK, wCancel }, margin, wDefaultGroupFieldname );

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
   wGroupMembershipIdFieldname.setText( Const.NVL( input.getGroupMembershipIdFieldname() , "" ) );
   wGroupMembershipUrlFieldname.setText( Const.NVL( input.getGroupMembershipUrlFieldname() , "" ) );
   wUserIdFieldname.setText( Const.NVL( input.getUserIdFieldname() , "" ) );
   wGroupIdFieldname.setText( Const.NVL( input.getGroupIdFieldname() , "" ) );
   wCreatedAtFieldname.setText( Const.NVL( input.getCreatedAtFieldname() , "" ) );
   wUpdatedAtFieldname.setText( Const.NVL( input.getUpdatedAtFieldname() , "" ) );
   wDefaultGroupFieldname.setText( Const.NVL( input.getDefaultGroupFieldname() , "" ) );

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

 private void getInfo( ZendeskInputGroupMembershipsMeta inf ) {
   inf.setSubDomain( wSubDomain.getText() );
   inf.setUsername( wUsername.getText() );
   inf.setPassword( wPassword.getText() );
   inf.setToken( wToken.getSelection() );
   inf.setGroupMembershipIdFieldname( wGroupMembershipIdFieldname.getText() );
   inf.setGroupMembershipUrlFieldname( wGroupMembershipUrlFieldname.getText() );
   inf.setUserIdFieldname( wUserIdFieldname.getText() );
   inf.setGroupIdFieldname( wGroupIdFieldname.getText() );
   inf.setCreatedAtFieldname( wCreatedAtFieldname.getText() );
   inf.setUpdatedAtFieldname( wUpdatedAtFieldname.getText() );
   inf.setDefaultGroupFieldname( wDefaultGroupFieldname.getText() );

   stepname = wStepname.getText(); // return value
 }
}
