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
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

abstract class AbstractReferences<E>
		implements References<E> {

	private final Set<Reference<E>> references = new HashSet<>();

	public void add(final Reference<E> reference) {
		references.add(reference);
	}

	@Override
	public Iterator<E> iterator() {
		final Collection<E> elements = new ArrayList<>();
		for (final Reference<E> reference : references) {
			final E element = reference.getReferenced();
			if (element != null) {
				elements.add(element);
			}
		}
		return elements.iterator();
	}

	@Override
	public boolean isEmpty() {
		return references.isEmpty();
	}

	public boolean hasInvalidReferences() {
		for (final Reference<E> reference : references) {
			if ((reference == null) || !reference.hasReference()) {
				return true;
			}
		}
		return false;
	}

	public int getReferencedCount() {
		int count = 0;
		for (final Reference<E> reference : references) {
			if (reference.hasReference()) {
				++count;
			}
		}
		return count;
	}

}
