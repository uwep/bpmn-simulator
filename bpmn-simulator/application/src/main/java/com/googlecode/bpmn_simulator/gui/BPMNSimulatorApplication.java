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
package com.googlecode.bpmn_simulator.gui;


import java.io.File;
import javax.swing.SwingUtilities;

import com.googlecode.bpmn_simulator.animation.module.Module;
import com.googlecode.bpmn_simulator.animation.module.ModuleRegistry;
import com.googlecode.bpmn_simulator.bpmn.swing.BPMNModule;
import com.googlecode.bpmn_simulator.gui.preferences.Config;

public final class BPMNSimulatorApplication {

	private BPMNSimulatorApplication() {
	}

	private static void loadModules() {
		try {
			Class.forName(BPMNModule.class.getCanonicalName());
		} catch (ClassNotFoundException e) {
		}
		for (final Module module : ModuleRegistry.getDefault().getModules()) {
			module.load();
		}
		Config.getInstance().loadLogicalElements();
		Config.getInstance().loadVisualElements();
	}

	public static void main(final String[] args) {

		Config.getInstance().load();

		Theme.init();

		loadModules();

		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				final BPMNSimulatorFrame frame = new BPMNSimulatorFrame();
				frame.setVisible(true);
				if (args.length >= 1) {
					frame.openSource(new DefinitionSource(new File(args[0]), null));
				}
			}
		});
	}

}
