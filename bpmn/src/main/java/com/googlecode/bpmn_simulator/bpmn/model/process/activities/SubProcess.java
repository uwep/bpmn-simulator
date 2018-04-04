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

import com.googlecode.bpmn_simulator.animation.token.Instance;
import com.googlecode.bpmn_simulator.animation.token.InstanceListener;
import com.googlecode.bpmn_simulator.animation.token.Token;
import com.googlecode.bpmn_simulator.bpmn.model.core.common.FlowElement;
import com.googlecode.bpmn_simulator.bpmn.model.core.common.FlowNode;
import com.googlecode.bpmn_simulator.bpmn.model.core.common.events.StartEvent;
import com.googlecode.bpmn_simulator.bpmn.model.core.common.gateways.Gateway;

public class SubProcess
		extends AbstractDecomposableActivity
		implements InstanceListener {

	private final boolean triggeredByEvent;

	public SubProcess(final String id, final String name,
			final boolean isForCompensation, final boolean triggeredByEvent) {
		super(id, name, isForCompensation);
		this.triggeredByEvent = triggeredByEvent;
	}

	public boolean isTriggeredByEvent() {
		return triggeredByEvent;
	}

	private Collection<StartEvent> getEmptyStartEvents() {
		final Collection<StartEvent> startEvents = new ArrayList<>();
		for (final FlowElement flowElement : getFlowElements()) {
			if (flowElement instanceof StartEvent) {
				final StartEvent startEvent = (StartEvent) flowElement;
				if (startEvent.getEventDefinition() == null) {
					startEvents.add(startEvent);
				}
			}
		}
		return startEvents;
	}

	private Collection<FlowElement> getActivitiesAndGatewaysWithoutIncoming() {
		final Collection<FlowElement> elements = new ArrayList<>();
		for (final FlowElement flowElement : getFlowElements()) {
			if ((flowElement instanceof Activity) || (flowElement instanceof Gateway)) {
				final FlowNode flowNode = (FlowNode) flowElement;
				if (flowNode.getIncoming().isEmpty()) {
					elements.add(flowNode);
				}
			}
		}
		return elements;
	}

	private Collection<FlowElement> getInstantiationElements() {
		final Collection<FlowElement> elements = new ArrayList<>();
		final Collection<StartEvent> startEvents = getEmptyStartEvents();
		if (startEvents.size() > 0) {
			elements.addAll(startEvents);
		} else {
			elements.addAll(getActivitiesAndGatewaysWithoutIncoming());
		}
		return elements;
	}

	@Override
	public void tokenEnter(final Token token) {
		final Instance instance = token.getInstance().addNewChildInstance(this);
		instance.addListener(this);
		for (final FlowElement element : getInstantiationElements()) {
			token.copyTo(element, instance);
		}
		token.remove();
	}

	@Override
	public void tokenExit(final Token token) {
	}

	@Override
	public void instanceRemove(final Instance activityInstance) {
		createTokenAtOutgoing(activityInstance.getParentInstance(), this, false, this);
	}

}
