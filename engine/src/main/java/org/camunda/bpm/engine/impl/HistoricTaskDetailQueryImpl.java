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
package org.camunda.bpm.engine.impl;

import org.camunda.bpm.engine.history.HistoricTaskDetail;
import org.camunda.bpm.engine.history.HistoricTaskDetailQuery;
import org.camunda.bpm.engine.impl.interceptor.CommandContext;
import org.camunda.bpm.engine.impl.interceptor.CommandExecutor;

import java.util.Date;
import java.util.List;


/**
 * @author Danny Gräf
 */
public class HistoricTaskDetailQueryImpl extends AbstractQuery<HistoricTaskDetailQuery, HistoricTaskDetail> implements HistoricTaskDetailQuery {

  private static final long serialVersionUID = 1L;
  protected String processDefinitionId;
  protected String processInstanceId;
  protected String executionId;
  protected String taskId;
  protected String userId;
  protected Date timestamp;
  protected String operationId;
  protected String operationType;
  protected String property;

  public HistoricTaskDetailQueryImpl() {
  }

  public HistoricTaskDetailQueryImpl(CommandContext commandContext) {
    super(commandContext);
  }

  public HistoricTaskDetailQueryImpl(CommandExecutor commandExecutor) {
    super(commandExecutor);
  }

  public HistoricTaskDetailQuery processDefinitionId(String processDefinitionId) {
    this.processDefinitionId = processDefinitionId;
    return this;
  }

  public HistoricTaskDetailQuery processInstanceId(String processInstanceId) {
    this.processInstanceId = processInstanceId;
    return this;
  }

  public HistoricTaskDetailQuery executionId(String executionId) {
    this.executionId = executionId;
    return this;
  }

  public HistoricTaskDetailQuery taskId(String taskId) {
    this.taskId = taskId;
    return this;
  }

  public HistoricTaskDetailQuery userId(String userId) {
    this.userId = userId;
    return this;
  }

  public HistoricTaskDetailQuery timestamp(Date timestamp) {
    this.timestamp = timestamp;
    return this;
  }

  public HistoricTaskDetailQuery operationId(String operationId) {
    this.operationId = operationId;
    return this;
  }

  public HistoricTaskDetailQuery operationType(String operationType) {
    this.operationType = operationType;
    return this;
  }

  public HistoricTaskDetailQuery property(String property) {
    this.property = property;
    return this;
  }

  public long executeCount(CommandContext commandContext) {
    checkQueryOk();
    return commandContext
      .getHistoricTaskDetailManager()
      .findHistoricTaskDetailCountByQueryCriteria(this);
  }

  public List<HistoricTaskDetail> executeList(CommandContext commandContext, Page page) {
    checkQueryOk();
    return commandContext
        .getHistoricTaskDetailManager()
        .findHistoricTaskDetailsByQueryCriteria(this, page);
  }

  public String getTaskId() {
    return taskId;
  }

  public String getProcessInstanceId() {
    return processInstanceId;
  }
}
