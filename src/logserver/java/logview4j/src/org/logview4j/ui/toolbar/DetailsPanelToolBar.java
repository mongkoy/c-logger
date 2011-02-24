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
 * $Id: DetailsPanelToolBar.java,v 1.3 2005/11/05 05:02:49 jpassenger Exp $
 */

package org.logview4j.ui.toolbar;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import org.logview4j.event.LogView4JEvent;
import org.logview4j.event.LogView4JEventId;
import org.logview4j.event.LogView4JEventManager;



/**
 * A toolbar for the details panel
 */
public class DetailsPanelToolBar extends MinimalJToolBar {
  
  protected ToolBarButton copyButton = null; 
  
  public DetailsPanelToolBar() {
    init();
  }
  
  
  /**
   * Initializes the toolbar buttons 
   */
  protected void init() {
    super.init();
    copyButton = new ToolBarButton("copy", "Copy event to clipboard", "images/copy.gif");
    add(copyButton);
    registerListeners();
  }
  
  private void registerListeners() {
    copyButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        copy();
      }
    });
  }
  
  private void copy() {
    LogView4JEvent event = new LogView4JEvent(LogView4JEventId.COPY_DETAILS);
    LogView4JEventManager.getInstance().fireEvent(event);
  }
  
}
