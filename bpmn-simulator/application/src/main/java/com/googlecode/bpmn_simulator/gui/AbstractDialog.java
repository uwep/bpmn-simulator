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

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.border.Border;

@SuppressWarnings("serial")
public abstract class AbstractDialog
		extends JDialog {

	private static final String KEY_COMMAND_CLOSE = "command_close";

	private static final KeyStroke KEYSTROKE_ESC = KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0);

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
		create();
		pack();
		setLocationRelativeTo(getParent());
		loadData();
		setVisible(true);
	}

	private void create() {
		getContentPane().setLayout(new BorderLayout());
		getContentPane().add(createContent(), BorderLayout.CENTER);
		getContentPane().add(createButtonPanel(), BorderLayout.PAGE_END);
	}

	protected abstract JComponent createContent();

	protected JPanel createButtonPanel() {
		final JPanel panel = new JPanel(new FlowLayout(FlowLayout.TRAILING));
		return panel;
	}

	private Action createCloseAction(final String text) {
		final Action actionClose = new AbstractAction(text) {
			@Override
			public void actionPerformed(final ActionEvent e) {
				dispatchEvent(new WindowEvent(AbstractDialog.this, WindowEvent.WINDOW_CLOSING));
			}
		};
		getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KEYSTROKE_ESC, KEY_COMMAND_CLOSE);
		getRootPane().getActionMap().put(KEY_COMMAND_CLOSE, actionClose);
		return actionClose;
	}

	protected JButton createCloseButton() {
		final JButton buttonClose = new JButton(createCloseAction(Messages.getString("close"))); //$NON-NLS-1$
		setComponentWidth(buttonClose, DEFAULT_BUTTON_WIDTH);
		buttonClose.setMnemonic(KeyEvent.VK_C);
		getRootPane().setDefaultButton(buttonClose);
		return buttonClose;
	}

	protected JButton createOkButton() {
		final JButton buttonOk = new JButton(Messages.getString("ok"));  //$NON-NLS-1$
		setComponentWidth(buttonOk, DEFAULT_BUTTON_WIDTH);
		buttonOk.setMnemonic(KeyEvent.VK_O);
		buttonOk.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent e) {
				storeData();
				dispose();
			}
		});
		getRootPane().setDefaultButton(buttonOk);
		return buttonOk;
	}

	protected JButton createCancelButton() {
		final JButton buttonCancel = new JButton(createCloseAction(Messages.getString("cancel"))); //$NON-NLS-1$
		setComponentWidth(buttonCancel, DEFAULT_BUTTON_WIDTH);
		buttonCancel.setMnemonic(KeyEvent.VK_C);
		return buttonCancel;
	}

	protected void loadData() {
	}

	protected void storeData() {
	}

}
