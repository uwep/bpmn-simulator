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
package com.googlecode.bpmn_simulator.bpmn.model.process.activities.tasks;

import com.googlecode.bpmn_simulator.animation.ref.Reference;
import com.googlecode.bpmn_simulator.animation.token.Token;
import com.googlecode.bpmn_simulator.bpmn.model.InstantiateElement;
import com.googlecode.bpmn_simulator.bpmn.model.collaboration.MessageFlow;

public final class ReceiveTask
		extends Task
		implements InstantiateElement {

	private boolean instantiate;
	

	public ReceiveTask(final String id, final String name,
			final boolean isForCompensation, final boolean instantiate) {
		super(id, name, isForCompensation);
		this.instantiate = instantiate;
	}

	@Override
	public boolean isInstantiate() {
		return instantiate;
	}

	@Override
	protected void onTokenComplete(Token token) {
		if (getBoundaryEventRef() != null && getBoundaryEventRef().isCatched()) {
			if (!getBoundaryEventRef().isCancelActivity())
				token.getInstance().createNewToken(getBoundaryEventRef(), this);
			super.onTokenComplete(token);
		} else {
			Reference<MessageFlow> msgFlowRef = this.getInMessageFlow();
			if (msgFlowRef != null && msgFlowRef.getReferenced() != null && msgFlowRef.getReferenced().containsMessage()) {
				if (this.messageReceiveDelay > 0)
					messageReceiveDelay--;
				else {
					msgFlowRef.getReferenced().cleanMessage();
					super.onTokenComplete(token);
					messageReceiveDelay = 50;
				}
			}
		}
	}

}
