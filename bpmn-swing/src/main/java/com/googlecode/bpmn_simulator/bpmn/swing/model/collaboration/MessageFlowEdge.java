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
package com.googlecode.bpmn_simulator.bpmn.swing.model.collaboration;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Stroke;

import com.googlecode.bpmn_simulator.animation.element.visual.Bounds;
import com.googlecode.bpmn_simulator.animation.element.visual.Waypoints;
import com.googlecode.bpmn_simulator.bpmn.model.collaboration.MessageFlow;
import com.googlecode.bpmn_simulator.bpmn.swing.di.AbstractBPMNEdge;
import com.googlecode.bpmn_simulator.bpmn.swing.di.Appearance;

@SuppressWarnings("serial")
public class MessageFlowEdge
			extends AbstractBPMNEdge<MessageFlow> {

	private static final Stroke START_STROKE = Appearance.getDefault().createStrokeSolid(2);
	private static final Stroke LINE_STROKE = Appearance.getDefault().createStrokeDashed(2);
	private static final Stroke END_STROKE = Appearance.getDefault().createStrokeSolid(2);

	public MessageFlowEdge(final MessageFlow element) {
		super(element);
	}

	@Override
	protected void paintElementLine(final Graphics2D g) {
		if (this.getLogicalElement().containsMessage())
			g.setColor(Color.GREEN);
		else
			g.setColor(Color.BLACK);
		g.setStroke(LINE_STROKE);
		super.paintElementLine(g);
		super.repaint();
	}

	@Override
	protected void paintElementStart(final Graphics2D g) {
		if (this.getLogicalElement().containsMessage())
			g.setColor(Color.GREEN);
		else
			g.setColor(Color.BLACK);
		super.paintElementStart(g);
		final Waypoints waypoints = getWaypointsRelative();
		if (waypoints.isValid()) {
			final Bounds bounds = Bounds.fromCenter(waypoints.first(), 3);
			g.setStroke(START_STROKE);
			getPresentation().drawOval(g, bounds);
		}
		super.repaint();
	}

	@Override
	protected void paintElementEnd(final Graphics2D g) {
		if (this.getLogicalElement().containsMessage())
			g.setColor(Color.GREEN);
		else
			g.setColor(Color.BLACK);
		super.paintElementEnd(g);
		g.setStroke(END_STROKE);
		final Waypoints waypoints = getWaypointsRelative();
		if (waypoints.isValid()) {
			getPresentation().drawArrowhead(g, waypoints.nextToLast(), waypoints.last());
		}
		super.repaint();
	}

}
