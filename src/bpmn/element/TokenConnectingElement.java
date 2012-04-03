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
package bpmn.element;

import java.awt.Color;

import bpmn.token.Instance;
import bpmn.token.Token;
import bpmn.token.TokenCollection;
import bpmn.token.TokenFlow;

public abstract class TokenConnectingElement extends ConnectingElement
		implements TokenFlow {

	private static final long serialVersionUID = 1L;

	private TokenCollection tokens = new TokenCollection();

	public TokenConnectingElement(final String id, final String name,
			ElementRef<FlowElement> source, ElementRef<FlowElement> target) {
		super(id, name, source, target);
	}

	protected TokenCollection getTokens() {
		return tokens;
	}

	@Override
	public void tokenEnter(Token token) {
		addToken(token);
		tokenDispatch(token);
	}

	@Override
	public void tokenDispatch(Token token) {
		assert(getTokens().contains(token));
		if (canForwardToken(token)) {
			forwardToken(token);
		}
		repaint();
	}

	@Override
	public void tokenExit(Token token) {
		removeToken(token);
	}

	protected boolean canForwardToken(Token token) {
		return (token.getSteps() >= getLength());
	}

	protected void addToken(Token token) {
		getTokens().add(token);
		repaint();
	}

	protected void removeToken(Token token) {
		getTokens().remove(token);
		repaint();
	}

	protected void forwardToken(Token token) {
		ElementRef<FlowElement> targetRef = getTargetRef();
		if ((targetRef != null) && targetRef.hasElement()) {
			final FlowElement flowElement = targetRef.getElement();
			if (flowElement instanceof TokenFlow) {
				token.passTo((TokenFlow)flowElement);
				token.remove();
			}
		} else {
			assert(false);
		}
	}

	public boolean hasIncomingPathWithActiveToken(Instance instance) {
		if (getTokens().byInstance(instance).getCount() > 0) {
			// Entweder das Element selbst hat noch Token dieser Instanz
			return true;
		} else {
			// oder eines der eingehenden
			final ElementRef<FlowElement> sourceRef = getSourceRef();
			if ((sourceRef != null) && sourceRef.hasElement()) {
				FlowElement flowElement = sourceRef.getElement();
				if (flowElement instanceof TokenFlow) {
					return ((TokenFlow)flowElement).hasIncomingPathWithActiveToken(instance);
				}
			}
		}
		return false;
	}

	@Override
	public Color getForeground() {
		final TokenCollection tokens = getTokens();
		if ((tokens != null) && (tokens.getCount() > 0)) {
			return Token.HIGHLIGHT_COLOR;
		}
		return super.getForeground();
	}

	@Override
	protected void paintTokens(Graphics g) {
		final TokenCollection tokens = getTokens();
		synchronized (tokens) {
			for (Token token : tokens) {
				token.getInstance().paint(g, waypointToRelative(getPosition(token.getSteps())));
			}
		}
	}

}
