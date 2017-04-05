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

public class GatewayExclusiveTestcase
		extends BPMNTestcase {

	private static final String START = "sid-90206028-DC9A-4492-A1B9-5BC9F4271735";
	private static final String C = "sid-7F143889-2BD5-4A8B-AD26-4CEBD1E63625";
	private static final String GATEWAY = "sid-E6003672-A2D0-4102-86CF-3A0C180B61D0";
	private static final String B = "sid-B404022C-DDE2-472F-8D6D-52EC413C2A75";
	private static final String DEFAULT = "sid-4DE9A6E6-46C3-4D1B-B7E3-63A6F9CA23C3";
	private static final String END = "sid-165A4839-06E8-4E06-B3F1-5F243A13A898";

	@Test
	public void testBranching() {
		final Definition<?> definition = loadDefinition(file("gateway_exclusive_branching.bpmn"));
		((SequenceFlow) Elements.findById(definition.getFlowElements(), B)).getConditionExpression().setValue(true);
		final StaticStateSequence<StaticAttributedElementsState<String, Integer>> expectedSequence = beginSequence(definition);
		expectedSequence.getLast()
				.addAttribute(START, ONE);
		expectedSequence.addLast()
				.moveAttribute(START, ONE, C);
		expectedSequence.addLast()
				.moveAttribute(C, ONE, GATEWAY);
		expectedSequence.addLast()
				.moveAttribute(GATEWAY, ONE, B);
		expectedSequence.addLast()
				.moveAttribute(B, ONE, END);
		expectedSequence.addLast()
				.removeAttribute(END, ONE);
		final StateSequence<AttributedElementsState<String, Integer>> sequence = start(definition, START);
		Assert.assertExpectedFlow(expectedSequence, sequence);
	}

	@Test
	public void testBranchingDefault() {
		final Definition<?> definition = loadDefinition(file("gateway_exclusive_branching.bpmn"));
		final StaticStateSequence<StaticAttributedElementsState<String, Integer>> expectedSequence = beginSequence(definition);
		expectedSequence.getLast()
				.addAttribute(START, ONE);
		expectedSequence.addLast()
				.moveAttribute(START, ONE, C);
		expectedSequence.addLast()
				.moveAttribute(C, ONE, GATEWAY);
		expectedSequence.addLast()
				.moveAttribute(GATEWAY, ONE, DEFAULT);
		expectedSequence.addLast()
				.moveAttribute(DEFAULT, ONE, END);
		expectedSequence.addLast()
				.removeAttribute(END, ONE);
		final StateSequence<AttributedElementsState<String, Integer>> sequence = start(definition, START);
		Assert.assertExpectedFlow(expectedSequence, sequence);
	}

	@Test
	public void testMerging() {
		final Definition<?> definition = loadDefinition(file("gateway_exclusive_merging.bpmn"));
		final StaticStateSequence<StaticAttributedElementsState<String, Integer>> expectedSequence = beginSequence(definition);
		final StateSequence<AttributedElementsState<String, Integer>> sequence = start(definition, "sid-3E48ED1B-6058-4745-801E-F1EC0DCB2F1E");
		expectedSequence.getLast()
				.addAttribute("sid-3E48ED1B-6058-4745-801E-F1EC0DCB2F1E", ONE);
		expectedSequence.addLast()
				.moveAttribute("sid-3E48ED1B-6058-4745-801E-F1EC0DCB2F1E", ONE,
						"sid-CE7CB19B-0224-44A4-8108-B50E5DA89C17", "sid-594934E5-4750-409B-95A4-577E6EDE70E8");
		expectedSequence.addLast()
				.removeAttribute("sid-CE7CB19B-0224-44A4-8108-B50E5DA89C17", ONE)
				.removeAttribute("sid-594934E5-4750-409B-95A4-577E6EDE70E8", ONE)
				.addAttribute("sid-928E1050-9BE2-4159-8C2F-0C4D92CB5A62", TWO);
		expectedSequence.addLast()
				.moveAttribute("sid-928E1050-9BE2-4159-8C2F-0C4D92CB5A62", TWO, "sid-0CDA055B-2593-4251-83FB-D5CF44E3AF99");
		expectedSequence.addLast()
				.moveAttribute("sid-0CDA055B-2593-4251-83FB-D5CF44E3AF99", TWO, "sid-A7BA29A5-DFC2-4BB8-A423-CB87FAC8E514");
		expectedSequence.addLast()
				.removeAttribute("sid-A7BA29A5-DFC2-4BB8-A423-CB87FAC8E514", TWO);
		Assert.assertExpectedFlow(expectedSequence, sequence);
	}

}
