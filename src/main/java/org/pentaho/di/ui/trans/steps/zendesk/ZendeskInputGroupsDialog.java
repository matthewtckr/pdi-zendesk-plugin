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
import org.pentaho.di.trans.steps.zendesk.ZendeskInputGroupsMeta;
import org.pentaho.di.ui.core.widget.LabelTextVar;
import org.pentaho.di.ui.core.widget.PasswordTextVar;
import org.pentaho.di.ui.trans.step.BaseStepDialog;

public class ZendeskInputGroupsDialog extends BaseStepDialog implements StepDialogInterface {

 private static Class<?> PKG = ZendeskInputGroupsMeta.class; // for i18n purposes, needed by Translator2!!
 private ZendeskInputGroupsMeta input;

 private CTabFolder wTabFolder;
 private CTabItem wGeneralTab, wGroupTab;
 private Composite wGeneralComp, wGroupComp;

 private LabelTextVar wSubDomain, wUsername;
 private Label wlPassword, wlToken;
 private PasswordTextVar wPassword;
 private Button wToken;

 private LabelTextVar wGroupIdFieldname;
 private LabelTextVar wGroupUrlFieldname;
 private LabelTextVar wGroupNameFieldname;
 private LabelTextVar wDeletedFieldname;
 private LabelTextVar wCreatedAtFieldname;
 private LabelTextVar wUpdatedAtFieldname;

