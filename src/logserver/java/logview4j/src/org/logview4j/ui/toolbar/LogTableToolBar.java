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
 * $Id: LogTableToolBar.java,v 1.15 2006/02/27 05:34:36 jpassenger Exp $
 */
package org.logview4j.ui.toolbar;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import org.logview4j.dto.LogView4JLoggingEvent;
import org.logview4j.event.LogView4JEvent;
import org.logview4j.event.LogView4JEventId;
import org.logview4j.event.LogView4JEventKey;
import org.logview4j.event.LogView4JEventListener;
import org.logview4j.event.LogView4JEventManager;

/**
 * A toolbar for the log table panel which has the ability to filter log levels
 */
public class LogTableToolBar extends MinimalJToolBar implements LogView4JEventListener {

	private static final Color INVALID_COLOR = new Color(222, 127, 0).brighter();
	protected SimpleToggleButton debugButton = new SimpleToggleButton("debug", "Show debug events", "images/level_off.gif", "images/debug.gif", true);
	protected SimpleToggleButton infoButton = new SimpleToggleButton("info", "Show info events", "images/level_off.gif", "images/info.gif", true);
	protected SimpleToggleButton warnButton = new SimpleToggleButton("warn", "Show warn events", "images/level_off.gif", "images/warning.gif", true);
	protected SimpleToggleButton errorButton = new SimpleToggleButton("error", "Show error events", "images/level_off.gif", "images/error.gif", true);
	protected SimpleToggleButton fatalButton = new SimpleToggleButton("fatal", "Show fatal events", "images/level_off.gif", "images/fatal.gif", true);
	protected SimpleToggleButton playButton = new SimpleToggleButton("play", "Event receival is active, click to pause", "images/play.gif", "images/pause.gif", true);
	protected ToolBarButton deleteButton = new ToolBarButton("delete", "Clear all events", "images/delete.gif");
	protected ToolBarButton markerButton = new ToolBarButton("marker", "Toggles marker on the selected row", "images/marker.gif");
	protected ToolBarButton prevMarkedButton = new ToolBarButton("prevMarked", "Select the previous marked row", "images/marker_previous.gif");
	protected ToolBarButton nextMarkedButton = new ToolBarButton("nextMarked", "Select the next marked row", "images/marker_next.gif");
	protected JTextField quickFilterTextField = new JTextField(25);
	protected JCheckBox regularExpressionCheckBox = new JCheckBox();

	public LogTableToolBar() {
		init();
	}

	/**
	 * Initializes the toolbar buttons
	 */
	protected void init() {

		super.init();

		markerButton.setEnabled(false);

		quickFilterTextField.setMaximumSize(new Dimension(250, 20));
		quickFilterTextField.setToolTipText("Quick filter");

		regularExpressionCheckBox.setOpaque(false);
		regularExpressionCheckBox.setToolTipText("Treat the filter text as a regular expression");


		add(quickFilterTextField);
		add(regularExpressionCheckBox);

		addDividerButton();

		add(debugButton);
		add(infoButton);
		add(warnButton);
		add(errorButton);
		add(fatalButton);

		addDividerButton();

		add(markerButton);
		add(prevMarkedButton);
		add(nextMarkedButton);

		addDividerButton();

		add(playButton);
		add(deleteButton);

		registerListeners();
	}

	/**
	 * Registers listeners on the buttons
	 */
	protected void registerListeners() {
		LevelChangeActionListener levelChangeListener = new LevelChangeActionListener();
		debugButton.addActionListener(levelChangeListener);
		infoButton.addActionListener(levelChangeListener);
		warnButton.addActionListener(levelChangeListener);
		errorButton.addActionListener(levelChangeListener);
		fatalButton.addActionListener(levelChangeListener);
		playButton.addActionListener(new PlayPauseActionListener());
		deleteButton.addActionListener(new ClearEventsActionListener());
		quickFilterTextField.getDocument().addDocumentListener(new QuickFilterDocumentListener());
		markerButton.addActionListener(new EventMarkerAction());
		prevMarkedButton.addActionListener(new PrevMarkedAction());
		nextMarkedButton.addActionListener(new NextMarkedAction());
		regularExpressionCheckBox.addActionListener(new RegexToggleListener());

		LogView4JEventManager.getInstance().register(this);
	}

