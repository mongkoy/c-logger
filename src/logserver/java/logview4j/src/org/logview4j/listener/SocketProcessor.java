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
 * $Id: SocketProcessor.java,v 1.9 2005/12/27 14:53:11 jpassenger Exp $
 */
package org.logview4j.listener;

import java.io.*;
import java.net.*;
import org.liblogger.LoggingEvent;
import org.liblogger.LoggingEventException;

import org.logview4j.dto.*;

/**
 * A socket processor processes socket connections
 * and reads off logging events from them, dispatching
 * the logging event to the LogReceiver
 */
public class SocketProcessor implements Runnable {

	protected Socket socket = null;
	protected final InboundEventQueue eventQueue = InboundEventQueue.getInstance();

	/**
	 * Creates a new socket processor with a socket conenction
	 * to read from
	 * @param socket the socket to read a logging event from
	 */
	public SocketProcessor(Socket socket) {
		this.socket = socket;
	}

	/**
	 * Reads a logging event from the socket
	 */
	public void run() {
		readData();
	}

	/**
	 * Reads the data from the socket
	 */
	protected void readData() {
		BufferedReader bufferedReader = null;
		InputStreamReader inputStreamReader = null;

		try {
			inputStreamReader = new InputStreamReader(socket.getInputStream());
			bufferedReader = new BufferedReader(inputStreamReader);

			String log;
			LoggingEvent loggingEvent;
			LogView4JLoggingEvent logView4JLoggingEvent;
			while((log = bufferedReader.readLine()) != null) {
				try {
					loggingEvent = LoggingEvent.valueOf(log);
					logView4JLoggingEvent = new LogView4JLoggingEvent(loggingEvent);
					loggingEvent = null;
					fireEvent(logView4JLoggingEvent);
				} catch (LoggingEventException ex) {
					ex.printStackTrace();
				}
			}
		}
		catch (IOException ex) {
			ex.printStackTrace();
		}
		/**
		 * Try to close the readers and socket
		 */
		if(bufferedReader != null) {
			try {
				bufferedReader.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		if(inputStreamReader != null) {
			try {
				inputStreamReader.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		closeSocket();

		/**
		 * Null out the readers and the socket
		 */
		bufferedReader = null;
		inputStreamReader = null;
		socket = null;
	}

	/**
	 * Closes the client socket
	 */
	protected void closeSocket() {
		if (socket != null) {
			try {
				socket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
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
