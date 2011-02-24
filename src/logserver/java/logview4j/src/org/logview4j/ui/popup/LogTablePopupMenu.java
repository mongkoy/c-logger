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
 * $Id: LogTablePopupMenu.java,v 1.2 2005/12/27 14:53:11 jpassenger Exp $
 */

package org.logview4j.ui.popup;

import java.awt.*;

import javax.swing.*;

import org.logview4j.ui.action.*;

/**
 * A popup menu for the log table 
 */
public class LogTablePopupMenu extends JPopupMenu {

  
  public LogTablePopupMenu() {
    init();
  }
  
  private void init() {
    
  }
  
  public void showMenu(Component parent, int x, int y) {
    removeAll();
    
    add(new JMenuItem(new DeleteAboveAction()));
    add(new JMenuItem(new DeleteBelowAction()));
    show(parent, x, y);
  }
}
