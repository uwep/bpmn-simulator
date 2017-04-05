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

import java.awt.Color;

import com.googlecode.bpmn_simulator.animation.element.logical.LogicalElement;
import com.googlecode.bpmn_simulator.animation.element.visual.Bounds;
import com.googlecode.bpmn_simulator.animation.element.visual.Label;
import com.googlecode.bpmn_simulator.animation.element.visual.VisualElement;
import com.googlecode.bpmn_simulator.animation.element.visual.VisualElements;

/*
 * +-------------------------------------+
 * |  outer bounds (=JComponent:bounds)  |
 * |  +-------------------------------+  |
 * |  |          inner bounds         |  |
 * |  +-------------------------------+  |
 * |                                     |
 * +-------------------------------------+
 */
@SuppressWarnings("serial")
abstract class AbstractVisualElement<E extends LogicalElement>
		extends AbstractVisual
		implements VisualElement {

	protected static final int MARGIN = 10;

	private final E logicalElement;

	private Label label;

	public AbstractVisualElement(final E element) {
		super();
		logicalElement = element;
	}

	public E getLogicalElement() {
		return logicalElement;
	}

	@Override
	public void setLabel(final Label label) {
		this.label = label;
	}

	public Label getLabel() {
		return label;
	}

	/*
	 * sets the inner bounds of this element absolute to its parent component
	 */
	protected void setInnerBounds(final Bounds bounds) {
		final Bounds outerBounds = bounds.enlarge(MARGIN);
		setBounds(outerBounds.getX(), outerBounds.getY(),
				outerBounds.getWidth(), outerBounds.getHeight());
	}

	/*
	 * gets the inner bounds of this element relative to it self
	 */
	protected Bounds getInnerBoundsRelative() {
		return new Bounds(MARGIN, MARGIN,
				getWidth() - (MARGIN * 2), getHeight() - (MARGIN * 2));
	}

	protected Bounds getInnerBounds() {
		return getInnerBoundsRelative().translate(getX(), getY());
	}

	protected Color getBackgroundColor() {
		final VisualElements.Info info = VisualElements.getInfo(getClass());
		if (info != null) {
			return new Color(info.getDefaultBackgroundColor());
		}
		return null;
	}

	protected Color getForegroundColor() {
		final VisualElements.Info info = VisualElements.getInfo(getClass());
		if (info != null) {
			return new Color(info.getDefaultForegroundColor());
		}
		return null;
	}

}
