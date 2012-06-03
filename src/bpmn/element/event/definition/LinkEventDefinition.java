/*
 * Copyright (C) 2012 Stefan Schweitzer
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
package bpmn.element.event.definition;

import java.util.Collection;

import javax.swing.Icon;

import bpmn.element.Element;
import bpmn.element.Visualization;
import bpmn.element.activity.Process;
import bpmn.element.event.AbstractEvent;
import bpmn.element.event.IntermediateCatchEvent;
import bpmn.instance.Instance;
import bpmn.token.Token;
import bpmn.trigger.TriggerCatchElement;
import bpmn.trigger.Trigger;

public final class LinkEventDefinition extends EventDefinition {

	private final String name;

	public LinkEventDefinition(final AbstractEvent event, final String name) {
		super(event);
		this.name = name;
	}

	public String getName() {
		return name;
	}

	@Override
	public Icon getIcon(final Visualization visualization, final boolean inverse) {
		return visualization.getIcon(inverse
				? Visualization.ICON_LINK_INVERSE
				: Visualization.ICON_LINK);
	}

	protected static TriggerCatchElement findLinkTargetInProcess(
			final Process process, final String targetName) {
		for (final Element element : process.getElements()) {
			if (element instanceof IntermediateCatchEvent) {
				final IntermediateCatchEvent event = (IntermediateCatchEvent)element;
				final EventDefinition definition = event.getDefinition(); 
				if (definition instanceof LinkEventDefinition) {
					final String name = ((LinkEventDefinition)definition).getName();
					if (targetName.equals(name)) {
						return event; 
					}
				}
			}
		}
		return null;
	}

	protected TriggerCatchElement findLinkTarget(
			final Collection<Process> processes, final String targetName) {
		for (final Process process : processes) {
			final TriggerCatchElement linkTarget = findLinkTargetInProcess(process, targetName);
			if (linkTarget != null) {
				return linkTarget; 
			}
		}
		return null;
	}

	@Override
	public void throwTrigger(final Token token) {
		super.throwTrigger(token);

		final Process process = getProcessByToken(token);
		final String targetName = getName();
		TriggerCatchElement linkTarget
				= findLinkTargetInProcess(process, targetName);
		if (linkTarget == null) {
			linkTarget = findLinkTarget(process.getModel().getProcesses(), targetName);
		}
		if (linkTarget != null) {
			final Instance instance = token.getInstance();
			linkTarget.catchTrigger(new Trigger(instance, instance));
		}
	}

}
