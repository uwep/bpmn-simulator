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
package com.googlecode.bpmn_simulator.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JToolBar;

import com.googlecode.bpmn_simulator.animation.input.Definition;
import com.googlecode.bpmn_simulator.animation.token.RootInstances;

@SuppressWarnings("serial")
public class InstancesToolbar
		extends JToolBar {

	private final RootInstances instances;

	private StartButton buttonStart;
	private JButton buttonReset;

	public InstancesToolbar(RootInstances instances) {
		super();
		create();
		this.instances = instances;
		buttonStart.setInstances(instances);
	}

	public void setDefinition(final Definition<?> definition) {
		buttonStart.setDefinition(definition);
		buttonStart.updateButton();
	}

	private void create() {
		buttonStart = new StartButton(Theme.ICON_START);
		buttonStart.setToolTipText(Messages.getString("Toolbar.start")); //$NON-NLS-1$
		add(buttonStart);

		buttonReset = new JButton(Theme.ICON_RESET);
		buttonReset.setToolTipText(Messages.getString("Toolbar.reset")); //$NON-NLS-1$
		buttonReset.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent event) {
				instances.clear();
			}
		});
		add(buttonReset);
	}

}
