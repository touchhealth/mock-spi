package br.com.touchtec.mockspi;

import com.google.common.collect.ImmutableSet;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;

public class MockSPIRunner {

	private final ContextCLSwitcherThreadRunner.Factory runnerFactory;
	private final MockSPIClassLoader.Factory factory;
	private final ImmutableSet<Binding<?>> bindings;

	@Inject
	public MockSPIRunner(ContextCLSwitcherThreadRunner.Factory runnerFactory,
			MockSPIClassLoader.Factory factory,
			@Assisted ImmutableSet<Binding<?>> bindings) {
		this.runnerFactory = runnerFactory;
		this.factory = factory;
		this.bindings = bindings;
	}

	public void run(Thread t) {
		MockSPIClassLoader loader = factory.create(bindings);
		ContextCLSwitcherThreadRunner runner = runnerFactory.create(loader);
		runner.run(t);
	}

	public static interface Factory {
		public MockSPIRunner create(ImmutableSet<Binding<?>> asSet);
	}

}
