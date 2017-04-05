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

import static org.junit.Assert.*;

import org.junit.Test;

import com.googlecode.bpmn_simulator.test.logic.StaticAttributedElementsState;

public class AttributedElementsTestcase {

	@Test
	public void testEquals() {
		final StaticAttributedElementsState<String, Integer> state1 = new StaticAttributedElementsState<>("a", "b");
		final StaticAttributedElementsState<String, Integer> state2 = new StaticAttributedElementsState<>("a", "b");
		assertEquals(state1, state2);
		state1.addAttribute("a", 1);
		state2.addAttribute("a", 1);
		assertEquals(state1, state2);
		state1.addAttribute("b", 2);
		state2.addAttribute("b", 2);
		assertEquals(state1, state2);
		state1.removeAttribute("a", 1);
		state2.removeAttribute("a", 1);
		assertEquals(state1, state2);
		state1.removeAttribute("b", 2);
		state2.removeAttribute("b", 2);
		assertEquals(state1, state2);
	}

	@Test
	public void testToString1() {
		final StaticAttributedElementsState<String, Integer> state = new StaticAttributedElementsState<>();
		assertEquals("{}", state.toString());
	}

	@Test
	public void testToString2() {
		final StaticAttributedElementsState<String, Integer> state = new StaticAttributedElementsState<>("a");
		state.addAttribute("a", 1);
		assertEquals("{a:<1>}", state.toString());
	}

	@Test
	public void testToString3() {
		final StaticAttributedElementsState<String, Integer> state = new StaticAttributedElementsState<>("a");
		state.addAttribute("a", 1);
		state.addAttribute("a", 2);
		assertEquals("{a:<1,2>}", state.toString());
	}

	@Test
	public void testToString4() {
		final StaticAttributedElementsState<String, Integer> state = new StaticAttributedElementsState<>("a", "b");
		assertEquals("{a, b}", state.toString());
	}

	@Test
	public void testNotEquals() {
		final StaticAttributedElementsState<String, Integer> state1 = new StaticAttributedElementsState<>("a", "b");
		final StaticAttributedElementsState<String, Integer> state2 = new StaticAttributedElementsState<>("x", "y");
		assertNotEquals(state1, state2);
		final StaticAttributedElementsState<String, Integer> state3 = new StaticAttributedElementsState<>("x", "y");
		state2.addAttribute("x", 1);
		assertNotEquals(state1, state2);
		state3.addAttribute("x", 2);
		assertNotEquals(state1, state2);
	}

	@Test
	public void testMove1() {
		final StaticAttributedElementsState<String, Integer> state1 = new StaticAttributedElementsState<>("a", "b");
		state1.addAttribute("a", 1);
		state1.moveAttribute("a", 1, "b");
		final StaticAttributedElementsState<String, Integer> state2 = new StaticAttributedElementsState<>("a", "b");
		state2.addAttribute("b", 1);
		assertEquals(state2, state1);
	}

	@Test
	public void testMove2() {
		final StaticAttributedElementsState<String, Integer> state1 = new StaticAttributedElementsState<>("a", "b", "c");
		state1.addAttribute("a", 1);
		state1.moveAttribute("a", 1, "b", "c");
		final StaticAttributedElementsState<String, Integer> state2 = new StaticAttributedElementsState<>("a", "b", "c");
		state2.addAttribute("b", 1);
		state2.addAttribute("c", 1);
		assertEquals(state2, state1);
	}

	@Test(expected=IllegalArgumentException.class)
	public void testException1() {
		final StaticAttributedElementsState<String, Integer> state = new StaticAttributedElementsState<>("a", "b");
		state.addAttribute("a", 1);
		state.addAttribute("a", 1);
	}

	@Test(expected=IllegalArgumentException.class)
	public void testException2() {
		final StaticAttributedElementsState<String, Integer> state = new StaticAttributedElementsState<>("a", "b");
		state.removeAttribute("a", 1);
	}

	@Test(expected=IllegalArgumentException.class)
	public void testException3() {
		final StaticAttributedElementsState<String, Integer> state = new StaticAttributedElementsState<>("a", "b");
		state.moveAttribute("a", 1, "b");
	}

}