	/**
	 * Toggles the button level state and notifies listeners of the change
	 * @param button the button that was clicked
	 */
	protected void updateLevelFilter(SimpleToggleButton button) {
		button.toggleOn();

		LogView4JEvent clearEvent = new LogView4JEvent(LogView4JEventId.CLEAR_EVENT_SELECTION);
		LogView4JEventManager.getInstance().fireEvent(clearEvent);

		LogView4JEvent event = new LogView4JEvent(LogView4JEventId.LOGGING_LEVEL_FILTER_CHANGED);
		event.set(LogView4JEventKey.LEVEL_FILTER_FLAGS, getLevelFlags());
		event.set(LogView4JEventKey.JUST_FIRE_EVENT, Boolean.TRUE);
		LogView4JEventManager.getInstance().fireEvent(event);
	}

	/**
	 * Toggles the play button state and fires an event
	 */
	protected void togglePlayPause() {
		playButton.toggleOn();

		if (playButton.isOn()) {
			playButton.setToolTipText("Event receival is active, click to pause");
		} else {
			playButton.setToolTipText("Event receival is inactive, click to start");
		}

		LogView4JEvent event = new LogView4JEvent(LogView4JEventId.PAUSE_PROCESSING_EVENTS);
		event.set(LogView4JEventKey.PAUSED, new Boolean(!playButton.isOn()));
		LogView4JEventManager.getInstance().fireEvent(event);
	}

	/**
	 * Fired to clear events
	 */
	protected void clearEvents() {
		LogView4JEvent event = new LogView4JEvent(LogView4JEventId.REMOVE_ALL_EVENTS);
		event.set(LogView4JEventKey.JUST_FIRE_EVENT, Boolean.TRUE);
		LogView4JEventManager.getInstance().fireEvent(event);
	}

	protected void quickFilterChanged() {
		LogView4JEvent clearEvent = new LogView4JEvent(LogView4JEventId.CLEAR_EVENT_SELECTION);
		LogView4JEventManager.getInstance().fireEvent(clearEvent);

		LogView4JEvent filterChangedEvent = new LogView4JEvent(LogView4JEventId.QUICK_FILTER_CHANGED);
		filterChangedEvent.set(LogView4JEventKey.QUICK_FILTER, quickFilterTextField.getText());
		filterChangedEvent.set(LogView4JEventKey.REGEX_FILTER, (regularExpressionCheckBox.isSelected()) ? Boolean.TRUE : Boolean.FALSE);
		LogView4JEventManager.getInstance().fireEvent(filterChangedEvent);
	}

	/**
	 * Fecthes the level filter flags
	 * @return the flags for the currently filtered levels
	 */
	protected Integer getLevelFlags() {
		int flags = 0;

		if (debugButton.isOn()) {
			flags = flags | LogView4JLoggingEvent.LEVEL_DEBUG_FLAG;
		}

		if (infoButton.isOn()) {
			flags = flags | LogView4JLoggingEvent.LEVEL_INFO_FLAG;
		}

		if (warnButton.isOn()) {
			flags = flags | LogView4JLoggingEvent.LEVEL_WARN_FLAG;
		}

		if (errorButton.isOn()) {
			flags = flags | LogView4JLoggingEvent.LEVEL_ERROR_FLAG;
		}

		if (fatalButton.isOn()) {
			flags = flags | LogView4JLoggingEvent.LEVEL_FATAL_FLAG;
		}

		return new Integer(flags);
	}

	/**
	 * Fired when a level filter is changed
	 */
	class LevelChangeActionListener implements ActionListener {

