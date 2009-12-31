package br.com.touchtec.mockspi;

import static com.google.common.base.Preconditions.checkNotNull;
import br.com.touchtec.mockspi.MockSPIClassLoader.Factory;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.google.inject.Inject;

@Untested
public class BindingBuilderMockSPICLFactory implements BindingBuilder {

	private final BindingBuilderImpl builder;
	private final MockSPIClassLoader.Factory factory;

	@Inject
	public BindingBuilderMockSPICLFactory(BindingBuilderImpl builder,
			Factory factory) {
		this.builder = builder;
		this.factory = factory;
	}

	@Override
	public <T> BindingBuilder add(Class<T> iface, ImmutableList<T> objs) {
		checkNotNull(iface);
		checkNotNull(objs);
		return builder.add(iface, objs);
	}

	public MockSPIClassLoader build() {
		return factory.create(builder.build());
	}

	@Override
	public ImmutableSet<Binding<?>> asSet() {
		return builder.asSet();
	}
}
