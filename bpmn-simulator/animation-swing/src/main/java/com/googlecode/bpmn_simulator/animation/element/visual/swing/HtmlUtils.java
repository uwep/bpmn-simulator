/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package com.googlecode.bpmn_simulator.animation.element.visual.swing;

import java.util.regex.Pattern;

public final class HtmlUtils {

	private static final String TAG_BR = "<br />"; //$NON-NLS-1$

	private static final Pattern REGEX_LINEBREAK = Pattern.compile("\r?\n"); //$NON-NLS-1$

	private HtmlUtils() {
		super();
	}

	public static String nl2br(final String text) {
		if (text == null) {
			return null;
		}
		return REGEX_LINEBREAK.matcher(text).replaceAll(TAG_BR);
	}

}
