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
 * $Id: LogView4JException.java,v 1.5 2005/12/27 14:53:11 jpassenger Exp $
 */

package org.logview4j.exception;

/**
 * A custom exception for the client 
 */
public class LogView4JException extends Exception {

  /**
   * Creates a new exception with a message
   * @param message the message
   */
  public LogView4JException(String message) {
    super(message);
  }
  
  /**
   * Creates a new exception with a message and a root cause
   * @param message the message
   * @param t the root cause
   */
  public LogView4JException(String message, Throwable t) {
    super(message, t);
  }
  
  public String toString() {
    if (getCause() != null) {
      return getCause().toString();
    }
    else {
      return super.toString();
    }
  }
}
