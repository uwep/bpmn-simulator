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

import java.io.File;

import javax.swing.filechooser.FileFilter;

import org.apache.commons.io.FilenameUtils;

import com.googlecode.bpmn_simulator.animation.module.Module;

class ModuleFileFilter extends FileFilter {

	private final Module module;

	public ModuleFileFilter(final Module module) {
		super();
		this.module = module;
	}

	public Module getModule() {
		return module;
	}

	@Override
	public boolean accept(final File file) {
		if (file != null) {
			if (file.isDirectory()) {
				return true;
			}
			return FilenameUtils.isExtension(file.getName(), module.getFileExtensions());
		}
		return false;
	}

	@Override
	public String getDescription() {
		return module.getFileDescription();
	}
	
}