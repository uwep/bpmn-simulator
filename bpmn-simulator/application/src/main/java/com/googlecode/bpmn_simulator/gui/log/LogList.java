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

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;

import javax.swing.Icon;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

import com.googlecode.bpmn_simulator.gui.Theme;

@SuppressWarnings("serial")
public class LogList
		extends JScrollPane {

	public LogList() {
		super(new LogListContent());
		setPreferredSize(new Dimension(100, 80));
	}

	private LogListContent getContent() {
		return (LogListContent) getViewport().getView();
	}

	public void addError(final String message) {
		getContent().addMessage(Theme.ICON_ERROR, message);
	}

	public void addWarning(final String message) {
		getContent().addMessage(Theme.ICON_WARNING, message);
	}

	public void clear() {
		getContent().clear();
	}

	private static class LogListContent
			extends JTable {

		private static final int TYPE_COLUMN_INDEX = 0;
		private static final int MESSAGE_COLUMN_INDEX = 1;

		private static final int ROW_HEIGHT = 18;
		private static final int TYPE_COLUMN_WIDTH = 20;

		public LogListContent() {
			super();

			initColumns();
			initPopupMenu();

			setShowGrid(false);
			setFillsViewportHeight(true);
			setTableHeader(null);
			setRowHeight(ROW_HEIGHT);
			setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			setFocusable(false);
		}

		@Override
		public String getToolTipText(MouseEvent event) {
			final int rowIndex = rowAtPoint(event.getPoint());
			if (rowIndex > -1) {
				return getModel().getValueAt(rowIndex, MESSAGE_COLUMN_INDEX).toString();
			}
			return super.getToolTipText(event);
		}

		@Override
		public boolean getScrollableTracksViewportWidth() {
			return getPreferredSize().width < getParent().getWidth();
		}

		private void initColumns() {
			setAutoCreateColumnsFromModel(false);

			final DefaultTableModel model = (DefaultTableModel) getModel();
			model.setColumnCount(2);

			final TableColumn typeColumn = new TableColumn(TYPE_COLUMN_INDEX);
			typeColumn.setResizable(false);
			typeColumn.setWidth(TYPE_COLUMN_WIDTH);
			typeColumn.setMinWidth(TYPE_COLUMN_WIDTH);
			typeColumn.setMaxWidth(TYPE_COLUMN_WIDTH);
			final LogTableCellRenderer cellRenderer = new LogTableCellRenderer();
			cellRenderer.setHorizontalAlignment(SwingConstants.CENTER);
			cellRenderer.setVerticalAlignment(SwingConstants.CENTER);
			typeColumn.setCellRenderer(cellRenderer);

			getColumnModel().addColumn(typeColumn);
			getColumnModel().addColumn(new AutosizeTableColumn(this, MESSAGE_COLUMN_INDEX));
		}

		private void copySelectedRowsToClipboard() {
			final Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
			final StringBuilder content = new StringBuilder();
			for (int rowIndex : getSelectedRows()) {
				final String message = (String) getModel().getValueAt(rowIndex, MESSAGE_COLUMN_INDEX);
				content.append(message).append(System.lineSeparator());
			}
			clipboard.setContents(new StringSelection(content.toString()), null);
		}

		private void initPopupMenu() {
			final JPopupMenu menu = new JPopupMenu();
			final JMenuItem copyMenuItem = new JMenuItem("Copy message to clipboard");
			copyMenuItem.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(final ActionEvent e) {
					copySelectedRowsToClipboard();
				}
			});
			menu.add(copyMenuItem);
			menu.addPopupMenuListener(new PopupMenuListener() {
				@Override
				public void popupMenuWillBecomeVisible(final PopupMenuEvent e) {
					copyMenuItem.setEnabled(getSelectedRowCount() > 0);
				}
				@Override
				public void popupMenuWillBecomeInvisible(final PopupMenuEvent e) {
				}
				@Override
				public void popupMenuCanceled(final PopupMenuEvent e) {
				}
			});
			setComponentPopupMenu(menu);
		}

		@Override
		public boolean isCellEditable(final int row, final int column) {
			return false;
		}

		public synchronized void clear() {
			((DefaultTableModel) getModel()).getDataVector().removeAllElements();
			updateUI();
		}

		/*
		public void scrollToEnd() {
			scrollRectToVisible(getCellRect(getRowCount() - 1, 0, true));
		}
		*/

		protected void addMessage(final Icon icon, final String message) {
			// swing componenten sind nicht thread-safe
			// deshalb muss SwingUtilities.invokeLater verwenden werden
			SwingUtilities.invokeLater(new LogMessage(icon, message));
		}

		private class LogMessage
				implements Runnable {

			private final Icon icon;
			private final String message;

			public LogMessage(final Icon icon, final String message) {
				this.icon = icon;
				this.message = message;
			}

			@Override
			public void run() {
				final DefaultTableModel model = (DefaultTableModel) getModel();
				model.addRow(new Object[] {
						icon,
						message,
						});
			}

		}

		private static class LogTableCellRenderer
				extends DefaultTableCellRenderer {

			@Override
			public void setValue(final Object value) {
				if (value instanceof Icon) {
					setIcon((Icon)value);
					setText(null);
				} else {
					setIcon(null);
					super.setValue(value);
				}
			}

		}

	}

}
