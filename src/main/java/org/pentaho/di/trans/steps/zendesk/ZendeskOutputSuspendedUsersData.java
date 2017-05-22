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

import org.pentaho.di.core.row.RowMetaInterface;
import org.zendesk.client.v2.model.User;

public class ZendeskOutputSuspendedUsersData extends ZendeskInputData {

  int userFieldNameIndex;
  int actionFieldNameIndex;
  int resultFieldNameIndex;
  RowMetaInterface outputRowMeta;

  public boolean suspendUser( long userId, boolean action ) {
    User user = new User();
    user.setId( userId );
    user.setSuspended( action );
    User result = conn.updateUser( user );
    return ( result.getSuspended() == action );
  }
}
