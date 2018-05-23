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
package com.googlecode.bpmn_simulator.bpmn.model;

import java.net.URI;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.activation.MimeType;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.googlecode.bpmn_simulator.animation.element.logical.LogicalElement;
import com.googlecode.bpmn_simulator.animation.element.logical.LogicalElements;
import com.googlecode.bpmn_simulator.animation.element.logical.LogicalFlowElement;
import com.googlecode.bpmn_simulator.animation.input.AbstractXmlDefinition;
import com.googlecode.bpmn_simulator.animation.ref.CastReference;
import com.googlecode.bpmn_simulator.animation.ref.NamedElements;
import com.googlecode.bpmn_simulator.animation.ref.NamedReference;
import com.googlecode.bpmn_simulator.animation.ref.Reference;
import com.googlecode.bpmn_simulator.animation.ref.ReferenceUtils;
import com.googlecode.bpmn_simulator.animation.ref.References;
import com.googlecode.bpmn_simulator.bpmn.Messages;
import com.googlecode.bpmn_simulator.bpmn.di.BPMNDiagram;
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
import com.googlecode.bpmn_simulator.bpmn.model.core.common.DefaultSequenceFlowElement;
import com.googlecode.bpmn_simulator.bpmn.model.core.common.Expression;
import com.googlecode.bpmn_simulator.bpmn.model.core.common.FlowElement;
import com.googlecode.bpmn_simulator.bpmn.model.core.common.FlowElementsContainer;
import com.googlecode.bpmn_simulator.bpmn.model.core.common.FlowNode;
import com.googlecode.bpmn_simulator.bpmn.model.core.common.Message;
import com.googlecode.bpmn_simulator.bpmn.model.core.common.SequenceFlow;
import com.googlecode.bpmn_simulator.bpmn.model.core.common.artifacts.Association;
import com.googlecode.bpmn_simulator.bpmn.model.core.common.artifacts.AssociationDirection;
import com.googlecode.bpmn_simulator.bpmn.model.core.common.artifacts.Group;
import com.googlecode.bpmn_simulator.bpmn.model.core.common.artifacts.TextAnnotation;
import com.googlecode.bpmn_simulator.bpmn.model.core.common.events.BoundaryEvent;
import com.googlecode.bpmn_simulator.bpmn.model.core.common.events.CancelEventDefinition;
import com.googlecode.bpmn_simulator.bpmn.model.core.common.events.CompensateEventDefinition;
import com.googlecode.bpmn_simulator.bpmn.model.core.common.events.ConditionalEventDefinition;
import com.googlecode.bpmn_simulator.bpmn.model.core.common.events.EndEvent;
import com.googlecode.bpmn_simulator.bpmn.model.core.common.events.ErrorEventDefinition;
import com.googlecode.bpmn_simulator.bpmn.model.core.common.events.EscalationEventDefinition;
import com.googlecode.bpmn_simulator.bpmn.model.core.common.events.Event;
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
import com.googlecode.bpmn_simulator.bpmn.model.core.common.gateways.Gateway;
import com.googlecode.bpmn_simulator.bpmn.model.core.common.gateways.InclusiveGateway;
import com.googlecode.bpmn_simulator.bpmn.model.core.common.gateways.ParallelGateway;
import com.googlecode.bpmn_simulator.bpmn.model.core.foundation.BaseElement;
import com.googlecode.bpmn_simulator.bpmn.model.core.foundation.Documentation;
import com.googlecode.bpmn_simulator.bpmn.model.core.infrastructure.Definitions;
import com.googlecode.bpmn_simulator.bpmn.model.process.Lane;
import com.googlecode.bpmn_simulator.bpmn.model.process.LaneSet;
import com.googlecode.bpmn_simulator.bpmn.model.process.activities.Activity;
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

