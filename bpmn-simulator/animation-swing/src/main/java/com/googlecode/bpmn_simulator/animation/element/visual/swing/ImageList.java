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

import java.awt.Graphics2D;
import java.awt.Image;
import java.util.ArrayList;
import java.util.List;

import com.googlecode.bpmn_simulator.animation.element.visual.Bounds;
import com.googlecode.bpmn_simulator.animation.element.visual.Dimension;
import com.googlecode.bpmn_simulator.animation.element.visual.HorizontalPosition;
import com.googlecode.bpmn_simulator.animation.element.visual.Orientation;
import com.googlecode.bpmn_simulator.animation.element.visual.Point;
import com.googlecode.bpmn_simulator.animation.element.visual.VerticalPosition;

public class ImageList {

	private final List<Image> images = new ArrayList<>();

	private HorizontalPosition horizontalPosition = HorizontalPosition.LEFT;
	private VerticalPosition verticalPosition = VerticalPosition.BOTTOM;

	public void add(final Image image) {
		images.add(image);
	}

	private static Dimension getImageDimension(final Image image) {
		return new Dimension(image.getWidth(null), image.getHeight(null));
	}

	private Dimension getDimension(final Orientation orientation) {
		Dimension dimension = new Dimension();
		for (final Image image : images) {
			dimension = dimension.grow(orientation, getImageDimension(image));
		}
		return dimension;
	}

	public void drawHorizontal(final Graphics2D g,
			final Point point, final HorizontalPosition hpos, final VerticalPosition vpos) {
		final Bounds bounds = getDimension(Orientation.HORIZONTAL).relativeTo(point, hpos, vpos);
		int x = bounds.getMinX();
		for (final Image image : images) {
			final Dimension imageDimension = getImageDimension(image);
			final int y;
			switch (verticalPosition) {
				case TOP:
					y = bounds.getMinY();
					break;
				case CENTER:
					y = bounds.getMinY() + ((bounds.getHeight() - imageDimension.getHeight()) / 2);
					break;
				case BOTTOM:
					y = bounds.getMinY() + (bounds.getHeight() - imageDimension.getHeight());
					break;
				default:
					throw new IllegalArgumentException();
			}
			g.drawImage(image, x, y, null);
			x += imageDimension.getWidth();
		}
	}

}
