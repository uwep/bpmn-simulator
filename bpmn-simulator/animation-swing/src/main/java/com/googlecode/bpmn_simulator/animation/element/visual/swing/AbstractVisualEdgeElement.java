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

import javax.swing.SwingUtilities;

import com.googlecode.bpmn_simulator.animation.element.logical.LogicalElement;
import com.googlecode.bpmn_simulator.animation.element.visual.HorizontalPosition;
import com.googlecode.bpmn_simulator.animation.element.visual.Label;
import com.googlecode.bpmn_simulator.animation.element.visual.Point;
import com.googlecode.bpmn_simulator.animation.element.visual.VerticalPosition;
import com.googlecode.bpmn_simulator.animation.element.visual.VisualEdgeElement;
import com.googlecode.bpmn_simulator.animation.element.visual.Waypoint;
import com.googlecode.bpmn_simulator.animation.element.visual.Waypoints;

@SuppressWarnings("serial")
public abstract class AbstractVisualEdgeElement<E extends LogicalElement>
		extends AbstractVisualElement<E>
		implements VisualEdgeElement {

	private Waypoints waypoints = new Waypoints();

	public AbstractVisualEdgeElement(final E element) {
		super(element);
	}

	@Override
	public void addElementWaypoint(final Waypoint waypoint) {
		waypoints.add(waypoint);
		setInnerBounds(waypoints.getBounds());
	}

	protected Waypoint relativeWaypoint(final Point point) {
		final java.awt.Point p = SwingUtilities.convertPoint(getParent(), point.getX(), point.getY(), this);
		return new Waypoint(p.x, p.y);
	}

	protected Waypoints getWaypointsRelative() {
		final Waypoints relativeWaypoints = new Waypoints();
		for (final Waypoint waypoint : waypoints) {
			relativeWaypoints.add(relativeWaypoint(waypoint));
		}
		return relativeWaypoints;
	}

	protected Waypoints getWaypoints() {
		return waypoints;
	}

	@Override
	public void alignLabel(final Label label) {
		final double length = getWaypoints().getLength();
		final Point centerPoint = getWaypoints().getWaypoint((float) (length / 2.));
		label.setPosition(centerPoint, HorizontalPosition.CENTER, VerticalPosition.BOTTOM);
	}

}
