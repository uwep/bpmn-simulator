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

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public final class Instance
		extends InstanceContainer
		implements DataHolder {

	private final int rootId;

	private final TokenFlow tokenFlow;

	private final Tokens tokens = new Tokens();

	private final Set<InstanceListener> listeners = new HashSet<>();
	private final Set<TokensListener> tokensListeners = new HashSet<>();

	private Instance parentInstance;

	private boolean autoRemove = true;

	private String name;

	private Map<? extends Object, ? extends Object> data = null;

	protected Instance(final InstanceContainer parentContainer, final Instance parentInstance, final int rootId,
			final TokenFlow tokenFlow) {
		super(parentContainer);
		if (parentContainer == null) {
			throw new NullPointerException();
		}
		this.parentInstance = parentInstance;
		this.rootId = rootId;
		this.tokenFlow = tokenFlow;
	}

	@Override
	protected void detach() {
		super.detach();
		parentInstance = null;
	}

	public int getRootId() {
		return rootId;
	}

	public Instance getParentInstance() {
		return parentInstance;
	}

	public void setAutoRemove(final boolean auto) {
		autoRemove = auto;
	}

	public boolean isAutoRemove() {
		return autoRemove;
	}

	public TokenFlow getTokenFlow() {
		return tokenFlow;
	}

	public void setName(final String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	@Override
	public Map<? extends Object, ? extends Object> getData() {
		return data;
	}

	@Override
	public void setData(final Map<? extends Object, ? extends Object> data) {
		this.data = data;
	}

	public void addListener(final InstanceListener listener) {
		synchronized (listeners) {
			listeners.add(listener);
		}
	}

	public void removeListener(final InstanceListener listener) {
		synchronized (listeners) {
			listeners.remove(listener);
		}
	}

	protected void notifyInstanceRemove() {
		synchronized (listeners) {
			for (final InstanceListener listener : listeners) {
				listener.instanceRemove(this);
			}
		}
	}

	public void addTokensListener(final TokensListener listener) {
		synchronized (tokensListeners) {
			tokensListeners.add(listener);
		}
	}

	public void removeTokensListener(final TokensListener listener) {
		synchronized (tokensListeners) {
			tokensListeners.remove(listener);
		}
	}

	private void notifyTokenAdded(final Token token) {
		synchronized (tokensListeners) {
			for (final TokensListener listener : tokensListeners) {
				listener.tokenAdded(token);
			}
		}
	}

	private void notifyTokenRemove(final Token token) {
		synchronized (tokensListeners) {
			for (final TokensListener listener : tokensListeners) {
				listener.tokenRemove(token);
			}
		}
	}

	public Tokens getTokens(final boolean recursive) {
		final Tokens allTokens = new Tokens();
		allTokens.addAll(tokens);
		if (recursive) {
			for (final Instance childInstance : this) {
				allTokens.addAll(childInstance.getTokens(recursive));
			}
		}
		return allTokens;
	}

	private void addToken(final Token token) {
		if (token == null) {
			throw new NullPointerException();
		}
		if (tokens.contains(token)) {
			throw new IllegalArgumentException();
		}
		tokens.add(token);
		notifyTokenAdded(token);
	}

	public boolean hasTokens() {
		return !tokens.isEmpty();
	}

	public boolean isEmpty() {
		return !hasTokens() && !hasChildInstances();
	}

	private void checkAutoRemove() {
		if (isAutoRemove() && isEmpty()) {
			remove();
		}
	}

	public Token createNewToken(final TokenFlow currentTokenFlow, final TokenFlow previousTokenFlow) {
		final Token token = new Token(this, currentTokenFlow, previousTokenFlow);
		addToken(token);
		token.getCurrentTokenFlow().tokenEnter(token);
		return token;
	}

	@Override
	protected void removeChildInstance(final Instance childInstance) {
		super.removeChildInstance(childInstance);
		checkAutoRemove();
	}

	protected void removeToken(final Token token) {
		if (token == null) {
			throw new NullPointerException();
		}
		if (!tokens.contains(token)) {
			throw new IllegalArgumentException();
		}
		token.getCurrentTokenFlow().tokenExit(token);
		notifyTokenRemove(token);
		tokens.remove(token);
		token.detach();
		checkAutoRemove();
	}

	protected void removeAllTokens() {
		for (final Token token : new ArrayList<>(tokens)) {
			token.remove();
		}
	}

	public void remove() {
		setAutoRemove(false);
		clear();
		notifyInstanceRemove();
		final InstanceContainer parentContainer = getParentContainer();
		if (parentContainer != null) {
			parentContainer.removeChildInstance(this);
		} else {
			throw new IllegalStateException();
		}
	}

	@Override
	public void clear() {
		super.clear();
		removeAllTokens();
	}

	@Override
	public void step(final int count) {
		super.step(count);
		for (final Token token : new ArrayList<>(tokens)) {
			if (tokens.contains(token)) {
				token.step(count);
			}
		}
	}

	@Override
	protected Instance createNewChildInstance(final TokenFlow tokenFlow) {
		return new Instance(this, this, getRootId(), tokenFlow);
	}

	@Override
	public String toString() {
		final StringBuilder builder = new StringBuilder(super.toString());
		builder.append(" (").append(getTokenFlow()).append(")");
		return builder.toString();
	}

	@Override
	protected void dump(final PrintStream out, final int level) {
		super.dump(out, level);
		final Iterator<Token> i = tokens.iterator();
		while (i.hasNext()) {
			out.println(pad(level + 1) + "- " + i.next().toString());
		}
	}

}
