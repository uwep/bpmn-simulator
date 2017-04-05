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
package com.googlecode.bpmn_simulator.animation.ref;

public class NamedReference<E>
		implements Reference<E> {

	private final NamedElements<E> elements;

	private final String name;

	public NamedReference(final NamedElements<E> elements, final String name) {
		super();
		this.elements = elements;
		this.name = name;
	}

	private boolean hasName() {
		return (name != null) && !name.isEmpty();
	}

	@Override
	public boolean hasReference() {
		return hasName()
				&& elements.hasElement(name);
	}

	@Override
	public E getReferenced() {
		if (hasName()) {
			return elements.getElement(name);
		}
		return null;
	}

}
