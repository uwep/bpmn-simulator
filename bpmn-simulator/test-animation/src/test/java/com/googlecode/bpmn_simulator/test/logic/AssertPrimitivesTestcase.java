/*
 * Copyright (C) 2015 Stefan Schweitzer
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
package com.googlecode.bpmn_simulator.test.logic;

import static com.googlecode.bpmn_simulator.test.logic.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.googlecode.bpmn_simulator.test.logic.StaticStateSequence;

public class AssertPrimitivesTestcase {

	@Rule
    public ExpectedException thrown = ExpectedException.none();

	private void setExpectedException(final String message) {
		thrown.expect(AssertionError.class);
		thrown.expectMessage(message);
	}

	@Test
	public void testStates0BothEmpty() {
		final StateSequence<Integer> expectedStates = new StaticStateSequence<>();
		final List<Integer> states = new ArrayList<>();
		assertExpectedFlow(expectedStates, states.iterator());
	}

	@Test
	public void testStates1BothEqual() {
		final StateSequence<Integer> expectedStates = new StaticStateSequence<>(1);
		final List<Integer> states = new ArrayList<>();
		states.add(1);
		assertExpectedFlow(expectedStates, states.iterator());
	}

	@Test
	public void testStates2BothEqual() {
		final StateSequence<Integer> expectedStates = new StaticStateSequence<>(1, 2);
		final List<Integer> states = new ArrayList<>();
		states.add(1);
		states.add(2);
		assertExpectedFlow(expectedStates, states.iterator());
	}

	@Test
	public void testStates1AvailableLarger() {
		final StateSequence<Integer> expectedStates = new StaticStateSequence<>(1);
		final List<Integer> states = new ArrayList<>();
		states.add(1);
		states.add(1);
		states.add(1);
		assertExpectedFlow(expectedStates, states.iterator());
	}

	@Test
	public void testStates2FirstAvailableLarger() {
		final StateSequence<Integer> expectedStates = new StaticStateSequence<>(1, 2);
		final List<Integer> states = new ArrayList<>();
		states.add(1);
		states.add(1);
		states.add(1);
		states.add(2);
		assertExpectedFlow(expectedStates, states.iterator());
	}

	@Test
	public void testStates2SecondAvailableLarger() {
		final StateSequence<Integer> expectedStates = new StaticStateSequence<>(1, 2);
		final List<Integer> states = new ArrayList<>();
		states.add(1);
		states.add(2);
		states.add(2);
		states.add(2);
		assertExpectedFlow(expectedStates, states.iterator());
	}

	@Test
	public void testStates2BothAvailableLarger() {
		final StateSequence<Integer> expectedStates = new StaticStateSequence<>(1, 2);
		final List<Integer> states = new ArrayList<>();
		states.add(1);
		states.add(1);
		states.add(1);
		states.add(2);
		states.add(2);
		states.add(2);
		assertExpectedFlow(expectedStates, states.iterator());
	}

	@Test
	public void testToStringEmpty() {
		final StateSequence<Integer> states = new StaticStateSequence<>();
		org.junit.Assert.assertEquals("{}", states.toString());
	}

	@Test
	public void testToString1() {
		final StateSequence<Integer> states = new StaticStateSequence<>(1);
		org.junit.Assert.assertEquals("{0:1}", states.toString());
	}

	@Test
	public void testToString2() {
		final StateSequence<Integer> states = new StaticStateSequence<>(1, 2);
		org.junit.Assert.assertEquals("{0:1, 1:2}", states.toString());
	}

	@Test
	public void testExceptionUnexpectedFirstState0() {
		setExpectedException("unexpected state 1. no more states expected");
		final StateSequence<Integer> expectedStates = new StaticStateSequence<>();
		final List<Integer> states = new ArrayList<>();
		states.add(1);
		assertExpectedFlow(expectedStates, states.iterator());
	}

	@Test
	public void testExceptionUnexpectedFirstState1() {
		setExpectedException("unexpected state[0] 2. expected state[0] 1");
		final StateSequence<Integer> expectedStates = new StaticStateSequence<>(1);
		final List<Integer> states = new ArrayList<>();
		states.add(2);
		assertExpectedFlow(expectedStates, states.iterator());
	}

	@Test
	public void testExceptionUnexpectedFirstState2() {
		setExpectedException("unexpected state[0] 2. expected state[0] 1");
		final StateSequence<Integer> expectedStates = new StaticStateSequence<>(1, 2);
		final List<Integer> states = new ArrayList<>();
		states.add(2);
		assertExpectedFlow(expectedStates, states.iterator());
	}

	@Test
	public void testExceptionUnexpectedLastState() {
		setExpectedException("unexpected state[1] 3. expected state[1] 2");
		final StateSequence<Integer> expectedStates = new StaticStateSequence<>(1, 2);
		final List<Integer> states = new ArrayList<>();
		states.add(1);
		states.add(3);
		assertExpectedFlow(expectedStates, states.iterator());
	}

	@Test
	public void testExceptionUnexpectedBothStates() {
		setExpectedException("unexpected state[0] 2. expected state[0] 1");
		final StateSequence<Integer> expectedStates = new StaticStateSequence<>(1, 2);
		final List<Integer> states = new ArrayList<>();
		states.add(2);
		states.add(1);
		assertExpectedFlow(expectedStates, states.iterator());
	}

	@Test
	public void testException2() {
		setExpectedException("no next state. more states expected");
		final StateSequence<Integer> expectedStates = new StaticStateSequence<>(1);
		final List<Integer> states = new ArrayList<>();
		assertExpectedFlow(expectedStates, states.iterator());
	}

	@Test
	public void testException3() {
		setExpectedException("unexpected state 1. no more states expected");
		final StateSequence<Integer> expectedStates = new StaticStateSequence<>();
		final List<Integer> states = new ArrayList<>();
		states.add(1);
		assertExpectedFlow(expectedStates, states.iterator());
	}

	@Test
	public void testException4() {
		setExpectedException("no next state available. next state[1] expected is 1");
		final StateSequence<Integer> expectedStates = new StaticStateSequence<>(1, 1);
		final List<Integer> states = new ArrayList<>();
		states.add(1);
		assertExpectedFlow(expectedStates, states.iterator());
	}

	@Test
	public void testException5() {
		setExpectedException("no next state available. next state[1] expected is 2");
		final StateSequence<Integer> expectedStates = new StaticStateSequence<>(1, 2);
		final List<Integer> states = new ArrayList<>();
		states.add(1);
		assertExpectedFlow(expectedStates, states.iterator());
	}

}
