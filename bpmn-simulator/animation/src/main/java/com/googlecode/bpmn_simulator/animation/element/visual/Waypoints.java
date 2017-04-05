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

import java.util.ArrayList;
import java.util.Iterator;

public class Waypoints
		extends ArrayList<Waypoint> {

	private static final long serialVersionUID = 8433732582598038968L;

	public boolean isValid() {
		return size() >= 2;
	}

	public Waypoint first() {
		return get(0);
	}

	public Waypoint second() {
		return get(1);
	}

	public Waypoint nextToLast() {
		if (size() >= 2)  {
			return get(size() - 2);
		}
		return null;
	}

	public Waypoint last() {
		if (size() >= 1)  {
			return get(size() - 1);
		}
		return null;
	}

	public Bounds getBounds() {
		final Iterator<Waypoint> i = iterator();
		if (i.hasNext()) {
			Waypoint waypoint = i.next();
			int x = waypoint.getX();
			int y = waypoint.getY();
			int minX = x;
			int maxX = x;
			int minY = y;
			int maxY = y;
			while (i.hasNext()) {
				waypoint = i.next();
				x = waypoint.getX();
				y = waypoint.getY();
				if (x < minX) {
					minX = x;
				}
				if (x > maxX) {
					maxX = x;
				}
				if (y < minY) {
					minY = y;
				}
				if (y > maxY) {
					maxY = y;
				}
			}
			return new Bounds(minX, minY, maxX - minX, maxY - minY);
		}
		return null;
	}

	public double getLength() {
		double length = 0.;
		Waypoint last = null;
		for (final Waypoint current : this) {
			if (last != null) {
				length += last.distanceTo(current);
			}
			last = current;
		}
		return length;
	}

	public Point getWaypoint(final double length) {
		double currentLength = 0.;
		Waypoint last = null;
		for (final Waypoint current : this) {
			if (last != null) {
				final double distance = last.distanceTo(current);
				if ((currentLength + distance) >= length) {
					return GeometryUtils.polarToCartesian(
							last,
							currentLength - length,
							current.angleTo(last));
				}
				currentLength += distance;
			}
			last = current;
		}
		assert last != null;
		return last; ///XXX
	}

	public double getAngleAt(final double length) {
		double currentLength = 0.;
		Waypoint last = null;
		for (final Waypoint current : this) {
			if (last != null) {
				final double distance = last.distanceTo(current);
				if ((currentLength + distance) >= length) {
					return current.angleTo(last);
				}
				currentLength += distance;
			}
			last = current;
		}
		assert last != null;
		return 0.;
	}

	public Waypoints translate(final int x, final int y) {
		final Waypoints waypoints = new Waypoints();
		for (Waypoint waypoint : this) {
			waypoints.add(new Waypoint(waypoint.getX() + x, waypoint.getY() + y));
		}
		return waypoints;
	}

}
