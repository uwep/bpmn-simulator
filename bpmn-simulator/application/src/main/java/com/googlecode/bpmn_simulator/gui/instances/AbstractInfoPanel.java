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
package com.googlecode.bpmn_simulator.gui.instances;

import java.awt.BorderLayout;
import java.awt.Insets;
import java.util.Map;

import javax.swing.JPanel;
import javax.swing.JScrollPane;

import com.googlecode.bpmn_simulator.gui.PropertiesTable;

@SuppressWarnings("serial")
abstract class AbstractInfoPanel
		extends JPanel {

	protected static final Insets DEFAULT_INSETS = new Insets(8, 8, 8, 8);

	private final PropertiesTable dataTable = new PropertiesTable();

	public AbstractInfoPanel() {
		super(new BorderLayout());
	}

	public void create() {
		add(createInfoPanel(), BorderLayout.PAGE_START);
		add(new JScrollPane(dataTable), BorderLayout.CENTER);
	}

	protected abstract JPanel createInfoPanel();

	public void setData(final Map<? extends Object, ? extends Object> data) {
		dataTable.setProperties(data);
	}

}
