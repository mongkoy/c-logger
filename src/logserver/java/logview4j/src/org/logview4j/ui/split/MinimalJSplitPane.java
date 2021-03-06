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
 * $Id: MinimalJSplitPane.java,v 1.4 2005/09/30 12:39:44 jpassenger Exp $
 */

/*
 * Copyright (c) 2000-2005 JGoodies Karsten Lentzsch. All Rights Reserved.
 *
 * Redistribution and use in source and binary forms, with or without 
 * modification, are permitted provided that the following conditions are met:
 * 
 *  o Redistributions of source code must retain the above copyright notice, 
 *    this list of conditions and the following disclaimer. 
 *     
 *  o Redistributions in binary form must reproduce the above copyright notice, 
 *    this list of conditions and the following disclaimer in the documentation 
 *    and/or other materials provided with the distribution. 
 *     
 *  o Neither the name of JGoodies Karsten Lentzsch nor the names of 
 *    its contributors may be used to endorse or promote products derived 
 *    from this software without specific prior written permission. 
 *     
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" 
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, 
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR 
 * PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR 
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, 
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, 
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; 
 * OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, 
 * WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE 
 * OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, 
 * EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE. 
 */
package org.logview4j.ui.split;

import java.awt.Component;

import javax.swing.BorderFactory;
import javax.swing.JSplitPane;
import javax.swing.plaf.SplitPaneUI;
import javax.swing.plaf.basic.BasicSplitPaneUI;

/**
 * A JSplitPane with all of the borders removed
 * 
 * Concepts from com.jgoodies.uif_lite.component.UIFSplitPane
 */
public class MinimalJSplitPane extends JSplitPane {

	/**
	 * Builds a minimal JSplitPane
	 * @param orientation the orientation of the split
	 * @param left the left component
	 * @param right the right component
	 */
	public MinimalJSplitPane(int orientation, Component left, Component right) {
		super(orientation, false, left, right);
	}

	/**
	 * Builds a minimal JSplitPane
	 * @param orientation the orientation of the split
	 */
	public MinimalJSplitPane(int orientation) {
		this(orientation, null, null);
	}

	/**
	 * Factory method for creating a clean split pane
	 *
	 * @param orientation the orientation (from JSplitPane)
	 * @param left the left component
	 * @param right the right component
	 * @param location the proportional location, between 0.0 and 1.0
	 * @return the newly constructed CleanSplitPane
	 */
	public static MinimalJSplitPane createCleanSplitPane(int orientation,
			Component left, Component right) {
		MinimalJSplitPane split = new MinimalJSplitPane(orientation, left, right);
		split.setBorder(BorderFactory.createEmptyBorder());
		split.setOneTouchExpandable(false);
		split.setDividerSize(5);
		return split;
	}

	/**
	 * Updates the UI to remove the divider border if possible
	 */
	public void updateUI() {
		super.updateUI();
		removeDividerBorder();
	}

	/**
	 * Removes the divider border if this is a BasicSplitPaneUI
	 */
	private void removeDividerBorder() {
		SplitPaneUI splitPaneUI = getUI();
		if (splitPaneUI instanceof BasicSplitPaneUI) {
			BasicSplitPaneUI basicUI = (BasicSplitPaneUI) splitPaneUI;
			basicUI.getDivider().setBorder(BorderFactory.createEmptyBorder());
		}
	}
}
