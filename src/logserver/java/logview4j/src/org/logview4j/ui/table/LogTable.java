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
 * $Id: LogTable.java,v 1.18 2006/06/07 01:31:41 jpassenger Exp $
 */
package org.logview4j.ui.table;

import java.awt.Color;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import javax.swing.JTable;

import javax.swing.SwingUtilities;
import javax.swing.event.ListSelectionEvent;

import org.logview4j.config.ConfigurationKey;
import org.logview4j.config.ConfigurationManager;
import org.logview4j.dto.LogView4JLoggingEvent;
import org.logview4j.event.LogView4JEvent;
import org.logview4j.event.LogView4JEventId;
import org.logview4j.event.LogView4JEventKey;
import org.logview4j.event.LogView4JEventListener;
import org.logview4j.event.LogView4JEventManager;
import org.logview4j.ui.matcher.LoggingLevelMatcherEditor;
import org.logview4j.ui.matcher.QuickFilterMatcherEditor;
import org.logview4j.ui.popup.LogTablePopupMenu;

import ca.odell.glazedlists.*;
import ca.odell.glazedlists.matchers.CompositeMatcherEditor;
import ca.odell.glazedlists.matchers.ThreadedMatcherEditor;
import ca.odell.glazedlists.swing.EventSelectionModel;
import ca.odell.glazedlists.swing.EventTableModel;

/**
 * A JTable used for displaying log events and is aware of filter changes
 */
public class LogTable extends JTable implements LogView4JEventListener, MouseListener {

	private static LogTable INSTANCE;
	protected static final String[] propertyNames = new String[]{"icon", "stackTracePresent", "levelString",
		"combinedTimeStamp", "category", "message"};
	protected static final String[] columnLabels = new String[]{" ", " ", "Level", "When", "Source", "Message"};
	protected static final int[] caseInsensitiveColumns = new int[]{2, 4, 5};
	protected static final int[] sortingColumns = new int[]{2, 3, 4, 5};
	private int maxEvents = ConfigurationManager.getInstance().getInt(ConfigurationKey.MAX_EVENTS, 10000);
	protected final EventList logEvents = new BasicEventList();
	protected final LoggingLevelMatcherEditor levelMatcherEditor = new LoggingLevelMatcherEditor();
	protected final FilterList filteredEvents;
	protected final QuickFilterMatcherEditor quickFilterMatcherEditor;
	protected final EventTableModel eventTableModel;
	protected final BasicEventList listOfMatchers = new BasicEventList();
	protected final CompositeMatcherEditor compositeMatcher = new CompositeMatcherEditor(listOfMatchers);
	protected final SortedList sortedEvents;
	protected final LogTableComparatorChooser tableComparorChooser;
	protected final LogTableCellRenderer renderer = new LogTableCellRenderer();
	protected final LogTableFormat tableFormat = new LogTableFormat(LogView4JLoggingEvent.class, propertyNames,
			columnLabels, caseInsensitiveColumns);
	public static Color selectedBackgroundColor = null;

	public static LogTable getInstance() {
		return INSTANCE;
	}

	public LogTable() {
		super();

		INSTANCE = this;

		quickFilterMatcherEditor = new QuickFilterMatcherEditor();

		listOfMatchers.add(levelMatcherEditor);
		listOfMatchers.add(quickFilterMatcherEditor);

		compositeMatcher.setMode(CompositeMatcherEditor.AND);

		filteredEvents = new FilterList(logEvents, new ThreadedMatcherEditor(compositeMatcher));
		sortedEvents = new SortedList(filteredEvents, null);
		eventTableModel = new EventTableModel(sortedEvents, tableFormat);
		setModel(eventTableModel);
		tableComparorChooser = new LogTableComparatorChooser(this, sortedEvents, false, sortingColumns, 3);

		try {
			init();
		} catch (Throwable t) {
			t.printStackTrace();
		}
	}

