package br.com.touchtec.mockspi;

import static com.google.common.base.Preconditions.checkNotNull;

import java.io.IOException;
import java.net.URL;
import java.util.Enumeration;
import java.util.ServiceLoader;
import java.util.Set;

import javax.annotation.Nullable;

import com.google.inject.Inject;

/**
 * This is not a general purpose {@link ClassLoader}. It overrides only the
 * needed methods to mock the behavior of {@link ServiceLoader}. One should not
 * expect coherence between {@link #getResource(String)} and
 * {@link #getResources(String)}, for instance.
 * 
 * @author Rodrigo Couto
 * 
 */
public class MockSPIClassLoader extends DelegateClassLoader
		implements
			ClassDefiner {
	// TODO only keeping delegatecl because of weird mockito behaviour

	private final BindingRegistry registry;
	private final BindingConverter converter;
	private final MockSPIURLFactory factory;
	private final DelegateClassGenerator generator;

	public MockSPIClassLoader(ClassLoader loader, BindingRegistry registry,
			BindingConverter converter, MockSPIURLFactory factory,
			DelegateClassGenerator generator) {
		super(loader);
		this.registry = checkNotNull(registry);
		this.converter = checkNotNull(converter);
		this.factory = checkNotNull(factory);
		this.generator = checkNotNull(generator);
	}

	/**
	 * This method purposefully breaks the contract of the
	 * {@link ClassLoader#getResources(String)} method. It will not call the
	 * parent method before searching.
	 * 
	 * @param name
	 *            {@link ClassLoader#getResources(String)} does not specifies
	 *            non-nullability
	 * @return if <code>name</code> is a known resource name, returns a URL to
	 *         the binding SPI file. Otherwise, calls super.
	 * @throws IOException
	 */
	@Override
	public Enumeration<URL> getResources(@Nullable String name)
			throws IOException {
		Binding<?> binding = registry.searchByResourceName(name);
		if (binding != null) {
			return Enumerations.newEnumeration(factory.createURL(binding));
		}
		return super.getResources(name);
	}

	/**
	 * This method purposefully breaks the contract of the
	 * {@link ClassLoader#loadClass(String)} method. It will not call the parent
	 * method before searching.
	 * 
	 * @param binaryName
	 *            {@link ClassLoader#loadClass(String)} does not specifies
	 *            non-nullability
	 * @return if <code>name</code> is a known binary name, returns a class
	 *         representing <i>i</i>-th implementation of the binding SPI file.
	 *         The binary name should encode the binding hashcode and <i>i</i>.
	 *         Otherwise, calls super.
	 */
	@Override
	public Class<?> loadClass(@Nullable String binaryName)
			throws ClassNotFoundException {
		if (converter.binaryNameMatches(binaryName)) {
			int hashcode = converter.binaryNameToHashcode(binaryName);
			int index = converter.binaryNameToIndex(binaryName);
			Binding<?> binding = registry.searchByHashcode(hashcode);
			return generator.generateClass(binaryName, binding, index, this);
		}
		return super.loadClass(binaryName);
	}

	private <T> Binding<T> getBinding(Class<T> clazz) {
		assert (clazz != null);
		return this.registry.searchByInterface(clazz);
	}

	public static <T> T getInstance(Class<T> clazz, int index) {
		MockSPIClassLoader loader = (MockSPIClassLoader) Thread.currentThread()
				.getContextClassLoader();
		Binding<T> binding = loader.getBinding(checkNotNull(clazz));
		return binding.getInstance(index);
	}

	public static class Factory {

		private final BindingRegistry.Factory factory;
		private final BindingConverter converter;
		private final MockSPIURLFactory urlFactory;
		private final DelegateClassGenerator generator;

		@Inject
		public Factory(BindingRegistry.Factory factory,
				BindingConverter converter, MockSPIURLFactory urlFactory,
				DelegateClassGenerator generator) {
			this.factory = checkNotNull(factory);
			this.converter = checkNotNull(converter);
			this.urlFactory = checkNotNull(urlFactory);
			this.generator = checkNotNull(generator);
		}

		public MockSPIClassLoader create(Set<Binding<?>> bindings) {
			return new MockSPIClassLoader(Thread.currentThread()
					.getContextClassLoader(), factory
					.create(checkNotNull(bindings)), converter, urlFactory,
					generator);
		}

	}

	@Override
	public Class<?> defineClass(String name, ByteArray ba) {
		byte[] b = checkNotNull(ba).getBytes();
		return defineClass(checkNotNull(name), b, 0, b.length);
	}
}
