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

package org.pentaho.di.trans.steps.zendesk;

import java.util.List;

import org.pentaho.di.core.Const;
import org.pentaho.di.core.annotations.Step;
import org.pentaho.di.core.database.DatabaseMeta;
import org.pentaho.di.core.exception.KettleException;
import org.pentaho.di.core.exception.KettleStepException;
import org.pentaho.di.core.exception.KettleXMLException;
import org.pentaho.di.core.row.RowMetaInterface;
import org.pentaho.di.core.row.value.ValueMetaInteger;
import org.pentaho.di.core.variables.VariableSpace;
import org.pentaho.di.core.xml.XMLHandler;
import org.pentaho.di.repository.ObjectId;
import org.pentaho.di.repository.Repository;
import org.pentaho.di.trans.Trans;
import org.pentaho.di.trans.TransMeta;
import org.pentaho.di.trans.step.StepDataInterface;
import org.pentaho.di.trans.step.StepInterface;
import org.pentaho.di.trans.step.StepMeta;
import org.pentaho.di.trans.step.StepMetaInterface;
import org.pentaho.metastore.api.IMetaStore;
import org.w3c.dom.Node;

@Step(  
    id = "ZendeskInputIncrementalTicket",
    image = "org/pentaho/di/trans/steps/zendesk/zendesk.png",
    i18nPackageName="org.pentaho.di.trans.steps.zendesk",
    name="ZendeskInputIncremental.Name",
    description = "ZendeskInputIncremental.TooltipDesc",
    categoryDescription="i18n:org.pentaho.di.trans.step:BaseStep.Category.Input"
)
public class ZendeskInputIncrementalMeta extends ZendeskInputMeta implements StepMetaInterface {

  public enum IncrementalType {
    TICKETS {
      @Override
      public String toString() {
        return "Tickets";
      }
    }, USERS {
      @Override
      public String toString() {
        return "Users";
      }
    }, ORGANIZATIONS {
      @Override
      public String toString() {
        return "Organizations";
      }
    }, HELPCENTER_ARTICLES {
      @Override
      public String toString() {
        return "HelpCenter Articles";
      }
    };
  }

  private IncrementalType downloadType;
  private String timestampFieldName;
  private String outputFieldName;

  @Override
  public StepInterface getStep( StepMeta stepMeta, StepDataInterface stepDataInterface, int copyNr,
      TransMeta transMeta, Trans trans ) {
    return new ZendeskInputIncremental( stepMeta, stepDataInterface, copyNr, transMeta, trans );
  }

  @Override
  public void setDefault() {
    super.setDefault();
    setDownloadType( IncrementalType.TICKETS );
  }

  public IncrementalType getDownloadType() {
    return downloadType;
  }

  public void setDownloadType( IncrementalType it ) {
    this.downloadType = it;
  }

  public String getTimestampFieldName() {
    return timestampFieldName;
  }

  public void setTimestampFieldName( String timestampFieldName ) {
    this.timestampFieldName = timestampFieldName;
  }

  public String getOutputFieldName() {
    return outputFieldName;
  }

  public void setOutputFieldName( String outputFieldName ) {
    this.outputFieldName = outputFieldName;
  }

  @Override
  public String getXML() throws KettleException {
    StringBuilder builder = new StringBuilder();
    builder.append( super.getXML() );
    builder.append( "    " ).append( XMLHandler.addTagValue( "downloadType",
      getDownloadType() == null ? null : getDownloadType().name() ) );
    builder.append( "    " ).append( XMLHandler.addTagValue( "timestampFieldName", getTimestampFieldName() ) );
    builder.append( "    " ).append( XMLHandler.addTagValue( "outputFieldName", getOutputFieldName() ) );
    return builder.toString();
  }

  @Override
  public void loadXML( Node stepnode, List<DatabaseMeta> databases, IMetaStore metaStore ) throws KettleXMLException {
    super.loadXML( stepnode, databases, metaStore );
    String downloadTypeValue = XMLHandler.getTagValue( stepnode, "downloadType" );
    if ( !Const.isEmpty( downloadTypeValue ) ) {
      setDownloadType( IncrementalType.valueOf( downloadTypeValue ) );
    } else {
      setDownloadType( null );
    }
    setTimestampFieldName( XMLHandler.getTagValue( stepnode, "timestampFieldName" ) );
    setOutputFieldName( XMLHandler.getTagValue( stepnode, "outputFieldName" ) );
  }

  @Override
  public void readRep( Repository rep, IMetaStore metaStore, ObjectId id_step, List<DatabaseMeta> databases )
    throws KettleException {
    super.readRep( rep, metaStore, id_step, databases );
    String downloadTypeValue = rep.getStepAttributeString( id_step, "downloadType" );
    if ( !Const.isEmpty( downloadTypeValue ) ) {
      setDownloadType( IncrementalType.valueOf( downloadTypeValue ) );
    } else {
      setDownloadType( null );
    }
    setTimestampFieldName( rep.getStepAttributeString( id_step, "timestampFieldName" ) );
    setOutputFieldName( rep.getStepAttributeString( id_step, "outputFieldName" ) );
  }

  @Override
  public void saveRep( Repository rep, IMetaStore metaStore, ObjectId id_transformation, ObjectId id_step )
    throws KettleException {
    super.saveRep( rep, metaStore, id_transformation, id_step );
    rep.saveStepAttribute( id_transformation, id_step, "downloadType",
      getDownloadType() == null ? null : getDownloadType().name() );
    rep.saveStepAttribute( id_transformation, id_step, "timestampFieldName", getTimestampFieldName() );
    rep.saveStepAttribute( id_transformation, id_step, "outputFieldName", getOutputFieldName() );
  }

  @Override
  public StepDataInterface getStepData() {
    return new ZendeskInputIncrementalData();
  }

  @Override
  public void getFields( RowMetaInterface inputRowMeta, String name, RowMetaInterface[] info, StepMeta nextStep,
      VariableSpace space, Repository repository, IMetaStore metaStore ) throws KettleStepException {
    super.getFields( inputRowMeta, name, info, nextStep, space, repository, metaStore );
    inputRowMeta.clear();
    inputRowMeta.addValueMeta( new ValueMetaInteger( space.environmentSubstitute( getOutputFieldName() ) ) );
  }

}
