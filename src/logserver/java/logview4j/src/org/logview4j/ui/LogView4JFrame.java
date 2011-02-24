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
 * $Id: LogView4JFrame.java,v 1.15 2005/12/27 14:53:11 jpassenger Exp $
 */
package org.logview4j.ui;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

import org.logview4j.config.*;
import org.logview4j.exception.*;
import org.logview4j.listener.*;
import org.logview4j.ui.action.*;
import org.logview4j.ui.details.*;
import org.logview4j.ui.image.*;
import org.logview4j.ui.panel.*;
import org.logview4j.ui.split.*;
import org.logview4j.ui.table.*;
import org.logview4j.ui.toolbar.*;

import com.jgoodies.looks.*;

/**
 * Main application frame
 */
public class LogView4JFrame extends JFrame {

	protected HeaderPanel logTableHeaderPanel = null;
	protected HeaderPanel logDetailHeaderPanel = null;
	protected JPanel mainPanel = new JPanel(new BorderLayout());
	protected MinimalJSplitPane leftSplitPanel = null;
	protected JSplitPane splitPanel = null;
	protected JScrollPane logTableScrollPane = new JScrollPane(new LogTable());
	protected DetailsPanel detailsPanel = new DetailsPanel();
	protected DetailsPanelToolBar detailsPanelToolBar = new DetailsPanelToolBar();
	protected LogTableToolBar logTableToolBar = new LogTableToolBar();
	protected SocketListener socketListener = null;
	protected final String version = ConfigurationManager.getInstance().getString(ConfigurationKey.VERSION);
	protected final int port = ConfigurationManager.getInstance().getInt(ConfigurationKey.LISTEN_PORT, -1);
	/**
	 * Menus
	 */
	protected JMenuBar menuBar = new JMenuBar();
	protected JMenu fileMenu = new JMenu("File");
	protected JMenu helpMenu = new JMenu("Help");

	public LogView4JFrame(boolean isOnline) throws LogView4JException {
		enableEvents(AWTEvent.WINDOW_EVENT_MASK);
		init(isOnline);
	}

	protected final void init(boolean isOnline) throws LogView4JException {
		setSize(new Dimension(1024, 768));

		String title;
		title = getFrameTitle(isOnline);
		setTitle(title);

		ImageIcon icon = ImageManager.getInstance().getImage("images/logview4j.gif");

		if (icon != null) {
			setIconImage(icon.getImage());
		}

		logTableScrollPane.setBorder(BorderFactory.createEmptyBorder());

		logDetailHeaderPanel = new HeaderPanel(detailsPanel, detailsPanelToolBar);
		logTableHeaderPanel = new HeaderPanel(logTableScrollPane, logTableToolBar);

		logTableHeaderPanel.setText("Events");
		logDetailHeaderPanel.setText("Event details");

		splitPanel = MinimalJSplitPane.createCleanSplitPane(JSplitPane.VERTICAL_SPLIT, logTableHeaderPanel, logDetailHeaderPanel);
		splitPanel.setDividerLocation(400);
		splitPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 3, 3));
		mainPanel.add(splitPanel, BorderLayout.CENTER);
		getContentPane().add(mainPanel, BorderLayout.CENTER);

		createMenu(isOnline);

		if (isOnline) {
			registerListeners();
		}
	}

	/**
	 * Generates a frame title
	 * @param isOnline Flag if the processing of log is via socket
	 * @return The frame title
	 */
	private String getFrameTitle(boolean isOnline) {
		StringBuilder buffer = new StringBuilder();
		buffer.append("LogView4J version: ");
		buffer.append(version);
		if (isOnline) {
			buffer.append(" listening on port: ");
			buffer.append(port);
		} else {
			buffer.append(" offline mode");
		}
		return buffer.toString();
	}

	/**
	 * Register event listeners
	 * @throws LogView4JException
	 */
	private void registerListeners() throws LogView4JException {

		try {
			socketListener = new SocketListener(port);
		} catch (LogView4JException e) {
			e.printStackTrace();
			errorMessage("LogView4J failed to listen on socket: " + port + " caused by:\n" + e.toString());
			exit();
		}

		Thread t = new Thread(socketListener);
		t.setPriority(Thread.NORM_PRIORITY - 1);
		t.start();
	}

	/**
	 * Shows an error message
	 * @param message the error message to show
	 */
	public void errorMessage(String message) {
		JOptionPane.showMessageDialog(this, message, "LogView4J Error", JOptionPane.ERROR_MESSAGE);
	}

	/**
	 * Exits the application
	 */
	public static void exit() {
		System.exit(0);
	}

	/**
	 * Handle close requests as exits
	 * @param e the window event
	 */
	protected void processWindowEvent(WindowEvent e) {
		super.processWindowEvent(e);

		if (e.getID() == WindowEvent.WINDOW_CLOSING) {
			exit();
		}
	}

	/**
	 * Creates the application menu
	 */
	protected void createMenu(boolean isOnline) {
		menuBar.putClientProperty(Options.HEADER_STYLE_KEY, HeaderStyle.BOTH);
		menuBar.add(fileMenu);
		menuBar.add(helpMenu);

		fileMenu.add(new ExitAction());
		if (!isOnline) {
			fileMenu.add(new LoadLogFileAction());
		}
		helpMenu.add(new AboutAction());
		setJMenuBar(menuBar);
	}
}
