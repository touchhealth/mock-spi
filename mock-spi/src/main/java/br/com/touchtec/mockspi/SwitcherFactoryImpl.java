package br.com.touchtec.mockspi;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Set;


import br.com.touchtec.mockspi.MockSPI.SwitcherFactory;
import br.com.touchtec.mockspi.MockSPIClassLoader.Factory;

import com.google.inject.Inject;

public class SwitcherFactoryImpl implements SwitcherFactory {

	private final MockSPIClassLoader.Factory factory;

	@Inject
	public SwitcherFactoryImpl(Factory factory) {
		this.factory = checkNotNull(factory);
	}

	@Override
	public CtxtClassLoaderSwitcher create(Set<Binding<?>> bindings) {
		return new CtxtClassLoaderSwitcher(factory
				.create(checkNotNull(bindings)));
	}

}
