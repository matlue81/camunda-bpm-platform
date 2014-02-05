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
package org.camunda.bpm.engine.impl.cmd;

import java.io.Serializable;

import org.camunda.bpm.engine.ProcessEngineException;
import org.camunda.bpm.engine.TaskAlreadyClaimedException;
import org.camunda.bpm.engine.impl.interceptor.Command;
import org.camunda.bpm.engine.impl.interceptor.CommandContext;
import org.camunda.bpm.engine.impl.persistence.entity.TaskEntity;


/**
 * @author Joram Barrez
 */
public class ClaimTaskCmd implements Command<Void>, Serializable {
  
  private static final long serialVersionUID = 1L;

  protected String taskId;
  protected String userId;
  
  public ClaimTaskCmd(String taskId, String userId) {
    this.taskId = taskId;
    this.userId = userId;
  }
  
  public Void execute(CommandContext commandContext) {
    if(taskId == null) {
      throw new ProcessEngineException("taskId is null");
    }
    
    TaskEntity task = commandContext
      .getTaskManager()
      .findTaskById(taskId);
    
    if (task == null) {
      throw new ProcessEngineException("Cannot find task with id " + taskId);
    }
    
    if(userId != null) {
      if (task.getAssignee() != null) {
        if(!task.getAssignee().equals(userId)) {
          // When the task is already claimed by another user, throw exception. Otherwise, ignore
          // this, post-conditions of method already met.
          throw new TaskAlreadyClaimedException(task.getId(), task.getAssignee());
        }
      } else {
        task.setAssignee(userId);
      }      
    } else {
      // Task should be assigned to no one
      task.setAssignee(null);
    }

//    task.createTaskDetailHistory("claim");

    return null;
  }
}
