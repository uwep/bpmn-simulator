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

import java.util.Iterator;

import com.googlecode.bpmn_simulator.animation.ref.Reference;
import com.googlecode.bpmn_simulator.animation.ref.ReferenceSet;
import com.googlecode.bpmn_simulator.animation.ref.References;
import com.googlecode.bpmn_simulator.animation.token.Instance;
import com.googlecode.bpmn_simulator.animation.token.Token;
import com.googlecode.bpmn_simulator.animation.token.Tokens;
import com.googlecode.bpmn_simulator.bpmn.model.core.common.DefaultSequenceFlowElement;
import com.googlecode.bpmn_simulator.bpmn.model.core.common.SequenceFlow;

public final class InclusiveGateway
		extends AbstractGateway
		implements DefaultSequenceFlowElement {

	private Reference<SequenceFlow> defaultSequenceFlow;

	public InclusiveGateway(final String id, final String name) {
		super(id, name);
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
	protected void forwardToken(final Token token) {
//		void copyTokenToOutgoing(final Token token, final Instance instance, final boolean firstOnly,
//				final DefaultSequenceFlowElement defaultSequenceFlowElement)
		copyTokenToOutgoing(token, token.getInstance(), false, this);
	}

	@Override
	protected void onTokenComplete(final Token token) {
		ReferenceSet<SequenceFlow> incomings = (ReferenceSet<SequenceFlow>)getIncoming();
		int countIncome = incomings.getReferencedCount();
		// count incoming sequence flows; 0 or 1 call super 
		if (countIncome < 2)
			super.onTokenComplete(token);
		else {
			final Tokens inputTokens = getTokenOtherIncoming(token);
			final int countToken = token.getInstance().getTokens(true).size();
			if (inputTokens.size() + 1 == countToken) {
				super.onTokenComplete(token);
				inputTokens.removeAll();
			}
		}
	}

	protected Tokens getTokenOtherIncoming(final Token forToken) {
		final Tokens tokens = new Tokens();
		final Tokens availableTokens = getCompleteTokens().getByInstance(forToken.getInstance());
		for (final SequenceFlow incoming : getIncoming()) {
			if (incoming.equals(forToken.getPreviousTokenFlow())) {
				continue;
			}
			final Tokens incomingTokens = availableTokens.getByPreviousTokenFlow(incoming);
			if (!incomingTokens.isEmpty()) 
				tokens.add(incomingTokens.get(0));
		}
		return tokens;
	}

}
