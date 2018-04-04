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
package com.googlecode.bpmn_simulator.bpmn.model.core.common.gateways;

import com.googlecode.bpmn_simulator.animation.ref.Reference;
import com.googlecode.bpmn_simulator.animation.ref.ReferenceSet;
import com.googlecode.bpmn_simulator.animation.ref.References;
import com.googlecode.bpmn_simulator.animation.token.Token;
import com.googlecode.bpmn_simulator.bpmn.model.InstantiateElement;
import com.googlecode.bpmn_simulator.bpmn.model.core.common.FlowNode;
import com.googlecode.bpmn_simulator.bpmn.model.core.common.SequenceFlow;
import com.googlecode.bpmn_simulator.bpmn.model.core.common.events.IntermediateCatchEvent;

public final class EventBasedGateway
		extends AbstractGateway
		implements InstantiateElement {

	private boolean instantiate;

	public EventBasedGateway(final String id, final String name, final boolean instantiate) {
		super(id, name);
		this.instantiate = instantiate;
	}

	@Override
	public boolean isInstantiate() {
		return instantiate;
	}

	private ReferenceSet<IntermediateCatchEvent> eventBases = new ReferenceSet<IntermediateCatchEvent>();
	
	public void addEventBase(Reference<IntermediateCatchEvent> ice) {
		eventBases.add(ice);
	}
	
	@Override
	protected void forwardToken(final Token token) {
		for (final IntermediateCatchEvent ice : eventBases) {
			if (ice.isCatched()) {
				final SequenceFlow outgoing = ice.getIncoming().iterator().next();
				token.getInstance().createNewToken(outgoing, this);
				break;
			}
		}
	}
	
	@Override
	protected void onTokenComplete(final Token token) {
		for (final IntermediateCatchEvent ice : eventBases) {
			if (ice.isCatched()) {
				super.onTokenComplete(token);
				break;
			}
		}
	}
}
