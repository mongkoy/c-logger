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
 * $Id: ConfigurationKey.java,v 1.3 2006/06/07 04:33:56 jpassenger Exp $
 */
package org.logview4j.config;

/**
 * A key into the configuration manager 
 */
public class ConfigurationKey {

	/**
	 * The key
	 */
	private final String key;

	/**
	 * Private constructor for type safe enum
	 * @param key the new key
	 */
	private ConfigurationKey(String key) {
		this.key = key;
	}

	/**
	 * Fetches the key
	 * @return
	 */
	public String getKey() {
		return key;
	}
	public static final ConfigurationKey LISTEN_PORT = new ConfigurationKey("logview4j.listen.port");
	public static final ConfigurationKey VERSION = new ConfigurationKey("logview4j.version");
	public static final ConfigurationKey MILLIS_BETWEEN_UPDATES = new ConfigurationKey("logview4j.millis.between.updates");
	public static final ConfigurationKey MAX_EVENTS = new ConfigurationKey("logview4j.max.events");
	public static final ConfigurationKey MAX_EVENT_PAYLOAD_SIZE = new ConfigurationKey("logview4j.max.event.payload.size");
	public static final ConfigurationKey RECORD_EVENTS_WHEN_PAUSED = new ConfigurationKey("logview4j.record.events.when.paused");
	public static final ConfigurationKey LOG_FILENAME_FORMAT = new ConfigurationKey("logview4j.log.filename.format");
}
