package bpmn;

import org.junit.Test;

import com.googlecode.bpmn_simulator.animation.input.Definition;
import com.googlecode.bpmn_simulator.bpmn.model.Elements;
import com.googlecode.bpmn_simulator.bpmn.model.core.common.SequenceFlow;
import com.googlecode.bpmn_simulator.test.logic.Assert;
import com.googlecode.bpmn_simulator.test.logic.AttributedElementsState;
import com.googlecode.bpmn_simulator.test.logic.StateSequence;
import com.googlecode.bpmn_simulator.test.logic.StaticAttributedElementsState;
import com.googlecode.bpmn_simulator.test.logic.StaticStateSequence;

public class ActivityTestcase
		extends BPMNTestcase {

	private static final String START = "sid-73D8F66E-9FE0-424C-90B4-03DBF3354B1B";
	private static final String A = "sid-29A54B65-B45C-4528-BF52-036442D92FB8";
	private static final String TASK = "sid-8BCABC37-AFF1-4732-BBB3-C1B9CC550641";
	private static final String STANDARD = "sid-6559A81B-A8CC-4FF5-861D-9F9CC7C7E4BA";
	private static final String CONDITIONAL = "sid-575DFB98-EEFA-46DB-AB2E-2ABCC8AFD718";
	private static final String DEFAULT = "sid-E79E2266-5E21-4F4A-B989-F691D765A840";
	private static final String END = "sid-1C5580DE-D864-4C2F-A19C-586A532DFB83";

	@Test
	public void testTaskDefault() {
		final Definition<?> definition = loadDefinition(file("task.bpmn"));
		final StaticStateSequence<StaticAttributedElementsState<String, Integer>> expectedSequence = beginSequence(definition);
		expectedSequence.getLast()
				.addAttribute(START, ONE);
		expectedSequence.addLast()
				.moveAttribute(START, ONE, A);
		expectedSequence.addLast()
				.moveAttribute(A, ONE, TASK);
		expectedSequence.addLast()
				.moveAttribute(TASK, ONE, STANDARD, DEFAULT);
		expectedSequence.addLast()
				.removeAttribute(STANDARD, ONE)
				.removeAttribute(DEFAULT, ONE)
				.addAttribute(END, TWO);
		expectedSequence.addLast()
				.removeAttribute(END, TWO);
		final StateSequence<AttributedElementsState<String, Integer>> sequence = start(definition, START);
		Assert.assertExpectedFlow(expectedSequence, sequence);
	}

	@Test
	public void testTaskConditional() {
		final Definition<?> definition = loadDefinition(file("task.bpmn"));
		((SequenceFlow) Elements.findById(definition.getFlowElements(), CONDITIONAL)).getConditionExpression().setValue(true);
		final StaticStateSequence<StaticAttributedElementsState<String, Integer>> expectedSequence = beginSequence(definition);
		expectedSequence.getLast()
				.addAttribute(START, ONE);
		expectedSequence.addLast()
				.moveAttribute(START, ONE, A);
		expectedSequence.addLast()
				.moveAttribute(A, ONE, TASK);
		expectedSequence.addLast()
				.moveAttribute(TASK, ONE, STANDARD, CONDITIONAL);
		expectedSequence.addLast()
				.removeAttribute(STANDARD, ONE)
				.removeAttribute(CONDITIONAL, ONE)
				.addAttribute(END, TWO);
		expectedSequence.addLast()
				.removeAttribute(END, TWO);
		final StateSequence<AttributedElementsState<String, Integer>> sequence = start(definition, START);
		Assert.assertExpectedFlow(expectedSequence, sequence);
	}

}
