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
package com.googlecode.bpmn_simulator.animation.element.visual.swing;

import java.awt.Color;

import com.googlecode.bpmn_simulator.animation.token.Instance;
import com.googlecode.bpmn_simulator.animation.token.Token;

public final class Colors {

	private static final Color DARKVIOLET = new Color(0x9400d3);
	private static final Color ORANGERED = new Color(0xff4500);
	private static final Color LIMEGREEN = new Color(0x32cd32);
	private static final Color CRIMSON = new Color(0xdc143c);
	private static final Color DARKTURQUOISE = new Color(0x00ced1);
	private static final Color DODGERBLUE = new Color(0x1e90ff);

	public static final Color BLACK = new Color(0x111111);
	public static final Color WHITE = new Color(0xFFFFFF);

	public static final Color GRAY = new Color(0xEEEEEE);

	public static final Color YELLOW = new Color(0xFFFFB5);
	public static final Color BLUE = new Color(0xDBF0F7);
	public static final Color BLUE2 = new Color(0xECF4FF);

	public static final Color GREEN = new Color(0xA4F0B7);
	public static final Color RED = new Color(0xFFA4A4);
	public static final Color ORANGE = new Color(0xFFD062);

	private static final Color[] INSTANCE_COLORS = {
		ORANGERED,
		DARKVIOLET,
		LIMEGREEN,
		CRIMSON,
		DARKTURQUOISE,
		DODGERBLUE,
	};

	private Colors() {
	}

	public static Color forToken(final Token token) {
		return forInstance(token.getInstance());
	}

	public static Color forInstance(final Instance instance) {
		final int index = instance.getRootId() % INSTANCE_COLORS.length;
		return INSTANCE_COLORS[index];
	}

}
