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
package org.camunda.bpm.engine.runtime;

import java.io.Serializable;

import org.camunda.bpm.engine.ProcessEngineConfiguration;
import org.camunda.bpm.engine.query.Query;

/**
 * @author roman.smirnov
 */
public interface VariableInstanceQuery extends Query<VariableInstanceQuery, VariableInstance> {
  
  /** Only select variable instances which have the variable name. **/
  VariableInstanceQuery variableName(String variableName);
  
  /** Only select variable instances which have the name like the assigned variable name.
   * The string can include the wildcard character '%' to express like-strategy: 
   * starts with (string%), ends with (%string) or contains (%string%).
   **/
  VariableInstanceQuery variableNameLike(String variableNameLike);
 
  /** Only select variable instances which have one of the executions ids. **/
  VariableInstanceQuery executionIdIn(String... executionIds);
  
  /** Only select variable instances which have one of the process instance ids. **/
  VariableInstanceQuery processInstanceIdIn(String... processInstanceIds);
  
  /** Only select variable instances which have one of the task ids. **/
  VariableInstanceQuery taskIdIn(String... taskIds);
  
  /** Only select variable instances which have one of the activity instance ids. **/
  VariableInstanceQuery activityInstanceIdIn(String... activityInstanceIds);
  
  /** 
   * Only select variables instances which have the given name and value. The type
   * of variable is determined based on the value, using types configured in 
   * {@link ProcessEngineConfiguration#getVariableTypes()}. 
   * Byte-arrays and {@link Serializable} objects (which are not primitive type wrappers)
   * are not supported.
   * @param name name of the variable, cannot be null.
   * @param value variable value, can be null.
   */
  VariableInstanceQuery variableValueEquals(String name, Object value);
  
  /** 
   * Only select variable instances which have the given name, but
   * with a different value than the passed value.
   * Byte-arrays and {@link Serializable} objects (which are not primitive type wrappers)
   * are not supported.
   * @param name name of the variable, cannot be null.
   * @param value variable value, can be null.
   */
  VariableInstanceQuery variableValueNotEquals(String name, Object value);

  /** 
   * Only select variable instances which value is greater than the passed value.
   * Booleans, Byte-arrays and {@link Serializable} objects (which are not primitive type wrappers)
   * are not supported.
   * @param name variable name, cannot be null.
   * @param value variable value, cannot be null.
   */
  VariableInstanceQuery variableValueGreaterThan(String name, Object value);
  
  /** 
   * Only select variable instances which value is greater than or equal to
   * the passed value. Booleans, Byte-arrays and {@link Serializable} objects (which 
   * are not primitive type wrappers) are not supported.
   * @param name variable name, cannot be null.
   * @param value variable value, cannot be null.
   */
  VariableInstanceQuery variableValueGreaterThanOrEqual(String name, Object value);
  
  /** 
   * Only select variable instances which value is less than the passed value.
   * Booleans, Byte-arrays and {@link Serializable} objects (which are not primitive type wrappers)
   * are not supported.
   * @param name variable name, cannot be null.
   * @param value variable value, cannot be null.
   */
  VariableInstanceQuery variableValueLessThan(String name, Object value);
  
  /** 
   * Only select variable instances which value is less than or equal to the passed value.
   * Booleans, Byte-arrays and {@link Serializable} objects (which are not primitive type wrappers)
   * are not supported.
   * @param name variable name, cannot be null.
   * @param value variable value, cannot be null.
   */
  VariableInstanceQuery variableValueLessThanOrEqual(String name, Object value);
  
  /** 
   * Only select variable instances which value is like the given value.
   * This be used on string variables only.
   * @param name variable name, cannot be null.
   * @param value variable value, cannot be null. The string can include the
   * wildcard character '%' to express like-strategy: 
   * starts with (string%), ends with (%string) or contains (%string%).
   */
  VariableInstanceQuery variableValueLike(String name, String value);

  /** Order by variable name (needs to be followed by {@link #asc()} or {@link #desc()}). */
  VariableInstanceQuery orderByVariableName();
  
  /** Order by variable type (needs to be followed by {@link #asc()} or {@link #desc()}). */
  VariableInstanceQuery orderByVariableType();
  
  /** Order by activity instance id (needs to be followed by {@link #asc()} or {@link #desc()}). */
  VariableInstanceQuery orderByActivityInstanceId();
  
}
