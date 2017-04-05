package bpmn;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;

import org.junit.Assert;

import com.googlecode.bpmn_simulator.animation.element.logical.LogicalFlowElement;
import com.googlecode.bpmn_simulator.animation.input.Definition;
import com.googlecode.bpmn_simulator.animation.token.TokenFlow;
import com.googlecode.bpmn_simulator.bpmn.model.BPMNDefinition;
import com.googlecode.bpmn_simulator.bpmn.model.Elements;
import com.googlecode.bpmn_simulator.bpmn.model.core.foundation.BaseElement;
import com.googlecode.bpmn_simulator.test.logic.AnimationStateSequence;
import com.googlecode.bpmn_simulator.test.logic.AttributedElementsState;
import com.googlecode.bpmn_simulator.test.logic.StateSequence;
import com.googlecode.bpmn_simulator.test.logic.StaticAttributedElementsState;
import com.googlecode.bpmn_simulator.test.logic.StaticStateSequence;

abstract class BPMNTestcase {

	protected static final Integer ONE = Integer.valueOf(1);
	protected static final Integer TWO = Integer.valueOf(2);

	protected File file(final String filename) {
		final URL url = Thread.currentThread().getContextClassLoader().getResource(filename);
		Assert.assertNotNull("resource could not be found", url);
		return new File(url.getFile());
	}

	protected static Definition<?> loadDefinition(final File file) {
		final Definition<?> definition = new BPMNDefinition<>();
		try (final InputStream input = new FileInputStream(file)) {
			definition.load(input);
			return definition;
		} catch (IOException e) {
			Assert.fail(e.toString());
		}
		return null;
	}

	protected static LogicalFlowElement getStartElement(final Definition<?> definition, final String start) {
		final LogicalFlowElement flowElement = Elements.findById(definition.getInstantiatingElements(), start);
		Assert.assertNotNull(flowElement);
		return flowElement;
	}

	protected static StateSequence<AttributedElementsState<String, Integer>> start(final Definition<?> definition, final String start) {
		final StateSequence<AttributedElementsState<String, Integer>> stateSequence
				= new BPMNStateSequence(definition, getStartElement(definition, start));
		return stateSequence;
	}

	private static <T extends LogicalFlowElement> Collection<String> asIds(final Collection<T> elements) {
		if (elements == null) {
			return null;
		}
		final Collection<String> ids = new ArrayList<>();
		for (final T element : elements) {
			if (element instanceof BaseElement) {
				ids.add(((BaseElement) element).getId());
			} else {
				Assert.fail();
			}
		}
		return ids;
	}

	protected static StaticStateSequence<StaticAttributedElementsState<String, Integer>> beginSequence(final Definition<?> definition) {
		return new StaticStateSequence<>(new StaticAttributedElementsState<String, Integer>(asIds(definition.getFlowElements())));
	}

	private static class BPMNStateSequence
			extends AnimationStateSequence {

		public BPMNStateSequence(final Definition<?> definition, final TokenFlow startElement) {
			super(definition, startElement);
		}

		@Override
		protected String getElementName(final LogicalFlowElement flowElement) {
			if (flowElement instanceof BaseElement) {
				return ((BaseElement) flowElement).getId();
			}
			return null;
		}

	}

}
