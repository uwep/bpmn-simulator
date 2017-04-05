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
package com.googlecode.bpmn_simulator.gui.mdi;

import java.awt.BorderLayout;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;

import javax.swing.Action;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import javax.swing.TransferHandler;

import com.googlecode.bpmn_simulator.gui.mdi.ScrollDesktop.ScrollDesktopPane;

@SuppressWarnings("serial")
public class MdiFrame
		extends JFrame {

	private static final KeyStroke KEYSTROKE_PASTE = KeyStroke.getKeyStroke(KeyEvent.VK_V, InputEvent.CTRL_DOWN_MASK);

	private final ScrollDesktop desktop = new ScrollDesktop();

	public MdiFrame() {
		this(null);
	}

	public MdiFrame(final String title) {
		super(title);

		getContentPane().add(desktop, BorderLayout.CENTER);
	}

	public ScrollDesktopPane getDesktop() {
		return desktop.getDesktopPane();
	}

	protected JMenu createWindowMenu() {
		final WindowMenu menu = new WindowMenu();
		menu.setDesktopPane(getDesktop());
		return menu;
	}

	public void setTransferHandler(final TransferHandler handler, final boolean enablePaste) {
		final JPanel contentPane = (JPanel) getContentPane();
		contentPane.setTransferHandler(handler);
		if (enablePaste) {
			contentPane.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KEYSTROKE_PASTE, TransferHandler.getPasteAction().getValue(Action.NAME));
			contentPane.getActionMap().put(TransferHandler.getPasteAction().getValue(Action.NAME), TransferHandler.getPasteAction());
		}
	}

}
