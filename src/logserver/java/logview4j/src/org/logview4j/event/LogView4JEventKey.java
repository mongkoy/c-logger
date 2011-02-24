/*
 * Copyright 1999-2005 The Apache Software Foundation.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
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
 * 
 * $Id: LogView4JEventKey.java,v 1.9 2006/02/22 02:04:28 jpassenger Exp $
 */

package org.logview4j.event;

/**
 * Event constants
 */
public class LogView4JEventKey {
  
  /**
   * The key
   */
  private final String key;
  
  private LogView4JEventKey(String key) {
    this.key = key;
  }
  
  /**
   * Return the hashcode of the key
   * @return the hashcode of the key
   */
  public int hashCode() {
    return key.hashCode();
  }
  
  /**
   * Checks keys for equality
   * @param other the other key to check
   * @return true if the keys are equal
   */
  public boolean equals(Object other) {
    if (other == null) {
      return false;
    }
    
    if (other instanceof LogView4JEventKey) {
      return key.equals(((LogView4JEventKey) other).key);
    }
    
    return false;
  }
  
  /**
   * A list of logging events
   */
  public static final LogView4JEventKey LOGGING_EVENTS = 
    new LogView4JEventKey("LOGGING_EVENTS");
  
  /**
   * The data from a logging event
   */
  public static final LogView4JEventKey LOGGING_EVENT = 
    new LogView4JEventKey("LOGGING_EVENT");
  
  /**
   * Should the configuration be visible
   */
  public static final LogView4JEventKey CONFIGURATION_VISIBLE = 
    new LogView4JEventKey("CONFIGURATION_VISIBLE");
  
  /**
   * Should the window menu update this new state?
   */
  public static final LogView4JEventKey UPDATE_WINDOW_MENU = 
    new LogView4JEventKey("UPDATE_WINDOW_MENU");
  
  /**
   * The Object source of the event
   */
  public static final LogView4JEventKey EVENT_SOURCE = 
    new LogView4JEventKey("EVENT_SOURCE");
  
  /**
   * The filter level flags
   */
  public static final LogView4JEventKey LEVEL_FILTER_FLAGS = 
    new LogView4JEventKey("LEVEL_FILTER_FLAGS");
  
  /**
   * The filter level flags
   */
  public static final LogView4JEventKey PAUSED = 
    new LogView4JEventKey("PAUSED");
  
  /**
   * The quick filter text
   */
  public static final LogView4JEventKey QUICK_FILTER = 
    new LogView4JEventKey("QUICK_FILTER");
  
  /**
   * A marker that indicates the event should just be fired
   * from the current thread, there is code later in the pipeline
   * to hanlde sycnhing with awt event thread
   */
  public static final LogView4JEventKey JUST_FIRE_EVENT = 
    new LogView4JEventKey("JUST_FIRE_EVENT");
  
  /**
   * The quick filter text
   */
  public static final LogView4JEventKey REGEX_FILTER = 
    new LogView4JEventKey("REGEX_FILTER");
  
  /**
   * The Error Message
   */
  public static final LogView4JEventKey ERROR_MESSAGE = 
    new LogView4JEventKey("ERROR_MESSAGE");
  
  /**
   * The Error Message
   */
  public static final LogView4JEventKey FILTER_VALID = 
    new LogView4JEventKey("FILTER_VALID");
  
  
}