		public void actionPerformed(ActionEvent e) {
			SimpleToggleButton button = (SimpleToggleButton) e.getSource();
			updateLevelFilter(button);
		}
	}

	/**
	 * Fired when the play button is pressed
	 */
	class PlayPauseActionListener implements ActionListener {

		public void actionPerformed(ActionEvent e) {
			togglePlayPause();
		}
	}

	/**
	 * Clears all log events
	 */
	class ClearEventsActionListener implements ActionListener {

		public void actionPerformed(ActionEvent e) {
			clearEvents();
		}
	}

	/**
	 * Listens for document changes to the quick filter
	 */
	class QuickFilterDocumentListener implements DocumentListener {

		public void changedUpdate(DocumentEvent e) {
			quickFilterChanged();
		}

		public void insertUpdate(DocumentEvent e) {
			quickFilterChanged();
		}

		public void removeUpdate(DocumentEvent e) {
			quickFilterChanged();
		}
	}

	class RegexToggleListener implements ActionListener {

		public void actionPerformed(ActionEvent arg0) {
			quickFilterChanged();
		}
	}

	class EventMarkerAction extends AbstractAction {

		public void actionPerformed(ActionEvent e) {
			LogView4JEvent markerEvent = new LogView4JEvent(LogView4JEventId.TOGGLE_SELECTION_MARKER);
			LogView4JEventManager.getInstance().fireEvent(markerEvent);
		}
	}

	class PrevMarkedAction extends AbstractAction {

		public void actionPerformed(ActionEvent e) {
			LogView4JEvent markerEvent = new LogView4JEvent(LogView4JEventId.PREV_MARKED);
			LogView4JEventManager.getInstance().fireEvent(markerEvent);
		}
	}

	class NextMarkedAction extends AbstractAction {

		public void actionPerformed(ActionEvent e) {
			LogView4JEvent markerEvent = new LogView4JEvent(LogView4JEventId.NEXT_MARKED);
			LogView4JEventManager.getInstance().fireEvent(markerEvent);
		}
	}

	protected void logEventSelected(LogView4JEvent event) {
		Object eventObj = event.get(LogView4JEventKey.LOGGING_EVENT);

		markerButton.setEnabled(false);

		if (eventObj == null) {
			markerButton.setEnabled(false);
		} else {
			LogView4JLoggingEvent[] events = (LogView4JLoggingEvent[]) eventObj;

			if (events.length > 0) {
				markerButton.setEnabled(true);
			}
		}
	}

	/**
	 * @param event
	 */
	public void eventReceived(LogView4JEvent event) {
		switch (event.getEventId().getId()) {
			case LogView4JEventId.LOGGING_EVENT_SELECTED_ID: {
				logEventSelected(event);
				break;
			}
			case LogView4JEventId.FILTER_VALID_ID: {
				filterValidStateChanged(event);
				break;
			}
		}
	}

	private void filterValidStateChanged(LogView4JEvent event) {
		Boolean isValid = (Boolean) event.get(LogView4JEventKey.FILTER_VALID);
		if (Boolean.TRUE.equals(isValid)) {
			quickFilterTextField.setToolTipText("Quick filter");
			quickFilterTextField.setBackground(Color.WHITE);
			quickFilterTextField.setForeground(Color.BLACK);
		} else {
			String error = (String) event.get(LogView4JEventKey.ERROR_MESSAGE);
			quickFilterTextField.setToolTipText("Quick filter : " + error);
			quickFilterTextField.setBackground(INVALID_COLOR);
			quickFilterTextField.setForeground(Color.WHITE);
		}
	}

	/**
	 * @return
	 */
	public LogView4JEventId[] getEventsOfInterest() {
		return new LogView4JEventId[]{
					LogView4JEventId.LOGGING_EVENT_SELECTED,
					LogView4JEventId.FILTER_VALID,};
	}
}
