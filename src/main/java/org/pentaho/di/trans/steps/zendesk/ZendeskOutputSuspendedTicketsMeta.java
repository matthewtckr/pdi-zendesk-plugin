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

import java.util.List;

import org.pentaho.di.core.CheckResult;
import org.pentaho.di.core.CheckResultInterface;
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
import org.pentaho.di.trans.step.StepInterface;
import org.pentaho.di.trans.step.StepMeta;
import org.pentaho.metastore.api.IMetaStore;
import org.w3c.dom.Node;

@Step(
    id = "ZendeskOutputSuspendedTickets",
    image = "org/pentaho/di/trans/steps/zendesk/zendesk.png",
    i18nPackageName = "org.pentaho.di.trans.steps.zendesk",
    name = "ZendeskOutputSuspendedTickets.Name",
    description = "ZendeskOutputSuspendedTickets.TooltipDesc",
    categoryDescription = "i18n:org.pentaho.di.trans.step:BaseStep.Category.Output"
  )
public class ZendeskOutputSuspendedTicketsMeta extends ZendeskInputMeta {

  public enum SuspendedTicketAction {
    DELETE {
      @Override
      public String toString() {
        return "Delete";
      }
    };
  }

  private static final Class<?> PKG = ZendeskOutputSuspendedTicketsMeta.class;

  private String ticketFieldName;
  private String resultFieldName;
  private SuspendedTicketAction action;

  public String getTicketFieldName() {
    return ticketFieldName;
  }

  public void setTicketFieldName( String ticketFieldName ) {
    this.ticketFieldName = ticketFieldName;
  }

  public String getResultFieldName() {
    return resultFieldName;
  }

  public void setResultFieldName( String resultFieldName ) {
    this.resultFieldName = resultFieldName;
  }

  public SuspendedTicketAction getAction() {
    return action;
  }

  public void setAction( SuspendedTicketAction action ) {
    this.action = action;
  }

  @Override
  public String getXML() throws KettleException {
    StringBuilder builder = new StringBuilder();
    builder.append( super.getXML() );
    builder.append( "    " ).append( XMLHandler.addTagValue( "action",
      getAction() == null ? null : getAction().name() ) );
    builder.append( "    " ).append( XMLHandler.addTagValue( "ticketFieldName", getTicketFieldName() ) );
    builder.append( "    " ).append( XMLHandler.addTagValue( "resultFieldName", getResultFieldName() ) );
    return builder.toString();
  }

  @Override
  public void loadXML( Node stepnode, List<DatabaseMeta> databases, IMetaStore metaStore ) throws KettleXMLException {
    super.loadXML( stepnode, databases, metaStore );
    setTicketFieldName( XMLHandler.getTagValue( stepnode, "ticketFieldName" ) );
    setResultFieldName( XMLHandler.getTagValue( stepnode, "resultFieldName" ) );
    String actionValue = XMLHandler.getTagValue( stepnode, "action" );
    if ( !Const.isEmpty( actionValue ) ) {
      setAction( SuspendedTicketAction.valueOf( actionValue ) );
    } else {
      setAction( null );
    }
  }

  @Override
  public void readRep( Repository rep, IMetaStore metaStore, ObjectId id_step, List<DatabaseMeta> databases )
      throws KettleException {
    super.readRep( rep, metaStore, id_step, databases );
    setTicketFieldName( rep.getStepAttributeString( id_step, "ticketFieldName" ) );
    setResultFieldName( rep.getStepAttributeString( id_step, "resultFieldName" ) );
    String actionValue = rep.getStepAttributeString( id_step, "action" );
    if ( !Const.isEmpty( actionValue ) ) {
      setAction( SuspendedTicketAction.valueOf( actionValue ) );
    } else {
      setAction( null );
    }
  }

  @Override
  public void saveRep( Repository rep, IMetaStore metaStore, ObjectId id_transformation, ObjectId id_step )
      throws KettleException {
    super.saveRep( rep, metaStore, id_transformation, id_step );
    rep.saveStepAttribute( id_transformation, id_step, "ticketFieldName", getTicketFieldName() );
    rep.saveStepAttribute( id_transformation, id_step, "resultFieldName", getResultFieldName() );
    rep.saveStepAttribute( id_transformation, id_step, "action", getAction() == null ? null : getAction().name() );
  }

  @Override
  public void check( List<CheckResultInterface> remarks, TransMeta transMeta, StepMeta stepMeta, RowMetaInterface prev,
      String[] input, String[] output, RowMetaInterface info, VariableSpace space, Repository repository,
      IMetaStore metaStore ) {
    super.check( remarks, transMeta, stepMeta, prev, input, output, info, space, repository, metaStore );
    if ( Const.isEmpty( getTicketFieldName() ) ) {
      remarks.add(
        new CheckResult( CheckResult.TYPE_RESULT_ERROR,
          BaseMessages.getString( PKG, "ZendeskOutputSuspendedTicketsMeta.CheckResult.ErrorNoTicketField" ),
          stepMeta ) );
    } else if ( prev.indexOfValue( getTicketFieldName() ) >= 0 ) {
      if ( ValueMetaInterface.TYPE_INTEGER == prev.getValueMeta(
          prev.indexOfValue( getTicketFieldName() ) ).getType() ) {
        remarks.add(
          new CheckResult( CheckResult.TYPE_RESULT_OK,
            BaseMessages.getString( PKG, "ZendeskOutputSuspendedTicketsMeta.CheckResult.OkTicketField" ),
            stepMeta ) );
      } else {
        remarks.add(
          new CheckResult( CheckResult.TYPE_RESULT_ERROR,
            BaseMessages.getString( PKG, "ZendeskOutputSuspendedTicketsMeta.CheckResult.ErrorTicketFieldWrongType" ),
            stepMeta ) );
      }
    } else {
      remarks.add(
        new CheckResult( CheckResult.TYPE_RESULT_ERROR,
          BaseMessages.getString( PKG, "ZendeskOutputSuspendedTicketsMeta.CheckResult.ErrorTicketFieldNotInRow" ),
          stepMeta ) );
    }

    if ( Const.isEmpty( getResultFieldName() ) ) {
      remarks.add(
        new CheckResult( CheckResult.TYPE_RESULT_WARNING,
          BaseMessages.getString( PKG, "ZendeskOutputSuspendedTicketsMeta.CheckResult.ErrorNoResultField" ),
          stepMeta ) );
    }
  }

  @Override
  public void getFields( RowMetaInterface inputRowMeta, String name, RowMetaInterface[] info, StepMeta nextStep,
      VariableSpace space, Repository repository, IMetaStore metaStore ) throws KettleStepException {
    super.getFields( inputRowMeta, name, info, nextStep, space, repository, metaStore );
    addFieldToRow( inputRowMeta, space.environmentSubstitute( getResultFieldName() ), ValueMetaInterface.TYPE_BOOLEAN );
  }

  @Override
  public StepInterface getStep( StepMeta stepMeta, StepDataInterface stepDataInterface, int copyNr,
      TransMeta transMeta, Trans trans ) {
    return new ZendeskOutputSuspendedTickets( stepMeta, stepDataInterface, copyNr, transMeta, trans );
  }

  @Override
  public StepDataInterface getStepData() {
    return new ZendeskOutputSuspendedTicketsData();
  }

}
