package org.logview4j.listener;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import org.liblogger.LoggingEvent;
import org.liblogger.LoggingEventException;
import org.logview4j.dto.LogView4JLoggingEvent;

/**
 * Super class of a data processor subclasses
 * @author Jefty Negapatan
 */
public abstract class DataProcessor implements Runnable {

	protected final InboundEventQueue eventQueue = InboundEventQueue.getInstance();

	/**
	 * Override this method from the subclasses
	 */
	public abstract void run();

	/**
	 * Reads the data from an input stream
	 */
	protected void readData(InputStream inputStream) {
		BufferedReader bufferedReader = null;
		InputStreamReader inputStreamReader = null;
		try {
			inputStreamReader = new InputStreamReader(inputStream);
			bufferedReader = new BufferedReader(inputStreamReader);

			String log;
			LoggingEvent loggingEvent;
			LogView4JLoggingEvent logView4JLoggingEvent;
			while ((log = bufferedReader.readLine()) != null) {
				try {
					loggingEvent = LoggingEvent.valueOf(log);
					logView4JLoggingEvent = new LogView4JLoggingEvent(loggingEvent);
					loggingEvent = null;
					fireEvent(logView4JLoggingEvent);
				} catch (LoggingEventException ex) {
					ex.printStackTrace();
				}
			}
		} catch (IOException ex) {
			ex.printStackTrace();
		}
		/**
		 * Try to close the readers and socket
		 */
		if (bufferedReader != null) {
			try {
				bufferedReader.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
			bufferedReader = null;
		}
		if (inputStreamReader != null) {
			try {
				inputStreamReader.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
			inputStreamReader = null;
		}
	}

	/**
	 * Fires the event
	 * @param loggingEvent the event to fire
	 */
	protected void fireEvent(LogView4JLoggingEvent loggingEvent) {
		eventQueue.put(loggingEvent);
	}
}
