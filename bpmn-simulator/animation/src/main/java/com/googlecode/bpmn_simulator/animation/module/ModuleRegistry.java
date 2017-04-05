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
package com.googlecode.bpmn_simulator.animation.module;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class ModuleRegistry {

	private static final ModuleRegistry instance = new ModuleRegistry();

	private final Map<String, Module> modules = new HashMap<>();

	public static ModuleRegistry getDefault() {
		return instance;
	}

	public void registerModule(final Module module) {
		if ((module == null) || modules.containsKey(module.getName())) {
			throw new IllegalArgumentException();
		}
		modules.put(module.getName(), module);
	}

	public Collection<Module> getModules() {
		return Collections.unmodifiableCollection(modules.values());
	}

	public Collection<Module> getModulesByFileExtension(final String extension) {
		final Collection<Module> modules = new ArrayList<>();
		if (extension != null) {
			for (final Module module : getModules()) {
				for (final String moduleExtension : module.getFileExtensions()) {
					if (extension.equalsIgnoreCase(moduleExtension)) {
						modules.add(module);
					}
				}
			}
		}
		return modules;
	}

}
