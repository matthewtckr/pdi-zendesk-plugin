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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.map.AbstractLinkedMap;
import org.pentaho.di.core.Const;
import org.pentaho.di.core.exception.KettleValueException;
import org.zendesk.client.v2.model.Audit;
import org.zendesk.client.v2.model.events.CcEvent;
import org.zendesk.client.v2.model.events.ChangeEvent;
import org.zendesk.client.v2.model.events.CommentEvent;
import org.zendesk.client.v2.model.events.CommentPrivacyChangeEvent;
import org.zendesk.client.v2.model.events.CreateEvent;
import org.zendesk.client.v2.model.events.ErrorEvent;
import org.zendesk.client.v2.model.events.Event;
import org.zendesk.client.v2.model.events.ExternalEvent;
import org.zendesk.client.v2.model.events.NotificationEvent;
import org.zendesk.client.v2.model.events.VoiceCommentEvent;

public class ZendeskTicketAuditHistory implements Cloneable {

  private static final String DUE_AT_DATE_FORMAT_STRING = "yyyy-MM-dd HH:mm:ss";
  private static final SimpleDateFormat DUE_AT_DATE_FORMAT = new SimpleDateFormat( DUE_AT_DATE_FORMAT_STRING );

  Long ticketId;
  Long auditId;
  Date createdTime;
  Long organizationId;
  Long requesterId;
  Long assigneeId;
  Long groupId;
  String subject;
  List<Long> collaborators;
  List<String> tags;
  String status;
  String priority;
  String channel;
  String type;
  String satisfaction;

  Long locale;
  Date dueAt;
  String satisfactionComment;
  Long formId;
  Long brandId;
  Map<String, String> customFields;
  TicketAuditComment comment;

  public ZendeskTicketAuditHistory() {
  }

  public ZendeskTicketAuditHistory( Audit audit ) throws KettleValueException {
    this.ticketId = audit.getTicketId();
    this.auditId = audit.getId();
    this.createdTime = audit.getCreatedAt();
    this.customFields = new LinkedHashMap<String, String>();
    this.processEvents( audit.getEvents(), null );
  }

  @Override
  public ZendeskTicketAuditHistory clone() {
    ZendeskTicketAuditHistory cloned = new ZendeskTicketAuditHistory();
    cloned.ticketId = this.ticketId == null ? null : new Long( this.ticketId );
    cloned.createdTime = null;
    cloned.organizationId = this.organizationId == null ? null : new Long( this.organizationId );
    cloned.requesterId = this.requesterId == null ? null : new Long( this.requesterId );
    cloned.assigneeId = this.assigneeId == null ? null : new Long( this.assigneeId );
    cloned.groupId = this.groupId == null ? null : new Long( this.groupId );
    cloned.subject = this.subject;
    cloned.tags = this.tags == null ? null : new ArrayList<String>( this.tags );
    cloned.status = this.status;
    cloned.priority = this.priority;
    cloned.channel = this.channel;
    cloned.type = this.type;
    cloned.satisfaction = this.satisfaction;
    cloned.customFields = this.customFields == null ? new LinkedHashMap<String, String>() : new LinkedHashMap<String, String>( this.customFields );
    cloned.comment = null;
    return cloned;
  }
  public ZendeskTicketAuditHistory createNextAudit( Audit audit, AbstractLinkedMap<Long, ZendeskTicketAuditHistory> auditSummaries ) throws CloneNotSupportedException, KettleValueException {
    ZendeskTicketAuditHistory nextAudit = (ZendeskTicketAuditHistory) this.clone();
    nextAudit.auditId = audit.getId();
    nextAudit.createdTime = audit.getCreatedAt();
    nextAudit.processEvents( audit.getEvents(), auditSummaries );
    return nextAudit;
  }

