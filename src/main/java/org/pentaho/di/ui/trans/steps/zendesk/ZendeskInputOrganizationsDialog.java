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
import org.pentaho.di.trans.steps.zendesk.ZendeskInputOrganizationsMeta;
import org.pentaho.di.ui.core.dialog.ErrorDialog;
import org.pentaho.di.ui.core.widget.LabelTextVar;
import org.pentaho.di.ui.core.widget.PasswordTextVar;
import org.pentaho.di.ui.trans.step.BaseStepDialog;

public class ZendeskInputOrganizationsDialog extends BaseStepDialog implements StepDialogInterface {

 private static Class<?> PKG = ZendeskInputOrganizationsMeta.class; // for i18n purposes, needed by Translator2!!
 private ZendeskInputOrganizationsMeta input;
 private boolean isReceivingInput;

 private CTabFolder wTabFolder;
 private CTabItem wGeneralTab, wOrganizationTab, wTagsTab, wFieldsTab, wDomainsTab;
 private Composite wGeneralComp, wOrganizationComp, wTagsComp, wFieldsComp, wDomainsComp;

 private LabelTextVar wSubDomain, wUsername;
 private Label wlPassword, wlToken;
 private PasswordTextVar wPassword;
 private Button wToken;

 private CCombo wIncomingFieldname;
 private LabelTextVar wOrganizationIdFieldname;
 private LabelTextVar wUrlFieldname;
 private LabelTextVar wExternalIdFieldname;
 private LabelTextVar wNameFieldname;
 private LabelTextVar wCreatedAtFieldname;
 private LabelTextVar wUpdatedAtFieldname;
 private LabelTextVar wDetailsFieldname;
 private LabelTextVar wNotesFieldname;
 private LabelTextVar wGroupIdFieldname;
 private LabelTextVar wSharedTicketsFieldname;
 private LabelTextVar wSharedCommentsFieldname;

 private LabelTextVar wTagFieldname;

 private LabelTextVar wDomainNameFieldname;

 private LabelTextVar wOrgFieldNameFieldname;
 private LabelTextVar wOrgFieldValueFieldname;

