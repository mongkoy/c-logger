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
 * $Id: ConfigurationManager.java,v 1.2 2006/06/07 04:33:56 jpassenger Exp $
 */

package org.logview4j.config;

import java.io.*;
import java.util.*;

/**
 * Manages configuration of the system 
 */
public class ConfigurationManager {
  
  private static final ConfigurationManager INSTANCE = new ConfigurationManager();
  
  private static final String CONFIG_FILE = "./config/logview4j.properties";
  
  private Properties properties = new Properties();
  
  private ConfigurationManager() {
    Properties p = loadProperties();
    properties.putAll(p);
  }
  
  public static ConfigurationManager getInstance() {
    return INSTANCE;
  }
  
  public String getString(ConfigurationKey key, String defaultValue) {
    return properties.getProperty(key.getKey(), defaultValue);
  }
  
  public String getString(ConfigurationKey key) {
    return properties.getProperty(key.getKey());
  }
  
  /**
   * Fetch an int from the properties file
   * @param key the key
   * @param defaultValue the default value
   * @return the value or the default value if the key is not set
   * or is not a valid long
   */
  public int getInt(ConfigurationKey key, int defaultValue) {
    int result = defaultValue;
    
    String value = properties.getProperty(key.getKey(), "" + defaultValue);
    
    try {
      result = Integer.parseInt(value);
    } 
    catch (NumberFormatException e) {
      System.out.println("ConfigurationManager.getInt() Invalid int for key: " + key);
      e.printStackTrace();
    }
    
    return result;
  }
  
  /**
   * Fetches a boolena value, returning false by default
   * @param key the key to fetch a boolean for
   * @return false if invalid or true if true is set
   */
  public boolean getBoolean(ConfigurationKey key) {
      String value = properties.getProperty(key.getKey(), "false");
      return Boolean.valueOf(value).booleanValue();
  }
  
  
  /**
   * Fetch an long from the properties file
   * @param key the key
   * @param defaultValue the default value
   * @return the value or the default value if the key is not set
   * or is not a valid long
   */
  public long getLong(ConfigurationKey key, long defaultValue) {
    long result = defaultValue;
    
    String value = properties.getProperty(key.getKey(), "" + defaultValue);
    
    try {
      result = Long.parseLong(value);
    } 
    catch (NumberFormatException e) {
      System.out.println("ConfigurationManager.getLong() Invalid long for key: " + key);
      e.printStackTrace();
    }
    
    return result;
  }
  
  /**
   * Loads the properties file
   * @throws IOException on failure
   */
  private Properties loadProperties() {
    Properties p = new Properties();
    InputStream in = null;
    File file = new File(CONFIG_FILE);
    
    try {
      in = new FileInputStream(file);
      p.load(in);
    } 
    catch (Throwable t) {
      System.out.println("ConfigurationManager.loadProperties() failed to load properties from file: " + file.getAbsolutePath());
      t.printStackTrace();
    }
    finally {
      if (in != null) {
        try {
          in.close();
        } 
        catch (IOException e) {
          e.printStackTrace();
        }
      }
    }
    
    return p;
  }


  
  
  

}
