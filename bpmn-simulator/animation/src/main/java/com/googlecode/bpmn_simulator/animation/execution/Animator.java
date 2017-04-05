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
package com.googlecode.bpmn_simulator.animation.execution;

import com.googlecode.bpmn_simulator.animation.token.RootInstances;

public class Animator
		extends AbstractAnimator {

	private RootInstances instances;

	public Animator(final RootInstances instances) {
		super();
		setInstances(instances);
		start();
	}

	protected synchronized void setInstances(final RootInstances instances) {
		this.instances = instances;
	}

	@Override
	public synchronized void step(final int count) {
		instances.step(count);
	}

}
