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

import java.awt.Dimension;
import java.util.Arrays;

import javax.swing.JSpinner;
import javax.swing.SpinnerListModel;
import javax.swing.SwingConstants;

@SuppressWarnings("serial")
public class SpeedSpinner
		extends JSpinner {

	public enum Speed {
		HALF("x\u00BD", 0.5f),
		NORMAL("x1", 1.0f),
		DOUBLE("x2", 2.0f),
		TRIPLE("x3", 3.0f),
		QUADUPLE("x4", 4.0f),
		QUINTUPLE("x5", 5.0f);

		private final String name;
		private final float factor;

		private Speed(final String name, final float factor) {
			this.name = name;
			this.factor = factor;
		}

		@Override
		public String toString() {
			return name;
		}

		public final float getFactor() {
			return factor;
		}

	}

	public SpeedSpinner() {
		super(new SpinnerListModel(Arrays.asList(Speed.values())));
		setValue(Speed.NORMAL);
		((ListEditor)getEditor()).getTextField().setHorizontalAlignment(SwingConstants.CENTER);
	}

	public float getSpeedFactor() {
		return ((Speed)getValue()).getFactor();
	}

	@Override
	public Dimension getPreferredSize() {
		final Dimension preferredSize = super.getPreferredSize();
		preferredSize.width = 50;
		preferredSize.height = 24;
		return preferredSize;
	}

	@Override
	public Dimension getMaximumSize() {
		return getPreferredSize();
	}

}
