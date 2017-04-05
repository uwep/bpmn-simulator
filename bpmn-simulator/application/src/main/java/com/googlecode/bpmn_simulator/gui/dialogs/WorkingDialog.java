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
package com.googlecode.bpmn_simulator.gui.dialogs;

import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.Window;

import javax.swing.BorderFactory;
import javax.swing.JDialog;
import javax.swing.SwingUtilities;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.core.Filter;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.Filter.Result;
import org.apache.logging.log4j.core.filter.ThresholdFilter;
import org.jdesktop.swingx.JXBusyLabel;

import com.googlecode.bpmn_simulator.gui.log.UIThreadAppender;

@SuppressWarnings("serial")
public class WorkingDialog
		extends JDialog
		implements CallbackRunnable.Callback {

	private static final String APPENDER_NAME = "WorkingDialog";

	private static final int WIDTH = 320;
	private static final int HEIGHT = 80;

	private static final int GAP = 8;

	private static final Filter FILTER = ThresholdFilter.createFilter(Level.INFO, Result.ACCEPT, Result.DENY);

	private final JXBusyLabel busyLabel = new JXBusyLabel();

	private UIThreadAppender appender;

	public WorkingDialog(final Window owner, final String title) {
		super(owner, title + "...", ModalityType.DOCUMENT_MODAL);
		create();
		setDefaultCloseOperation(HIDE_ON_CLOSE);
		setResizable(false);
		setAlwaysOnTop(true);
		setType(Type.UTILITY);
		setSize(WIDTH, HEIGHT);
		setLocationRelativeTo(owner);
		setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
	}

	private void create() {
		getContentPane().setLayout(new BorderLayout());
		final Font currentFont = busyLabel.getFont();
		busyLabel.setFont(currentFont.deriveFont(currentFont.getSize2D() + 2.f));
		busyLabel.setIconTextGap(GAP);
		busyLabel.setBorder(BorderFactory.createEmptyBorder(GAP, GAP, GAP, GAP));
		getContentPane().add(busyLabel, BorderLayout.CENTER);
	}

	public void run(final Runnable runnable) {
		busyLabel.setBusy(true);
		appender = new UIThreadAppender(APPENDER_NAME, null, FILTER) {
			@Override
			protected void onAppend(final LogEvent event) {
				if (event.getLevel().equals(Level.INFO)) {
					busyLabel.setText(event.getMessage().getFormattedMessage());
				}
			}
		};
		appender.register();
		final Thread thread = new Thread(new CallbackRunnable(runnable, this));
		thread.start();
		setVisible(true);
	}

	@Override
	public void onFinish() {
		appender.unregister();
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				busyLabel.setBusy(false);
				setVisible(false);
			}
		});
	}

	@Override
	public void onException(final Throwable t) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				busyLabel.setText(t.toString());
			}
		});
	}

}
