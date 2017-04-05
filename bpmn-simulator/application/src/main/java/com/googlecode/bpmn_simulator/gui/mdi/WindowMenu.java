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
package com.googlecode.bpmn_simulator.gui.mdi;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyVetoException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.swing.JDesktopPane;
import javax.swing.JInternalFrame;
import javax.swing.JMenu;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;

@SuppressWarnings("serial")
public class WindowMenu
		extends JMenu
		implements MenuListener {

	private JDesktopPane desktop;

	public WindowMenu() {
		addMenuListener(this);
	}

	public void setDesktopPane(final JDesktopPane desktop) {
		this.desktop = desktop;
	}

	protected JDesktopPane getDesktopPane() {
		return desktop;
	}

	@Override
	public void menuCanceled(final MenuEvent event) {
	}

	@Override
	public void menuDeselected(final MenuEvent event) {
		removeAll();
	}

	@Override
	public void menuSelected(final MenuEvent event) {
		createWindowMenuItems();
	}

	protected List<JInternalFrame> getFrames() {
		final List<JInternalFrame> frames =
				new ArrayList<>(Arrays.asList(getDesktopPane().getAllFrames()));
		Collections.sort(frames, new Comparator<JInternalFrame>() {
			@Override
			public int compare(final JInternalFrame frame1, final JInternalFrame frame2) {
				return frame1.getTitle().compareTo(frame2.getTitle());
			}
		});
		return frames;
	}

	protected void createWindowMenuItems() {
		final JDesktopPane desktopPane = getDesktopPane();
		if (desktopPane != null) {
			for (JInternalFrame frame : getFrames()) {
				add(new WindowMenuItem(frame));
			}
		}
	}

	private static class WindowMenuItem
			extends JRadioButtonMenuItem
			implements ActionListener {

		private final JInternalFrame frame;

		public WindowMenuItem(final JInternalFrame frame) {
			super(frame.getTitle(), frame.isSelected());
			setIcon(frame.getFrameIcon());
			this.frame = frame;
			addActionListener(this);
		}

		@Override
		public void actionPerformed(final ActionEvent event) {
			frame.moveToFront();
			try {
				frame.setSelected(true);
			} catch (PropertyVetoException e) {
				e.printStackTrace();
			}
		}

	}

}
