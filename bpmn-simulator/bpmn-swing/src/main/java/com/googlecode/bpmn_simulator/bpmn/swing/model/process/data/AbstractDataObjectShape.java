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
package com.googlecode.bpmn_simulator.bpmn.swing.model.process.data;

import java.awt.Graphics2D;
import java.awt.Image;

import com.googlecode.bpmn_simulator.animation.element.logical.LogicalElement;
import com.googlecode.bpmn_simulator.animation.element.visual.Bounds;
import com.googlecode.bpmn_simulator.animation.element.visual.HorizontalPosition;
import com.googlecode.bpmn_simulator.animation.element.visual.Label;
import com.googlecode.bpmn_simulator.animation.element.visual.Point;
import com.googlecode.bpmn_simulator.animation.element.visual.VerticalPosition;
import com.googlecode.bpmn_simulator.bpmn.swing.di.AbstractBPMNShape;

@SuppressWarnings("serial")
abstract class AbstractDataObjectShape<E extends LogicalElement>
		extends AbstractBPMNShape<E> {

	public AbstractDataObjectShape(final E element) {
		super(element);
	}

	@Override
	public void alignLabel(final Label label) {
		label.setPosition(getInnerBounds().getPoint(HorizontalPosition.CENTER, VerticalPosition.BOTTOM),
				HorizontalPosition.CENTER, VerticalPosition.BOTTOM);
	}

	private static int getN(final Bounds bounds) {
		return (int) Math.round(Math.min(bounds.getWidth(), bounds.getHeight()) * 0.25);
	}

	protected abstract Image getDataIcon();

	@Override
	protected void paintElementBackground(final Graphics2D g) {
		super.paintElementBackground(g);
		final Bounds bounds = getInnerBoundsRelative();
		getPresentation().fillDocument(g, bounds, getN(bounds));
	}

	@Override
	protected void paintElementForeground(final Graphics2D g) {
		super.paintElementForeground(g);
		final Bounds bounds = getInnerBoundsRelative();
		final int n = getN(bounds);
		getPresentation().drawDocument(g, bounds, n);
		final Point p = new Point(bounds.getMaxX() - n, bounds.getMinY() + n);
		getPresentation().drawLine(g,
				new Point(bounds.getMaxX() - n, bounds.getMinY()), p);
		getPresentation().drawLine(g,
				new Point(bounds.getMaxX(), bounds.getMinY() + n), p);
		final Image icon = getDataIcon();
		if (icon != null) {
			getPresentation().drawImage(g, icon, getIconBounds());
		}
	}

}
