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

public class Point {

	private final int x;
	private final int y;

	public Point(final int x, final int y) {
		super();
		this.x = x;
		this.y = y;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public Point translate(final int x, final int y) {
		return new Point(getX() + x, getY() + y);
	}

	public double distanceTo(final Point point) {
		final int h = getX() - point.getX();
		final int v = getY() - point.getY();
		return Math.sqrt((h * h) + (v * v));
	}

	public double angleTo(final Point point) {
		return Math.atan2(point.getX() - getX(), point.getY() - getY());
	}

}
