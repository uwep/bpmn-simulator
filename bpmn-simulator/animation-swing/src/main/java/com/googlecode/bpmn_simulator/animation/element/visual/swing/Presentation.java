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

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Polygon;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.geom.AffineTransform;
import java.awt.geom.Path2D;
import java.util.Iterator;

import com.googlecode.bpmn_simulator.animation.element.visual.Bounds;
import com.googlecode.bpmn_simulator.animation.element.visual.GeometryUtils;
import com.googlecode.bpmn_simulator.animation.element.visual.Point;
import com.googlecode.bpmn_simulator.animation.element.visual.Waypoint;
import com.googlecode.bpmn_simulator.animation.element.visual.Waypoints;
import com.googlecode.bpmn_simulator.animation.token.Token;
import com.googlecode.bpmn_simulator.animation.token.Tokens;

public class Presentation {

	public static final int PENTAGON = 5;
	public static final int HEXAGON = 6;

	private static final RenderingHints QUALITY = new RenderingHints(null);
	private static final RenderingHints SPEED = new RenderingHints(null);

	private static final double ARROW_ANGLE = 10.;
	private static final int ARROW_LENGTH = 10;

	private static final Stroke TOKEN_STROKE = new BasicStroke(1.f);

	static {
		QUALITY.put(RenderingHints.KEY_RENDERING,
				RenderingHints.VALUE_RENDER_QUALITY);
		QUALITY.put(RenderingHints.KEY_COLOR_RENDERING,
				RenderingHints.VALUE_COLOR_RENDER_QUALITY);
		QUALITY.put(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
		QUALITY.put(RenderingHints.KEY_TEXT_ANTIALIASING,
				RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_HRGB);
		QUALITY.put(RenderingHints.KEY_ALPHA_INTERPOLATION,
				RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
		SPEED.put(RenderingHints.KEY_RENDERING,
				RenderingHints.VALUE_RENDER_SPEED);
		SPEED.put(RenderingHints.KEY_COLOR_RENDERING,
				RenderingHints.VALUE_COLOR_RENDER_SPEED);
		SPEED.put(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_OFF);
		SPEED.put(RenderingHints.KEY_TEXT_ANTIALIASING,
				RenderingHints.VALUE_TEXT_ANTIALIAS_OFF);
		SPEED.put(RenderingHints.KEY_ALPHA_INTERPOLATION,
				RenderingHints.VALUE_ALPHA_INTERPOLATION_SPEED);
	}

	private static RenderingHints renderingHints = QUALITY;

	private AffineTransform lastAffineTransform;

	public synchronized void init(final Graphics2D g) {
		g.setRenderingHints(renderingHints);
	}

	public void drawImage(final Graphics g, final Image image, final Bounds bounds) {
		g.drawImage(image, bounds.getX(), bounds.getY(), bounds.getWidth(), bounds.getHeight(), null);
	}

	public void drawLine(final Graphics g, final Point from, final Point to) {
		g.drawLine(from.getX(), from.getY(), to.getX(), to.getY());
	}

	public void drawLine(final Graphics g, final Waypoints points) {
		Waypoint from;
		Waypoint to = null;
		final Iterator<Waypoint> i = points.iterator();
		while (i.hasNext()) {
			from = to;
			to = i.next();
			if (from != null) {
				drawLine(g, from, to);
			}
		}
	}

	public void drawArrowhead(final Graphics2D g, final Waypoint arrowFrom, final Waypoint arrowTo) {
		g.draw(createArrowPath(arrowFrom, arrowTo, ARROW_ANGLE, ARROW_LENGTH));
	}

	public void fillArrowhead(final Graphics2D g, final Waypoint arrowFrom, final Waypoint arrowTo) {
		g.fill(createArrowPath(arrowFrom, arrowTo, ARROW_ANGLE, ARROW_LENGTH));
	}

	public void drawRect(final Graphics g, final Bounds bounds) {
		g.drawRect(bounds.getX(), bounds.getY(), bounds.getWidth(), bounds.getHeight());
	}

	public void fillRect(final Graphics g, final Bounds bounds) {
		g.fillRect(bounds.getX(), bounds.getY(), bounds.getWidth(), bounds.getHeight());
	}

	public void drawRoundRect(final Graphics g, final Bounds bounds, final int arcSize) {
		g.drawRoundRect(bounds.getX(), bounds.getY(), bounds.getWidth(), bounds.getHeight(), arcSize, arcSize);
	}

	public void fillRoundRect(final Graphics g, final Bounds bounds, final int arcSize) {
		g.fillRoundRect(bounds.getX(), bounds.getY(), bounds.getWidth(), bounds.getHeight(), arcSize, arcSize);
	}

	public void drawOval(final Graphics g, final Bounds bounds) {
		g.drawOval(bounds.getX(), bounds.getY(), bounds.getWidth(), bounds.getHeight());
	}

	public void fillOval(final Graphics g, final Bounds bounds) {
		g.fillOval(bounds.getX(), bounds.getY(), bounds.getWidth(), bounds.getHeight());
	}

	public void drawArc(final Graphics g, final Bounds bounds, final int startAngle, final int arcAngle) {
		g.drawArc(bounds.getX(), bounds.getY(), bounds.getWidth(), bounds.getHeight(), startAngle, arcAngle);
	}

	public void fillDiamond(final Graphics2D g, final Bounds bounds) {
		g.fill(createDiamond(bounds));
	}

	public void drawDiamond(final Graphics2D g, final Bounds bounds) {
		g.draw(createDiamond(bounds));
	}

	public void fillConvexPolygon(final Graphics2D g, final Bounds bounds, final int edgeCount) {
		g.fill(createConvexPolygon(bounds, edgeCount));
	}

	public void drawConvexPolygon(final Graphics2D g, final Bounds bounds, final int edgeCount) {
		g.draw(createConvexPolygon(bounds, edgeCount));
	}

	public void fillDocument(final Graphics2D g, final Bounds bounds, final int n) {
		g.fill(createDocument(bounds, n));
	}

	public void drawDocument(final Graphics2D g, final Bounds bounds, final int n) {
		g.draw(createDocument(bounds, n));
	}

	public void drawToken(final Graphics2D g, final Token token, final int centerX, final int centerY) {
		final Bounds bounds = Bounds.fromCenter(centerX, centerY, AbstractVisualElement.MARGIN);
		final Shape shape = createStar(bounds, PENTAGON);
		g.setPaint(Colors.forToken(token));
		g.fill(shape);
		g.setStroke(TOKEN_STROKE);
		g.setPaint(Color.BLACK);
		g.draw(shape);
	}

	public void drawTokens(final Graphics2D g, final Tokens tokens,
			final int x, final int y) {
		int x2 = x;
		final Iterator<Token> iterator = tokens.iterator();
		while (iterator.hasNext()) {
			drawToken(g, iterator.next(), x2, y);
			x2 += 6;
		}
	}

	public static Shape createDocument(final Bounds bounds, final int n) {
		final Polygon polygon = new Polygon();
		polygon.addPoint(bounds.getMaxX() - n, bounds.getMinY());
		polygon.addPoint(bounds.getMinX(), bounds.getMinY());
		polygon.addPoint(bounds.getMinX(), bounds.getMaxY());
		polygon.addPoint(bounds.getMaxX(), bounds.getMaxY());
		polygon.addPoint(bounds.getMaxX(), bounds.getMinY() +  n);
		return polygon;
	}

	private static double getRadius(final Bounds bounds) {
		return Math.min(bounds.getWidth(), bounds.getHeight()) / 2.;
	}

	public static Shape createConvexPolygon(final Bounds bounds, final int edgeCount) {
		final Polygon polygon = new Polygon();
		final Point center = bounds.getCenter();
		final double r = getRadius(bounds);
		final double radPerCorner = GeometryUtils.RAD_FULL / edgeCount;
		Point point = null;
		for (int i = 0; i < edgeCount; ++i) {
			point = GeometryUtils.polarToCartesian(
					center, r,
					radPerCorner * i - radPerCorner / 2.);
			polygon.addPoint(point.getX(), point.getY());
		}
		return polygon;
	}

	public static Shape createStar(final Bounds bounds, final int cornerCount) {
		final Polygon polygon = new Polygon();
		final Point center = bounds.getCenter();
		final double r = getRadius(bounds);
		final double radPerCorner = GeometryUtils.RAD_FULL / cornerCount;
		Point point = null;
		for (int i = 0; i < cornerCount; ++i) {
			final double rad = radPerCorner * i;
			point = GeometryUtils.polarToCartesian(center, r, rad - radPerCorner / 2.);
			polygon.addPoint(point.getX(), point.getY());
			point = GeometryUtils.polarToCartesian(center, r * 0.5, rad);
			polygon.addPoint(point.getX(), point.getY());
		}
		return polygon;
	}

	public static Shape createDiamond(final Bounds bounds) {
		final Polygon polygon = new Polygon();
		polygon.addPoint(bounds.getMinX(), (int) bounds.getCenterY());
		polygon.addPoint((int) bounds.getCenterX(), bounds.getMinY());
		polygon.addPoint(bounds.getMaxX(), (int) bounds.getCenterY());
		polygon.addPoint((int) bounds.getCenterX(), bounds.getMaxY());
		return polygon;
	}

	public static Shape createArrowPath(
			final Waypoint from, final Waypoint to,
			final double d, final double length) {
		final Path2D.Float path = new Path2D.Float();
		final double angle = from.angleTo(to);
		final Point point1 = GeometryUtils.polarToCartesian(to, length, angle - d);
		path.moveTo(point1.getX(), point1.getY());
		path.lineTo(to.getX(), to.getY());
		final Point point2 = GeometryUtils.polarToCartesian(to, length, angle + d);
		path.lineTo(point2.getX(), point2.getY());
		return path;
	}

	public void rotate(final Graphics2D g, final Point point, final double angle) {
		lastAffineTransform = g.getTransform();
		g.rotate(angle + (GeometryUtils.RAD_FULL / 4.), point.getX(), point.getY());
	}

	public void restore(final Graphics2D g) {
		g.setTransform(lastAffineTransform);
	}

}
