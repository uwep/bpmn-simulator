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
package com.googlecode.bpmn_simulator.bpmn.swing.model.collaboration;

import java.awt.Graphics2D;
import com.googlecode.bpmn_simulator.bpmn.di.ParticipantBandKind;
import com.googlecode.bpmn_simulator.bpmn.model.collaboration.Participant;
import com.googlecode.bpmn_simulator.bpmn.swing.di.AbstractBPMNShape;

@SuppressWarnings("serial")
public class ParticipantShape
		extends AbstractBPMNShape<Participant> {

	public ParticipantShape(final Participant element) {
		super(element);
	}

	@Override
	protected void paintElementBackground(final Graphics2D g) {
		super.paintElementBackground(g);
		final ParticipantBandKind bandKind = getParticipantBandKind();
		if (bandKind != null) {
			switch (bandKind) {
				case TOP_NON_INITIATING:
					g.setPaint(getBackgroundColor().darker());
				case TOP_INITIATING:
					//TODO
					getPresentation().fillRect(g, getInnerBoundsRelative());
					break;
				case BOTTOM_NON_INITIATING:
					g.setPaint(getBackgroundColor().darker());
				case BOTTOM_INITIATING:
					//TODO
					getPresentation().fillRect(g, getInnerBoundsRelative());
					break;
				default:
					getPresentation().fillRect(g, getInnerBoundsRelative());
			}
		} else {
			getPresentation().fillRect(g, getInnerBoundsRelative());
		}
	}

	@Override
	protected void paintElementForeground(final Graphics2D g) {
		super.paintElementForeground(g);
		final ParticipantBandKind bandKind = getParticipantBandKind();
		if (bandKind != null) {
			switch (bandKind) {
				case TOP_NON_INITIATING:
				case TOP_INITIATING:
					//TODO
					getPresentation().drawRect(g, getInnerBoundsRelative());
					break;
				case BOTTOM_NON_INITIATING:
				case BOTTOM_INITIATING:
					//TODO
					getPresentation().drawRect(g, getInnerBoundsRelative());
					break;
				default:
					getPresentation().drawRect(g, getInnerBoundsRelative());
			}
		} else {
			getPresentation().drawRect(g, getInnerBoundsRelative());
		}
	}

}
