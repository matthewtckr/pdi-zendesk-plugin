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
import org.pentaho.di.repository.ObjectId;
import org.pentaho.di.repository.Repository;
import org.pentaho.di.trans.Trans;
import org.pentaho.di.trans.TransMeta;
import org.pentaho.di.trans.step.StepDataInterface;
import org.pentaho.di.trans.step.StepInterface;
import org.pentaho.di.trans.step.StepMeta;
import org.pentaho.di.trans.steps.zendesk.ZendeskInputTicketFieldsMeta.TicketField.Attribute;
import org.pentaho.metastore.api.IMetaStore;
import org.w3c.dom.Node;

@Step(
    id = "ZendeskInputTicketFields",
    image = "org/pentaho/di/trans/steps/zendesk/zendesk.png",
    i18nPackageName = "org.pentaho.di.trans.steps.zendesk",
    name = "ZendeskInputTicketFields.Name",
    description = "ZendeskInputTicketFields.TooltipDesc",
    categoryDescription = "i18n:org.pentaho.di.trans.step:BaseStep.Category.Input"
  )
public class ZendeskInputTicketFieldsMeta extends ZendeskInputMeta {

  private TicketField[] ticketFields;

  public TicketField[] getTicketFields() {
    return ticketFields;
  }

  public void setTicketFields( TicketField[] fields ) {
    this.ticketFields = fields;
  }

  public void allocate( int fieldCount ) {
    ticketFields = new TicketField[fieldCount];
  }

  @Override
  public void getFields( RowMetaInterface inputRowMeta, String name, RowMetaInterface[] info, StepMeta nextStep,
      VariableSpace space, Repository repository, IMetaStore metaStore ) throws KettleStepException {
    super.getFields( inputRowMeta, name, info, nextStep, space, repository, metaStore );
    if ( ticketFields != null ) {
      for ( int i = 0; i < ticketFields.length; i++ ) {
        addFieldToRow( inputRowMeta,
          space.environmentSubstitute( ticketFields[i].getName() ),
          ticketFields[i].getType().getValueMetaType() );
      }
    }
  }

  @Override
  public StepInterface getStep( StepMeta stepMeta, StepDataInterface stepDataInterface, int copyNr,
      TransMeta transMeta, Trans trans ) {
    return new ZendeskInputTicketFields( stepMeta, stepDataInterface, copyNr, transMeta, trans );
  }

  @Override
  public StepDataInterface getStepData() {
    return new ZendeskInputTicketFieldsData();
  }

  @Override
  public String getXML() throws KettleException {
    StringBuilder xml = new StringBuilder();
    xml.append( super.getXML() );
    xml.append( "    " ).append( XMLHandler.openTag( "fields" ) ).append( Const.CR );
    if ( ticketFields != null ) {
      for ( int i = 0; i < ticketFields.length; i++ ) {
        xml.append( "      " ).append( XMLHandler.openTag( "field" ) ).append( Const.CR );
        xml.append( "        " ).append( XMLHandler.addTagValue( "name", ticketFields[i].getName() ) );
        xml.append( "        " ).append( XMLHandler.addTagValue( "type", ticketFields[i].getType().name() ) );
        xml.append( "      " ).append( XMLHandler.closeTag( "field" ) ).append( Const.CR );
      }
    }
    xml.append( "    " ).append( XMLHandler.closeTag( "fields" ) ).append( Const.CR );
    return xml.toString();
  }

