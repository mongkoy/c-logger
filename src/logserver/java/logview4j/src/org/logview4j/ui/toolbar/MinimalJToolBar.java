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
 * $Id: MinimalJToolBar.java,v 1.4 2006/02/22 02:04:28 jpassenger Exp $
 */
package org.logview4j.ui.toolbar;

import java.awt.Dimension;
import java.awt.Insets;

import javax.swing.*;

import org.logview4j.ui.image.ImageManager;

import com.jgoodies.looks.plastic.PlasticLookAndFeel;

/**
 * A transparent JToolBar that has no border  
 */
public class MinimalJToolBar extends JToolBar {

	public MinimalJToolBar() {
	}

	/**
	 * Initialize the toolbar
	 */
	protected void init() {
		setOpaque(false);
		setBorder(BorderFactory.createEmptyBorder());
		setFloatable(false);
		setMargin(new Insets(0, 0, 0, 0));
		putClientProperty("JToolBar.isRollover", Boolean.TRUE);
		putClientProperty(PlasticLookAndFeel.IS_3D_KEY, Boolean.FALSE);
	}

	protected void addDividerButton() {
		JLabel divider = new JLabel(ImageManager.getInstance().getImage("images/divider.gif"));
		divider.setPreferredSize(new Dimension(4, 18));
		divider.setBorder(BorderFactory.createEmptyBorder());
		add(divider);
	}
}
