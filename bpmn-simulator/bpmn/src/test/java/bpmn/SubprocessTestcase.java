package bpmn;

import org.junit.Test;

import com.googlecode.bpmn_simulator.animation.input.Definition;
import com.googlecode.bpmn_simulator.test.logic.Assert;
import com.googlecode.bpmn_simulator.test.logic.AttributedElementsState;
import com.googlecode.bpmn_simulator.test.logic.StateSequence;
import com.googlecode.bpmn_simulator.test.logic.StaticAttributedElementsState;
import com.googlecode.bpmn_simulator.test.logic.StaticStateSequence;

public class SubprocessTestcase
		extends BPMNTestcase {

	public static final String START = "sid-9AFB33B7-6CBC-4AAD-A5C7-F754280AF7DF";
	public static final String A = "sid-94255DF9-0239-4918-BF85-291D35EB05CC";
	public static final String START1 = "sid-E5F38179-AD45-4018-A8F9-534DFFDEB3C7";
	public static final String B = "sid-E2F17951-601C-4235-A90E-1956E1CB6288";
	public static final String START2 = "sid-9F86D345-7498-4EA4-B5D5-2B46746CFA8A";
	public static final String C = "sid-8648F5DC-CC0C-4D13-AC6E-0647949CCB23";
	public static final String END2 = "sid-CBEF3967-B9E3-4D0F-B3FA-8BECCDEA580E";
	public static final String D = "sid-B563234F-715B-4896-AC1A-77D991176BBB";
	public static final String END1 = "sid-86055DEA-BA92-4B0A-83C4-391143942D87";
	public static final String E = "sid-BBEE50FF-5A8E-49E2-A2C5-CDCF2BDA7DD6";
	public static final String END = "sid-FB5CB91A-9A02-4958-B33F-228031DE85B6";

	@Test
	public void test() {
		final Definition<?> definition = loadDefinition(file("subprocess.bpmn"));
		final StaticStateSequence<StaticAttributedElementsState<String, Integer>> expectedSequence = beginSequence(definition);
		expectedSequence.getLast().addAttribute(START, ONE);
		expectedSequence.addLast().moveAttribute(START, ONE, A);
		expectedSequence.addLast().moveAttribute(A, ONE, START1);
		expectedSequence.addLast().moveAttribute(START1, ONE, B);
		expectedSequence.addLast().moveAttribute(B, ONE, START2);
		expectedSequence.addLast().moveAttribute(START2, ONE, C);
		expectedSequence.addLast().moveAttribute(C, ONE, END2);
		expectedSequence.addLast().moveAttribute(END2, ONE, D);
		expectedSequence.addLast().moveAttribute(D, ONE, END1);
		expectedSequence.addLast().moveAttribute(END1, ONE, E);
		expectedSequence.addLast().moveAttribute(E, ONE, END);
		expectedSequence.addLast().removeAttribute(END, ONE);
		final StateSequence<AttributedElementsState<String, Integer>> sequence = start(definition, START);
		Assert.assertExpectedFlow(expectedSequence, sequence);
	}

}
