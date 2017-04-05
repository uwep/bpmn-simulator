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
package com.googlecode.bpmn_simulator.bpmn.model.core.common;

import com.googlecode.bpmn_simulator.animation.ref.Reference;
import com.googlecode.bpmn_simulator.animation.ref.ReferenceSet;
import com.googlecode.bpmn_simulator.animation.ref.References;
import com.googlecode.bpmn_simulator.animation.token.Instance;
import com.googlecode.bpmn_simulator.animation.token.Token;
import com.googlecode.bpmn_simulator.animation.token.TokenFlow;
import com.googlecode.bpmn_simulator.animation.token.Tokens;

public abstract class AbstractFlowNode
		extends AbstractFlowElement
		implements FlowNode {

	private final ReferenceSet<SequenceFlow> incoming = new ReferenceSet<>();
	private final ReferenceSet<SequenceFlow> outgoing = new ReferenceSet<>();

	public AbstractFlowNode(final String id, final String name) {
		super(id, name);
	}

	@Override
	public void addIncoming(final Reference<SequenceFlow> incoming) {
		this.incoming.add(incoming);
	}

	@Override
	public References<SequenceFlow> getIncoming() {
		return incoming;
	}

	@Override
	public void addOutgoing(final Reference<SequenceFlow> outgoing) {
		this.outgoing.add(outgoing);
	}

	@Override
	public References<SequenceFlow> getOutgoing() {
		return outgoing;
	}

	protected final void copyTokenToAllOutgoing(final Token token) {
		copyTokenToOutgoing(token, token.getInstance(), false, null);
	}

	protected final void copyTokenToFirstOutgoing(final Token token) {
		copyTokenToOutgoing(token, token.getInstance(), true, null);
	}

	protected final void copyTokenToOutgoing(final Token token,
			final Instance instance,
			final boolean firstOnly,
			final DefaultSequenceFlowElement defaultSequenceFlowElement) {
		createTokenAtOutgoing(instance, token.getPreviousTokenFlow(), firstOnly, defaultSequenceFlowElement);
	}

	private static boolean createTokenAtDefaultOutgoing(
			final Instance instance,
			final TokenFlow previous,
			final DefaultSequenceFlowElement element) {
		final SequenceFlow defaultSequenceFlow = element.getDefaultSequenceFlow();
		if (defaultSequenceFlow != null) {
			instance.createNewToken(defaultSequenceFlow, previous);
			return true;
		}
		return false;
	}

	protected final void createTokenAtOutgoing(
			final Instance instance,
			final TokenFlow previous,
			final boolean firstOnly,
			final DefaultSequenceFlowElement defaultSequenceFlowElement) {
		int conditionalCount = 0;
		for (final SequenceFlow outgoing : getOutgoing()) {
			if (!outgoing.isDefault()) {
				final boolean conditional = outgoing.isConditional();
				if (!conditional
						|| outgoing.getConditionExpression().getResult()) {
					instance.createNewToken(outgoing, previous);
					if (conditional) {
						++conditionalCount;
					}
					if (firstOnly) {
						break; 
					}
				}
			}
		}
		if ((conditionalCount == 0) && (defaultSequenceFlowElement != null)) {
			if (createTokenAtDefaultOutgoing(instance, previous, defaultSequenceFlowElement)) {
				++conditionalCount;
			}
		}
	}

	protected void forwardToken(final Token token) {
		copyTokenToAllOutgoing(token);
	}

	@Override
	protected void onTokenComplete(final Token token) {
		forwardToken(token);
		token.remove();
	}

	/**
	 * @return doesn't contains the forToken. returns null if not every incoming has a token
	 */
	protected Tokens getTokenForEveryIncoming(final Token forToken) {
		final Tokens tokens = new Tokens();
		final Tokens availableTokens = getCompleteTokens().getByInstance(forToken.getInstance());
		for (final SequenceFlow incoming : getIncoming()) {
			if (incoming.equals(forToken.getPreviousTokenFlow())) {
				continue;
			}
			final Tokens incomingTokens = availableTokens.getByPreviousTokenFlow(incoming);
			if (incomingTokens.isEmpty()) {
				return null;
			}
			tokens.add(incomingTokens.get(0));
		}
		return tokens;
	}

}
