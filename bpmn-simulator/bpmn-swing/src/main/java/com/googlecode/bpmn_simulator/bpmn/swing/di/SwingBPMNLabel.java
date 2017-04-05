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
package com.googlecode.bpmn_simulator.bpmn.swing.di;

import java.awt.Graphics;

import com.googlecode.bpmn_simulator.animation.element.visual.Font;
import com.googlecode.bpmn_simulator.animation.element.visual.swing.AbstractLabel;
import com.googlecode.bpmn_simulator.animation.element.visual.swing.FontUtils;
import com.googlecode.bpmn_simulator.animation.ref.Reference;
import com.googlecode.bpmn_simulator.bpmn.di.BPMNLabel;
import com.googlecode.bpmn_simulator.bpmn.di.BPMNLabelStyle;

@SuppressWarnings("serial")
public class SwingBPMNLabel
		extends AbstractLabel
		implements BPMNLabel {

	private Reference<BPMNLabelStyle> style;

	private Font font;

	public SwingBPMNLabel() {
		super();
	}

	@Override
	public void setStyle(final Reference<BPMNLabelStyle> style) {
		this.style = style;
	}

	@Override
	protected void paintComponent(final Graphics g) {
		if ((style != null) && style.hasReference() && (style.getReferenced().getFont() != null)) {
			if (font == null) {
				font = style.getReferenced().getFont();
				setFont(getFont().deriveFont(FontUtils.toAttributes(font)));
			}
		}
		super.paintComponent(g);
	}

}