 public ZendeskInputOrganizationsDialog( Shell parent, Object in, TransMeta tr, String sname ) {
   super( parent, (BaseStepMeta) in, tr, sname );
   input = (ZendeskInputOrganizationsMeta) in;
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
   shell.setText( BaseMessages.getString( PKG, "ZendeskInputOrganizations.Shell.Title" ) );

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
   wlIncomingFieldname.setText( BaseMessages.getString( PKG, "ZendeskInputOrganizationsDialog.IncomingFieldname.Label" ) );
   wlIncomingFieldname.setToolTipText( BaseMessages.getString( PKG, "ZendeskInputOrganizationsDialog.IncomingFieldname.Tooltip" ) );
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

   // ////////////////////////////
   // START OF ORGANIZATION TAB //
   // ////////////////////////////

   wOrganizationTab = new CTabItem( wTabFolder, SWT.NONE );
   wOrganizationTab.setText( BaseMessages.getString( PKG, "ZendeskInputOrganizationsDialog.OrganizationTab.TabItem" ) );

   wOrganizationComp = new Composite( wTabFolder, SWT.NONE );
   props.setLook( wOrganizationComp );

   FormLayout organizationLayout = new FormLayout();
   organizationLayout.marginWidth = margin;
   organizationLayout.marginHeight = margin;
   wOrganizationComp.setLayout( organizationLayout );

   // organizationIdFieldname
   wOrganizationIdFieldname =
     new LabelTextVar(
       transMeta, wOrganizationComp, BaseMessages.getString( PKG, "ZendeskInputOrganizationsDialog.OrganizationIdFieldname.Label" ),
       BaseMessages.getString( PKG, "ZendeskInputOrganizationsDialog.OrganizationIdFieldname.Tooltip" ) );
   props.setLook( wOrganizationIdFieldname );
   wOrganizationIdFieldname.addModifyListener( lsMod );
   FormData fdOrganizationIdFieldname = new FormData();
   fdOrganizationIdFieldname.left = new FormAttachment( 0, -margin );
   fdOrganizationIdFieldname.top = new FormAttachment( wOrganizationComp, 2 * margin );
   fdOrganizationIdFieldname.right = new FormAttachment( 100, -margin );
   wOrganizationIdFieldname.setLayoutData( fdOrganizationIdFieldname );

   // wUrlFieldname
   wUrlFieldname =
     new LabelTextVar(
       transMeta, wOrganizationComp, BaseMessages.getString( PKG, "ZendeskInputOrganizationsDialog.OrganizationURLFieldname.Label" ),
       BaseMessages.getString( PKG, "ZendeskInputOrganizationsDialog.OrganizationURLFieldname.Tooltip" ) );
   props.setLook( wUrlFieldname );
   wUrlFieldname.addModifyListener( lsMod );
   FormData fdUrlFieldname = new FormData();
   fdUrlFieldname.left = new FormAttachment( 0, -margin );
   fdUrlFieldname.top = new FormAttachment( wOrganizationIdFieldname, 2 * margin );
   fdUrlFieldname.right = new FormAttachment( 100, -margin );
   wUrlFieldname.setLayoutData( fdUrlFieldname );

   // externalIdFieldname
   wExternalIdFieldname =
     new LabelTextVar(
       transMeta, wOrganizationComp, BaseMessages.getString( PKG, "ZendeskInputOrganizationsDialog.ExternalIdFieldname.Label" ),
       BaseMessages.getString( PKG, "ZendeskInputOrganizationsDialog.ExternalIdFieldname.Tooltip" ) );
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
       transMeta, wOrganizationComp, BaseMessages.getString( PKG, "ZendeskInputOrganizationsDialog.NameFieldname.Label" ),
       BaseMessages.getString( PKG, "ZendeskInputOrganizationsDialog.NameFieldname.Tooltip" ) );
   props.setLook( wNameFieldname );
   wNameFieldname.addModifyListener( lsMod );
   FormData fdNameFieldname = new FormData();
   fdNameFieldname.left = new FormAttachment( 0, -margin );
   fdNameFieldname.top = new FormAttachment( wExternalIdFieldname, 2 * margin );
   fdNameFieldname.right = new FormAttachment( 100, -margin );
   wNameFieldname.setLayoutData( fdNameFieldname );

   // detailsFieldname
   wDetailsFieldname =
     new LabelTextVar(
       transMeta, wOrganizationComp, BaseMessages.getString( PKG, "ZendeskInputOrganizationsDialog.DetailsFieldname.Label" ),
       BaseMessages.getString( PKG, "ZendeskInputOrganizationsDialog.DetailsFieldname.Tooltip" ) );
   props.setLook( wDetailsFieldname );
   wDetailsFieldname.addModifyListener( lsMod );
   FormData fdDetailsFieldname = new FormData();
   fdDetailsFieldname.left = new FormAttachment( 0, -margin );
   fdDetailsFieldname.top = new FormAttachment( wNameFieldname, 2 * margin );
   fdDetailsFieldname.right = new FormAttachment( 100, -margin );
   wDetailsFieldname.setLayoutData( fdDetailsFieldname );

   // notesFieldname
   wNotesFieldname =
     new LabelTextVar(
       transMeta, wOrganizationComp, BaseMessages.getString( PKG, "ZendeskInputOrganizationsDialog.NotesFieldname.Label" ),
       BaseMessages.getString( PKG, "ZendeskInputOrganizationsDialog.NotesFieldname.Tooltip" ) );
   props.setLook( wNotesFieldname );
   wNotesFieldname.addModifyListener( lsMod );
   FormData fdNotesFieldname = new FormData();
   fdNotesFieldname.left = new FormAttachment( 0, -margin );
   fdNotesFieldname.top = new FormAttachment( wDetailsFieldname, 2 * margin );
   fdNotesFieldname.right = new FormAttachment( 100, -margin );
   wNotesFieldname.setLayoutData( fdNotesFieldname );

