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

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Stroke;

import com.googlecode.bpmn_simulator.animation.element.visual.swing.AbstractVisual;
import com.googlecode.bpmn_simulator.bpmn.di.BPMNPlane;
import com.googlecode.bpmn_simulator.bpmn.model.core.foundation.BaseElement;

@SuppressWarnings("serial")
public abstract class AbstractBPMNPlane<E extends BaseElement>
		extends AbstractVisual
		implements BPMNPlane {

	private static final Stroke DEFAULT_STROKE = new BasicStroke(1.f);

	protected static final int MARGIN = 10;

	private final E bpmnElement;

	public AbstractBPMNPlane(final E element) {
		super();
		bpmnElement = element;
	}

	protected E getLogicalElement() {
		return bpmnElement;
	}

	@Override
	protected void paintElementBackground(final Graphics2D g) {
		g.setPaint(Color.WHITE);
	}

	protected Stroke getStroke() {
		return DEFAULT_STROKE;
	}

	@Override
	protected void paintElementForeground(final Graphics2D g) {
		g.setStroke(getStroke());
		g.setPaint(Color.BLACK);
	}

}
