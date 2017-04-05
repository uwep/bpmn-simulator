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
package com.googlecode.bpmn_simulator.tokenimport.bonita;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.border.Border;

import org.bonitasoft.engine.bpm.process.ProcessDeploymentInfo;
import org.bonitasoft.engine.identity.User;

@SuppressWarnings("serial")
class ConfigurationDialog
		extends JDialog {

	private static final int GAP = 4;

	private static Border EMPTY_BORDER = BorderFactory.createEmptyBorder(GAP, GAP, GAP, GAP);

	private final JTable processTable = new JTable();
	private final ListTableModel<ProcessDeploymentInfo> processModel;

	private final JCheckBox failedActivitiesCheck = new JCheckBox("Auch fehlgeschlagene Tasks importieren");

	private final JComboBox<UserFilter> userFilterComboBox = new JComboBox<>(UserFilter.values());

	private final JTable userTable = new JTable();
	private final ListTableModel<User> userModel;

	private boolean ok = false;

	public ConfigurationDialog(final List<ProcessDeploymentInfo> processes, final List<User> users) {
		super((Frame) null, "Token Import aus Bonita", true);

		processModel = new ListTableModel<ProcessDeploymentInfo>(processes, "Name", "Version", "Description") {
			@Override
			protected Object getValue(final ProcessDeploymentInfo value, final int columnIndex) {
				switch (columnIndex) {
					case 0:
						return value.getName();
					case 1:
						return value.getVersion();
					case 2:
						return value.getDescription();
				}
				return null;
			}
		};
		processTable.setModel(processModel);

		userModel = new ListTableModel<User>(users, "Username", "First name", "Last name") {
			@Override
			protected Object getValue(final User value, final int columnIndex) {
				switch (columnIndex) {
					case 0:
						return value.getUserName();
					case 1:
						return value.getFirstName();
					case 2:
						return value.getLastName();
				}
				return null;
			}
		};
		userTable.setModel(userModel);

		create();

		setAlwaysOnTop(true);

		pack();

		setLocationRelativeTo(null);
	}

	public static void main(String[] args) {
		ConfigurationDialog dialog = new ConfigurationDialog(new ArrayList<ProcessDeploymentInfo>(), new ArrayList<User>());
		dialog.setVisible(true);
		dialog.dispose();
	}

	private void create() {
		getContentPane().setLayout(new BorderLayout(GAP, GAP));
		getContentPane().add(createContentComponent(), BorderLayout.CENTER);
		getContentPane().add(createActionComponent(), BorderLayout.PAGE_END);
	}

	protected Component createContentComponent() {
		final JPanel panel = new JPanel(new GridBagLayout());
		panel.setBorder(EMPTY_BORDER);
		final GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.BOTH;
		c.insets = new Insets(4, 4, 4, 4);

		c.gridy = 0;
		c.gridx = 0;
		panel.add(new JLabel("Token für folgenden Prozesse importieren:"), c);
		c.gridx = 1;
		panel.add(new JLabel("Nur für die gilt:"), c);

		c.weightx = 1.;
		c.weighty = 1.;
		c.gridy = 1;
		c.gridx = 0;
		panel.add(createProcessComponent(), c);
		c.gridx = 1;
		panel.add(createUserComponent(), c);
		c.weightx = 0.;
		c.weighty = 0.;

		c.gridy = 2;
		c.gridx = 0;
		panel.add(failedActivitiesCheck, c);

		return panel;
	}

	protected Component createProcessComponent() {
		processTable.setAutoCreateRowSorter(true);
		return createSelectionTable(processTable);
	}

	private void updateUserComponent() {
		Object selectedItem = userFilterComboBox.getSelectedItem();
		final boolean tableEnabled = (selectedItem != UserFilter.NONE);
		userTable.setEnabled(tableEnabled);
		userTable.getTableHeader().setEnabled(tableEnabled);
		if (!tableEnabled) {
			userTable.clearSelection();
		}
	}

	protected Component createUserComponent() {
		final JPanel panel = new JPanel(new BorderLayout(GAP, GAP));
		userTable.setAutoCreateRowSorter(true);
		final Component userSelectionComponent = createSelectionTable(userTable);
		userFilterComboBox.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				updateUserComponent();
			}
		});
		panel.add(userFilterComboBox, BorderLayout.PAGE_START);
		panel.add(userSelectionComponent, BorderLayout.CENTER);
		userFilterComboBox.setSelectedItem(UserFilter.NONE);
		updateUserComponent();
		return panel;
	}

	protected static Component createSelectionTable(final JTable table) {
		table.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		table.setCellSelectionEnabled(false);
		table.setRowSelectionAllowed(true);
		table.setColumnSelectionAllowed(false);
		table.selectAll();
		table.setFillsViewportHeight(true);
		final JPanel panel = new JPanel(new BorderLayout());
		panel.add(new JScrollPane(table), BorderLayout.CENTER);
		final JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.TRAILING, 0, 0));
		final JButton selectAllButton = new JButton("Select all");
		selectAllButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent e) {
				table.selectAll();
			}
		});
		actionPanel.add(selectAllButton);
		final JButton selectNoneButton = new JButton("Select none");
		selectNoneButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent e) {
				table.clearSelection();
			}
		});
		actionPanel.add(selectNoneButton);
		panel.add(actionPanel, BorderLayout.PAGE_END);
		return panel;
	}

	private Component createActionComponent() {
		final JPanel panel = new JPanel(new FlowLayout(FlowLayout.TRAILING));
		final JButton buttonCancel = new JButton("Cancel");
		buttonCancel.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				ok = false;
				setVisible(false);
			}
		});
		panel.add(buttonCancel);
		final JButton buttonOk = new JButton("OK");
		buttonOk.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				ok = true;
				setVisible(false);
			}
		});
		panel.add(buttonOk);
		getRootPane().setDefaultButton(buttonOk);
		return panel;
	}

	public Collection<Long> getProcessDefinitionIds() {
		final Collection<Long> ids = new ArrayList<>();
		for (final int rowIndex : processTable.getSelectedRows()) {
			ids.add(processModel.getRowData(processTable.convertRowIndexToModel(rowIndex)).getProcessId());
		}
		return ids;
	}

	public UserFilter getUserFilter() {
		return (UserFilter) userFilterComboBox.getSelectedItem();
	}

	public Collection<Long> getUserIds() {
		final Collection<Long> ids = new ArrayList<>();
		for (final int rowIndex : userTable.getSelectedRows()) {
			ids.add(userModel.getRowData(userTable.convertRowIndexToModel(rowIndex)).getId());
		}
		return ids;
	}

	public void setImportFailed(boolean importFailed) {
		this.failedActivitiesCheck.setSelected(importFailed);
	}

	public boolean getImportFailedActivities() {
		return failedActivitiesCheck.isSelected();
	}

	public boolean showDialog() {
		setVisible(true);
		return ok;
	}

}
