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

import java.util.Vector;

import javax.swing.table.DefaultTableModel;

import com.googlecode.bpmn_simulator.animation.element.logical.LogicalElement;
import com.googlecode.bpmn_simulator.animation.element.logical.LogicalElements;
import com.googlecode.bpmn_simulator.animation.element.logical.LogicalFlowElement;

@SuppressWarnings("serial")
class ElementsTableModel<T extends LogicalElement>
		extends DefaultTableModel {

	public static final int COLUMN_TYPE = 0;
	public static final int COLUMN_NAME = 1;
	public static final int COLUMN_STEPS = 2;

	public ElementsTableModel() {
		super();
		setColumnCount(3);
	}

	@Override
	public boolean isCellEditable(final int row, final int column) {
		if (column == COLUMN_STEPS) {
			return super.isCellEditable(row, column);
		} else {
			return false;
		}
	}

	private T getElementAt(final int rowIndex) {
		final Vector<?> row = (Vector<?>) getDataVector().elementAt(rowIndex);
		if (row != null) {
			return (T) row.elementAt(0);
		}
		return null;
	}

	@Override
	public Object getValueAt(final int row, final int column) {
		final T element = getElementAt(row);
		switch (column) {
			case COLUMN_TYPE:
				return LogicalElements.getName(element);
			case COLUMN_NAME:
				return element.toString();
			case COLUMN_STEPS:
				if (element instanceof LogicalFlowElement) {
					return ((LogicalFlowElement) element).getStepCount();
				}
				return null;
		}
		return super.getValueAt(row, column);
	}

	private static Integer valueToInt(final Object value) {
		if (value instanceof Number) {
			return ((Number) value).intValue();
		} else if (value instanceof String) {
			return Integer.parseInt((String) value);
		} else {
			throw new IllegalArgumentException();
		}
	}

	@Override
	public void setValueAt(final Object aValue, final int row, final int column) {
		final T element = getElementAt(row);
		if (column == COLUMN_STEPS) {
			if (element instanceof LogicalFlowElement) {
				final Integer stepCount = valueToInt(aValue);
				if (stepCount != null) {
					((LogicalFlowElement) element).setStepCount(stepCount);
				}
			}
		}
	}

	public void addElement(final T element) {
		addRow(new Object[] { element, null, null });
	}

	public void clear() {
		while (getRowCount() > 0) {
			removeRow(0);
		}
	}

}
