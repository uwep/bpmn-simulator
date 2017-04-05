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
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.MessageFormat;
import java.util.Map;
import java.util.Properties;

import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;

@SuppressWarnings("serial")
public class PropertiesTable
		extends JTable {

	private static final KeyStroke COPY_KEYSTROKE = KeyStroke.getKeyStroke(KeyEvent.VK_C, ActionEvent.CTRL_MASK, false);

	private final TableColumn nameColumn = new TableColumn(0);
	private final TableColumn valueColumn = new TableColumn(1);

	public PropertiesTable() {
		super();

		setAutoResizeMode(AUTO_RESIZE_LAST_COLUMN);

		nameColumn.setHeaderValue("Name");
		addColumn(nameColumn);
		valueColumn.setHeaderValue("Value");
		valueColumn.setCellRenderer(new NullTableCellRenderer());
		addColumn(valueColumn);

		getTableHeader().setReorderingAllowed(false);

		setFillsViewportHeight(true);
		setAutoCreateRowSorter(true);
		setAutoCreateColumnsFromModel(false);

		registerKeyboardAction(new CopyActionListener(), "Copy", COPY_KEYSTROKE, WHEN_FOCUSED);
		addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (!e.isConsumed() && (e.getClickCount() == 2)) {
					showSelectedValue();
					e.consume();
				}
			}
		});
	}

	private static String asString(final Object object) {
		return (object == null) ? "" : object.toString();
	}

	private void showSelectedValue() {
		final int row = getSelectedRow();
		if (row > -1) {
			final int rowIndex = convertRowIndexToModel(row);
			final String name = asString(getValueAt(rowIndex, nameColumn.getModelIndex()));
			final Object value = getValueAt(rowIndex, valueColumn.getModelIndex());
			showValue(name, value);
		}
	}

	private void showValue(final String name, final Object value) {
		final ValueDialog dialog = new ValueDialog(this, name);
		dialog.setValue(value);
		dialog.setVisible(true);
	}

	protected String propertiesAsString() {
		final StringBuilder builder = new StringBuilder();
		final Properties properties = getProperties();
		for (final String name : properties.stringPropertyNames()) {
			builder.append(name);
			builder.append('=');
			builder.append(properties.getProperty(name));
			builder.append(System.lineSeparator());
		}
		return builder.toString();
	}

	public Properties getProperties() {
		final Properties properties = new Properties();
		final TableModel model = getModel();
		for (final int selectedRow : getSelectedRows()) {
			final int row = convertRowIndexToModel(selectedRow);
			final Object key = model.getValueAt(row, nameColumn.getModelIndex());
			final Object value = model.getValueAt(row, valueColumn.getModelIndex());
			properties.setProperty(key.toString(), (value == null) ? null : value.toString());
		}
		return properties;
	}

	public void setProperties(final Map<? extends Object, ? extends Object> properties) {
		final DefaultTableModel tableModel = new DefaultTableModel(0, 2);
		int nameWidth = 0;
		if (properties != null) {
			final FontMetrics fontMetrics = getFontMetrics(getFont());
			for (final Object key : properties.keySet()) {
				final String name = key.toString();
				nameWidth = Math.max(nameWidth, fontMetrics.stringWidth(name));
				tableModel.addRow(new Object[] { name, properties.get(key) });
			}
		}
		setModel(tableModel);
		final Dimension preferredSize = new Dimension(nameWidth * 2, Math.min(getRowCount(), 10) * getRowHeight());
		setPreferredScrollableViewportSize(preferredSize);
	}

	@Override
	public boolean isCellEditable(final int row, final int column) {
		return false;
	}

	private class CopyActionListener
			implements ActionListener {

		@Override
		public void actionPerformed(final ActionEvent e) {
			final Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
			clipboard.setContents(new StringSelection(propertiesAsString()), null);
		}

	}

	private static class NullTableCellRenderer
			extends DefaultTableCellRenderer {

		@Override
		public Component getTableCellRendererComponent(JTable table,
				Object value, boolean isSelected, boolean hasFocus, int row,
				int column) {
			setBackground((value == null) ? Color.LIGHT_GRAY : null);
			return super.getTableCellRendererComponent(table, value, isSelected, hasFocus,
					row, column);
		}
		
	}

	private static class ValueDialog
			extends JDialog {

		private static final int DEFAULT_WIDTH = 400;
		private static final int DEFAULT_HEIGHT = 200;

		private final JTextArea textArea = new JTextArea();

		public ValueDialog(final JComponent owner, final String name) {
			super(SwingUtilities.getWindowAncestor(owner),
					MessageFormat.format("Value for ''{0}''", name),
					ModalityType.DOCUMENT_MODAL);

			setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

			create();

			setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);
			setLocationRelativeTo(SwingUtilities.getWindowAncestor(owner));
		}

		private void create() {
			getContentPane().setLayout(new BorderLayout());
			textArea.setLineWrap(true);
			textArea.setEditable(false);
			getContentPane().add(textArea, BorderLayout.CENTER);
		}

		public void setValue(final Object value) {
			if (value != null) {
				textArea.setText(value.toString());
			} else {
				textArea.setText(null);
			}
		}

	}

}
