package org.liblogger;

import org.apache.log4j.Level;

/**
 * Logging event of liblogger
 * @author Jefty Negapatan
 */
public class LoggingEvent {

	protected Level level;
	protected String loggerName;
	protected long timestamp;
	protected String message;

	/**
	 * Get the value of message
	 *
	 * @return the value of message
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * Set the value of message
	 *
	 * @param message new value of message
	 */
	public void setMessage(String message) {
		this.message = message;
	}


	/**
	 * Get the value of timestamp
	 *
	 * @return the value of timestamp
	 */
	public long getTimestamp() {
		return timestamp;
	}

	/**
	 * Set the value of timestamp
	 *
	 * @param timestamp new value of timestamp
	 */
	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}


	/**
	 * Get the value of loggerName
	 *
	 * @return the value of loggerName
	 */
	public String getLoggerName() {
		return loggerName;
	}

	/**
	 * Set the value of loggerName
	 *
	 * @param loggerName new value of loggerName
	 */
	public void setLoggerName(String loggerName) {
		this.loggerName = loggerName;
	}

	/**
	 * Get the value of level
	 *
	 * @return the value of level
	 */
	public Level getLevel() {
		return level;
	}

	/**
	 * Set the value of level
	 *
	 * @param level new value of level
	 */
	public void setLevel(Level level) {
		this.level = level;
	}

	/**
	 * Returns a LoggingEvent instance based on the liblogger log
	 * @param log log
	 * @return LoggingEvent instance
	 */
	public static LoggingEvent valueOf(String log) throws LoggingEventException {
		LoggingEvent loggingEvent = new LoggingEvent();
		int startTagIndex;
		int endTagIndex;
		String subString;

		startTagIndex = log.indexOf('[');
		/* The first character must be [ */
		if(startTagIndex != 0) {
			throw new LoggingEventException("Log level is unknown.");
		}
		endTagIndex = log.indexOf(']', (startTagIndex + 1));
		if(endTagIndex < 0) {
			throw new LoggingEventException("Log level is unknown.");
		}
		subString = log.substring((startTagIndex + 1), endTagIndex);
		loggingEvent.setLevel(Level.toLevel(subString));

		startTagIndex = log.indexOf('[', (endTagIndex + 1));
		if(startTagIndex != (endTagIndex + 1)) {
			throw new LoggingEventException("Log timestamp is unknown.");
		}
		endTagIndex = log.indexOf(']', (startTagIndex + 1));
		if(endTagIndex < 0) {
			throw new LoggingEventException("Log timestamp is unknown.");
		}
		subString = log.substring((startTagIndex + 1), endTagIndex);
		try {
			loggingEvent.setTimestamp(Long.parseLong(subString));
		} catch (NumberFormatException ex) {
			throw new LoggingEventException("Log timestamp is unknown.");
		}

		startTagIndex = log.indexOf('[', (endTagIndex + 1));
		if(startTagIndex != (endTagIndex + 1)) {
			throw new LoggingEventException("Logger name is unknown.");
		}
		endTagIndex = log.indexOf(']', (startTagIndex + 1));
		if(endTagIndex < 0) {
			throw new LoggingEventException("Logger name is unknown.");
		}
		subString = log.substring((startTagIndex + 1), endTagIndex);
		loggingEvent.setLoggerName(subString);

		try {
			subString = log.substring((endTagIndex + 1));
			loggingEvent.setMessage(subString);
		} catch (IndexOutOfBoundsException ex) {
			throw new LoggingEventException("Log message is not present.");
		}

		return loggingEvent;
	}
}
