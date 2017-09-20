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
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public abstract class InstanceContainer
		implements Iterable<Instance> {

	private final Collection<Instance> childInstances = new ArrayList<>();

	private final Set<InstancesListener> instancesListeners = new HashSet<>();

	private InstanceContainer parent;

	protected InstanceContainer(final InstanceContainer parent) {
		super();
		this.parent = parent;
	}

	protected void detach() {
		parent = null;
	}
	
	public InstanceContainer getTopLevelContainer() {
		InstanceContainer container = this;
		while (true) {
			final InstanceContainer parent = container.getParentContainer();
			if (parent == null) {
				break;
			}
			container = parent;
		}
		return container;
	}

	public InstanceContainer getParentContainer() {
		return parent;
	}

	@Override
	public Iterator<Instance> iterator() {
		return childInstances.iterator();
	}

	public void addInstancesListener(final InstancesListener listener) {
		synchronized (instancesListeners) {
			instancesListeners.add(listener);
		}
	}

	public void removeInstancesListener(final InstancesListener listener) {
		synchronized (instancesListeners) {
			instancesListeners.remove(listener);
		}
	}

	private void notifyInstanceAdded(final Instance instance) {
		synchronized (instancesListeners) {
			for (final InstancesListener listener : instancesListeners) {
				listener.instanceAdded(instance);
			}
		}
	}

	private void notifyInstanceRemove(final Instance instance) {
		synchronized (instancesListeners) {
			for (final InstancesListener listener : instancesListeners) {
				listener.instanceRemove(instance);
			}
		}
	}

	protected void addChildInstance(final Instance childInstance) {
		if (childInstance == null) {
			throw new NullPointerException();
		}
		if (childInstances.contains(childInstance)) {
			throw new IllegalArgumentException();
		}
		childInstances.add(childInstance);
		notifyInstanceAdded(childInstance);
	}

	public boolean hasChildInstances() {
		return !childInstances.isEmpty();
	}

	protected abstract Instance createNewChildInstance(TokenFlow tokenFlow);

	public Instance addNewChildInstance(final TokenFlow tokenFlow) {
		final Instance instance = createNewChildInstance(tokenFlow);
		addChildInstance(instance);
		return instance;
	}

	protected void removeChildInstance(final Instance childInstance) {
		if (childInstance == null) {
			throw new NullPointerException();
		}
		if (!childInstances.contains(childInstance)) {
			throw new IllegalArgumentException();
		}
		notifyInstanceRemove(childInstance);
		childInstances.remove(childInstance);
		childInstance.detach();
	}

	protected void removeAllChildInstances() {
		for (final Instance childInstance : new ArrayList<>(childInstances)) {
			childInstance.remove();
		}
	}

	public void clear() {
		removeAllChildInstances();
	}

	public void step(final int count) {
		for (final Instance childInstance : new ArrayList<>(childInstances)) {
			if (childInstances.contains(childInstance)) {
				childInstance.step(count);
			}
		}
	}

	public void dump(final PrintStream out) {
		getTopLevelContainer().dump(out, 0);
	}

	protected static String pad(final int count) {
		final StringBuilder builder = new StringBuilder(count);
		for (int i = 0; i < count; ++i) {
			builder.append('\t');
		}
		return builder.toString();
	}

	protected void dump(final PrintStream out, final int level) {
		out.println(pad(level) + "+ " + toString());
		final Iterator<Instance> i = childInstances.iterator();
		while (i.hasNext()) {
			i.next().dump(out, level + 1);
		}
	}

}
