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

package org.pentaho.di.ui.trans.steps.zendesk;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CCombo;
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
import org.pentaho.di.core.exception.KettleStepException;
import org.pentaho.di.core.row.RowMeta;
import org.pentaho.di.core.row.RowMetaInterface;
import org.pentaho.di.i18n.BaseMessages;
import org.pentaho.di.trans.TransMeta;
import org.pentaho.di.trans.step.BaseStepMeta;
import org.pentaho.di.trans.step.StepDialogInterface;
import org.pentaho.di.trans.steps.zendesk.ZendeskOutputSuspendedUsersMeta;
import org.pentaho.di.ui.core.dialog.ErrorDialog;
import org.pentaho.di.ui.core.widget.LabelTextVar;
import org.pentaho.di.ui.core.widget.PasswordTextVar;
import org.pentaho.di.ui.trans.step.BaseStepDialog;

public class ZendeskOutputSuspendedUsersDialog extends BaseStepDialog implements StepDialogInterface {

 private static Class<?> PKG = ZendeskOutputSuspendedUsersMeta.class; // for i18n purposes, needed by Translator2!!
 private ZendeskOutputSuspendedUsersMeta input;

 private LabelTextVar wSubDomain, wUsername, wResult;
 private Label wlPassword, wlToken, wlUserFieldname, wlActionFieldname;
 private PasswordTextVar wPassword;
 private Button wToken;
 private CCombo wUserFieldname, wActionFieldname;

 public ZendeskOutputSuspendedUsersDialog( Shell parent, Object in, TransMeta tr, String sname ) {
   super( parent, (BaseStepMeta) in, tr, sname );
   input = (ZendeskOutputSuspendedUsersMeta) in;
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
   shell.setText( BaseMessages.getString( PKG, "ZendeskOutputSuspendedUsers.Shell.Title" ) );

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
   props.setLook( wSubDomain );
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

   RowMetaInterface previousFields;
   try {
     previousFields = transMeta.getPrevStepFields( stepMeta );
   } catch ( KettleStepException e ) {
     new ErrorDialog( shell,
       BaseMessages.getString( PKG, "ZendeskInputDialog.ErrorDialog.UnableToGetInputFields.Title" ),
       BaseMessages.getString( PKG, "ZendeskInputDialog.ErrorDialog.UnableToGetInputFields.Message" ), e );
     previousFields = new RowMeta();
   }

   // Incoming User ID Field Name
   wlUserFieldname = new Label( shell, SWT.RIGHT );
   wlUserFieldname.setText( BaseMessages.getString( PKG, "ZendeskOutputSuspendedUsersDialog.UserFieldname.Label" ) );
   wlUserFieldname.setToolTipText( BaseMessages.getString( PKG, "ZendeskOutputSuspendedUsersDialog.UserFieldname.Tooltip" ) );
   props.setLook( wlUserFieldname );
   FormData fdlUserFieldname = new FormData();
   fdlUserFieldname.left = new FormAttachment( 0, 0 );
   fdlUserFieldname.top = new FormAttachment( wToken, 2 * margin );
   fdlUserFieldname.right = new FormAttachment( middle, -margin );
   wlUserFieldname.setLayoutData( fdlUserFieldname );
   
   wUserFieldname = new CCombo( shell, SWT.BORDER | SWT.READ_ONLY );
   props.setLook( wUserFieldname );
   wUserFieldname.addModifyListener( lsMod );
   FormData fdUserFieldname = new FormData();
   fdUserFieldname.left = new FormAttachment( middle, 0 );
   fdUserFieldname.top = new FormAttachment( wToken, margin );
   fdUserFieldname.right = new FormAttachment( 100, -margin );
   wUserFieldname.setLayoutData( fdUserFieldname );
   wUserFieldname.setItems( previousFields.getFieldNames() );
   wUserFieldname.addSelectionListener( new SelectionAdapter() {
     public void widgetSelected( SelectionEvent e ) {
       input.setChanged();
     }
   } );

   // Incoming Action Field Name
   wlActionFieldname = new Label( shell, SWT.RIGHT );
   wlActionFieldname.setText( BaseMessages.getString( PKG, "ZendeskOutputSuspendedUsersDialog.ActionFieldname.Label" ) );
   wlActionFieldname.setToolTipText( BaseMessages.getString( PKG, "ZendeskOutputSuspendedUsersDialog.ActionFieldname.Tooltip" ) );
   props.setLook( wlActionFieldname );
   FormData fdlActionFieldname = new FormData();
   fdlActionFieldname.left = new FormAttachment( 0, 0 );
   fdlActionFieldname.top = new FormAttachment( wUserFieldname, 2 * margin );
   fdlActionFieldname.right = new FormAttachment( middle, -margin );
   wlActionFieldname.setLayoutData( fdlActionFieldname );
   
   wActionFieldname = new CCombo( shell, SWT.BORDER | SWT.READ_ONLY );
   props.setLook( wActionFieldname );
   wActionFieldname.addModifyListener( lsMod );
   FormData fdActionFieldname = new FormData();
   fdActionFieldname.left = new FormAttachment( middle, 0 );
   fdActionFieldname.top = new FormAttachment( wUserFieldname, margin );
   fdActionFieldname.right = new FormAttachment( 100, -margin );
   wActionFieldname.setLayoutData( fdActionFieldname );
   wActionFieldname.setItems( previousFields.getFieldNames() );
   wActionFieldname.addSelectionListener( new SelectionAdapter() {
     public void widgetSelected( SelectionEvent e ) {
       input.setChanged();
     }
   } );

   // Result Field
   wResult = new LabelTextVar( transMeta, shell,
     BaseMessages.getString( PKG, "ZendeskOutputSuspendedUsersDialog.Result.Label" ),
     BaseMessages.getString( PKG, "ZendeskOutputSuspendedUsersDialog.Result.Tooltip" ) );
   props.setLook( wResult );
   wResult.addModifyListener( lsMod );
   FormData fdResult = new FormData();
   fdResult.left = new FormAttachment( 0, -margin );
   fdResult.top = new FormAttachment( wActionFieldname, 2 * margin );
   fdResult.right = new FormAttachment( 100, -margin );
   wResult.setLayoutData( fdResult );

   // Some buttons
   wOK = new Button( shell, SWT.PUSH );
   wOK.setText( BaseMessages.getString( PKG, "System.Button.OK" ) );
   wCancel = new Button( shell, SWT.PUSH );
   wCancel.setText( BaseMessages.getString( PKG, "System.Button.Cancel" ) );

   setButtonPositions( new Button[] { wOK, wCancel }, margin, wResult );

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
   wUserFieldname.setText( Const.NVL( input.getUserFieldName(), "" ) );
   wActionFieldname.setText( Const.NVL( input.getActionFieldName(), "" ) );
   wResult.setText( Const.NVL( input.getResultFieldName(), "" ) );

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

 private void getInfo( ZendeskOutputSuspendedUsersMeta inf ) {
   inf.setSubDomain( wSubDomain.getText() );
   inf.setUsername( wUsername.getText() );
   inf.setPassword( wPassword.getText() );
   inf.setToken( wToken.getSelection() );
   inf.setUserFieldName( wUserFieldname.getText() );
   inf.setActionFieldName( wActionFieldname.getText() );
   inf.setResultFieldName( wResult.getText() );
   stepname = wStepname.getText(); // return value
 }
}
