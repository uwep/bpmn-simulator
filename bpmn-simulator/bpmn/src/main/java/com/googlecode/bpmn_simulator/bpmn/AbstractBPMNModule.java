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
package com.googlecode.bpmn_simulator.bpmn;

import java.util.Arrays;
import java.util.Collection;

import com.googlecode.bpmn_simulator.animation.element.logical.LogicalElements;
import com.googlecode.bpmn_simulator.animation.module.AbstractModule;
import com.googlecode.bpmn_simulator.bpmn.model.choreography.Choreography;
import com.googlecode.bpmn_simulator.bpmn.model.choreography.activities.CallChoreography;
import com.googlecode.bpmn_simulator.bpmn.model.choreography.activities.ChoreographyTask;
import com.googlecode.bpmn_simulator.bpmn.model.choreography.activities.SubChoreography;
import com.googlecode.bpmn_simulator.bpmn.model.collaboration.Collaboration;
import com.googlecode.bpmn_simulator.bpmn.model.collaboration.MessageFlow;
import com.googlecode.bpmn_simulator.bpmn.model.collaboration.Participant;
import com.googlecode.bpmn_simulator.bpmn.model.collaboration.conversations.CallConversation;
import com.googlecode.bpmn_simulator.bpmn.model.collaboration.conversations.Conversation;
import com.googlecode.bpmn_simulator.bpmn.model.collaboration.conversations.ConversationLink;
import com.googlecode.bpmn_simulator.bpmn.model.collaboration.conversations.SubConversation;
import com.googlecode.bpmn_simulator.bpmn.model.core.common.Error;
import com.googlecode.bpmn_simulator.bpmn.model.core.common.Expression;
import com.googlecode.bpmn_simulator.bpmn.model.core.common.Message;
import com.googlecode.bpmn_simulator.bpmn.model.core.common.SequenceFlow;
import com.googlecode.bpmn_simulator.bpmn.model.core.common.artifacts.Association;
import com.googlecode.bpmn_simulator.bpmn.model.core.common.artifacts.Group;
import com.googlecode.bpmn_simulator.bpmn.model.core.common.artifacts.TextAnnotation;
import com.googlecode.bpmn_simulator.bpmn.model.core.common.events.BoundaryEvent;
import com.googlecode.bpmn_simulator.bpmn.model.core.common.events.CancelEventDefinition;
import com.googlecode.bpmn_simulator.bpmn.model.core.common.events.CompensateEventDefinition;
import com.googlecode.bpmn_simulator.bpmn.model.core.common.events.ConditionalEventDefinition;
import com.googlecode.bpmn_simulator.bpmn.model.core.common.events.EndEvent;
import com.googlecode.bpmn_simulator.bpmn.model.core.common.events.ErrorEventDefinition;
import com.googlecode.bpmn_simulator.bpmn.model.core.common.events.EscalationEventDefinition;
import com.googlecode.bpmn_simulator.bpmn.model.core.common.events.IntermediateCatchEvent;
import com.googlecode.bpmn_simulator.bpmn.model.core.common.events.IntermediateThrowEvent;
import com.googlecode.bpmn_simulator.bpmn.model.core.common.events.LinkEventDefinition;
import com.googlecode.bpmn_simulator.bpmn.model.core.common.events.MessageEventDefinition;
import com.googlecode.bpmn_simulator.bpmn.model.core.common.events.SignalEventDefinition;
import com.googlecode.bpmn_simulator.bpmn.model.core.common.events.StartEvent;
import com.googlecode.bpmn_simulator.bpmn.model.core.common.events.TerminateEventDefinition;
import com.googlecode.bpmn_simulator.bpmn.model.core.common.events.TimerEventDefinition;
import com.googlecode.bpmn_simulator.bpmn.model.core.common.gateways.ComplexGateway;
import com.googlecode.bpmn_simulator.bpmn.model.core.common.gateways.EventBasedGateway;
import com.googlecode.bpmn_simulator.bpmn.model.core.common.gateways.ExclusiveGateway;
import com.googlecode.bpmn_simulator.bpmn.model.core.common.gateways.InclusiveGateway;
import com.googlecode.bpmn_simulator.bpmn.model.core.common.gateways.ParallelGateway;
import com.googlecode.bpmn_simulator.bpmn.model.core.foundation.Documentation;
import com.googlecode.bpmn_simulator.bpmn.model.process.Lane;
import com.googlecode.bpmn_simulator.bpmn.model.process.LaneSet;
import com.googlecode.bpmn_simulator.bpmn.model.process.activities.AdHocSubProcess;
import com.googlecode.bpmn_simulator.bpmn.model.process.activities.CallActivity;
import com.googlecode.bpmn_simulator.bpmn.model.process.activities.MultiInstanceLoopCharacteristics;
import com.googlecode.bpmn_simulator.bpmn.model.process.activities.Process;
import com.googlecode.bpmn_simulator.bpmn.model.process.activities.StandardLoopCharacteristics;
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
import com.googlecode.bpmn_simulator.bpmn.model.process.data.DataObject;
import com.googlecode.bpmn_simulator.bpmn.model.process.data.DataObjectReference;
import com.googlecode.bpmn_simulator.bpmn.model.process.data.DataOutput;
import com.googlecode.bpmn_simulator.bpmn.model.process.data.DataStore;
import com.googlecode.bpmn_simulator.bpmn.model.process.data.DataStoreReference;
import com.googlecode.bpmn_simulator.bpmn.model.process.data.InputOutputSpecification;

