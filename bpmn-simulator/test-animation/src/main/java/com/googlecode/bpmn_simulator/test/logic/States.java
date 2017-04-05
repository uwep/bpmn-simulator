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
package com.googlecode.bpmn_simulator.test.logic;

import java.io.PrintStream;
import java.util.Iterator;
import java.util.TreeSet;

public final class States {

	private States() {
	}

	public static <ELEMENT, ATTRIBUTE> String toString(final AttributedElementsState<ELEMENT, ATTRIBUTE> state) {
		final StringBuilder builder = new StringBuilder();
		builder.append('{');
		final Iterator<ELEMENT> elementIterator = new TreeSet<>(state.getElements()).iterator();
		while (elementIterator.hasNext()) {
			final ELEMENT element = elementIterator.next();
			builder.append(element);
			final Iterator<ATTRIBUTE> attributeIterator = new TreeSet<>(state.getElementAttributes(element)).iterator();
			if (attributeIterator.hasNext()) {
				builder.append(":<");
				while (attributeIterator.hasNext()) {
					final ATTRIBUTE attribute = attributeIterator.next();
					builder.append(attribute);
					if (attributeIterator.hasNext()) {
						builder.append(',');
					}
				}
				builder.append('>');
			}
			if (elementIterator.hasNext()) {
				builder.append(", ");
			}
		}
		builder.append('}');
		return builder.toString();
	}

	public static <STATE> void print(final StateSequence<STATE> stateSequence,
			final boolean combine, final PrintStream out) {
		final Iterator<STATE> iter = stateSequence.iterator();
		STATE currentState = null;
		STATE previousState = null;
		while (iter.hasNext()) {
			previousState = currentState;
			currentState = iter.next();
			if (!combine || ((currentState != null) && !currentState.equals(previousState))) {
				out.println(currentState);
			}
		}
	}

}
