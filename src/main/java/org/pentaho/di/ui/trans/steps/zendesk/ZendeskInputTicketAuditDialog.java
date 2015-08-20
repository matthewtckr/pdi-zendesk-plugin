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
import org.pentaho.di.trans.steps.zendesk.ZendeskInputTicketAuditMeta;
import org.pentaho.di.ui.core.dialog.ErrorDialog;
import org.pentaho.di.ui.core.widget.LabelTextVar;
import org.pentaho.di.ui.core.widget.PasswordTextVar;
import org.pentaho.di.ui.trans.step.BaseStepDialog;

public class ZendeskInputTicketAuditDialog extends BaseStepDialog implements StepDialogInterface {

 private static Class<?> PKG = ZendeskInputTicketAuditMeta.class; // for i18n purposes, needed by Translator2!!
 private ZendeskInputTicketAuditMeta input;

 private LabelTextVar wSubDomain, wUsername;
 private Label wlPassword, wlToken;
 private PasswordTextVar wPassword;
 private Button wToken;
 private FormData fdSubDomain, fdUsername, fdPassword, fdlToken, fdToken;

 private Label wlTicketIdFieldname;
 private CCombo wTicketIdFieldname;

 private LabelTextVar wAuditIdFieldname;
 private LabelTextVar wCreatedTimeFieldname;
 private LabelTextVar wOrganizationIdFieldname;
 private LabelTextVar wRequesterIdFieldname;
 private LabelTextVar wAssigneeIdFieldname;
 private LabelTextVar wGroupIdFieldname;
 private LabelTextVar wSubjectFieldname;
 private LabelTextVar wTagsFieldname;
 private LabelTextVar wStatusFieldname;
 private LabelTextVar wPriorityFieldname;
 private LabelTextVar wChannelFieldname;
 private LabelTextVar wTypeFieldname;
 private LabelTextVar wSatisfactionFieldname;

 private LabelTextVar wCommentIdFieldname;
 private LabelTextVar wAuthorIdFieldname;
 private LabelTextVar wPublicCommentFieldname;
 private LabelTextVar wCommentBodyFieldname;
 private LabelTextVar wCommentHTMLBodyFieldname;
 private LabelTextVar wChangedToPrivateFieldname;

 private LabelTextVar wCustomFieldFieldname;
 private LabelTextVar wCustomFieldValueFieldname;

 private FormData fdlTicketIdFieldname;
 private FormData fdTicketIdFieldname;
 private FormData fdAuditIdFieldname;
 private FormData fdCreatedTimeFieldname;
 private FormData fdOrganizationIdFieldname;
 private FormData fdRequesterIdFieldname;
 private FormData fdAssigneeIdFieldname;
 private FormData fdGroupIdFieldname;
 private FormData fdSubjectFieldname;
 private FormData fdTagsFieldname;
 private FormData fdStatusFieldname;
 private FormData fdPriorityFieldname;
 private FormData fdChannelFieldname;
 private FormData fdTypeFieldname;
 private FormData fdSatisfactionFieldname;

 private FormData fdCommentIdFieldname;
 private FormData fdAuthorIdFieldname;
 private FormData fdPublicCommentFieldname;
 private FormData fdCommentBodyFieldname;
 private FormData fdCommentHTMLBodyFieldname;
 private FormData fdChangedToPrivateFieldname;

