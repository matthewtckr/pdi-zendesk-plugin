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
import org.eclipse.swt.custom.CCombo;
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
import org.pentaho.di.core.exception.KettleStepException;
import org.pentaho.di.core.row.RowMeta;
import org.pentaho.di.core.row.RowMetaInterface;
import org.pentaho.di.i18n.BaseMessages;
import org.pentaho.di.trans.TransMeta;
import org.pentaho.di.trans.step.BaseStepMeta;
import org.pentaho.di.trans.step.StepDialogInterface;
import org.pentaho.di.trans.steps.zendesk.ZendeskInputUsersMeta;
import org.pentaho.di.ui.core.dialog.ErrorDialog;
import org.pentaho.di.ui.core.widget.LabelTextVar;
import org.pentaho.di.ui.core.widget.PasswordTextVar;
import org.pentaho.di.ui.trans.step.BaseStepDialog;

public class ZendeskInputUsersDialog extends BaseStepDialog implements StepDialogInterface {

 private static Class<?> PKG = ZendeskInputUsersMeta.class; // for i18n purposes, needed by Translator2!!
 private ZendeskInputUsersMeta input;
 private boolean isReceivingInput;

 private CTabFolder wTabFolder;
 private CTabItem wGeneralTab, wUserTab, wIdentityTab;
 private Composite wGeneralComp, wUserComp, wIdentityComp;

 private LabelTextVar wSubDomain, wUsername;
 private Label wlPassword, wlToken;
 private PasswordTextVar wPassword;
 private Button wToken;

 private CCombo wIncomingFieldname;
 private LabelTextVar wUserIdFieldname;
 private LabelTextVar wUrlFieldname;
 private LabelTextVar wExternalIdFieldname;
 private LabelTextVar wNameFieldname;
 private LabelTextVar wEmailFieldname;
 private LabelTextVar wAliasFieldname;
 private LabelTextVar wCreatedAtFieldname;
 private LabelTextVar wUpdatedAtFieldname;
 private LabelTextVar wActiveFieldname;
 private LabelTextVar wVerifiedFieldname;
 private LabelTextVar wSharedFieldname;
 private LabelTextVar wLocaleIdFieldname;
 private LabelTextVar wTimeZoneFieldname;
 private LabelTextVar wLastLoginAtFieldname;
 private LabelTextVar wPhoneFieldname;
 private LabelTextVar wSignatureFieldname;
 private LabelTextVar wDetailsFieldname;
 private LabelTextVar wNotesFieldname;
 private LabelTextVar wOrganizationIdFieldname;
 private LabelTextVar wRoleFieldname;
 private LabelTextVar wCustomRoleIdFieldname;
 private LabelTextVar wModeratorFieldname;
 private LabelTextVar wTicketRestrictionFieldname;
 private LabelTextVar wOnlyPrivateCommentsFieldname;
 private LabelTextVar wTagsFieldname;
 private LabelTextVar wSuspendedFieldname;
 private LabelTextVar wRemotePhotoUrlFieldname;
 private LabelTextVar wUserFieldsFieldname;
 private LabelTextVar wIdentityIdFieldname;
 private LabelTextVar wIdentityUrlFieldname;
 private LabelTextVar wIdentityTypeFieldname;
 private LabelTextVar wIdentityValueFieldname;
 private LabelTextVar wIdentityVerifiedFieldname;
 private LabelTextVar wIdentityPrimaryFieldname;
 private LabelTextVar wIdentityCreatedAtFieldname;
 private LabelTextVar wIdentityUpdatedAtFieldname;

