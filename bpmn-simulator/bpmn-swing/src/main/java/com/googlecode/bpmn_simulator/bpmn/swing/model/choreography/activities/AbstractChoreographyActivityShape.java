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
package com.googlecode.bpmn_simulator.bpmn.swing.model.choreography.activities;

import java.awt.Graphics2D;

import com.googlecode.bpmn_simulator.animation.element.visual.Bounds;
import com.googlecode.bpmn_simulator.animation.element.visual.HorizontalPosition;
import com.googlecode.bpmn_simulator.animation.element.visual.Label;
import com.googlecode.bpmn_simulator.animation.element.visual.VerticalPosition;
import com.googlecode.bpmn_simulator.animation.element.visual.swing.ImageList;
import com.googlecode.bpmn_simulator.bpmn.model.choreography.activities.AbstractChoreographyActivity;
import com.googlecode.bpmn_simulator.bpmn.swing.di.AbstractBPMNShape;

@SuppressWarnings("serial")
public abstract class AbstractChoreographyActivityShape<E extends AbstractChoreographyActivity>
		extends AbstractBPMNShape<E> {

	public AbstractChoreographyActivityShape(final E element) {
		super(element);
	}

	@Override
	public void alignLabel(final Label label) {
		label.setBounds(getInnerBounds());
	}

	@Override
	protected void paintElementBackground(final Graphics2D g) {
		super.paintElementBackground(g);
		getPresentation().fillRect(g, getInnerBoundsRelative());
	}

	protected ImageList getMarkers() {
		return new ImageList();
	}

	protected void paintElementMarkers(final Graphics2D g) {
		final Bounds bounds = getInnerBoundsRelative();
		final ImageList markers = getMarkers();
		markers.drawHorizontal(g,
				bounds.getPoint(HorizontalPosition.CENTER, VerticalPosition.BOTTOM),
				HorizontalPosition.CENTER, VerticalPosition.TOP);
	}

	@Override
	protected void paintElementForeground(final Graphics2D g) {
		super.paintElementForeground(g);
		getPresentation().drawRect(g, getInnerBoundsRelative());
	}

}