   // groupIdFieldname
   wGroupIdFieldname =
     new LabelTextVar(
       transMeta, wOrganizationComp, BaseMessages.getString( PKG, "ZendeskInputOrganizationsDialog.GroupIdFieldname.Label" ),
       BaseMessages.getString( PKG, "ZendeskInputOrganizationsDialog.GroupIdFieldname.Tooltip" ) );
   props.setLook( wGroupIdFieldname );
   wGroupIdFieldname.addModifyListener( lsMod );
   FormData fdGroupIdFieldname = new FormData();
   fdGroupIdFieldname.left = new FormAttachment( 0, -margin );
   fdGroupIdFieldname.top = new FormAttachment( wNotesFieldname, 2 * margin );
   fdGroupIdFieldname.right = new FormAttachment( 100, -margin );
   wGroupIdFieldname.setLayoutData( fdGroupIdFieldname );

   // sharedTicketsFieldname
   wSharedTicketsFieldname =
     new LabelTextVar(
       transMeta, wOrganizationComp, BaseMessages.getString( PKG, "ZendeskInputOrganizationsDialog.SharedTicketsFieldname.Label" ),
       BaseMessages.getString( PKG, "ZendeskInputOrganizationsDialog.SharedTicketsFieldname.Tooltip" ) );
   props.setLook( wSharedTicketsFieldname );
   wSharedTicketsFieldname.addModifyListener( lsMod );
   FormData fdSharedTicketsFieldname = new FormData();
   fdSharedTicketsFieldname.left = new FormAttachment( 0, -margin );
   fdSharedTicketsFieldname.top = new FormAttachment( wGroupIdFieldname, 2 * margin );
   fdSharedTicketsFieldname.right = new FormAttachment( 100, -margin );
   wSharedTicketsFieldname.setLayoutData( fdSharedTicketsFieldname );

   // sharedCommentsFieldname
   wSharedCommentsFieldname =
     new LabelTextVar(
       transMeta, wOrganizationComp, BaseMessages.getString( PKG, "ZendeskInputOrganizationsDialog.SharedCommentsFieldname.Label" ),
       BaseMessages.getString( PKG, "ZendeskInputOrganizationsDialog.SharedCommentsFieldname.Tooltip" ) );
   props.setLook( wSharedCommentsFieldname );
   wSharedCommentsFieldname.addModifyListener( lsMod );
   FormData fdSharedCommentsFieldname = new FormData();
   fdSharedCommentsFieldname.left = new FormAttachment( 0, -margin );
   fdSharedCommentsFieldname.top = new FormAttachment( wSharedTicketsFieldname, 2 * margin );
   fdSharedCommentsFieldname.right = new FormAttachment( 100, -margin );
   wSharedCommentsFieldname.setLayoutData( fdSharedCommentsFieldname );

   // createdAtFieldname
   wCreatedAtFieldname =
     new LabelTextVar(
       transMeta, wOrganizationComp, BaseMessages.getString( PKG, "ZendeskInputOrganizationsDialog.CreatedAtFieldname.Label" ),
       BaseMessages.getString( PKG, "ZendeskInputOrganizationsDialog.CreatedAtFieldname.Tooltip" ) );
   props.setLook( wCreatedAtFieldname );
   wCreatedAtFieldname.addModifyListener( lsMod );
   FormData fdCreatedAtFieldname = new FormData();
   fdCreatedAtFieldname.left = new FormAttachment( 0, -margin );
   fdCreatedAtFieldname.top = new FormAttachment( wSharedCommentsFieldname, 2 * margin );
   fdCreatedAtFieldname.right = new FormAttachment( 100, -margin );
   wCreatedAtFieldname.setLayoutData( fdCreatedAtFieldname );

