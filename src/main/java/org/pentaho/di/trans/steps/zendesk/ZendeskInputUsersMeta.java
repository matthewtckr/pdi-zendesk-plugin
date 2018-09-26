/*! ******************************************************************************
 *
 * Pentaho Data Integration
 *
 * Copyright (C) 2002-2018 by Pentaho : http://www.pentaho.com
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

package org.pentaho.di.trans.steps.zendesk;

import java.util.ArrayList;
import java.util.List;

import org.pentaho.di.core.Const;
import org.pentaho.di.core.annotations.Step;
import org.pentaho.di.core.database.DatabaseMeta;
import org.pentaho.di.core.exception.KettleException;
import org.pentaho.di.core.exception.KettleStepException;
import org.pentaho.di.core.exception.KettleXMLException;
import org.pentaho.di.core.row.RowMetaInterface;
import org.pentaho.di.core.row.ValueMetaInterface;
import org.pentaho.di.core.variables.VariableSpace;
import org.pentaho.di.core.xml.XMLHandler;
import org.pentaho.di.i18n.BaseMessages;
import org.pentaho.di.repository.ObjectId;
import org.pentaho.di.repository.Repository;
import org.pentaho.di.trans.Trans;
import org.pentaho.di.trans.TransMeta;
import org.pentaho.di.trans.step.StepDataInterface;
import org.pentaho.di.trans.step.StepIOMeta;
import org.pentaho.di.trans.step.StepIOMetaInterface;
import org.pentaho.di.trans.step.StepInterface;
import org.pentaho.di.trans.step.StepMeta;
import org.pentaho.di.trans.step.errorhandling.Stream;
import org.pentaho.di.trans.step.errorhandling.StreamIcon;
import org.pentaho.di.trans.step.errorhandling.StreamInterface;
import org.pentaho.di.trans.step.errorhandling.StreamInterface.StreamType;
import org.pentaho.metastore.api.IMetaStore;
import org.w3c.dom.Node;

@Step(
    id = "ZendeskInputUsers",
    image = "org/pentaho/di/trans/steps/zendesk/zendesk.png",
    i18nPackageName = "org.pentaho.di.trans.steps.zendesk",
    name = "ZendeskInputUsers.Name",
    description = "ZendeskInputUsers.TooltipDesc",
    categoryDescription = "i18n:org.pentaho.di.trans.step:BaseStep.Category.Input"
  )
public class ZendeskInputUsersMeta extends ZendeskInputMeta {

  private static final Class<?> PKG = ZendeskInputUsersMeta.class;

  private String incomingFieldname;
  private UserField[] userFields;
  private IdentityField[] identityFields;

  private StepIOMetaInterface ioMeta;
  private String userStepName;
  private String userIdentityStepName;

  private StepMeta userStepMeta;
  private StepMeta userIdentityStepMeta;
  
  public UserField[] getUserFields() {
    return userFields;
  }

  public void setUserFields( UserField[] fields ) {
    this.userFields = fields;
  }

  public IdentityField[] getIdentityFields() {
    return identityFields;
  }

  public void setIdentityFields( IdentityField[] fields ) {
    this.identityFields = fields;
  }

  public void allocateUserFields( int count ) {
    this.userFields = new UserField[count];
  }

  public void allocateIdentityFields( int count ) {
    this.identityFields = new IdentityField[count];
  }

  @Override
  public void getFields( RowMetaInterface inputRowMeta, String name, RowMetaInterface[] info, StepMeta nextStep,
      VariableSpace space, Repository repository, IMetaStore metaStore ) throws KettleStepException {
    super.getFields( inputRowMeta, name, info, nextStep, space, repository, metaStore );
    if ( nextStep != null ) {
      if ( nextStep.equals( userStepMeta ) ) {
        prepareExecutionResultsUser( inputRowMeta, space );
      } else if ( nextStep.equals( userIdentityStepMeta ) ) {
        prepareExecutionResultsUserIdentity( inputRowMeta, space );
      }
    }
  }

  private void prepareExecutionResultsUser( RowMetaInterface inputRowMeta, VariableSpace space ) throws KettleStepException {
    inputRowMeta.clear();
    if ( userFields != null ) {
      for ( int i = 0; i < userFields.length; i++ ) {
        addFieldToRow( inputRowMeta, space.environmentSubstitute( userFields[i].getName() ), userFields[i].getType().getValueMetaType() );
      }
    }
  }

  private void prepareExecutionResultsUserIdentity( RowMetaInterface inputRowMeta, VariableSpace space ) throws KettleStepException {
    inputRowMeta.clear();
    if ( identityFields != null ) {
      for ( int i = 0; i < identityFields.length; i++ ) {
        addFieldToRow( inputRowMeta, space.environmentSubstitute( identityFields[i].getName() ), identityFields[i].getType().getValueMetaType() );
      }
    }
  }

  @Override
  public StepIOMetaInterface getStepIOMeta() {
    if ( ioMeta == null ) {
      ioMeta = new StepIOMeta( true, true, false, false, true, false );

      ioMeta.addStream( new Stream( StreamType.TARGET, userStepMeta, BaseMessages.getString(
        PKG, "ZendeskInputUsersMeta.UserOverviewStream.Description" ), StreamIcon.TARGET, null ) );
      ioMeta.addStream( new Stream( StreamType.TARGET, userIdentityStepMeta, BaseMessages.getString(
        PKG, "ZendeskInputUsersMeta.UserIdentityStream.Description" ), StreamIcon.TARGET, null ) );
    }
    return ioMeta;
  }

  @Override
  public void searchInfoAndTargetSteps( List<StepMeta> steps ) {
    userStepMeta = StepMeta.findStep( steps, userStepName );
    userIdentityStepMeta = StepMeta.findStep( steps, userIdentityStepName );
  }

  @Override
  public void handleStreamSelection( StreamInterface stream ) {
    List<StreamInterface> targets = getStepIOMeta().getTargetStreams();
    int index = targets.indexOf( stream );
    StepMeta step = targets.get( index ).getStepMeta();
    switch ( index ) {
      case 0:
        setUserStepMeta( step );
        break;
      case 1:
        setUserIdentityStepMeta( step );
        break;
      default:
        break;
    }
  }

  public boolean supportsErrorHandling() {
    return true;
  }

  @Override
  public StepInterface getStep( StepMeta stepMeta, StepDataInterface stepDataInterface, int copyNr,
      TransMeta transMeta, Trans trans ) {
    return new ZendeskInputUsers( stepMeta, stepDataInterface, copyNr, transMeta, trans );
  }

  @Override
  public StepDataInterface getStepData() {
    return new ZendeskInputUsersData();
  }

  @Override
  public String getXML() throws KettleException {
    StringBuilder xml = new StringBuilder();
    xml.append( super.getXML() );
    xml.append( "    " ).append( XMLHandler.addTagValue( "incomingFieldname", getIncomingFieldname() ) );
    xml.append( "    " ).append( XMLHandler.addTagValue( "userStepName",
      getUserStepMeta() != null ? getUserStepMeta().getName() : getUserStepName() ) );
    xml.append( "    " ).append( XMLHandler.addTagValue( "userIdentityStepName",
      getUserIdentityStepMeta() != null ? getUserIdentityStepMeta().getName() : getUserIdentityStepName() ) );

    xml.append( "    " ).append( XMLHandler.openTag( "userFields" ) ).append( Const.CR );
    if ( userFields != null ) {
      for ( int i = 0; i < userFields.length; i++ ) {
        xml.append( "      " ).append( XMLHandler.openTag( "field" ) ).append( Const.CR );
        xml.append( "        " ).append( XMLHandler.addTagValue( "name", userFields[i].getName() ) );
        xml.append( "        " ).append( XMLHandler.addTagValue( "type", userFields[i].getType().name() ) );
        xml.append( "      " ).append( XMLHandler.closeTag( "field" ) ).append( Const.CR );
      }
    }
    xml.append( "    " ).append( XMLHandler.closeTag( "userFields" ) ).append( Const.CR );

    xml.append( "    " ).append( XMLHandler.openTag( "identityFields" ) ).append( Const.CR );
    if ( identityFields != null ) {
      for ( int i = 0; i < identityFields.length; i++ ) {
        xml.append( "      " ).append( XMLHandler.openTag( "field" ) ).append( Const.CR );
        xml.append( "        " ).append( XMLHandler.addTagValue( "name", identityFields[i].getName() ) );
        xml.append( "        " ).append( XMLHandler.addTagValue( "type", identityFields[i].getType().name() ) );
        xml.append( "      " ).append( XMLHandler.closeTag( "field" ) ).append( Const.CR );
      }
    }
    xml.append( "    " ).append( XMLHandler.closeTag( "identityFields" ) ).append( Const.CR );
    return xml.toString();
  }

  @Override
  public void loadXML( Node stepnode, List<DatabaseMeta> databases, IMetaStore metaStore ) throws KettleXMLException {
    super.loadXML( stepnode, databases, metaStore );
    setIncomingFieldname( XMLHandler.getTagValue( stepnode, "incomingFieldname" ) );
    setUserStepName( XMLHandler.getTagValue( stepnode, "userStepName" ) );
    setUserIdentityStepName( XMLHandler.getTagValue( stepnode, "userIdentityStepName" ) );

    Node userFieldsNode = XMLHandler.getSubNode( stepnode,  "userFields" );
    Node identityFieldsNode = XMLHandler.getSubNode( stepnode,  "identityFields" );
    if ( userFieldsNode != null ) {
      int userFieldsCount = XMLHandler.countNodes( userFieldsNode, "field" );
      allocateUserFields( userFieldsCount );
      for ( int i = 0; i < userFieldsCount; i++ ) {
        Node userFieldNode = XMLHandler.getSubNodeByNr( userFieldsNode, "field", i );
        String name = XMLHandler.getTagValue( userFieldNode, "name" );
        UserField.Attribute type = UserField.Attribute.valueOf( XMLHandler.getTagValue( userFieldNode, "type" ) );
        if ( type != null ) {
          userFields[i] = new UserField( name, type );
        }
      }
      if ( identityFieldsNode != null ) {
        int identityFieldsCount = XMLHandler.countNodes( identityFieldsNode, "field" );
        allocateIdentityFields( identityFieldsCount );
        for ( int i = 0; i < identityFieldsCount; i++ ) {
          Node identityFieldNode = XMLHandler.getSubNodeByNr( identityFieldsNode, "field", i );
          String name = XMLHandler.getTagValue( identityFieldNode, "name" );
          IdentityField.Attribute type = IdentityField.Attribute.valueOf( XMLHandler.getTagValue( identityFieldNode, "type" ) );
          if ( type != null ) {
            identityFields[i] = new IdentityField( name, type );
          }
        }
      }
    } else {
      // Read Legacy XML
      List<UserField> tempUserFields = new ArrayList<>();
      String temp = null;
      temp = XMLHandler.getTagValue( stepnode, "userIdFieldname" );
      if ( temp != null ) {
        tempUserFields.add( new UserField( temp, UserField.Attribute.USERID ) );
      }
      temp = XMLHandler.getTagValue( stepnode, "urlFieldname" );
      if ( temp != null ) {
        tempUserFields.add( new UserField( temp, UserField.Attribute.URL ) );
      }
      temp = XMLHandler.getTagValue( stepnode, "externalIdFieldname" );
      if ( temp != null ) {
        tempUserFields.add( new UserField( temp, UserField.Attribute.EXTERNALID ) );
      }
      temp = XMLHandler.getTagValue( stepnode, "nameFieldname" );
      if ( temp != null ) {
        tempUserFields.add( new UserField( temp, UserField.Attribute.NAME ) );
      }
      temp = XMLHandler.getTagValue( stepnode, "emailFieldname" );
      if ( temp != null ) {
        tempUserFields.add( new UserField( temp, UserField.Attribute.EMAIL ) );
      }
      temp = XMLHandler.getTagValue( stepnode, "aliasFieldname" );
      if ( temp != null ) {
        tempUserFields.add( new UserField( temp, UserField.Attribute.ALIAS ) );
      }
      temp = XMLHandler.getTagValue( stepnode, "createdAtFieldname" );
      if ( temp != null ) {
        tempUserFields.add( new UserField( temp, UserField.Attribute.CREATED_AT ) );
      }
      temp = XMLHandler.getTagValue( stepnode, "updatedAtFieldname" );
      if ( temp != null ) {
        tempUserFields.add( new UserField( temp, UserField.Attribute.UPDATED_AT ) );
      }
      temp = XMLHandler.getTagValue( stepnode, "activeFieldname" );
      if ( temp != null ) {
        tempUserFields.add( new UserField( temp, UserField.Attribute.ACTIVE ) );
      }
      temp = XMLHandler.getTagValue( stepnode, "verifiedFieldname" );
      if ( temp != null ) {
        tempUserFields.add( new UserField( temp, UserField.Attribute.VERIFIED ) );
      }
      temp = XMLHandler.getTagValue( stepnode, "sharedFieldname" );
      if ( temp != null ) {
        tempUserFields.add( new UserField( temp, UserField.Attribute.SHARED ) );
      }
      temp = XMLHandler.getTagValue( stepnode, "localeIdFieldname" );
      if ( temp != null ) {
        tempUserFields.add( new UserField( temp, UserField.Attribute.LOCALE_ID ) );
      }
      temp = XMLHandler.getTagValue( stepnode, "timeZoneFieldname" );
      if ( temp != null ) {
        tempUserFields.add( new UserField( temp, UserField.Attribute.TIMEZONE ) );
      }
      temp = XMLHandler.getTagValue( stepnode, "lastLoginAtFieldname" );
      if ( temp != null ) {
        tempUserFields.add( new UserField( temp, UserField.Attribute.LAST_LOGIN_AT ) );
      }
      temp = XMLHandler.getTagValue( stepnode, "phoneFieldname" );
      if ( temp != null ) {
        tempUserFields.add( new UserField( temp, UserField.Attribute.PHONE ) );
      }
      temp = XMLHandler.getTagValue( stepnode, "signatureFieldname" );
      if ( temp != null ) {
        tempUserFields.add( new UserField( temp, UserField.Attribute.SIGNATURE ) );
      }
      temp = XMLHandler.getTagValue( stepnode, "detailsFieldname" );
      if ( temp != null ) {
        tempUserFields.add( new UserField( temp, UserField.Attribute.DETAILS ) );
      }
      temp = XMLHandler.getTagValue( stepnode, "notesFieldname" );
      if ( temp != null ) {
        tempUserFields.add( new UserField( temp, UserField.Attribute.NOTES ) );
      }
      temp = XMLHandler.getTagValue( stepnode, "organizationIdFieldname" );
      if ( temp != null ) {
        tempUserFields.add( new UserField( temp, UserField.Attribute.ORGANIZATION_ID ) );
      }
      temp = XMLHandler.getTagValue( stepnode, "roleFieldname" );
      if ( temp != null ) {
        tempUserFields.add( new UserField( temp, UserField.Attribute.ROLE ) );
      }
      temp = XMLHandler.getTagValue( stepnode, "customRoleIdFieldname" );
      if ( temp != null ) {
        tempUserFields.add( new UserField( temp, UserField.Attribute.CUSTOM_ROLE_ID ) );
      }
      temp = XMLHandler.getTagValue( stepnode, "moderatorFieldname" );
      if ( temp != null ) {
        tempUserFields.add( new UserField( temp, UserField.Attribute.MODERATOR ) );
      }
      temp = XMLHandler.getTagValue( stepnode, "onlyPrivateCommentsFieldname" );
      if ( temp != null ) {
        tempUserFields.add( new UserField( temp, UserField.Attribute.ONLY_PRIVATE_COMMENTS ) );
      }
      temp = XMLHandler.getTagValue( stepnode, "tagsFieldname" );
      if ( temp != null ) {
        tempUserFields.add( new UserField( temp, UserField.Attribute.TAGS ) );
      }
      temp = XMLHandler.getTagValue( stepnode, "suspendedFieldname" );
      if ( temp != null ) {
        tempUserFields.add( new UserField( temp, UserField.Attribute.SUSPENDED ) );
      }
      temp = XMLHandler.getTagValue( stepnode, "remotePhotoUrlFieldname" );
      if ( temp != null ) {
        tempUserFields.add( new UserField( temp, UserField.Attribute.REMOTE_PHOTO_URL ) );
      }
      temp = XMLHandler.getTagValue( stepnode, "userFieldsFieldname" );
      if ( temp != null ) {
        tempUserFields.add( new UserField( temp, UserField.Attribute.USER_FIELDS ) );
      }
      userFields = tempUserFields.toArray( new UserField[0] );

      // Identity Fields
      List<IdentityField> tempIdentityFields = new ArrayList<IdentityField>();
      temp = XMLHandler.getTagValue( stepnode, "identityIdFieldname" );
      if ( temp != null ) {
        tempIdentityFields.add( new IdentityField( temp, IdentityField.Attribute.ID ) );
      }
      temp = XMLHandler.getTagValue( stepnode, "identityUrlFieldname" );
      if ( temp != null ) {
        tempIdentityFields.add( new IdentityField( temp, IdentityField.Attribute.URL ) );
      }
      temp = XMLHandler.getTagValue( stepnode, "identityTypeFieldname" );
      if ( temp != null ) {
        tempIdentityFields.add( new IdentityField( temp, IdentityField.Attribute.TYPE ) );
      }
      temp = XMLHandler.getTagValue( stepnode, "identityValueFieldname" );
      if ( temp != null ) {
        tempIdentityFields.add( new IdentityField( temp, IdentityField.Attribute.VALUE ) );
      }
      temp = XMLHandler.getTagValue( stepnode, "identityVerifiedFieldname" );
      if ( temp != null ) {
        tempIdentityFields.add( new IdentityField( temp, IdentityField.Attribute.VERIFIED ) );
      }
      temp = XMLHandler.getTagValue( stepnode, "identityPrimaryFieldname" );
      if ( temp != null ) {
        tempIdentityFields.add( new IdentityField( temp, IdentityField.Attribute.PRIMARY ) );
      }
      temp = XMLHandler.getTagValue( stepnode, "identityCreatedAtFieldname" );
      if ( temp != null ) {
        tempIdentityFields.add( new IdentityField( temp, IdentityField.Attribute.CREATED_AT ) );
      }
      temp = XMLHandler.getTagValue( stepnode, "identityUpdatedAtFieldname" );
      if ( temp != null ) {
        tempIdentityFields.add( new IdentityField( temp, IdentityField.Attribute.UPDATED_AT ) );
      }
      if ( tempIdentityFields.size() > 0 ) {
        tempIdentityFields.add( 0, new IdentityField( Const.NVL( XMLHandler.getTagValue( stepnode, "userIdFieldname" ), "User_ID" ), IdentityField.Attribute.USERID ) );
      }
      identityFields = tempIdentityFields.toArray( new IdentityField[0] );
    }
  }

  @Override
  public void readRep( Repository rep, IMetaStore metaStore, ObjectId id_step, List<DatabaseMeta> databases )
    throws KettleException {
    super.readRep( rep, metaStore, id_step, databases );
    setIncomingFieldname( rep.getStepAttributeString( id_step, "incomingFieldname" ) );
    setUserStepName( rep.getStepAttributeString( id_step, "userStepName" ) );
    setUserIdentityStepName( rep.getStepAttributeString( id_step, "userIdentityStepName" ) );

    int userFieldCount = rep.countNrStepAttributes( id_step, "userFieldName" );
    if ( userFieldCount > 0 ) {
      allocateUserFields( userFieldCount );
      for ( int i = 0; i < userFieldCount; i++ ) {
        String name = rep.getStepAttributeString( id_step, i, "userFieldName" );
        UserField.Attribute type = UserField.Attribute.valueOf( rep.getStepAttributeString( id_step, i, "userFieldType" ) );
        userFields[i] = new UserField( name, type );
      }
    }
    int identityFieldCount = rep.countNrStepAttributes( id_step, "identityFieldName" );
    if ( identityFieldCount > 0  ) {
      allocateIdentityFields( identityFieldCount );
      for ( int i = 0; i < identityFieldCount; i++ ) {
        String name = rep.getStepAttributeString( id_step, i, "identityFieldName" );
        IdentityField.Attribute type = IdentityField.Attribute.valueOf( rep.getStepAttributeString( id_step, i, "identityFieldType" ) );
        identityFields[i] = new IdentityField( name, type );
      }
    }
    if ( identityFieldCount == 0 && identityFieldCount == 0 ) {
      // Legacy Repo Import
      List<UserField> tempUserFields = new ArrayList<>();
      String temp = null;
      temp = rep.getStepAttributeString( id_step, "userIdFieldname" );
      if ( temp != null ) {
        tempUserFields.add( new UserField( temp, UserField.Attribute.USERID ) );
      }
      temp = rep.getStepAttributeString( id_step, "urlFieldname" );
      if ( temp != null ) {
        tempUserFields.add( new UserField( temp, UserField.Attribute.URL ) );
      }
      temp = rep.getStepAttributeString( id_step, "externalIdFieldname" );
      if ( temp != null ) {
        tempUserFields.add( new UserField( temp, UserField.Attribute.EXTERNALID ) );
      }
      temp = rep.getStepAttributeString( id_step, "nameFieldname" );
      if ( temp != null ) {
        tempUserFields.add( new UserField( temp, UserField.Attribute.NAME ) );
      }
      temp = rep.getStepAttributeString( id_step, "emailFieldname" );
      if ( temp != null ) {
        tempUserFields.add( new UserField( temp, UserField.Attribute.EMAIL ) );
      }
      temp = rep.getStepAttributeString( id_step, "aliasFieldname" );
      if ( temp != null ) {
        tempUserFields.add( new UserField( temp, UserField.Attribute.ALIAS ) );
      }
      temp = rep.getStepAttributeString( id_step, "createdAtFieldname" );
      if ( temp != null ) {
        tempUserFields.add( new UserField( temp, UserField.Attribute.CREATED_AT ) );
      }
      temp = rep.getStepAttributeString( id_step, "updatedAtFieldname" );
      if ( temp != null ) {
        tempUserFields.add( new UserField( temp, UserField.Attribute.UPDATED_AT ) );
      }
      temp = rep.getStepAttributeString( id_step, "activeFieldname" );
      if ( temp != null ) {
        tempUserFields.add( new UserField( temp, UserField.Attribute.ACTIVE ) );
      }
      temp = rep.getStepAttributeString( id_step, "verifiedFieldname" );
      if ( temp != null ) {
        tempUserFields.add( new UserField( temp, UserField.Attribute.VERIFIED ) );
      }
      temp = rep.getStepAttributeString( id_step, "sharedFieldname" );
      if ( temp != null ) {
        tempUserFields.add( new UserField( temp, UserField.Attribute.SHARED ) );
      }
      temp = rep.getStepAttributeString( id_step, "localeIdFieldname" );
      if ( temp != null ) {
        tempUserFields.add( new UserField( temp, UserField.Attribute.LOCALE_ID ) );
      }
      temp = rep.getStepAttributeString( id_step, "timeZoneFieldname" );
      if ( temp != null ) {
        tempUserFields.add( new UserField( temp, UserField.Attribute.TIMEZONE ) );
      }
      temp = rep.getStepAttributeString( id_step, "lastLoginAtFieldname" );
      if ( temp != null ) {
        tempUserFields.add( new UserField( temp, UserField.Attribute.LAST_LOGIN_AT ) );
      }
      temp = rep.getStepAttributeString( id_step, "phoneFieldname" );
      if ( temp != null ) {
        tempUserFields.add( new UserField( temp, UserField.Attribute.PHONE ) );
      }
      temp = rep.getStepAttributeString( id_step, "signatureFieldname" );
      if ( temp != null ) {
        tempUserFields.add( new UserField( temp, UserField.Attribute.SIGNATURE ) );
      }
      temp = rep.getStepAttributeString( id_step, "detailsFieldname" );
      if ( temp != null ) {
        tempUserFields.add( new UserField( temp, UserField.Attribute.DETAILS ) );
      }
      temp = rep.getStepAttributeString( id_step, "notesFieldname" );
      if ( temp != null ) {
        tempUserFields.add( new UserField( temp, UserField.Attribute.NOTES ) );
      }
      temp = rep.getStepAttributeString( id_step, "organizationIdFieldname" );
      if ( temp != null ) {
        tempUserFields.add( new UserField( temp, UserField.Attribute.ORGANIZATION_ID ) );
      }
      temp = rep.getStepAttributeString( id_step, "roleFieldname" );
      if ( temp != null ) {
        tempUserFields.add( new UserField( temp, UserField.Attribute.ROLE ) );
      }
      temp = rep.getStepAttributeString( id_step, "customRoleIdFieldname" );
      if ( temp != null ) {
        tempUserFields.add( new UserField( temp, UserField.Attribute.CUSTOM_ROLE_ID ) );
      }
      temp = rep.getStepAttributeString( id_step, "moderatorFieldname" );
      if ( temp != null ) {
        tempUserFields.add( new UserField( temp, UserField.Attribute.MODERATOR ) );
      }
      temp = rep.getStepAttributeString( id_step, "ticketRestrictionFieldname" );
      if ( temp != null ) {
        tempUserFields.add( new UserField( temp, UserField.Attribute.TICKET_RESTRICTION ) );
      }
      temp = rep.getStepAttributeString( id_step, "onlyPrivateCommentsFieldname" );
      if ( temp != null ) {
        tempUserFields.add( new UserField( temp, UserField.Attribute.ONLY_PRIVATE_COMMENTS ) );
      }
      temp = rep.getStepAttributeString( id_step, "tagsFieldname" );
      if ( temp != null ) {
        tempUserFields.add( new UserField( temp, UserField.Attribute.TAGS ) );
      }
      temp = rep.getStepAttributeString( id_step, "suspendedFieldname" );
      if ( temp != null ) {
        tempUserFields.add( new UserField( temp, UserField.Attribute.SUSPENDED ) );
      }
      temp = rep.getStepAttributeString( id_step, "remotePhotoUrlFieldname" );
      if ( temp != null ) {
        tempUserFields.add( new UserField( temp, UserField.Attribute.REMOTE_PHOTO_URL ) );
      }
      temp = rep.getStepAttributeString( id_step, "userFieldsFieldname" );
      if ( temp != null ) {
        tempUserFields.add( new UserField( temp, UserField.Attribute.USER_FIELDS ) );
      }
      userFields = tempUserFields.toArray( new UserField[0] );

      // Identity Fields
      List<IdentityField> tempIdentityFields = new ArrayList<>();
      temp = rep.getStepAttributeString( id_step, "identityIdFieldname" );
      if ( temp != null ) {
        tempIdentityFields.add( new IdentityField( temp, IdentityField.Attribute.ID ) );
      }
      temp = rep.getStepAttributeString( id_step, "identityUrlFieldname" );
      if ( temp != null ) {
        tempIdentityFields.add( new IdentityField( temp, IdentityField.Attribute.URL ) );
      }
      temp = rep.getStepAttributeString( id_step, "identityTypeFieldname" );
      if ( temp != null ) {
        tempIdentityFields.add( new IdentityField( temp, IdentityField.Attribute.TYPE ) );
      }
      temp = rep.getStepAttributeString( id_step, "identityValueFieldname" );
      if ( temp != null ) {
        tempIdentityFields.add( new IdentityField( temp, IdentityField.Attribute.VALUE ) );
      }
      temp = rep.getStepAttributeString( id_step, "identityVerifiedFieldname" );
      if ( temp != null ) {
        tempIdentityFields.add( new IdentityField( temp, IdentityField.Attribute.VERIFIED ) );
      }
      temp = rep.getStepAttributeString( id_step, "identityPrimaryFieldname" );
      if ( temp != null ) {
        tempIdentityFields.add( new IdentityField( temp, IdentityField.Attribute.PRIMARY ) );
      }
      temp = rep.getStepAttributeString( id_step, "identityCreatedAtFieldname" );
      if ( temp != null ) {
        tempIdentityFields.add( new IdentityField( temp, IdentityField.Attribute.CREATED_AT ) );
      }
      temp = rep.getStepAttributeString( id_step, "identityUpdatedAtFieldname" );
      if ( temp != null ) {
        tempIdentityFields.add( new IdentityField( temp, IdentityField.Attribute.UPDATED_AT ) );
      }
      if ( tempIdentityFields.size() > 0 ) {
        tempIdentityFields.add( 0, new IdentityField( Const.NVL( rep.getStepAttributeString( id_step, "userIdFieldname" ), "User_ID" ), IdentityField.Attribute.USERID ) );
      }
      identityFields = tempIdentityFields.toArray( new IdentityField[0] );
    }
  }

  @Override
  public void saveRep( Repository rep, IMetaStore metaStore, ObjectId id_transformation, ObjectId id_step )
    throws KettleException {
    super.saveRep( rep, metaStore, id_transformation, id_step );
    rep.saveStepAttribute( id_transformation, id_step, "incomingFieldname", getIncomingFieldname() );
    rep.saveStepAttribute( id_transformation, id_step, "userStepName",
      getUserStepMeta() != null ? getUserStepMeta().getName() : getUserStepName() );
    rep.saveStepAttribute( id_transformation, id_step, "userIdentityStepName",
      getUserIdentityStepMeta() != null ? getUserIdentityStepMeta().getName() : getUserIdentityStepName() );

    if ( userFields != null ) {
      for ( int i = 0; i < userFields.length; i++ ) {
        rep.saveStepAttribute( id_transformation, id_step, i, "userFieldName", userFields[i].getName() );
        rep.saveStepAttribute( id_transformation, id_step, i, "userFieldType", userFields[i].getType().name() );
      }
    }
    if ( identityFields != null ) {
      for ( int i = 0; i < identityFields.length; i++ ) {
        rep.saveStepAttribute( id_transformation, id_step, i, "identityFieldName", identityFields[i].getName() );
        rep.saveStepAttribute( id_transformation, id_step, i, "identityFieldType", identityFields[i].getType().name() );
      }
    }
  }

  @Override
  public void setDefault() {
    super.setDefault();
    UserField.Attribute[] values = UserField.Attribute.values();
    allocateUserFields( values.length );
    for ( int i = 0; i < values.length; i++ ) {
      userFields[i] = new UserField( values[i].toString(), values[i] );
    }

    IdentityField.Attribute[] idValues = IdentityField.Attribute.values();
    allocateIdentityFields( idValues.length );
    for ( int i = 0; i < idValues.length; i++ ) {
      identityFields[i] = new IdentityField( idValues[i].toString(), idValues[i] );
    }
  }

  public String getIncomingFieldname() {
    return incomingFieldname;
  }

  public void setIncomingFieldname( String incomingFieldname ) {
    this.incomingFieldname = incomingFieldname;
  }

  public StepIOMetaInterface getIoMeta() {
    return ioMeta;
  }

  public void setIoMeta( StepIOMetaInterface ioMeta ) {
    this.ioMeta = ioMeta;
  }

  public String getUserStepName() {
    return userStepName;
  }

  public void setUserStepName( String userStepName ) {
    this.userStepName = userStepName;
  }

  public String getUserIdentityStepName() {
    return userIdentityStepName;
  }

  public void setUserIdentityStepName( String userIdentityStepName ) {
    this.userIdentityStepName = userIdentityStepName;
  }

  public StepMeta getUserStepMeta() {
    return userStepMeta;
  }

  public void setUserStepMeta( StepMeta userStepMeta ) {
    this.userStepMeta = userStepMeta;
  }

  public StepMeta getUserIdentityStepMeta() {
    return userIdentityStepMeta;
  }

  public void setUserIdentityStepMeta( StepMeta userIdentityStepMeta ) {
    this.userIdentityStepMeta = userIdentityStepMeta;
  }

  public static class UserField {
    private String name;
    private Attribute type;

    public UserField( String name, Attribute type ) {
      this.name = name;
      this.type = type;
    }

    public String getName() {
      return name;
    }

    public void setName(String name) {
      this.name = name;
    }

    public Attribute getType() {
      return type;
    }

    public void setType(Attribute type) {
      this.type = type;
    }

    public enum Attribute {
      USERID( ValueMetaInterface.TYPE_INTEGER ) {
        @Override
        public String toString() {
          return "User_ID";
        }
      },
      URL( ValueMetaInterface.TYPE_STRING ) {
        @Override
        public String toString() {
          return "User_URL";
        }
      },
      EXTERNALID( ValueMetaInterface.TYPE_STRING ) {
        @Override
        public String toString() {
          return "User_External_ID";
        }
      },
      NAME( ValueMetaInterface.TYPE_STRING ) {
        @Override
        public String toString() {
          return "User_Name";
        }
      },
      EMAIL( ValueMetaInterface.TYPE_STRING ) {
        @Override
        public String toString() {
          return "User_Email";
        }
      },
      ALIAS( ValueMetaInterface.TYPE_STRING ) {
        @Override
        public String toString() {
          return "User_Alias";
        }
      },
      CREATED_AT( ValueMetaInterface.TYPE_DATE ) {
        @Override
        public String toString() {
          return "Created_Time";
        }
      },
      UPDATED_AT( ValueMetaInterface.TYPE_DATE ) {
        @Override
        public String toString() {
          return "Updated_Time";
        }
      },
      ACTIVE( ValueMetaInterface.TYPE_BOOLEAN ) {
        @Override
        public String toString() {
          return "Is_Active";
        }
      },
      VERIFIED( ValueMetaInterface.TYPE_BOOLEAN ) {
        @Override
        public String toString() {
          return "Is_Verified";
        }
      },
      SHARED( ValueMetaInterface.TYPE_BOOLEAN ) {
        @Override
        public String toString() {
          return "Is_Shared";
        }
      },
      LOCALE_ID( ValueMetaInterface.TYPE_INTEGER ) {
        @Override
        public String toString() {
          return "Locale_ID";
        }
      },
      TIMEZONE( ValueMetaInterface.TYPE_STRING ) {
        @Override
        public String toString() {
          return "Timezone";
        }
      },
      LAST_LOGIN_AT( ValueMetaInterface.TYPE_DATE ) {
        @Override
        public String toString() {
          return "Last_Login_Time";
        }
      },
      PHONE( ValueMetaInterface.TYPE_STRING ) {
        @Override
        public String toString() {
          return "Phone";
        }
      },
      SIGNATURE( ValueMetaInterface.TYPE_STRING ) {
        @Override
        public String toString() {
          return "Signature";
        }
      },
      DETAILS( ValueMetaInterface.TYPE_STRING ) {
        @Override
        public String toString() {
          return "Details";
        }
      },
      NOTES( ValueMetaInterface.TYPE_STRING ) {
        @Override
        public String toString() {
          return "Notes";
        }
      },
      ORGANIZATION_ID( ValueMetaInterface.TYPE_INTEGER ) {
        @Override
        public String toString() {
          return "Organization_ID";
        }
      },
      ROLE( ValueMetaInterface.TYPE_STRING ) {
        @Override
        public String toString() {
          return "Role";
        }
      },
      CUSTOM_ROLE_ID( ValueMetaInterface.TYPE_INTEGER ) {
        @Override
        public String toString() {
          return "Custom_Role_ID";
        }
      },
      MODERATOR( ValueMetaInterface.TYPE_BOOLEAN ) {
        @Override
        public String toString() {
          return "Is_Moderator";
        }
      },
      TICKET_RESTRICTION( ValueMetaInterface.TYPE_STRING ) {
        @Override
        public String toString() {
          return "Ticket_Restriction";
        }
      },
      ONLY_PRIVATE_COMMENTS( ValueMetaInterface.TYPE_BOOLEAN ) {
        @Override
        public String toString() {
          return "Is_Only_Private_Comments";
        }
      },
      TAGS( ValueMetaInterface.TYPE_STRING ) {
        @Override
        public String toString() {
          return "User_Tags";
        }
      },
      SUSPENDED( ValueMetaInterface.TYPE_BOOLEAN ) {
        @Override
        public String toString() {
          return "Is_Suspended";
        }
      },
      REMOTE_PHOTO_URL( ValueMetaInterface.TYPE_STRING ) {
        @Override
        public String toString() {
          return "Photo_URL";
        }
      },
      USER_FIELDS( ValueMetaInterface.TYPE_STRING ) {
        @Override
        public String toString() {
          return "User_Fields";
        }
      };

      private final int dataType;

      private Attribute( int type ) {
        this.dataType = type;
      }

      public int getValueMetaType() {
        return this.dataType;
      }

      public static Attribute getEnumFromValue( String name ) {
        if ( Const.isEmpty( name ) ) {
          return null;
        }
        for ( Attribute value : Attribute.values() ) {
          if ( value.toString().equals( name ) ) {
            return value;
          }
        }
        return null;
      }
    };
  }

  public static class IdentityField {
    private String name;
    private Attribute type;

    public IdentityField( String name, Attribute type ) {
      this.name = name;
      this.type = type;
    }

    public String getName() {
      return name;
    }

    public void setName(String name) {
      this.name = name;
    }

    public Attribute getType() {
      return type;
    }

    public void setType(Attribute type) {
      this.type = type;
    }

    public enum Attribute {
      USERID( ValueMetaInterface.TYPE_INTEGER ) {
        @Override
        public String toString() {
          return "User_ID";
        }
      },
      ID( ValueMetaInterface.TYPE_INTEGER ) {
        @Override
        public String toString() {
          return "Identity_ID";
        }
      },
      URL( ValueMetaInterface.TYPE_STRING ) {
        @Override
        public String toString() {
          return "Identity_URL";
        }
      },
      TYPE( ValueMetaInterface.TYPE_STRING ) {
        @Override
        public String toString() {
          return "Identity_Type";
        }
      },
      VALUE( ValueMetaInterface.TYPE_STRING ) {
        @Override
        public String toString() {
          return "Identity_Value";
        }
      },
      VERIFIED( ValueMetaInterface.TYPE_BOOLEAN ) {
        @Override
        public String toString() {
          return "Identity_Verified";
        }
      },
      PRIMARY( ValueMetaInterface.TYPE_BOOLEAN ) {
        @Override
        public String toString() {
          return "Identity_Primary";
        }
      },
      CREATED_AT( ValueMetaInterface.TYPE_DATE ) {
        @Override
        public String toString() {
          return "Identity_Created_Time";
        }
      },
      UPDATED_AT( ValueMetaInterface.TYPE_DATE ) {
        @Override
        public String toString() {
          return "Identity_Updated_Time";
        }
      };

      private final int dataType;

      private Attribute( int type ) {
        this.dataType = type;
      }

      public int getValueMetaType() {
        return this.dataType;
      }

      public static Attribute getEnumFromValue( String name ) {
        if ( Const.isEmpty( name ) ) {
          return null;
        }
        for ( Attribute value : Attribute.values() ) {
          if ( value.toString().equals( name ) ) {
            return value;
          }
        }
        return null;
      }
    };
  }
}
