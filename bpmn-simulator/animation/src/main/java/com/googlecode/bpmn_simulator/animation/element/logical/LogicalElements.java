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
package com.googlecode.bpmn_simulator.animation.element.logical;

import java.net.URI;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public final class LogicalElements {

	private static final Map<Class<? extends LogicalElement>, Info> infos = new HashMap<>();

	private LogicalElements() {
		super();
	}

	public static Collection<Class<? extends LogicalElement>> getAll() {
		return Collections.unmodifiableCollection(infos.keySet());
	}

	public static void register(final Class<? extends LogicalElement> element, final Info info) {
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

	public static void register(final Class<? extends LogicalElement> element, final String name) {
		register(element, new DefaultInfo(name, null, Info.DEFAULT_STEP_COUNT));
	}

	public static Info getInfo(final Class<? extends LogicalElement> element) {
		synchronized (infos) {
			return infos.get(element);
		}
	}

	public static String getName(final Class<? extends LogicalElement> element) {
		final Info info = getInfo(element);
		return (info == null) ? element.getSimpleName() : info.getName();
	}

	public static String getName(final LogicalElement element) {
		return getName(element.getClass());
	}

	public static int getDefaultStepCount(final Class<? extends LogicalElement> element) {
		final Info info = getInfo(element);
		return (info == null) ? Info.DEFAULT_STEP_COUNT : info.getDefaultStepCount();
	}

	public static void setDefaultStepCount(final Class<? extends LogicalElement> element, final int count) {
		final Info info = getInfo(element);
		if (info != null) {
			info.setDefaultStepCount(count);
		} else {
			throw new IllegalArgumentException();
		}
	}

	public interface Info {

		int DEFAULT_STEP_COUNT = 90;

		String getName();

		URI getDetails();

		int getDefaultStepCount();

		void setDefaultStepCount(int count);

	}

	private static class DefaultInfo
			implements Info {

		private final String name;
		private final URI details;

		private int defaultStepCount;

		public DefaultInfo(final String name, final URI details, final int defaultStepCount) {
			super();
			if (name == null) {
				throw new IllegalArgumentException();
			}
			this.name = name;
			this.details = details;
			this.defaultStepCount = defaultStepCount;
		}

		@Override
		public String getName() {
			return name;
		}

		@Override
		public URI getDetails() {
			return details;
		}

		@Override
		public int getDefaultStepCount() {
			return defaultStepCount;
		}

		@Override
		public void setDefaultStepCount(final int count) {
			defaultStepCount = count;
		}

		@Override
		public String toString() {
			return getName();
		}

	}

}