  @SuppressWarnings( "unchecked" )
  void processEvents( List<Event> events, AbstractLinkedMap<Long, ZendeskTicketAuditHistory> auditSummaries ) throws KettleValueException {
    for ( Event event : events ) {
      String fieldType = event.getClass().getName();
      String fieldName = "";
      Object fieldValueObject = new Object();
      String fieldValue = "";

      if ( fieldType.equals( ExternalEvent.class.getName() ) ||
          fieldType.equals( NotificationEvent.class.getName() ) || fieldType.equals( ErrorEvent.class.getName() ) ||
          fieldType.equals( "OrganizationActivity" ) || fieldType.equals( "AttachmentRedactionEvent" ) ||
          fieldType.equals( "CommentRedactionEvent" ) ) {
        //TODO: Replace OrgActivity and RedactionEvents with class references, pending upstream project commit
        // Do nothing
      } else if ( fieldType.equals( CommentPrivacyChangeEvent.class.getName() ) ) {
        if ( auditSummaries != null ) {
          Long commentId = ( (CommentPrivacyChangeEvent) event ).getCommentId();
          Boolean commentVisibility = ( (CommentPrivacyChangeEvent) event ).getPublic();
          if ( commentId != null ) {
            for ( ZendeskTicketAuditHistory auditEntry : auditSummaries.values() ) {
              if ( auditEntry.comment != null && commentId.equals( auditEntry.comment.commentId ) ) {
                auditEntry.comment.publicComment = commentVisibility;
                auditEntry.comment.changedToPrivate = this.createdTime;
              }
            }
          }
        }
      } else if ( fieldType.equals( CreateEvent.class.getName() ) ) {
        fieldName = ( (CreateEvent) event ).getFieldName();
        fieldValueObject = ( (CreateEvent) event ).getValueObject();
        fieldValue = ( (CreateEvent) event ).getValue();
      } else if ( fieldType.equals( ChangeEvent.class.getName() ) ) {
        fieldName = ( (ChangeEvent) event ).getFieldName();
        fieldValueObject = ( (ChangeEvent) event ).getValueObject();
        fieldValue = ( (ChangeEvent) event ).getValue();
      } else if ( fieldType.equals( CommentEvent.class.getName() ) ) {
        this.comment = new TicketAuditComment( (CommentEvent) event );
      } else if ( fieldType.equals( VoiceCommentEvent.class.getName() ) ) {
        this.comment = new TicketAuditComment( (VoiceCommentEvent) event );
      } else if ( fieldType.equals( CcEvent.class.getName() ) ) {
        this.collaborators = ( (CcEvent) event ).getRecipients();
      }

      if ( !Const.isEmpty( fieldName ) ) {
        switch( fieldName ) {
          case "organization_id":
            this.organizationId = Const.isEmpty( fieldValue ) ? null : Long.valueOf( fieldValue );
            break;
          case "requester_id":
            this.requesterId = Const.isEmpty( fieldValue ) ? null : Long.valueOf( fieldValue );
            break;
          case "assignee_id":
            this.assigneeId = Const.isEmpty( fieldValue ) ? null : Long.valueOf( fieldValue );
            break;
          case "group_id":
            this.groupId = Const.isEmpty( fieldValue ) ? null : Long.valueOf( fieldValue );
            break;
          case "subject":
            this.subject = fieldValue;
            break;
          case "tags":
            this.tags = (fieldValueObject instanceof List ) ? (List<String>) fieldValueObject : null;
            break;
          case "status":
            this.status = fieldValue;
            break;
          case "priority":
            this.priority = fieldValue;
            break;
          case "channel":
            this.channel = fieldValue;
            break;
          case "type":
            this.type = fieldValue;
            break;
          case "satisfaction_score":
            this.satisfaction = fieldValue;
            break;
          case "brand_id":
            this.brandId = Long.valueOf( fieldValue );
            break;
          case "ticket_form_id":
            this.formId = Long.valueOf( fieldValue );
            break;
          case "locale_id":
            this.locale = Long.valueOf( fieldValue );
            break;
          case "satisfaction_comment":
            this.satisfactionComment = fieldValue;
            break;
          case "due_at":
            try {
              this.dueAt = DUE_AT_DATE_FORMAT.parse( fieldValue );
            } catch ( ParseException e ) {
              throw new KettleValueException( "Unable to parse [" + fieldValue + "] + with format [" + DUE_AT_DATE_FORMAT_STRING + "]", e );
            }
          case "current_collaborators":
            // Ignore, these are "pretty-printed emails",
            // and the CcEvent is used above instead
            break;
          default:
            this.customFields.put( fieldName, fieldValue );
            break;
        }
      }
    }
  }

  public class TicketAuditComment {
    Long commentId;
    Long authorId;
    Boolean publicComment;
    Date changedToPrivate;
    String commentBody;
    String commentHTMLBody;

    public TicketAuditComment( CommentEvent event ) {
      this.commentId = event.getId();
      this.authorId = event.getAuthorId();
      this.publicComment = event.getPublic();
      this.changedToPrivate = null;
      this.commentBody = event.getBody();
      this.commentHTMLBody = event.getHtmlBody();
    }

    public TicketAuditComment( VoiceCommentEvent event ) {
      this.commentId = event.getId();
      this.authorId = event.getAuthorId();
      this.publicComment = event.getPublic();
      this.changedToPrivate = null;
      this.commentBody = event.getBody();
      this.commentHTMLBody = event.getHtmlBody();
    }
  }
}
