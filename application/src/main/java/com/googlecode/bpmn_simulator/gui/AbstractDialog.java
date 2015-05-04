/*
 * Copyright (C) 2012 Stefan Schweitzer
 *
 * This software was created by Stefan Schweitzer as a student's project at
 * Fachhochschule Kaiserslautern (University of Applied Sciences).
 * Supervisor: Professor Dr. Thomas Allweyer. For more information please see
 * http://www.fh-kl.de/~allweyer
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this Software except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.googlecode.bpmn_simulator.gui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.Border;

@SuppressWarnings("serial")
public abstract class AbstractDialog
		extends JDialog {

	protected static final int GAP = 10;

	protected static final Insets INSETS = new Insets(4, 4, 4, 4);

	protected static final int DEFAULT_BUTTON_WIDTH = 100;

	public AbstractDialog(final JFrame parent, final String title) {
		super(parent, title, true);

		setResizable(false);

		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
	}

	protected static Border createGapBorder() {
		return BorderFactory.createEmptyBorder(GAP, GAP, GAP, GAP);
	}

	protected static void setComponentWidth(final JComponent component, final int width) {
		final Dimension dimension = component.getPreferredSize();
		dimension.width = width;
		component.setPreferredSize(dimension);
	}

	protected static Component createDirectoryEdit(final JTextField textField) {
		final JPanel panel = new JPanel(new BorderLayout());
		panel.add(textField, BorderLayout.CENTER);
		final JButton button = new JButton("...");
		panel.add(button, BorderLayout.LINE_END);
		button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent event) {
				final JFileChooser fileChooser = new JFileChooser(textField.getText());
				fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				fileChooser.setAcceptAllFileFilterUsed(true);
				if (fileChooser.showOpenDialog(textField) == JFileChooser.APPROVE_OPTION) {
					textField.setText(fileChooser.getSelectedFile().getAbsolutePath());
				}
			}
		});
		return panel;
	}

	public void showDialog() {
		pack();
		setLocationRelativeTo(getParent());
		setVisible(true);
	}

	protected void create() {
		getContentPane().setLayout(new BorderLayout());

		getContentPane().add(createButtonPanel(), BorderLayout.PAGE_END);
	}

	protected JPanel createButtonPanel() {
		final JPanel panel = new JPanel(new FlowLayout(FlowLayout.TRAILING));
		return panel;
	}

}
