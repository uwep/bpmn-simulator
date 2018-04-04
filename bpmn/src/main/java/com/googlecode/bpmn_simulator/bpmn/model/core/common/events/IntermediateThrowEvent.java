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
package com.googlecode.bpmn_simulator.bpmn.model.core.common.events;

import com.googlecode.bpmn_simulator.animation.ref.Reference;
import com.googlecode.bpmn_simulator.animation.token.Token;
import com.googlecode.bpmn_simulator.bpmn.model.collaboration.MessageFlow;
import com.googlecode.bpmn_simulator.bpmn.model.core.common.Message;

public final class IntermediateThrowEvent
		extends AbstractThrowEvent {

	public IntermediateThrowEvent(final String id, final String name) {
		super(id, name);
	}

	public Reference<IntermediateCatchEvent> getCatchEvent() {
		return catchEvent;
	}

	public void setCatchEvent(Reference<IntermediateCatchEvent> catchEvent) {
		this.catchEvent = catchEvent;
	}

	private Reference<IntermediateCatchEvent> catchEvent;
	
	@Override
	protected void forwardToken(Token token) {
		if (this.getEventDefinition() != null && this.getEventDefinition() instanceof LinkEventDefinition) {
			if (catchEvent != null && catchEvent.hasReference()) {
				token.getInstance().createNewToken(catchEvent.getReferenced(), this);
//				token.remove();
			} else			
				super.forwardToken(token);
		} else			
			super.forwardToken(token);
		
	}

}
