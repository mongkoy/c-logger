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
 * $Id: SocketProcessorManager.java,v 1.3 2005/12/27 14:53:11 jpassenger Exp $
 */
package org.logview4j.listener;

import org.logview4j.event.LogView4JEvent;
import org.logview4j.event.LogView4JEventId;
import org.logview4j.event.LogView4JEventKey;
import org.logview4j.event.LogView4JEventListener;
import org.logview4j.event.LogView4JEventManager;

/**
 * Manages socket processors
 */
public class SocketProcessorManager implements LogView4JEventListener {

	protected volatile boolean paused = false;
	private static final SocketProcessorManager instance = new SocketProcessorManager();

	private SocketProcessorManager() {
		LogView4JEventManager.getInstance().register(this);
	}

	public static SocketProcessorManager getInstance() {
		return instance;
	}

	public boolean isPaused() {
		return paused;
	}

	/**
	 * Fired when the system is paused or unpaused
	 * @param event the event
	 */
	public void eventReceived(LogView4JEvent event) {
		paused = ((Boolean) event.get(LogView4JEventKey.PAUSED)).booleanValue();
	}

	public LogView4JEventId[] getEventsOfInterest() {
		return new LogView4JEventId[]{
					LogView4JEventId.PAUSE_PROCESSING_EVENTS
				};
	}
}
