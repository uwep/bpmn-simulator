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
package com.googlecode.bpmn_simulator.bpmn.swing.model.core.common;

import java.awt.Graphics2D;
import java.awt.Dimension;
import java.awt.Paint;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JCheckBox;

import com.googlecode.bpmn_simulator.animation.element.visual.Bounds;
import com.googlecode.bpmn_simulator.animation.element.visual.GeometryUtils;
import com.googlecode.bpmn_simulator.animation.element.visual.Point;
import com.googlecode.bpmn_simulator.animation.element.visual.Waypoint;
import com.googlecode.bpmn_simulator.animation.element.visual.Waypoints;
import com.googlecode.bpmn_simulator.animation.ref.ReferenceUtils;
import com.googlecode.bpmn_simulator.bpmn.model.core.common.Expression;
import com.googlecode.bpmn_simulator.bpmn.model.core.common.SequenceFlow;
import com.googlecode.bpmn_simulator.bpmn.model.core.common.gateways.Gateway;
import com.googlecode.bpmn_simulator.bpmn.swing.di.AbstractBPMNTokenEdge;

@SuppressWarnings("serial")
public final class SequenceFlowEdge
		extends AbstractBPMNTokenEdge<SequenceFlow> {

	private static final double SYMBOL_DEFAULT_RAD = Math.toRadians(45.);

	private static final int START_SYMBOL_SIZE = 8;

	private JCheckBox checkBox;

	public SequenceFlowEdge(final SequenceFlow element) {
		super(element);
	}

	@Override
	public void addNotify() {
		super.addNotify();
		if (checkBox == null) {
			createCheckbox();
		}
		updateCheckboxPosition();
	}

	private void createCheckbox() {
		final SequenceFlow sequenceflow = getLogicalElement();
		if (sequenceflow.isConditional()) {
			final Expression expression = sequenceflow.getConditionExpression();
			checkBox = new JCheckBox();
			checkBox.setOpaque(false);
			checkBox.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(final ActionEvent event) {
					expression.setValue(checkBox.isSelected());
				}
			});
			checkBox.setSelected(expression.getResult());
			getParent().add(checkBox, 0);
		}
	}

	private void updateCheckboxPosition() {
		if (checkBox != null) {
			final Waypoints waypoints = getWaypoints();
			final Point point = waypoints.getWaypoint(waypoints.getLength() / 2.);
			if (point != null) {
				final Dimension size = checkBox.getPreferredSize();
				checkBox.setBounds(point.getX() - (size.width / 2), point.getY() - (size.height / 2),
						size.width, size.height);
			}
		}
	}

	@Override
	public void addElementWaypoint(final Waypoint waypoint) {
		super.addElementWaypoint(waypoint);
		updateCheckboxPosition();
	}

	private boolean isSourceGateway() {
		return (ReferenceUtils.element(getLogicalElement().getSource()) instanceof Gateway);
	}

	@Override
	protected void paintElementStart(final Graphics2D g) {
		super.paintElementStart(g);
		final SequenceFlow sequenceFlow = getLogicalElement();
		if (sequenceFlow.isDefault()) {
			if (!sequenceFlow.isConditional()) {
				drawDefaultSymbol(g);
			}
		} else {
			if (sequenceFlow.isConditional() && !isSourceGateway()) {
				drawConditionalSymbol(g);
			}
		}
	}

	private void drawDefaultSymbol(final Graphics2D g) {
		final Waypoints waypoints = getWaypointsRelative();
		final Point position = waypoints.getWaypoint(START_SYMBOL_SIZE);
		final double angle = waypoints.getAngleAt(START_SYMBOL_SIZE) - SYMBOL_DEFAULT_RAD;
		final double length = START_SYMBOL_SIZE * 0.8;
		final Point from = GeometryUtils.polarToCartesian(position, length, angle);
		final Point to = GeometryUtils.polarToCartesian(position, length, GeometryUtils.invert(angle));
		getPresentation().drawLine(g, from, to);
	}

	private void drawConditionalSymbol(final Graphics2D g) {
		final Waypoints waypoints = getWaypointsRelative();
		final Point position = waypoints.getWaypoint(START_SYMBOL_SIZE);
		final double angle = waypoints.getAngleAt(START_SYMBOL_SIZE);
		final Bounds diamondBounds = Bounds.fromCenter(position, START_SYMBOL_SIZE, (int) (START_SYMBOL_SIZE * 0.6));
		getPresentation().rotate(g, position, angle);
		final Paint paint = g.getPaint();
		g.setPaint(getBackgroundColor());
		getPresentation().fillDiamond(g, diamondBounds);
		g.setPaint(paint);
		getPresentation().drawDiamond(g, diamondBounds);
		getPresentation().restore(g);
	}

	@Override
	protected void paintElementEnd(final Graphics2D g) {
		super.paintElementEnd(g);
		final Waypoints waypoints = getWaypointsRelative();
		if (waypoints.isValid()) {
			getPresentation().fillArrowhead(g, waypoints.nextToLast(), waypoints.last());
		}
	}

}