   // updatedAtFieldname
   wUpdatedAtFieldname =
     new LabelTextVar(
       transMeta, wOrganizationComp, BaseMessages.getString( PKG, "ZendeskInputOrganizationsDialog.UpdatedAtFieldname.Label" ),
       BaseMessages.getString( PKG, "ZendeskInputOrganizationsDialog.UpdatedAtFieldname.Tooltip" ) );
   props.setLook( wUpdatedAtFieldname );
   wUpdatedAtFieldname.addModifyListener( lsMod );
   FormData fdUpdatedAtFieldname = new FormData();
   fdUpdatedAtFieldname.left = new FormAttachment( 0, -margin );
   fdUpdatedAtFieldname.top = new FormAttachment( wCreatedAtFieldname, 2 * margin );
   fdUpdatedAtFieldname.right = new FormAttachment( 100, -margin );
   wUpdatedAtFieldname.setLayoutData( fdUpdatedAtFieldname );

   FormData fdOrganizationComp = new FormData();
   fdOrganizationComp.left = new FormAttachment( 0, 0 );
   fdOrganizationComp.top = new FormAttachment( 0, 0 );
   fdOrganizationComp.right = new FormAttachment( 100, 0 );
   fdOrganizationComp.bottom = new FormAttachment( 100, 0 );
   wOrganizationComp.setLayoutData( fdOrganizationComp );

   wOrganizationComp.layout();
   wOrganizationTab.setControl( wOrganizationComp );

   // //////////////////////////
   // END OF ORGANIZATION TAB //
   // //////////////////////////

   // ////////////////////
   // START OF TAGS TAB //
   // ////////////////////

   wTagsTab = new CTabItem( wTabFolder, SWT.NONE );
   wTagsTab.setText( BaseMessages.getString( PKG, "ZendeskInputOrganizationsDialog.TagsTab.TabItem" ) );

   wTagsComp = new Composite( wTabFolder, SWT.NONE );
   props.setLook( wTagsComp );

   FormLayout tagsLayout = new FormLayout();
   tagsLayout.marginWidth = margin;
   tagsLayout.marginHeight = margin;
   wTagsComp.setLayout( tagsLayout );

   // tagFieldname
   wTagFieldname =
     new LabelTextVar(
       transMeta, wTagsComp, BaseMessages.getString( PKG, "ZendeskInputOrganizationsDialog.TagFieldname.Label" ),
       BaseMessages.getString( PKG, "ZendeskInputOrganizationsDialog.TagFieldname.Tooltip" ) );
   props.setLook( wTagFieldname );
   wTagFieldname.addModifyListener( lsMod );
   FormData fdTagFieldname = new FormData();
   fdTagFieldname.left = new FormAttachment( 0, -margin );
   fdTagFieldname.top = new FormAttachment( wTagsComp, 2 * margin );
   fdTagFieldname.right = new FormAttachment( 100, -margin );
   wTagFieldname.setLayoutData( fdTagFieldname );

   FormData fdTagsComp = new FormData();
   fdTagsComp.left = new FormAttachment( 0, 0 );
   fdTagsComp.top = new FormAttachment( 0, 0 );
   fdTagsComp.right = new FormAttachment( 100, 0 );
   fdTagsComp.bottom = new FormAttachment( 100, 0 );
   wTagsComp.setLayoutData( fdTagsComp );

   wTagsComp.layout();
   wTagsTab.setControl( wTagsComp );

   // //////////////////
   // END OF TAGS TAB //
   // //////////////////

   // //////////////////////
   // START OF FIELDS TAB //
   // //////////////////////

   wFieldsTab = new CTabItem( wTabFolder, SWT.NONE );
   wFieldsTab.setText( BaseMessages.getString( PKG, "ZendeskInputOrganizationsDialog.FieldsTab.TabItem" ) );

   wFieldsComp = new Composite( wTabFolder, SWT.NONE );
   props.setLook( wFieldsComp );

   FormLayout fieldsLayout = new FormLayout();
   fieldsLayout.marginWidth = margin;
   fieldsLayout.marginHeight = margin;
   wFieldsComp.setLayout( fieldsLayout );

