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

import java.util.ArrayList;
import java.util.Collection;

public class Tokens
		extends ArrayList<Token> {

	private static final long serialVersionUID = 3081643367990940828L;

	public Tokens() {
		super();
	}

	public Tokens(final Collection<? extends Token> c) {
		super(c);
	}

	@Override
	public boolean addAll(final Collection<? extends Token> c) {
		for (final Token token : c) {
			if (contains(token)) {
				assert false;
			}
		}
		return super.addAll(c);
	}

	@Override
	public boolean add(final Token token) {
		assert !contains(token) : "Token " + token + " already exist";
		return super.add(token);
	}

	@Override
	public boolean remove(final Object token) {
		assert contains(token) : "Token " + token + " doesn't exist";
		return super.remove(token);
	}

	public Collection<Instance> getInstances() {
		final Collection<Instance> instances = new ArrayList<>();
		for (final Token token : this) {
			final Instance instance = token.getInstance();
			if (!instances.contains(instance)) {
				instances.add(instance);
			}
		}
		return instances;
	}

	public Tokens getByInstance(final Instance instance) {
		final Tokens tokens = new Tokens();
		for (final Token token : this) {
			final Instance tokenInstance = token.getInstance();
			if (instance.equals(tokenInstance)) {
				tokens.add(token);
			}
		}
		return tokens;
	}

	public Tokens getByCurrentTokenFlow(final TokenFlow tokenFlow) {
		final Tokens tokens = new Tokens();
		for (final Token token : this) {
			final TokenFlow tokenTokenFlow = token.getCurrentTokenFlow();
			if (tokenFlow.equals(tokenTokenFlow)) {
				tokens.add(token);
			}
		}
		return tokens;
	}

	public Tokens getByPreviousTokenFlow(final TokenFlow tokenFlow) {
		final Tokens tokens = new Tokens();
		for (final Token token : this) {
			final TokenFlow tokenTokenFlow = token.getPreviousTokenFlow();
			if (tokenFlow.equals(tokenTokenFlow)) {
				tokens.add(token);
			}
		}
		return tokens;
	}

	public void removeAll() {
		for (final Token token : this) {
			token.remove();
		}
	}

}
