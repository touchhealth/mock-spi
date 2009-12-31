package br.com.touchtec.mockspi;

import static org.mockito.Matchers.same;
import static org.mockito.Mockito.mock;

import org.junit.Test;

import com.google.inject.TypeLiteral;

public class ContextCLSwitcherThreadRunnerTest extends
		MockeryTestCase<ContextCLSwitcherThreadRunner> {

	@Override
	protected TypeLiteral<ContextCLSwitcherThreadRunner> getType() {
		return new TypeLiteral<ContextCLSwitcherThreadRunner>() {};
	}

	@Test
	public void runnerShouldSetCtxtCL() {
		Thread thread = mock(Thread.class);
		get().run(thread);
		verify(thread).setContextClassLoader(
				same(recall(Annotations.assisted(), ClassLoader.class)));
	}

	@Test
	public void runnerShouldDelegateToOtherRunner() {
		Thread thread = mock(Thread.class);
		get().run(thread);
		verify(ThreadRunner.class).run(same(thread));
	}

}
