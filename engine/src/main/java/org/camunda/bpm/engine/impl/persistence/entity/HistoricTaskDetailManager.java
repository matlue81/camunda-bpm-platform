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
package org.camunda.bpm.engine.impl.persistence.entity;

import org.camunda.bpm.engine.history.HistoricTaskDetail;
import org.camunda.bpm.engine.impl.HistoricTaskDetailQueryImpl;
import org.camunda.bpm.engine.impl.Page;
import org.camunda.bpm.engine.impl.cfg.ProcessEngineConfigurationImpl;
import org.camunda.bpm.engine.impl.context.Context;
import org.camunda.bpm.engine.impl.history.event.HistoryEvent;
import org.camunda.bpm.engine.impl.history.handler.HistoryEventHandler;
import org.camunda.bpm.engine.impl.history.producer.HistoryEventProducer;
import org.camunda.bpm.engine.impl.persistence.AbstractHistoricManager;

import java.util.List;
import java.util.Map;


/**
 * @author Danny Gräf
 */
public class HistoricTaskDetailManager extends AbstractHistoricManager {

  public long findHistoricTaskDetailCountByQueryCriteria(HistoricTaskDetailQueryImpl historicTaskDetailQuery) {
    return (Long) getDbSqlSession().selectOne("selectHistoricTaskDetailCountByQueryCriteria", historicTaskDetailQuery);
  }

  @SuppressWarnings("unchecked")
  public List<HistoricTaskDetail> findHistoricTaskDetailsByQueryCriteria(HistoricTaskDetailQueryImpl historicDetailQuery, Page page) {
    return getDbSqlSession().selectList("selectHistoricTaskDetailsByQueryCriteria", historicDetailQuery, page);
  }

  public void createHistoricTaskDetails(String userId, String operation, TaskEntity taskEntity, Map<String, Object> propertyChanges) {
    ProcessEngineConfigurationImpl configuration = Context.getProcessEngineConfiguration();
    String operationId = configuration.getIdGenerator().getNextId();

    int historyLevel = configuration.getHistoryLevel();
    if(historyLevel>=ProcessEngineConfigurationImpl.HISTORYLEVEL_FULL) {

      final HistoryEventProducer eventProducer = configuration.getHistoryEventProducer();
      final HistoryEventHandler eventHandler = configuration.getHistoryEventHandler();

      for (Map.Entry<String, Object> change : propertyChanges.entrySet()) {
        HistoryEvent evt = eventProducer.createTaskDetailChangeEvt(userId, operationId, operation, change.getKey(), change.getValue(), taskEntity);
        eventHandler.handleEvent(evt);
      }
    }
  }

  public void deleteHistoricTaskDetailsByProcessInstanceId(String historicProcessInstanceId) {
    Context.getCommandContext().getDbSqlSession().delete("deleteHistoricTaskDetailsByProcessInstanceId", historicProcessInstanceId);
  }

  public void deleteHistoricTaskDetailsByTaskId(String taskId) {
    Context.getCommandContext().getDbSqlSession().delete("deleteHistoricTaskDetailsByTaskId", taskId);
  }
}
