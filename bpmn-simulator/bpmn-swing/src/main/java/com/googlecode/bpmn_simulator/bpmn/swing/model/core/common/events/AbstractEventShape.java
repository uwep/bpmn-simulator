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
package com.googlecode.bpmn_simulator.bpmn.swing.model.core.common.events;

import java.awt.Graphics2D;
import java.awt.Image;

import com.googlecode.bpmn_simulator.animation.element.visual.HorizontalPosition;
import com.googlecode.bpmn_simulator.animation.element.visual.Label;
import com.googlecode.bpmn_simulator.animation.element.visual.VerticalPosition;
import com.googlecode.bpmn_simulator.bpmn.model.core.common.events.ConditionalEventDefinition;
import com.googlecode.bpmn_simulator.bpmn.model.core.common.events.ErrorEventDefinition;
import com.googlecode.bpmn_simulator.bpmn.model.core.common.events.Event;
import com.googlecode.bpmn_simulator.bpmn.model.core.common.events.EventDefinition;
import com.googlecode.bpmn_simulator.bpmn.model.core.common.events.LinkEventDefinition;
import com.googlecode.bpmn_simulator.bpmn.model.core.common.events.MessageEventDefinition;
import com.googlecode.bpmn_simulator.bpmn.model.core.common.events.TerminateEventDefinition;
import com.googlecode.bpmn_simulator.bpmn.model.core.common.events.TimerEventDefinition;
import com.googlecode.bpmn_simulator.bpmn.swing.di.AbstractBPMNTokenShape;
import com.googlecode.bpmn_simulator.bpmn.swing.di.Appearance;

@SuppressWarnings("serial")
abstract class AbstractEventShape<E extends Event>
		extends AbstractBPMNTokenShape<E> {

	public AbstractEventShape(final E element) {
		super(element);
	}

	@Override
	public void alignLabel(final Label label) {
		label.setPosition(getInnerBounds().getPoint(HorizontalPosition.CENTER, VerticalPosition.BOTTOM),
				HorizontalPosition.CENTER, VerticalPosition.BOTTOM);
	}

	@Override
	protected void paintElementBackground(final Graphics2D g) {
		super.paintElementBackground(g);
		getPresentation().fillOval(g, getInnerBoundsRelative());
	}

	@Override
	protected void paintElementForeground(final Graphics2D g) {
		super.paintElementForeground(g);
		getPresentation().drawOval(g, getInnerBoundsRelative());
		paintElementIcon(g);
	}

	protected Image getIconImage(final boolean inverted) {
		final EventDefinition definition = getLogicalElement().getEventDefinition();
		if (definition instanceof ConditionalEventDefinition) {
			return Appearance.getDefault().getImage(inverted
					? null : Appearance.IMAGE_CONDITIONAL);
		} else if (definition instanceof ErrorEventDefinition) {
			return Appearance.getDefault().getImage(inverted
					? Appearance.IMAGE_ERROR_INVERSE : Appearance.IMAGE_ERROR);
		} else if (definition instanceof LinkEventDefinition) {
			return Appearance.getDefault().getImage(inverted
					? Appearance.IMAGE_LINK_INVERSE : Appearance.IMAGE_LINK);
		} else if (definition instanceof MessageEventDefinition) {
			return Appearance.getDefault().getImage(inverted
					? Appearance.IMAGE_MESSAGE_INVERSE : Appearance.IMAGE_MESSAGE);
		} else if (definition instanceof TerminateEventDefinition) {
			return Appearance.getDefault().getImage(inverted
					? Appearance.IMAGE_TERMINATE : null);
		} else if (definition instanceof TimerEventDefinition) {
			assert !inverted;
			return Appearance.getDefault().getImage(Appearance.IMAGE_TIMER);
		}
		return null;
	}

	protected Image getIconImage() {
		return getIconImage(false);
	}

	private void paintElementIcon(final Graphics2D g) {
		final Image image = getIconImage();
		if (image != null) {
			getPresentation().drawImage(g, image, getInnerBoundsRelative().shrink(6));
		}
	}

}