	/**
	 * Initializes the component
	 */
	protected void init() {

		setRowHeight(18);

		selectedBackgroundColor = getSelectionBackground();

		getTableHeader().setReorderingAllowed(false);

		getColumnModel().getColumn(0).setMaxWidth(20);
		getColumnModel().getColumn(0).setMinWidth(20);
		getColumnModel().getColumn(1).setMaxWidth(20);
		getColumnModel().getColumn(1).setMinWidth(20);
		getColumnModel().getColumn(2).setPreferredWidth(50);
		getColumnModel().getColumn(2).setMaxWidth(50);
		getColumnModel().getColumn(3).setPreferredWidth(115);
		getColumnModel().getColumn(3).setMaxWidth(115);
		getColumnModel().getColumn(4).setPreferredWidth(200);
		getColumnModel().getColumn(5).setPreferredWidth(300);

		setShowVerticalLines(false);

		setDefaultRenderer(String.class, renderer);
		setDefaultRenderer(Object.class, renderer);
		setDefaultRenderer(Long.class, renderer);

		EventSelectionModel myEventSelectionModel = new EventSelectionModel(sortedEvents);
		setSelectionModel(myEventSelectionModel);
		getSelectionModel().setSelectionMode(ListSelection.MULTIPLE_INTERVAL_SELECTION_DEFENSIVE);

		registerListeners();
	}

	/**
	 * Registers event listeners
	 */
	private void registerListeners() {
		LogView4JEventManager.getInstance().register(this);
		getSelectionModel().addListSelectionListener(this);
		addMouseListener(this);
	}

	private void deleteEventsAbove() {
		if (getSelectedRow() == -1) {
			return;
		}

		int numberToRemove = getSelectedRow();

		logEvents.getReadWriteLock().writeLock().lock();

		for (int i = 0; i < numberToRemove; i++) {
			logEvents.remove(sortedEvents.get(0));
		}

		logEvents.getReadWriteLock().writeLock().unlock();
	}

	private void deleteEventsBelow() {
		if (getSelectedRow() == -1) {
			return;
		}

		int selectedRow = getSelectedRow() + 1;

		int numberToRemove = sortedEvents.size() - selectedRow;

		logEvents.getReadWriteLock().writeLock().lock();

		for (int i = 0; i < numberToRemove; i++) {
			logEvents.remove(sortedEvents.get(selectedRow));
		}

		logEvents.getReadWriteLock().writeLock().unlock();
	}

	/**
	 * @param event
	 */
	public void eventReceived(LogView4JEvent event) {

		switch (event.getEventId().getId()) {
			case LogView4JEventId.LOGGING_EVENTS_RECEIVED_ID: {
				List events = (List) event.get(LogView4JEventKey.LOGGING_EVENTS);
				addEvents(events);
				break;
			}
			case LogView4JEventId.REMOVE_ALL_EVENTS_ID: {
				logEvents.getReadWriteLock().writeLock().lock();
				logEvents.clear();
				logEvents.getReadWriteLock().writeLock().unlock();
				System.gc();
				break;
			}
			case LogView4JEventId.CLEAR_EVENT_SELECTION_ID: {
				clearSelection();
				break;
			}
			case LogView4JEventId.TOGGLE_SELECTION_MARKER_ID: {
				toggleSelectionMarker();
				break;
			}
			case LogView4JEventId.DELETE_EVENTS_ABOVE_ID: {
				deleteEventsAbove();
				break;
			}
			case LogView4JEventId.DELETE_EVENTS_BELOW_ID: {
				deleteEventsBelow();
				break;
			}
			case LogView4JEventId.PREV_MARKED_ID: {
				displayPreviouslyMarked();
				break;
			}
			case LogView4JEventId.NEXT_MARKED_ID: {
				displayNextMarked();
				break;
			}
		}
	}

	private void displayPreviouslyMarked() {
		int index = getSelectedRow() - 1;
		int rowToShow = -1;

		LogView4JLoggingEvent loggingEvent = null;

		while (index > -1 && index < sortedEvents.size()) {
			loggingEvent = (LogView4JLoggingEvent) sortedEvents.get(index);
			if (loggingEvent.isMarked()) {
				rowToShow = index;
				break;
			}
			index--;
		}
		if (rowToShow != -1) {
			getSelectionModel().setSelectionInterval(rowToShow, rowToShow);
			Rectangle rect = getCellRect(rowToShow, 0, true);
			scrollRectToVisible(rect);
		}
	}

	private void displayNextMarked() {
		int index = getSelectedRow() + 1;
		if (index < 0) {
			index = 0;
		}
		int rowToShow = -1;

		LogView4JLoggingEvent loggingEvent = null;

		while (index > -1 && index < sortedEvents.size()) {
			loggingEvent = (LogView4JLoggingEvent) sortedEvents.get(index);
			if (loggingEvent.isMarked()) {
				rowToShow = index;
				break;
			}
			index++;
		}
		if (rowToShow != -1) {
			getSelectionModel().setSelectionInterval(rowToShow, rowToShow);
			Rectangle rect = getCellRect(rowToShow, 0, true);
			scrollRectToVisible(rect);
		}
	}

