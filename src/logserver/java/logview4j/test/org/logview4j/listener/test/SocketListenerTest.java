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
 * $Id: SocketListenerTest.java,v 1.13 2006/02/22 02:04:29 jpassenger Exp $
 */
package org.logview4j.listener.test;

import java.io.PrintStream;
import java.net.Socket;
import java.util.Random;

import junit.framework.TestCase;

import org.apache.log4j.Level;
import org.liblogger.LoggingEvent;
import org.logview4j.config.*;
import org.logview4j.event.LogView4JEvent;
import org.logview4j.event.LogView4JEventId;
import org.logview4j.event.LogView4JEventListener;

/**
 * Tests the socket listener
 */
public class SocketListenerTest extends TestCase implements LogView4JEventListener {

	private int eventsSent = 0;

	public void atestHeavyLoad() {
		int iterations = 2000;

		System.out.println("Executing heavy load test, sending: " + iterations + " messages");

		eventsSent = 0;

		long start = System.currentTimeMillis();

		Random dice = new Random();

		int i = 0;

		try {

			Socket socket = new Socket("localhost", ConfigurationManager.getInstance().getInt(ConfigurationKey.LISTEN_PORT, 50007));
			PrintStream out = new PrintStream(socket.getOutputStream());

			for (i = 0; i < iterations; i++) {

				LoggingEvent event = createRandomEvent(dice);
				out.println(event);
				eventsSent++;
				out.flush();
			}

			out.close();
			socket.close();
			out = null;
			socket = null;
		} catch (Throwable t) {
			t.printStackTrace();
			fail("Failed on attempt: " + i);
		}


		long end = System.currentTimeMillis();

		System.out.println("Average message send took: " + (((float) (end - start)) / (float) iterations) + " millis");
	}

	/**
	 * Tests listening by starting the listener and firing some data at it
	 * over a long period
	 * @throws Exception thrown on failure
	 */
	public void testSoakListening() throws Exception {

		int iterations = 100000;

		System.out.println("Executing soak test, sending: " + iterations + " messages");

		eventsSent = 0;

		long start = System.currentTimeMillis();

		Random dice = new Random();

		int i = 0;

		try {

			Socket socket = new Socket("localhost", ConfigurationManager.getInstance().getInt(ConfigurationKey.LISTEN_PORT, 4447));
			PrintStream out = new PrintStream(socket.getOutputStream());
			for (i = 0; i < iterations; i++) {

				LoggingEvent event = createRandomEvent(dice);
				out.println(event);
				eventsSent++;
				out.flush();
				Thread.sleep((long) dice.nextInt(10));
			}

			out.close();
			socket.close();
			out = null;
			socket = null;
		} catch (Throwable t) {
			t.printStackTrace();
			fail("Failed on attempt: " + i);
		}


		long end = System.currentTimeMillis();

		System.out.println("Average message send took: " + (((float) (end - start)) / (float) iterations) + " millis");
	}

	/**
	 * @param dice
	 * @return
	 */
	private LoggingEvent createRandomEvent(Random dice) {

		String category = createRandomCategory(dice);
		Level level = createRandomLevel(dice);
		String message = createRandomMessage(dice);
		LoggingEvent event = new LoggingEvent();
		event.setLevel(level);
		event.setLoggerName(category);
		event.setMessage(message);
		event.setTimestamp(System.currentTimeMillis());

		return event;
	}

	private static final String[] RANDOM_WORDS = new String[]{
		"memory",
		"Memory",
		"leaks",
		"can",
		"arise",
		"when",
		"you",
		"create",
		"short-lived",
		"transformations",
		"of",
		"long-lived",
		"eventlists",
		". ",
		"because",
		"the",
		"transformationlists",
		"register",
		"themselves",
		"as",
		"listeners",
		"Listeners",
		"on",
		"their",
		"source",
		"Source",
		", ",
		"this",
		"reference",
		"from",
		"their",
		"list",
		"List",
		"may",
		"prevent",
		"them",
		"from",
		"being",
		"garbage",
		"collected",};
	private static final String[] RANDOM_PACKAGES = new String[]{
		"com",
		"net",
		"java",
		"Java",
		"lang",
		"Lang",
		"util",
		"Util",
		"logview4j",
		"org",
		"apache",
		"Foo",
		"foo",
		"Bar",
		"bar"
	};

	/**
	 * @param dice
	 * @return
	 */
	private String createRandomMessage(Random dice) {
		int words = dice.nextInt(25);

		StringBuffer buffer = new StringBuffer();

		for (int i = 0; i < words; i++) {
			buffer.append(RANDOM_WORDS[dice.nextInt(RANDOM_WORDS.length)]);
			buffer.append(' ');
		}

		return buffer.toString();
	}

	/**
	 * @param dice
	 * @return
	 */
	private Level createRandomLevel(Random dice) {
		int level = dice.nextInt(95);

		if (level < 25) {
			return Level.DEBUG;
		} else if (level < 75) {
			return Level.INFO;
		} else if (level < 80) {
			return Level.WARN;
		} else if (level < 85) {
			return Level.ERROR;
		} else if (level < 100) {
			return Level.FATAL;
		}

		return null;
	}

	/**
	 * @param dice
	 * @return
	 */
	private String createRandomCategory(Random dice) {
		int words = dice.nextInt(7) + 1;

		StringBuffer buffer = new StringBuffer();

		for (int i = 0; i < words; i++) {
			if (i > 0) {
				buffer.append('.');
			}
			buffer.append(RANDOM_PACKAGES[dice.nextInt(RANDOM_PACKAGES.length)]);
		}

		return buffer.toString();
	}

	/**
	 * @param event
	 */
	public void eventReceived(LogView4JEvent event) {
//    switch (event.getEventId().getId()) {
//      case LogView4JEventId.LOGGING_EVENT_RECEIVED_ID: {
//        eventsReceived++;
//        LogView4JLoggingEvent loggingEvent = (LogView4JLoggingEvent)event.get(LogView4JEventKey.LOGGING_EVENT);
//      }
//    }
	}

	/**
	 * @return
	 */
	public LogView4JEventId[] getEventsOfInterest() {
		return new LogView4JEventId[]{};
	}
}
