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
package com.googlecode.bpmn_simulator.gui.elements;

import java.awt.BorderLayout;
import java.awt.Component;
import java.text.MessageFormat;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableColumnModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

import com.googlecode.bpmn_simulator.animation.element.logical.LogicalElement;
import com.googlecode.bpmn_simulator.animation.execution.Animator;
import com.googlecode.bpmn_simulator.animation.input.Definition;
import com.googlecode.bpmn_simulator.gui.Messages;

@SuppressWarnings("serial")
public class ElementsFrame
		extends JFrame {

	private static final int DEFAULT_WIDTH = 400;
	private static final int DEFAULT_HEIGHT = 600;

	private final ElementsTableModel<LogicalElement> model = new ElementsTableModel<>();

	private final TableColumn columnType = new TableColumn(0);
	private final TableColumn columnName = new TableColumn(1);
	private final TableColumn columnSteps = new TableColumn(2);

	private final JTable table = new JTable(model);

	public ElementsFrame() {
		super(Messages.getString("Elements.elements")); //$NON-NLS-1$

		setDefaultCloseOperation(HIDE_ON_CLOSE);

		setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);

		create();
	}

	public void setDefinition(final Definition<?> definition) {
		model.clear();
		if (definition != null) {
			for (final LogicalElement logicalElement : definition.getElements()) {
				model.addElement(logicalElement);
			}
		}
	}

	protected void create() {
		setLayout(new BorderLayout());
		table.setAutoCreateColumnsFromModel(false);
		table.setAutoCreateRowSorter(true);
		table.setCellSelectionEnabled(false);
		table.setColumnSelectionAllowed(false);
		table.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		table.setFillsViewportHeight(true);
		table.setRowHeight(28);
		table.setShowVerticalLines(false);
		final TableColumnModel columnModel = new DefaultTableColumnModel();
		columnType.setHeaderValue("Type");
		columnType.setPreferredWidth(100);
		columnModel.addColumn(columnType);
		columnName.setHeaderValue("Name");
		columnName.setPreferredWidth(200);
		columnModel.addColumn(columnName);
		columnSteps.setHeaderValue("Duration");
		columnSteps.setCellRenderer(new StepTableCellRenderer());
		columnSteps.setMaxWidth(60);
		columnModel.addColumn(columnSteps);
		table.setColumnModel(columnModel);
		getContentPane().add(new JScrollPane(table), BorderLayout.CENTER);
	}

	private static class StepTableCellRenderer
			extends DefaultTableCellRenderer {

		@Override
		public Component getTableCellRendererComponent(final JTable table,
				Object value, boolean isSelected, boolean hasFocus,
				int row, int column) {
			Object displayValue = value;
			setHorizontalAlignment(SwingConstants.RIGHT);
			if ((value != null) && (value instanceof Number)) {
				final double sec = Animator.stepsToSeconds(((Number) value).intValue());
				displayValue = MessageFormat.format("{0,number} sec", sec);
			}
			return super.getTableCellRendererComponent(table, displayValue, isSelected, hasFocus,
					row, column);
		}

	}

}