public abstract class AbstractBPMNModule
		extends AbstractModule {

	private static final String FILE_DESCRIPTION = "BPMN 2.0 XML"; //$NON-NLS-1$
	private static final String[] FILE_EXTENSIONS = { "bpmn", "xml" }; //$NON-NLS-1$ //$NON-NLS-2$

	@Override
	public String getFileDescription() {
		return FILE_DESCRIPTION;
	}

	@Override
	public Collection<String> getFileExtensions() {
		return Arrays.asList(FILE_EXTENSIONS);
	}

	protected void registerLogicalElements() {
		// choreography
		LogicalElements.register(Choreography.class, Messages.getString("choreography")); //$NON-NLS-1$
		// choreography/activities
		LogicalElements.register(CallChoreography.class, Messages.getString("callChoreography")); //$NON-NLS-1$
		LogicalElements.register(ChoreographyTask.class, Messages.getString("choreographyTask")); //$NON-NLS-1$
		LogicalElements.register(SubChoreography.class, Messages.getString("subChoreography")); //$NON-NLS-1$
		// collaboration
		LogicalElements.register(Collaboration.class, Messages.getString("collaboration")); //$NON-NLS-1$
		LogicalElements.register(MessageFlow.class, Messages.getString("messageFlow")); //$NON-NLS-1$
		LogicalElements.register(Participant.class, Messages.getString("participant")); //$NON-NLS-1$
		// collaboration/conversations
		LogicalElements.register(CallConversation.class, Messages.getString("callConversation")); //$NON-NLS-1$
		LogicalElements.register(Conversation.class, Messages.getString("conversation")); //$NON-NLS-1$
		LogicalElements.register(ConversationLink.class, Messages.getString("conversationLink")); //$NON-NLS-1$
		LogicalElements.register(SubConversation.class, Messages.getString("subConversation")); //$NON-NLS-1$
		// core/common
		LogicalElements.register(Error.class, Messages.getString("error")); //$NON-NLS-1$
		LogicalElements.register(Expression.class, Messages.getString("expression")); //$NON-NLS-1$
		LogicalElements.register(Message.class, Messages.getString("message")); //$NON-NLS-1$
		LogicalElements.register(SequenceFlow.class, Messages.getString("sequenceFlow")); //$NON-NLS-1$
		// core/common/artifacts
		LogicalElements.register(Association.class, Messages.getString("association")); //$NON-NLS-1$
		LogicalElements.register(Group.class, Messages.getString("group")); //$NON-NLS-1$
		LogicalElements.register(TextAnnotation.class, Messages.getString("textAnnotation")); //$NON-NLS-1$
		// core/common/events
		LogicalElements.register(BoundaryEvent.class, Messages.getString("boundaryEvent")); //$NON-NLS-1$
		LogicalElements.register(CancelEventDefinition.class, Messages.getString("cancelEventDefinition")); //$NON-NLS-1$
		LogicalElements.register(CompensateEventDefinition.class, Messages.getString("compensateEventDefinition")); //$NON-NLS-1$
		LogicalElements.register(ConditionalEventDefinition.class, Messages.getString("conditionalEventDefinition")); //$NON-NLS-1$
		LogicalElements.register(EndEvent.class, Messages.getString("endEvent")); //$NON-NLS-1$
		LogicalElements.register(ErrorEventDefinition.class, Messages.getString("errorEventDefinition")); //$NON-NLS-1$
		LogicalElements.register(EscalationEventDefinition.class, Messages.getString("escalationEventDefinition")); //$NON-NLS-1$
		LogicalElements.register(IntermediateCatchEvent.class, Messages.getString("intermediateCatchEvent")); //$NON-NLS-1$
		LogicalElements.register(IntermediateThrowEvent.class, Messages.getString("intermediateThrowEvent")); //$NON-NLS-1$
		LogicalElements.register(LinkEventDefinition.class, Messages.getString("linkEventDefinition")); //$NON-NLS-1$
		LogicalElements.register(MessageEventDefinition.class, Messages.getString("messageEventDefinition")); //$NON-NLS-1$
		LogicalElements.register(SignalEventDefinition.class, Messages.getString("signalEventDefinition")); //$NON-NLS-1$
		LogicalElements.register(StartEvent.class, Messages.getString("startEvent")); //$NON-NLS-1$
		LogicalElements.register(TerminateEventDefinition.class, Messages.getString("terminateEventDefinition")); //$NON-NLS-1$
		LogicalElements.register(TimerEventDefinition.class, Messages.getString("timerEventDefinition")); //$NON-NLS-1$
		// core/common/gateways
		LogicalElements.register(ComplexGateway.class, Messages.getString("complexGateway")); //$NON-NLS-1$
		LogicalElements.register(EventBasedGateway.class, Messages.getString("eventBasedGateway")); //$NON-NLS-1$
		LogicalElements.register(ExclusiveGateway.class, Messages.getString("exclusiveGateway")); //$NON-NLS-1$
		LogicalElements.register(InclusiveGateway.class, Messages.getString("inclusiveGateway")); //$NON-NLS-1$
		LogicalElements.register(ParallelGateway.class, Messages.getString("parallelGateway")); //$NON-NLS-1$
		// core/foundation
		LogicalElements.register(Documentation.class, Messages.getString("documentation")); //$NON-NLS-1$
		// process
		LogicalElements.register(Lane.class, Messages.getString("lane")); //$NON-NLS-1$
		LogicalElements.register(LaneSet.class, Messages.getString("laneSet")); //$NON-NLS-1$
		// process/activities
		LogicalElements.register(AdHocSubProcess.class, Messages.getString("adHocSubProcess")); //$NON-NLS-1$
		LogicalElements.register(CallActivity.class, Messages.getString("callActivity")); //$NON-NLS-1$
		LogicalElements.register(MultiInstanceLoopCharacteristics.class, Messages.getString("multiInstanceLoopCharacteristics")); //$NON-NLS-1$
		LogicalElements.register(Process.class, Messages.getString("process")); //$NON-NLS-1$
		LogicalElements.register(StandardLoopCharacteristics.class, Messages.getString("standardLoopCharacteristics")); //$NON-NLS-1$
		LogicalElements.register(SubProcess.class, Messages.getString("subProcess")); //$NON-NLS-1$
		LogicalElements.register(Transaction.class, Messages.getString("transaction")); //$NON-NLS-1$
		// process/activities/task
		LogicalElements.register(BusinessRuleTask.class, Messages.getString("bussinesRuleTask")); //$NON-NLS-1$
		LogicalElements.register(ManualTask.class, Messages.getString("manualTask")); //$NON-NLS-1$
		LogicalElements.register(ReceiveTask.class, Messages.getString("receiveTask")); //$NON-NLS-1$
		LogicalElements.register(ScriptTask.class, Messages.getString("scriptTask")); //$NON-NLS-1$
		LogicalElements.register(SendTask.class, Messages.getString("sendTask")); //$NON-NLS-1$
		LogicalElements.register(ServiceTask.class, Messages.getString("serviceTask")); //$NON-NLS-1$
		LogicalElements.register(Task.class, Messages.getString("task")); //$NON-NLS-1$
		LogicalElements.register(UserTask.class, Messages.getString("userTask")); //$NON-NLS-1$
		// process/data
		LogicalElements.register(DataAssociation.class, Messages.getString("dataAssociation")); //$NON-NLS-1$
		LogicalElements.register(DataInput.class, Messages.getString("dataInput")); //$NON-NLS-1$
		LogicalElements.register(DataObject.class, Messages.getString("dataObject")); //$NON-NLS-1$
		LogicalElements.register(DataObjectReference.class, Messages.getString("dataObjectReference")); //$NON-NLS-1$
		LogicalElements.register(DataOutput.class, Messages.getString("dataOutput")); //$NON-NLS-1$
		LogicalElements.register(DataStore.class, Messages.getString("dataStore")); //$NON-NLS-1$
		LogicalElements.register(DataStoreReference.class, Messages.getString("dataStoreReference")); //$NON-NLS-1$
		LogicalElements.register(InputOutputSpecification.class, Messages.getString("inputOutputSpecification")); //$NON-NLS-1$
	}

}