   // orgFieldNameFieldname
   wOrgFieldNameFieldname =
     new LabelTextVar(
       transMeta, wFieldsComp, BaseMessages.getString( PKG, "ZendeskInputOrganizationsDialog.CustomFieldNameFieldname.Label" ),
       BaseMessages.getString( PKG, "ZendeskInputOrganizationsDialog.CustomFieldNameFieldname.Tooltip" ) );
   props.setLook( wOrgFieldNameFieldname );
   wOrgFieldNameFieldname.addModifyListener( lsMod );
   FormData fdOrgFieldNameFieldname = new FormData();
   fdOrgFieldNameFieldname.left = new FormAttachment( 0, -margin );
   fdOrgFieldNameFieldname.top = new FormAttachment( wFieldsComp, 2 * margin );
   fdOrgFieldNameFieldname.right = new FormAttachment( 100, -margin );
   wOrgFieldNameFieldname.setLayoutData( fdOrgFieldNameFieldname );

   // orgFieldValueFieldname
   wOrgFieldValueFieldname =
     new LabelTextVar(
       transMeta, wFieldsComp, BaseMessages.getString( PKG, "ZendeskInputOrganizationsDialog.CustomFieldValueFieldname.Label" ),
       BaseMessages.getString( PKG, "ZendeskInputOrganizationsDialog.CustomFieldValueFieldname.Tooltip" ) );
   props.setLook( wOrgFieldNameFieldname );
   wOrgFieldValueFieldname.addModifyListener( lsMod );
   FormData fdOrgFieldValueFieldname = new FormData();
   fdOrgFieldValueFieldname.left = new FormAttachment( 0, -margin );
   fdOrgFieldValueFieldname.top = new FormAttachment( wOrgFieldNameFieldname, 2 * margin );
   fdOrgFieldValueFieldname.right = new FormAttachment( 100, -margin );
   wOrgFieldValueFieldname.setLayoutData( fdOrgFieldValueFieldname );

   FormData fdFieldsComp = new FormData();
   fdFieldsComp.left = new FormAttachment( 0, 0 );
   fdFieldsComp.top = new FormAttachment( 0, 0 );
   fdFieldsComp.right = new FormAttachment( 100, 0 );
   fdFieldsComp.bottom = new FormAttachment( 100, 0 );
   wFieldsComp.setLayoutData( fdFieldsComp );

   wFieldsComp.layout();
   wFieldsTab.setControl( wFieldsComp );

   // ////////////////////
   // END OF FIELDS TAB //
   // ////////////////////

   // ///////////////////////
   // START OF DOMAINS TAB //
   // ///////////////////////

   wDomainsTab = new CTabItem( wTabFolder, SWT.NONE );
   wDomainsTab.setText( BaseMessages.getString( PKG, "ZendeskInputOrganizationsDialog.DomainsTab.TabItem" ) );

   wDomainsComp = new Composite( wTabFolder, SWT.NONE );
   props.setLook( wDomainsComp );

   FormLayout domainsLayout = new FormLayout();
   domainsLayout.marginWidth = margin;
   domainsLayout.marginHeight = margin;
   wDomainsComp.setLayout( domainsLayout );

   // domainNameFieldname
   wDomainNameFieldname =
     new LabelTextVar(
       transMeta, wDomainsComp, BaseMessages.getString( PKG, "ZendeskInputOrganizationsDialog.DomainFieldname.Label" ),
       BaseMessages.getString( PKG, "ZendeskInputOrganizationsDialog.DomainFieldname.Tooltip" ) );
   props.setLook( wDomainNameFieldname );
   wDomainNameFieldname.addModifyListener( lsMod );
   FormData fdDomainNameFieldname = new FormData();
   fdDomainNameFieldname.left = new FormAttachment( 0, -margin );
   fdDomainNameFieldname.top = new FormAttachment( wDomainsComp, 2 * margin );
   fdDomainNameFieldname.right = new FormAttachment( 100, -margin );
   wDomainNameFieldname.setLayoutData( fdDomainNameFieldname );

