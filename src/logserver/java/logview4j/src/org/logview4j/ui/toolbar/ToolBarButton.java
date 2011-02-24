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
 * $Id: ToolBarButton.java,v 1.3 2005/10/03 10:17:51 jpassenger Exp $
 */

package org.logview4j.ui.toolbar;

import java.awt.Insets;

import javax.swing.JButton;
import javax.swing.UIManager;

import org.logview4j.ui.image.ImageManager;

/**
 * A JButton for displaying in the MinimalJToolBar
 */
public class ToolBarButton extends JButton {
  
  public ToolBarButton(String name, String altText, String icon) {
    super();
    setName(name);
    setIcon(ImageManager.getInstance().getImage(icon));
    setToolTipText(altText);
    UIManager.put("Button.select", UIManager.get("control"));
    init();
  }
  
  /**
   * Initializes the button
   *
   */
  protected void init() {
    setOpaque(false);
    setMargin(new Insets(1, 1, 1, 1));
  }
}
