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
 * $Id: DetailsPanel.java,v 1.5 2006/02/22 23:39:56 jpassenger Exp $
 */

package org.logview4j.ui.details;

import java.awt.Font;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;

import javax.swing.BorderFactory;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import org.logview4j.dto.LogView4JLoggingEvent;
import org.logview4j.event.LogView4JEvent;
import org.logview4j.event.LogView4JEventId;
import org.logview4j.event.LogView4JEventKey;
import org.logview4j.event.LogView4JEventListener;
import org.logview4j.event.LogView4JEventManager;

/**
 * A details panel for displaying the details of a log event
 */
public class DetailsPanel extends JScrollPane implements LogView4JEventListener {

  protected JTextArea textArea = new JTextArea();
  
  public DetailsPanel() {
    init();
  }
  
  /**
   * Initializes this component
   */
  protected void init() {
    getViewport().add(textArea);
    setBorder(BorderFactory.createEmptyBorder());
    
    textArea.setBorder(BorderFactory.createEmptyBorder(3, 3, 3, 3));

    textArea.setLineWrap(false);
    textArea.setTabSize(4);
    textArea.setFont(new Font("Courier New", Font.PLAIN, 12));
    textArea.setEditable(false);
    
    registerListeners();
  }



  /**
   * Registers for selection events
   */
  protected void registerListeners() {
    LogView4JEventManager.getInstance().register(this);
  }



  /**
   * Handles events from the event manager
   * @param event the event
   */
  public void eventReceived(LogView4JEvent event) {
    switch (event.getEventId().getId()) {
      case LogView4JEventId.LOGGING_EVENT_SELECTED_ID: {
        logEventSelected(event);
        break;
      }
      case LogView4JEventId.COPY_DETAILS_ID: {
        copy();
        break;
      }
    }
  }

  /**
   * Fired when a log event is selected, the log event itself might
   * be null
   * @param event the event dto which contains the logging event
   */
  protected void logEventSelected(LogView4JEvent event) {
    LogView4JLoggingEvent[] loggingEvents = (LogView4JLoggingEvent[]) event.get(LogView4JEventKey.LOGGING_EVENT);
    if (loggingEvents.length == 1){
      setLoggingEvent(loggingEvents[0]);      
    }
    else {
      setLoggingEvent(null);      
    }
  }
  
  

  /**
   * Sets the logging event to display
   * @param loggingEvent the logging event to display
   */
  public void setLoggingEvent(LogView4JLoggingEvent loggingEvent) {
    if (loggingEvent == null) {
      textArea.setText(null);
    }
    else {
      textArea.setText(loggingEvent.getFormattedString());
      textArea.setCaretPosition(0);
    }
  }

  /**
   * @return
   */
  public LogView4JEventId[] getEventsOfInterest() {
    return new LogView4JEventId[] {
      LogView4JEventId.LOGGING_EVENT_SELECTED,
      LogView4JEventId.COPY_DETAILS,
    };
  }


  /**
   * Copies the details to the clipboard
   */
  private void copy() {
    StringSelection st = new StringSelection(textArea.getText());
    Clipboard cp = Toolkit.getDefaultToolkit().getSystemClipboard();
    cp.setContents(st, null);
  }
  
}
