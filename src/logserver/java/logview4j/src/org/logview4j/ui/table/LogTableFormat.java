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
 * $Id: LogTableFormat.java,v 1.1 2005/10/06 12:54:42 jpassenger Exp $
 */

package org.logview4j.ui.table;

import java.util.Comparator;

import ca.odell.glazedlists.GlazedLists;
import ca.odell.glazedlists.impl.beans.BeanTableFormat;


/**
 * A TableFormat that knows how to sort dates 
 */
public class LogTableFormat extends BeanTableFormat {

  private final boolean [] caseInsensitiveColumnnIndex;
  
  /**
   * Creates a new log table format that is case insensitive for 
   * string columns
   * @param propertyNames the property names
   * @param columnNames the column names
   */
  public LogTableFormat(Class beanClass, String [] propertyNames, String [] columnNames, int [] caseInsensitiveColumnns) {
    super(beanClass, propertyNames, columnNames);

    caseInsensitiveColumnnIndex = indexCaseInsensitiveColumns(caseInsensitiveColumnns);
  }

  /**
   * Indexes the case insensitive columns
   * @param caseInsensitiveColumnns the case insensitive columns
   * @return the indexed columns
   */
  private boolean[] indexCaseInsensitiveColumns(int [] columns) {
    boolean [] index = new boolean [super.columnLabels.length];
    
    /**
     * Mark the column as case insensitive if 
     * requested
     */
    for (int i = 0; i < columns.length; i++) {
      if (columns[i] > -1 && columns[i] < index.length) {
        index[columns[i]] = true;
      }
    }
  
    return index;
  }



  /**
   * Fetches the comparator for the requested column
   * @param column the column to fetch the comparator for
   * @return the comparator for the requested column
   */
  public Comparator getColumnComparator(int column) {
    if (caseInsensitiveColumnnIndex[column]) {
      return GlazedLists.caseInsensitiveComparator();
    }
    else {
      return super.getColumnComparator(column);
    }
  }
}
