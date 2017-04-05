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

import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JComponent;

@SuppressWarnings("serial")
public abstract class AbstractVisual
		extends JComponent {

	private static final Presentation presentation = new Presentation();

	public Presentation getPresentation() {
		return presentation;
	}

	@Override
	public void paint(final Graphics g) {
		super.paint(g);
		final Graphics2D g2d = (Graphics2D) g;
		getPresentation().init(g2d);
		paintElement(g2d);
	}

	protected void paintElement(final Graphics2D g) {
		paintElementBackground(g);
		paintElementForeground(g);
	}

	protected abstract void paintElementBackground(Graphics2D g);

	protected abstract void paintElementForeground(Graphics2D g);

}