   FormData fdDomainsComp = new FormData();
   fdDomainsComp.left = new FormAttachment( 0, 0 );
   fdDomainsComp.top = new FormAttachment( 0, 0 );
   fdDomainsComp.right = new FormAttachment( 100, 0 );
   fdDomainsComp.bottom = new FormAttachment( 100, 0 );
   wDomainsComp.setLayoutData( fdDomainsComp );

   wDomainsComp.layout();
   wDomainsTab.setControl( wDomainsComp );

   // /////////////////////
   // END OF DOMAINS TAB //
   // /////////////////////

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
   wOrganizationIdFieldname.setText( Const.NVL( input.getOrganizationIdFieldname(), "" ) );
   wUrlFieldname.setText( Const.NVL( input.getUrlFieldname(), "" ) );
   wExternalIdFieldname.setText( Const.NVL( input.getExternalIdFieldname(), "" ) );
   wNameFieldname.setText( Const.NVL( input.getNameFieldname(), "" ) );
   wCreatedAtFieldname.setText( Const.NVL( input.getCreatedAtFieldname(), "" ) );
   wUpdatedAtFieldname.setText( Const.NVL( input.getUpdatedAtFieldname(), "" ) );
   wDetailsFieldname.setText( Const.NVL( input.getDetailsFieldname(), "" ) );
   wNotesFieldname.setText( Const.NVL( input.getNotesFieldname(), "" ) );
   wGroupIdFieldname.setText( Const.NVL( input.getGroupIdFieldname(), "" ) );
   wSharedTicketsFieldname.setText( Const.NVL( input.getSharedTicketsFieldname(), "" ) );
   wSharedCommentsFieldname.setText( Const.NVL( input.getSharedCommentsFieldname(), "" ) );
   wTagFieldname.setText( Const.NVL( input.getTagFieldname(), "" ) );
   wOrgFieldNameFieldname.setText( Const.NVL( input.getOrgFieldNameFieldname(), "" ) );
   wOrgFieldValueFieldname.setText( Const.NVL( input.getOrgFieldValueFieldname(), "" ) );
   wDomainNameFieldname.setText( Const.NVL( input.getDomainNameFieldname(), "" ) );

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

 private void getInfo( ZendeskInputOrganizationsMeta inf ) {
   inf.setSubDomain( wSubDomain.getText() );
   inf.setUsername( wUsername.getText() );
   inf.setPassword( wPassword.getText() );
   inf.setToken( wToken.getSelection() );
   if ( isReceivingInput ) {
     inf.setIncomingFieldname( wIncomingFieldname.getText() );
   } else {
     inf.setIncomingFieldname( "" );
   }
   inf.setOrganizationIdFieldname( wOrganizationIdFieldname.getText() );
   inf.setUrlFieldname( wUrlFieldname.getText() );
   inf.setExternalIdFieldname( wExternalIdFieldname.getText() );
   inf.setNameFieldname( wNameFieldname.getText() );
   inf.setCreatedAtFieldname( wCreatedAtFieldname.getText() );
   inf.setUpdatedAtFieldname( wUpdatedAtFieldname.getText() );
   inf.setDetailsFieldname( wDetailsFieldname.getText() );
   inf.setNotesFieldname( wNotesFieldname.getText() );
   inf.setGroupIdFieldname( wGroupIdFieldname.getText() );
   inf.setSharedTicketsFieldname( wSharedTicketsFieldname.getText() );
   inf.setSharedCommentsFieldname( wSharedCommentsFieldname.getText() );
   inf.setTagFieldname( wTagFieldname.getText() );
   inf.setOrgFieldNameFieldname( wOrgFieldNameFieldname.getText() );
   inf.setOrgFieldValueFieldname( wOrgFieldValueFieldname.getText() );
   inf.setDomainNameFieldname( wDomainNameFieldname.getText() );

   stepname = wStepname.getText(); // return value
 }
}
