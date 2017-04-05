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
package com.googlecode.bpmn_simulator.gui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;

import javax.swing.JInternalFrame;
import javax.swing.JScrollPane;

import com.googlecode.bpmn_simulator.animation.element.visual.Diagram;

@SuppressWarnings("serial")
public class DiagramFrame
		extends JInternalFrame {

	private static final Color BACKGROUND_COLOR = Color.WHITE;

	private final Component component;

	public DiagramFrame(final Diagram<?> diagram) {
		super(diagram.getName(), true, false, true);
		component = (diagram instanceof Component) ? (Component) diagram : null;

		final JScrollPane scrollPane = new JScrollPane(component);
		scrollPane.getViewport().setBackground(BACKGROUND_COLOR);
		setContentPane(scrollPane);
	}

	public void showFrame() {
		setLocation(0, 0);
		setVisible(true);
		pack();
	}

	public RenderedImage createImage() {
		final int width = component.getWidth();
		final int height = component.getHeight();
		final BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_3BYTE_BGR);
		final Graphics graphics = image.getGraphics();
		graphics.setColor(BACKGROUND_COLOR);
		graphics.fillRect(0, 0, width, height);
		component.paintAll(graphics);
		return image;
	}

}
