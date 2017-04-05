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
package com.googlecode.bpmn_simulator.bpmn.swing;

import com.googlecode.bpmn_simulator.animation.element.visual.VisualElements;
import com.googlecode.bpmn_simulator.animation.input.Definition;
import com.googlecode.bpmn_simulator.animation.module.ModuleRegistry;
import com.googlecode.bpmn_simulator.bpmn.AbstractBPMNModule;
import com.googlecode.bpmn_simulator.bpmn.model.choreography.activities.CallChoreography;
import com.googlecode.bpmn_simulator.bpmn.model.choreography.activities.ChoreographyTask;
import com.googlecode.bpmn_simulator.bpmn.model.choreography.activities.SubChoreography;
import com.googlecode.bpmn_simulator.bpmn.model.collaboration.MessageFlow;
import com.googlecode.bpmn_simulator.bpmn.model.collaboration.Participant;
import com.googlecode.bpmn_simulator.bpmn.model.collaboration.conversations.CallConversation;
import com.googlecode.bpmn_simulator.bpmn.model.collaboration.conversations.Conversation;
import com.googlecode.bpmn_simulator.bpmn.model.collaboration.conversations.ConversationLink;
import com.googlecode.bpmn_simulator.bpmn.model.collaboration.conversations.SubConversation;
import com.googlecode.bpmn_simulator.bpmn.model.core.common.SequenceFlow;
import com.googlecode.bpmn_simulator.bpmn.model.core.common.artifacts.Association;
import com.googlecode.bpmn_simulator.bpmn.model.core.common.artifacts.Group;
import com.googlecode.bpmn_simulator.bpmn.model.core.common.artifacts.TextAnnotation;
import com.googlecode.bpmn_simulator.bpmn.model.core.common.events.BoundaryEvent;
import com.googlecode.bpmn_simulator.bpmn.model.core.common.events.EndEvent;
import com.googlecode.bpmn_simulator.bpmn.model.core.common.events.IntermediateCatchEvent;
import com.googlecode.bpmn_simulator.bpmn.model.core.common.events.IntermediateThrowEvent;
import com.googlecode.bpmn_simulator.bpmn.model.core.common.events.StartEvent;
import com.googlecode.bpmn_simulator.bpmn.model.core.common.gateways.ComplexGateway;
import com.googlecode.bpmn_simulator.bpmn.model.core.common.gateways.EventBasedGateway;
import com.googlecode.bpmn_simulator.bpmn.model.core.common.gateways.ExclusiveGateway;
import com.googlecode.bpmn_simulator.bpmn.model.core.common.gateways.InclusiveGateway;
import com.googlecode.bpmn_simulator.bpmn.model.core.common.gateways.ParallelGateway;
import com.googlecode.bpmn_simulator.bpmn.model.process.Lane;
import com.googlecode.bpmn_simulator.bpmn.model.process.activities.AdHocSubProcess;
import com.googlecode.bpmn_simulator.bpmn.model.process.activities.CallActivity;
import com.googlecode.bpmn_simulator.bpmn.model.process.activities.SubProcess;
import com.googlecode.bpmn_simulator.bpmn.model.process.activities.Transaction;
import com.googlecode.bpmn_simulator.bpmn.model.process.activities.tasks.BusinessRuleTask;
import com.googlecode.bpmn_simulator.bpmn.model.process.activities.tasks.ManualTask;
import com.googlecode.bpmn_simulator.bpmn.model.process.activities.tasks.ReceiveTask;
import com.googlecode.bpmn_simulator.bpmn.model.process.activities.tasks.ScriptTask;
import com.googlecode.bpmn_simulator.bpmn.model.process.activities.tasks.SendTask;
import com.googlecode.bpmn_simulator.bpmn.model.process.activities.tasks.ServiceTask;
import com.googlecode.bpmn_simulator.bpmn.model.process.activities.tasks.Task;
import com.googlecode.bpmn_simulator.bpmn.model.process.activities.tasks.UserTask;
import com.googlecode.bpmn_simulator.bpmn.model.process.data.DataAssociation;
import com.googlecode.bpmn_simulator.bpmn.model.process.data.DataInput;
import com.googlecode.bpmn_simulator.bpmn.model.process.data.DataObjectReference;
import com.googlecode.bpmn_simulator.bpmn.model.process.data.DataOutput;
import com.googlecode.bpmn_simulator.bpmn.model.process.data.DataStoreReference;
import com.googlecode.bpmn_simulator.bpmn.swing.di.SwingDIDefinition;
import com.googlecode.bpmn_simulator.bpmn.swing.model.choreography.activities.CallChoreographyShape;
import com.googlecode.bpmn_simulator.bpmn.swing.model.choreography.activities.ChoreographyTaskShape;
import com.googlecode.bpmn_simulator.bpmn.swing.model.choreography.activities.SubChoreographyShape;
import com.googlecode.bpmn_simulator.bpmn.swing.model.collaboration.MessageFlowEdge;
import com.googlecode.bpmn_simulator.bpmn.swing.model.collaboration.ParticipantShape;
import com.googlecode.bpmn_simulator.bpmn.swing.model.collaboration.conversations.CallConversationShape;
import com.googlecode.bpmn_simulator.bpmn.swing.model.collaboration.conversations.ConversationLinkEdge;
import com.googlecode.bpmn_simulator.bpmn.swing.model.collaboration.conversations.ConversationShape;
import com.googlecode.bpmn_simulator.bpmn.swing.model.collaboration.conversations.SubConversationShape;
import com.googlecode.bpmn_simulator.bpmn.swing.model.core.common.SequenceFlowEdge;
import com.googlecode.bpmn_simulator.bpmn.swing.model.core.common.artifacts.AssociationEdge;
import com.googlecode.bpmn_simulator.bpmn.swing.model.core.common.artifacts.GroupShape;
import com.googlecode.bpmn_simulator.bpmn.swing.model.core.common.artifacts.TextAnnotationShape;
import com.googlecode.bpmn_simulator.bpmn.swing.model.core.common.events.BoundaryEventShape;
import com.googlecode.bpmn_simulator.bpmn.swing.model.core.common.events.EndEventShape;
import com.googlecode.bpmn_simulator.bpmn.swing.model.core.common.events.IntermediateCatchEventShape;
import com.googlecode.bpmn_simulator.bpmn.swing.model.core.common.events.IntermediateThrowEventShape;
import com.googlecode.bpmn_simulator.bpmn.swing.model.core.common.events.StartEventShape;
import com.googlecode.bpmn_simulator.bpmn.swing.model.core.common.gateways.ComplexGatewayShape;
import com.googlecode.bpmn_simulator.bpmn.swing.model.core.common.gateways.EventBasedGatewayShape;
import com.googlecode.bpmn_simulator.bpmn.swing.model.core.common.gateways.ExclusiveGatewayShape;
import com.googlecode.bpmn_simulator.bpmn.swing.model.core.common.gateways.InclusiveGatewayShape;
import com.googlecode.bpmn_simulator.bpmn.swing.model.core.common.gateways.ParallelGatewayShape;
import com.googlecode.bpmn_simulator.bpmn.swing.model.process.LaneShape;
import com.googlecode.bpmn_simulator.bpmn.swing.model.process.activities.AdHocSubProcessShape;
import com.googlecode.bpmn_simulator.bpmn.swing.model.process.activities.CallActivityShape;
import com.googlecode.bpmn_simulator.bpmn.swing.model.process.activities.SubProcessShape;
import com.googlecode.bpmn_simulator.bpmn.swing.model.process.activities.TransactionShape;
import com.googlecode.bpmn_simulator.bpmn.swing.model.process.activities.task.BusinessRuleTaskShape;
import com.googlecode.bpmn_simulator.bpmn.swing.model.process.activities.task.ManualTaskShape;
import com.googlecode.bpmn_simulator.bpmn.swing.model.process.activities.task.ReceiveTaskShape;
import com.googlecode.bpmn_simulator.bpmn.swing.model.process.activities.task.ScriptTaskShape;
import com.googlecode.bpmn_simulator.bpmn.swing.model.process.activities.task.SendTaskShape;
import com.googlecode.bpmn_simulator.bpmn.swing.model.process.activities.task.ServiceTaskShape;
import com.googlecode.bpmn_simulator.bpmn.swing.model.process.activities.task.TaskShape;
import com.googlecode.bpmn_simulator.bpmn.swing.model.process.activities.task.UserTaskShape;
import com.googlecode.bpmn_simulator.bpmn.swing.model.process.data.DataAssociationEdge;
import com.googlecode.bpmn_simulator.bpmn.swing.model.process.data.DataInputShape;
import com.googlecode.bpmn_simulator.bpmn.swing.model.process.data.DataObjectReferenceShape;
import com.googlecode.bpmn_simulator.bpmn.swing.model.process.data.DataOutputShape;
import com.googlecode.bpmn_simulator.bpmn.swing.model.process.data.DataStoreReferenceShape;

