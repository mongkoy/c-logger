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
 * $Id: ColorFactory.java,v 1.5 2005/10/03 10:17:50 jpassenger Exp $
 */
package org.logview4j.ui.color;

import java.awt.Color;

import javax.swing.UIManager;

/**
 * A factory for storing colors for the user interface
 */
public class ColorFactory {

	/**
	 * Fetches the gradient header background color
	 * @return the color of the gradient header background
	 */
	public static Color getGradientBackground() {
		Color c = UIManager.getColor("SimpleInternalFrame.activeTitleBackground");
		return c;
	}

	/**
	 * Fetch the background color for the gradients in LogView4J
	 * @return the background color for the gradients in LogView4J
	 */
	public static Color getGradientTextForeground() {
		return Color.WHITE;
	}
}