 private FormData fdCustomFieldFieldname;
 private FormData fdCustomFieldValueFieldname;
 public ZendeskInputTicketAuditDialog( Shell parent, Object in, TransMeta tr, String sname ) {
   super( parent, (BaseStepMeta) in, tr, sname );
   input = (ZendeskInputTicketAuditMeta) in;
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
   shell.setText( BaseMessages.getString( PKG, "ZendeskInputTicketAudit.Shell.Title" ) );

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

   // TicketIdFieldname
   wlTicketIdFieldname = new Label( shell, SWT.RIGHT );
   wlTicketIdFieldname.setText( BaseMessages.getString( PKG, "ZendeskInputTicketAuditDialog.TicketIdFieldname.Label" ) );
   props.setLook( wlTicketIdFieldname );
   fdlTicketIdFieldname = new FormData();
   fdlTicketIdFieldname.left = new FormAttachment( 0, 0 );
   fdlTicketIdFieldname.top = new FormAttachment( wToken, 2 * margin );
   fdlTicketIdFieldname.right = new FormAttachment( middle, -margin );
   wlTicketIdFieldname.setLayoutData( fdlTicketIdFieldname );

   RowMetaInterface previousFields;
   try {
     previousFields = transMeta.getPrevStepFields( stepMeta );
   } catch ( KettleStepException e ) {
     new ErrorDialog( shell,
       BaseMessages.getString( PKG, "ZendeskInputDialog.ErrorDialog.UnableToGetInputFields.Title" ),
       BaseMessages.getString( PKG, "ZendeskInputDialog.ErrorDialog.UnableToGetInputFields.Message" ), e );
     previousFields = new RowMeta();
   }

   wTicketIdFieldname = new CCombo( shell, SWT.BORDER | SWT.READ_ONLY );
   props.setLook( wTicketIdFieldname );
   wTicketIdFieldname.addModifyListener( lsMod );
   fdTicketIdFieldname = new FormData();
   fdTicketIdFieldname.left = new FormAttachment( middle, 0 );
   fdTicketIdFieldname.top = new FormAttachment( wToken, margin );
   fdTicketIdFieldname.right = new FormAttachment( 100, -margin );
   wTicketIdFieldname.setLayoutData( fdTicketIdFieldname );
   wTicketIdFieldname.setItems( previousFields.getFieldNames() );
   wTicketIdFieldname.addSelectionListener( new SelectionAdapter() {
     public void widgetSelected( SelectionEvent e ) {
       input.setChanged();
     }
   } );
   
   // auditIdFieldname
   wAuditIdFieldname =
     new LabelTextVar(
       transMeta, shell, BaseMessages.getString( PKG, "ZendeskInputTicketAuditDialog.AuditIdFieldname.Label" ),
       BaseMessages.getString( PKG, "ZendeskInputTicketAuditDialog.AuditIdFieldname.Tooltip" ) );
   props.setLook( wAuditIdFieldname );
   wAuditIdFieldname.addModifyListener( lsMod );
   fdAuditIdFieldname = new FormData();
   fdAuditIdFieldname.left = new FormAttachment( 0, -margin );
   fdAuditIdFieldname.top = new FormAttachment( wTicketIdFieldname, 2 * margin );
   fdAuditIdFieldname.right = new FormAttachment( 100, -margin );
   wAuditIdFieldname.setLayoutData( fdAuditIdFieldname );

   // wCreatedTimeFieldname
   wCreatedTimeFieldname =
     new LabelTextVar(
       transMeta, shell, BaseMessages.getString( PKG, "ZendeskInputTicketAuditDialog.CreatedTimeFieldname.Label" ),
       BaseMessages.getString( PKG, "ZendeskInputTicketAuditDialog.CreatedTimeFieldname.Tooltip" ) );
   props.setLook( wCreatedTimeFieldname );
   wCreatedTimeFieldname.addModifyListener( lsMod );
   fdCreatedTimeFieldname = new FormData();
   fdCreatedTimeFieldname.left = new FormAttachment( 0, -margin );
   fdCreatedTimeFieldname.top = new FormAttachment( wAuditIdFieldname, 2 * margin );
   fdCreatedTimeFieldname.right = new FormAttachment( 100, -margin );
   wCreatedTimeFieldname.setLayoutData( fdCreatedTimeFieldname );

   // wOrganizationIdFieldname
   wOrganizationIdFieldname =
     new LabelTextVar(
       transMeta, shell, BaseMessages.getString( PKG, "ZendeskInputTicketAuditDialog.OrganizationIdFieldname.Label" ),
       BaseMessages.getString( PKG, "ZendeskInputTicketAuditDialog.OrganizationIdFieldname.Tooltip" ) );
   props.setLook( wOrganizationIdFieldname );
   wOrganizationIdFieldname.addModifyListener( lsMod );
   fdOrganizationIdFieldname = new FormData();
   fdOrganizationIdFieldname.left = new FormAttachment( 0, -margin );
   fdOrganizationIdFieldname.top = new FormAttachment( wCreatedTimeFieldname, 2 * margin );
   fdOrganizationIdFieldname.right = new FormAttachment( 100, -margin );
   wOrganizationIdFieldname.setLayoutData( fdOrganizationIdFieldname );

   // wRequesterIdFieldname
   wRequesterIdFieldname =
     new LabelTextVar(
       transMeta, shell, BaseMessages.getString( PKG, "ZendeskInputTicketAuditDialog.RequesterIdFieldname.Label" ),
       BaseMessages.getString( PKG, "ZendeskInputTicketAuditDialog.RequesterIdFieldname.Tooltip" ) );
   props.setLook( wRequesterIdFieldname );
   wRequesterIdFieldname.addModifyListener( lsMod );
   fdRequesterIdFieldname = new FormData();
   fdRequesterIdFieldname.left = new FormAttachment( 0, -margin );
   fdRequesterIdFieldname.top = new FormAttachment( wOrganizationIdFieldname, 2 * margin );
   fdRequesterIdFieldname.right = new FormAttachment( 100, -margin );
   wRequesterIdFieldname.setLayoutData( fdRequesterIdFieldname );

   // wAssigneeIdFieldname
   wAssigneeIdFieldname =
     new LabelTextVar(
       transMeta, shell, BaseMessages.getString( PKG, "ZendeskInputTicketAuditDialog.AssigneeIdFieldname.Label" ),
       BaseMessages.getString( PKG, "ZendeskInputTicketAuditDialog.AssigneeIdFieldname.Tooltip" ) );
   props.setLook( wAssigneeIdFieldname );
   wAssigneeIdFieldname.addModifyListener( lsMod );
   fdAssigneeIdFieldname = new FormData();
   fdAssigneeIdFieldname.left = new FormAttachment( 0, -margin );
   fdAssigneeIdFieldname.top = new FormAttachment( wRequesterIdFieldname, 2 * margin );
   fdAssigneeIdFieldname.right = new FormAttachment( 100, -margin );
   wAssigneeIdFieldname.setLayoutData( fdAssigneeIdFieldname );

   // wGroupIdFieldname
   wGroupIdFieldname =
     new LabelTextVar(
       transMeta, shell, BaseMessages.getString( PKG, "ZendeskInputTicketAuditDialog.GroupIdFieldname.Label" ),
       BaseMessages.getString( PKG, "ZendeskInputTicketAuditDialog.GroupIdFieldname.Tooltip" ) );
   props.setLook( wGroupIdFieldname );
   wGroupIdFieldname.addModifyListener( lsMod );
   fdGroupIdFieldname = new FormData();
   fdGroupIdFieldname.left = new FormAttachment( 0, -margin );
   fdGroupIdFieldname.top = new FormAttachment( wAssigneeIdFieldname, 2 * margin );
   fdGroupIdFieldname.right = new FormAttachment( 100, -margin );
   wGroupIdFieldname.setLayoutData( fdGroupIdFieldname );

   // wSubjectFieldname
   wSubjectFieldname =
     new LabelTextVar(
       transMeta, shell, BaseMessages.getString( PKG, "ZendeskInputTicketAuditDialog.SubjectFieldname.Label" ),
       BaseMessages.getString( PKG, "ZendeskInputTicketAuditDialog.SubjectFieldname.Tooltip" ) );
   props.setLook( wSubjectFieldname );
   wSubjectFieldname.addModifyListener( lsMod );
   fdSubjectFieldname = new FormData();
   fdSubjectFieldname.left = new FormAttachment( 0, -margin );
   fdSubjectFieldname.top = new FormAttachment( wGroupIdFieldname, 2 * margin );
   fdSubjectFieldname.right = new FormAttachment( 100, -margin );
   wSubjectFieldname.setLayoutData( fdSubjectFieldname );

   // wTagsFieldname
   wTagsFieldname =
     new LabelTextVar(
       transMeta, shell, BaseMessages.getString( PKG, "ZendeskInputTicketAuditDialog.TagsFieldname.Label" ),
       BaseMessages.getString( PKG, "ZendeskInputTicketAuditDialog.TagsFieldname.Tooltip" ) );
   props.setLook( wTagsFieldname );
   wTagsFieldname.addModifyListener( lsMod );
   fdTagsFieldname = new FormData();
   fdTagsFieldname.left = new FormAttachment( 0, -margin );
   fdTagsFieldname.top = new FormAttachment( wSubjectFieldname, 2 * margin );
   fdTagsFieldname.right = new FormAttachment( 100, -margin );
   wTagsFieldname.setLayoutData( fdTagsFieldname );

   // wStatusFieldname
   wStatusFieldname =
     new LabelTextVar(
       transMeta, shell, BaseMessages.getString( PKG, "ZendeskInputTicketAuditDialog.StatusFieldname.Label" ),
       BaseMessages.getString( PKG, "ZendeskInputTicketAuditDialog.StatusFieldname.Tooltip" ) );
   props.setLook( wStatusFieldname );
   wStatusFieldname.addModifyListener( lsMod );
   fdStatusFieldname = new FormData();
   fdStatusFieldname.left = new FormAttachment( 0, -margin );
   fdStatusFieldname.top = new FormAttachment( wTagsFieldname, 2 * margin );
   fdStatusFieldname.right = new FormAttachment( 100, -margin );
   wStatusFieldname.setLayoutData( fdStatusFieldname );

   // wPriorityFieldname
   wPriorityFieldname =
     new LabelTextVar(
       transMeta, shell, BaseMessages.getString( PKG, "ZendeskInputTicketAuditDialog.PriorityFieldname.Label" ),
       BaseMessages.getString( PKG, "ZendeskInputTicketAuditDialog.PriorityFieldname.Tooltip" ) );
   props.setLook( wPriorityFieldname );
   wPriorityFieldname.addModifyListener( lsMod );
   fdPriorityFieldname = new FormData();
   fdPriorityFieldname.left = new FormAttachment( 0, -margin );
   fdPriorityFieldname.top = new FormAttachment( wStatusFieldname, 2 * margin );
   fdPriorityFieldname.right = new FormAttachment( 100, -margin );
   wPriorityFieldname.setLayoutData( fdPriorityFieldname );

   // wChannelFieldname
   wChannelFieldname =
     new LabelTextVar(
       transMeta, shell, BaseMessages.getString( PKG, "ZendeskInputTicketAuditDialog.ChannelFieldname.Label" ),
       BaseMessages.getString( PKG, "ZendeskInputTicketAuditDialog.ChannelFieldname.Tooltip" ) );
   props.setLook( wChannelFieldname );
   wChannelFieldname.addModifyListener( lsMod );
   fdChannelFieldname = new FormData();
   fdChannelFieldname.left = new FormAttachment( 0, -margin );
   fdChannelFieldname.top = new FormAttachment( wPriorityFieldname, 2 * margin );
   fdChannelFieldname.right = new FormAttachment( 100, -margin );
   wChannelFieldname.setLayoutData( fdChannelFieldname );

   // wTypeFieldname
   wTypeFieldname =
     new LabelTextVar(
       transMeta, shell, BaseMessages.getString( PKG, "ZendeskInputTicketAuditDialog.TypeFieldname.Label" ),
       BaseMessages.getString( PKG, "ZendeskInputTicketAuditDialog.TypeFieldname.Tooltip" ) );
   props.setLook( wTypeFieldname );
   wTypeFieldname.addModifyListener( lsMod );
   fdTypeFieldname = new FormData();
   fdTypeFieldname.left = new FormAttachment( 0, -margin );
   fdTypeFieldname.top = new FormAttachment( wChannelFieldname, 2 * margin );
   fdTypeFieldname.right = new FormAttachment( 100, -margin );
   wTypeFieldname.setLayoutData( fdTypeFieldname );

   // wSatisfactionFieldname
   wSatisfactionFieldname =
     new LabelTextVar(
       transMeta, shell, BaseMessages.getString( PKG, "ZendeskInputTicketAuditDialog.SatisfactionFieldname.Label" ),
       BaseMessages.getString( PKG, "ZendeskInputTicketAuditDialog.SatisfactionFieldname.Tooltip" ) );
   props.setLook( wSatisfactionFieldname );
   wSatisfactionFieldname.addModifyListener( lsMod );
   fdSatisfactionFieldname = new FormData();
   fdSatisfactionFieldname.left = new FormAttachment( 0, -margin );
   fdSatisfactionFieldname.top = new FormAttachment( wTypeFieldname, 2 * margin );
   fdSatisfactionFieldname.right = new FormAttachment( 100, -margin );
   wSatisfactionFieldname.setLayoutData( fdSatisfactionFieldname );

   // wCommentIdFieldname
   wCommentIdFieldname =
     new LabelTextVar(
       transMeta, shell, BaseMessages.getString( PKG, "ZendeskInputTicketAuditDialog.CommentIdFieldname.Label" ),
       BaseMessages.getString( PKG, "ZendeskInputTicketAuditDialog.CommentIdFieldname.Tooltip" ) );
   props.setLook( wCommentIdFieldname );
   wCommentIdFieldname.addModifyListener( lsMod );
   fdCommentIdFieldname = new FormData();
   fdCommentIdFieldname.left = new FormAttachment( 0, -margin );
   fdCommentIdFieldname.top = new FormAttachment( wSatisfactionFieldname, 2 * margin );
   fdCommentIdFieldname.right = new FormAttachment( 100, -margin );
   wCommentIdFieldname.setLayoutData( fdCommentIdFieldname );

   // wAuthorIdFieldname
   wAuthorIdFieldname =
     new LabelTextVar(
       transMeta, shell, BaseMessages.getString( PKG, "ZendeskInputTicketAuditDialog.AuthorIdFieldname.Label" ),
       BaseMessages.getString( PKG, "ZendeskInputTicketAuditDialog.AuthorIdFieldname.Tooltip" ) );
   props.setLook( wAuthorIdFieldname );
   wAuthorIdFieldname.addModifyListener( lsMod );
   fdAuthorIdFieldname = new FormData();
   fdAuthorIdFieldname.left = new FormAttachment( 0, -margin );
   fdAuthorIdFieldname.top = new FormAttachment( wCommentIdFieldname, 2 * margin );
   fdAuthorIdFieldname.right = new FormAttachment( 100, -margin );
   wAuthorIdFieldname.setLayoutData( fdAuthorIdFieldname );

   // wPublicCommentFieldname
   wPublicCommentFieldname =
     new LabelTextVar(
       transMeta, shell, BaseMessages.getString( PKG, "ZendeskInputTicketAuditDialog.PublicCommentFieldname.Label" ),
       BaseMessages.getString( PKG, "ZendeskInputTicketAuditDialog.PublicCommentFieldname.Tooltip" ) );
   props.setLook( wPublicCommentFieldname );
   wPublicCommentFieldname.addModifyListener( lsMod );
   fdPublicCommentFieldname = new FormData();
   fdPublicCommentFieldname.left = new FormAttachment( 0, -margin );
   fdPublicCommentFieldname.top = new FormAttachment( wAuthorIdFieldname, 2 * margin );
   fdPublicCommentFieldname.right = new FormAttachment( 100, -margin );
   wPublicCommentFieldname.setLayoutData( fdPublicCommentFieldname );

   // wCommentBodyFieldname
   wCommentBodyFieldname =
     new LabelTextVar(
       transMeta, shell, BaseMessages.getString( PKG, "ZendeskInputTicketAuditDialog.CommentBodyFieldname.Label" ),
       BaseMessages.getString( PKG, "ZendeskInputTicketAuditDialog.CommentBodyFieldname.Tooltip" ) );
   props.setLook( wCommentBodyFieldname );
   wCommentBodyFieldname.addModifyListener( lsMod );
   fdCommentBodyFieldname = new FormData();
   fdCommentBodyFieldname.left = new FormAttachment( 0, -margin );
   fdCommentBodyFieldname.top = new FormAttachment( wPublicCommentFieldname, 2 * margin );
   fdCommentBodyFieldname.right = new FormAttachment( 100, -margin );
   wCommentBodyFieldname.setLayoutData( fdCommentBodyFieldname );

   // wCommentHTMLBodyFieldname
   wCommentHTMLBodyFieldname =
     new LabelTextVar(
       transMeta, shell, BaseMessages.getString( PKG, "ZendeskInputTicketAuditDialog.CommentHTMLBodyFieldname.Label" ),
       BaseMessages.getString( PKG, "ZendeskInputTicketAuditDialog.CommentHTMLBodyFieldname.Tooltip" ) );
   props.setLook( wCommentHTMLBodyFieldname );
   wCommentHTMLBodyFieldname.addModifyListener( lsMod );
   fdCommentHTMLBodyFieldname = new FormData();
   fdCommentHTMLBodyFieldname.left = new FormAttachment( 0, -margin );
   fdCommentHTMLBodyFieldname.top = new FormAttachment( wCommentBodyFieldname, 2 * margin );
   fdCommentHTMLBodyFieldname.right = new FormAttachment( 100, -margin );
   wCommentHTMLBodyFieldname.setLayoutData( fdCommentHTMLBodyFieldname );

   // wChangedToPrivateFieldname
   wChangedToPrivateFieldname =
     new LabelTextVar(
       transMeta, shell, BaseMessages.getString( PKG, "ZendeskInputTicketAuditDialog.ChangedToPrivateFieldname.Label" ),
       BaseMessages.getString( PKG, "ZendeskInputTicketAuditDialog.ChangedToPrivateFieldname.Tooltip" ) );
   props.setLook( wChangedToPrivateFieldname );
   wChangedToPrivateFieldname.addModifyListener( lsMod );
   fdChangedToPrivateFieldname = new FormData();
   fdChangedToPrivateFieldname.left = new FormAttachment( 0, -margin );
   fdChangedToPrivateFieldname.top = new FormAttachment( wCommentHTMLBodyFieldname, 2 * margin );
   fdChangedToPrivateFieldname.right = new FormAttachment( 100, -margin );
   wChangedToPrivateFieldname.setLayoutData( fdChangedToPrivateFieldname );

   // wCustomFieldFieldname
   wCustomFieldFieldname =
     new LabelTextVar(
       transMeta, shell, BaseMessages.getString( PKG, "ZendeskInputTicketAuditDialog.CustomFieldFieldname.Label" ),
       BaseMessages.getString( PKG, "ZendeskInputTicketAuditDialog.CustomFieldFieldname.Tooltip" ) );
   props.setLook( wCustomFieldFieldname );
   wCustomFieldFieldname.addModifyListener( lsMod );
   fdCustomFieldFieldname = new FormData();
   fdCustomFieldFieldname.left = new FormAttachment( 0, -margin );
   fdCustomFieldFieldname.top = new FormAttachment( wChangedToPrivateFieldname, 2 * margin );
   fdCustomFieldFieldname.right = new FormAttachment( 100, -margin );
   wCustomFieldFieldname.setLayoutData( fdCustomFieldFieldname );

   // wCustomFieldValueFieldname
   wCustomFieldValueFieldname =
     new LabelTextVar(
       transMeta, shell, BaseMessages.getString( PKG, "ZendeskInputTicketAuditDialog.CustomFieldValueFieldname.Label" ),
       BaseMessages.getString( PKG, "ZendeskInputTicketAuditDialog.CustomFieldValueFieldname.Tooltip" ) );
   props.setLook( wCustomFieldValueFieldname );
   wCustomFieldValueFieldname.addModifyListener( lsMod );
   fdCustomFieldValueFieldname = new FormData();
   fdCustomFieldValueFieldname.left = new FormAttachment( 0, -margin );
   fdCustomFieldValueFieldname.top = new FormAttachment( wCustomFieldFieldname, 2 * margin );
   fdCustomFieldValueFieldname.right = new FormAttachment( 100, -margin );
   wCustomFieldValueFieldname.setLayoutData( fdCustomFieldValueFieldname );

   // Some buttons
   wOK = new Button( shell, SWT.PUSH );
   wOK.setText( BaseMessages.getString( PKG, "System.Button.OK" ) );
   wCancel = new Button( shell, SWT.PUSH );
   wCancel.setText( BaseMessages.getString( PKG, "System.Button.Cancel" ) );

   setButtonPositions( new Button[] { wOK, wCancel }, margin, wCustomFieldValueFieldname );

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
   wTicketIdFieldname.setText( Const.NVL( input.getTicketIdFieldname(), "" ) );
   wAuditIdFieldname.setText( Const.NVL( input.getAuditIdFieldname(), "" ) );
   wCreatedTimeFieldname.setText( Const.NVL( input.getCreatedTimeFieldname(), "" ) );
   wOrganizationIdFieldname.setText( Const.NVL( input.getOrganizationIdFieldname(), "" )  );
   wRequesterIdFieldname.setText( Const.NVL( input.getRequesterIdFieldname(), "" ) );
   wAssigneeIdFieldname.setText( Const.NVL( input.getAssigneeIdFieldname(), "" ) );
   wGroupIdFieldname.setText( Const.NVL( input.getGroupIdFieldname(), "" ) );
   wSubjectFieldname.setText( Const.NVL( input.getSubjectFieldname(), "" ) );
   wTagsFieldname.setText( Const.NVL( input.getTagsFieldname(), "" ) );
   wStatusFieldname.setText( Const.NVL( input.getStatusFieldname(), "" ) );
   wPriorityFieldname.setText( Const.NVL( input.getPriorityFieldname(), "" ) );
   wChannelFieldname.setText( Const.NVL( input.getChannelFieldname(), "" ) );
   wTypeFieldname.setText( Const.NVL( input.getTypeFieldname(), "" ) );
   wSatisfactionFieldname.setText( Const.NVL( input.getSatisfactionFieldname(), "" ) );
   wCommentIdFieldname.setText( Const.NVL( input.getCommentIdFieldname(), "" ) );
   wAuthorIdFieldname.setText( Const.NVL( input.getAuthorIdFieldname(), "" ) );
   wPublicCommentFieldname.setText( Const.NVL( input.getPublicCommentFieldname(), "" ) );
   wCommentBodyFieldname.setText( Const.NVL( input.getCommentBodyFieldname(), "" ) );
   wCommentHTMLBodyFieldname.setText( Const.NVL( input.getCommentHTMLBodyFieldname(), "" ) );
   wChangedToPrivateFieldname.setText( Const.NVL( input.getChangedToPrivateFieldname(), "" ) );
   wCustomFieldFieldname.setText( Const.NVL( input.getCustomFieldFieldname(), "" ) );
   wCustomFieldValueFieldname.setText( Const.NVL( input.getCustomFieldValueFieldname(), "" ) );

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

 private void getInfo( ZendeskInputTicketAuditMeta inf ) {
   inf.setSubDomain( wSubDomain.getText() );
   inf.setUsername( wUsername.getText() );
   inf.setPassword( wPassword.getText() );
   inf.setToken( wToken.getSelection() );
   inf.setTicketIdFieldname( wTicketIdFieldname.getText() );
   inf.setAuditIdFieldname( wAuditIdFieldname.getText() );
   inf.setCreatedTimeFieldname( wCreatedTimeFieldname.getText() );
   inf.setOrganizationIdFieldname( wOrganizationIdFieldname.getText() );
   inf.setRequesterIdFieldname( wRequesterIdFieldname.getText() );
   inf.setAssigneeIdFieldname( wAssigneeIdFieldname.getText() );
   inf.setGroupIdFieldname( wGroupIdFieldname.getText() );
   inf.setSubjectFieldname( wSubjectFieldname.getText() );
   inf.setTagsFieldname( wTagsFieldname.getText() );
   inf.setStatusFieldname( wStatusFieldname.getText() );
   inf.setPriorityFieldname( wPriorityFieldname.getText() );
   inf.setChannelFieldname( wChannelFieldname.getText() );
   inf.setTypeFieldname( wTypeFieldname.getText() );
   inf.setSatisfactionFieldname( wSatisfactionFieldname.getText() );
   inf.setCommentIdFieldname( wCommentIdFieldname.getText() );
   inf.setAuthorIdFieldname( wAuthorIdFieldname.getText() );
   inf.setPublicCommentFieldname( wPublicCommentFieldname.getText() );
   inf.setCommentBodyFieldname( wCommentBodyFieldname.getText() );
   inf.setCommentHTMLBodyFieldname( wCommentHTMLBodyFieldname.getText() );
   inf.setChangedToPrivateFieldname( wChangedToPrivateFieldname.getText() );
   inf.setCustomFieldFieldname( wCustomFieldFieldname.getText() );
   inf.setCustomFieldValueFieldname( wCustomFieldValueFieldname.getText() );

   stepname = wStepname.getText(); // return value
 }
}
