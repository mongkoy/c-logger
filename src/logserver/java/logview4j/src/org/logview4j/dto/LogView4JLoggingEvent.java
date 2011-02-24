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
 * $Id: LogView4JLoggingEvent.java,v 1.9 2006/06/07 01:36:39 jpassenger Exp $
 */
package org.logview4j.dto;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.swing.ImageIcon;

import org.apache.log4j.Priority;
import org.liblogger.LoggingEvent;
import org.logview4j.config.ConfigurationKey;
import org.logview4j.config.ConfigurationManager;
import org.logview4j.ui.image.ImageManager;

/**
 * A light weight, immutible logging event that extracts what is needed from the log4j log event and
 * discards the rest.
 * 
 * Its important to have our own implementation of logging event to simplify the hooks into
 * GlazedLists
 * 
 * Perhaps some investigation into string back references and how this affect garbage colelction of
 * the inbound LoggingEvent objects is warranted
 */
public class LogView4JLoggingEvent {

	/**
	 * Flags for resolving levels in filters
	 */
	public static final int LEVEL_DEBUG_FLAG = 1;
	public static final int LEVEL_INFO_FLAG = 2;
	public static final int LEVEL_WARN_FLAG = 4;
	public static final int LEVEL_ERROR_FLAG = 8;
	public static final int LEVEL_FATAL_FLAG = 16;
	private static long eventIdGenerator = 0;
	private static final int TOTAL_LENGTH = 19;
	private static int maxPayloadSize = ConfigurationManager.getInstance().getInt(ConfigurationKey.MAX_EVENT_PAYLOAD_SIZE, 100000);
	private static final String TRUNCATE_MESSAGE_PREFIX = "\n\n[Message truncated from: ";
	private static final String TRUNCATE_MESSAGE_SUFFIX = " characters, adjust logview4j.max.event.payload.size if required]";
	/**
	 * 32 bit references to these strings for light-weight storage
	 */
	private static final String[] LEVELS = new String[]{
		"DEBUG",
		"INFO",
		"WARN",
		"ERROR",
		"FATAL"
	};
	/**
	 * 32 bit references to these strings for light-weight storage
	 */
	private static final String[] ICONS = new String[]{
		"images/debug.gif",
		"images/info.gif",
		"images/warning.gif",
		"images/error.gif",
		"images/fatal.gif"
	};
	/**
	 * TODO make this configurable
	 */
	private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
	/**
	 * All final as these are immutable
	 */
	private final String category;
	private final int level;
	private final String message;
	private final long when;
	private transient String formattedString = null;
	private transient boolean marked = false;
	private final long eventId;
	private final long combinedTimeStamp;

	/**
	 * Extract log information locally
	 * @param e the event to extract from
	 */
	public LogView4JLoggingEvent(LoggingEvent e) {
		level = e.getLevel().toInt();
		category = e.getLoggerName();
		String tempMessage = e.getMessage() != null ? e.getMessage().toString() : null;
		when = e.getTimestamp();
		eventId = eventIdGenerator++;
		combinedTimeStamp = createCombinedTimeStamp();

		/**
		 * Truncate huge messages up front
		 */
		if (tempMessage != null && tempMessage.length() > maxPayloadSize) {
			StringBuffer buffer = new StringBuffer();
			buffer.append(tempMessage.substring(0, maxPayloadSize));
			buffer.append(TRUNCATE_MESSAGE_PREFIX).append(tempMessage.length()).append(TRUNCATE_MESSAGE_SUFFIX);
			message = buffer.toString();
		} else {
			message = tempMessage;
		}

		tempMessage = null;
		e = null;
	}

	/**
	 * Extract a reference to the static icon name
	 * @param levelInt the int value of the level
	 * @return an icon name
	 */
	private String getIconName() {

		switch (level) {
			case Priority.DEBUG_INT: {
				return ICONS[0];
			}
			case Priority.INFO_INT: {
				return ICONS[1];
			}
			case Priority.WARN_INT: {
				return ICONS[2];
			}
			case Priority.ERROR_INT: {
				return ICONS[3];
			}
			case Priority.FATAL_INT: {
				return ICONS[4];
			}
		}

		return null;
	}

	/**
	 * Extract a reference to the static level strings
	 * @param levelInt the int value of the level
	 * @return a level string
	 */
	public String getLevelString() {

		switch (level) {
			case Priority.DEBUG_INT: {
				return LEVELS[0];
			}
			case Priority.INFO_INT: {
				return LEVELS[1];
			}
			case Priority.WARN_INT: {
				return LEVELS[2];
			}
			case Priority.ERROR_INT: {
				return LEVELS[3];
			}
			case Priority.FATAL_INT: {
				return LEVELS[4];
			}
		}

		return null;
	}

	public String getCategory() {
		return category;
	}

	public String getFullyQualifiedClassName() {
		return null;
	}

	public int getLevel() {
		return level;
	}

	public String getMessage() {
		return message;
	}

	public String[] getStackTrace() {
		return null;
	}

	public long getWhen() {
		return when;
	}

	public long getCombinedTimeStamp() {
		return combinedTimeStamp;
	}

	/* TODO: Remove this function together with the table format */
	public boolean getStackTracePresent() {
		return false;
	}

	public ImageIcon getIcon() {
		return ImageManager.getInstance().getImage(getIconName());
	}

	/**
	 * Fecth a flag for the current level to enable quick determination in a set of level flags
	 * @return the level flag for this log events level
	 */
	public int getLevelFlag() {
		switch (level) {
			case Priority.DEBUG_INT: {
				return LEVEL_DEBUG_FLAG;
			}
			case Priority.INFO_INT: {
				return LEVEL_INFO_FLAG;
			}
			case Priority.WARN_INT: {
				return LEVEL_WARN_FLAG;
			}
			case Priority.ERROR_INT: {
				return LEVEL_ERROR_FLAG;
			}
			case Priority.FATAL_INT: {
				return LEVEL_FATAL_FLAG;
			}
			default: {
				return 0;
			}
		}
	}

	/**
	 * Fetch a cached formatted string
	 * @return the cached formatted string
	 */
	public String getFormattedString() {
		if (formattedString == null) {
			StringBuffer buffer = new StringBuffer();

			buffer.append("Level:     ").append(getLevelString()).append('\n');
			buffer.append("When:      ").append(DATE_FORMAT.format(new Date(when))).append('\n');
			buffer.append("From:      ").append(getCategory()).append("\n\n");
			buffer.append(message).append('\n');

			formattedString = buffer.toString();
			buffer.setLength(0);
			buffer = null;
		}

		return formattedString;
	}

	/**
	 * Harvest the quick filter match items
	 * @param textToMatch the text to match against
	 */
	public void harvestQuickFilterItems(List textToMatch) {
		textToMatch.add(getCategory());

		textToMatch.add(getMessage());
	}

	/**
	 * Toggles the marker on this log event
	 */
	public void toggleMarker() {
		marked = !marked;
	}

	/**
	 *
	 * @return the marked.
	 */
	public boolean isMarked() {
		return marked;
	}

	/**
	 * Creates a combined timestamp where the event id is used to descriminate log events that
	 * arrive at exactly teh same millisecond
	 * @return the combined timestamp
	 */
	private long createCombinedTimeStamp() {
		StringBuffer buffer = new StringBuffer();
		buffer.append(when);

		String idString = eventId + "";

		int lengthToPad = TOTAL_LENGTH - buffer.length() - idString.length();

		for (int i = 0; i < lengthToPad; i++) {
			buffer.append('0');
		}

		buffer.append(idString);

		return Long.parseLong(buffer.toString());
	}
}
