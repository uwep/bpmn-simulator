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
package com.googlecode.bpmn_simulator.bpmn.model.core.common;

import com.googlecode.bpmn_simulator.animation.ref.Reference;
import com.googlecode.bpmn_simulator.animation.ref.ReferenceUtils;
import com.googlecode.bpmn_simulator.animation.token.Token;

public final class SequenceFlow
		extends AbstractFlowElement {

	private final Reference<FlowNode> source;
	private final Reference<FlowNode> target;

	private Expression conditionExpression;

	public SequenceFlow(final String id, final String name,
			final Reference<FlowNode> source, final Reference<FlowNode> target) {
		super(id, name);
		this.source = source;
		this.target = target;
	}

	public Reference<FlowNode> getSource() {
		return source;
	}

	public Reference<FlowNode> getTarget() {
		return target;
	}

	public void setConditionExpression(final Expression expression) {
		this.conditionExpression = expression;
	}

	public Expression getConditionExpression() {
		return conditionExpression;
	}

	public boolean isConditional() {
		return getConditionExpression() != null;
	}

	public boolean isDefault() {
		final FlowNode sourceFlowNode = ReferenceUtils.element(getSource());
		if (sourceFlowNode instanceof DefaultSequenceFlowElement) {
			return equals(((DefaultSequenceFlowElement) sourceFlowNode).getDefaultSequenceFlow());
		}
		return false;
	}

	@Override
	protected void onTokenComplete(final Token token) {
		final FlowNode target = ReferenceUtils.element(getTarget());
		if (target != null) {
			token.copyTo(target);
		}
		token.remove();
	}

}
