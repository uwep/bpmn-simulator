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

import java.awt.Dimension;
import java.awt.Rectangle;

import javax.swing.JComponent;
import javax.swing.Scrollable;

import com.googlecode.bpmn_simulator.animation.element.visual.swing.AbstractSwingDiagram;
import com.googlecode.bpmn_simulator.bpmn.di.BPMNDiagram;

@SuppressWarnings("serial")
public class SwingBPMNDiagram
		extends AbstractSwingDiagram
		implements BPMNDiagram<JComponent>, Scrollable {

	private JComponent diagramPlane;

	protected SwingBPMNDiagram(final String name) {
		super();
		setName(name);
	}

	@Override
	public void setPlane(final JComponent plane) {
		if (diagramPlane != null) {
			remove(diagramPlane);
		}
		diagramPlane = plane;
		setLayout(new PlaneLayout(diagramPlane));
		add(diagramPlane);
	}

	@Override
	public void add(final JComponent element) {
		add(element, 0);
	}

	@Override
	public Dimension getPreferredScrollableViewportSize() {
		return super.getPreferredSize();
	}

	@Override
	public int getScrollableUnitIncrement(Rectangle visibleRect,
			int orientation, int direction) {
		return 0;
	}

	@Override
	public int getScrollableBlockIncrement(Rectangle visibleRect,
			int orientation, int direction) {
		return 0;
	}

	@Override
	public boolean getScrollableTracksViewportWidth() {
		return false;
	}

	@Override
	public boolean getScrollableTracksViewportHeight() {
		return false;
	}

}
