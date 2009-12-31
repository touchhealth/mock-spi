package br.com.touchtec.mockspi;

import static org.junit.Assert.assertSame;
import static org.junit.Assert.fail;

import org.junit.Before;
import org.junit.Test;

import br.com.touchtec.mockspi.CtxtClassLoaderSwitcher;

public class CtxtClassLoaderSwitcherTest {

	private static class ContextCLLogger implements Runnable {

		private ClassLoader cl;

		@Override
		public void run() {
			cl = Thread.currentThread().getContextClassLoader();
		}

		public ClassLoader getClassLoader() {
			return cl;
		}

	}

	private ClassLoader loader;

	@Before
	public void before() {
		this.loader = new ClassLoader() {};
	}

	@Test
	public void runnableShouldHaveTheCorrectContextCL() throws Throwable {
		ContextCLLogger logger = new ContextCLLogger();
		CtxtClassLoaderSwitcher switcher = new CtxtClassLoaderSwitcher(loader);
		switcher.switchLoader(logger);
		assertSame(loader, logger.getClassLoader());
	}

	@Test
	public void exceptionsThrownByRunnableShouldBeReThrown() {
		final RuntimeException expected = new RuntimeException("oie");
		CtxtClassLoaderSwitcher switcher = new CtxtClassLoaderSwitcher(loader);
		try {
			switcher.switchLoader(new Runnable() {

				@Override
				public void run() {
					throw expected;
				}
			});
			fail();
		} catch (RuntimeException actual) {
			assertSame(expected, actual);
		} catch (Exception e) {
			fail();
		}
	}

}
