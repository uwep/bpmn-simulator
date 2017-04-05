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
package com.googlecode.bpmn_simulator.animation.module;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.googlecode.bpmn_simulator.animation.element.logical.LogicalElement;
import com.googlecode.bpmn_simulator.animation.element.visual.VisualElement;

public abstract class AbstractModule
		implements Module {

	private final Map<Class<? extends LogicalElement>, Set<Class<? extends VisualElement>>> elements = new HashMap<>();

	protected AbstractModule() {
		super();
	}

	protected void addElement(final Class<? extends LogicalElement> logical, Class<? extends VisualElement> visual) {
		final Set<Class<? extends VisualElement>> visuals = new HashSet<>();
		Collections.addAll(visuals, visual);
		elements.put(logical, visuals);
	}

	@Override
	public Map<Class<? extends LogicalElement>, Set<Class<? extends VisualElement>>> getElements() {
		return Collections.unmodifiableMap(elements);
	}

	@Override
	public String toString() {
		return getDescription();
	}

}
