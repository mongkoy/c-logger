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
 * $Id: LoggingLevelMatcherEditor.java,v 1.2 2005/10/03 10:17:50 jpassenger Exp $
 */

package org.logview4j.ui.matcher;

import org.logview4j.event.LogView4JEvent;
import org.logview4j.event.LogView4JEventId;
import org.logview4j.event.LogView4JEventKey;
import org.logview4j.event.LogView4JEventListener;
import org.logview4j.event.LogView4JEventManager;

import ca.odell.glazedlists.matchers.AbstractMatcherEditor;

/**
 * A matcher editor handles building matchers for the currently
 * selected log level filter
 */
public class LoggingLevelMatcherEditor extends AbstractMatcherEditor implements LogView4JEventListener {
  
  /**
   * Creates a new log level matcher editor and registers it with
   * the event manager
   */
  public LoggingLevelMatcherEditor() {
    LogView4JEventManager.getInstance().register(this);
  }
  
  /**
   * Fired when a requested event is received
   * @param event the event that was fired
   */
  public void eventReceived(LogView4JEvent event) {
    switch(event.getEventId().getId()) {
      case LogView4JEventId.LOGGING_LEVEL_FILTER_CHANGED_ID: {
        filterLevelChanged((Integer) event.get(LogView4JEventKey.LEVEL_FILTER_FLAGS));
      }
    }
  }

  /**
   * Fired when the level filter has changed and tells the table to refilter
   * @param flags the new level filter flags
   */
  protected void filterLevelChanged(Integer flags) {
    this.fireChanged(new LoggingLevelMatcher(flags.intValue()));
  }

  /**
   * Tell the event manager what events to listen for
   * @return the events to listen for
   */
  public LogView4JEventId[] getEventsOfInterest() {
    return new LogView4JEventId[] {
        LogView4JEventId.LOGGING_LEVEL_FILTER_CHANGED
    };
  }
}
