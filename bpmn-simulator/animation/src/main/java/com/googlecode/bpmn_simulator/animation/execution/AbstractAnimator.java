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
package com.googlecode.bpmn_simulator.animation.execution;

import java.util.HashSet;
import java.util.Set;

abstract class AbstractAnimator
		implements Runnable {

	private static final int STEPS_PER_SEC = 25;
	private static final long MS_PER_STEP = 1000 / STEPS_PER_SEC;

	private final Set<AnimationListener> animationListeners = new HashSet<>();

	private Thread thread;

	private volatile boolean paused;

	private float speedFactor = 1.0f;

	public AbstractAnimator() {
		super();
	}

	public static int secondsToSteps(final double sec) {
		return (int) (STEPS_PER_SEC * sec);
	}

	public static double stepsToSeconds(final int steps) {
		return (double) steps / STEPS_PER_SEC;
	}

	public void addAnimationListener(final AnimationListener listener) {
		synchronized (animationListeners) {
			animationListeners.add(listener);
		}
	}

	public void removeAnimationListener(final AnimationListener listener) {
		synchronized (animationListeners) {
			animationListeners.remove(listener);
		}
	}

	protected void notifyAnimationPlay() {
		synchronized (animationListeners) {
			for (final AnimationListener listener : animationListeners) {
				listener.animationPlay();
			}
		}
	}

	protected void notifyAnimationPause() {
		synchronized (animationListeners) {
			for (final AnimationListener listener : animationListeners) {
				listener.animationPause();
			}
		}
	}

	public void setSpeed(final float factor) {
		assert factor > 0.0f;
		speedFactor = factor;
	}

	protected long getStepSleep() {
		return (long) (MS_PER_STEP / speedFactor);
	}

	@Override
	public void run() {
		try {
			while (thread.isAlive() && !thread.isInterrupted()) {
				Thread.sleep(getStepSleep());
				if (!isPaused()) {
					step(1);
				}
			}
		} catch (InterruptedException e) {
			// ok
		}
	}

	public abstract void step(int count);

	public void play() {
		paused = false;
		notifyAnimationPlay();
	}

	public void pause() {
		paused = true;
		notifyAnimationPause();
	}

	public boolean isPaused() {
		return paused;
	}

	protected synchronized void start() {
		assert thread == null;
		if (thread == null) {
			thread = new Thread(this, "Animation"); //$NON-NLS-1$
			thread.start();
		}
	}

	public void end() {
		assert thread != null;
		if (thread != null) {
			synchronized (thread) {
				thread.interrupt();
				try {
					thread.join();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				thread = null;
			}
		}
		paused = false;
	}

}
