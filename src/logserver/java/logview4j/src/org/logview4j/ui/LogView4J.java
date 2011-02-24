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
 * $Id: LogView4J.java,v 1.7 2005/12/27 14:53:11 jpassenger Exp $
 */
package org.logview4j.ui;

import java.awt.*;

import javax.swing.UIManager;

import org.logview4j.exception.LogView4JException;

import com.jgoodies.looks.plastic.PlasticLookAndFeel;
import com.jgoodies.looks.plastic.PlasticXPLookAndFeel;
import com.jgoodies.looks.plastic.theme.Silver;

/**
 * Product entry point
 */
public class LogView4J {

	private static Frame rootFrame = null;

	public static void main(String[] args) throws LogView4JException {
		setLookAndFeel();

		LogView4JFrame frame = new LogView4JFrame();
		rootFrame = frame;
		frame.setLocationRelativeTo(null);
		frame.show();
	}

	public static Frame getRootFrame() {
		return rootFrame;
	}

	/**
	 * Sets the look and feel for the application
	 */
	private static void setLookAndFeel() {
		PlasticLookAndFeel.setMyCurrentTheme(new Silver());
		try {
			UIManager.setLookAndFeel(new PlasticXPLookAndFeel());
		} catch (Exception e) {
		}
	}
}
