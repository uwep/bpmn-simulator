package com.googlecode.bpmn_simulator.gui.preferences;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import javax.swing.table.AbstractTableModel;

@SuppressWarnings("serial")
public class ElementsTableModel
		extends AbstractTableModel {

	protected static final int COLUMN_TYPE = 0;
	protected static final int COLUMN_NAME = 1;
	protected static final int COLUMN_STEP_COUNT = 2;
	protected static final int COLUMN_FOREGROUND_COLOR = 3;
	protected static final int COLUMN_BACKGROUND_COLOR = 4;

	private final List<ElementsTableModel.Element> elements = new ArrayList<>();

	public ElementsTableModel() {
		super();
	}

	public void addElement(final ElementsTableModel.Element element) {
		if (elements.add(element)) {
			final int rowIndex = elements.size() - 1;
			fireTableRowsInserted(rowIndex, rowIndex);
		}
	}

	public void clear() {
		elements.clear();
		fireTableDataChanged();
	}

	@Override
	public int getRowCount() {
		return elements.size();
	}

	@Override
	public int getColumnCount() {
		return 5;
	}

	@Override
	public Class<?> getColumnClass(final int columnIndex) {
		switch (columnIndex) {
			case COLUMN_TYPE:
			case COLUMN_NAME:
				return String.class;
			case COLUMN_STEP_COUNT:
				return Integer.class;
			case COLUMN_FOREGROUND_COLOR:
			case COLUMN_BACKGROUND_COLOR:
				return Color.class;
			default:
				throw new IllegalArgumentException();
		}
	}

	@Override
	public Object getValueAt(final int rowIndex, final int columnIndex) {
		final ElementsTableModel.Element element = elements.get(rowIndex);
		if (element != null) {
			switch (columnIndex) {
				case COLUMN_TYPE:
					return element.getType();
				case COLUMN_NAME:
					return element.getName();
				case COLUMN_STEP_COUNT:
					return element.getStepCount();
				case COLUMN_FOREGROUND_COLOR:
					return element.getForegroundColor();
				case COLUMN_BACKGROUND_COLOR:
					return element.getBackgroundColor();
				default:
					throw new IllegalArgumentException();
			}
		}
		return null;
	}

	@Override
	public boolean isCellEditable(final int rowIndex, final int columnIndex) {
		return (columnIndex != COLUMN_TYPE) && (columnIndex != COLUMN_NAME);
	}

	@Override
	public void setValueAt(final Object aValue, final int rowIndex, final int columnIndex) {
		final ElementsTableModel.Element element = elements.get(rowIndex);
		if (element != null) {
			switch (columnIndex) {
				case COLUMN_STEP_COUNT:
					element.setStepCount((Integer) aValue);
					break;
				case COLUMN_FOREGROUND_COLOR:
					element.setForegroundColor((Color) aValue);
					break;
				case COLUMN_BACKGROUND_COLOR:
					element.setBackgroundColor((Color) aValue);
					break;
				default:
					throw new IllegalArgumentException();
			}
		}
	}

	public interface Element {
		String getType();
		String getName();
		Integer getStepCount();
		Color getForegroundColor();
		Color getBackgroundColor();
		void setStepCount(int count);
		void setForegroundColor(Color color);
		void setBackgroundColor(Color color);
	}

}
