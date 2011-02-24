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
 * $Id: LogView4JEvent.java,v 1.4 2005/10/03 10:17:49 jpassenger Exp $
 */
package org.logview4j.event;

import java.util.HashMap;
import java.util.Map;

/**
 * An event class for transmitting events
 */
public class LogView4JEvent {

	/**
	 * The event id
	 */
	private final LogView4JEventId eventId;
	/**
	 * The data for the event
	 */
	private Map data = new HashMap();

	/**
	 * Constructs a new event with the requested event id
	 * @param eventId the event id for this event
	 */
	public LogView4JEvent(LogView4JEventId eventId) {
		this.eventId = eventId;
	}

	/**
	 * Fetches the event id
	 * @return the eventId for this event
	 */
	public LogView4JEventId getEventId() {
		return eventId;
	}

	/**
	 * Fetches data from this event
	 * @param key the key
	 * @return the value for the requested key
	 */
	public Object get(LogView4JEventKey key) {
		return data.get(key);
	}

	/**
	 * Stores data in this event
	 * @param key the key
	 * @param value the value to store
	 */
	public void set(LogView4JEventKey key, Object value) {
		data.put(key, value);
	}

	/**
	 * Checks to see if this event contains the requested key
	 * @param key the key to check for
	 * @return true if the key exists, false if it does not
	 */
	public boolean contains(LogView4JEventKey key) {
		return data.containsKey(key);
	}

	/**
	 * Returns this as a String
	 * @return this as a String
	 */
	public String toString() {
		StringBuffer buffer = new StringBuffer();
		buffer.append("[LogView4JEvent id=").append(getEventId().getId()).append("]");
		return buffer.toString();
	}
}
