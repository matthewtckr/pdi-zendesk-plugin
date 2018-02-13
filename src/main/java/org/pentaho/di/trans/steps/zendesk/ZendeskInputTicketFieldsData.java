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

import org.pentaho.di.core.row.RowMetaInterface;
import org.pentaho.di.trans.steps.zendesk.ZendeskInputTicketFieldsMeta.TicketField.Attribute;
import org.zendesk.client.v2.model.Field;

public class ZendeskInputTicketFieldsData extends ZendeskInputData {

  RowMetaInterface rowMeta;

  public Object getValue( Field field, Attribute attribute ) {
    if ( field == null || attribute == null ) {
      return null;
    }
    switch ( attribute ) {
      case ID:
        return field.getId();
      case URL:
        return field.getUrl();
      case TYPE:
        return field.getType();
      case TITLE:
        return field.getTitle();
      case RAW_TITLE:
        return field.getRawTitle();
      case DESCRIPTION:
        return field.getDescription();
      case RAW_DESCRIPTION:
        return field.getRawDescription();
      case POSITION:
    	return Long.valueOf( field.getPosition() );
      case ACTIVE:
        return field.getActive();
      case REQUIRED:
        return field.getRequired();
      case COLLAPSED_FOR_AGENTS:
    	return field.getCollapsedForAgents();
      case REGEXP_FOR_VALIDATION:
    	return field.getRegexpForValidation();
      case TITLE_IN_PORTAL:
    	return field.getTitleInPortal();
      case RAW_TITLE_IN_PORTAL:
    	return field.getRawTitleInPortal();
      case VISIBLE_IN_PORTAL:
        return field.getVisibleInPortal();
      case EDITABLE_IN_PORTAL:
    	return field.getEditableInPortal();
      case REQUIRED_IN_PORTAL:
    	return field.getRequiredInPortal();
      case TAG:
    	return field.getTag();
      case CREATED_AT:
        return field.getCreatedAt();
      case UPDATED_AT:
        return field.getUpdatedAt();
      case REMOVABLE:
    	return field.getRemovable();
	  default:
		return null;
    }
  }
}
