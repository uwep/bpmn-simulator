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
package com.googlecode.bpmn_simulator.bpmn.model.core.foundation;

import javax.activation.MimeTypeParseException;
import javax.activation.MimeType;

import com.googlecode.bpmn_simulator.bpmn.model.TextElement;

public final class Documentation
		extends AbstractBaseElement
		implements TextElement {

	private static final MimeType TEXT_PLAIN = newMimeType("text", "plain"); //$NON-NLS-1$ //$NON-NLS-2$
	private static final MimeType TEXT_HTML = newMimeType("text", "html"); //$NON-NLS-1$ //$NON-NLS-2$

	private static final MimeType DEFAULT_TEXTFORMAT = TEXT_PLAIN;

	private String text;

	private MimeType textFormat;

	public Documentation(final String id, final String text) {
		this(id, text, null);
	}

	public Documentation(final String id, final String text, final MimeType textFormat) {
		super(id);
		setText(text);
		setTextFormat(textFormat);
	}

	protected static final MimeType newMimeType(final String primary,
			final String sub) {
		try {
			return new MimeType(primary, sub);
		} catch (MimeTypeParseException e) {
			return null;
		}
	}

	private static String transformLineEnding(final String text) {
		return text.replaceAll("\r?\n", System.getProperty("line.separator")); //$NON-NLS-1$ //$NON-NLS-2$
	}

	public final void setText(final String text) {
		assert (text != null) && !text.isEmpty();
		this.text = transformLineEnding(text);
	}

	@Override
	public final String getText() {
		return text;
	}

	public final void setTextFormat(final MimeType textFormat) {
		assert (textFormat == null)
				|| textFormat.match(TEXT_PLAIN)
				|| textFormat.match(TEXT_HTML);
		this.textFormat = textFormat;
	}

	@Override
	public MimeType getTextFormat() {
		return (textFormat == null) ? DEFAULT_TEXTFORMAT : textFormat;
	}

	private static String escapeHTML(final String text) {
		return text
				.replaceAll("&", "&amp;") //$NON-NLS-1$ //$NON-NLS-2$
				.replaceAll("<", "&lt;") //$NON-NLS-1$ //$NON-NLS-2$
				.replaceAll(">", "&gt;") //$NON-NLS-1$ //$NON-NLS-2$
				.replaceAll(System.getProperty("line.separator"), "<br/>"); //$NON-NLS-1$ //$NON-NLS-2$
	}

	public String toHtml() {
		final MimeType textFormat = getTextFormat();
		if (TEXT_PLAIN.match(textFormat)) {
			return escapeHTML(getText());
		} else if (TEXT_HTML.match(textFormat)) {
			return getText();
		} else {
			assert false;
			return getText();
		}
	}

}
