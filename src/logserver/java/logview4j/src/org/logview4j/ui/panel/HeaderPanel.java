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
 * $Id: HeaderPanel.java,v 1.3 2005/10/01 14:45:24 jpassenger Exp $
 */

package org.logview4j.ui.panel;

import java.awt.BorderLayout;

import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JToolBar;
import javax.swing.border.Border;

import org.logview4j.ui.border.RaisedBorder;
import org.logview4j.ui.border.ShadowBorder;
import org.logview4j.ui.color.ColorFactory;
import org.logview4j.ui.toolbar.MinimalJToolBar;

/**
 * A header panel has a shadow border, a gradient panel at the top
 * and a toolbar for buttons etc in the gradient panel, like eclipse
 */
public class HeaderPanel extends JPanel {
  
  protected Border shadowBorder = new ShadowBorder();
  protected Border raisedBorder = new RaisedBorder();
  
  protected GradientPanel gradientPanel = new GradientPanel(new BorderLayout());
  protected JPanel mainPanel = new JPanel(new BorderLayout());
  protected JLabel title = new JLabel();
  protected JToolBar toolbar = null;
  protected JComponent rootComponent = null;
  
  public HeaderPanel(JComponent rootComponent) {
    super(new BorderLayout());
    this.rootComponent = rootComponent;
    toolbar = new MinimalJToolBar();
    init();
  }
  
  public HeaderPanel(JComponent rootComponent, JToolBar newToolbar) {
    super(new BorderLayout());
    this.rootComponent = rootComponent;
    this.toolbar = newToolbar;
    init();
  }
  
  protected void init() {
    setBorder(shadowBorder);
    gradientPanel.setBorder(raisedBorder);
    
    title.setForeground(ColorFactory.getGradientTextForeground());
    title.setOpaque(false);   
    
    mainPanel.add(rootComponent, BorderLayout.CENTER);
    
    gradientPanel.add(title, BorderLayout.CENTER);
    gradientPanel.add(toolbar, BorderLayout.EAST);
    add(gradientPanel, BorderLayout.NORTH);
    add(mainPanel, BorderLayout.CENTER);
  }
  
  public void setText(String text) {
    title.setText(text);
  }
  
  public void setIcon(ImageIcon icon) {
    title.setIcon(icon);
  }
  

}
