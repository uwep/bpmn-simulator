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

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.net.URI;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.jdesktop.swingx.JXHyperlink;
import org.jdesktop.swingx.hyperlink.HyperlinkAction;

import com.googlecode.bpmn_simulator.animation.input.Definition;
import com.googlecode.bpmn_simulator.bpmn.model.core.infrastructure.Definitions;

@SuppressWarnings("serial")
public class ModelPropertiesDialog
		extends AbstractDialog {

	private final Definition<?> definition;

	public ModelPropertiesDialog(final JFrame parent, final Definition<?> definition) {
		super(parent, Messages.getString("Properties.properties")); //$NON-NLS-1$
		this.definition = definition;
	}

	protected static JTextField createField(final String text) {
		final JTextField textField = new JTextField(text);
		textField.setBorder(null);
		textField.setEditable(false);
		textField.setCaretPosition(0);
		return textField;
	}

	@Override
	protected JComponent createContent() {
		final JPanel panel = new JPanel(new GridBagLayout());
		panel.setBorder(createGapBorder());

		final GridBagConstraints c = new GridBagConstraints();
		c.insets = new Insets(4, 4, 4, 4);
		c.fill = GridBagConstraints.NONE;
		c.anchor = GridBagConstraints.BASELINE_LEADING;

		c.gridy = 0;

		c.gridx = 0;
		c.gridwidth = 1;
		panel.add(new JLabel(Messages.getString("Properties.encoding")), c); //$NON-NLS-1$
		c.gridx = 1;
		c.gridwidth = 2;
		panel.add(createField(definition.getEncoding()), c);

		String exporter = null;
		String exporterVersion = null;
		URI typeLanguage = null;
		URI expressionLanguage = null;
		String name = null;
		if (definition instanceof Definitions<?>) {
			final Definitions<?> definitions = (Definitions<?>) definition;
			exporter = definitions.getExporter();
			exporterVersion = definitions.getExporterVersion();
			typeLanguage = definitions.getTypeLanguage();
			expressionLanguage = definitions.getExpressionLanguage();
			name = definitions.getName();
		}

		c.gridy = 1;

		c.gridx = 0;
		c.gridwidth = 1;
		panel.add(new JLabel(Messages.getString("Properties.exporterName")), c); //$NON-NLS-1$
		c.gridx = 1;
		c.gridwidth = 2;
		panel.add(createField(exporter), c);

		c.gridy = 2;

		c.gridx = 0;
		c.gridwidth = 1;
		panel.add(new JLabel(Messages.getString("Properties.exporterVersion")), c); //$NON-NLS-1$
		c.gridx = 2;
		c.gridwidth = 1;
		panel.add(createField(exporterVersion), c);

		c.gridy = 3;

		c.gridx = 0;
		c.gridwidth = 1;
		panel.add(new JLabel(Messages.getString("Properties.typeLanguage")), c); //$NON-NLS-1$
		c.gridx = 2;
		c.gridwidth = 1;
		panel.add(new JXHyperlink(HyperlinkAction.createHyperlinkAction(typeLanguage)), c);

		c.gridy = 4;

		c.gridx = 0;
		c.gridwidth = 1;
		panel.add(new JLabel(Messages.getString("Properties.expressionLanguage")), c); //$NON-NLS-1$
		c.gridx = 2;
		c.gridwidth = 1;
		panel.add(new JXHyperlink(HyperlinkAction.createHyperlinkAction(expressionLanguage)), c);

		c.gridy = 5;

		c.gridx = 0;
		c.gridwidth = 1;
		panel.add(new JLabel(Messages.getString("Properties.name")), c); //$NON-NLS-1$
		c.gridx = 2;
		c.gridwidth = 1;
		panel.add(createField(name), c);

		return panel;
	}

	@Override
	protected JPanel createButtonPanel() {
		final JPanel panel = super.createButtonPanel();
		panel.add(createCloseButton());
		return panel;
	}

}
