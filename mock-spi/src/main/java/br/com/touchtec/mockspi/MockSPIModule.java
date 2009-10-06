package br.com.touchtec.mockspi;

import com.google.inject.AbstractModule;
import com.google.inject.assistedinject.FactoryProvider;
import com.google.inject.name.Names;

public class MockSPIModule extends AbstractModule {

	// TODO where is this module's unit test?

	@Override
	public void configure() {
		bind(BindingBuilder.class);
		bind(BindingRegistry.Factory.class);
		bind(MockSPI.SwitcherFactory.class).to(SwitcherFactoryImpl.class);
		bind(Binding.Factory.class).to(BindingImpl.Factory.class);
		bindFactory(MockSPIURLStreamHandler.class,
				MockSPIURLStreamHandler.Factory.class);
		bindFactory(MockSPIURLConnection.class,
				MockSPIURLConnection.Factory.class);
		bindConstants();
	}

	private void bindConstants() {
		bind(Integer.class).annotatedWith(Names.named("mockspi.url.port"))
				.toInstance(666);
		bind(String.class).annotatedWith(Names.named("mockspi.url.host"))
				.toInstance("localhost");
		bind(String.class).annotatedWith(Names.named("mockspi.url.protocol"))
				.toInstance("mock-spi");
	}

	private <I, F> void bindFactory(Class<I> impl, Class<F> fact) {
		bind(fact).toProvider(FactoryProvider.newFactory(fact, impl));
	}

}
