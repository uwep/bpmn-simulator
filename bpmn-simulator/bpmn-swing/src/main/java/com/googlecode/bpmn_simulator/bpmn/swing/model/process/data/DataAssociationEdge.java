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

import java.awt.BasicStroke;
import java.awt.Graphics2D;
import java.awt.Stroke;

import com.googlecode.bpmn_simulator.animation.element.visual.Waypoints;
import com.googlecode.bpmn_simulator.bpmn.model.process.data.DataAssociation;
import com.googlecode.bpmn_simulator.bpmn.swing.di.AbstractBPMNEdge;
import com.googlecode.bpmn_simulator.bpmn.swing.di.Appearance;

@SuppressWarnings("serial")
public class DataAssociationEdge
		extends AbstractBPMNEdge<DataAssociation> {

	private static final Stroke LINE_STROKE = Appearance.getDefault().createStrokeDotted(1);
	private static final Stroke ARROW_STROKE = new BasicStroke(1);

	public DataAssociationEdge(final DataAssociation element) {
		super(element);
	}

	@Override
	protected void paintElementForeground(final Graphics2D g) {
		g.setStroke(LINE_STROKE);
		super.paintElementForeground(g);
	}

	@Override
	protected void paintElementEnd(final Graphics2D g) {
		super.paintElementEnd(g);
		final Waypoints waypoints = getWaypointsRelative();
		if (waypoints.isValid()) {
			g.setStroke(ARROW_STROKE);
			getPresentation().drawArrowhead(g, waypoints.nextToLast(), waypoints.last());
		}
	}

}
