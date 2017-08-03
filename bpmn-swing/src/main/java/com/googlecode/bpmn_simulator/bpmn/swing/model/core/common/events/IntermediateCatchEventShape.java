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

import com.googlecode.bpmn_simulator.animation.element.visual.HorizontalPosition;
import com.googlecode.bpmn_simulator.animation.element.visual.Point;
import com.googlecode.bpmn_simulator.animation.element.visual.VerticalPosition;
import com.googlecode.bpmn_simulator.bpmn.model.core.common.events.IntermediateCatchEvent;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JCheckBox;

@SuppressWarnings("serial")
public class IntermediateCatchEventShape
		extends AbstractIntermediateEventShape<IntermediateCatchEvent> {

	private JCheckBox checkBox;

	public IntermediateCatchEventShape(final IntermediateCatchEvent element) {
		super(element);
	}

	/* (non-Javadoc)
	 * @see javax.swing.JComponent#addNotify()
	 */
	@Override
	public void addNotify() {
		// TODO Auto-generated method stub
		super.addNotify();
		if (checkBox == null) {
			createCheckbox();
		}
		updateCheckboxPosition();
	}

	private void createCheckbox() {
		checkBox = new JCheckBox();
		checkBox.setSelected(false);
		checkBox.setOpaque(false);
		checkBox.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent event) {				
				getLogicalElement().setCatched(checkBox.isSelected());
			}
		});
		checkBox.setSelected(getLogicalElement().isCatched());
		getParent().add(checkBox, 0);
	}

	private void updateCheckboxPosition() {
		if (checkBox != null) {
			final Point point = getInnerBounds().getPoint(HorizontalPosition.CENTER, VerticalPosition.BOTTOM);
			if (point != null) {
				final Dimension size = checkBox.getPreferredSize();
				checkBox.setBounds(point.getX() - (size.width / 2), point.getY() - (size.height / 2),
						size.width, size.height);
			}
		}
	}

}
