package bpmn;

import org.junit.Test;

import com.googlecode.bpmn_simulator.animation.input.Definition;
import com.googlecode.bpmn_simulator.test.logic.Assert;
import com.googlecode.bpmn_simulator.test.logic.AttributedElementsState;
import com.googlecode.bpmn_simulator.test.logic.StateSequence;
import com.googlecode.bpmn_simulator.test.logic.StaticAttributedElementsState;
import com.googlecode.bpmn_simulator.test.logic.StaticStateSequence;

public class GatewayParallelTestcase
		extends BPMNTestcase {

	@Test
	public void testBranching() {
		final Definition<?> definition = loadDefinition(file("gateway_parallel_branching.bpmn"));
		final StaticStateSequence<StaticAttributedElementsState<String, Integer>> expectedSequence = beginSequence(definition);
		expectedSequence.getLast()
			.addAttribute("sid-02B82C93-B2EA-440B-8FD7-069B6350BB41", ONE);
		expectedSequence.addLast()
			.moveAttribute("sid-02B82C93-B2EA-440B-8FD7-069B6350BB41", ONE, "sid-3C928CD1-CDD0-4739-8F5C-1F3B194B3066");
		expectedSequence.addLast()
		.moveAttribute("sid-3C928CD1-CDD0-4739-8F5C-1F3B194B3066", ONE, "sid-09AE6099-3209-4D8A-8D50-D26ECC31BFCB");
		expectedSequence.addLast()
			.removeAttribute("sid-09AE6099-3209-4D8A-8D50-D26ECC31BFCB", ONE)
			.addAttribute("sid-FBEABEC5-A684-44CE-8310-780B5AFD6E9D", ONE)
			.addAttribute("sid-475F4BFB-4E58-463F-B9CC-363A5543912F", ONE);
		expectedSequence.addLast()
			.removeAttribute("sid-FBEABEC5-A684-44CE-8310-780B5AFD6E9D", ONE)
			.removeAttribute("sid-475F4BFB-4E58-463F-B9CC-363A5543912F", ONE)
			.addAttribute("sid-853FE668-0107-4CFB-96BE-46CD574D5B4B", TWO);
		expectedSequence.addLast()
			.removeAttribute("sid-853FE668-0107-4CFB-96BE-46CD574D5B4B", TWO);
		final StateSequence<AttributedElementsState<String, Integer>> sequence = start(definition, "sid-02B82C93-B2EA-440B-8FD7-069B6350BB41");
		Assert.assertExpectedFlow(expectedSequence, sequence);
	}

	@Test
	public void testMerging() {
		final Definition<?> definition = loadDefinition(file("gateway_parallel_merging.bpmn"));
		final StaticStateSequence<StaticAttributedElementsState<String, Integer>> expectedSequence = beginSequence(definition);
		final StateSequence<AttributedElementsState<String, Integer>> sequence = start(definition, "sid-49E99A54-AFE9-4F6F-A18B-B78796146AC4");
		expectedSequence.getLast()
				.addAttribute("sid-49E99A54-AFE9-4F6F-A18B-B78796146AC4", ONE);
		expectedSequence.addLast()
			.moveAttribute("sid-49E99A54-AFE9-4F6F-A18B-B78796146AC4", ONE,
					"sid-48CB7DEA-38BB-423B-8683-5BBB23A79CF0", "sid-BCD54062-0720-4989-897A-2C2B6F936370");
		expectedSequence.addLast()
			.removeAttribute("sid-48CB7DEA-38BB-423B-8683-5BBB23A79CF0", ONE)
			.removeAttribute("sid-BCD54062-0720-4989-897A-2C2B6F936370", ONE)
			.addAttribute("sid-C279BA58-597C-4168-B92F-FB42670D5449", TWO);
		expectedSequence.addLast()
			.removeAttribute("sid-C279BA58-597C-4168-B92F-FB42670D5449", TWO)
			.addAttribute("sid-7E47B750-3E32-49DC-BCBE-77DB03429F09", ONE);
		expectedSequence.addLast()
			.moveAttribute("sid-7E47B750-3E32-49DC-BCBE-77DB03429F09", ONE, "sid-0AF4A6C8-814F-42AE-90CF-79C3442F8B75");
		expectedSequence.addLast()
			.removeAttribute("sid-0AF4A6C8-814F-42AE-90CF-79C3442F8B75", ONE);
		Assert.assertExpectedFlow(expectedSequence, sequence);
	}

}