public class BPMNModule
		extends AbstractBPMNModule {

	private static final String NAME = "bpmn-swing"; //$NON-NLS-1$
	private static final String DESCRIPTION = "Business Process Model and Notation (BPMN) 2.0"; //$NON-NLS-1$

	static {
		ModuleRegistry.getDefault().registerModule(new BPMNModule());
	}

	private BPMNModule() {
		super();
	}

	@Override
	public void load() {
		registerLogicalElements();
		registerVisualElements();
		addElements();
	}

	private void registerVisualElements() {
		// choreography
		//VisualElements.register(ChoreographyPlane.class, VisualElements.Info.WHITE);
		// choreography/activities
		VisualElements.register(ChoreographyTaskShape.class, VisualElements.Info.YELLOW);
		VisualElements.register(SubChoreographyShape.class, VisualElements.Info.YELLOW);
		// collaboration
		VisualElements.register(ParticipantShape.class, VisualElements.Info.BLUE);
		// collaboration/conversations
		VisualElements.register(CallConversationShape.class, VisualElements.Info.YELLOW);
		VisualElements.register(ConversationShape.class, VisualElements.Info.YELLOW);
		VisualElements.register(SubConversationShape.class, VisualElements.Info.YELLOW);
		// core/common
		VisualElements.register(SequenceFlowEdge.class, VisualElements.Info.WHITE, VisualElements.Info.BLACK);
		// core/common/artifacts
		VisualElements.register(TextAnnotationShape.class, VisualElements.Info.BLUE);
		// core/common/events
		VisualElements.register(BoundaryEventShape.class, VisualElements.Info.ORANGE);
		VisualElements.register(EndEventShape.class, VisualElements.Info.RED);
		VisualElements.register(IntermediateCatchEventShape.class, VisualElements.Info.ORANGE);
		VisualElements.register(IntermediateThrowEventShape.class, VisualElements.Info.ORANGE);
		VisualElements.register(StartEventShape.class, VisualElements.Info.GREEN);
		// core/common/gateways
		VisualElements.register(ComplexGatewayShape.class, VisualElements.Info.ORANGE);
		VisualElements.register(EventBasedGatewayShape.class, VisualElements.Info.ORANGE);
		VisualElements.register(ExclusiveGatewayShape.class, VisualElements.Info.ORANGE);
		VisualElements.register(InclusiveGatewayShape.class, VisualElements.Info.ORANGE);
		VisualElements.register(ParallelGatewayShape.class, VisualElements.Info.ORANGE);
		// process
		VisualElements.register(LaneShape.class, VisualElements.Info.BLUE);
		// process/activities
		VisualElements.register(AdHocSubProcessShape.class, VisualElements.Info.YELLOW);
		VisualElements.register(CallActivityShape.class, VisualElements.Info.YELLOW);
		VisualElements.register(SubProcessShape.class, VisualElements.Info.YELLOW);
		// process/activities/task
		VisualElements.register(BusinessRuleTaskShape.class, VisualElements.Info.YELLOW);
		VisualElements.register(ManualTaskShape.class, VisualElements.Info.YELLOW);
		VisualElements.register(ReceiveTaskShape.class, VisualElements.Info.YELLOW);
		VisualElements.register(ScriptTaskShape.class, VisualElements.Info.YELLOW);
		VisualElements.register(SendTaskShape.class, VisualElements.Info.YELLOW);
		VisualElements.register(ServiceTaskShape.class, VisualElements.Info.YELLOW);
		VisualElements.register(TaskShape.class, VisualElements.Info.YELLOW);
		VisualElements.register(UserTaskShape.class, VisualElements.Info.YELLOW);
		// process/data
		VisualElements.register(DataInputShape.class, VisualElements.Info.GRAY);
		VisualElements.register(DataObjectReferenceShape.class, VisualElements.Info.GRAY);
		VisualElements.register(DataOutputShape.class, VisualElements.Info.GRAY);
		VisualElements.register(DataStoreReferenceShape.class, VisualElements.Info.GRAY);
	}

	private void addElements() {
		// choreography
		//addElement(Choreography.class, ChoreographyPlane.class);
		// choreography/activities
		addElement(CallChoreography.class, CallChoreographyShape.class);
		addElement(ChoreographyTask.class, ChoreographyTaskShape.class);
		addElement(SubChoreography.class, SubChoreographyShape.class);
		// collaboration
		//addElement(Collaboration.class, CollaborationPlane.class);
		addElement(MessageFlow.class, MessageFlowEdge.class);
		addElement(Participant.class, ParticipantShape.class);
		// collaboration/conversations
		addElement(CallConversation.class, CallConversationShape.class);
		addElement(ConversationLink.class, ConversationLinkEdge.class);
		addElement(Conversation.class, ConversationShape.class);
		addElement(SubConversation.class, SubConversationShape.class);
		// core/common
		addElement(SequenceFlow.class, SequenceFlowEdge.class);
		// core/common/artifacts
		addElement(Association.class, AssociationEdge.class);
		addElement(Group.class, GroupShape.class);
		addElement(TextAnnotation.class, TextAnnotationShape.class);
		// core/common/events
		addElement(BoundaryEvent.class, BoundaryEventShape.class);
		addElement(EndEvent.class, EndEventShape.class);
		addElement(IntermediateCatchEvent.class, IntermediateCatchEventShape.class);
		addElement(IntermediateThrowEvent.class, IntermediateThrowEventShape.class);
		addElement(StartEvent.class, StartEventShape.class);
		// core/common/gateways
		addElement(ComplexGateway.class, ComplexGatewayShape.class);
		addElement(EventBasedGateway.class, EventBasedGatewayShape.class);
		addElement(ExclusiveGateway.class, ExclusiveGatewayShape.class);
		addElement(InclusiveGateway.class, InclusiveGatewayShape.class);
		addElement(ParallelGateway.class, ParallelGatewayShape.class);
		// process
		addElement(Lane.class, LaneShape.class);
		// process/activities
		addElement(AdHocSubProcess.class, AdHocSubProcessShape.class);
		addElement(CallActivity.class, CallActivityShape.class);
		//addElement(Process.class, ProcessPlane.class);
		addElement(SubProcess.class, SubProcessShape.class/*, SubProcessPlane.class*/);
		addElement(Transaction.class, TransactionShape.class);
		// process/activities/task
		addElement(BusinessRuleTask.class, BusinessRuleTaskShape.class);
		addElement(ManualTask.class, ManualTaskShape.class);
		addElement(ReceiveTask.class, ReceiveTaskShape.class);
		addElement(ScriptTask.class, ScriptTaskShape.class);
		addElement(SendTask.class, SendTaskShape.class);
		addElement(ServiceTask.class, ServiceTaskShape.class);
		addElement(Task.class, TaskShape.class);
		addElement(UserTask.class, UserTaskShape.class);
		// process/data
		addElement(DataAssociation.class, DataAssociationEdge.class);
		addElement(DataInput.class, DataInputShape.class);
		addElement(DataObjectReference.class, DataObjectReferenceShape.class);
		addElement(DataOutput.class, DataOutputShape.class);
		addElement(DataStoreReference.class, DataStoreReferenceShape.class);
	}

	@Override
	public String getName() {
		return NAME;
	}

	@Override
	public String getDescription() {
		return DESCRIPTION;
	}

	@Override
	public Definition<?> createEmptyDefinition() {
		return new SwingDIDefinition();
	}

}
