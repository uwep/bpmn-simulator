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

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.List;

import javax.swing.TransferHandler;

@SuppressWarnings("serial")
public abstract class DefinitionSourceTransferHandler
		extends TransferHandler {

	private static final DataFlavor DATAFLAVOR_INPUTSTREAM_UTF8 = createDataFlavor("text/plain", InputStream.class, Charset.forName("UTF-8"));
	private static final DataFlavor DATAFLAVOR_URL = createDataFlavor("application/x-java-url", URL.class, null);
	private static final DataFlavor DATAFLAVOR_LIST_FILE = DataFlavor.javaFileListFlavor;

	protected static DataFlavor createDataFlavor(final String mimeType, final Class<?> representationClass, final Charset charset) {
		final StringBuilder builder = new StringBuilder(mimeType);
		if (representationClass != null) {
			builder.append(";class=" + representationClass.getCanonicalName());
		}
		if (charset != null) {
			builder.append(";charset=" + charset.name());
		}
		try {
			return new DataFlavor(builder.toString());
		} catch (ClassNotFoundException e) {
		}
		return null;
	}

	@Override
	public boolean canImport(final TransferSupport support) {
		final Transferable transferable = support.getTransferable();
		if (transferable.isDataFlavorSupported(DATAFLAVOR_LIST_FILE)
				|| transferable.isDataFlavorSupported(DATAFLAVOR_URL)
				|| transferable.isDataFlavorSupported(DATAFLAVOR_INPUTSTREAM_UTF8)) {
			support.setDropAction(COPY);
			return true;
		}
		return false;
	}

	private static File transferableToFile(final Transferable transferable) {
		if (transferable.isDataFlavorSupported(DATAFLAVOR_LIST_FILE)) {
			try {
				final List<File> files = (List<File>) transferable.getTransferData(DATAFLAVOR_LIST_FILE);
				if ((files != null) && (files.size() == 1)) {
					return files.get(0);
				}
			} catch (UnsupportedFlavorException e) {
			} catch (IOException e) {
			}
		}
		return null;
	}

	private boolean importFile(final Transferable transferable) {
		final File file = transferableToFile(transferable);
		if (file != null) {
			onImportDefinition(new DefinitionSource(file, null));
			return true;
		}
		return false;
	}

	private boolean importUrl(final Transferable transferable) {
		if (transferable.isDataFlavorSupported(DATAFLAVOR_URL)) {
			try {
				final URL url = (URL) transferable.getTransferData(DATAFLAVOR_URL);
				if (url != null) {
					onImportDefinition(new DefinitionSource(url, null));
					return true;
				}
			} catch (UnsupportedFlavorException e) {
			} catch (IOException e) {
			}
		}
		return false;
	}

	private boolean importStream(final Transferable transferable) {
		if (transferable.isDataFlavorSupported(DATAFLAVOR_INPUTSTREAM_UTF8)) {
			try {
				final InputStream inputStream = (InputStream) transferable.getTransferData(DATAFLAVOR_INPUTSTREAM_UTF8);
				if (inputStream != null) {
					onImportDefinition(new DefinitionSource(inputStream, null));
					return true;
				}
			} catch (UnsupportedFlavorException e) {
			} catch (IOException e) {
			}
		}
		return false;
	}

	@Override
	public boolean importData(final TransferSupport support) {
		final Transferable transferable = support.getTransferable();
		return importUrl(transferable)
				||importFile(transferable)
				|| importStream(transferable);
	}

	protected abstract void onImportDefinition(DefinitionSource source);

}
