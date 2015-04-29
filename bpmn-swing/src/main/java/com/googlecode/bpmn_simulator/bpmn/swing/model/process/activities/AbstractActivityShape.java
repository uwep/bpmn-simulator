/*
 * Copyright (C) 2015 Stefan Schweitzer
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
package com.googlecode.bpmn_simulator.bpmn.swing.model.process.activities;

import java.awt.Graphics2D;

import com.googlecode.bpmn_simulator.animation.element.visual.Bounds;
import com.googlecode.bpmn_simulator.animation.element.visual.HorizontalPosition;
import com.googlecode.bpmn_simulator.animation.element.visual.Label;
import com.googlecode.bpmn_simulator.animation.element.visual.VerticalPosition;
import com.googlecode.bpmn_simulator.animation.element.visual.swing.ImageList;
import com.googlecode.bpmn_simulator.bpmn.model.process.activities.Activity;
import com.googlecode.bpmn_simulator.bpmn.swing.di.AbstractBPMNTokenShape;
import com.googlecode.bpmn_simulator.bpmn.swing.di.Appearance;

@SuppressWarnings("serial")
public abstract class AbstractActivityShape<E extends Activity>
		extends AbstractBPMNTokenShape<E> {

	public AbstractActivityShape(final E element) {
		super(element);
	}

	protected int getPadding() {
		return Appearance.getDefault().getArcSize() / 4;
	}

	@Override
	public void alignLabel(final Label label) {
		final int padding = getPadding();
		label.setPosition(getInnerBounds().getPoint(HorizontalPosition.LEFT, VerticalPosition.TOP).translate(padding, padding),
				HorizontalPosition.RIGHT, VerticalPosition.BOTTOM);
	}

	@Override
	protected void paintElementBackground(final Graphics2D g) {
		super.paintElementBackground(g);
		getPresentation().fillRoundRect(g, getInnerBoundsRelative(), Appearance.getDefault().getArcSize());
	}

	protected ImageList getMarkers() {
		final ImageList markers = new ImageList();
		final Appearance appearance = Appearance.getDefault();
		if (!isExpanded()) {
			markers.add(appearance.getImage(Appearance.IMAGE_COLLAPSED));
		}
		return markers;
	}

	protected void paintElementMarkers(final Graphics2D g) {
		final Bounds bounds = getInnerBoundsRelative();
		final ImageList markers = getMarkers();
		markers.drawHorizontal(g,
				bounds.getPoint(HorizontalPosition.CENTER, VerticalPosition.BOTTOM),
				HorizontalPosition.CENTER, VerticalPosition.TOP);
	}

	@Override
	protected void paintElementForeground(final Graphics2D g) {
		super.paintElementForeground(g);
		final Bounds bounds = getInnerBoundsRelative();
		getPresentation().drawRoundRect(g, bounds, Appearance.getDefault().getArcSize());
		paintElementMarkers(g);
	}

	@Override
	protected void paintTokens(final Graphics2D g) {
		// TODO Auto-generated method stub
	}

}
