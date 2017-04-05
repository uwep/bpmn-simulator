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

import java.awt.Font;
import java.awt.Insets;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;

import org.jdesktop.swingx.JXHyperlink;
import org.jdesktop.swingx.hyperlink.HyperlinkAction;

@SuppressWarnings("serial")
public class AboutDialog
		extends AbstractDialog {

	public AboutDialog(final JFrame owner) {
		super(owner, Messages.getString("About.about")); //$NON-NLS-1$
	}

	@Override
	protected JComponent createContent() {
		final JTabbedPane pane = new JTabbedPane();
		pane.addTab(Messages.getString("About.info"), createTabInfo()); //$NON-NLS-1$
		pane.addTab(Messages.getString("About.licence"), createTabLicence()); //$NON-NLS-1$
		pane.addTab(Messages.getString("About.properties"), createTabProperties());
		return pane;
	}

	@Override
	protected JPanel createButtonPanel() {
		final JPanel panel = super.createButtonPanel();
		panel.add(createCloseButton());
		return panel;
	}

	protected JPanel createTabInfo() {
		final JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

		panel.add(Box.createVerticalStrut(20));

		final StringBuilder applicationInfo =
				new StringBuilder(ApplicationInfo.getName());
		final String version = ApplicationInfo.getVersion();
		if (version != null) {
			applicationInfo.append(' ');
			applicationInfo.append(version);
		}
		final JLabel labelInfo = new JLabel(applicationInfo.toString());
		labelInfo.setFont(labelInfo.getFont().deriveFont(Font.BOLD, 20));
		labelInfo.setAlignmentX(CENTER_ALIGNMENT);
		panel.add(labelInfo);

		panel.add(Box.createVerticalStrut(20));

		final JXHyperlink website = new JXHyperlink(HyperlinkAction.createHyperlinkAction(ApplicationInfo.getWebsite()));
		website.setAlignmentX(CENTER_ALIGNMENT);
		panel.add(website);

		return panel;
	}

	protected JComponent createTabLicence() {
		final JTextArea textArea =
				new JTextArea(ApplicationInfo.getLicense());
		textArea.setEditable(false);
		textArea.setFont(new Font("Courier New", Font.PLAIN, 11)); //$NON-NLS-1$
		textArea.setMargin(new Insets(8, 8, 8, 8));
		final JScrollPane scrollPane = new JScrollPane(textArea);
		scrollPane.setBorder(null);
		return scrollPane;
	}

	protected JComponent createTabProperties() {
		final PropertiesTable propertiesTable = new PropertiesTable();
		propertiesTable.setProperties(System.getProperties());
		return new JScrollPane(propertiesTable);
	}

}
