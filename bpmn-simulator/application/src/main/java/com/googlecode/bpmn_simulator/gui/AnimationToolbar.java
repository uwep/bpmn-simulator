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
package com.googlecode.bpmn_simulator.gui;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JToolBar;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.googlecode.bpmn_simulator.animation.execution.AnimationListener;
import com.googlecode.bpmn_simulator.animation.execution.Animator;

@SuppressWarnings("serial")
public class AnimationToolbar
		extends JToolBar
		implements AnimationListener {

	private Animator animator;

	private JButton buttonPauseContinue;
	private JButton buttonStep;

	private JLabel labelSpeed;
	private SpeedSpinner spinnerSpeed;

	public AnimationToolbar(final Animator animator) {
		super();
		create();
		setAnimator(animator);
	}

	public void setAnimator(final Animator animator) {
		if (this.animator != null) {
			this.animator.removeAnimationListener(this);
		}
		this.animator = animator;
		if (this.animator != null) {
			this.animator.addAnimationListener(this);
		}
		updateControls();
	}

	private void create() {
		buttonPauseContinue = new JButton();
		buttonPauseContinue.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent event) {
				if (animator.isPaused()) {
					animator.play();
				} else {
					animator.pause();
				}
			}
		});
		add(buttonPauseContinue);

		buttonStep = new JButton(Theme.ICON_STEP);
		buttonStep.setToolTipText(Messages.getString("Toolbar.step")); //$NON-NLS-1$
		buttonStep.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent event) {
				animator.step(3);
			}
		});
		add(buttonStep);

		addSeparator(new Dimension(32, 32));

		labelSpeed = new JLabel(Theme.ICON_SPEED);
		labelSpeed.setToolTipText(Messages.getString("Toolbar.frameRate")); //$NON-NLS-1$
		add(labelSpeed);

		spinnerSpeed = new SpeedSpinner();
		spinnerSpeed.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(final ChangeEvent event) {
				animator.setSpeed(((SpeedSpinner)event.getSource()).getSpeedFactor());
			}
		});
		labelSpeed.setLabelFor(spinnerSpeed);
		add(spinnerSpeed);

		updateControls();
	}

	protected void updateControls() {
		final boolean isPaused = (animator != null) && animator.isPaused();

		buttonPauseContinue.setEnabled(animator != null);
		buttonStep.setEnabled(isPaused);
		spinnerSpeed.setEnabled(animator != null);
		labelSpeed.setEnabled(animator != null);

		buttonPauseContinue.setIcon(isPaused ? Theme.ICON_PLAY : Theme.ICON_PAUSE);
		buttonPauseContinue.setToolTipText(
				isPaused
						? Messages.getString("Toolbar.play") //$NON-NLS-1$
						: Messages.getString("Toolbar.pause")); //$NON-NLS-1$
	}

	@Override
	public void animationPlay() {
		updateControls();
	}

	@Override
	public void animationPause() {
		updateControls();
	}

}
