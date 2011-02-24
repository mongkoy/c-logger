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

import org.apache.log4j.spi.*;
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
    ObjectInputStream in = null;
    
    try {
      in = new ObjectInputStream(socket.getInputStream());
      
      while (true) {
        LoggingEvent loggingEvent = (LoggingEvent) in.readObject();
        
        LogView4JLoggingEvent event = new LogView4JLoggingEvent(loggingEvent);
        loggingEvent = null;
        fireEvent(event);
      }
    } 
    catch (EOFException e) {

    }
    catch (SocketException s) {
  
    }
    catch (Throwable t) {
      t.printStackTrace();
    }
    
    /**
     * Try to close the input stream and socket
     */
    close(in);
    closeSocket();
    
    /**
     * Null out the input stream and the socket
     */
    in = null;
    socket = null;
  }

  /**
   * Closes the input stream
   * @param in the input stream
   */
  protected void close(ObjectInputStream in) {
    if (in != null) {
      try {
        in.close();
      } 
      catch (IOException e) {
        e.printStackTrace();
      }
    }
  }
  
  /**
   * Closes the client socket
   */
  protected void closeSocket() {
    if (socket != null) {
      try {
        socket.close();
      } 
      catch (IOException e) {
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
