/* Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.camunda.bpm.engine.history;

import java.util.Date;


/**
 * Represents a historic task detail that is stored permanent as a task revision / activity log.
 *
 * @author Danny Gräf
 */
public interface HistoricTaskDetail {

  /** The unique identifier of this historic task detail. */
  String getId();

  /** Process definition reference. */
  String getProcessDefinitionId();

  /** Process instance reference. */
  String getProcessInstanceId();

  /** Execution reference. */
  String getExecutionId();

  /** Task instance reference. */
  String getTaskId();

  /** User that operates. */
  String getUserId();

  /** Timestamp of this change. */
  Date getTimestamp();

  /** The unique identifier of this operation, that identifies multiple changes. */
  String getOperationId();

  /** Type of this operation, like create, assign, claim or something else (enum usage?). */
  String getOperationType();

  /** The property changed by this operation. */
  String getProperty();

  /** The new value of the property. */
  String getValue();

}
