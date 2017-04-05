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
package com.googlecode.bpmn_simulator.bpmn.model.process.activities;

import java.util.ArrayList;
import java.util.Collection;

import com.googlecode.bpmn_simulator.bpmn.model.core.common.FlowElement;
import com.googlecode.bpmn_simulator.bpmn.model.core.common.FlowElementsContainer;

abstract class AbstractDecomposableActivity
		extends AbstractActivity
		implements FlowElementsContainer {

	private final Collection<FlowElement> flowElements = new ArrayList<>();

	public AbstractDecomposableActivity(final String id, String name,
			final boolean isForCompensation) {
		super(id, name, isForCompensation);
	}

	@Override
	public void addFlowElement(final FlowElement flowElement) {
		flowElements.add(flowElement);
	}

	@Override
	public Collection<FlowElement> getFlowElements() {
		return flowElements;
	}

}
