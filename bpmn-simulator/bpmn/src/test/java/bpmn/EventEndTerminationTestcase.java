package bpmn;

import org.junit.Test;

import com.googlecode.bpmn_simulator.animation.input.Definition;
import com.googlecode.bpmn_simulator.test.logic.Assert;
import com.googlecode.bpmn_simulator.test.logic.AttributedElementsState;
import com.googlecode.bpmn_simulator.test.logic.StateSequence;
import com.googlecode.bpmn_simulator.test.logic.StaticAttributedElementsState;
import com.googlecode.bpmn_simulator.test.logic.StaticStateSequence;

public class EventEndTerminationTestcase
		extends BPMNTestcase {

	@Test
	public void test() {
		final Definition<?> definition = loadDefinition(file("event_end_termination.bpmn"));
		final StaticStateSequence<StaticAttributedElementsState<String, Integer>> expectedSequence = beginSequence(definition);
		expectedSequence.getLast().addAttribute("sid-3C27EB24-3919-47B2-83F8-AAA6C67C29E0", ONE); // start event
		expectedSequence.addLast()
				.removeAttribute("sid-3C27EB24-3919-47B2-83F8-AAA6C67C29E0", ONE)
				.addAttribute("sid-8E52E6CB-1CA3-4464-ABF2-25E812545115", ONE)
				.addAttribute("sid-08B4199E-BCB2-4AF0-B675-366FFF772BD3", ONE);
		expectedSequence.addLast()
				.moveAttribute("sid-8E52E6CB-1CA3-4464-ABF2-25E812545115", ONE, "sid-AA3ADEAB-04CE-414A-9D08-92A6AD8B2F05")
				.moveAttribute("sid-08B4199E-BCB2-4AF0-B675-366FFF772BD3", ONE, "sid-610330A4-4CEC-4ECA-8FC7-57D9180A9ECC");
		expectedSequence.addLast()
				.removeAttribute("sid-AA3ADEAB-04CE-414A-9D08-92A6AD8B2F05", ONE)
				.removeAttribute("sid-610330A4-4CEC-4ECA-8FC7-57D9180A9ECC", ONE);
		final StateSequence<AttributedElementsState<String, Integer>> sequence = start(definition, "sid-3C27EB24-3919-47B2-83F8-AAA6C67C29E0");
		Assert.assertExpectedFlow(expectedSequence, sequence);
	}

}
