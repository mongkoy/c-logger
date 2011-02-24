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
 * $Id: AboutAction.java,v 1.1 2005/12/27 14:53:11 jpassenger Exp $
 */
package org.logview4j.ui.action;

import java.awt.event.*;

import javax.swing.*;

import org.logview4j.config.*;
import org.logview4j.ui.*;
import org.logview4j.ui.image.*;

/**
 * Handles showing the about action
 */
public class AboutAction extends AbstractAction {

	protected final String version = ConfigurationManager.getInstance().getString(ConfigurationKey.VERSION);
	protected final int port = ConfigurationManager.getInstance().getInt(ConfigurationKey.LISTEN_PORT, -1);

	public AboutAction() {
		putValue(Action.NAME, "About");
	}

	/**
	 * Handles showing the about box
	 * @param e the action event
	 */
	public void actionPerformed(ActionEvent e) {
		JOptionPane.showMessageDialog(LogView4J.getRootFrame(), getBodyMessage(), "LogView4J", JOptionPane.INFORMATION_MESSAGE,
				ImageManager.getInstance().getImage("images/logview4j_about.gif"));
	}

	/**
	 * Fetch the body message
	 * @return the body message
	 */
	private String getBodyMessage() {
		StringBuffer buffer = new StringBuffer();

		buffer.append("<html>");

		buffer.append("LogView4J version: ").append(version);
		buffer.append("<p>");
		buffer.append("Listening on port: ").append(port);
		buffer.append("<p>");
		buffer.append("Author: Josh Passenger");

		buffer.append("</html>");

		return buffer.toString();
	}
}
