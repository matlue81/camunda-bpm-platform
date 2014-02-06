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
package org.camunda.bpm.engine.test.history;

import org.camunda.bpm.engine.history.HistoricTaskDetail;
import org.camunda.bpm.engine.history.HistoricTaskDetailQuery;
import org.camunda.bpm.engine.impl.cfg.ProcessEngineConfigurationImpl;
import org.camunda.bpm.engine.impl.interceptor.Command;
import org.camunda.bpm.engine.impl.interceptor.CommandContext;
import org.camunda.bpm.engine.impl.persistence.entity.TaskEntity;
import org.camunda.bpm.engine.impl.test.PluggableProcessEngineTestCase;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.task.DelegationState;
import org.camunda.bpm.engine.task.Task;
import org.camunda.bpm.engine.test.Deployment;

import static org.camunda.bpm.engine.history.HistoricTaskDetail.*;

import java.util.Date;
import java.util.Map;

/**
 * @author Danny Gräf
 */
public class HistoricTaskDetailTest extends PluggableProcessEngineTestCase {

  private ProcessInstance process;
  private Task task;

  public void testTaskEntityBeanPropertyChanges() {
    TaskEntity entity = new TaskEntity();

    // assign and validate changes
    entity.setAssignee("icke");
    Map<String, Object> changes = entity.getPropertyChanges();
    assertEquals(1, changes.size());
    assertEquals("icke", changes.get("assignee").toString());

    // assign it again
    entity.setAssignee("er");
    changes = entity.getPropertyChanges();
    assertEquals(1, changes.size());
    assertEquals("er", changes.get("assignee").toString());

    // set a due date
    entity.setDueDate(new Date());
    changes = entity.getPropertyChanges();
    assertEquals(2, changes.size());
  }

  public void testDeleteTask() {
    // given: a single task
    task = taskService.newTask();
    taskService.saveTask(task);

    // then: delete the task
    taskService.deleteTask(task.getId(), "duplicated");

    // expect: one entry for the deletion
    HistoricTaskDetailQuery query = queryOperationDetails(OPERATION_TYPE_DELETE);
    assertEquals(1, query.count());

    // assert: details
    HistoricTaskDetail delete = query.singleResult();
    assertEquals("delete", delete.getProperty());
    assertTrue(Boolean.parseBoolean(delete.getValue()));

    cleanupHistory();
  }

  @Deployment(resources = {"org/camunda/bpm/engine/test/history/HistoryLevelTest.bpmn20.xml"})
  public void testCreateAndCompleteTask() {
    startTestProcess();

    // expect: one entry for the task creation
    HistoricTaskDetailQuery query = historyService.createHistoricTaskDetailQuery();
    assertEquals(1, query.count());

    // assert: all details
    HistoricTaskDetail create = query.singleResult();
    assertNotNull(create.getId());
    assertNotNull(create.getProcessDefinitionId());
    assertNotNull(create.getProcessInstanceId());
    assertNotNull(create.getExecutionId());
    assertNull(create.getUserId());
    assertEquals(task.getId(), create.getTaskId());
    assertEquals(OPERATION_TYPE_CREATE, create.getOperationType());
    assertNotNull(create.getOperationId());
    assertTrue(create.getTimestamp().before(new Date()));
    assertEquals("name", create.getProperty());
    assertEquals("a task", create.getValue());

    completeProcess();

    // expect: one entry for the task completion
    query = queryOperationDetails(OPERATION_TYPE_COMPLETE);
    assertEquals(1, query.count());
    HistoricTaskDetail complete = query.singleResult();
    assertEquals("delete", complete.getProperty());
    assertTrue(Boolean.parseBoolean(complete.getValue()));
  }

  @Deployment(resources = {"org/camunda/bpm/engine/test/history/HistoryLevelTest.bpmn20.xml"})
  public void testAssignTask() {
    startTestProcess();

    // then: assign the task
    taskService.setAssignee(task.getId(), "icke");

    // expect: one entry for the task assignment
    HistoricTaskDetailQuery query = queryOperationDetails(OPERATION_TYPE_ASSIGN);
    assertEquals(1, query.count());

    // assert: details
    HistoricTaskDetail assign = query.singleResult();
    assertEquals("assignee", assign.getProperty());
    assertEquals("icke", assign.getValue());

    completeProcess();
  }

