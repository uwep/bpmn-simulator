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

import java.net.URI;

public final class ApplicationInfo {

	private static final String NOTICE =
			"Copyright (C) 2015 Stefan Schweitzer\n" //$NON-NLS-1$
			+ "\n"  //$NON-NLS-1$
			+ "This software was created by Stefan Schweitzer as a student's project at\n" //$NON-NLS-1$
			+ "Fachhochschule Kaiserslautern (University of Applied Sciences),\n" //$NON-NLS-1$
			+ "refactored 2017 by Uwe Poborski TH Köln.\n"
			+ "Licensed under the Apache License, Version 2.0 (the \"License\");\n" //$NON-NLS-1$
			+ "you may not use this Software except in compliance with the License.\n" //$NON-NLS-1$
			+ "You may obtain a copy of the License at\n" //$NON-NLS-1$
			+ "\n" //$NON-NLS-1$
			+ "       http://www.apache.org/licenses/LICENSE-2.0\n" //$NON-NLS-1$
			+ "\n" //$NON-NLS-1$
			+ "Unless required by applicable law or agreed to in writing, software\n" //$NON-NLS-1$
			+ "distributed under the License is distributed on an \"AS IS\" BASIS,\n" //$NON-NLS-1$
			+ "WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.\n" //$NON-NLS-1$
			+ "See the License for the specific language governing permissions and\n" //$NON-NLS-1$
			+ "limitations under the License."; //$NON-NLS-1$

	private static final String NAME = "BPMN2.0 Token Animation";  //$NON-NLS-1$

	private static final URI WEBSITE = URI.create("https://github.com/uwep/bpmn-simulator.git"); //$NON-NLS-1$

	private ApplicationInfo() {
	}

	public static String getName() {
		return NAME;
	}

	public static String getVersion() {
		final Package pkg = BPMNSimulatorApplication.class.getPackage();
		if (pkg != null) {
			return pkg.getImplementationVersion();
		}
		return "unknow";
	}

	public static URI getWebsite() {
		return WEBSITE;
	}

	public static String getLicense() {
		return NOTICE;
	}

}
