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
 * $Id: RegexFilterMatcher.java,v 1.1 2006/02/22 02:04:29 jpassenger Exp $
 */
package org.logview4j.ui.matcher;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Pattern;

import ca.odell.glazedlists.TextFilterator;
import ca.odell.glazedlists.matchers.Matcher;

public class RegexFilterMatcher implements Matcher {

	private TextFilterator filtrator;
	private Pattern pattern;

	public RegexFilterMatcher(Pattern pattern, TextFilterator filtrator) {
		super();
		this.pattern = pattern;
		this.filtrator = filtrator;
	}

	public boolean matches(Object objectToMatch) {
		boolean matches = false;
		List strings = new ArrayList();
		filtrator.getFilterStrings(strings, objectToMatch);

		for (Iterator iter = strings.iterator(); !matches && iter.hasNext();) {
			String s = (String) iter.next();
			java.util.regex.Matcher m = pattern.matcher(s);
			matches = m.find();
		}
		return matches;
	}
}
