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
import java.util.Collection;

import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.SwingUtilities;

import com.googlecode.bpmn_simulator.animation.element.logical.LogicalElements;
import com.googlecode.bpmn_simulator.animation.element.logical.LogicalFlowElement;
import com.googlecode.bpmn_simulator.animation.input.Definition;
import com.googlecode.bpmn_simulator.animation.token.RootInstances;

@SuppressWarnings("serial")
public class StartButton
		extends JButton
		implements ActionListener {

	private RootInstances instances;

	private Definition<?> definition;

	public StartButton(final Icon icon) {
		super(icon);
		addActionListener(this);
		updateButton();
	}

	public void setDefinition(final Definition<?> definition) {
		this.definition = definition;
		updateButton();
	}

	public void setInstances(final RootInstances instances) {
		this.instances = instances;
		updateButton();
	}

	protected void updateButton() {
		setEnabled((definition != null) && (instances != null));
	}

	private void instantiate(final LogicalFlowElement instantiatingElement) {
		instances.addNewChildInstance(null).createNewToken(instantiatingElement, null);
	}

	private JPopupMenu createMenu(final Collection<LogicalFlowElement> instantiatingElements) {
		final JPopupMenu menu = new JPopupMenu();
		for (final LogicalFlowElement element : instantiatingElements) {
			final StringBuilder text = new StringBuilder();
			text.append(LogicalElements.getName(element));
			text.append(" - ");
			// UweP 2017
			text.append(element);
			final JMenuItem menuItem =  new JMenuItem(text.toString());
			menuItem.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(final ActionEvent event) {
					instantiate(element);
				}
			});
			menu.add(menuItem);
		}
		return menu;
	}

	@Override
	public void actionPerformed(final ActionEvent e) {
		final Collection<LogicalFlowElement> elements = definition.getInstantiatingElements();
		if (elements.size() == 1) {
			instantiate(elements.iterator().next());
		} else if (elements.size() > 1) {
			createMenu(elements).show(this, 0, getHeight());
		} else {
			JOptionPane.showMessageDialog(SwingUtilities.getRoot(this),
					"A process can not be startet.\n"
					+ "There are no start elements in any process.",
					Messages.getString("error"),
					JOptionPane.ERROR_MESSAGE);
		}
	}

}
