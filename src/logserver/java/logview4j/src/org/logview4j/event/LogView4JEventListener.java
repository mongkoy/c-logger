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
 * $Id: LogView4JEventListener.java,v 1.3 2005/09/18 13:44:28 jpassenger Exp $
 */

package org.logview4j.event;

/**
 * The interface that listeners must implement to receive events
 */
public interface LogView4JEventListener {
  
  /**
   * Fired when an event is received
   * @param event the event
   */
  public void eventReceived(LogView4JEvent event);
  
  /**
   * Invoked by the Event manager
   * @return the events of interest to this class
   */
  public LogView4JEventId [] getEventsOfInterest();
}
