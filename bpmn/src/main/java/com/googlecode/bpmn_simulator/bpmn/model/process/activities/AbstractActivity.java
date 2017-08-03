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

import com.googlecode.bpmn_simulator.animation.ref.Reference;
import com.googlecode.bpmn_simulator.animation.token.Token;
import com.googlecode.bpmn_simulator.bpmn.model.core.common.AbstractFlowNode;
import com.googlecode.bpmn_simulator.bpmn.model.core.common.SequenceFlow;
import com.googlecode.bpmn_simulator.bpmn.model.core.common.events.BoundaryEvent;

public abstract class AbstractActivity
		extends AbstractFlowNode
		implements Activity {

	private boolean forCompensation;

	private Reference<SequenceFlow> defaultSequenceFlow;

	private LoopCharacteristics loopCharacteristics;

	private BoundaryEvent boundaryEventRef;
	
	public AbstractActivity(final String id, final String name, final boolean isForCompensation) {
		super(id, name);
		forCompensation = isForCompensation;
	}

	@Override
	public void setDefaultSequenceFlow(final Reference<SequenceFlow> sequenceFlow) {
		defaultSequenceFlow = sequenceFlow;
	}

	@Override
	public SequenceFlow getDefaultSequenceFlow() {
		if (defaultSequenceFlow != null) {
			return defaultSequenceFlow.getReferenced();
		}
		return null;
	}

	@Override
	public void setLoopCharacteristics(final LoopCharacteristics loopCharacteristics) {
		this.loopCharacteristics = loopCharacteristics;
	}

	@Override
	public LoopCharacteristics getLoopCharacteristics() {
		return loopCharacteristics;
	}

	@Override
	public boolean isForCompensation() {
		return forCompensation;
	}

	@Override
	protected void forwardToken(final Token token) {
		if (boundaryEventRef != null && boundaryEventRef.isCatched()) {
			token.getInstance().createNewToken(boundaryEventRef, this);
		} else
			copyTokenToOutgoing(token, token.getInstance(), false, this);
	}

	@Override
	public BoundaryEvent getBounderyEvent() {
		return this.boundaryEventRef;
	}

	@Override
	public void setBoundaryEvent(BoundaryEvent boundaryEvent) {
		this.boundaryEventRef = boundaryEvent;
	}

}
