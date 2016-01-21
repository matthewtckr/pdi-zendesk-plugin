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

import java.util.ArrayList;
import java.util.List;

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
import org.pentaho.di.trans.steps.zendesk.ZendeskInputIncrementalMeta;
import org.pentaho.di.trans.steps.zendesk.ZendeskInputIncrementalMeta.IncrementalType;
import org.pentaho.di.ui.core.dialog.ErrorDialog;
import org.pentaho.di.ui.core.widget.LabelTextVar;
import org.pentaho.di.ui.core.widget.PasswordTextVar;
import org.pentaho.di.ui.trans.step.BaseStepDialog;

public class ZendeskInputIncrementalDialog extends BaseStepDialog implements StepDialogInterface {

 private static Class<?> PKG = ZendeskInputIncrementalMeta.class; // for i18n purposes, needed by Translator2!!
 private ZendeskInputIncrementalMeta input;

 private LabelTextVar wSubDomain, wUsername, wOutputFieldname;
 private Label wlPassword, wlDownloadType, wlFieldname, wlToken;
 private PasswordTextVar wPassword;
 private Button wToken;
 private CCombo wDownloadType;
 private CCombo wFieldname;

 public ZendeskInputIncrementalDialog( Shell parent, Object in, TransMeta tr, String sname ) {
   super( parent, (BaseStepMeta) in, tr, sname );
   input = (ZendeskInputIncrementalMeta) in;
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
   shell.setText( BaseMessages.getString( PKG, "ZendeskInputIncremental.Shell.Title" ) );

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

   // Download Type Field Name
   wlDownloadType = new Label( shell, SWT.RIGHT );
   wlDownloadType.setText( BaseMessages.getString( PKG, "ZendeskInputIncremental.DownloadTypeFieldname.Label" ) );
   wlDownloadType.setToolTipText( BaseMessages.getString( PKG, "ZendeskInputIncremental.DownloadTypeFieldname.Tooltip" ) );
   props.setLook( wlDownloadType );
   FormData fdlDownloadType = new FormData();
   fdlDownloadType.left = new FormAttachment( 0, 0 );
   fdlDownloadType.top = new FormAttachment( wToken, 2 * margin );
   fdlDownloadType.right = new FormAttachment( middle, -margin );
   wlDownloadType.setLayoutData( fdlDownloadType );

   wDownloadType = new CCombo( shell, SWT.BORDER | SWT.READ_ONLY );
   props.setLook( wDownloadType );
   wDownloadType.addModifyListener( lsMod );
   FormData fdDownloadType = new FormData();
   fdDownloadType.left = new FormAttachment( middle, 0 );
   fdDownloadType.top = new FormAttachment( wToken, margin );
   fdDownloadType.right = new FormAttachment( 100, -margin );
   wDownloadType.setLayoutData( fdDownloadType );
   
   List<String> downloadTypes = new ArrayList<String>();
   for ( IncrementalType downloadType : IncrementalType.values() ) {
     downloadTypes.add( downloadType.toString() );
   }
   wDownloadType.setItems( downloadTypes.toArray( new String[downloadTypes.size()] ) );
   wDownloadType.addSelectionListener( new SelectionAdapter() {
     public void widgetSelected( SelectionEvent e ) {
       input.setChanged();
       setOutputLabel();
     }
   } );

   // Incoming Timestamp Field Name
   wlFieldname = new Label( shell, SWT.RIGHT );
   wlFieldname.setText( BaseMessages.getString( PKG, "ZendeskInputIncremental.TimestampFieldname.Label" ) );
   props.setLook( wlFieldname );
   FormData fdlFieldname = new FormData();
   fdlFieldname.left = new FormAttachment( 0, 0 );
   fdlFieldname.top = new FormAttachment( wDownloadType, 2 * margin );
   fdlFieldname.right = new FormAttachment( middle, -margin );
   wlFieldname.setLayoutData( fdlFieldname );

   RowMetaInterface previousFields;
   try {
     previousFields = transMeta.getPrevStepFields( stepMeta );
   } catch ( KettleStepException e ) {
     new ErrorDialog( shell,
       BaseMessages.getString( PKG, "ZendeskInputDialog.ErrorDialog.UnableToGetInputFields.Title" ),
       BaseMessages.getString( PKG, "ZendeskInputDialog.ErrorDialog.UnableToGetInputFields.Message" ), e );
     previousFields = new RowMeta();
   }
   
   wFieldname = new CCombo( shell, SWT.BORDER | SWT.READ_ONLY );
   props.setLook( wFieldname );
   wFieldname.addModifyListener( lsMod );
   FormData fdFieldname = new FormData();
   fdFieldname.left = new FormAttachment( middle, 0 );
   fdFieldname.top = new FormAttachment( wDownloadType, margin );
   fdFieldname.right = new FormAttachment( 100, -margin );
   wFieldname.setLayoutData( fdFieldname );
   wFieldname.setItems( previousFields.getFieldNames() );
   wFieldname.addSelectionListener( new SelectionAdapter() {
     public void widgetSelected( SelectionEvent e ) {
       input.setChanged();
     }
   } );

   // Output Fieldname
   wOutputFieldname = new LabelTextVar( transMeta, shell,
     BaseMessages.getString( PKG, "ZendeskInputIncremental.OutputFieldName.Label" ),
     BaseMessages.getString( PKG, "ZendeskInputIncremental.OutputFieldName.Tooltip" ) );
   props.setLook( wSubDomain );
   wOutputFieldname.addModifyListener( lsMod );
   FormData fdOutputFieldname = new FormData();
   fdOutputFieldname.left = new FormAttachment( 0, -margin );
   fdOutputFieldname.top = new FormAttachment( wFieldname, 2 * margin );
   fdOutputFieldname.right = new FormAttachment( 100, -margin );
   wOutputFieldname.setLayoutData( fdOutputFieldname );

   // Some buttons
   wOK = new Button( shell, SWT.PUSH );
   wOK.setText( BaseMessages.getString( PKG, "System.Button.OK" ) );
   wCancel = new Button( shell, SWT.PUSH );
   wCancel.setText( BaseMessages.getString( PKG, "System.Button.Cancel" ) );

   setButtonPositions( new Button[] { wOK, wCancel }, margin, wOutputFieldname );

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

 private void setOutputLabel() {
   String label = wOutputFieldname.getLabelWidget().getText();
   String toolTip = wOutputFieldname.getLabelWidget().getToolTipText();
   int newSelection = wDownloadType.getSelectionIndex();
   switch( IncrementalType.values()[newSelection] ) {
     case ORGANIZATIONS:
       label = BaseMessages.getString( PKG, "ZendeskInputIncremental.OutputFieldName.Organization.Label" );
       toolTip = BaseMessages.getString( PKG, "ZendeskInputIncremental.OutputFieldName.Organization.Tooltip" );
       break;
     case USERS:
       label = BaseMessages.getString( PKG, "ZendeskInputIncremental.OutputFieldName.User.Label" );
       toolTip = BaseMessages.getString( PKG, "ZendeskInputIncremental.OutputFieldName.User.Tooltip" );
       break;
     case TICKETS:
       label = BaseMessages.getString( PKG, "ZendeskInputIncremental.OutputFieldName.Ticket.Label" );
       toolTip = BaseMessages.getString( PKG, "ZendeskInputIncremental.OutputFieldName.Ticket.Tooltip" );
       break;
     case HELPCENTER_ARTICLES:
       label = BaseMessages.getString( PKG, "ZendeskInputIncremental.OutputFieldName.HCArticle.Label" );
       toolTip = BaseMessages.getString( PKG, "ZendeskInputIncremental.OutputFieldName.HCArticle.Tooltip" );
      break;
   }
   wOutputFieldname.getLabelWidget().setText( label );
   wOutputFieldname.getLabelWidget().setToolTipText( toolTip );
 }

 /**
  * Copy information from the meta-data input to the dialog fields.
  */
 public void getData() {
   wSubDomain.setText( Const.NVL( input.getSubDomain(), "" ) );
   wUsername.setText( Const.NVL( input.getUsername(), "" ) );
   wPassword.setText( Const.NVL( input.getPassword(), "" ) );
   wToken.setSelection( input.isToken() );
   if ( null == input.getDownloadType() ) {
     input.setChanged();
     wDownloadType.setText( IncrementalType.TICKETS.toString() );
   } else {
     wDownloadType.setText( input.getDownloadType().toString() );
   }
   wFieldname.setText( Const.NVL( input.getTimestampFieldName(), "" ) );
   wOutputFieldname.setText( Const.NVL( input.getOutputFieldName(), "" ) );

   setOutputLabel();
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

 private void getInfo( ZendeskInputIncrementalMeta inf ) {
   inf.setSubDomain( wSubDomain.getText() );
   inf.setUsername( wUsername.getText() );
   inf.setPassword( wPassword.getText() );
   inf.setToken( wToken.getSelection() );
   inf.setDownloadType( IncrementalType.values()[wDownloadType.getSelectionIndex()] );
   inf.setTimestampFieldName( wFieldname.getText() );
   inf.setOutputFieldName( wOutputFieldname.getText() );
   stepname = wStepname.getText(); // return value
 }
}
