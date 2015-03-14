package br.com.touchtec.mockspi;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.Arrays;
import java.util.Collections;
import java.util.Set;

import org.junit.Test;

import br.com.touchtec.mockspi.Binding;
import br.com.touchtec.mockspi.BindingRegistry;

import com.google.common.base.Function;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Iterables;

public class BindingRegistryTest {

	private <T> Binding<T> bindingFor(Class<T> iface) {
		return AdapterBinding.create(iface);
	}

	private BindingRegistry empty() {
		BindingRegistry registry = new BindingRegistry.Factory()
				.create(Collections.<Binding<?>> emptySet());
		return registry;
	}

	private BindingRegistry registryFor(Class<?>... ifaces) {
		Set<Binding<?>> set = ImmutableSet.<Binding<?>> builder().addAll(
				Iterables.transform(Arrays.asList(ifaces),
						new Function<Class<?>, Binding<?>>() {

							@Override
							public Binding<?> apply(Class<?> iface) {
								return bindingFor(iface);
							}
						})).build();
		return new BindingRegistry.Factory().create(set);
	}

	@Test
	public void unmatchedResourceNameShouldReturnNull() {
		BindingRegistry registry = empty();
		assertNull(registry.searchByResourceName("asdfasdf"));
	}

	@Test
	public void matchedButUnknownResourceNameShouldReturnNull() {
		BindingRegistry registry = empty();
		assertNull(registry
				.searchByResourceName(BindingRegistry.PREFIX + "Ola"));
	}

	@Test
	public void matchedKnownResourceNameShouldReturnItsBinding() {
		BindingRegistry registry = registryFor(Runnable.class);
		Binding<Runnable> binding = bindingFor(Runnable.class);
		assertEquals(binding, registry
				.searchByResourceName(BindingRegistry.PREFIX
						+ "java.lang.Runnable"));
	}

	@Test
	public void unknownHashcodeShouldReturnNull() {
		BindingRegistry registry = empty();
		assertNull(registry.searchByHashcode(1234));
	}

	@Test
	public void knownHashcodeShouldReturnItsBinding() {
		BindingRegistry registry = registryFor(Runnable.class);
		Binding<Runnable> binding = bindingFor(Runnable.class);
		assertEquals(binding, registry.searchByHashcode(binding.hashCode()));
	}

	@Test
	public void unknownInterfaceShouldReturnNull() {
		BindingRegistry registry = empty();
		assertNull(registry.searchByInterface(Runnable.class));
	}

	@Test
	public void knownInterfaceShouldReturnItsBinding() {
		BindingRegistry registry = registryFor(Runnable.class);
		Binding<Runnable> binding = bindingFor(Runnable.class);
		assertEquals(binding, registry.searchByInterface(Runnable.class));
	}
}
