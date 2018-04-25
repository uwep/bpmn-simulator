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


import com.googlecode.bpmn_simulator.animation.token.Instance;
import com.googlecode.bpmn_simulator.animation.token.InstanceListener;
import com.googlecode.bpmn_simulator.animation.token.Token;
import com.googlecode.bpmn_simulator.bpmn.model.core.common.FlowElement;
import com.googlecode.bpmn_simulator.bpmn.model.core.common.events.StartEvent;

public final class CallActivity
		extends AbstractAtomicActivity 
		implements InstanceListener {


	@Override
	protected void onTokenAction(Token token) {
		
		super.onTokenAction(token);
	}

	public CallActivity(final String id, final String name, final boolean isForCompensation) {
		super(id, name, isForCompensation);
	}

	private Process process = null;
	private boolean waitingCallable = false;

	public boolean isWaitingCallable() {
		return waitingCallable;
	}
	
	private Instance calledInstance = null;

	public Instance getCalledInstance() {
		return calledInstance;
	}

	public Process getProcess() {
		return process;
	}

	public void setProcess(Process process) {
		this.process = process;
	}

	@Override
	protected void onTokenComplete(final Token token) {
		if (this.waitingCallable == false)
			super.onTokenComplete(token);	
	}


	@Override
	public void tokenEnter(final Token token) {
		super.tokenEnter(token);
		if (process != null) {
			FlowElement fNode = null;
			for (final FlowElement flowElement : process.getFlowElements()) {
				if (flowElement instanceof StartEvent) {
					final StartEvent startEvent = (StartEvent) flowElement;
					if (startEvent.getEventDefinition() == null) {
						 fNode = flowElement;
					}
				}
				
			}
			if (fNode != null) {
				Token newToken = token.getInstance().createNewInstance(fNode);
				calledInstance = newToken.getInstance();
				calledInstance.addListener(this);
				this.waitingCallable = true;
			}
		}
	}

//	@Override
//	public void tokenExit(final Token token) {
//	}

	@Override
	public void instanceRemove(final Instance activityInstance) {
		this.waitingCallable = false;
	}


}
