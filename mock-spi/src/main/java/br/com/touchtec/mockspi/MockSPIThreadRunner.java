package br.com.touchtec.mockspi;

import static com.google.common.base.Preconditions.checkNotNull;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.google.inject.Inject;

public class MockSPIThreadRunner implements BindingBuilder, ThreadRunner {

	private final BindingBuilderMockSPICLFactory builderFactory;
	private final ContextCLSwitcherThreadRunner.Factory runnerFactory;

	@Inject
	public MockSPIThreadRunner(BindingBuilderMockSPICLFactory builderFactory,
			ContextCLSwitcherThreadRunner.Factory runnerFactory) {
		this.builderFactory = checkNotNull(builderFactory);
		this.runnerFactory = checkNotNull(runnerFactory);
	}

	@Untested
	@Override
	public <T> BindingBuilder add(Class<T> iface, ImmutableList<T> objs) {
		checkNotNull(iface);
		checkNotNull(objs);
		return builderFactory.add(iface, objs);
	}

	@Untested
	@Override
	public void run(Thread t) {
		checkNotNull(t);
		MockSPIClassLoader loader = builderFactory.build();
		ThreadRunner runner = runnerFactory.create(loader);
		runner.run(t);
	}

	@Override
	public ImmutableSet<Binding<?>> asSet() {
		return this.builderFactory.asSet();
	}

}