	/**
	 * Adds events to the event list making sure we blow away earler events if we have reached our
	 * maximum event count
	 * @param events the events to add
	 */
	private void addEvents(List events) {

		logEvents.getReadWriteLock().writeLock().lock();

		/**
		 * Check the size
		 */
		if (logEvents.size() + events.size() > maxEvents) {
			int eventsToRemove = Math.min(logEvents.size(), logEvents.size() + events.size() - maxEvents - 1);

			List toRemove = new ArrayList(eventsToRemove);

			for (int i = 0; i < eventsToRemove; i++) {
				toRemove.add(logEvents.get(i));
			}

			logEvents.removeAll(toRemove);

			toRemove = null;
		}

		logEvents.addAll(events);
		logEvents.getReadWriteLock().writeLock().unlock();
	}

	/**
	 * @return
	 */
	public LogView4JEventId[] getEventsOfInterest() {
		return new LogView4JEventId[]{LogView4JEventId.LOGGING_EVENTS_RECEIVED, LogView4JEventId.REMOVE_ALL_EVENTS,
					LogView4JEventId.CLEAR_EVENT_SELECTION, LogView4JEventId.TOGGLE_SELECTION_MARKER,
					LogView4JEventId.DELETE_EVENTS_ABOVE, LogView4JEventId.DELETE_EVENTS_BELOW,
					LogView4JEventId.PREV_MARKED, LogView4JEventId.NEXT_MARKED,};
	}

	/**
	 * Handles selection changes in the table and tell the event manager that the selection has
	 * changed
	 * @param e the ListSelectionEvent
	 */
	public void valueChanged(ListSelectionEvent e) {
		super.valueChanged(e);

		int[] selectedRows = getSelectedRows();
		LogView4JLoggingEvent[] loggingEvents = new LogView4JLoggingEvent[selectedRows.length];
		for (int i = 0; i < selectedRows.length; i++) {
			loggingEvents[i] = (LogView4JLoggingEvent) sortedEvents.get(selectedRows[i]);
		}

		LogView4JEvent event = new LogView4JEvent(LogView4JEventId.LOGGING_EVENT_SELECTED);
		event.set(LogView4JEventKey.LOGGING_EVENT, loggingEvents);
		LogView4JEventManager.getInstance().fireEvent(event);
	}

	protected void toggleSelectionMarker() {
		int[] selectedRows = getSelectedRows();
		LogView4JLoggingEvent loggingEvent = null;
		for (int i = 0; i < selectedRows.length; i++) {
			int selectedRow = selectedRows[i];

			if (selectedRow > -1 && selectedRow < sortedEvents.size()) {
				loggingEvent = (LogView4JLoggingEvent) sortedEvents.get(selectedRow);
				loggingEvent.toggleMarker();
				sortedEvents.set(selectedRow, loggingEvent);
			}
		}

	}

	public LogView4JLoggingEvent getLogEvent(int row) {
		if (row > -1 && row < sortedEvents.size()) {
			return (LogView4JLoggingEvent) sortedEvents.get(row);
		}

		return null;
	}

	/**
	 * Compartor for event time stamps
	 */
	class DateComparator implements Comparator {

		/**
		 * Compare event time stamps
		 * @param o1 teh first event
		 * @param o2 the second event
		 * @return the result of comparing the whens
		 */
		public int compare(Object o1, Object o2) {
			LogView4JLoggingEvent e1 = (LogView4JLoggingEvent) o1;
			LogView4JLoggingEvent e2 = (LogView4JLoggingEvent) o2;

			long w1 = e1.getWhen();
			long w2 = e2.getWhen();

			if (w1 < w2) {
				return 1;
			} else if (w1 > w2) {
				return -1;
			} else {
				return 0;
			}
		}
	}

	/**
	 * Fired when teh mouse is clicked on the table,
	 * checks for right click and shows the popup menu
	 * @param e the event
	 */
	public void mouseClicked(MouseEvent e) {
		if (getSelectedRow() > -1) {
			if (SwingUtilities.isRightMouseButton(e)) {
				LogTablePopupMenu popup = new LogTablePopupMenu();
				popup.showMenu(this, e.getX(), e.getY());
			}
		}
	}

	public void mouseEntered(MouseEvent e) {
	}

	public void mouseExited(MouseEvent e) {
	}

	public void mousePressed(MouseEvent e) {
	}

	public void mouseReleased(MouseEvent e) {
	}
}
