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
package com.googlecode.bpmn_simulator.bpmn.di;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collection;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.googlecode.bpmn_simulator.animation.element.visual.Bounds;
import com.googlecode.bpmn_simulator.animation.element.visual.Font;
import com.googlecode.bpmn_simulator.animation.element.visual.Waypoint;
import com.googlecode.bpmn_simulator.animation.ref.NamedElements;
import com.googlecode.bpmn_simulator.animation.ref.NamedReference;
import com.googlecode.bpmn_simulator.animation.ref.Reference;
import com.googlecode.bpmn_simulator.bpmn.model.BPMNDefinition;
import com.googlecode.bpmn_simulator.bpmn.model.NamedElement;
import com.googlecode.bpmn_simulator.bpmn.model.TextElement;
import com.googlecode.bpmn_simulator.bpmn.model.core.foundation.BaseElement;

/**
 * Diagram Interchange (DI) Definition
 */
public abstract class AbstractDIDefinition<DIAGRAM extends BPMNDiagram<?>>
		extends BPMNDefinition<DIAGRAM> {

	private static final String BPMNDI = "http://www.omg.org/spec/BPMN/20100524/DI"; //$NON-NLS-1$

	private static final String DC = "http://www.omg.org/spec/DD/20100524/DC"; //$NON-NLS-1$

	private static final String DI = "http://www.omg.org/spec/DD/20100524/DI"; //$NON-NLS-1$

	private static final String ELEMENT_DIAGRAM = "BPMNDiagram"; //$NON-NLS-1$
	private static final String ELEMENT_PLANE = "BPMNPlane"; //$NON-NLS-1$
	private static final String ELEMENT_SHAPE = "BPMNShape"; //$NON-NLS-1$
	private static final String ELEMENT_BOUNDS = "Bounds"; //$NON-NLS-1$
	private static final String ELEMENT_EDGE = "BPMNEdge"; //$NON-NLS-1$
	private static final String ELEMENT_WAYPOINT = "waypoint"; //$NON-NLS-1$
	private static final String ELEMENT_LABEL = "BPMNLabel"; //$NON-NLS-1$
	private static final String ELEMENT_LABELSTYLE = "BPMNLabelStyle"; //$NON-NLS-1$
	private static final String ELEMENT_FONT = "Font"; //$NON-NLS-1$

	private final NamedElements<BPMNLabelStyle> labelStyles = new NamedElements<>();

	private final Collection<DIAGRAM> diagrams = new ArrayList<>();

	@Override
	public final Collection<DIAGRAM> getDiagrams() {
		return diagrams;
	}

	protected abstract DIAGRAM createDiagram(String name);

	protected abstract BPMNPlane createPlaneFor(DIAGRAM diagram, BaseElement element);

	protected abstract BPMNShape createShapeFor(DIAGRAM diagram, BaseElement element);

	protected abstract BPMNEdge createEdgeFor(DIAGRAM diagram, BaseElement element);

	protected abstract BPMNLabel createLabelFor(DIAGRAM diagram, BaseElement element);

	@Override
	protected boolean readAnyRootElement(final Node node) {
		return super.readAnyRootElement(node)
				|| readElementBPMNDiagram(node);
	}

	protected Bounds getBoundsAttribute(final Node node) {
		final int x = (int) Math.round(getAttributeDouble(node, "x")); //$NON-NLS-1$
		final int y = (int) Math.round(getAttributeDouble(node, "y")); //$NON-NLS-1$
		final int width = (int) Math.round(getAttributeDouble(node, "width")); //$NON-NLS-1$
		final int height = (int) Math.round(getAttributeDouble(node, "height")); //$NON-NLS-1$
		return new Bounds(x, y, width, height);
	}

	protected Waypoint getPointAttribute(final Node node) {
		final int x = (int) Math.round(getAttributeDouble(node, "x")); //$NON-NLS-1$
		final int y = (int) Math.round(getAttributeDouble(node, "y")); //$NON-NLS-1$
		return new Waypoint(x, y);
	}

	protected Boolean getIsExpandedAttribute(final Node node) {
		return getOptionalAttributeBoolean(node, "isExpanded"); //$NON-NLS-1$
	}

	protected Boolean getIsHorizontalAttribute(final Node node) {
		return getOptionalAttributeBoolean(node, "isHorizontal"); //$NON-NLS-1$
	}

	protected Boolean getIsMarkerVisibleAttribute(final Node node) {
		return getOptionalAttributeBoolean(node, "isMarkerVisible"); //$NON-NLS-1$
	}

	protected Boolean getIsMessageVisibleAttribute(final Node node) {
		return getOptionalAttributeBoolean(node, "isMessageVisible"); //$NON-NLS-1$
	}

	protected ParticipantBandKind getParticipantBandKindAttribute(final Node node) {
		return ParticipantBandKind.fromString(getAttributeString(node, "participantBandKind")); //$NON-NLS-1$
	}

	protected BaseElement getAttributeElement(final Node node, final String name) {
		final String elementId = getAttributeString(node, name);
		final BaseElement baseElement = getElement(elementId);
		if (baseElement == null) {
			LOG.error(MessageFormat.format("Unknown BPMN element with id ''{0}'' for {1}", elementId, name));
		}
		return baseElement;
	}

	protected BaseElement getBPMNElementAttribute(final Node node) {
		return getAttributeElement(node, "bpmnElement"); //$NON-NLS-1$
	}

	protected boolean readElementBPMNShape(final Node node, final DIAGRAM diagram) {
		if (isElementNode(node, BPMNDI, ELEMENT_SHAPE)) {
			final BaseElement element = getBPMNElementAttribute(node);
			final BPMNShape shape = createShapeFor(diagram, element);
			if (shape != null) {
				final Boolean isExpanded = getIsExpandedAttribute(node);
				if (isExpanded != null) {
					shape.setExpanded(isExpanded.booleanValue());
				}
				final Boolean isHorizontal = getIsHorizontalAttribute(node);
				if (isHorizontal != null) {
					shape.setHorizontal(isHorizontal.booleanValue());
				}
				final Boolean isMarkerVisible = getIsMarkerVisibleAttribute(node);
				if (isMarkerVisible != null) {
					shape.setMarkerVisible(isMarkerVisible.booleanValue());
				}
				final Boolean isMessageVisible = getIsMessageVisibleAttribute(node);
				if (isMessageVisible != null) {
					shape.setMessageVisible(isMessageVisible.booleanValue());
				}
				final ParticipantBandKind participantBandKind = getParticipantBandKindAttribute(node);
				if (participantBandKind != null) {
					shape.setParticipantBandKind(participantBandKind);
				}
				final NodeList childNodes = node.getChildNodes();
				for (int i = 0; i < childNodes.getLength(); ++i) {
					final Node childNode = childNodes.item(i);
					if (!readElementBounds(childNode, shape)
							&& !readElementLabel(childNode, diagram, element, shape)) {
						showUnknowNode(childNode);
					}
				}
			} else {
				LOG.error(MessageFormat.format("can''t create shape for {0}", element));
			}
			return true;
		} else {
			return false;
		}
	}

	protected boolean readElementBPMNEdge(final Node node, final DIAGRAM diagram) {
		if (isElementNode(node, BPMNDI, ELEMENT_EDGE)) {
			final BaseElement element = getBPMNElementAttribute(node);
			final BPMNEdge edge = createEdgeFor(diagram, element);
			if (edge != null) {
				final NodeList childNodes = node.getChildNodes();
				for (int i = 0; i < childNodes.getLength(); ++i) {
					final Node childNode = childNodes.item(i);
					if (!readElementWaypoint(childNode, edge)
							&& !readElementLabel(childNode, diagram, element, edge)) {
						showUnknowNode(childNode);
					}
				}
			} else {
				LOG.error(MessageFormat.format("can''t create edge for {0}", element));
			}
			return true;
		} else {
			return false;
		}
	}

	protected static String getElementText(final BaseElement element) {
		final String text;
		if (element instanceof TextElement) {
			text = ((TextElement) element).getText(); ///XXX: textFormat
		} else if (element instanceof NamedElement) {
			text = ((NamedElement) element).getName();
		} else {
			if (element == null) {
				text = "?";
			} else {
				text = element.getId();
			}
		}
		return text;
	}

	private boolean readLabelElements(final Node node,
			final DIAGRAM diagram, final BaseElement element, final BPMNLabel label) {
		if (label != null) {
			label.setText(getElementText(element));
			final NodeList childNodes = node.getChildNodes();
			boolean boundsRead = false;
			for (int i = 0; i < childNodes.getLength(); ++i) {
				final Node childNode = childNodes.item(i);
				if (readElementBounds(childNode, label)) {
					boundsRead = true;
				} else {
					showUnknowNode(childNode);
				}
			}
			return boundsRead;
		} else {
			LOG.error(MessageFormat.format("can''t create label for {0}", element));
		}
		return false;
	}

	protected boolean readElementLabel(final Node node,
			final DIAGRAM diagram, final BaseElement element, final BPMNEdge edge) {
		if (isElementNode(node, BPMNDI, ELEMENT_LABEL)) {
			final BPMNLabel label = createLabelFor(diagram, element);
			if (label != null) {
				label.setStyle(getAttributeLabelStyle(node));
				if (!readLabelElements(node, diagram, element, label)) {
					edge.alignLabel(label);
				}
				return true;
			}
		}
		return false;
	}

	protected boolean readElementLabel(final Node node,
			final DIAGRAM diagram, final BaseElement element, final BPMNShape shape) {
		if (isElementNode(node, BPMNDI, ELEMENT_LABEL)) {
			final BPMNLabel label = createLabelFor(diagram, element);
			if (label != null) {
				label.setTextVertical(shape.isHorizontal());
				label.setStyle(getAttributeLabelStyle(node));
				if (!readLabelElements(node, diagram, element, label)) {
					shape.alignLabel(label);
				}
				return true;
			}
		}
		return false;
	}

	protected boolean readElementBPMNPlane(final Node node, final DIAGRAM diagram) {
		if (isElementNode(node, BPMNDI, ELEMENT_PLANE)) {
			final BaseElement element = getBPMNElementAttribute(node);
			if (element != null) {
				final BPMNPlane plane = createPlaneFor(diagram, element);
				if (plane != null) {
					final NodeList childNodes = node.getChildNodes();
					for (int i = 0; i < childNodes.getLength(); ++i) {
						final Node childNode = childNodes.item(i);
						if (!readElementBPMNShape(childNode, diagram)
								&& !readElementBPMNEdge(childNode, diagram)) {
							showUnknowNode(childNode);
						}
					}
				} else {
					LOG.error(MessageFormat.format("can''t create plane for {0}", element));
				}
			}
			return true;
		}
		return false;
	}

	protected Reference<BPMNLabelStyle> getAttributeLabelStyle(final Node node) {
		final String id = getAttributeString(node, "labelStyle");
		return new NamedReference<>(labelStyles, id);
	}

	private boolean readElementFont(final Node node, final BPMNLabelStyle labelStyle) {
		if (isElementNode(node, DC, ELEMENT_FONT)) {
			final Font font = new Font(getAttributeString(node, "name"),
					getOptionalAttributeDouble(node, "size"),
					getOptionalAttributeBoolean(node, "isBold", false),
					getOptionalAttributeBoolean(node, "isItalic", false),
					getOptionalAttributeBoolean(node, "isUnderline", false),
					getOptionalAttributeBoolean(node, "isStrikeThrough", false));
			labelStyle.setFont(font);
			return true;
		}
		return false;
	}

	private boolean readElementBPMNLabelStyle(final Node node, final DIAGRAM diagram) {
		if (isElementNode(node, BPMNDI, ELEMENT_LABELSTYLE)) {
			final BPMNLabelStyle labelStyle = new BPMNLabelStyle(getIdAttribute(node));
			final NodeList childNodes = node.getChildNodes();
			for (int i = 0; i < childNodes.getLength(); ++i) {
				final Node childNode = childNodes.item(i);
				if (!readElementFont(childNode, labelStyle)) {
					showUnknowNode(childNode);
				}
			}
			labelStyles.setElement(labelStyle.getId(), labelStyle);
			return true;
		}
		return false;
	}

	protected boolean readElementBPMNDiagram(final Node node) {
		if (isElementNode(node, BPMNDI, ELEMENT_DIAGRAM)) {
			final String name = getNameAttribute(node);
			final DIAGRAM diagram = createDiagram(name);
			final NodeList childNodes = node.getChildNodes();
			for (int i = 0; i < childNodes.getLength(); ++i) {
				final Node childNode = childNodes.item(i);
				if (!readElementBPMNPlane(childNode, diagram)
						&& !readElementBPMNLabelStyle(childNode, diagram)) {
					showUnknowNode(childNode);
				}
			}
			diagrams.add(diagram);
			return true;
		}
		return false;
	}

	protected boolean readElementBounds(final Node node,
			final BPMNShape shape) {
		if (isElementNode(node, DC, ELEMENT_BOUNDS)) {
			shape.setElementBounds(getBoundsAttribute(node));
			return true;
		} else {
			return false;
		}
	}

	protected boolean readElementWaypoint(final Node node,
			final BPMNEdge edge) {
		if (isElementNode(node, DI, ELEMENT_WAYPOINT)) {
			edge.addElementWaypoint(getPointAttribute(node));
			return true;
		} else {
			return false;
		}
	}

	protected boolean readElementBounds(final Node node,
			final BPMNLabel label) {
		if (isElementNode(node, DC, ELEMENT_BOUNDS)) {
			label.setBounds(getBoundsAttribute(node));
			return true;
		} else {
			return false;
		}
	}

}
