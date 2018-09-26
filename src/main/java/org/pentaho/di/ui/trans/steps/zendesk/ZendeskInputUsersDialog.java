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
import org.pentaho.di.core.exception.KettleStepException;
import org.pentaho.di.core.row.RowMeta;
import org.pentaho.di.core.row.RowMetaInterface;
import org.pentaho.di.i18n.BaseMessages;
import org.pentaho.di.trans.TransMeta;
import org.pentaho.di.trans.step.BaseStepMeta;
import org.pentaho.di.trans.step.StepDialogInterface;
import org.pentaho.di.trans.steps.zendesk.ZendeskInputUsersMeta;
import org.pentaho.di.trans.steps.zendesk.ZendeskInputUsersMeta.IdentityField;
import org.pentaho.di.trans.steps.zendesk.ZendeskInputUsersMeta.UserField;
import org.pentaho.di.ui.core.dialog.ErrorDialog;
import org.pentaho.di.ui.core.widget.ColumnInfo;
import org.pentaho.di.ui.core.widget.LabelTextVar;
import org.pentaho.di.ui.core.widget.PasswordTextVar;
import org.pentaho.di.ui.core.widget.TableView;
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
 
  private TableView wUserFields, wIdentityFields;
  private Button wUserGet, wIdentityGet;

  private CCombo wIncomingFieldname;

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

    wUserGet = new Button( wUserComp, SWT.PUSH );
    wUserGet.setText( BaseMessages.getString( PKG, "ZendeskInputDialog.GetFields.Button" ) );
    FormData fdUserGet = new FormData();
    fdUserGet.left = new FormAttachment( 50, 0 );
    fdUserGet.bottom = new FormAttachment( 100, 0 );
    wUserGet.setLayoutData( fdUserGet );

    final int currentUserRows = input.getUserFields().length;

    ColumnInfo[] userColInfo =
      new ColumnInfo[] {
        new ColumnInfo(
          BaseMessages.getString( PKG, "ZendeskInputDialog.FieldsTable.Name" ),
          ColumnInfo.COLUMN_TYPE_TEXT, false ),
        new ColumnInfo(
          BaseMessages.getString( PKG, "ZendeskInputDialog.FieldsTable.Element" ),
          ColumnInfo.COLUMN_TYPE_CCOMBO, getUserElementNames(), false ),
      };
    userColInfo[0].setUsingVariables( true );
    userColInfo[0].setToolTip( BaseMessages.getString( PKG, "ZendeskInputDialog.FieldsTable.Name.Tooltip" ) );
    userColInfo[1].setToolTip( BaseMessages.getString( PKG, "ZendeskInputDialog.FieldsTable.Element.Tooltip" ) );

    wUserFields = new TableView( transMeta, wUserComp, SWT.FULL_SELECTION | SWT.MULTI, userColInfo, currentUserRows, lsMod, props );

    FormData fdUserFields = new FormData();
    fdUserFields.left = new FormAttachment( 0, 0 );
    fdUserFields.top = new FormAttachment( 0, 0 );
    fdUserFields.right = new FormAttachment( 100, 0 );
    fdUserFields.bottom = new FormAttachment( wUserGet, -margin );
    wUserFields.setLayoutData( fdUserFields );

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

    wIdentityGet = new Button ( wIdentityComp, SWT.PUSH );
    wIdentityGet.setText( BaseMessages.getString( PKG, "ZendeskInputDialog.GetFields.Button" ) );
    FormData fdIdentityGet = new FormData();
    fdIdentityGet.left = new FormAttachment( 50, 0 );
    fdIdentityGet.bottom = new FormAttachment( 100, 0 );
    wIdentityGet.setLayoutData( fdIdentityGet );

    final int currentIdentityRows = input.getIdentityFields().length;

    ColumnInfo[] identityColInfo =
      new ColumnInfo[] {
        new ColumnInfo(
          BaseMessages.getString( PKG, "ZendeskInputDialog.FieldsTable.Name" ),
          ColumnInfo.COLUMN_TYPE_TEXT, false ),
        new ColumnInfo(
          BaseMessages.getString( PKG, "ZendeskInputDialog.FieldsTable.Element" ),
          ColumnInfo.COLUMN_TYPE_CCOMBO, getIdentityElementNames(), false ),
      };
    identityColInfo[0].setUsingVariables( true );
    identityColInfo[0].setToolTip( BaseMessages.getString( PKG, "ZendeskInputDialog.FieldsTable.Name.Tooltip" ) );
    identityColInfo[1].setToolTip( BaseMessages.getString( PKG, "ZendeskInputDialog.FieldsTable.Element.Tooltip" ) );

    wIdentityFields = new TableView( transMeta, wIdentityComp, SWT.FULL_SELECTION | SWT.MULTI, identityColInfo, currentIdentityRows, lsMod, props );

    FormData fdIdentityFields = new FormData();
    fdIdentityFields.left = new FormAttachment( 0, 0 );
    fdIdentityFields.top = new FormAttachment( 0, 0 );
    fdIdentityFields.right = new FormAttachment( 100, 0 );
    fdIdentityFields.bottom = new FormAttachment( wIdentityGet, -margin );
    wIdentityFields.setLayoutData( fdIdentityFields );

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
    Listener lsUserGet = new Listener() {
      public void handleEvent( Event e ) {
        getUserFields();
      }
    };
    Listener lsIdentityGet = new Listener() {
      public void handleEvent( Event e ) {
        getIdentityFields();
      }
    };

    wCancel.addListener( SWT.Selection, lsCancel );
    wOK.addListener( SWT.Selection, lsOK );
    wUserGet.addListener( SWT.Selection, lsUserGet );
    wIdentityGet.addListener( SWT.Selection, lsIdentityGet );

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

    for ( int i = 0; i < input.getUserFields().length; i++ ) {
      TableItem item = wUserFields.table.getItem( i );
      item.setText( 1, Const.NVL( input.getUserFields()[i].getName(), "" ) );
      item.setText( 2, Const.NVL( input.getUserFields()[i].getType().toString(), "" ) );
    }
    wUserFields.removeEmptyRows();
    wUserFields.setRowNums();

    for ( int i = 0; i < input.getIdentityFields().length; i++ ) {
      TableItem item = wIdentityFields.table.getItem( i );
      item.setText( 1, Const.NVL( input.getIdentityFields()[i].getName(), "" ) );
      item.setText( 2, Const.NVL( input.getIdentityFields()[i].getType().toString(), "" ) );
    }
    wIdentityFields.removeEmptyRows();
    wIdentityFields.setRowNums();
    
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

    int nrUserFields = wUserFields.nrNonEmpty();
    List<UserField> userFields = new ArrayList<>();
    for ( int i = 0; i < nrUserFields; i++ ) {
      TableItem item = wUserFields.getNonEmpty( i );
      String colName = item.getText( 1 );
      UserField.Attribute colType = UserField.Attribute.getEnumFromValue( item.getText( 2 ) );
      if ( colType != null && !Const.isEmpty( colName ) ) {
        userFields.add( new UserField( colName, colType ) );
      }
    }
    inf.setUserFields( userFields.toArray( new UserField[0] ) );

    int nrIdentityFields = wIdentityFields.nrNonEmpty();
    List<IdentityField> identityFields = new ArrayList<>();
    for ( int i = 0; i < nrIdentityFields; i++ ) {
      TableItem item = wIdentityFields.getNonEmpty( i );
      String colName = item.getText( 1 );
      IdentityField.Attribute colType = IdentityField.Attribute.getEnumFromValue( item.getText( 2 ) );
      if ( colType != null && !Const.isEmpty( colName ) ) {
        identityFields.add( new IdentityField( colName, colType ) );
      }
    }
    inf.setIdentityFields( identityFields.toArray( new IdentityField[0] ) );

    stepname = wStepname.getText(); // return value
  }

  public void getUserFields() {
    int clearFields = SWT.NO;
    int nrInputFields = wUserFields.nrNonEmpty();
    if ( nrInputFields > 0 ) {
      MessageBox mb = new MessageBox( shell, SWT.YES | SWT.NO | SWT.ICON_QUESTION );
      mb.setMessage( BaseMessages.getString( PKG, "ZendeskInputDialog.ClearFieldList.DialogMessage" ) );
      mb.setText( BaseMessages.getString( PKG, "ZendeskInputDialog.ClearFieldList.DialogTitle" ) );
      clearFields = mb.open();
    }

    if ( clearFields == SWT.YES ) {
      // Clear User Fields grid
    	wUserFields.table.removeAll();
    }

    for ( UserField.Attribute value : UserField.Attribute.values() ) {
      TableItem item = new TableItem( wUserFields.table, SWT.NONE );
      item.setText( 1, value.toString() );
      item.setText( 2, value.toString() );
    }

    wUserFields.removeEmptyRows();
    wUserFields.setRowNums();
    wUserFields.optWidth( true );
    input.setChanged();
  }

  public void getIdentityFields() {
    int clearFields = SWT.NO;
    int nrInputFields = wIdentityFields.nrNonEmpty();
    if ( nrInputFields > 0 ) {
      MessageBox mb = new MessageBox( shell, SWT.YES | SWT.NO | SWT.ICON_QUESTION );
      mb.setMessage( BaseMessages.getString( PKG, "ZendeskInputDialog.ClearFieldList.DialogMessage" ) );
      mb.setText( BaseMessages.getString( PKG, "ZendeskInputDialog.ClearFieldList.DialogTitle" ) );
      clearFields = mb.open();
    }

    if ( clearFields == SWT.YES ) {
      // Clear User Fields grid
    	wIdentityFields.table.removeAll();
    }

    for ( IdentityField.Attribute value : IdentityField.Attribute.values() ) {
      TableItem item = new TableItem( wIdentityFields.table, SWT.NONE );
      item.setText( 1, value.toString() );
      item.setText( 2, value.toString() );
    }

    wIdentityFields.removeEmptyRows();
    wIdentityFields.setRowNums();
    wIdentityFields.optWidth( true );
    input.setChanged();
  }

  private String[] getUserElementNames() {
    UserField.Attribute[] values = UserField.Attribute.values();
    String[] valueNames = new String[values.length];
    for ( int i = 0; i < values.length; i++ ) {
      valueNames[i] = values[i].toString();
    }
    return valueNames;
  }

  private String[] getIdentityElementNames() {
    IdentityField.Attribute[] values = IdentityField.Attribute.values();
    String[] valueNames = new String[values.length];
    for ( int i = 0; i < values.length; i++ ) {
      valueNames[i] = values[i].toString();
    }
    return valueNames;
  }
}
