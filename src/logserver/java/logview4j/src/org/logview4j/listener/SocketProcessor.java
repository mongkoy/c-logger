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

/**
 * A socket processor processes socket connections
 * and reads off logging events from them, dispatching
 * the logging event to the LogReceiver
 */
public class SocketProcessor extends DataProcessor {

	protected Socket socket;

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
		if (socket != null) {
			try {
				readData(socket.getInputStream(), true);
			} catch (IOException ex) {
				ex.printStackTrace();
			}
			try {
				socket.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
	}
}
