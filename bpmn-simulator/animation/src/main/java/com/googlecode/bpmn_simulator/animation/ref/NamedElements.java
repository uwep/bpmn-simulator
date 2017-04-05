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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class NamedElements<E> {

	private final Map<String, E> elements = new HashMap<>();

	public Collection<E> getElements() {
		return elements.values();
	}

	public void setElement(final String name, final E element) {
		assert !elements.containsKey(name) : "Element with name " + name + " already exist";
		elements.put(name, element);
	}

	public E getElement(final String name) {
		final E element = elements.get(name);
		assert element != null;
		return element;
	}

	private static <E> boolean is(final E element, final Class<? extends E> clazz) {
		return (element != null)
				&& (clazz != null)
				&& clazz.isAssignableFrom(element.getClass());
	}

	public <T extends E> T getElement(final String name, final Class<T> clazz) {
		final E element = getElement(name);
		if (is(element, clazz)) {
			return clazz.cast(element);
		}
		return null;
	}

	public <T extends E> Map<String, T> getElementsByClass(final Class<T> clazz) {
		final Map<String, T> elements = new HashMap<>();
		for (final String name : this.elements.keySet()) {
			final E element = getElement(name);
			if (is(element, clazz)) {
				elements.put(name, clazz.cast(element));
			}
		}
		return elements;
	}

	public boolean hasElement(final String name) {
		return elements.containsKey(name);
	}

	@Override
	public String toString() {
		final List<String> names = new ArrayList<>(elements.keySet());
		Collections.sort(names);
		final StringBuilder builder = new StringBuilder();
		final Iterator<String> i = names.iterator();
		while (i.hasNext()) {
			final String name = i.next();
			builder.append('\'');
			builder.append(name);
			builder.append('\'');
			builder.append('=');
			builder.append(elements.get(name));
			if (i.hasNext()) {
				builder.append(',');
			}
		}
		return builder.toString();
	}

}
