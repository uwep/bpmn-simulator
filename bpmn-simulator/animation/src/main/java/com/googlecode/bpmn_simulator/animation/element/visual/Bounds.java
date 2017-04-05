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

public class Bounds
		extends Dimension {

	private final int x;
	private final int y;

	public Bounds(final int x, final int y,
			final int width, final int height) {
		super(width, height);
		assert x >= 0;
		this.x = x;
		assert y >= 0;
		this.y = y;
	}

	public Bounds(final int x, final int y,
			final Dimension dimension) {
		this(x, y, dimension.getWidth(), dimension.getHeight());
	}

	public static Bounds fromCenter(final Point center, final int size_w, final int size_h) {
		return fromCenter(center.getX(), center.getY(), size_w, size_h);
	}

	public static Bounds fromCenter(final Point center, final int size) {
		return fromCenter(center.getX(), center.getY(), size);
	}

	public static Bounds fromCenter(final int x, final int y, final int size_w, final int size_h) {
		return new Bounds(x - size_w, y - size_h , size_w * 2, size_h * 2);
	}

	public static Bounds fromCenter(final int x, final int y, final int size) {
		return fromCenter(x, y, size, size);
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public int getMinX() {
		return getX();
	}

	public float getCenterX() {
		return getX() + (getWidth() / 2.f);
	}

	public int getMaxX() {
		return getX() + getWidth();
	}

	public int getMinY() {
		return getY();
	}

	public float getCenterY() {
		return getY() + (getHeight() / 2.f);
	}

	public int getMaxY() {
		return getY() + getHeight();
	}

	public Point getCenter() {
		return new Point((int) getCenterX(), (int) getCenterY());
	}

	public float getX(final HorizontalPosition position) {
		switch (position) {
			case LEFT:
				return getMinX();
			case CENTER:
				return getCenterX();
			case RIGHT:
				return getMaxX();
			default:
				throw new IllegalArgumentException();
		}
	}

	public float getY(final VerticalPosition position) {
		switch (position) {
			case TOP:
				return getMinY();
			case CENTER:
				return getCenterY();
			case BOTTOM:
				return getMaxY();
			default:
				throw new IllegalArgumentException();
		}
	}

	public Point getPoint(final HorizontalPosition hpos, final VerticalPosition vpos) {
		return new Point((int) getX(hpos), (int) getY(vpos));
	}

	public Bounds translate(final int x, final int y) {
		return new Bounds(getX() + x, getY() + y, getWidth(), getHeight());
	}

	public Bounds enlarge(final int lr, final int tb) {
		return new Bounds(getX() - lr, getY() - tb,
				getWidth() + (2 * lr), getHeight() + (2 * tb));
	}

	public Bounds enlarge(final int n) {
		return enlarge(n, n);
	}

	public Bounds scaleSize(final float factor) {
		final int lr = Math.round(((getWidth() * factor) - getWidth()) / 2.f);
		final int tb = Math.round(((getHeight() * factor) - getHeight()) / 2.f);
		return enlarge(lr, tb);
	}

	public Bounds shrinkLeft(final int n) {
		return new Bounds(getX() + n, getY(), getWidth() - n, getHeight());
	}

	public Bounds shrink(final int lr, final int tb) {
		return enlarge(-lr, -tb);
	}

	public Bounds shrink(final int n) {
		return shrink(n, n);
	}

	public Bounds top(final int size) {
		return new Bounds(getMinX(), getMinY(), getWidth(), size);
	}

	public Bounds bottom(final int size) {
		return new Bounds(getMinX(), getMaxY() - size, getWidth(), size);
	}

	public Bounds left(final int size) {
		return new Bounds(getMinX(), getMinY(), size, getHeight());
	}

	public Bounds centerBottom(final int width, final int height) {
		return new Bounds(
				(int) Math.round(getCenterX() - width / 2.),
				getMaxY() - height,
				width, height);
	}

	public Bounds center(final int width, final int height) {
		return new Bounds(
				getMinX() + (int) Math.round((getWidth() - width) / 2.),
				getMinY() + (int) Math.round((getHeight() - height) / 2.),
				width, height);
	}

}
