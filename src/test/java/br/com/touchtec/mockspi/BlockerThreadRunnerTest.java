package br.com.touchtec.mockspi;

import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;

import org.junit.Ignore;
import org.junit.Test;

import com.google.inject.TypeLiteral;

public class BlockerThreadRunnerTest extends
		MockeryTestCase<BlockerThreadRunner> {

	@Override
	protected TypeLiteral<BlockerThreadRunner> getType() {
		return new TypeLiteral<BlockerThreadRunner>() {};
	}

	@Test
	public void runShouldStartThread() {
		Thread thread = mock(Thread.class);
		get().run(thread);
		verify(thread).start();
	}

	@Ignore
	@Test
	public void runShouldJoinAfterStartThread() throws InterruptedException {
		fail();
	}

	@Ignore
	@Test
	public void runShouldWrapIEinRE() throws InterruptedException {
		fail();
	}

}