  @Deployment(resources = {"org/camunda/bpm/engine/test/history/HistoryLevelTest.bpmn20.xml"})
  public void testChangeTaskOwner() {
    startTestProcess();

    // then: change the task owner
    taskService.setOwner(task.getId(), "icke");

    // expect: one entry for the owner change
    HistoricTaskDetailQuery query = queryOperationDetails(OPERATION_TYPE_SET_OWNER);
    assertEquals(1, query.count());

    // assert: details
    HistoricTaskDetail change = query.singleResult();
    assertEquals("owner", change.getProperty());
    assertEquals("icke", change.getValue());

    completeProcess();
  }

  @Deployment(resources = {"org/camunda/bpm/engine/test/history/HistoryLevelTest.bpmn20.xml"})
  public void testClaimTask() {
    startTestProcess();

    // then: claim a new the task
    taskService.claim(task.getId(), "icke");

    // expect: one entry for the claim
    HistoricTaskDetailQuery query = queryOperationDetails(OPERATION_TYPE_CLAIM);
    assertEquals(1, query.count());

    // assert: details
    HistoricTaskDetail claim = query.singleResult();
    assertEquals("assignee", claim.getProperty());
    assertEquals("icke", claim.getValue());

    completeProcess();
  }

  @Deployment(resources = {"org/camunda/bpm/engine/test/history/HistoryLevelTest.bpmn20.xml"})
  public void testDelegateTask() {
    startTestProcess();

    // then: delegate the assigned task
    taskService.claim(task.getId(), "icke");
    taskService.delegateTask(task.getId(), "er");

    // expect: three entries for the delegation
    HistoricTaskDetailQuery query = queryOperationDetails(OPERATION_TYPE_DELEGATE);
    assertEquals(3, query.count());

    // assert: details
    assertEquals("icke", queryOperationDetails(OPERATION_TYPE_DELEGATE, "owner").singleResult().getValue());
    assertEquals("er", queryOperationDetails(OPERATION_TYPE_DELEGATE, "assignee").singleResult().getValue());
    assertEquals(DelegationState.PENDING.toString(), queryOperationDetails(OPERATION_TYPE_DELEGATE, "delegation").singleResult().getValue());

    completeProcess();
  }

  @Deployment(resources = {"org/camunda/bpm/engine/test/history/HistoryLevelTest.bpmn20.xml"})
  public void testResolveTask() {
    startTestProcess();

    // then: resolve the task
    taskService.resolveTask(task.getId());

    // expect: one entry for the resolving
    HistoricTaskDetailQuery query = queryOperationDetails(OPERATION_TYPE_RESOLVE);
    assertEquals(1, query.count());

    // assert: details
    assertEquals(DelegationState.RESOLVED.toString(), query.singleResult().getValue());

    completeProcess();
  }

  private void startTestProcess() {
    processEngineConfiguration.setHistoryLevel(ProcessEngineConfigurationImpl.HISTORYLEVEL_FULL);
    process = runtimeService.startProcessInstanceByKey("HistoryLevelTest");
    task = taskService.createTaskQuery().singleResult();
  }

  private HistoricTaskDetailQuery queryOperationDetails(String type) {
    return historyService.createHistoricTaskDetailQuery().operationType(type);
  }

  private HistoricTaskDetailQuery queryOperationDetails(String type, String property) {
    return historyService.createHistoricTaskDetailQuery().operationType(type).property(property);
  }

  private void completeProcess() {
    taskService.complete(task.getId());
    assertProcessEnded(process.getId());
  }

  private void cleanupHistory() {
    processEngineConfiguration.getCommandExecutorTxRequired().execute(new Command<Object>() {
      @Override
      public Object execute(CommandContext commandContext) {
        commandContext.getHistoricTaskInstanceManager().deleteHistoricTaskInstanceById(task.getId());
        return null;
      }
    });
  }
}
