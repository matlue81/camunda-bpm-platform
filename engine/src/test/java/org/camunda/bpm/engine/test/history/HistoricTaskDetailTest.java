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
import org.camunda.bpm.engine.impl.persistence.entity.TaskEntity;
import org.camunda.bpm.engine.impl.test.PluggableProcessEngineTestCase;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.task.Task;
import org.camunda.bpm.engine.test.Deployment;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

/**
 * @author Danny Gräf
 */
public class HistoricTaskDetailTest extends PluggableProcessEngineTestCase {

  public void testTaskEntityBeanPropertyChanges() {
    TaskEntity entity = new TaskEntity();

    // assign and validate changes
    entity.setAssignee("icke");
    Map<String, Object> changes = entity.getPropertyChanges();
    assertEquals(1, changes.size());
    assertEquals("icke", changes.get("assignee").toString());

    // then: assign it again
    entity.setAssignee("er");
    changes = entity.getPropertyChanges();
    assertEquals(1, changes.size());
    assertEquals("er", changes.get("assignee").toString());
  }

  @Deployment(resources = {"org/camunda/bpm/engine/test/history/HistoryLevelTest.bpmn20.xml"})
  public void testTaskComplete() {
    processEngineConfiguration.setHistoryLevel(ProcessEngineConfigurationImpl.HISTORYLEVEL_FULL);

    // given: a process with an user task
    ProcessInstance process = runtimeService.startProcessInstanceByKey("HistoryLevelTest");
    Task task = taskService.createTaskQuery().singleResult();

    // expect: one entry for task creation
    HistoricTaskDetailQuery query = historyService.createHistoricTaskDetailQuery();
    assertEquals(1, query.count());

    // assert: all details
    HistoricTaskDetail createName = query.singleResult();
    String createOperationId = createName.getOperationId();
    assertNotNull(createOperationId);
    Date createTimestamp = createName.getTimestamp();
    assertTrue(createTimestamp.before(new Date()));
    assertNotNull(createName.getId());
    assertNotNull(createName.getExecutionId());
    assertNotNull(createName.getProcessDefinitionId());
    assertNotNull(createName.getProcessInstanceId());
    assertEquals(task.getId(), createName.getTaskId());
    assertNull(createName.getUserId());
    assertEquals("a task", createName.getValue());

    // then: assign the task
    taskService.delegateTask(task.getId(), "kermit");

    // then: complete the task
    taskService.complete(task.getId());

    // expect: process ended
    assertProcessEnded(process.getId());
//
//    // expect: a second history entry of task completion
//    assertEquals(2, query.count());
//    HistoricTaskDetail complete = query.list().get(1);
//    assertEquals("complete", complete.getOperationType());
//    assertEquals("icke", complete.getUserId());
  }

}
