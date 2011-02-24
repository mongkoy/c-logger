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
 * $Id: LogView4JEventManager.java,v 1.4 2005/10/03 10:11:26 jpassenger Exp $
 */

package org.logview4j.event;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.SwingUtilities;

/**
 * Manages events for LogView4J and ensures that all events
 * are dispatched on the AWT event dispatcher thread
 */
public class LogView4JEventManager {

  /**
   * A map of listeners
   */
  private Map listeners = new HashMap();
  
  /**
   * Singleton instance
   */
  private static LogView4JEventManager instance = new LogView4JEventManager();
  
  /**
   * Private constructor to support singleton
   */
  private LogView4JEventManager() {
    
  }
  
  /**
   * Singleton getter
   * @return the singleton instance
   */
  public static LogView4JEventManager getInstance() {
    return instance;
  }
  
  /**
   * Removes all listeners
   */
  public void clearAllListeners() {
    listeners.clear();
  }
  
  /**
   * Registers an event listener to its events of interest
   * @param listener the event listener to register
   */
  public void register(LogView4JEventListener listener) {
    LogView4JEventId [] events = listener.getEventsOfInterest();
    
    if (events == null) {
      return;
    }
    
    for (int i = 0; i < events.length; i++) {
      List list = getListeners(events[i]);
      list.add(listener);
    }
  }
  
  /**
   * Fires an event to the registered listeners, checking to see if the
   * current thread is the AWT event dispatcher thread and if not creates
   * a new Runnable and uses SwingUtilities.invokeLater()
   * @param event the event to fire
   */
  public void fireEvent(LogView4JEvent event) {
  
    /**
     * Don't bother checking calling thread for these events
     */
    if (event.contains(LogView4JEventKey.JUST_FIRE_EVENT)) {
      fireEventOnAWTThread(event);
      return;
    }
    
    if (SwingUtilities.isEventDispatchThread()) {
      fireEventOnAWTThread(event);
    }
    else {
      LogView4JEventDispatcher dispatcher = new LogView4JEventDispatcher(event);
      SwingUtilities.invokeLater(dispatcher);
    }
  }
  
  /**
   * Fires the event on the AWT event dispatcher thread.  This method
   * id private and can only be accessed by calling fireEvent() which 
   * checks to see if the current thread is the AWT event dispatcher 
   * thread and if not uses SwingUtilities.invokeLater() to get here.
   * @param event the event to dispatch
   */
  protected void fireEventOnAWTThread(LogView4JEvent event) {
    List listenersList = getListeners(event.getEventId());
    
    for (int i = 0; i < listenersList.size(); i++) {
      LogView4JEventListener listener = (LogView4JEventListener) listenersList.get(i);
      listener.eventReceived(event);
    } 
  }

  /**
   * Get the listeners for the requested event id
   * @param id the id to fetch listeners for
   * @return the list of listeners
   */
  private List getListeners(LogView4JEventId id) {
    if (listeners.containsKey(id)) {
      return (List) listeners.get(id);
    }
    
    List listenerList = new ArrayList();
    
    listeners.put(id, listenerList);
    
    return listenerList;
  }
  
  /**
   * An inner class that facilitates dispatching 
   * the event on the AWT thread
   */
  class LogView4JEventDispatcher implements Runnable {

    /**
     * The event to dispatch
     */
    protected final LogView4JEvent event;
    
    /**
     * Creates the dispatcher witht eh event to dispatch
     * @param event the event to dispatch
     */
    public LogView4JEventDispatcher(LogView4JEvent event) {
      this.event = event;
    }
    
    /**
     * Executed by the AWT event dispatcher thread
     */
    public void run() {
      fireEventOnAWTThread(event);
    }
  }
}