  @Override
  public void loadXML( Node stepnode, List<DatabaseMeta> databases, IMetaStore metaStore ) throws KettleXMLException {
    super.loadXML( stepnode, databases, metaStore );
    Node fieldsNode = XMLHandler.getSubNode( stepnode, "fields" );
    if ( fieldsNode != null ) {
      int fieldCount = XMLHandler.countNodes( fieldsNode, "field" );
      allocate( fieldCount );
      for ( int i = 0; i < fieldCount; i++ ) {
        Node fieldNode = XMLHandler.getSubNodeByNr( fieldsNode, "field", i );
        String name = XMLHandler.getTagValue( fieldNode, "name" );
        Attribute type = Attribute.valueOf( XMLHandler.getTagValue( fieldNode, "type" ) );
        if ( type != null ) {
          ticketFields[i] = new TicketField( name, type );
        }
      }
    } else {
      // Legacy XML Conversion
      List<TicketField> tempFields = new ArrayList<>();
      String temp = null;
      temp = XMLHandler.getTagValue( stepnode, "ticketFieldIdFieldname" );
      if ( temp != null ) {
        tempFields.add( new TicketField( temp, Attribute.ID ) );
      }
      temp = XMLHandler.getTagValue( stepnode, "ticketFieldUrlFieldname" );
      if ( temp != null ) {
        tempFields.add( new TicketField( temp, Attribute.URL ) );
      }
      temp = XMLHandler.getTagValue( stepnode, "ticketFieldTypeFieldname" );
      if ( temp != null ) {
        tempFields.add( new TicketField( temp, Attribute.TYPE ) );
      }
      temp = XMLHandler.getTagValue( stepnode, "ticketFieldTitleFieldname" );
      if ( temp != null ) {
        tempFields.add( new TicketField( temp, Attribute.TITLE ) );
      }
      temp = XMLHandler.getTagValue( stepnode, "ticketFieldActiveFieldname" );
      if ( temp != null ) {
        tempFields.add( new TicketField( temp, Attribute.ACTIVE ) );
      }
      temp = XMLHandler.getTagValue( stepnode, "ticketFieldRequiredFieldname" );
      if ( temp != null ) {
        tempFields.add( new TicketField( temp, Attribute.REQUIRED ) );
      }
      temp = XMLHandler.getTagValue( stepnode, "ticketFieldVisibleEndUsersFieldname" );
      if ( temp != null ) {
        tempFields.add( new TicketField( temp, Attribute.VISIBLE_IN_PORTAL ) );
      }
      temp = XMLHandler.getTagValue( stepnode, "createdAtFieldname" );
      if ( temp != null ) {
        tempFields.add( new TicketField( temp, Attribute.CREATED_AT ) );
      }
      temp = XMLHandler.getTagValue( stepnode, "updatedAtFieldname" );
      if ( temp != null ) {
        tempFields.add( new TicketField( temp, Attribute.UPDATED_AT ) );
      }
      ticketFields = tempFields.toArray( new TicketField[0] );
    }
  }

  @Override
  public void readRep( Repository rep, IMetaStore metaStore, ObjectId id_step, List<DatabaseMeta> databases )
    throws KettleException {
    super.readRep( rep, metaStore, id_step, databases );

    int fieldCount = rep.countNrStepAttributes( id_step, "fieldName" );
    if ( fieldCount > 0 ) {
      allocate( fieldCount );
      for ( int i = 0; i < fieldCount; i++ ) {
        String name = rep.getStepAttributeString( id_step, i, "fieldName" );
        Attribute type = Attribute.valueOf( rep.getStepAttributeString( id_step, i, "fieldType" ) );
        ticketFields[i] = new TicketField( name, type );
      }
    } else {
      // Legacy Repository Conversion
      List<TicketField> tempFields = new ArrayList<>();
      String temp = null;
      temp = rep.getStepAttributeString( id_step, "ticketFieldIdFieldname" );
      if ( temp != null ) {
        tempFields.add( new TicketField( temp, Attribute.ID ) );
      }
      temp = rep.getStepAttributeString( id_step, "ticketFieldUrlFieldname" );
      if ( temp != null ) {
        tempFields.add( new TicketField( temp, Attribute.URL ) );
      }
      temp = rep.getStepAttributeString( id_step, "ticketFieldTypeFieldname" );
      if ( temp != null ) {
        tempFields.add( new TicketField( temp, Attribute.TYPE ) );
      }
      temp = rep.getStepAttributeString( id_step, "ticketFieldTitleFieldname" );
      if ( temp != null ) {
        tempFields.add( new TicketField( temp, Attribute.TITLE ) );
      }
      temp = rep.getStepAttributeString( id_step, "ticketFieldActiveFieldname" );
      if ( temp != null ) {
        tempFields.add( new TicketField( temp, Attribute.ACTIVE ) );
      }
      temp = rep.getStepAttributeString( id_step, "ticketFieldRequiredFieldname" );
      if ( temp != null ) {
        tempFields.add( new TicketField( temp, Attribute.REQUIRED ) );
      }
      temp = rep.getStepAttributeString( id_step, "ticketFieldVisibleEndUsersFieldname" );
      if ( temp != null ) {
        tempFields.add( new TicketField( temp, Attribute.VISIBLE_IN_PORTAL ) );
      }
      temp = rep.getStepAttributeString( id_step, "createdAtFieldname" );
      if ( temp != null ) {
        tempFields.add( new TicketField( temp, Attribute.CREATED_AT ) );
      }
      temp = rep.getStepAttributeString( id_step, "updatedAtFieldname" );
      if ( temp != null ) {
        tempFields.add( new TicketField( temp, Attribute.UPDATED_AT ) );
      }
      ticketFields = tempFields.toArray( new TicketField[0] );
    }
  }

