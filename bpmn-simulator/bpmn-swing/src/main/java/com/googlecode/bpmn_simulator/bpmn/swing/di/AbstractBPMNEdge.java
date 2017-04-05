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

import java.awt.Graphics2D;

import com.googlecode.bpmn_simulator.animation.element.logical.LogicalElement;
import com.googlecode.bpmn_simulator.animation.element.visual.swing.AbstractVisualEdgeElement;
import com.googlecode.bpmn_simulator.bpmn.di.BPMNEdge;

@SuppressWarnings("serial")
public abstract class AbstractBPMNEdge<E extends LogicalElement>
		extends AbstractVisualEdgeElement<E>
		implements BPMNEdge {

	public AbstractBPMNEdge(final E element) {
		super(element);
	}

	@Override
	protected void paintElementBackground(final Graphics2D g) {
	}

	@Override
	protected void paintElementForeground(final Graphics2D g) {
		g.setPaint(getForegroundColor());
		paintElementLine(g);
		paintElementStart(g);
		paintElementEnd(g);
	}

	protected void paintElementStart(final Graphics2D g) {
	}

	protected void paintElementLine(final Graphics2D g) {
		getPresentation().drawLine(g, getWaypointsRelative());
	}

	protected void paintElementEnd(final Graphics2D g) {
	}

}
