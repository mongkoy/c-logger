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
 * $Id: MinimalJTabbedPane.java,v 1.2 2005/10/19 12:28:53 jpassenger Exp $
 */
package org.logview4j.ui.tab;

import javax.swing.JTabbedPane;

import com.jgoodies.looks.Options;

/**
 * A minimal rendering JTabbedPane
 */
public class MinimalJTabbedPane extends JTabbedPane {

	public MinimalJTabbedPane() {
		super();
		init();
	}

	/**
	 * Initializes the tabbed panel
	 */
	private void init() {
		putClientProperty(Options.NO_CONTENT_BORDER_KEY, Boolean.TRUE);
	}
}
