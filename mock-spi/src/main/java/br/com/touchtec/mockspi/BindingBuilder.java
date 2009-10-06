package br.com.touchtec.mockspi;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.google.inject.Inject;

public class BindingBuilder {

	private final BiMap<Class<?>, Binding<?>> bindings;
	private final Binding.Factory factory;

	@Inject
	public BindingBuilder(Binding.Factory factory) {
		this.factory = checkNotNull(factory);
		this.bindings = HashBiMap.create();
	}

	public <T> BindingBuilder add(Class<T> iface, T... objs) {
		// TODO what if one impl is null?
		checkNotNull(iface);
		checkNotNull(objs);
		List<T> asList = Arrays.asList(objs);
		if (bindings.containsKey(iface)) {
			// this is guaranteed not to pollute the heap
			@SuppressWarnings("unchecked")
			Binding<T> binding = (Binding<T>) bindings.get(iface);
			this.bindings.put(iface, factory.append(binding, asList));
		} else {
			this.bindings.put(iface, factory.create(iface, asList));
		}
		return this;
	}

	public Set<Binding<?>> build() {
		return this.bindings.values();
	}
}