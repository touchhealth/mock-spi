package br.com.touchtec.mockspi;

import static com.google.common.base.Preconditions.checkNotNull;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;

public class ContextCLSwitcherThreadRunner implements ThreadRunner {

	private final ThreadRunner runner;
	private final ClassLoader cl;

	@Inject
	public ContextCLSwitcherThreadRunner(ThreadRunner runner,
			@Assisted ClassLoader cl) {
		this.runner = checkNotNull(runner);
		this.cl = checkNotNull(cl);
	}

	@Override
	public void run(Thread t) {
		checkNotNull(t);
		t.setContextClassLoader(cl);
		runner.run(t);
	}

	public static interface Factory {
		public ContextCLSwitcherThreadRunner create(ClassLoader cl);
	}
}
