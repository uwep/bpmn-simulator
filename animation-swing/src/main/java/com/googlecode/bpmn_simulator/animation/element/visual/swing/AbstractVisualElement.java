/*
 * Copyright (C) 2014 Stefan Schweitzer
 *
 * This software was created by Stefan Schweitzer as a student's project at
 * Fachhochschule Kaiserslautern (University of Applied Sciences).
 * Supervisor: Professor Dr. Thomas Allweyer. For more information please see
 * http://www.fh-kl.de/~allweyer
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this Software except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.googlecode.bpmn_simulator.animation.element.visual.swing;

import javax.swing.JComponent;

import com.googlecode.bpmn_simulator.animation.element.logical.LogicalElement;
import com.googlecode.bpmn_simulator.animation.element.visual.Bounds;
import com.googlecode.bpmn_simulator.animation.element.visual.Label;
import com.googlecode.bpmn_simulator.animation.element.visual.VisualElement;

/**
 * +-------------------------------------+
 * |  outer bounds (=JComponent:bounds)  |
 * |  +-------------------------------+  |
 * |  |          inner bounds         |  |
 * |  +-------------------------------+  |
 * |                                     |
 * +-------------------------------------+
 * @param E Edge element
 */
@SuppressWarnings("serial")
abstract class AbstractVisualElement<E extends LogicalElement>
		extends JComponent
		implements VisualElement {

	private static final int MARGIN = 10;

	private final E logicalElement;

	private Label label;

	private static final Presentation presentation = new Presentation();

	public AbstractVisualElement(final E element) {
		super();
		logicalElement = element;
		logicalElement.addTokenFlowListener(this);
	}

	public E getLogicalElement() {
		return logicalElement;
	}

	public Presentation getPresentation() {
		return presentation;
	}

	@Override
	public void setLabel(final Label label) {
		this.label = label;
	}

	public Label getLabel() {
		return label;
	}

	protected void setInnerBounds(final Bounds bounds) {
		final Bounds outerBounds = bounds.enlarge(MARGIN);
		setBounds(outerBounds.getX(), outerBounds.getY(),
				outerBounds.getWidth(), outerBounds.getHeight());
	}

}