public class BPMNDefinition<E extends BPMNDiagram<?>>
		extends AbstractXmlDefinition<E>
		implements Definitions<E> {

	public static final String ELEMENT_NAME = Messages.getString("definitions"); //$NON-NLS-1$

	private static final String SCHEMA_FILENAME =
			"com/googlecode/bpmn_simulator/bpmn/xsd/BPMN20.xsd"; //$NON-NLS-1$

	protected static final String BPMN =
			"http://www.omg.org/spec/BPMN/20100524/MODEL";  //$NON-NLS-1$

	private final NamedElements<BaseElement> elements
			= new NamedElements<>();

	private String id;
	private String name;

	private URI expressionLanguage;
	private URI typeLanguage;

	private String exporter;
	private String exporterVersion;

	private List<Documentation> documentations = new ArrayList<>();

	private Collection<Process> processes = new ArrayList<>();

	public BPMNDefinition() {
		super(SCHEMA_FILENAME);
	}

	@Override
	public Collection<LogicalElement> getElements() {
		return new ArrayList<LogicalElement>(elements.getElements());
	}

	@Override
	public Collection<LogicalFlowElement> getFlowElements() {
		final Collection<LogicalFlowElement> flowElements = new ArrayList<>();
		for (final LogicalElement element : getElements()) {
			if (element instanceof LogicalFlowElement) {
				flowElements.add((LogicalFlowElement) element);
			}
		}
		return flowElements;
	}

	@Override
	public Collection<LogicalFlowElement> getInstantiatingElements() {
		final Collection<LogicalFlowElement> instantiatingElements = new ArrayList<>();
		for (final Process process : processes) {
			for (final FlowElement flowElement : process.getFlowElements()) {
				if (flowElement instanceof StartEvent) {
					instantiatingElements.add(flowElement);
				}
			}
			if (instantiatingElements.isEmpty()) {
				for (final FlowElement flowElement : process.getFlowElements()) {
					if (flowElement instanceof IntermediateCatchEvent) {
						if (((IntermediateCatchEvent)flowElement).getEventDefinition() instanceof LinkEventDefinition) {			
							instantiatingElements.add(flowElement);
							((IntermediateCatchEvent)flowElement).setCatched(true);
						}
					}
				}
			}
		}
		return instantiatingElements;
	}

	@Override
	public Collection<Process> getProcesses() {
		return processes;
	}

	@Override
	public String getId() {
		return id;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public String getExporter() {
		return exporter;
	}

	@Override
	public String getExporterVersion() {
		return exporterVersion;
	}

	@Override
	public URI getExpressionLanguage() {
		return expressionLanguage;
	}

	@Override
	public URI getTypeLanguage() {
		return typeLanguage;
	}

	@Override
	public void addDocumentation(final Documentation documentation) {
		documentations.add(documentation);
	}

	@Override
	public Collection<Documentation> getDocumentation() {
		return documentations;
	}

	protected void registerElement(final BaseElement element) {
		if (element.getId() != null) {
			elements.setElement(element.getId(), element);
		}
	}

	protected BaseElement getElement(final String id) {
		if (id != null) {
			return elements.getElement(id);
		}
		return null;
	}

	protected void notifyElementLoading(final BaseElement element) {
		LOG.info(MessageFormat.format("Loading {0} ''{1}''",
				LogicalElements.getName(element), element.getId()));
	}

	protected String getIdAttribute(final Node node) {
		return getAttributeString(node, "id"); //$NON-NLS-1$
	}

	protected String getNameAttribute(final Node node) {
		return getAttributeString(node, "name"); //$NON-NLS-1$
	}

	protected String getExporterAttribute(final Node node) {
		return getAttributeString(node, "exporter"); //$NON-NLS-1$
	}

	protected String getExporterVersionAttribute(final Node node) {
		return getAttributeString(node, "exporterVersion"); //$NON-NLS-1$
	}

	protected URI getExpressionLanguageAttribute(final Node node) {
		return getAttributeURI(node, "expressionLanguage"); //$NON-NLS-1$
	}

	protected URI getTypeLanguageAttribute(final Node node) {
		return getAttributeURI(node, "typeLanguage"); //$NON-NLS-1$
	}

	protected MimeType getTextFormatAttribute(final Node node) {
		return getAttributeMimeType(node, "textFormat"); //$NON-NLS-1$
	}

	protected <T extends BaseElement> Reference<T> getAttributeIDREF(
			final Node node, final String name, final Class<T> clazz) {
		return new CastReference<>(new NamedReference<>(elements, getAttributeString(node, name)), clazz);
	}

	protected Reference<FlowNode> getSourceRefAttribute(final Node node) {
		return getAttributeIDREF(node, "sourceRef", FlowNode.class); //$NON-NLS-1$
	}

	protected Reference<FlowNode> getTargetRefAttribute(final Node node) {
		return getAttributeIDREF(node, "targetRef", FlowNode.class); //$NON-NLS-1$
	}

	protected Reference<FlowNode> getAttachedToRefAttribute(final Node node) {
		return getAttributeIDREF(node, "attachedToRef", FlowNode.class); //$NON-NLS-1$
	}

	protected Reference<SequenceFlow> getDefaultAttribute(final Node node) {
		return new CastReference<>(new NamedReference<>(
				elements, getAttributeString(node, "default")), SequenceFlow.class); //$NON-NLS-1$
	}

	protected boolean getInstantiateAttribute(final Node node) {
		return getAttributeBoolean(node, "instantiate"); //$NON-NLS-1$
	}

	protected boolean getIsInterruptingAttribute(final Node node) {
		return getAttributeBoolean(node, "isInterrupting"); //$NON-NLS-1$
	}

	protected boolean getCancelActivityAttribute(final Node node) {
		return getAttributeBoolean(node, "cancelActivity"); //$NON-NLS-1$
	}

	protected boolean getIsCollectionAttribute(final Node node) {
		return getAttributeBoolean(node, "isCollection"); //$NON-NLS-1$
	}

	protected boolean getTriggeredByEventAttribute(final Node node) {
		return getAttributeBoolean(node, "triggeredByEvent"); //$NON-NLS-1$
	}

	protected boolean getIsClosedAttribute(final Node node) {
		return getAttributeBoolean(node, "isClosed"); //$NON-NLS-1$
	}

	protected boolean getIsSequentialAttribute(final Node node) {
		return getAttributeBoolean(node, "isSequential"); //$NON-NLS-1$
	}

	protected boolean getIsForCompensationAttribute(final Node node) {
		return getAttributeBoolean(node, "isForCompensation"); //$NON-NLS-1$
	}
	
	protected String getCalledElementAttribute(final Node node) {
		return getAttributeString(node, "calledElement"); //$NON-NLS-1$
	}


	protected AssociationDirection getParameterAssociationDirection(final Node node) {
		final String value = getAttributeString(node, "associationDirection"); //$NON-NLS-1$
		final AssociationDirection direction = AssociationDirection.byValue(value);
		if (direction != null) {
			return direction;
		}
		return AssociationDirection.NONE;
	}

	protected boolean readAnyArtifact(final Node node) {
		return readElementAssociation(node)
				|| readElementGroup(node)
				|| readElementTextAnnotation(node);
	}

	protected boolean readAnyRootElement(final Node node) {
		return readElementMessage(node)
				|| readElementDataStore(node)
				|| readElementProcess(node)
				|| readElementCollaboration(node)
				|| readElementChoreography(node);
	}

	protected boolean readElementDefinitions(final Node node) {
		if (isElementNode(node, BPMN, "definitions")) { //$NON-NLS-1$
			exporter = getExporterAttribute(node);
			exporterVersion = getExporterVersionAttribute(node);
			expressionLanguage = getExpressionLanguageAttribute(node);
			typeLanguage = getTypeLanguageAttribute(node);
			final NodeList childNodes = node.getChildNodes();
			for (int i = 0; i < childNodes.getLength(); ++i) {
				final Node childNode = childNodes.item(i);
				if (!readAnyRootElement(childNode)) {
					showUnknowNode(childNode);
				}
			}
			return true;
		}
		return false;
	}

	protected boolean readElementMessage(final Node node) {
		if (isElementNode(node, BPMN, "message")) { //$NON-NLS-1$
			final Message message = new Message(getIdAttribute(node),
					getNameAttribute(node));
			readChildrenOfBaseElement(node, message);
			registerElement(message);
			return true;
		} else {
			return false;
		}
	}

	protected boolean readAnyChildOfBaseElement(final Node node, final BaseElement element) {
		return readElementDocumentation(node, element)
				|| readElementExtensionElements(node, element);
	}

	protected void readChildrenOfBaseElement(final Node node, final BaseElement element) {
		final NodeList childNodes = node.getChildNodes();
		for (int i = 0; i < childNodes.getLength(); ++i) {
			final Node childNode = childNodes.item(i);
			if (!readAnyChildOfBaseElement(childNode, element)) {
				showUnknowNode(childNode);
			}
		}
	}

	protected boolean readElementIncomingOrOutgoing(final Node node, final FlowNode element) {
		if (isElementNode(node, BPMN, "incoming") //$NON-NLS-1$
				|| isElementNode(node, BPMN, "outgoing")) { //$NON-NLS-1$
			// Ignored
			return true;
		}
		return false;
	}

	protected boolean readAnyChildOfFlowElement(final Node node, final FlowElement element) {
		return readAnyChildOfBaseElement(node, element);
	}

	protected boolean readAnyChildOfFlowNode(final Node node, final FlowNode element) {
		return readAnyChildOfFlowElement(node, element)
				|| readElementIncomingOrOutgoing(node, element);
	}

	protected void readChildrenOfFlowNode(final Node node, final FlowNode element) {
		final NodeList childNodes = node.getChildNodes();
		for (int i = 0; i < childNodes.getLength(); ++i) {
			final Node childNode = childNodes.item(i);
			if (!readAnyChildOfFlowNode(childNode, element)) {
				showUnknowNode(childNode);
			}
		}
	}

	protected boolean readAnyEventDefinition(final Node node, final Event event) {
		if (isElementNode(node, BPMN, "cancelEventDefinition")) { //$NON-NLS-1$
			event.setEventDefinition(new CancelEventDefinition(getIdAttribute(node)));
		} else if (isElementNode(node, BPMN, "compensateEventDefinition")) { //$NON-NLS-1$
			event.setEventDefinition(new CompensateEventDefinition(getIdAttribute(node)));
		} else if (isElementNode(node, BPMN, "conditionalEventDefinition")) { //$NON-NLS-1$
			event.setEventDefinition(new ConditionalEventDefinition(getIdAttribute(node)));
		} else if (isElementNode(node, BPMN, "errorEventDefinition")) { //$NON-NLS-1$
			event.setEventDefinition(new ErrorEventDefinition(getIdAttribute(node)));
		} else if (isElementNode(node, BPMN, "escalationEventDefinition")) { //$NON-NLS-1$
			event.setEventDefinition(new EscalationEventDefinition(getIdAttribute(node)));
		} else if (isElementNode(node, BPMN, "linkEventDefinition")) { //$NON-NLS-1$
			event.setEventDefinition(new LinkEventDefinition(getIdAttribute(node), getNameAttribute(node)));
		} else if (isElementNode(node, BPMN, "messageEventDefinition")) { //$NON-NLS-1$
			event.setEventDefinition(new MessageEventDefinition(getIdAttribute(node)));
		} else if (isElementNode(node, BPMN, "signalEventDefinition")) { //$NON-NLS-1$
			event.setEventDefinition(new SignalEventDefinition(getIdAttribute(node)));
		} else if (isElementNode(node, BPMN, "terminateEventDefinition")) { //$NON-NLS-1$
				event.setEventDefinition(new TerminateEventDefinition(getIdAttribute(node)));
		} else if (isElementNode(node, BPMN, "timerEventDefinition")) { //$NON-NLS-1$
			event.setEventDefinition(new TimerEventDefinition(getIdAttribute(node)));
		} else {
			return false;
		}
		return true;
	}

	protected void readChildrenOfEvent(final Node node, final Event event) {
		final NodeList childNodes = node.getChildNodes();
		for (int i = 0; i < childNodes.getLength(); ++i) {
			final Node childNode = childNodes.item(i);
			if (!readAnyChildOfFlowNode(childNode, event)
					&& !readAnyEventDefinition(childNode, event)
					&& !readAnyDataAssociation(childNode)) {
				showUnknowNode(childNode);
			}
		}
	}

	protected boolean readElementStartEvent(final Node node,
			final FlowElementsContainer container) {
		if (isElementNode(node, BPMN, "startEvent")) { //$NON-NLS-1$
			final StartEvent event = new StartEvent(
					getIdAttribute(node), getNameAttribute(node),
					getIsInterruptingAttribute(node));
			container.addFlowElement(event);
			registerElement(event);
			readChildrenOfEvent(node, event);
			return true;
		} else {
			return false;
		}
	}

	protected boolean readElementEndEvent(final Node node,
			final FlowElementsContainer container) {
		if (isElementNode(node, BPMN, "endEvent")) { //$NON-NLS-1$
			final EndEvent event = new EndEvent(getIdAttribute(node),
					getNameAttribute(node));
			readChildrenOfEvent(node, event);
			container.addFlowElement(event);
			registerElement(event);
			return true;
		}
		return false;
	}

	protected boolean readElementIntermediateThrowEvent(final Node node,
			final FlowElementsContainer container) {
		if (isElementNode(node, BPMN, "intermediateThrowEvent")) { //$NON-NLS-1$
			final IntermediateThrowEvent event = new IntermediateThrowEvent(getIdAttribute(node),
					getNameAttribute(node));
			readChildrenOfEvent(node, event);
			container.addFlowElement(event);
			registerElement(event);
			return true;
		}
		return false;
	}

	protected boolean readElementIntermediateCatchEvent(final Node node,
			final FlowElementsContainer container) {
		if (isElementNode(node, BPMN, "intermediateCatchEvent")) { //$NON-NLS-1$
			final IntermediateCatchEvent event = new IntermediateCatchEvent(getIdAttribute(node),
					getNameAttribute(node));
			readChildrenOfEvent(node, event);
			container.addFlowElement(event);
			registerElement(event);
			return true;
		}
		return false;
	}

	protected boolean readElementBoundaryEvent(final Node node,
			final FlowElementsContainer container) {
		if (isElementNode(node, BPMN, "boundaryEvent")) { //$NON-NLS-1$
			final BoundaryEvent event = new BoundaryEvent(getIdAttribute(node),
					getNameAttribute(node), getAttachedToRefAttribute(node), getCancelActivityAttribute(node));
			readChildrenOfEvent(node, event);
			container.addFlowElement(event);
			registerElement(event);
			return true;
		}
		return false;
	}

	protected boolean readAnyDataAssociation(final Node node) {
		if (isElementNode(node, BPMN, "dataInputAssociation") //$NON-NLS-1$
				|| isElementNode(node, BPMN, "dataOutputAssociation")) { //$NON-NLS-1$
			final DataAssociation dataAssociation
					= new DataAssociation(getIdAttribute(node));
			registerElement(dataAssociation);
			return true;
		} else {
			return false;
		}
	}

	protected boolean readElementStandardLoopCharacteristics(final Node node,
			final Activity activity) {
		if (isElementNode(node, BPMN, "standardLoopCharacteristics")) { //$NON-NLS-1$
			final StandardLoopCharacteristics loopCharacteristics
					= new StandardLoopCharacteristics(getIdAttribute(node));
			activity.setLoopCharacteristics(loopCharacteristics);
			registerElement(loopCharacteristics);
			return true;
		}
		return false;
	}

	protected boolean readElementMultiInstanceLoopCharacteristics(final Node node,
			final Activity activity) {
		if (isElementNode(node, BPMN, "multiInstanceLoopCharacteristics")) { //$NON-NLS-1$
			final MultiInstanceLoopCharacteristics loopCharacteristics
					= new MultiInstanceLoopCharacteristics(getIdAttribute(node), getIsSequentialAttribute(node));
			activity.setLoopCharacteristics(loopCharacteristics);
			registerElement(loopCharacteristics);
			return true;
		}
		return false;
	}

	protected boolean readAnyLoopCharacteristics(final Node node,
			final Activity activity) {
		return readElementStandardLoopCharacteristics(node, activity)
				|| readElementMultiInstanceLoopCharacteristics(node, activity);
	}

	protected boolean readAnyChildOfInputOutputSpecification(final Node node,
			final InputOutputSpecification ioSpecification) {
		return readElementDataInput(node)
				|| readElementDataOutput(node);
	}

	protected void readChildrenOfInputOutputSpecification(final Node node,
			final InputOutputSpecification ioSpecification) {
		final NodeList childNodes = node.getChildNodes();
		for (int i = 0; i < childNodes.getLength(); ++i) {
			final Node childNode = childNodes.item(i);
			if (!readAnyChildOfInputOutputSpecification(childNode, ioSpecification)) {
				showUnknowNode(childNode);
			}
		}
	}

	protected boolean readElementInputOutputSpecification(final Node node) {
		if (isElementNode(node, BPMN, "ioSpecification")) { //$NON-NLS-1$
			final InputOutputSpecification ioSpecification
					= new InputOutputSpecification(getIdAttribute(node));
			readChildrenOfInputOutputSpecification(node, ioSpecification);
			registerElement(ioSpecification);
			return true;
		}
		return false;
	}

	protected boolean readAnyChildOfActivity(final Node node, final Activity activity) {
		return readAnyChildOfFlowNode(node, activity)
				|| readAnyLoopCharacteristics(node, activity)
				|| readAnyDataAssociation(node)
				|| readElementInputOutputSpecification(node);
	}

	protected void readDefaultSequenceFlowAttribute(
			final Node node, final DefaultSequenceFlowElement element) {
		element.setDefaultSequenceFlow(getDefaultAttribute(node));
	}

	protected boolean readElementExtensionElements(final Node node,
			final BaseElement element) {
		if (isElementNode(node, BPMN, "extensionElements")) { //$NON-NLS-1$
			//TODO
			return true;
		}
		return false;
	}

	protected boolean readElementDocumentation(final Node node,
			final BaseElement element) {
		if (isElementNode(node, BPMN, "documentation")) { //$NON-NLS-1$
			final String text = node.getTextContent();
			if (!isNullOrEmpty(text)) {
				element.addDocumentation(new Documentation(
						getIdAttribute(node),
						text, getTextFormatAttribute(node)));
			}
			return true;
		}
		return false;
	}

	protected boolean readElementConditionExpression(final Node node,
			final SequenceFlow sequenceFlow) {
		if (isElementNode(node, BPMN, "conditionExpression")) { //$NON-NLS-1$
			final String text = node.getTextContent();
			if (!isNullOrEmpty(text)) { ///XXX:
				sequenceFlow.setConditionExpression(new Expression(getIdAttribute(node)));
			}
			return true;
		}
		return false;
	}

	protected boolean readElementMessageFlow(final Node node) {
		if (isElementNode(node, BPMN, "messageFlow")) { //$NON-NLS-1$
			final MessageFlow messageFlow = new MessageFlow(
					getIdAttribute(node), getNameAttribute(node),
					getSourceRefAttribute(node), getTargetRefAttribute(node));
			readAnyChildOfBaseElement(node, messageFlow);
			registerElement(messageFlow);
			return true;
		} else {
			return false;
		}
	}

	protected void readChildrenOfSequenceFlow(final Node node,
			final SequenceFlow sequenceFlow) {
		final NodeList childNodes = node.getChildNodes();
		for (int i = 0; i < childNodes.getLength(); ++i) {
			final Node childNode = childNodes.item(i);
			if (!readAnyChildOfFlowElement(childNode, sequenceFlow)
					&& !readElementConditionExpression(childNode, sequenceFlow)) {
				showUnknowNode(childNode);
			}
		}
	}

	protected boolean readElementSequenceflow(final Node node,
			final FlowElementsContainer container) {
		if (isElementNode(node, BPMN, "sequenceFlow")) { //$NON-NLS-1$
			final SequenceFlow sequenceFlow = new SequenceFlow(
					getIdAttribute(node), getNameAttribute(node),
					getSourceRefAttribute(node), getTargetRefAttribute(node));
			readChildrenOfSequenceFlow(node, sequenceFlow);
			container.addFlowElement(sequenceFlow);
			registerElement(sequenceFlow);
			return true;
		} else {
			return false;
		}
	}

	protected <T extends FlowElementsContainer & Activity>
			void readChildrenOfFlowElementsContainerActivity(
			final Node node, final T container) {
		readDefaultSequenceFlowAttribute(node, container);
		final NodeList childNodes = node.getChildNodes();
		for (int i = 0; i < childNodes.getLength(); ++i) {
			final Node childNode = childNodes.item(i);
			if (!readAnyChildOfBaseElement(childNode, container)
					&& !readAnyFlowElement(childNode, container)
					&& !readAnyChildOfActivity(childNode, container)) {
				showUnknowNode(childNode);
			}
		}
	}

	protected void readChildrenOfActivity(final Node node,
			final Activity activity) {
		readDefaultSequenceFlowAttribute(node, activity);
		final NodeList childNodes = node.getChildNodes();
		for (int i = 0; i < childNodes.getLength(); ++i) {
			final Node childNode = childNodes.item(i);
			if (!readAnyChildOfActivity(childNode, activity)) {
				showUnknowNode(childNode);
			}
		}
	}

	protected boolean readElementCallActivity(final Node node,
			final FlowElementsContainer activity) {
		if (isElementNode(node, BPMN, "callActivity")) { //$NON-NLS-1$
			final CallActivity callActivity = new CallActivity(
					getIdAttribute(node), getNameAttribute(node),
					getIsForCompensationAttribute(node));
			String calledElement = getCalledElementAttribute(node);
			if (calledElement != null) {
				callActivity.setProcess(new Process(null, calledElement));
			}
			readChildrenOfActivity(node, callActivity);
			registerElement(callActivity);
			return true;
		}
		return false;
	}

	protected boolean readAnyTask(final Node node,
			final FlowElementsContainer container) {
		final Task task;
		if (isElementNode(node, BPMN, "manualTask")) { //$NON-NLS-1$
			task = new ManualTask(getIdAttribute(node), getNameAttribute(node),
					getIsForCompensationAttribute(node));
		} else if (isElementNode(node, BPMN, "userTask")) { //$NON-NLS-1$
			task = new UserTask(getIdAttribute(node), getNameAttribute(node),
					getIsForCompensationAttribute(node));
		} else if (isElementNode(node, BPMN, "businessRuleTask")) { //$NON-NLS-1$
			task = new BusinessRuleTask(getIdAttribute(node), getNameAttribute(node),
					getIsForCompensationAttribute(node));
		} else if (isElementNode(node, BPMN, "scriptTask")) { //$NON-NLS-1$
			task = new ScriptTask(getIdAttribute(node), getNameAttribute(node),
					getIsForCompensationAttribute(node));
		} else if (isElementNode(node, BPMN, "serviceTask")) { //$NON-NLS-1$
			task = new ServiceTask(getIdAttribute(node), getNameAttribute(node),
					getIsForCompensationAttribute(node));
		} else if (isElementNode(node, BPMN, "sendTask")) { //$NON-NLS-1$
			task = new SendTask(getIdAttribute(node), getNameAttribute(node),
					getIsForCompensationAttribute(node));
		} else if (isElementNode(node, BPMN, "receiveTask")) { //$NON-NLS-1$
			task = new ReceiveTask(getIdAttribute(node), getNameAttribute(node),
					getIsForCompensationAttribute(node), getInstantiateAttribute(node));
		} else if (isElementNode(node, BPMN, "task")) { //$NON-NLS-1$
			task = new Task(getIdAttribute(node), getNameAttribute(node),
					getIsForCompensationAttribute(node));
		} else {
			return false;
		}
		readChildrenOfActivity(node, task);
		container.addFlowElement(task);
		registerElement(task);
		return true;
	}

	protected static boolean readElementText(final Node node,
			final TextAnnotation textAnnotation) {
		if (isElementNode(node, BPMN, "text")) { //$NON-NLS-1$
			textAnnotation.setText(getNodeText(node));
			return true;
		} else {
			return false;
		}
	}

	protected boolean readElementTextAnnotation(final Node node) {
		if (isElementNode(node, BPMN, "textAnnotation")) { //$NON-NLS-1$
			final TextAnnotation textAnnotation = new TextAnnotation(getIdAttribute(node));
			final NodeList childNodes = node.getChildNodes();
			for (int i = 0; i < childNodes.getLength(); ++i) {
				final Node childNode = childNodes.item(i);
				if (!readAnyChildOfBaseElement(childNode, textAnnotation)
						&& !readElementText(childNode, textAnnotation)) {
					showUnknowNode(childNode);
				}
			}
			registerElement(textAnnotation);
			return true;
		}
		return false;
	}

	protected boolean readElementAssociation(final Node node) {
		if (isElementNode(node, BPMN, "association")) { //$NON-NLS-1$
			final Association association = new Association(getIdAttribute(node));
			association.setDirection(getParameterAssociationDirection(node));
			readChildrenOfBaseElement(node, association);
			registerElement(association);
			return true;
		}
		return false;
	}

	protected boolean readElementGroup(final Node node) {
		if (isElementNode(node, BPMN, "group")) { //$NON-NLS-1$
			final Group group = new Group(getIdAttribute(node));
			readChildrenOfBaseElement(node, group);
			registerElement(group);
			return true;
		}
		return false;
	}

	protected boolean readAnyGateway(final Node node,
			final FlowElementsContainer container) {
		final Gateway gateway;
		if (isElementNode(node, BPMN, "parallelGateway")) { //$NON-NLS-1$
			gateway = new ParallelGateway(
					getIdAttribute(node), getNameAttribute(node));
		} else if (isElementNode(node, BPMN, "inclusiveGateway")) { //$NON-NLS-1$
			final InclusiveGateway inclusiveGateway = new InclusiveGateway(
					getIdAttribute(node), getNameAttribute(node));
			readDefaultSequenceFlowAttribute(node, inclusiveGateway);
			gateway = inclusiveGateway;
		} else if (isElementNode(node, BPMN, "exclusiveGateway")) { //$NON-NLS-1$
			final ExclusiveGateway exclusiveGateway = new ExclusiveGateway(
					getIdAttribute(node), getNameAttribute(node));
			readDefaultSequenceFlowAttribute(node, exclusiveGateway);
			gateway = exclusiveGateway;
		} else if (isElementNode(node, BPMN, "eventBasedGateway")) { //$NON-NLS-1$
			gateway = new EventBasedGateway(
					getIdAttribute(node), getNameAttribute(node), getInstantiateAttribute(node));
		} else if (isElementNode(node, BPMN, "complexGateway")) { //$NON-NLS-1$
			gateway = new ComplexGateway(
					getIdAttribute(node), getNameAttribute(node));
		} else {
			return false;
		}
		readChildrenOfFlowNode(node, gateway);
		container.addFlowElement(gateway);
		registerElement(gateway);
		return true;
	}

	protected boolean readElementDataObject(final Node node,
			final FlowElementsContainer container) {
		if (isElementNode(node, BPMN, "dataObject")) { //$NON-NLS-1$
			final DataObject dataObject = new DataObject(
					getIdAttribute(node), getNameAttribute(node));
			dataObject.setIsCollection(getIsCollectionAttribute(node));
			readChildrenOfBaseElement(node, dataObject);
			container.addFlowElement(dataObject);
			registerElement(dataObject);
			return true;
		}
		return false;
	}

	protected boolean readElementDataObjectReference(final Node node,
			final FlowElementsContainer container) {
		if (isElementNode(node, BPMN, "dataObjectReference")) { //$NON-NLS-1$
			final DataObjectReference dataObjectReference = new DataObjectReference(
					getIdAttribute(node), getNameAttribute(node),
					getAttributeIDREF(node, "dataObjectRef", DataObject.class)); //$NON-NLS-1$
			readChildrenOfBaseElement(node, dataObjectReference);
			container.addFlowElement(dataObjectReference);
			registerElement(dataObjectReference);
			return true;
		}
		return false;
	}

	protected boolean readElementDataStore(final Node node) {
		if (isElementNode(node, BPMN, "dataStore")) { //$NON-NLS-1$
			final DataStore dataStore = new DataStore(
					getIdAttribute(node), getNameAttribute(node));
			readChildrenOfBaseElement(node, dataStore);
			registerElement(dataStore);
			return true;
		}
		return false;
	}

	protected boolean readElementDataStoreReference(final Node node,
			final FlowElementsContainer container) {
		if (isElementNode(node, BPMN, "dataStoreReference")) { //$NON-NLS-1$
			final DataStoreReference dataStoreReference = new DataStoreReference(
					getIdAttribute(node), getNameAttribute(node),
					getAttributeIDREF(node, "dataObjectRef", DataStore.class)); //$NON-NLS-1$
			readChildrenOfBaseElement(node, dataStoreReference);
			container.addFlowElement(dataStoreReference);
			registerElement(dataStoreReference);
			return true;
		}
		return false;
	}

	protected boolean readElementDataInput(final Node node) {
		if (isElementNode(node, BPMN, "dataInput")) { //$NON-NLS-1$
			final DataInput dataInput = new DataInput(
					getIdAttribute(node), getNameAttribute(node));
			readChildrenOfBaseElement(node, dataInput);
			registerElement(dataInput);
			return true;
		}
		return false;
	}

	protected boolean readElementDataOutput(final Node node) {
		if (isElementNode(node, BPMN, "dataOutput")) { //$NON-NLS-1$
			final DataOutput dataOutput = new DataOutput(
					getIdAttribute(node), getNameAttribute(node));
			readChildrenOfBaseElement(node, dataOutput);
			registerElement(dataOutput);
			return true;
		} else {
			return false;
		}
	}

	protected void readChildrenOfLane(final Node node, final Lane lane) {
		final NodeList childNodes = node.getChildNodes();
		for (int i = 0; i < childNodes.getLength(); ++i) {
			final Node childNode = childNodes.item(i);
			if (!readAnyChildOfBaseElement(childNode, lane)
					&& !readElementChildLaneSet(childNode)) {
				showUnknowNode(childNode);
			}
		}
	}

	protected boolean readElementLane(final Node node) {
		if (isElementNode(node, BPMN, "lane")) { //$NON-NLS-1$
			final Lane lane = new Lane(
					getIdAttribute(node), getNameAttribute(node));
			registerElement(lane);
			readChildrenOfLane(node, lane);
			return true;
		}
		return false;
	}

	protected void readChildrenOfLaneSet(final Node node, final LaneSet laneSet) {
		final NodeList childNodes = node.getChildNodes();
		for (int i = 0; i < childNodes.getLength(); ++i) {
			final Node childNode = childNodes.item(i);
			if (!readAnyChildOfBaseElement(childNode, laneSet)
					&& !readElementLane(childNode)) {
				showUnknowNode(childNode);
			}
		}
	}

	protected boolean readElementChildLaneSet(final Node node) {
		if (isElementNode(node, BPMN, "childLaneSet")) { //$NON-NLS-1$
			final LaneSet laneSet = new LaneSet(
					getIdAttribute(node), getNameAttribute(node));
			registerElement(laneSet);
			readChildrenOfLaneSet(node, laneSet);
			return true;
		}
		return false;
	}

	protected boolean readElementLaneSet(final Node node,
			final FlowElementsContainer container) {
		if (isElementNode(node, BPMN, "laneSet")) { //$NON-NLS-1$
			final LaneSet laneSet = new LaneSet(
					getIdAttribute(node), getNameAttribute(node));
			registerElement(laneSet);
			readChildrenOfLaneSet(node, laneSet);
			return true;
		}
		return false;
	}

	protected boolean readAnyActivity(final Node node,
			final FlowElementsContainer container) {
		return readElementSubprocess(node, container)
				|| readElementAdHocSubprocess(node, container)
				|| readElementTransaction(node, container)
				|| readAnyTask(node, container)
				|| readElementCallActivity(node, container);
	}

	protected boolean readAnyFlowElement(final Node node,
			final FlowElementsContainer container) {
		return readAnyActivity(node, container)
				|| readElementStartEvent(node, container)
				|| readElementEndEvent(node, container)
				|| readElementIntermediateThrowEvent(node, container)
				|| readElementIntermediateCatchEvent(node, container)
				|| readElementBoundaryEvent(node, container)
				|| readAnyGateway(node, container)
				|| readElementSequenceflow(node, container)
				|| readAnyArtifact(node)
				|| readAnyDataAssociation(node)
				|| readElementDataObject(node, container)
				|| readElementDataObjectReference(node, container)
				|| readElementDataStoreReference(node, container)
				|| readElementLaneSet(node, container)
				|| readElementChoreographyTask(node, container)
				|| readElementSubChoreography(node, container)
				|| readElementCallChoreography(node, container);
	}

	protected boolean readElementParticipant(final Node node,
			final Collaboration collaboration) {
		if (isElementNode(node, BPMN, "participant")) { //$NON-NLS-1$
			final Participant participant = new Participant(
					getIdAttribute(node), getNameAttribute(node));
			registerElement(participant);
			collaboration.addParticipant(participant);
			return true;
		}
		return false;
	}

	protected boolean readElementCallConversation(final Node node,
			final Collaboration collaboration) {
		if (isElementNode(node, BPMN, "callConversation")) { //$NON-NLS-1$
			final CallConversation conversation = new CallConversation(
					getIdAttribute(node), getNameAttribute(node));
			registerElement(conversation);
			return true;
		}
		return false;
	}

	protected boolean readElementSubConversation(final Node node,
			final Collaboration collaboration) {
		if (isElementNode(node, BPMN, "subConversation")) { //$NON-NLS-1$
			final SubConversation conversation = new SubConversation(
					getIdAttribute(node), getNameAttribute(node));
			registerElement(conversation);
			return true;
		}
		return false;
	}

	protected boolean readElementConversation(final Node node,
			final Collaboration collaboration) {
		if (isElementNode(node, BPMN, "conversation")) { //$NON-NLS-1$
			final Conversation conversation = new Conversation(
					getIdAttribute(node), getNameAttribute(node));
			registerElement(conversation);
			return true;
		}
		return false;
	}

	protected boolean readAnyConversationNode(final Node node,
			final Collaboration collaboration) {
		return readElementConversation(node, collaboration)
				|| readElementSubConversation(node, collaboration)
				|| readElementCallConversation(node, collaboration);
	}

	protected boolean readElementConversationLink(final Node node,
			final Collaboration collaboration) {
		if (isElementNode(node, BPMN, "conversationLink")) { //$NON-NLS-1$
			final ConversationLink conversationLink = new ConversationLink(
					getIdAttribute(node), getNameAttribute(node));
			registerElement(conversationLink);
			return true;
		}
		return false;
	}

	protected void readChildrenOfCollaboration(final Node node,
			final Collaboration collaboration) {
		final NodeList childNodes = node.getChildNodes();
		for (int i = 0; i < childNodes.getLength(); ++i) {
			final Node childNode = childNodes.item(i);
			if (!readAnyChildOfBaseElement(childNode, collaboration)
					&& !readElementParticipant(childNode, collaboration)
					&& !readAnyConversationNode(childNode, collaboration)
					&& !readElementConversationLink(childNode, collaboration)
					&& !readElementMessageFlow(childNode)
					&& !readAnyArtifact(childNode)) {
				showUnknowNode(childNode);
			}
		}
	}

	protected boolean readElementCollaboration(final Node node) {
		if (isElementNode(node, BPMN, "collaboration")) { //$NON-NLS-1$
			final Collaboration collaboration = new Collaboration(
					getIdAttribute(node), getNameAttribute(node),
					getIsClosedAttribute(node));
			readChildrenOfCollaboration(node, collaboration);
			registerElement(collaboration);
			return true;
		}
		return false;
	}

	protected void readChildrenOfChoreography(final Node node,
			final Choreography choreography) {
		final NodeList childNodes = node.getChildNodes();
		for (int i = 0; i < childNodes.getLength(); ++i) {
			final Node childNode = childNodes.item(i);
			if (!readAnyChildOfBaseElement(childNode, choreography)
					&& !readAnyFlowElement(childNode, choreography)
					&& !readElementParticipant(childNode, choreography)
					&& !readElementMessageFlow(childNode)
					&& !readAnyArtifact(childNode)) {
				showUnknowNode(childNode);
			}
		}
	}

	protected boolean readElementChoreography(final Node node) {
		if (isElementNode(node, BPMN, "choreography")) { //$NON-NLS-1$
			final Choreography choreography = new Choreography(
					getIdAttribute(node), getNameAttribute(node),
					getIsClosedAttribute(node));
			readChildrenOfChoreography(node, choreography);
			registerElement(choreography);
			return true;
		}
		return false;
	}

	protected void readChildrenOfSubChoreography(final Node node,
			final SubChoreography subChoreography) {
		final NodeList childNodes = node.getChildNodes();
		for (int i = 0; i < childNodes.getLength(); ++i) {
			final Node childNode = childNodes.item(i);
			if (!readAnyChildOfFlowNode(childNode, subChoreography)
					&& !readAnyFlowElement(childNode, subChoreography)
					&& !readAnyArtifact(childNode)) {
				showUnknowNode(childNode);
			}
		}
	}

	protected boolean readElementSubChoreography(final Node node,
			final FlowElementsContainer container) {
		if (isElementNode(node, BPMN, "subChoreography")) { //$NON-NLS-1$
			final SubChoreography subChoreography = new SubChoreography(
					getIdAttribute(node), getNameAttribute(node));
			readChildrenOfSubChoreography(node, subChoreography);
			container.addFlowElement(subChoreography);
			registerElement(subChoreography);
			return true;
		}
		return false;
	}

	protected boolean readElementChoreographyTask(final Node node,
			final FlowElementsContainer container) {
		if (isElementNode(node, BPMN, "choreographyTask")) { //$NON-NLS-1$
			final ChoreographyTask choreographyTask = new ChoreographyTask(
					getIdAttribute(node), getNameAttribute(node));
			container.addFlowElement(choreographyTask);
			registerElement(choreographyTask);
			return true;
		}
		return false;
	}

	protected boolean readElementCallChoreography(final Node node,
			final FlowElementsContainer container) {
		if (isElementNode(node, BPMN, "callChoreography")) { //$NON-NLS-1$
			final CallChoreography callChoreography = new CallChoreography(
					getIdAttribute(node), getNameAttribute(node));
			container.addFlowElement(callChoreography);
			registerElement(callChoreography);
			return true;
		}
		return false;
	}

	protected void readChildrenOfProcess(final Node node,
			final FlowElementsContainer container) {
		final NodeList childNodes = node.getChildNodes();
		for (int i = 0; i < childNodes.getLength(); ++i) {
			final Node childNode = childNodes.item(i);
			if (!readAnyChildOfBaseElement(childNode, container)
					&& !readAnyFlowElement(childNode, container)
					&& !readElementInputOutputSpecification(childNode)) {
				showUnknowNode(childNode);
			}
		}
	}

	protected boolean readElementProcess(final Node node) {
		if (isElementNode(node, BPMN, "process")) { //$NON-NLS-1$
			final Process process = new Process(
					getIdAttribute(node), getNameAttribute(node));
			notifyElementLoading(process);
			readChildrenOfProcess(node, process);
			processes.add(process);
			registerElement(process);
			return true;
		}
		return false;
	}

	protected boolean readElementAdHocSubprocess(final Node node,
			final FlowElementsContainer container) {
		if (isElementNode(node, BPMN, "adHocSubProcess")) { //$NON-NLS-1$
			final AdHocSubProcess subprocess = new AdHocSubProcess(
					getIdAttribute(node), getNameAttribute(node),
					getIsForCompensationAttribute(node), getTriggeredByEventAttribute(node));
			readChildrenOfFlowElementsContainerActivity(node, subprocess);
			container.addFlowElement(subprocess);
			registerElement(subprocess);
			return true;
		}
		return false;
	}

	protected boolean readElementSubprocess(final Node node,
			final FlowElementsContainer container) {
		if (isElementNode(node, BPMN, "subProcess")) { //$NON-NLS-1$
			final SubProcess subprocess = new SubProcess(
					getIdAttribute(node), getNameAttribute(node),
					getIsForCompensationAttribute(node), getTriggeredByEventAttribute(node));
			readChildrenOfFlowElementsContainerActivity(node, subprocess);
			container.addFlowElement(subprocess);
			registerElement(subprocess);
			return true;
		}
		return false;
	}

	protected boolean readElementTransaction(final Node node,
			final FlowElementsContainer container) {
		if (isElementNode(node, BPMN, "transaction")) { //$NON-NLS-1$
			final Transaction transaction = new Transaction(
					getIdAttribute(node), getNameAttribute(node),
					getIsForCompensationAttribute(node), getTriggeredByEventAttribute(node));
			readChildrenOfFlowElementsContainerActivity(node, transaction);
			container.addFlowElement(transaction);
			registerElement(transaction);
			return true;
		}
		return false;
	}

	private void assignSequenceFlowsToFlowNodes() {
		final Map<String, SequenceFlow> sequenceFlows = elements.getElementsByClass(SequenceFlow.class);
		for (final Entry<String, SequenceFlow> element : sequenceFlows.entrySet()) {
			final String name = element.getKey();
			final SequenceFlow sequenceFlow = element.getValue();
			final Reference<SequenceFlow> sequenceFlowRef = new CastReference<>(new NamedReference<>(elements, name), SequenceFlow.class);
			final FlowNode source = ReferenceUtils.element(sequenceFlow.getSource());
			if (source != null) {
				source.addOutgoing(sequenceFlowRef);
			} else {
				LOG.warn(MessageFormat.format("Source FlowNode for SequenceFlow {} not found", name));
			}
			final FlowNode target = ReferenceUtils.element(sequenceFlow.getTarget());
			if (target != null) {
				target.addIncoming(sequenceFlowRef);
			} else {
				LOG.warn(MessageFormat.format("Target FlowNode for SequenceFlow {} not found", name));
			}
		}
	}

	private void assignMessageFlowsToFlowNodes() {
		final Map<String, MessageFlow> messageFlows = elements.getElementsByClass(MessageFlow.class);
		for (final Entry<String, MessageFlow> element : messageFlows.entrySet()) {
			final String name = element.getKey();
			final MessageFlow messageFlow = element.getValue();
			final Reference<MessageFlow> messageFlowRef = new CastReference<>(new NamedReference<>(elements, name), MessageFlow.class);
			final FlowNode source = ReferenceUtils.element(messageFlow.getSourceRef());
			if (source != null) {
				source.setOutMessageFlowf(messageFlowRef);
			} else {
				LOG.warn(MessageFormat.format("Source FlowNode for MessageFlow {} not found", name));
			}
			final FlowNode target = ReferenceUtils.element(messageFlow.getTargetRef());
			if (target != null) {
				target.setInMessageFlow(messageFlowRef);
			} else {
				LOG.warn(MessageFormat.format("Target FlowNode for MessageFlow {} not found", name));
			}
		}
	}

	private void assignEventsToEventBasedGateway() {
		final Map<String, EventBasedGateway> eventBasedGateways = elements.getElementsByClass(EventBasedGateway.class);
		for (final Entry<String, EventBasedGateway> element : eventBasedGateways.entrySet()) {
			final String name = element.getKey();
			final EventBasedGateway eventBasedGateway = element.getValue();
			for (final SequenceFlow outgoing : eventBasedGateway.getOutgoing()) {
				final FlowNode target = ReferenceUtils.element(outgoing.getTarget());
				if (target instanceof IntermediateCatchEvent) {
					final Reference<IntermediateCatchEvent> iceRef = new CastReference<>(new NamedReference<>(elements, target.getId()), IntermediateCatchEvent.class);
					eventBasedGateway.addEventBase(iceRef);
				} else {
					LOG.warn(MessageFormat.format("Target must be IntermediateCatchEvent", name));
				}
			}
		}
	}

	private void assignLinkEvents() {
		final Map<String, IntermediateThrowEvent> intermediateThrowEvents = elements.getElementsByClass(IntermediateThrowEvent.class);
		for (final Entry<String, IntermediateThrowEvent> element : intermediateThrowEvents.entrySet()) {
			if (element.getValue().getEventDefinition() != null && !(element.getValue().getEventDefinition() instanceof LinkEventDefinition))
				intermediateThrowEvents.remove(element);
		}
		final Map<String, IntermediateCatchEvent> intermediateCatchEvents = elements.getElementsByClass(IntermediateCatchEvent.class);
		for (final Entry<String, IntermediateCatchEvent> element : intermediateCatchEvents.entrySet()) {
			final IntermediateCatchEvent intermediateCatchEvent = element.getValue();
			if (intermediateCatchEvent.getEventDefinition() != null && intermediateCatchEvent.getEventDefinition() instanceof LinkEventDefinition) {
				intermediateCatchEvent.setCatched(true);
				for (final Entry<String, IntermediateThrowEvent> linkThrowEventEntry : intermediateThrowEvents.entrySet()) {
					final IntermediateThrowEvent linkThrowEvent = linkThrowEventEntry.getValue();
					final String throwEventName = linkThrowEvent.getName();
					if (throwEventName.equals(intermediateCatchEvent.getName()))
						linkThrowEvent.setCatchEvent(new CastReference<>(new NamedReference<>(elements, intermediateCatchEvent.getId()), IntermediateCatchEvent.class));
				}
			}
		}
	}
	
	private void assignCallActivityToProcess() {
		final Map<String, CallActivity> callActivitys = elements.getElementsByClass(CallActivity.class);
		for (final Entry<String, CallActivity> element : callActivitys.entrySet()) {
			CallActivity callActivity = element.getValue();
			Process process = callActivity.getProcess();
			if (process != null) {
				process = elements.getElement(process.getName(), Process.class);
				if (process != null)
					callActivity.setProcess(process);
			}
		}

	}
	
	@Override
	protected void loadData(final Node node) {
		if (readElementDefinitions(node)) {
			assignSequenceFlowsToFlowNodes();
			assignMessageFlowsToFlowNodes();
			assignEventsToEventBasedGateway();
			assignLinkEvents();
			assignCallActivityToProcess();
		} else {
			LOG.error("schema doesn''t contains definitions");
		}
	}

	@Override
	public Collection<E> getDiagrams() {
		return Collections.emptyList();
	}

}
