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
 * $Id: LogView4JEventId.java,v 1.11 2006/02/22 02:04:28 jpassenger Exp $
 */

package org.logview4j.event;


/**
 * A type safe enum of event ids
 */
public class LogView4JEventId {
  
  private int id = -1;
  
  private LogView4JEventId(int id) {
    this.id = id;
  }
  
  /**
   * Fetches the id for this event
   * @return the id for this event
   */
  public int getId() {
    return id;
  }
  
  /**
   * Return the id as the hashcode
   * @return the id
   */
  public int hashCode() {
    return id;
  }
  
  /**
   * Checks ids for equality
   * @param other the other id to check
   * @return true if the ids are equal
   */
  public boolean equals(Object other) {
    if (other == null) {
      return false;
    }
    
    if (other instanceof LogView4JEventId) {
      return ((LogView4JEventId) other).id == id;
    }
    
    return false;
  }
  
  /**
   * Fired when a log event is received
   */
  public static final int LOGGING_EVENTS_RECEIVED_ID = 100;
  public static final LogView4JEventId LOGGING_EVENTS_RECEIVED = 
    new LogView4JEventId(LOGGING_EVENTS_RECEIVED_ID);
  
  /**
   * Fired when a log event is selected in the log table
   */
  public static final int LOGGING_EVENT_SELECTED_ID = 101;
  public static final LogView4JEventId LOGGING_EVENT_SELECTED = 
    new LogView4JEventId(LOGGING_EVENT_SELECTED_ID);
  
  /**
   * Fired when the configuration panel is requested to be displayed
   */
  public static final int CONFIGURATION_DISPLAY_STATE_CHANGED_ID = 102;
  public static final LogView4JEventId CONFIGURATION_DISPLAY_STATE_CHANGED = 
    new LogView4JEventId(CONFIGURATION_DISPLAY_STATE_CHANGED_ID);
  
  /**
   * Fired when a logging level filetr has been changed
   */
  public static final int LOGGING_LEVEL_FILTER_CHANGED_ID = 103;
  public static final LogView4JEventId LOGGING_LEVEL_FILTER_CHANGED = 
    new LogView4JEventId(LOGGING_LEVEL_FILTER_CHANGED_ID); 
  
  /**
   * Fired when a logging level filetr has been changed
   */
  public static final int REMOVE_ALL_EVENTS_ID = 104;
  public static final LogView4JEventId REMOVE_ALL_EVENTS = 
    new LogView4JEventId(REMOVE_ALL_EVENTS_ID); 
  
  /**
   * Fired when the system is paused or unpaused
   */
  public static final int PAUSE_PROCESSING_EVENTS_ID = 105;
  public static final LogView4JEventId PAUSE_PROCESSING_EVENTS = 
    new LogView4JEventId(PAUSE_PROCESSING_EVENTS_ID); 
  
  /**
   * Fired when the system is paused or unpaused
   */
  public static final int QUICK_FILTER_CHANGED_ID = 106;
  public static final LogView4JEventId QUICK_FILTER_CHANGED = 
    new LogView4JEventId(QUICK_FILTER_CHANGED_ID); 
  
  /**
   * Fired when the event tablke needs to be clears
   */
  public static final int CLEAR_EVENT_SELECTION_ID = 107;
  public static final LogView4JEventId CLEAR_EVENT_SELECTION = 
    new LogView4JEventId(CLEAR_EVENT_SELECTION_ID); 
  
  /**
   * Fired when the marker on an event is toggled
   */
  public static final int TOGGLE_SELECTION_MARKER_ID = 108;
  public static final LogView4JEventId TOGGLE_SELECTION_MARKER = 
    new LogView4JEventId(TOGGLE_SELECTION_MARKER_ID);
  
  /**
   * Fired when the user requests the details to be copied to the clipboard
   */
  public static final int COPY_DETAILS_ID = 109;
  public static final LogView4JEventId COPY_DETAILS = 
    new LogView4JEventId(COPY_DETAILS_ID);
  
  /**
   * Fired when the user requests to delete events below this mark
   */
  public static final int DELETE_EVENTS_BELOW_ID = 110;
  public static final LogView4JEventId DELETE_EVENTS_BELOW = 
    new LogView4JEventId(DELETE_EVENTS_BELOW_ID);
  
  /**
   * Fired when the user requests to delete events above this mark
   */
  public static final int DELETE_EVENTS_ABOVE_ID = 111;
  public static final LogView4JEventId DELETE_EVENTS_ABOVE = 
    new LogView4JEventId(DELETE_EVENTS_ABOVE_ID);

  /**
   * Fired when the filter changes from being valid to invalid and vice versa
   */
  public static final int FILTER_VALID_ID = 112;
  public static final LogView4JEventId FILTER_VALID = 
    new LogView4JEventId(FILTER_VALID_ID);

  /**
   * Fired when the filter changes from being valid to invalid and vice versa
   */
  public static final int NEXT_MARKED_ID = 113;
  public static final LogView4JEventId NEXT_MARKED = 
    new LogView4JEventId(NEXT_MARKED_ID);

  /**
   * Fired when the filter changes from being valid to invalid and vice versa
   */
  public static final int PREV_MARKED_ID = 114;
  public static final LogView4JEventId PREV_MARKED = 
    new LogView4JEventId(PREV_MARKED_ID);

}
