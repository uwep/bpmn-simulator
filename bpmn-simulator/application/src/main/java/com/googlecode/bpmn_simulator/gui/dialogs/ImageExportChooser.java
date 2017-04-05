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
package com.googlecode.bpmn_simulator.gui.dialogs;

import java.awt.Component;
import java.io.File;

import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.apache.commons.io.FilenameUtils;

@SuppressWarnings("serial")
public class ImageExportChooser
		extends JFileChooser {

	public ImageExportChooser() {
		super();
		setFileSelectionMode(JFileChooser.FILES_ONLY);
		setMultiSelectionEnabled(false);
		setAcceptAllFileFilterUsed(false);
		for (final String suffix : ImageIO.getWriterFileSuffixes()) {
			addChoosableFileFilter(new FileNameExtensionFilter(suffix, suffix));
		}
	}

	public boolean showExportDialog(final Component parent) {
		return showSaveDialog(parent) == JFileChooser.APPROVE_OPTION;
	}

	@Override
	public File getSelectedFile() {
		final File file = super.getSelectedFile();
		if (file != null) {
			final String extension = getSelectedImageFormat();
			if (!FilenameUtils.isExtension(file.getName(), extension)) {
				return new File(file.getPath() + FilenameUtils.EXTENSION_SEPARATOR + extension);
			}
		}
		return file;
	}

	public String getSelectedImageFormat() {
		final FileFilter fileFilter = getFileFilter();
		if (fileFilter != null) {
			return fileFilter.getDescription();
		}
		return null;
	}

}