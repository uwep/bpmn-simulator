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
package com.googlecode.bpmn_simulator.tokenimport;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.googlecode.bpmn_simulator.animation.input.Definition;
import com.googlecode.bpmn_simulator.animation.token.RootInstances;

public abstract class AbstractTokenImporter
		implements TokenImporter {

	protected static final Logger LOG = LogManager.getLogger(LOGGER_NAME);

	private Definition<?> definition;

	private RootInstances instances;

	protected RootInstances getInstances() {
		return instances;
	}

	@Override
	public void setInstances(final RootInstances instances) {
		this.instances = instances;
	}

	protected Definition<?> getDefinition() {
		return definition;
	}

	@Override
	public void setDefinition(final Definition<?> definition) {
		this.definition = definition;
	}

	@Override
	public boolean configure()
			throws TokenImportException {
		return true;
	}

	@Override
	public void importTokens()
			throws TokenImportException {
		if (instances == null) {
			throw new TokenImportException("instances not set");
		}
		if (definition == null) {
			throw new TokenImportException("definition not set");
		}
	}

}
