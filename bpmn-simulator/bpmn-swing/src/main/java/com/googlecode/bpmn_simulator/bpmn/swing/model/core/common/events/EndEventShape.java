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

import java.awt.BasicStroke;
import java.awt.Image;
import java.awt.Stroke;

import com.googlecode.bpmn_simulator.bpmn.model.core.common.events.EndEvent;

@SuppressWarnings("serial")
public class EndEventShape
		extends AbstractEventShape<EndEvent> {

	private static final Stroke STROKE = new BasicStroke(4);

	public EndEventShape(final EndEvent element) {
		super(element);
	}

	@Override
	protected Stroke getStroke() {
		return STROKE;
	}

	@Override
	protected Image getIconImage() {
		return getIconImage(true);
	}

}
