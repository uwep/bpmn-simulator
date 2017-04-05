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

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import com.googlecode.bpmn_simulator.animation.module.Module;

public class DefinitionSource {

	private File file = null;
	private URL url = null;
	private InputStream inputStream = null;

	private Module module = null;

	private DefinitionSource(final Module module) {
		super();
		this.module = module;
	}

	public DefinitionSource(final File file, final Module module) {
		this(module);
		if (file == null) {
			throw new IllegalArgumentException();
		}
		this.file = file;
	}

	public DefinitionSource(final URL url, final Module module) {
		this(module);
		if (url == null) {
			throw new IllegalArgumentException();
		}
		this.url = url;
	}

	public DefinitionSource(final InputStream inputStream, final Module module) {
		this(module);
		if (inputStream == null) {
			throw new IllegalArgumentException();
		}
		this.inputStream = inputStream;
	}

	public Module getModule() {
		return module;
	}

	public File getFile() {
		return file;
	}

	public static File getFile(final DefinitionSource source) {
		return ((source != null) && source.isFile()) ? source.getFile() : null;
	}

	public URL getURL() {
		return url;
	}

	public boolean isFile() {
		return getFile() != null;
	}

	public boolean isURL() {
		return !isFile() && (getURL() != null);
	}

	public boolean isClipboard() {
		return !isFile() && !isURL() && (inputStream != null);
	}

	public boolean canReopen() {
		return isFile() || isURL();
	}

	public String getName() {
		if (isFile()) {
			return getFile().getAbsolutePath();
		} else if (isURL()) {
			return getURL().toString();
		} else if (isClipboard()) {
			return "Clipboard";
		}
		return null;
	}

	public InputStream getStream()
			throws IOException {
		if (isFile()) {
			return new FileInputStream(file);
		} else if (isURL()) {
			return url.openStream();
		} else if (isClipboard()) {
			return inputStream;
		}
		return null;
	}

}
