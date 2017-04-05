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

import javax.swing.SwingUtilities;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Filter;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.Logger;
import org.apache.logging.log4j.core.appender.AbstractAppender;

@SuppressWarnings("serial")
public abstract class UIThreadAppender
		extends AbstractAppender {

	private final String loggerName;

	protected UIThreadAppender(final String name,
			final String loggerName, final Filter filter) {
		super(name, filter, null, false);
		this.loggerName = loggerName;
		start();
	}

	@Override
	public final void append(final LogEvent event) {
		if (SwingUtilities.isEventDispatchThread()) {
			onAppend(event);
		} else {
			SwingUtilities.invokeLater(new Runnable() {
				@Override
				public void run() {
					onAppend(event);
				}
			});
		}
	}

	protected abstract void onAppend(LogEvent event);

	private Logger getLogger() {
		if (loggerName == null) {
			return (Logger) LogManager.getRootLogger();
		} else {
			return (Logger) LogManager.getLogger(loggerName);
		}
	}

	public void register() {
		getLogger().addAppender(this);
	}

	public void unregister() {
		getLogger().removeAppender(this);
	}

}
