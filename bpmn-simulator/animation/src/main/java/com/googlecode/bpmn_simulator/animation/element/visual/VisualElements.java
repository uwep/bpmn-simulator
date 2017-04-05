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
package com.googlecode.bpmn_simulator.animation.element.visual;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public final class VisualElements {

	private static final Map<Class<? extends VisualElement>, Info> infos = new HashMap<>();

	private VisualElements() {
		super();
	}

	public static Collection<Class<? extends VisualElement>> getAll() {
		return Collections.unmodifiableCollection(infos.keySet());
	}

	public static void register(final Class<? extends VisualElement> element, final Info info) {
		synchronized (infos) {
			if (infos.containsKey(element)) {
				throw new IllegalArgumentException("element " + element.getName() + " already exists");
			}
			if (info == null) {
				throw new NullPointerException();
			}
			infos.put(element, info);
		}
	}

	public static void register(final Class<? extends VisualElement> element, final int defaultBackgroundColor) {
		register(element, defaultBackgroundColor, Info.DEFAULT_FOREGROUND_COLOR);
	}

	public static void register(final Class<? extends VisualElement> element, final int defaultBackgroundColor, final int defaultForegroundColor) {
		register(element, new DefaultInfo(defaultBackgroundColor, defaultForegroundColor));
	}

	public static Info getInfo(final Class<? extends VisualElement> element) {
		synchronized (infos) {
			return infos.get(element);
		}
	}

	public static int getDefaultBackgroundColor(final Class<? extends VisualElement> element) {
		final Info info = getInfo(element);
		if (info != null) {
			return info.getDefaultBackgroundColor();
		}
		return Info.DEFAULT_BACKGROUND_COLOR;
	}

	public static int getDefaultForegroundColor(final Class<? extends VisualElement> element) {
		final Info info = getInfo(element);
		if (info != null) {
			return info.getDefaultForegroundColor();
		}
		return Info.DEFAULT_FOREGROUND_COLOR;
	}

	public static void setDefaultBackgroundColor(final Class<? extends VisualElement> element, final int color) {
		final Info info = getInfo(element);
		if (info != null) {
			info.setDefaultBackgroundColor(color);
		} else {
			register(element, color);
		}
	}

	public static void setDefaultForegroundColor(final Class<? extends VisualElement> element, final int color) {
		final Info info = getInfo(element);
		if (info != null) {
			info.setDefaultForegroundColor(color);
		} else {
			register(element, Info.DEFAULT_BACKGROUND_COLOR, color);
		}
	}

	public interface Info {

		int BLACK = 0x000000;
		int WHITE = 0xffffff;
		int GRAY = 0xEEEEEE;

		int YELLOW = 0xFFFFB5;
		int ORANGE = 0xFFD062;
		int RED = 0xFFA4A4;
		int GREEN = 0xA4F0B7;
		int BLUE = 0xDBF0F7;

		int DEFAULT_FOREGROUND_COLOR = BLACK;
		int DEFAULT_BACKGROUND_COLOR = WHITE;

		void setDefaultForegroundColor(int color);

		int getDefaultForegroundColor();

		void setDefaultBackgroundColor(int color);

		int getDefaultBackgroundColor();

	}

	private static class DefaultInfo
			implements Info {

		private int defaultForegroundColor;
		private int defaultBackgroundColor;

		public DefaultInfo(final int defaultBackgroundColor, final int defaultForegroundColor) {
			super();
			this.defaultForegroundColor = defaultForegroundColor;
			this.defaultBackgroundColor = defaultBackgroundColor;
		}

		@Override
		public void setDefaultForegroundColor(final int color) {
			defaultForegroundColor = color;
		}

		@Override
		public int getDefaultForegroundColor() {
			return defaultForegroundColor;
		}

		@Override
		public void setDefaultBackgroundColor(final int color) {
			defaultBackgroundColor = color;
		}

		@Override
		public int getDefaultBackgroundColor() {
			return defaultBackgroundColor;
		}

	}

}
