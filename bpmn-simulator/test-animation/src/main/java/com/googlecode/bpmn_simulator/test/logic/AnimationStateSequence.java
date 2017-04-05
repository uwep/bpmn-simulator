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

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import com.googlecode.bpmn_simulator.animation.element.logical.LogicalFlowElement;
import com.googlecode.bpmn_simulator.animation.input.Definition;
import com.googlecode.bpmn_simulator.animation.token.RootInstances;
import com.googlecode.bpmn_simulator.animation.token.TokenFlow;

public class AnimationStateSequence
		extends AbstractStateSequence<AttributedElementsState<String, Integer>> {

	private final Definition<?> definition;

	private final TokenFlow startElement;

	private final Map<String, LogicalFlowElement> elements = new HashMap<>();

	public AnimationStateSequence(final Definition<?> definition, final TokenFlow startElement) {
		super();
		this.definition = definition;
		this.startElement = startElement;
		readElements();
	}

	protected String getElementName(final LogicalFlowElement flowElement) {
		return flowElement.toString();
	}

	@Override
	public Iterator<AttributedElementsState<String, Integer>> iterator() {
		return new AnimationIterator();
	}

	private void readElements() {
		for (final LogicalFlowElement flowElement : definition.getFlowElements()) {
			final String name = getElementName(flowElement);
			if (name != null) {
				elements.put(name, flowElement);
			}
		}
	}

	public Set<Integer> getElementAttributes(final String id) {
		final Set<Integer> attributes = new HashSet<>();
		final LogicalFlowElement flowElement = elements.get(id);
		if (flowElement != null) {
			attributes.add(Integer.valueOf(flowElement.getTokens().size()));
		}
		return attributes;
	}

	private class AnimationIterator
			implements Iterator<AttributedElementsState<String, Integer>> {

		private final RootInstances instances = new RootInstances();

		public AnimationIterator() {
			super();
			instances.addNewChildInstance(null).createNewToken(startElement, null);
		}

		@Override
		public boolean hasNext() {
			return instances.hasChildInstances();
		}

		private AttributedElementsState<String, Integer>  copyState() {
			final Set<String> elements = AnimationStateSequence.this.elements.keySet();
			final StaticAttributedElementsState<String, Integer> state = new StaticAttributedElementsState<>(elements);
			for (final String element : elements) {
				final LogicalFlowElement flowElement = AnimationStateSequence.this.elements.get(element);
				final int tokenCount = flowElement.getTokens().size();
				if (tokenCount > 0) {
					state.addAttribute(element, Integer.valueOf(tokenCount));
				}
			}
			return state;
		}

		@Override
		public AttributedElementsState<String, Integer> next() {
			instances.step(1);
			return copyState();
		}

		@Override
		public void remove() {
			throw new UnsupportedOperationException();
		}

	}

}
