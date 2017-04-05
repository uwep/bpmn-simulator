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

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;

@SuppressWarnings("serial")
public class RecentMenu
		extends JMenu {

	private static final int MAX_ENTRIES = 10;

	private final List<File> files = new RecentList<>(MAX_ENTRIES);

	private final Preferences preferences;

	private final JMenuItem menuItemClear;

	public RecentMenu(final Preferences preferences, final String text, final String clearText) {
		super(text);
		this.preferences = preferences;
		menuItemClear = new JMenuItem(clearText);
		menuItemClear.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent event) {
				clearFiles();
			}
		});
		addMenuListener(new MenuListener() {

			@Override
			public void menuSelected(final MenuEvent e) {
				recreateMenu();
			}

			@Override
			public void menuDeselected(final MenuEvent e) {
			}

			@Override
			public void menuCanceled(final MenuEvent e) {
			}

		});
		loadFiles();
	}

	private void updateMenuItem() {
		setEnabled(!files.isEmpty());
	}

	private void recreateMenu() {
		removeAll();
		for (final File file : files) {
			final MenuDataItem<File> menuItem = new MenuDataItem<>(file.getName(), file);
			menuItem.setToolTipText(file.getAbsolutePath());
			menuItem.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(final ActionEvent e) {
					onFile(menuItem.getData());
				}
			});
			add(menuItem);
		}
		if (!files.isEmpty()) {
			addSeparator();
			add(menuItemClear);
		}
	}

	protected void onFile(final File file) {
	}

	private void loadFiles() {
		files.clear();
		try {
			preferences.sync();
		} catch (BackingStoreException e) {
		}
		for (int i = MAX_ENTRIES - 1; i >= 0 ; --i) {
			final String value = preferences.get(String.valueOf(i), null);
			if (value != null) {
				final File file = new File(value);
				if (file.isFile() && file.exists() && file.canRead()) {
					files.add(file);
				}
			}
		}
		recreateMenu();
		updateMenuItem();
	}

	private void saveFiles() {
		try {
			preferences.clear();
		} catch (BackingStoreException e) {
		}
		for (int i = files.size() - 1; i >= 0 ; --i) {
			preferences.put(String.valueOf(i), files.get(i).toString());
		}
		try {
			preferences.flush();
		} catch (BackingStoreException e) {
		}
	}

	public void clearFiles() {
		files.clear();
		saveFiles();
		recreateMenu();
		updateMenuItem();
	}

	public void addFile(final File file) {
		files.add(file);
		saveFiles();
		recreateMenu();
		updateMenuItem();
	}

	private class MenuDataItem<E> extends JMenuItem {

		private E data;

		public MenuDataItem(final String text, final E data) {
			super(text);
			setData(data);
		}

		public E getData() {
			return data;
		}

		public void setData(final E data) {
			if (data == null) {
				throw new IllegalArgumentException();
			}
			this.data = data;
		}

	}

	private static class RecentList<E> extends ArrayList<E> {

		private final int maxSize;

		public RecentList(final int maxSize) {
			super(maxSize);
			this.maxSize = maxSize;
		}

		private void shrinkTo(final int size) {
			while (size() > size) {
				remove(size() - 1);
			}
		}

		@Override
		public boolean add(final E e) {
			if (e == null) {
				throw new IllegalArgumentException();
			}
			final int index = indexOf(e);
			if (index == 0) {
				return false;
			} else if (index > 0) {
				remove(e);
			}
			add(0, e);
			shrinkTo(maxSize);
			return true;
		}

	}

}
