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
package com.googlecode.bpmn_simulator.animation.token;

import java.util.Map;

public class Token
		implements DataHolder {

	private final TokenFlow currentTokenFlow;

	private final TokenFlow previousTokenFlow;

	private Instance instance;

	private int position;

	private Map<? extends Object, ? extends Object> data = null;

	protected Token(final Instance instance,
			final TokenFlow current, final TokenFlow previous) {
		super();
		if (instance == null) {
			throw new NullPointerException();
		}
		this.instance = instance;
		if (current == null) {
			throw new NullPointerException();
		}
		this.currentTokenFlow = current;
		this.previousTokenFlow = previous;
	}

	protected void detach() {
		instance = null;
	}

	public Instance getInstance() {
		return instance;
	}

	public TokenFlow getCurrentTokenFlow() {
		return currentTokenFlow;
	}

	public TokenFlow getPreviousTokenFlow() {
		return previousTokenFlow;
	}

	@Override
	public Map<? extends Object, ? extends Object> getData() {
		return data;
	}

	@Override
	public void setData(final Map<? extends Object, ? extends Object> data) {
		this.data = data;
	}

	public int getPosition() {
		return position;
	}

	public void setPosition(final int position) {
		assert position >= 0;
		this.position = position;
	}

	public void step(final int count) {
		if (count <= 0) {
			throw new IllegalArgumentException();
		}
		setPosition(getPosition() + count);
		getCurrentTokenFlow().tokenDispatch(this);
	}

	@Override
	public String toString() {
		final StringBuilder buffer = new StringBuilder(super.toString());
		buffer.append(" (").append(currentTokenFlow).append(")");
		return buffer.toString();
	}

	public void remove() {
		getInstance().removeToken(this);
	}

	public void copyTo(final TokenFlow tokenFlow) {
		copyTo(tokenFlow, getInstance());
	}

	public void copyTo(final TokenFlow tokenFlow, final Instance instance) {
		if (getCurrentTokenFlow() == tokenFlow) {
			throw new IllegalArgumentException();
		}
		instance.createNewToken(tokenFlow, getCurrentTokenFlow());
	}

	public void moveTo(final TokenFlow tokenFlow) {
		copyTo(tokenFlow);
		remove();
	}

}
