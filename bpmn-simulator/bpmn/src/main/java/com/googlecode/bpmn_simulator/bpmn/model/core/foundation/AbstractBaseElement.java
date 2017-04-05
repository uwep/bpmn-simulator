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

import java.util.ArrayList;
import java.util.Collection;

import com.googlecode.bpmn_simulator.animation.element.logical.LogicalElement;

public abstract class AbstractBaseElement
		implements BaseElement, LogicalElement {

	private final String id;

	private Collection<Documentation> documentations = new ArrayList<>();

	public AbstractBaseElement(final String id) {
		super();
		this.id = id;
	}

	@Override
	public String getId() {
		return id;
	}

	@Override
	public void addDocumentation(final Documentation documentation) {
		documentations.add(documentation);
	}

	@Override
	public Collection<Documentation> getDocumentation() {
		return documentations;
	}

	protected static String firstNotEmpty(final String... s) {
		for (int i = 0; i < s.length; ++i) {
			if ((s[i] != null) && !s[i].isEmpty()) {
				return s[i];
			}
		}
		return null;
	}

	@Override
	public String toString() {
		return getId();
	}

}
