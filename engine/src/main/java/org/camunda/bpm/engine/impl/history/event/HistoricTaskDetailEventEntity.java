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
package org.camunda.bpm.engine.impl.history.event;

import java.util.Date;

/**
 * @author Danny Gräf
 */
public class HistoricTaskDetailEventEntity extends HistoryEvent {

  protected String operationId;
  protected String operationType;
  protected String taskId;
  protected String userId;
  protected Date timeStamp;
  protected String stringValue;
  protected Date dateValue;

  public String getOperationId() {
    return operationId;
  }

  public String getOperationType() {
    return operationType;
  }

  public String getTaskId() {
    return taskId;
  }

  public String getUserId() {
    return userId;
  }

  public Date getTimeStamp() {
    return timeStamp;
  }

  public String getStringValue() {
    return stringValue;
  }

  public Date getDateValue() {
    return dateValue;
  }

  public void setOperationId(String operationId) {
    this.operationId = operationId;
  }

  public void setOperationType(String operationType) {
    this.operationType = operationType;
  }

  public void setTaskId(String taskId) {
    this.taskId = taskId;
  }

  public void setUserId(String userId) {
    this.userId = userId;
  }

  public void setTimeStamp(Date timeStamp) {
    this.timeStamp = timeStamp;
  }

  public void setStringValue(String stringValue) {
    this.stringValue = stringValue;
  }

  public void setDateValue(Date dateValue) {
    this.dateValue = dateValue;
  }

  @Override
  public String toString() {
    return this.getClass().getSimpleName()
        + "[taskId" + taskId
        + ", operationId =" + operationId
        + ", operationType =" + operationId
        + ", userId =" + userId
        + ", timeStamp =" + timeStamp
        + ", stringValue =" + stringValue
        + ", dateValue =" + dateValue
        + ", id=" + id
        + ", eventType=" + eventType
        + ", executionId=" + executionId
        + ", processDefinitionId=" + processDefinitionId
        + ", processInstanceId=" + processInstanceId
        + "]";
  }
}
