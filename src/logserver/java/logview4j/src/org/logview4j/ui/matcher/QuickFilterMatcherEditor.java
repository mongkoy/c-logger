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
 * $Id: QuickFilterMatcherEditor.java,v 1.4 2006/02/22 02:39:16 jpassenger Exp $
 */

package org.logview4j.ui.matcher;

import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import org.logview4j.event.LogView4JEvent;
import org.logview4j.event.LogView4JEventId;
import org.logview4j.event.LogView4JEventKey;
import org.logview4j.event.LogView4JEventListener;
import org.logview4j.event.LogView4JEventManager;

import ca.odell.glazedlists.matchers.AbstractMatcherEditor;
import ca.odell.glazedlists.matchers.Matcher;

public class QuickFilterMatcherEditor extends AbstractMatcherEditor implements
    LogView4JEventListener {

  private static final Matcher NO_MATCHER = new QuickFilterMatcher(
      new String[0], new LogLevelFilterator());

  protected final LogLevelFilterator filtrator = new LogLevelFilterator();

  protected Matcher matcher = NO_MATCHER;

  // member variable use to indicate if filter is valid.
  private boolean valid;

  public QuickFilterMatcherEditor() {
    this.valid = true;
    LogView4JEventManager.getInstance().register(this);
  }

  public Matcher getMatcher() {
    return matcher;
  }

  public void eventReceived(LogView4JEvent event) {
    Boolean regex = (Boolean) event.get(LogView4JEventKey.REGEX_FILTER);
    String value = (String) event.get(LogView4JEventKey.QUICK_FILTER);
    boolean isValid = true;
    String invalidText = null;
    boolean matchesAll = false;
    Matcher newMatcher = null;

    String[] filterValues = new String[0];
    if (value == null || value.trim().equals("")) {
      newMatcher = NO_MATCHER;
      matchesAll = true;
    } else {
      if (Boolean.TRUE.equals(regex)) {
        Pattern pattern = null;
        try {
          pattern = Pattern.compile(value);
          newMatcher = new RegexFilterMatcher(pattern, filtrator);
        } catch (PatternSyntaxException pse) {
          // illigal pattern, we want to ignore the change.
          isValid = false;
          invalidText = "Illegal Pattern : " + pse.getMessage(); 
        }
      } else {
        filterValues = value.split(" ");
        newMatcher = new QuickFilterMatcher(filterValues, filtrator);
      }
    }
    if (isValid){
      matcher = newMatcher;
      if (matchesAll){
        fireMatchAll();
      }
      else {
        /**
         * Must clear the selection to avoid problems with table trying to
         * select rows that don't exist anymore
         */
        LogView4JEvent clearEvent = new LogView4JEvent(
            LogView4JEventId.CLEAR_EVENT_SELECTION);
        LogView4JEventManager.getInstance().fireEvent(clearEvent);
        fireChanged(matcher);
      }
      if (!valid){
        valid = true;
        LogView4JEvent validFilterEvent = new LogView4JEvent(
            LogView4JEventId.FILTER_VALID);
        validFilterEvent.set(LogView4JEventKey.FILTER_VALID, Boolean.TRUE);
        LogView4JEventManager.getInstance().fireEvent(validFilterEvent);
      }
    }
    else {
      // invalid filter
      if (valid){
        valid = false;
        LogView4JEvent validFilterEvent = new LogView4JEvent(
            LogView4JEventId.FILTER_VALID);
        validFilterEvent.set(LogView4JEventKey.FILTER_VALID, Boolean.FALSE);
        validFilterEvent.set(LogView4JEventKey.ERROR_MESSAGE, invalidText);
        LogView4JEventManager.getInstance().fireEvent(validFilterEvent);
      }
    }
  }

  public LogView4JEventId[] getEventsOfInterest() {
    return new LogView4JEventId[] { LogView4JEventId.QUICK_FILTER_CHANGED };
  }

}
