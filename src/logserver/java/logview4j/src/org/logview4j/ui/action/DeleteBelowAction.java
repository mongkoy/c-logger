package org.logview4j.ui.action;

import java.awt.event.*;

import javax.swing.*;

import org.logview4j.dto.*;
import org.logview4j.event.*;
import org.logview4j.ui.image.*;
import org.logview4j.ui.table.*;

/**
 * Action that deletes all log events above this event considering the current sorting 
 */
public class DeleteBelowAction extends AbstractAction {

	public DeleteBelowAction() {
		putValue(Action.NAME, "Delete below");
		putValue(Action.SMALL_ICON, ImageManager.getInstance().getImage("images/remove_below.gif"));
		putValue(Action.LONG_DESCRIPTION, "Deletes all events below this event");
	}

	/**
	 * Fired whe this action is invoked
	 * @param e the event
	 */
	public void actionPerformed(ActionEvent e) {
		LogView4JEvent event = new LogView4JEvent(LogView4JEventId.DELETE_EVENTS_BELOW);
		LogView4JLoggingEvent logEvent = LogTable.getInstance().getLogEvent(LogTable.getInstance().getSelectedRow());
		event.set(LogView4JEventKey.LOGGING_EVENT, logEvent);
		LogView4JEventManager.getInstance().fireEvent(event);
	}
}
