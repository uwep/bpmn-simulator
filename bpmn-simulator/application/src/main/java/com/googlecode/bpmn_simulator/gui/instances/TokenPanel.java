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

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;

import com.googlecode.bpmn_simulator.animation.element.logical.LogicalElement;
import com.googlecode.bpmn_simulator.animation.token.Token;
import com.googlecode.bpmn_simulator.animation.token.TokenFlow;

@SuppressWarnings("serial")
class TokenPanel
		extends AbstractInfoPanel {

	private final ElementLabel currentElementLabel = new ElementLabel();
	private final ElementLabel previousElementLabel = new ElementLabel();

	@Override
	protected JPanel createInfoPanel() {
		final JPanel panel = new JPanel(new GridBagLayout());
		final GridBagConstraints c = new GridBagConstraints();
		c.insets = DEFAULT_INSETS;
		c.anchor = GridBagConstraints.LINE_START;
		c.fill = GridBagConstraints.HORIZONTAL;

		c.gridy = 0;
		c.gridx = 0;
		panel.add(new JLabel("Element:"), c);
		c.gridx = 1;
		c.weightx = 0.75;
		panel.add(currentElementLabel, c);
		c.weightx = 0.;

		c.gridy = 1;
		c.gridx = 0;
		panel.add(new JLabel("Previous Element:"), c);
		c.gridx = 1;
		panel.add(previousElementLabel, c);

		return panel;
	}

	private static void setTokenFlow(final ElementLabel elementLabel, final TokenFlow tokenFlow) {
		if (tokenFlow instanceof LogicalElement) {
			elementLabel.setElement((LogicalElement) tokenFlow);
		} else {
			elementLabel.setElement(null);
		}
	}

	public void setToken(final Token token) {
		setTokenFlow(currentElementLabel, token.getCurrentTokenFlow());
		setTokenFlow(previousElementLabel, token.getPreviousTokenFlow());
		setData(token.getData());
	}

}
