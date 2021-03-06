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
package com.googlecode.bpmn_simulator.bpmn.swing.model.core.common.gateways;

import java.awt.BasicStroke;
import java.awt.Graphics2D;
import java.awt.Stroke;

import com.googlecode.bpmn_simulator.animation.element.visual.Bounds;
import com.googlecode.bpmn_simulator.animation.element.visual.Point;
import com.googlecode.bpmn_simulator.bpmn.model.core.common.gateways.ComplexGateway;

@SuppressWarnings("serial")
public final class ComplexGatewayShape
		extends AbstractGatewayShape<ComplexGateway> {

	private static final Stroke SYMBOL_STROKE = new BasicStroke(3.f);

	public ComplexGatewayShape(final ComplexGateway element) {
		super(element);
	}

	@Override
	protected void paintElementForeground(final Graphics2D g) {
		super.paintElementForeground(g);
		g.setStroke(SYMBOL_STROKE);
		Bounds symbolBounds = getInnerBoundsRelative().scaleSize(0.3f);
		getPresentation().drawLine(g,
				new Point(symbolBounds.getMinX(), symbolBounds.getMinY()),
				new Point(symbolBounds.getMaxX(), symbolBounds.getMaxY()));
		getPresentation().drawLine(g,
				new Point(symbolBounds.getMinX(), symbolBounds.getMaxY()),
				new Point(symbolBounds.getMaxX(), symbolBounds.getMinY()));
		symbolBounds = getInnerBoundsRelative().scaleSize(0.5f);
		getPresentation().drawLine(g,
				new Point((int) symbolBounds.getCenterX(), symbolBounds.getMinY()),
				new Point((int) symbolBounds.getCenterX(), symbolBounds.getMaxY()));
		getPresentation().drawLine(g,
				new Point(symbolBounds.getMinX(), (int) symbolBounds.getCenterY()),
				new Point(symbolBounds.getMaxX(), (int) symbolBounds.getCenterY()));
	}

}
