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
package com.googlecode.bpmn_simulator.animation.element.visual;

public class Dimension {

	private int width;
	private int height;

	public Dimension() {
		this(0, 0);
	}

	public Dimension(final int width, final int height) {
		super();
		assert width >= 0;
		this.width = width;
		assert height >= 0;
		this.height = height;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public int getSize(final Orientation orientation) {
		if (orientation == Orientation.HORIZONTAL) {
			return getWidth();
		} else {
			return getHeight();
		}
	}

	public Dimension grow(final Orientation orientation, final Dimension dimension) {
		return grow(orientation, dimension.getSize(orientation));
	}

	public Dimension grow(final Orientation orientation, final int size) {
		if (orientation == Orientation.HORIZONTAL) {
			return new Dimension(getWidth() + size, getHeight());
		} else {
			return new Dimension(getWidth(), getHeight() + size);
		}
	}

	public Bounds relativeTo(final Point point,
			final HorizontalPosition hpos, final VerticalPosition vpos) {
		int x, y;
		switch (hpos) {
			case LEFT:
				x = point.getX() - getWidth();
				break;
			case CENTER:
				x = (int) (point.getX() - (getWidth() / 2.));
				break;
			case RIGHT:
				x = point.getX();
				break;
			default:
				throw new IllegalArgumentException();
		}
		switch (vpos) {
			case TOP:
				y = point.getY() - getHeight();
				break;
			case CENTER:
				y = (int) (point.getY() - (getHeight() / 2.));
				break;
			case BOTTOM:
				y = point.getY();
				break;
			default:
				throw new IllegalArgumentException();
		}
		return new Bounds(x, y, this);
	}

}
