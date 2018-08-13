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
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.pentaho.di.core.Const;
import org.pentaho.di.core.Props;
import org.pentaho.di.i18n.BaseMessages;
import org.pentaho.di.trans.TransMeta;
import org.pentaho.di.trans.step.BaseStepMeta;
import org.pentaho.di.trans.step.StepDialogInterface;
import org.pentaho.di.trans.steps.zendesk.ZendeskInputTicketFieldsMeta;
import org.pentaho.di.trans.steps.zendesk.ZendeskInputTicketFieldsMeta.TicketField;
import org.pentaho.di.trans.steps.zendesk.ZendeskInputTicketFieldsMeta.TicketField.Attribute;
import org.pentaho.di.ui.core.widget.ColumnInfo;
import org.pentaho.di.ui.core.widget.LabelTextVar;
import org.pentaho.di.ui.core.widget.PasswordTextVar;
import org.pentaho.di.ui.core.widget.TableView;
import org.pentaho.di.ui.trans.step.BaseStepDialog;

public class ZendeskInputTicketFieldsDialog extends BaseStepDialog implements StepDialogInterface {

 private static Class<?> PKG = ZendeskInputTicketFieldsMeta.class; // for i18n purposes, needed by Translator2!!
 private ZendeskInputTicketFieldsMeta input;

 private CTabFolder wTabFolder;
 private CTabItem wGeneralTab, wFieldsTab;
 private Composite wGeneralComp, wFieldsComp;

 private LabelTextVar wSubDomain, wUsername;
 private Label wlPassword, wlToken;
 private PasswordTextVar wPassword;
 private Button wToken;

 private TableView wFields;

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
   // START OF SECTIONS TAB //
   // //////////////////////

   wFieldsTab = new CTabItem( wTabFolder, SWT.NONE );
   wFieldsTab.setText( BaseMessages.getString( PKG, "ZendeskInputTicketFieldsDialog.FieldsTab.TabItem" ) );

   wFieldsComp = new Composite( wTabFolder, SWT.NONE );
   props.setLook( wFieldsComp );

   FormLayout fieldsLayout = new FormLayout();
   fieldsLayout.marginWidth = margin;
   fieldsLayout.marginHeight = margin;
   wFieldsComp.setLayout( fieldsLayout );
   
   wGet = new Button( wFieldsComp, SWT.PUSH );
   wGet.setText( BaseMessages.getString( PKG, "ZendeskInputDialog.GetFields.Button" ) );
   fdGet = new FormData();
   fdGet.left = new FormAttachment( 50, 0 );
   fdGet.bottom = new FormAttachment( 100, 0 );
   wGet.setLayoutData( fdGet );
   
   final int currentRows = input.getTicketFields().length;
   
   ColumnInfo[] colinf =
     new ColumnInfo[] {
       new ColumnInfo(
         BaseMessages.getString( PKG, "ZendeskInputDialog.FieldsTable.Name" ),
         ColumnInfo.COLUMN_TYPE_TEXT, false ),
       new ColumnInfo(
         BaseMessages.getString( PKG, "ZendeskInputDialog.FieldsTable.Element" ),
         ColumnInfo.COLUMN_TYPE_CCOMBO, getElementNames(), false ),
     };
   colinf[0].setUsingVariables( true );
   colinf[0].setToolTip(
     BaseMessages.getString( PKG, "ZendeskInputDialog.FieldsTable.Name.Tooltip" ) );
   colinf[1].setToolTip(
     BaseMessages.getString( PKG, "ZendeskInputDialog.FieldsTable.Element.Tooltip" ) );

   wFields = new TableView( transMeta, wFieldsComp, SWT.FULL_SELECTION | SWT.MULTI, colinf, currentRows, lsMod, props );

   FormData fdFields = new FormData();
   fdFields.left = new FormAttachment( 0, 0 );
   fdFields.top = new FormAttachment( 0, 0 );
   fdFields.right = new FormAttachment( 100, 0 );
   fdFields.bottom = new FormAttachment( wGet, -margin );
   wFields.setLayoutData( fdFields );

   FormData fdFieldsComp = new FormData();
   fdFieldsComp.left = new FormAttachment( 0, 0 );
   fdFieldsComp.top = new FormAttachment( 0, 0 );
   fdFieldsComp.right = new FormAttachment( 100, 0 );
   fdFieldsComp.bottom = new FormAttachment( 100, 0 );
   wFieldsComp.setLayoutData( fdFieldsComp );

   wFieldsComp.layout();
   wFieldsTab.setControl( wFieldsComp );

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
   lsGet = new Listener() {
     public void handleEvent( Event e ) {
       getFields();
     }
   };

   wCancel.addListener( SWT.Selection, lsCancel );
   wOK.addListener( SWT.Selection, lsOK );
   wGet.addListener( SWT.Selection, lsGet );

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
   wFields.optWidth( true );

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

   for ( int i = 0; i < input.getTicketFields().length; i++ ) {
     TableItem item = wFields.table.getItem( i );
     item.setText( 1, Const.NVL( input.getTicketFields()[i].getName(), "") );
     item.setText( 2, Const.NVL( input.getTicketFields()[i].getType().toString(), "" ) );
   }
   wFields.removeEmptyRows();
   wFields.setRowNums();

   wStepname.selectAll();
   wStepname.setFocus();
 }

  public void getFields() {
    int clearFields = SWT.NO;
    int nrInputFields = wFields.nrNonEmpty();
    if ( nrInputFields > 0 ) {
      MessageBox mb = new MessageBox( shell, SWT.YES | SWT.NO | SWT.ICON_QUESTION );
      mb.setMessage( BaseMessages.getString( PKG, "ZendeskInputDialog.ClearFieldList.DialogMessage" ) );
      mb.setText( BaseMessages.getString( PKG, "ZendeskInputDialog.ClearFieldList.DialogTitle" ) );
      clearFields = mb.open();
    }

    if ( clearFields == SWT.YES ) {
      // Clear Fields Grid
      wFields.table.removeAll();
    }

    for ( Attribute value : Attribute.values() ) {
      TableItem item = new TableItem( wFields.table, SWT.NONE );
      item.setText( 1, value.toString() );
      item.setText( 2, value.toString() );
    }

    wFields.removeEmptyRows();
    wFields.setRowNums();
    wFields.optWidth( true );
    input.setChanged();
  }

  private String[] getElementNames() {
    Attribute[] values = Attribute.values();
    String[] valueNames = new String[values.length];
    for ( int i = 0; i < values.length; i++ ) {
      valueNames[i] = values[i].toString();
    }
    return valueNames;
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

    int nrFields = wFields.nrNonEmpty();
    List<TicketField> fields = new ArrayList<>();
    for ( int i = 0; i < nrFields; i++ ) {
      TableItem item = wFields.getNonEmpty( i );
      String colName = item.getText( 1 );
      Attribute colType = Attribute.getEnumFromValue( item.getText( 2 ) );
      if ( colType != null && !Const.isEmpty( colName ) ) {
        fields.add( new TicketField( colName, colType ) );
      }
    }
    inf.setTicketFields( fields.toArray( new TicketField[]{} ) );

    stepname = wStepname.getText(); // return value
  }
}
