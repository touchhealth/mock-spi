package br.com.touchtec.mockspi;

import java.lang.Thread.UncaughtExceptionHandler;

import com.google.inject.Inject;

public class ExceptionAwareThreadRunner implements ThreadRunner {

	private static class Handler implements UncaughtExceptionHandler {
		private RuntimeException exc;

		@Override
		public void uncaughtException(Thread t, Throwable e) {
			if (e instanceof RuntimeException) {
				exc = (RuntimeException) e;
			}
		}

		public void rethrow() {
			if (exc != null) {
				throw exc;
			}
		}
	}

	private final BlockerThreadRunner runner;

	@Inject
	public ExceptionAwareThreadRunner(BlockerThreadRunner runner) {
		this.runner = runner;
	}

	@Override
	public void run(Thread t) {
		Handler eh = new Handler();
		t.setUncaughtExceptionHandler(eh);
		runner.run(t);
		eh.rethrow();
	}

}
