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
 * $Id: SocketListener.java,v 1.5 2005/10/03 10:11:26 jpassenger Exp $
 */
package org.logview4j.listener;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import org.logview4j.exception.LogView4JException;
import org.logview4j.threads.LogView4JThreadManager;

/**
 * The socket listener 
 */
public class SocketListener implements Runnable {

	/**
	 * The server socket to listen through
	 */
	protected ServerSocket serverSocket;

	/**
	 * Creates a new SocketListener which attempts to open
	 * a server socket on the requested port and start accepting connections
	 * @param port the port to listen on
	 * @throws LogView4JException thrown on failure to listen on the requested port
	 */
	public SocketListener(int port) throws LogView4JException {

		try {
			SocketProcessorManager.getInstance();
			serverSocket = new ServerSocket(port);
			startAccepting();
		} catch (IOException io) {
			throw new LogView4JException("Failed to listen on port: " + port, io);
		}
	}

	/**
	 * Starts a new thread to accept connections
	 */
	protected void startAccepting() {
		Thread thread = new Thread(this);
		thread.setDaemon(true);
		thread.setPriority(Thread.NORM_PRIORITY - 1);
		thread.start();
	}

	/**
	 * Continually accepts connection on the server socket
	 * and hands off the processing to a SocketProcessor instance
	 * using a thread from the thread pool
	 */
	public void run() {
		while (true) {
			try {
				Socket socket = serverSocket.accept();

				SocketProcessor processor = new SocketProcessor(socket);
				LogView4JThreadManager.getInstance().execute(processor);
			} catch (IOException e) {
				// TODO log this exception formally
				e.printStackTrace();
			}
		}

	}
}
