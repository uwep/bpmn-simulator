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
package com.googlecode.bpmn_simulator.bpmn.model.collaboration;

import com.googlecode.bpmn_simulator.animation.ref.NamedReference;
import com.googlecode.bpmn_simulator.animation.ref.Reference;
import com.googlecode.bpmn_simulator.animation.ref.ReferenceUtils;
import com.googlecode.bpmn_simulator.animation.token.Token;
import com.googlecode.bpmn_simulator.bpmn.model.core.common.FlowNode;
import com.googlecode.bpmn_simulator.bpmn.model.core.common.Message;
import com.googlecode.bpmn_simulator.bpmn.model.core.common.AbstractFlowNode;
import com.googlecode.bpmn_simulator.bpmn.model.core.common.events.BoundaryEvent;
import com.googlecode.bpmn_simulator.bpmn.model.core.common.events.StartEvent;
import com.googlecode.bpmn_simulator.bpmn.model.core.foundation.AbstractBaseElementNamed;

public class MessageFlow
		extends AbstractBaseElementNamed {

	private Reference<FlowNode> sourceRef;
	private Reference<FlowNode> targetRef;
	
	private Message containedMessage;

	
	public MessageFlow(final String id, final String name, Reference<FlowNode> sourceRef, Reference<FlowNode> targetRef) {
		super(id, name);
		this.sourceRef = sourceRef;
		this.targetRef = targetRef;
	}


	/**
	 * @return the sourceRef
	 */
	public Reference<FlowNode> getSourceRef() {
		return sourceRef;
	}


	/**
	 * @return the targetRef
	 */
	public Reference<FlowNode> getTargetRef() {
		return targetRef;
	}


	public boolean containsMessage() {
		return containedMessage == null ? false : true;
	}
	
	public void cleanMessage() {
		setContainedMessage(null);
	}
	
	public void setContainedMessage(Message containedMessage) {
		this.setContainedMessage(containedMessage, null);
	}


	public Message getContainedMessage() {
		return containedMessage;
	}


	public void setContainedMessage(Message containedMessage, Token token) {
		this.containedMessage = containedMessage;
		if (targetRef != null && targetRef.hasReference()) {
			FlowNode fNode = targetRef.getReferenced();
			if (fNode instanceof StartEvent && token != null)
//			token.getInstance().getParentContainer().addNewChildInstance(null).createNewToken(fNode, null);
			token.getInstance().createNewInstance(fNode);
			if (fNode instanceof BoundaryEvent)
				((BoundaryEvent)fNode).setCatched(true);
		}
	}

}
