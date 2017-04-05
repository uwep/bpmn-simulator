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

import java.util.HashSet;
import java.util.Set;

import com.googlecode.bpmn_simulator.animation.execution.Animator;
import com.googlecode.bpmn_simulator.animation.token.Token;
import com.googlecode.bpmn_simulator.animation.token.TokenFlowListener;
import com.googlecode.bpmn_simulator.animation.token.Tokens;
import com.googlecode.bpmn_simulator.bpmn.model.core.foundation.AbstractBaseElementNamed;

public abstract class AbstractFlowElement
		extends AbstractBaseElementNamed
		implements FlowElement {

	private static final int DEFAULT_STEP_COUNT = Animator.secondsToSteps(3.);

	private int stepCount = DEFAULT_STEP_COUNT;

	private TokenEvent actionTrigger = TokenEvent.ON_EXIT;

	private final Set<TokenFlowListener> tokenFlowListeners
			= new HashSet<>();

	private final Tokens tokens = new Tokens();

	public AbstractFlowElement(final String id, final String name) {
		super(id, name);
	}

	@Override
	public void addTokenFlowListener(final TokenFlowListener listener) {
		assert !tokenFlowListeners.contains(listener);
		tokenFlowListeners.add(listener);
	}

	@Override
	public void removeTokenFlowListener(final TokenFlowListener listener) {
		tokenFlowListeners.add(listener);
	}

	protected void notifyTokenChanged(final Token token) {
		for (final TokenFlowListener listener : tokenFlowListeners) {
			listener.tokenChanged(token);
		}
	}

	@Override
	public Tokens getTokens() {
		return tokens;
	}

	@Override
	public void tokenEnter(final Token token) {
		if (tokens.add(token)) {
			if (actionTrigger == TokenEvent.ON_ENTER) {
				onTokenAction(token);
			}
			notifyTokenChanged(token);
		}
	}

	@Override
	public void tokenExit(final Token token) {
		if (tokens.remove(token)) {
			if (actionTrigger == TokenEvent.ON_EXIT) {
				onTokenAction(token);
			}
			notifyTokenChanged(token);
		}
	}

	protected void onTokenAction(final Token token) {
	}

	@Override
	public int getStepCount() {
		return stepCount;
	}

	@Override
	public void setStepCount(final int count) {
		assert stepCount >= 0;
		stepCount = count;
	}

	protected Tokens getCompleteTokens() {
		final Tokens tokens = new Tokens();
		for (final Token token : getTokens()) {
			if (isTokenComplete(token)) {
				tokens.add(token);
			}
		}
		return tokens;
	}

	protected boolean isTokenComplete(final Token token) {
		return token.getPosition() > getStepCount();
	}

	protected abstract void onTokenComplete(Token token);

	@Override
	public final void tokenDispatch(final Token token) {
		assert getTokens().contains(token);
		if (isTokenComplete(token)) {
			onTokenComplete(token);
		} else {
			notifyTokenChanged(token);
		}
	}

	private enum TokenEvent {
		ON_ENTER,
		ON_EXIT,
	}

}
