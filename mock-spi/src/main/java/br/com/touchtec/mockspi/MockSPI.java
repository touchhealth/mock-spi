package br.com.touchtec.mockspi;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Set;

import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;

public class MockSPI {

	public static interface SwitcherFactory {
		public CtxtClassLoaderSwitcher create(Set<Binding<?>> bindings);
	}

	private final BindingBuilder builder;
	private final SwitcherFactory factory;

	@Inject
	MockSPI(BindingBuilder builder, SwitcherFactory factory) {
		this.builder = checkNotNull(builder);
		this.factory = checkNotNull(factory);
	}

	public <T> MockSPI andBind(Class<T> iface, T... impls) {
		this.builder.add(checkNotNull(iface), checkNotNull(impls));
		return this;
	}

	public void andMock(Runnable runnable) {
		checkNotNull(runnable);
		CtxtClassLoaderSwitcher switcher = factory.create(this.builder.build());
		switcher.switchLoader(runnable);
	}

	public static <T> MockSPI bind(Class<T> iface, T... impls) {
		Injector injector = Guice.createInjector(new MockSPIModule());
		return injector.getInstance(MockSPI.class).andBind(iface, impls);
	}
}
