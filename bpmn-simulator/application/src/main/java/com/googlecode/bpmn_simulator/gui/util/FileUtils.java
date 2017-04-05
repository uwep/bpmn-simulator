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
package com.googlecode.bpmn_simulator.gui.util;

import java.awt.Component;
import java.io.File;
import java.text.MessageFormat;

import javax.swing.JOptionPane;

public final class FileUtils {

	private FileUtils() {
	}

	public static boolean isValidDirectory(final String directory) {
		if ((directory != null) && !directory.isEmpty()) {
			final File file = new File(directory);
			return file.isDirectory() && file.exists();
		}
		return false;
	}

	public static boolean canWriteFile(final Component parent, final File file) {
		return !file.exists()
				|| (JOptionPane.showConfirmDialog(parent,
						MessageFormat.format("File ''{0}'' already exists.\nDo you want to overwrite this file?", file.getName()),
						"File exists",
						JOptionPane.YES_NO_OPTION,
						JOptionPane.WARNING_MESSAGE) == JOptionPane.YES_OPTION);
	}

}
