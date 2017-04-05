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
package com.googlecode.bpmn_simulator.gui.log;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.core.Filter;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.Filter.Result;
import org.apache.logging.log4j.core.filter.ThresholdFilter;

import com.googlecode.bpmn_simulator.gui.Messages;

@SuppressWarnings("serial")
public class LogFrame
		extends JFrame {

	private static final String APPENDER_NAME = "LogWindow"; //$NON-NLS-1$

	private static final int DEFAULT_WIDTH = 600;
	private static final int DEFAULT_HEIGHT = 400;

	private static final Filter FILTER = ThresholdFilter.createFilter(Level.WARN, Result.ACCEPT, Result.DENY);

	private final LogList listLog = new LogList();

	private int warningCount;
	private int errorCount;

	public LogFrame() {
		super(Messages.getString("Log.messages")); //$NON-NLS-1$

		create();

		setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);
		setLocationRelativeTo(null);

		final UIThreadAppender appender = new UIThreadAppender(APPENDER_NAME, null, FILTER) {

			@Override
			public void onAppend(final LogEvent logEvent) {
				final Level level = logEvent.getLevel();
				if (level.equals(Level.WARN)) {
					addWarning(logEvent.getMessage().getFormattedMessage());
				} else if (level.equals(Level.ERROR) || level.equals(Level.FATAL)) {
					final Throwable throwable = logEvent.getThrown();
					if (throwable == null) {
						addError(logEvent.getMessage().getFormattedMessage());
					} else {
						addError(throwable.toString());
					}
				}
			}

		};
		appender.register();
	}

	protected void create() {
		getContentPane().setLayout(new BorderLayout());

		getContentPane().add(listLog, BorderLayout.CENTER);

		final JPanel panel = new JPanel();
		final JButton buttonClose = new JButton(Messages.getString("close")); //$NON-NLS-1$
		buttonClose.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent event) {
				setVisible(false);
			}
		});
		panel.add(buttonClose);
		getContentPane().add(panel, BorderLayout.PAGE_END);
	}

	protected void addWarning(final String message) {
		listLog.addWarning(message);
		++warningCount;
	}

	protected void addError(final String message) {
		listLog.addError(message);
		++errorCount;
		setVisible(true);
		toFront();
	}

	public boolean hasMessages() {
		return (warningCount + errorCount) > 0;
	}

	public boolean hasErrors() {
		return errorCount > 0;
	}

	public void clear() {
		listLog.clear();
		warningCount = 0;
		errorCount = 0;
	}

}
