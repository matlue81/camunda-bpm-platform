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
package org.camunda.bpm.cockpit;

import java.util.Set;
import org.camunda.bpm.cockpit.db.CommandExecutor;
import org.camunda.bpm.cockpit.db.QueryService;
import org.camunda.bpm.cockpit.plugin.PluginRegistry;
import org.camunda.bpm.engine.ProcessEngine;

/**
 * <p>The {@link CockpitRuntimeDelegate} is a delegate to provide
 * the camunda cockpit plugin service.</p>
 *
 * @author roman.smirnov
 */
public interface CockpitRuntimeDelegate {

  /**
   * Returns a configured {@link QueryService} to execute custom
   * statements to the corresponding process engine.
   * @param processEngineName
   * @return a {@link QueryService}
   */
  public QueryService getQueryService(String processEngineName);

  /**
   * Returns a configured {@link CommandExecutor} to execute
   * commands to the corresponding process engine.
   * @param processEngineName
   * @return a {@link CommandExecutor}
   */
  public CommandExecutor getCommandExecutor(String processEngineName);

  /**
   * Returns a {@link ProcessEngine} to the assigned
   * <code>processEngineName</code>
   *
   * @param processEngineName
   * @return a {@link ProcessEngine}
   */
  public ProcessEngine getProcessEngine(String processEngineName);

  /**
   * Returns the list of {@link ProcessEngine} names available to the runtime
   * @return
   */
  public Set<String> getProcessEngineNames();

  /**
   * Returns the default {@link ProcessEngine} provided by the
   * @return
   */
  public ProcessEngine getDefaultProcessEngine();

  /**
   * A registry that provides access to the plugins registered
   * in the application.
   *
   * @return
   */
  public PluginRegistry getPluginRegistry();
}
