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

public class GatewayInclusiveTestcase
		extends BPMNTestcase {

	private static final String START = "sid-2D63D556-FCC4-4280-B841-6FAA393ECA3C";
	private static final String C = "sid-3469581F-3070-4074-A075-AA07D7943201";
	private static final String GATEWAY = "sid-8423404F-0ABE-4C95-BDC1-52C5D258156E";
	private static final String A = "sid-A399196C-C368-4176-A4A8-25ECA2BAFEAD";
	private static final String B = "sid-CE4DEFB8-5919-4E0C-9FCB-8123992D8F19";
	private static final String DEFAULT = "sid-154FE2F6-8784-49E8-9050-BF4C68C7D8F2";
	private static final String END = "sid-28879A46-3DA4-4CFF-B485-9EC72D37D643";

	@Test
	public void testBranching() {
		final Definition<?> definition = loadDefinition(file("gateway_inclusive_branching.bpmn"));
		final StaticStateSequence<StaticAttributedElementsState<String, Integer>> expectedSequence = beginSequence(definition);
		((SequenceFlow) Elements.findById(definition.getFlowElements(), A)).getConditionExpression().setValue(true);
		((SequenceFlow) Elements.findById(definition.getFlowElements(), B)).getConditionExpression().setValue(true);
		final StateSequence<AttributedElementsState<String, Integer>> sequence = start(definition, START);
		expectedSequence.getLast()
				.addAttribute(START, ONE);
		expectedSequence.addLast()
				.moveAttribute(START, ONE, C);
		expectedSequence.addLast()
				.moveAttribute(C, ONE, GATEWAY);
		expectedSequence.addLast()
				.moveAttribute(GATEWAY, ONE, A, B);
		expectedSequence.addLast()
				.removeAttribute(A, ONE)
				.removeAttribute(B, ONE)
				.addAttribute(END, TWO);
		expectedSequence.addLast()
				.removeAttribute(END, TWO);
		Assert.assertExpectedFlow(expectedSequence, sequence);
	}

	@Test
	public void testBranchingDefault() {
		final Definition<?> definition = loadDefinition(file("gateway_inclusive_branching.bpmn"));
		final StaticStateSequence<StaticAttributedElementsState<String, Integer>> expectedSequence = beginSequence(definition);
		final StateSequence<AttributedElementsState<String, Integer>> sequence = start(definition, START);
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
		Assert.assertExpectedFlow(expectedSequence, sequence);
	}

	@Test
	public void testMerging() {
		final Definition<?> definition = loadDefinition(file("gateway_inclusive_merging.bpmn"));
		final StaticStateSequence<StaticAttributedElementsState<String, Integer>> expectedSequence = beginSequence(definition);
		final StateSequence<AttributedElementsState<String, Integer>> sequence = start(definition, "sid-241F06A3-AE48-47B9-9324-46138840DFE7");
		expectedSequence.getLast()
				.addAttribute("sid-241F06A3-AE48-47B9-9324-46138840DFE7", ONE);
		expectedSequence.addLast()
				.moveAttribute("sid-241F06A3-AE48-47B9-9324-46138840DFE7", ONE,
						"sid-BE5C92DE-AC9A-4BBF-A855-5577AADCABD0", "sid-E7B7467D-887A-45BD-9F9A-D67D6325667B");
		expectedSequence.addLast()
				.removeAttribute("sid-BE5C92DE-AC9A-4BBF-A855-5577AADCABD0", ONE)
				.removeAttribute("sid-E7B7467D-887A-45BD-9F9A-D67D6325667B", ONE)
				.addAttribute("sid-5264D7A1-B132-46AD-A7C7-7219BD35BAF4", TWO);
		expectedSequence.addLast()
				.removeAttribute("sid-5264D7A1-B132-46AD-A7C7-7219BD35BAF4", TWO)
				.addAttribute("sid-E012B0C6-4128-4753-B5E6-5F913C020F5B", ONE);
		expectedSequence.addLast()
				.moveAttribute("sid-E012B0C6-4128-4753-B5E6-5F913C020F5B", ONE, "sid-3B11A146-F052-4776-A7E7-311DE9000A09");
		expectedSequence.addLast()
				.removeAttribute("sid-3B11A146-F052-4776-A7E7-311DE9000A09", ONE);
		Assert.assertExpectedFlow(expectedSequence, sequence);
	}

}
