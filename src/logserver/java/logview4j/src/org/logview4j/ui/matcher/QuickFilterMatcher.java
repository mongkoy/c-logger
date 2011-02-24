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
 * $Id: QuickFilterMatcher.java,v 1.1 2005/10/03 10:11:27 jpassenger Exp $
 */

package org.logview4j.ui.matcher;

import ca.odell.glazedlists.TextFilterator;
import ca.odell.glazedlists.impl.filter.TextMatcher;

public class QuickFilterMatcher extends TextMatcher {

  /**
   * Creates a new QuickFilterMatcher 
   * @param filterValues the filter values
   * @param filterator the filterator
   */
  public QuickFilterMatcher(String[] filterValues, TextFilterator filterator) {
    super(filterValues, filterator);
  }

}
