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
package com.googlecode.bpmn_simulator.bpmn.swing.di;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.LayoutManager;
import java.awt.Rectangle;

import javax.swing.JComponent;

class PlaneLayout
		implements LayoutManager {

	private static final Dimension MIN_SIZE = new Dimension(100, 100);

	private static final int PADDING = 10;

	private final JComponent plane;

	public PlaneLayout(final JComponent plane) {
		super();
		this.plane = plane;
	}

	@Override
	public void addLayoutComponent(final String name, final Component comp) {
	}

	@Override
	public void removeLayoutComponent(final Component comp) {
	}

	@Override
	public Dimension preferredLayoutSize(final Container parent) {
		int i = 0;
		double width = 0.;
		double height = 0.;
		for (final Component component : parent.getComponents()) {
			if (component != plane) {
				final Rectangle bounds = component.getBounds();
				width = Math.max(width, bounds.getMaxX());
				height = Math.max(height, bounds.getMaxY());
				++i;
			}
		}
		if (i > 0) {
			return new Dimension((int) width + PADDING, (int) height + PADDING);
		} else {
			return minimumLayoutSize(parent);
		}
	}

	@Override
	public Dimension minimumLayoutSize(final Container parent) {
		return MIN_SIZE;
	}

	@Override
	public void layoutContainer(final Container parent) {
		if (plane != null) {
			plane.setSize(parent.getSize());
		}
	}
	
}