  @Override
  public void saveRep( Repository rep, IMetaStore metaStore, ObjectId id_transformation, ObjectId id_step )
    throws KettleException {
    super.saveRep( rep, metaStore, id_transformation, id_step );
    if ( ticketFields != null ) {
      for ( int i = 0; i < ticketFields.length; i++ ) {
        rep.saveStepAttribute( id_transformation, id_step, i, "fieldName", ticketFields[i].getName() );
        rep.saveStepAttribute( id_transformation, id_step, i, "fieldType", ticketFields[i].getType().name() );
      }
    }
  }

  @Override
  public void setDefault() {
    super.setDefault();
    Attribute[] values = Attribute.values();
    allocate(values.length);
    for ( int i = 0; i < values.length; i++ ) {
      ticketFields[i] = new TicketField( values[i].toString(), values[i] );
    }
  }


  public static class TicketField {
    private String name;
    private Attribute type;

    public TicketField( String name, Attribute type ) {
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
      ID( ValueMetaInterface.TYPE_INTEGER ) {
        @Override
        public String toString() {
          return "ID";
        }
      },
      URL( ValueMetaInterface.TYPE_STRING ) {
        @Override
        public String toString() {
          return "URL";
        }
      },
      TYPE( ValueMetaInterface.TYPE_STRING ) {
        @Override
        public String toString() {
         return "Type";
        }
      },
      TITLE( ValueMetaInterface.TYPE_STRING ) {
        @Override
        public String toString() {
          return "Title";
        }
      },
      RAW_TITLE( ValueMetaInterface.TYPE_STRING ) {
        @Override
        public String toString() {
          return "Raw Title";
        }
      },
      DESCRIPTION( ValueMetaInterface.TYPE_STRING ) {
        @Override
        public String toString() {
          return "Description";
        }
      },
      RAW_DESCRIPTION( ValueMetaInterface.TYPE_STRING ) {
        @Override
        public String toString() {
          return "Raw Description";
        }
      },
      POSITION( ValueMetaInterface.TYPE_INTEGER ) {
        @Override
        public String toString() {
          return "Position";
        }
      },
      ACTIVE( ValueMetaInterface.TYPE_BOOLEAN ) {
        @Override
        public String toString() {
          return "Is Active";
        }
      },
      REQUIRED( ValueMetaInterface.TYPE_BOOLEAN ) {
        @Override
        public String toString() {
          return "Is Required";
        }
      },
      COLLAPSED_FOR_AGENTS( ValueMetaInterface.TYPE_BOOLEAN ) {
        @Override
        public String toString() {
          return "Collapsed for Agents";
        }
      },
      REGEXP_FOR_VALIDATION( ValueMetaInterface.TYPE_STRING ) {
        @Override
        public String toString() {
          return "RegExp for Validation";
        }
      },
      TITLE_IN_PORTAL( ValueMetaInterface.TYPE_STRING ) {
        @Override
        public String toString() {
          return "Title in Portal";
        }
      },
      RAW_TITLE_IN_PORTAL( ValueMetaInterface.TYPE_STRING ) {
        @Override
        public String toString() {
          return "Raw Title in Portal";
        }
      },
      VISIBLE_IN_PORTAL( ValueMetaInterface.TYPE_BOOLEAN ) {
        @Override
        public String toString() {
          return "Is Visible for End Users";
        }
      },
      EDITABLE_IN_PORTAL( ValueMetaInterface.TYPE_BOOLEAN ) {
        @Override
        public String toString() {
          return "Editable in Portal";
        }
      },
      REQUIRED_IN_PORTAL( ValueMetaInterface.TYPE_BOOLEAN ) {
        @Override
        public String toString() {
          return "Required in Portal";
        }
      },
      TAG( ValueMetaInterface.TYPE_STRING ) {
        @Override
        public String toString() {
          return "Tag";
        }
      },
      CREATED_AT( ValueMetaInterface.TYPE_DATE ) {
        @Override
        public String toString() {
          return "Created At";
        }
      },
      UPDATED_AT( ValueMetaInterface.TYPE_DATE ) {
        @Override
        public String toString() {
          return "Updated At";
        }
      },
      REMOVABLE( ValueMetaInterface.TYPE_BOOLEAN ) {
        @Override
        public String toString() {
          return "Removable";
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
    }
  }
}
