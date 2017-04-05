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
package com.googlecode.bpmn_simulator.gui.instances;

import javax.swing.JLabel;

import com.googlecode.bpmn_simulator.animation.element.logical.LogicalElement;
import com.googlecode.bpmn_simulator.animation.element.logical.LogicalElements;

@SuppressWarnings("serial")
public class ElementLabel
		extends JLabel {

	public void setElement(final LogicalElement element) {
		if (element == null) {
			setText(null);
		} else {
			final StringBuilder text = new StringBuilder(element.toString());
			text.append(' ');
			text.append('(');
			text.append(LogicalElements.getName(element));
			text.append(')');
			setText(text.toString());
		}
	}

}
