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

package org.camunda.bpm.engine.test.transactions;

import org.camunda.bpm.engine.ProcessEngineException;
import org.camunda.bpm.engine.impl.pvm.delegate.ActivityBehavior;
import org.camunda.bpm.engine.impl.pvm.delegate.ActivityExecution;
import org.camunda.bpm.engine.impl.test.PluggableProcessEngineTestCase;
import org.camunda.bpm.engine.test.Deployment;


/**
 * @author Tom Baeyens
 */
public class TransactionRollbackTest extends PluggableProcessEngineTestCase {
  
  public static class Buzzz implements ActivityBehavior {
    public void execute(ActivityExecution execution) throws Exception {
      throw new ProcessEngineException("Buzzz");
    }
  }

  @Deployment
  public void testRollback() {
    try {
      runtimeService.startProcessInstanceByKey("RollbackProcess");
      
      fail("Starting the process instance should throw an exception");
      
    } catch (Exception e) {
      assertEquals("Buzzz", e.getMessage());
    }
    
    assertEquals(0, runtimeService.createExecutionQuery().count());
  }
  
  @Deployment(resources = {
  		"org/camunda/bpm/engine/test/transactions/trivial.bpmn20.xml",
  		"org/camunda/bpm/engine/test/transactions/rollbackAfterSubProcess.bpmn20.xml"})
	public void testRollbackAfterSubProcess() {
		try {
			runtimeService.startProcessInstanceByKey("RollbackAfterSubProcess");
			
			fail("Starting the process instance should throw an exception");
			
		} catch (Exception e) {
			assertEquals("Buzzz", e.getMessage());
		}

		assertEquals(0, runtimeService.createExecutionQuery().count());

	}
}
