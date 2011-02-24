package org.logview4j.listener;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.liblogger.LoggingEvent;
import org.liblogger.LoggingEventException;
import org.logview4j.config.ConfigurationKey;
import org.logview4j.config.ConfigurationManager;
import org.logview4j.dto.LogView4JLoggingEvent;

/**
 * Super class of a data processor subclasses
 * @author Jefty Negapatan
 */
public abstract class DataProcessor implements Runnable {

	protected final InboundEventQueue eventQueue = InboundEventQueue.getInstance();
	protected static final SimpleDateFormat dateFormatter = new SimpleDateFormat
					(ConfigurationManager.getInstance().getString
					(ConfigurationKey.LOG_FILENAME_FORMAT));

	/**
	 * Override this method from the subclasses
	 */
	public abstract void run();

	/**
	 * Reads the data from an input stream and display it to the UI.
	 * The original data will not be mirrored to a file.
	 * @param inputStream input stream
	 */
	protected void readData(InputStream inputStream)
	{
		readData(inputStream, false);
	}

	/**
	 * Reads the data from an input stream and display it to the UI.
	 * The original data will be mirrored to a file if set to true.
	 * @param inputStream input stream
	 * @param mirrorDataToFile flag to mirror the original data to a file
	 */
	protected void readData(InputStream inputStream, boolean mirrorDataToFile) {
		BufferedReader bufferedReader = null;
		InputStreamReader inputStreamReader = null;
		PrintStream printStream = null;
		FileOutputStream fileOutputStream = null;
		try {
			inputStreamReader = new InputStreamReader(inputStream);
			bufferedReader = new BufferedReader(inputStreamReader);

			if(mirrorDataToFile) {
				String filename;
				filename = generateLogFileName();
				fileOutputStream = new FileOutputStream(filename);
				printStream = new PrintStream(fileOutputStream);
			}

			String log;
			LoggingEvent loggingEvent;
			LogView4JLoggingEvent logView4JLoggingEvent;
			while ((log = bufferedReader.readLine()) != null) {
				try {
					/* Mirror to file if set to true */
					if(printStream != null) {
						printStream.println(log);
					}
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
		/* Close all the opened streams */
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
		if(printStream != null) {
			printStream.close();
			printStream = null;
		}
		if(fileOutputStream != null) {
			try {
				fileOutputStream.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
			fileOutputStream = null;
		}
	}

	/**
	 * Generates a log filename based on the format specified in
	 * logview4j.log.filename.format property
	 * @return log filename
	 */
	protected static String generateLogFileName() {
		StringBuilder buffer = new StringBuilder();
		String dateFormat;

		dateFormat = dateFormatter.format(new Date(System.currentTimeMillis()));
		buffer.append(dateFormat);
		buffer.append(".log");

		return buffer.toString();
	}

	/**
	 * Fires the event
	 * @param loggingEvent the event to fire
	 */
	protected void fireEvent(LogView4JLoggingEvent loggingEvent) {
		eventQueue.put(loggingEvent);
	}
}