 public ZendeskInputGroupsDialog( Shell parent, Object in, TransMeta tr, String sname ) {
   super( parent, (BaseStepMeta) in, tr, sname );
   input = (ZendeskInputGroupsMeta) in;
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
   shell.setText( BaseMessages.getString( PKG, "ZendeskInputGroups.Shell.Title" ) );

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
   fdSubDomain.top = new FormAttachment( wStepname, 2 * margin );
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

   // //////////////////////
   // START OF GROUPS TAB //
   // //////////////////////

   wGroupTab = new CTabItem( wTabFolder, SWT.NONE );
   wGroupTab.setText( BaseMessages.getString( PKG, "ZendeskInputGroupsDialog.GroupTab.TabItem" ) );

   wGroupComp = new Composite( wTabFolder, SWT.NONE );
   props.setLook( wGroupComp );

   FormLayout groupLayout = new FormLayout();
   groupLayout.marginWidth = margin;
   groupLayout.marginHeight = margin;
   wGroupComp.setLayout( groupLayout );

   // groupIdFieldname
   wGroupIdFieldname =
     new LabelTextVar(
       transMeta, wGroupComp, BaseMessages.getString( PKG, "ZendeskInputGroupsDialog.GroupIdFieldname.Label" ),
       BaseMessages.getString( PKG, "ZendeskInputGroupsDialog.GroupIdFieldname.Tooltip" ) );
   props.setLook( wGroupIdFieldname );
   wGroupIdFieldname.addModifyListener( lsMod );
   FormData fdGroupIdFieldname = new FormData();
   fdGroupIdFieldname.left = new FormAttachment( 0, -margin );
   fdGroupIdFieldname.top = new FormAttachment( wToken, 2 * margin );
   fdGroupIdFieldname.right = new FormAttachment( 100, -margin );
   wGroupIdFieldname.setLayoutData( fdGroupIdFieldname );

   // groupUrlFieldname
   wGroupUrlFieldname =
     new LabelTextVar(
       transMeta, wGroupComp, BaseMessages.getString( PKG, "ZendeskInputGroupsDialog.GroupURLFieldname.Label" ),
       BaseMessages.getString( PKG, "ZendeskInputGroupsDialog.GroupURLFieldname.Tooltip" ) );
   props.setLook( wGroupUrlFieldname );
   wGroupUrlFieldname.addModifyListener( lsMod );
   FormData fdGroupUrlFieldname = new FormData();
   fdGroupUrlFieldname.left = new FormAttachment( 0, -margin );
   fdGroupUrlFieldname.top = new FormAttachment( wGroupIdFieldname, 2 * margin );
   fdGroupUrlFieldname.right = new FormAttachment( 100, -margin );
   wGroupUrlFieldname.setLayoutData( fdGroupUrlFieldname );

   // groupNameFieldname
   wGroupNameFieldname =
     new LabelTextVar(
       transMeta, wGroupComp, BaseMessages.getString( PKG, "ZendeskInputGroupsDialog.GroupNameFieldname.Label" ),
       BaseMessages.getString( PKG, "ZendeskInputGroupsDialog.GroupNameFieldname.Tooltip" ) );
   props.setLook( wGroupNameFieldname );
   wGroupNameFieldname.addModifyListener( lsMod );
   FormData fdGroupNameFieldname = new FormData();
   fdGroupNameFieldname.left = new FormAttachment( 0, -margin );
   fdGroupNameFieldname.top = new FormAttachment( wGroupUrlFieldname, 2 * margin );
   fdGroupNameFieldname.right = new FormAttachment( 100, -margin );
   wGroupNameFieldname.setLayoutData( fdGroupNameFieldname );

   // deletedFieldname
   wDeletedFieldname =
     new LabelTextVar(
       transMeta, wGroupComp, BaseMessages.getString( PKG, "ZendeskInputGroupsDialog.DeletedFieldname.Label" ),
       BaseMessages.getString( PKG, "ZendeskInputGroupsDialog.DeletedFieldname.Tooltip" ) );
   props.setLook( wDeletedFieldname );
   wGroupIdFieldname.addModifyListener( lsMod );
   FormData fdDeletedFieldname = new FormData();
   fdDeletedFieldname.left = new FormAttachment( 0, -margin );
   fdDeletedFieldname.top = new FormAttachment( wGroupNameFieldname, 2 * margin );
   fdDeletedFieldname.right = new FormAttachment( 100, -margin );
   wDeletedFieldname.setLayoutData( fdDeletedFieldname );

   // createdAtFieldname
   wCreatedAtFieldname =
     new LabelTextVar(
       transMeta, wGroupComp, BaseMessages.getString( PKG, "ZendeskInputGroupsDialog.CreatedAtFieldname.Label" ),
       BaseMessages.getString( PKG, "ZendeskInputGroupsDialog.CreatedAtFieldname.Tooltip" ) );
   props.setLook( wCreatedAtFieldname );
   wCreatedAtFieldname.addModifyListener( lsMod );
   FormData fdCreatedAtFieldname = new FormData();
   fdCreatedAtFieldname.left = new FormAttachment( 0, -margin );
   fdCreatedAtFieldname.top = new FormAttachment( wDeletedFieldname, 2 * margin );
   fdCreatedAtFieldname.right = new FormAttachment( 100, -margin );
   wCreatedAtFieldname.setLayoutData( fdCreatedAtFieldname );

   // updatedAtFieldname
   wUpdatedAtFieldname =
     new LabelTextVar(
       transMeta, wGroupComp, BaseMessages.getString( PKG, "ZendeskInputGroupsDialog.UpdatedAtFieldname.Label" ),
       BaseMessages.getString( PKG, "ZendeskInputGroupsDialog.UpdatedAtFieldname.Tooltip" ) );
   props.setLook( wUpdatedAtFieldname );
   wUpdatedAtFieldname.addModifyListener( lsMod );
   FormData fdUpdatedAtFieldname = new FormData();
   fdUpdatedAtFieldname.left = new FormAttachment( 0, -margin );
   fdUpdatedAtFieldname.top = new FormAttachment( wCreatedAtFieldname, 2 * margin );
   fdUpdatedAtFieldname.right = new FormAttachment( 100, -margin );
   wUpdatedAtFieldname.setLayoutData( fdUpdatedAtFieldname );

   FormData fdGroupComp = new FormData();
   fdGroupComp.left = new FormAttachment( 0, 0 );
   fdGroupComp.top = new FormAttachment( 0, 0 );
   fdGroupComp.right = new FormAttachment( 100, 0 );
   fdGroupComp.bottom = new FormAttachment( 100, 0 );
   wGroupComp.setLayoutData( fdGroupComp );

   wGroupComp.layout();
   wGroupTab.setControl( wGroupComp );

   // ////////////////////
   // END OF GROUPS TAB //
   // ////////////////////

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
   wGroupIdFieldname.setText( Const.NVL( input.getGroupIdFieldname() , "" ) );
   wGroupUrlFieldname.setText( Const.NVL( input.getGroupUrlFieldname() , "" ) );
   wGroupNameFieldname.setText( Const.NVL( input.getGroupNameFieldname() , "" ) );
   wDeletedFieldname.setText( Const.NVL( input.getDeletedFieldname() , "" ) );
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

 private void getInfo( ZendeskInputGroupsMeta inf ) {
   inf.setSubDomain( wSubDomain.getText() );
   inf.setUsername( wUsername.getText() );
   inf.setPassword( wPassword.getText() );
   inf.setToken( wToken.getSelection() );
   inf.setGroupIdFieldname( wGroupIdFieldname.getText() );
   inf.setGroupUrlFieldname( wGroupUrlFieldname.getText() );
   inf.setGroupNameFieldname( wGroupNameFieldname.getText() );
   inf.setDeletedFieldname( wDeletedFieldname.getText() );
   inf.setCreatedAtFieldname( wCreatedAtFieldname.getText() );
   inf.setUpdatedAtFieldname( wUpdatedAtFieldname.getText() );

   stepname = wStepname.getText(); // return value
 }
}
