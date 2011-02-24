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
 * $Id: LoggingLevelMatcher.java,v 1.1 2005/10/02 13:39:36 jpassenger Exp $
 */

package org.logview4j.ui.matcher;

import org.logview4j.dto.LogView4JLoggingEvent;

import ca.odell.glazedlists.matchers.Matcher;

/**
 * A matcher that is able to filter out log events in a set of valid log levels
 * very quickly using integer math
 */
public class LoggingLevelMatcher implements Matcher {

  /**
   * Level flags are build by adding togetehr valid currently filtered log events
   */
  protected int levelFlags = 0;
  
  /**
   * Creates a new matcher editor with a set of valid levels
   * @param levelFlags the level flags for comparison
   */
  public LoggingLevelMatcher(int levelFlags) {
    this.levelFlags = levelFlags;
  }
  
  /**
   * Fired to check if a logging event dto matches the requested levels
   * @param o the log event
   * @return true if the current log event's level flag is contained in the current
   * level flags for this matcher, false if it does no
   */
  public boolean matches(Object o) {

    LogView4JLoggingEvent event = (LogView4JLoggingEvent) o;

    if ((levelFlags & event.getLevelFlag()) == event.getLevelFlag()) {
      return true;
    }
    
    return false;
  }

}
