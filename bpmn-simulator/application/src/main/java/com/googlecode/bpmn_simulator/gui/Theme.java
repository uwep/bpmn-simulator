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

import java.awt.Image;
import java.net.URL;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

public final class Theme {

	public static final Icon ICON_OPEN = loadIcon("open.png"); //$NON-NLS-1$
	public static final Icon ICON_IMPORT = loadIcon("import.png"); //$NON-NLS-1$

	public static final Icon ICON_START = loadIcon("start.png"); //$NON-NLS-1$;
	public static final Icon ICON_RESET = loadIcon("stop.png"); //$NON-NLS-1$;
	public static final Icon ICON_PAUSE = loadIcon("pause.png"); //$NON-NLS-1$
	public static final Icon ICON_PLAY = loadIcon("play.png"); //$NON-NLS-1$
	public static final Icon ICON_STEP = loadIcon("step.png"); //$NON-NLS-1$

	public static final Icon ICON_SPEED = loadIcon("speed.png"); //$NON-NLS-1$

	public static final Icon ICON_MESSAGES = loadIcon("messages.png"); //$NON-NLS-1$
	public static final Icon ICON_MESSAGESERROR = loadIcon("messagesError.png"); //$NON-NLS-1$

	public static final Icon ICON_ERROR
			= resizeIcon(UIManager.getIcon("OptionPane.errorIcon")); //$NON-NLS-1$

	public static final Icon ICON_WARNING
			= resizeIcon(UIManager.getIcon("OptionPane.warningIcon")); //$NON-NLS-1$

	private Theme() {
	}

	private static ImageIcon loadIcon(final String filename) {
		final URL url = Theme.class.getResource(filename);
		if (url != null) {
			return new ImageIcon(url);
		}
		return null;
	}

	private static Icon resizeIcon(final Icon icon) {
		if ((icon != null) && (icon instanceof ImageIcon)) {
			final Image image
					= ((ImageIcon) icon).getImage().getScaledInstance(16, 16, Image.SCALE_SMOOTH);
			if (image != null) {
				return new ImageIcon(image);
			}
		}
		return null;
	}

	public static void init() {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (UnsupportedLookAndFeelException e) {
			e.printStackTrace();
		}
		//JFrame.setDefaultLookAndFeelDecorated(true);
	}

}
