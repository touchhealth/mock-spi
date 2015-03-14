package br.com.touchtec.mockspi;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.concurrent.ThreadFactory;

import com.google.common.collect.ImmutableList;
import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;

public class MockSPI {

	private final BindingBuilder builder;
	private final ExceptionExpectatorBuilder.Factory expectatorFactory;
	private final MockSPIRunner.Factory factory;
	private final ThreadFactory threadFactory;

	@Inject
	MockSPI(BindingBuilder builder,
			ExceptionExpectatorBuilder.Factory expectatorFactory,
			MockSPIRunner.Factory factory, ThreadFactory threadFactory) {
		this.builder = checkNotNull(builder);
		this.expectatorFactory = checkNotNull(expectatorFactory);
		this.factory = checkNotNull(factory);
		this.threadFactory = checkNotNull(threadFactory);
	}

	@Untested
	public <T> MockSPI andBind(Class<T> iface, T... impls) {
		checkNotNull(impls);
		this.builder.add(checkNotNull(iface), ImmutableList.of(impls));
		return this;
	}

	@Untested
	public void andMock(Runnable runnable) {
		checkNotNull(runnable);
		MockSPIRunner spiRunner = this.factory.create(builder.asSet());
		spiRunner.run(threadFactory.newThread(runnable));
	}

	public <E extends Exception> ExceptionExpectatorBuilder<E> andExpect(
			Class<E> excClass) {
		checkNotNull(excClass);
		MockSPIRunner spiRunner = this.factory.create(builder.asSet());
		return expectatorFactory.create(excClass, spiRunner);
	}

	@Untested
	public static <T> MockSPI bind(Class<T> iface, T... impls) {
		Injector injector = Guice.createInjector(new MockSPIModule());
		return injector.getInstance(MockSPI.class).andBind(iface, impls);
	}

}