 public ZendeskInputUsersDialog( Shell parent, Object in, TransMeta tr, String sname ) {
   super( parent, (BaseStepMeta) in, tr, sname );
   input = (ZendeskInputUsersMeta) in;
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
   shell.setText( BaseMessages.getString( PKG, "ZendeskInputUsers.Shell.Title" ) );

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

   Label wlIncomingFieldname = new Label( wGeneralComp, SWT.RIGHT );
   wlIncomingFieldname.setText( BaseMessages.getString( PKG, "ZendeskInputUsersDialog.IncomingFieldname.Label" ) );
   wlIncomingFieldname.setToolTipText( BaseMessages.getString( PKG, "ZendeskInputUsersDialog.IncomingFieldname.Tooltip" ) );
   props.setLook( wlIncomingFieldname );
   FormData fdlIncomingFieldname = new FormData();
   fdlIncomingFieldname.left = new FormAttachment( 0, 0 );
   fdlIncomingFieldname.top = new FormAttachment( wlToken, 2 * margin );
   fdlIncomingFieldname.right = new FormAttachment( middle, -margin );
   wlIncomingFieldname.setLayoutData( fdlIncomingFieldname );

   wIncomingFieldname = new CCombo( wGeneralComp, SWT.SINGLE | SWT.LEFT | SWT.BORDER );
   props.setLook( wIncomingFieldname );
   wIncomingFieldname.addModifyListener( lsMod );
   FormData fdIncomingFieldname = new FormData();
   fdIncomingFieldname.left = new FormAttachment( middle, 0 );
   fdIncomingFieldname.top = new FormAttachment( wToken, 2 * margin );
   fdIncomingFieldname.right = new FormAttachment( 100, -margin );
   wIncomingFieldname.setLayoutData( fdIncomingFieldname );

   wlIncomingFieldname.setVisible( false );
   wIncomingFieldname.setVisible( false );
   isReceivingInput = transMeta.findNrPrevSteps( stepMeta ) > 0;
   if ( isReceivingInput ) {
     wlIncomingFieldname.setVisible( true );
     wIncomingFieldname.setVisible( true );
     RowMetaInterface previousFields;
     try {
       previousFields = transMeta.getPrevStepFields( stepMeta );
     } catch ( KettleStepException e ) {
       new ErrorDialog( shell,
         BaseMessages.getString( PKG, "ZendeskInputDialog.Error.UnableToGetInputFields.Title" ),
         BaseMessages.getString( PKG, "ZendeskInputDialog.Error.UnableToGetInputFields.Message" ), e );
       previousFields = new RowMeta();
     }

     wIncomingFieldname.setItems( previousFields.getFieldNames() );
   }

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

   // ////////////////////
   // START OF USER TAB //
   // ////////////////////

   wUserTab = new CTabItem( wTabFolder, SWT.NONE );
   wUserTab.setText( BaseMessages.getString( PKG, "ZendeskInputUsersDialog.UserTab.TabItem" ) );

   wUserComp = new Composite( wTabFolder, SWT.NONE );
   props.setLook( wUserComp );

   FormLayout userLayout = new FormLayout();
   userLayout.marginWidth = margin;
   userLayout.marginHeight = margin;
   wUserComp.setLayout( userLayout );

   // userIdFieldname
   wUserIdFieldname =
     new LabelTextVar(
       transMeta, wUserComp, BaseMessages.getString( PKG, "ZendeskInputUsersDialog.UserIdFieldname.Label" ),
       BaseMessages.getString( PKG, "ZendeskInputUsersDialog.UserIdFieldname.Tooltip" ) );
   props.setLook( wUserIdFieldname );
   wUserIdFieldname.addModifyListener( lsMod );
   FormData fdUserIdFieldname = new FormData();
   fdUserIdFieldname.left = new FormAttachment( 0, -margin );
   fdUserIdFieldname.top = new FormAttachment( wUserComp, 2 * margin );
   fdUserIdFieldname.right = new FormAttachment( 100, -margin );
   wUserIdFieldname.setLayoutData( fdUserIdFieldname );

   // urlFieldname
   wUrlFieldname =
     new LabelTextVar(
       transMeta, wUserComp, BaseMessages.getString( PKG, "ZendeskInputUsersDialog.UserURLFieldname.Label" ),
       BaseMessages.getString( PKG, "ZendeskInputUsersDialog.UserURLFieldname.Tooltip" ) );
   props.setLook( wUrlFieldname );
   wUrlFieldname.addModifyListener( lsMod );
   FormData fdUrlFieldname = new FormData();
   fdUrlFieldname.left = new FormAttachment( 0, -margin );
   fdUrlFieldname.top = new FormAttachment( wUserIdFieldname, 2 * margin );
   fdUrlFieldname.right = new FormAttachment( 100, -margin );
   wUrlFieldname.setLayoutData( fdUrlFieldname );

   // externalIdFieldname
   wExternalIdFieldname =
     new LabelTextVar(
       transMeta, wUserComp, BaseMessages.getString( PKG, "ZendeskInputUsersDialog.ExternalIdFieldname.Label" ),
       BaseMessages.getString( PKG, "ZendeskInputUsersDialog.ExternalIdFieldname.Tooltip" ) );
   props.setLook( wExternalIdFieldname );
   wExternalIdFieldname.addModifyListener( lsMod );
   FormData fdExternalIdFieldname = new FormData();
   fdExternalIdFieldname.left = new FormAttachment( 0, -margin );
   fdExternalIdFieldname.top = new FormAttachment( wUrlFieldname, 2 * margin );
   fdExternalIdFieldname.right = new FormAttachment( 100, -margin );
   wExternalIdFieldname.setLayoutData( fdExternalIdFieldname );

   // nameFieldname
   wNameFieldname =
     new LabelTextVar(
       transMeta, wUserComp, BaseMessages.getString( PKG, "ZendeskInputUsersDialog.NameFieldname.Label" ),
       BaseMessages.getString( PKG, "ZendeskInputUsersDialog.NameFieldname.Tooltip" ) );
   props.setLook( wNameFieldname );
   wNameFieldname.addModifyListener( lsMod );
   FormData fdNameFieldname = new FormData();
   fdNameFieldname.left = new FormAttachment( 0, -margin );
   fdNameFieldname.top = new FormAttachment( wExternalIdFieldname, 2 * margin );
   fdNameFieldname.right = new FormAttachment( 100, -margin );
   wNameFieldname.setLayoutData( fdNameFieldname );

   // emailFieldname
   wEmailFieldname =
     new LabelTextVar(
       transMeta, wUserComp, BaseMessages.getString( PKG, "ZendeskInputUsersDialog.NameFieldname.Label" ),
       BaseMessages.getString( PKG, "ZendeskInputUsersDialog.NameFieldname.Tooltip" ) );
   props.setLook( wEmailFieldname );
   wEmailFieldname.addModifyListener( lsMod );
   FormData fdEmailFieldname = new FormData();
   fdEmailFieldname.left = new FormAttachment( 0, -margin );
   fdEmailFieldname.top = new FormAttachment( wNameFieldname, 2 * margin );
   fdEmailFieldname.right = new FormAttachment( 100, -margin );
   wEmailFieldname.setLayoutData( fdEmailFieldname );

   // aliasFieldname
   wAliasFieldname =
     new LabelTextVar(
       transMeta, wUserComp, BaseMessages.getString( PKG, "ZendeskInputUsersDialog.AliasFieldname.Label" ),
       BaseMessages.getString( PKG, "ZendeskInputUsersDialog.AliasFieldname.Tooltip" ) );
   props.setLook( wAliasFieldname );
   wAliasFieldname.addModifyListener( lsMod );
   FormData fdAliasFieldname = new FormData();
   fdAliasFieldname.left = new FormAttachment( 0, -margin );
   fdAliasFieldname.top = new FormAttachment( wEmailFieldname, 2 * margin );
   fdAliasFieldname.right = new FormAttachment( 100, -margin );
   wAliasFieldname.setLayoutData( fdAliasFieldname );

   // createdAtFieldname
   wCreatedAtFieldname =
     new LabelTextVar(
       transMeta, wUserComp, BaseMessages.getString( PKG, "ZendeskInputUsersDialog.CreatedAtFieldname.Label" ),
       BaseMessages.getString( PKG, "ZendeskInputUsersDialog.CreatedAtFieldname.Tooltip" ) );
   props.setLook( wCreatedAtFieldname );
   wCreatedAtFieldname.addModifyListener( lsMod );
   FormData fdCreatedAtFieldname = new FormData();
   fdCreatedAtFieldname.left = new FormAttachment( 0, -margin );
   fdCreatedAtFieldname.top = new FormAttachment( wAliasFieldname, 2 * margin );
   fdCreatedAtFieldname.right = new FormAttachment( 100, -margin );
   wCreatedAtFieldname.setLayoutData( fdCreatedAtFieldname );

   // updatedAtFieldname
   wUpdatedAtFieldname =
     new LabelTextVar(
       transMeta, wUserComp, BaseMessages.getString( PKG, "ZendeskInputUsersDialog.UpdatedAtFieldname.Label" ),
       BaseMessages.getString( PKG, "ZendeskInputUsersDialog.UpdatedAtFieldname.Tooltip" ) );
   props.setLook( wUpdatedAtFieldname );
   wUpdatedAtFieldname.addModifyListener( lsMod );
   FormData fdUpdatedAtFieldname = new FormData();
   fdUpdatedAtFieldname.left = new FormAttachment( 0, -margin );
   fdUpdatedAtFieldname.top = new FormAttachment( wCreatedAtFieldname, 2 * margin );
   fdUpdatedAtFieldname.right = new FormAttachment( 100, -margin );
   wUpdatedAtFieldname.setLayoutData( fdUpdatedAtFieldname );

   // activeFieldname
   wActiveFieldname =
     new LabelTextVar(
       transMeta, wUserComp, BaseMessages.getString( PKG, "ZendeskInputUsersDialog.ActiveFieldname.Label" ),
       BaseMessages.getString( PKG, "ZendeskInputUsersDialog.ActiveFieldname.Tooltip" ) );
   props.setLook( wActiveFieldname );
   wActiveFieldname.addModifyListener( lsMod );
   FormData fdActiveFieldname = new FormData();
   fdActiveFieldname.left = new FormAttachment( 0, -margin );
   fdActiveFieldname.top = new FormAttachment( wUpdatedAtFieldname, 2 * margin );
   fdActiveFieldname.right = new FormAttachment( 100, -margin );
   wActiveFieldname.setLayoutData( fdActiveFieldname );

   // verifiedFieldname
   wVerifiedFieldname =
     new LabelTextVar(
       transMeta, wUserComp, BaseMessages.getString( PKG, "ZendeskInputUsersDialog.VerifiedFieldname.Label" ),
       BaseMessages.getString( PKG, "ZendeskInputUsersDialog.VerifiedFieldname.Tooltip" ) );
   props.setLook( wVerifiedFieldname );
   wVerifiedFieldname.addModifyListener( lsMod );
   FormData fdVerifiedFieldname = new FormData();
   fdVerifiedFieldname.left = new FormAttachment( 0, -margin );
   fdVerifiedFieldname.top = new FormAttachment( wActiveFieldname, 2 * margin );
   fdVerifiedFieldname.right = new FormAttachment( 100, -margin );
   wVerifiedFieldname.setLayoutData( fdVerifiedFieldname );

   // sharedFieldname
   wSharedFieldname =
     new LabelTextVar(
       transMeta, wUserComp, BaseMessages.getString( PKG, "ZendeskInputUsersDialog.SharedFieldname.Label" ),
       BaseMessages.getString( PKG, "ZendeskInputUsersDialog.SharedFieldname.Tooltip" ) );
   props.setLook( wSharedFieldname );
   wSharedFieldname.addModifyListener( lsMod );
   FormData fdSharedFieldname = new FormData();
   fdSharedFieldname.left = new FormAttachment( 0, -margin );
   fdSharedFieldname.top = new FormAttachment( wVerifiedFieldname, 2 * margin );
   fdSharedFieldname.right = new FormAttachment( 100, -margin );
   wSharedFieldname.setLayoutData( fdSharedFieldname );

   // localeIdFieldname
   wLocaleIdFieldname =
     new LabelTextVar(
       transMeta, wUserComp, BaseMessages.getString( PKG, "ZendeskInputUsersDialog.LocaleIDFieldname.Label" ),
       BaseMessages.getString( PKG, "ZendeskInputUsersDialog.LocaleIDFieldname.Tooltip" ) );
   props.setLook( wLocaleIdFieldname );
   wLocaleIdFieldname.addModifyListener( lsMod );
   FormData fdLocaleIdFieldname = new FormData();
   fdLocaleIdFieldname.left = new FormAttachment( 0, -margin );
   fdLocaleIdFieldname.top = new FormAttachment( wSharedFieldname, 2 * margin );
   fdLocaleIdFieldname.right = new FormAttachment( 100, -margin );
   wLocaleIdFieldname.setLayoutData( fdLocaleIdFieldname );

   // timeZoneFieldname
   wTimeZoneFieldname =
     new LabelTextVar(
       transMeta, wUserComp, BaseMessages.getString( PKG, "ZendeskInputUsersDialog.TimezoneFieldname.Label" ),
       BaseMessages.getString( PKG, "ZendeskInputUsersDialog.TimezoneFieldname.Tooltip" ) );
   props.setLook( wTimeZoneFieldname );
   wTimeZoneFieldname.addModifyListener( lsMod );
   FormData fdTimeZoneFieldname = new FormData();
   fdTimeZoneFieldname.left = new FormAttachment( 0, -margin );
   fdTimeZoneFieldname.top = new FormAttachment( wLocaleIdFieldname, 2 * margin );
   fdTimeZoneFieldname.right = new FormAttachment( 100, -margin );
   wTimeZoneFieldname.setLayoutData( fdTimeZoneFieldname );

   // lastLoginAtFieldname
   wLastLoginAtFieldname =
     new LabelTextVar(
       transMeta, wUserComp, BaseMessages.getString( PKG, "ZendeskInputUsersDialog.LastLoginAtFieldname.Label" ),
       BaseMessages.getString( PKG, "ZendeskInputUsersDialog.LastLoginAtFieldname.Tooltip" ) );
   props.setLook( wLastLoginAtFieldname );
   wLastLoginAtFieldname.addModifyListener( lsMod );
   FormData fdLastLoginAtFieldname = new FormData();
   fdLastLoginAtFieldname.left = new FormAttachment( 0, -margin );
   fdLastLoginAtFieldname.top = new FormAttachment( wTimeZoneFieldname, 2 * margin );
   fdLastLoginAtFieldname.right = new FormAttachment( 100, -margin );
   wLastLoginAtFieldname.setLayoutData( fdLastLoginAtFieldname );

   // phoneFieldname
   wPhoneFieldname =
     new LabelTextVar(
       transMeta, wUserComp, BaseMessages.getString( PKG, "ZendeskInputUsersDialog.PhoneFieldname.Label" ),
       BaseMessages.getString( PKG, "ZendeskInputUsersDialog.PhoneFieldname.Tooltip" ) );
   props.setLook( wPhoneFieldname );
   wPhoneFieldname.addModifyListener( lsMod );
   FormData fdPhoneFieldname = new FormData();
   fdPhoneFieldname.left = new FormAttachment( 0, -margin );
   fdPhoneFieldname.top = new FormAttachment( wLastLoginAtFieldname, 2 * margin );
   fdPhoneFieldname.right = new FormAttachment( 100, -margin );
   wPhoneFieldname.setLayoutData( fdPhoneFieldname );

   // signatureFieldname
   wSignatureFieldname =
     new LabelTextVar(
       transMeta, wUserComp, BaseMessages.getString( PKG, "ZendeskInputUsersDialog.SignatureFieldname.Label" ),
       BaseMessages.getString( PKG, "ZendeskInputUsersDialog.SignatureFieldname.Tooltip" ) );
   props.setLook( wSignatureFieldname );
   wSignatureFieldname.addModifyListener( lsMod );
   FormData fdSignatureFieldname = new FormData();
   fdSignatureFieldname.left = new FormAttachment( 0, -margin );
   fdSignatureFieldname.top = new FormAttachment( wPhoneFieldname, 2 * margin );
   fdSignatureFieldname.right = new FormAttachment( 100, -margin );
   wSignatureFieldname.setLayoutData( fdSignatureFieldname );

   // wDetailsFieldname
   wDetailsFieldname =
     new LabelTextVar(
       transMeta, wUserComp, BaseMessages.getString( PKG, "ZendeskInputUsersDialog.DetailsFieldname.Label" ),
       BaseMessages.getString( PKG, "ZendeskInputUsersDialog.DetailsFieldname.Tooltip" ) );
   props.setLook( wDetailsFieldname );
   wDetailsFieldname.addModifyListener( lsMod );
   FormData fdDetailsFieldname = new FormData();
   fdDetailsFieldname.left = new FormAttachment( 0, -margin );
   fdDetailsFieldname.top = new FormAttachment( wSignatureFieldname, 2 * margin );
   fdDetailsFieldname.right = new FormAttachment( 100, -margin );
   wDetailsFieldname.setLayoutData( fdDetailsFieldname );

   // notesFieldname
   wNotesFieldname =
     new LabelTextVar(
       transMeta, wUserComp, BaseMessages.getString( PKG, "ZendeskInputUsersDialog.NotesFieldname.Label" ),
       BaseMessages.getString( PKG, "ZendeskInputUsersDialog.NotesFieldname.Tooltip" ) );
   props.setLook( wNotesFieldname );
   wNotesFieldname.addModifyListener( lsMod );
   FormData fdNotesFieldname = new FormData();
   fdNotesFieldname.left = new FormAttachment( 0, -margin );
   fdNotesFieldname.top = new FormAttachment( wDetailsFieldname, 2 * margin );
   fdNotesFieldname.right = new FormAttachment( 100, -margin );
   wNotesFieldname.setLayoutData( fdNotesFieldname );

   // organizationIdFieldname
   wOrganizationIdFieldname =
     new LabelTextVar(
       transMeta, wUserComp, BaseMessages.getString( PKG, "ZendeskInputUsersDialog.OrganizationIDFieldname.Label" ),
       BaseMessages.getString( PKG, "ZendeskInputUsersDialog.OrganizationIDFieldname.Tooltip" ) );
   props.setLook( wOrganizationIdFieldname );
   wOrganizationIdFieldname.addModifyListener( lsMod );
   FormData fdOrganizationIdFieldname = new FormData();
   fdOrganizationIdFieldname.left = new FormAttachment( 0, -margin );
   fdOrganizationIdFieldname.top = new FormAttachment( wNotesFieldname, 2 * margin );
   fdOrganizationIdFieldname.right = new FormAttachment( 100, -margin );
   wOrganizationIdFieldname.setLayoutData( fdOrganizationIdFieldname );

   // roleFieldname
   wRoleFieldname =
     new LabelTextVar(
       transMeta, wUserComp, BaseMessages.getString( PKG, "ZendeskInputUsersDialog.RoleFieldname.Label" ),
       BaseMessages.getString( PKG, "ZendeskInputUsersDialog.RoleFieldname.Tooltip" ) );
   props.setLook( wRoleFieldname );
   wRoleFieldname.addModifyListener( lsMod );
   FormData fdRoleFieldname = new FormData();
   fdRoleFieldname.left = new FormAttachment( 0, -margin );
   fdRoleFieldname.top = new FormAttachment( wOrganizationIdFieldname, 2 * margin );
   fdRoleFieldname.right = new FormAttachment( 100, -margin );
   wRoleFieldname.setLayoutData( fdRoleFieldname );

   // customRoleIdFieldname
   wCustomRoleIdFieldname =
     new LabelTextVar(
       transMeta, wUserComp, BaseMessages.getString( PKG, "ZendeskInputUsersDialog.CustomRoleIDFieldname.Label" ),
       BaseMessages.getString( PKG, "ZendeskInputUsersDialog.CustomRoleIDFieldname.Tooltip" ) );
   props.setLook( wCustomRoleIdFieldname );
   wCustomRoleIdFieldname.addModifyListener( lsMod );
   FormData fdCustomRoleIdFieldname = new FormData();
   fdCustomRoleIdFieldname.left = new FormAttachment( 0, -margin );
   fdCustomRoleIdFieldname.top = new FormAttachment( wRoleFieldname, 2 * margin );
   fdCustomRoleIdFieldname.right = new FormAttachment( 100, -margin );
   wCustomRoleIdFieldname.setLayoutData( fdCustomRoleIdFieldname );

   // moderatorFieldname
   wModeratorFieldname =
     new LabelTextVar(
       transMeta, wUserComp, BaseMessages.getString( PKG, "ZendeskInputUsersDialog.ModeratorFieldname.Label" ),
       BaseMessages.getString( PKG, "ZendeskInputUsersDialog.ModeratorFieldname.Tooltip" ) );
   props.setLook( wModeratorFieldname );
   wModeratorFieldname.addModifyListener( lsMod );
   FormData fdModeratorFieldname = new FormData();
   fdModeratorFieldname.left = new FormAttachment( 0, -margin );
   fdModeratorFieldname.top = new FormAttachment( wCustomRoleIdFieldname, 2 * margin );
   fdModeratorFieldname.right = new FormAttachment( 100, -margin );
   wModeratorFieldname.setLayoutData( fdModeratorFieldname );

   // ticketRestrictionFieldname
   wTicketRestrictionFieldname =
     new LabelTextVar(
       transMeta, wUserComp, BaseMessages.getString( PKG, "ZendeskInputUsersDialog.TicketRestrictionFieldname.Label" ),
       BaseMessages.getString( PKG, "ZendeskInputUsersDialog.TicketRestrictionFieldname.Tooltip" ) );
   props.setLook( wTicketRestrictionFieldname );
   wTicketRestrictionFieldname.addModifyListener( lsMod );
   FormData fdTicketRestrictionFieldname = new FormData();
   fdTicketRestrictionFieldname.left = new FormAttachment( 0, -margin );
   fdTicketRestrictionFieldname.top = new FormAttachment( wModeratorFieldname, 2 * margin );
   fdTicketRestrictionFieldname.right = new FormAttachment( 100, -margin );
   wTicketRestrictionFieldname.setLayoutData( fdTicketRestrictionFieldname );

   // onlyPrivateCommentsFieldname
   wOnlyPrivateCommentsFieldname =
     new LabelTextVar(
       transMeta, wUserComp, BaseMessages.getString( PKG, "ZendeskInputUsersDialog.OnlyPrivateCommentsFieldname.Label" ),
       BaseMessages.getString( PKG, "ZendeskInputUsersDialog.OnlyPrivateCommentsFieldname.Tooltip" ) );
   props.setLook( wOnlyPrivateCommentsFieldname );
   wOnlyPrivateCommentsFieldname.addModifyListener( lsMod );
   FormData fdOnlyPrivateCommentsFieldname = new FormData();
   fdOnlyPrivateCommentsFieldname.left = new FormAttachment( 0, -margin );
   fdOnlyPrivateCommentsFieldname.top = new FormAttachment( wTicketRestrictionFieldname, 2 * margin );
   fdOnlyPrivateCommentsFieldname.right = new FormAttachment( 100, -margin );
   wOnlyPrivateCommentsFieldname.setLayoutData( fdOnlyPrivateCommentsFieldname );

   // tagsFieldname
   wTagsFieldname =
     new LabelTextVar(
       transMeta, wUserComp, BaseMessages.getString( PKG, "ZendeskInputUsersDialog.TagsFieldname.Label" ),
       BaseMessages.getString( PKG, "ZendeskInputUsersDialog.TagsFieldname.Tooltip" ) );
   props.setLook( wTagsFieldname );
   wTagsFieldname.addModifyListener( lsMod );
   FormData fdTagsFieldname = new FormData();
   fdTagsFieldname.left = new FormAttachment( 0, -margin );
   fdTagsFieldname.top = new FormAttachment( wOnlyPrivateCommentsFieldname, 2 * margin );
   fdTagsFieldname.right = new FormAttachment( 100, -margin );
   wTagsFieldname.setLayoutData( fdTagsFieldname );

   // suspendedFieldname
   wSuspendedFieldname =
     new LabelTextVar(
       transMeta, wUserComp, BaseMessages.getString( PKG, "ZendeskInputUsersDialog.SuspendedFieldname.Label" ),
       BaseMessages.getString( PKG, "ZendeskInputUsersDialog.SuspendedFieldname.Tooltip" ) );
   props.setLook( wSuspendedFieldname );
   wSuspendedFieldname.addModifyListener( lsMod );
   FormData fdSuspendedFieldname = new FormData();
   fdSuspendedFieldname.left = new FormAttachment( 0, -margin );
   fdSuspendedFieldname.top = new FormAttachment( wTagsFieldname, 2 * margin );
   fdSuspendedFieldname.right = new FormAttachment( 100, -margin );
   wSuspendedFieldname.setLayoutData( fdSuspendedFieldname );

   // remotePhotoUrlFieldname
   wRemotePhotoUrlFieldname =
     new LabelTextVar(
       transMeta, wUserComp, BaseMessages.getString( PKG, "ZendeskInputUsersDialog.RemotePhotoURLFieldname.Label" ),
       BaseMessages.getString( PKG, "ZendeskInputUsersDialog.RemotePhotoURLFieldname.Tooltip" ) );
   props.setLook( wRemotePhotoUrlFieldname );
   wRemotePhotoUrlFieldname.addModifyListener( lsMod );
   FormData fdRemotePhotoUrlFieldname = new FormData();
   fdRemotePhotoUrlFieldname.left = new FormAttachment( 0, -margin );
   fdRemotePhotoUrlFieldname.top = new FormAttachment( wSuspendedFieldname, 2 * margin );
   fdRemotePhotoUrlFieldname.right = new FormAttachment( 100, -margin );
   wRemotePhotoUrlFieldname.setLayoutData( fdRemotePhotoUrlFieldname );

   // userFieldsFieldname
   wUserFieldsFieldname =
     new LabelTextVar(
       transMeta, wUserComp, BaseMessages.getString( PKG, "ZendeskInputUsersDialog.UserFieldsFieldname.Label" ),
       BaseMessages.getString( PKG, "ZendeskInputUsersDialog.UserFieldsFieldname.Tooltip" ) );
   props.setLook( wUserFieldsFieldname );
   wUserFieldsFieldname.addModifyListener( lsMod );
   FormData fdUserFieldsFieldname = new FormData();
   fdUserFieldsFieldname.left = new FormAttachment( 0, -margin );
   fdUserFieldsFieldname.top = new FormAttachment( wRemotePhotoUrlFieldname, 2 * margin );
   fdUserFieldsFieldname.right = new FormAttachment( 100, -margin );
   wUserFieldsFieldname.setLayoutData( fdUserFieldsFieldname );

   FormData fdUserComp = new FormData();
   fdUserComp.left = new FormAttachment( 0, 0 );
   fdUserComp.top = new FormAttachment( 0, 0 );
   fdUserComp.right = new FormAttachment( 100, 0 );
   fdUserComp.bottom = new FormAttachment( 100, 0 );
   wUserComp.setLayoutData( fdUserComp );

   wUserComp.layout();
   wUserTab.setControl( wUserComp );

   // //////////////////
   // END OF USER TAB //
   // //////////////////

   // ////////////////////////
   // START OF IDENTITY TAB //
   // ////////////////////////

   wIdentityTab = new CTabItem( wTabFolder, SWT.NONE );
   wIdentityTab.setText( BaseMessages.getString( PKG, "ZendeskInputUsersDialog.IdentityTab.TabItem" ) );

   wIdentityComp = new Composite( wTabFolder, SWT.NONE );
   props.setLook( wIdentityComp );

   FormLayout identityLayout = new FormLayout();
   identityLayout.marginWidth = margin;
   identityLayout.marginHeight = margin;
   wIdentityComp.setLayout( identityLayout );

   // identityIdFieldname
   wIdentityIdFieldname =
     new LabelTextVar(
       transMeta, wIdentityComp, BaseMessages.getString( PKG, "ZendeskInputUsersDialog.IdentityIDFieldname.Label" ),
       BaseMessages.getString( PKG, "ZendeskInputUsersDialog.IdentityIDFieldname.Tooltip" ) );
   props.setLook( wIdentityIdFieldname );
   wIdentityIdFieldname.addModifyListener( lsMod );
   FormData fdIdentityIdFieldname = new FormData();
   fdIdentityIdFieldname.left = new FormAttachment( 0, -margin );
   fdIdentityIdFieldname.top = new FormAttachment( wUserFieldsFieldname, 2 * margin );
   fdIdentityIdFieldname.right = new FormAttachment( 100, -margin );
   wIdentityIdFieldname.setLayoutData( fdIdentityIdFieldname );

   // identityUrlFieldname
   wIdentityUrlFieldname =
     new LabelTextVar(
       transMeta, wIdentityComp, BaseMessages.getString( PKG, "ZendeskInputUsersDialog.IdentityURLFieldname.Label" ),
       BaseMessages.getString( PKG, "ZendeskInputUsersDialog.IdentityURLFieldname.Tooltip" ) );
   props.setLook( wIdentityUrlFieldname );
   wIdentityUrlFieldname.addModifyListener( lsMod );
   FormData fdIdentityUrlFieldname = new FormData();
   fdIdentityUrlFieldname.left = new FormAttachment( 0, -margin );
   fdIdentityUrlFieldname.top = new FormAttachment( wIdentityIdFieldname, 2 * margin );
   fdIdentityUrlFieldname.right = new FormAttachment( 100, -margin );
   wIdentityUrlFieldname.setLayoutData( fdIdentityUrlFieldname );

   // identityTypeFieldname
   wIdentityTypeFieldname =
     new LabelTextVar(
       transMeta, wIdentityComp, BaseMessages.getString( PKG, "ZendeskInputUsersDialog.IdentityTypeFieldname.Label" ),
       BaseMessages.getString( PKG, "ZendeskInputUsersDialog.IdentityTypeFieldname.Tooltip" ) );
   props.setLook( wIdentityTypeFieldname );
   wIdentityTypeFieldname.addModifyListener( lsMod );
   FormData fdIdentityTypeFieldname = new FormData();
   fdIdentityTypeFieldname.left = new FormAttachment( 0, -margin );
   fdIdentityTypeFieldname.top = new FormAttachment( wIdentityUrlFieldname, 2 * margin );
   fdIdentityTypeFieldname.right = new FormAttachment( 100, -margin );
   wIdentityTypeFieldname.setLayoutData( fdIdentityTypeFieldname );

   // identityValueFieldname
   wIdentityValueFieldname =
     new LabelTextVar(
       transMeta, wIdentityComp, BaseMessages.getString( PKG, "ZendeskInputUsersDialog.IdentityValueFieldname.Label" ),
       BaseMessages.getString( PKG, "ZendeskInputUsersDialog.IdentityValueFieldname.Tooltip" ) );
   props.setLook( wIdentityValueFieldname );
   wIdentityValueFieldname.addModifyListener( lsMod );
   FormData fdIdentityValueFieldname = new FormData();
   fdIdentityValueFieldname.left = new FormAttachment( 0, -margin );
   fdIdentityValueFieldname.top = new FormAttachment( wIdentityTypeFieldname, 2 * margin );
   fdIdentityValueFieldname.right = new FormAttachment( 100, -margin );
   wIdentityValueFieldname.setLayoutData( fdIdentityValueFieldname );

   // identityVerifiedFieldname
   wIdentityVerifiedFieldname =
     new LabelTextVar(
       transMeta, wIdentityComp, BaseMessages.getString( PKG, "ZendeskInputUsersDialog.IdentityVerifiedFieldname.Label" ),
       BaseMessages.getString( PKG, "ZendeskInputUsersDialog.IdentityVerifiedFieldname.Tooltip" ) );
   props.setLook( wIdentityVerifiedFieldname );
   wIdentityVerifiedFieldname.addModifyListener( lsMod );
   FormData fdIdentityVerifiedFieldname = new FormData();
   fdIdentityVerifiedFieldname.left = new FormAttachment( 0, -margin );
   fdIdentityVerifiedFieldname.top = new FormAttachment( wIdentityValueFieldname, 2 * margin );
   fdIdentityVerifiedFieldname.right = new FormAttachment( 100, -margin );
   wIdentityVerifiedFieldname.setLayoutData( fdIdentityVerifiedFieldname );

   // identityPrimaryFieldname
   wIdentityPrimaryFieldname =
     new LabelTextVar(
       transMeta, wIdentityComp, BaseMessages.getString( PKG, "ZendeskInputUsersDialog.IdentityPrimaryFieldname.Label" ),
       BaseMessages.getString( PKG, "ZendeskInputUsersDialog.IdentityPrimaryFieldname.Tooltip" ) );
   props.setLook( wIdentityPrimaryFieldname );
   wIdentityPrimaryFieldname.addModifyListener( lsMod );
   FormData fdIdentityPrimaryFieldname = new FormData();
   fdIdentityPrimaryFieldname.left = new FormAttachment( 0, -margin );
   fdIdentityPrimaryFieldname.top = new FormAttachment( wIdentityVerifiedFieldname, 2 * margin );
   fdIdentityPrimaryFieldname.right = new FormAttachment( 100, -margin );
   wIdentityPrimaryFieldname.setLayoutData( fdIdentityPrimaryFieldname );

   // identityCreatedAtFieldname
   wIdentityCreatedAtFieldname =
     new LabelTextVar(
       transMeta, wIdentityComp, BaseMessages.getString( PKG, "ZendeskInputUsersDialog.IdentityCreatedAtFieldname.Label" ),
       BaseMessages.getString( PKG, "ZendeskInputUsersDialog.IdentityCreatedAtFieldname.Tooltip" ) );
   props.setLook( wIdentityCreatedAtFieldname );
   wIdentityCreatedAtFieldname.addModifyListener( lsMod );
   FormData fdIdentityCreatedAtFieldname = new FormData();
   fdIdentityCreatedAtFieldname.left = new FormAttachment( 0, -margin );
   fdIdentityCreatedAtFieldname.top = new FormAttachment( wIdentityPrimaryFieldname, 2 * margin );
   fdIdentityCreatedAtFieldname.right = new FormAttachment( 100, -margin );
   wIdentityCreatedAtFieldname.setLayoutData( fdIdentityCreatedAtFieldname );

   // identityUpdatedAtFieldname
   wIdentityUpdatedAtFieldname =
     new LabelTextVar(
       transMeta, wIdentityComp, BaseMessages.getString( PKG, "ZendeskInputUsersDialog.IdentityUpdatedAtFieldname.Label" ),
       BaseMessages.getString( PKG, "ZendeskInputUsersDialog.IdentityUpdatedAtFieldname.Tooltip" ) );
   props.setLook( wIdentityUpdatedAtFieldname );
   wIdentityUpdatedAtFieldname.addModifyListener( lsMod );
   FormData fdIdentityUpdatedAtFieldname = new FormData();
   fdIdentityUpdatedAtFieldname.left = new FormAttachment( 0, -margin );
   fdIdentityUpdatedAtFieldname.top = new FormAttachment( wIdentityCreatedAtFieldname, 2 * margin );
   fdIdentityUpdatedAtFieldname.right = new FormAttachment( 100, -margin );
   wIdentityUpdatedAtFieldname.setLayoutData( fdIdentityUpdatedAtFieldname );

   FormData fdIdentityComp = new FormData();
   fdIdentityComp.left = new FormAttachment( 0, 0 );
   fdIdentityComp.top = new FormAttachment( 0, 0 );
   fdIdentityComp.right = new FormAttachment( 100, 0 );
   fdIdentityComp.bottom = new FormAttachment( 100, 0 );
   wIdentityComp.setLayoutData( fdIdentityComp );

   wIdentityComp.layout();
   wIdentityTab.setControl( wIdentityComp );

   // //////////////////////
   // END OF IDENTITY TAB //
   // //////////////////////

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
   if ( isReceivingInput ) {
     wIncomingFieldname.setText( Const.NVL( input.getIncomingFieldname(), "" ) );
   } else {
     wIncomingFieldname.setText( "" );
   }
   wUserIdFieldname.setText( Const.NVL( input.getUserIdFieldname(), "" ) );
   wUrlFieldname.setText( Const.NVL( input.getUrlFieldname(), "" ) );
   wExternalIdFieldname.setText( Const.NVL( input.getExternalIdFieldname(), "" ) );
   wNameFieldname.setText( Const.NVL( input.getNameFieldname(), "" ) );
   wEmailFieldname.setText( Const.NVL( input.getEmailFieldname(), "" ) );
   wAliasFieldname.setText( Const.NVL( input.getAliasFieldname(), "" ) );
   wCreatedAtFieldname.setText( Const.NVL( input.getCreatedAtFieldname(), "" ) );
   wUpdatedAtFieldname.setText( Const.NVL( input.getUpdatedAtFieldname(), "" ) );
   wActiveFieldname.setText( Const.NVL( input.getActiveFieldname(), "" ) );
   wVerifiedFieldname.setText( Const.NVL( input.getVerifiedFieldname(), "" ) );
   wSharedFieldname.setText( Const.NVL( input.getSharedFieldname(), "" ) );
   wLocaleIdFieldname.setText( Const.NVL( input.getLocaleIdFieldname(), "" ) );
   wTimeZoneFieldname.setText( Const.NVL( input.getTimeZoneFieldname(), "" ) );
   wLastLoginAtFieldname.setText( Const.NVL( input.getLastLoginAtFieldname(), "" ) );
   wPhoneFieldname.setText( Const.NVL( input.getPhoneFieldname(), "" ) );
   wSignatureFieldname.setText( Const.NVL( input.getSignatureFieldname(), "" ) );
   wDetailsFieldname.setText( Const.NVL( input.getDetailsFieldname(), "" ) );
   wNotesFieldname.setText( Const.NVL( input.getNotesFieldname(), "" ) );
   wOrganizationIdFieldname.setText( Const.NVL( input.getOrganizationIdFieldname(), "" ) );
   wRoleFieldname.setText( Const.NVL( input.getRoleFieldname(), "" ) );
   wCustomRoleIdFieldname.setText( Const.NVL( input.getCustomRoleIdFieldname(), "" ) );
   wModeratorFieldname.setText( Const.NVL( input.getModeratorFieldname(), "" ) );
   wTicketRestrictionFieldname.setText( Const.NVL( input.getTicketRestrictionFieldname(), "" ) );
   wOnlyPrivateCommentsFieldname.setText( Const.NVL( input.getOnlyPrivateCommentsFieldname(), "" ) );
   wTagsFieldname.setText( Const.NVL( input.getTagsFieldname(), "" ) );
   wSuspendedFieldname.setText( Const.NVL( input.getSuspendedFieldname(), "" ) );
   wRemotePhotoUrlFieldname.setText( Const.NVL( input.getRemotePhotoUrlFieldname(), "" ) );
   wUserFieldsFieldname.setText( Const.NVL( input.getUserFieldsFieldname(), "" ) );
   wIdentityIdFieldname.setText( Const.NVL( input.getIdentityIdFieldname(), "" ) );
   wIdentityUrlFieldname.setText( Const.NVL( input.getIdentityUrlFieldname(), "" ) );
   wIdentityTypeFieldname.setText( Const.NVL( input.getIdentityTypeFieldname(), "" ) );
   wIdentityValueFieldname.setText( Const.NVL( input.getIdentityValueFieldname(), "" ) );
   wIdentityVerifiedFieldname.setText( Const.NVL( input.getIdentityVerifiedFieldname(), "" ) );
   wIdentityPrimaryFieldname.setText( Const.NVL( input.getIdentityPrimaryFieldname(), "" ) );
   wIdentityCreatedAtFieldname.setText( Const.NVL( input.getIdentityCreatedAtFieldname(), "" ) );
   wIdentityUpdatedAtFieldname.setText( Const.NVL( input.getIdentityUpdatedAtFieldname(), "" ) );

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

 private void getInfo( ZendeskInputUsersMeta inf ) {
   inf.setSubDomain( wSubDomain.getText() );
   inf.setUsername( wUsername.getText() );
   inf.setPassword( wPassword.getText() );
   inf.setToken( wToken.getSelection() );
   if ( isReceivingInput ) {
     inf.setIncomingFieldname( wIncomingFieldname.getText() );
   } else {
     inf.setIncomingFieldname( "" );
   }
   inf.setUserIdFieldname( wUserIdFieldname.getText() );
   inf.setUrlFieldname( wUrlFieldname.getText() );
   inf.setExternalIdFieldname( wExternalIdFieldname.getText() );
   inf.setNameFieldname( wNameFieldname.getText() );
   inf.setEmailFieldname( wEmailFieldname.getText() );
   inf.setAliasFieldname( wAliasFieldname.getText() );
   inf.setCreatedAtFieldname( wCreatedAtFieldname.getText() );
   inf.setUpdatedAtFieldname( wUpdatedAtFieldname.getText() );
   inf.setActiveFieldname( wActiveFieldname.getText() );
   inf.setVerifiedFieldname( wVerifiedFieldname.getText() );
   inf.setSharedFieldname( wSharedFieldname.getText() );
   inf.setLocaleIdFieldname( wLocaleIdFieldname.getText() );
   inf.setTimeZoneFieldname( wTimeZoneFieldname.getText() );
   inf.setLastLoginAtFieldname( wLastLoginAtFieldname.getText() );
   inf.setPhoneFieldname( wPhoneFieldname.getText() );
   inf.setSignatureFieldname( wSignatureFieldname.getText() );
   inf.setDetailsFieldname( wDetailsFieldname.getText() );
   inf.setNotesFieldname( wNotesFieldname.getText() );
   inf.setOrganizationIdFieldname( wOrganizationIdFieldname.getText() );
   inf.setRoleFieldname( wRoleFieldname.getText() );
   inf.setCustomRoleIdFieldname( wCustomRoleIdFieldname.getText() );
   inf.setModeratorFieldname( wModeratorFieldname.getText() );
   inf.setTicketRestrictionFieldname( wTicketRestrictionFieldname.getText() );
   inf.setOnlyPrivateCommentsFieldname( wOnlyPrivateCommentsFieldname.getText() );
   inf.setTagsFieldname( wTagsFieldname.getText() );
   inf.setSuspendedFieldname( wSuspendedFieldname.getText() );
   inf.setRemotePhotoUrlFieldname( wRemotePhotoUrlFieldname.getText() );
   inf.setUserFieldsFieldname( wUserFieldsFieldname.getText() );
   inf.setIdentityIdFieldname( wIdentityIdFieldname.getText() );
   inf.setIdentityUrlFieldname( wIdentityUrlFieldname.getText() );
   inf.setIdentityTypeFieldname( wIdentityTypeFieldname.getText() );
   inf.setIdentityValueFieldname( wIdentityValueFieldname.getText() );
   inf.setIdentityVerifiedFieldname( wIdentityVerifiedFieldname.getText() );
   inf.setIdentityPrimaryFieldname( wIdentityPrimaryFieldname.getText() );
   inf.setIdentityCreatedAtFieldname( wIdentityCreatedAtFieldname.getText() );
   inf.setIdentityUpdatedAtFieldname( wIdentityUpdatedAtFieldname.getText() );

   stepname = wStepname.getText(); // return value
 }
}
