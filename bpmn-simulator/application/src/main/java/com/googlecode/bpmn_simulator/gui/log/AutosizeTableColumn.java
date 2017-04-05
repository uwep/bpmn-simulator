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

import java.awt.Component;

import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;

@SuppressWarnings("serial")
public class AutosizeTableColumn
		extends TableColumn {

	private final JTable table;

	public AutosizeTableColumn(final JTable table, final int modelIndex) {
		super(modelIndex);
		this.table = table;
	}

	private int getPrefCellWidth(final int rowIndex, final int columnIndex) {
		final Object value = table.getValueAt(rowIndex, columnIndex);
		final TableCellRenderer renderer = table.getCellRenderer(rowIndex, columnIndex);
		final Component component = renderer.getTableCellRendererComponent(table,
				value, false, false, rowIndex, columnIndex);
		return (int) component.getPreferredSize().getWidth() + 4;
	}

	@Override
	public int getPreferredWidth() {
		int columnWidth = 0;
		final int columnIndex = getModelIndex();
		for (int rowIndex = 0; rowIndex < table.getRowCount(); ++rowIndex) {
			final int cellWidth = getPrefCellWidth(rowIndex, columnIndex);
			if (cellWidth > columnWidth) {
				columnWidth = cellWidth;
			}
		}
		return columnWidth;
	}

}
