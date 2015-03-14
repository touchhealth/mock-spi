package br.com.touchtec.mockspi;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Arrays;
import java.util.Set;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.google.inject.Inject;

public class BindingBuilderImpl implements BindingBuilder {

	private final BiMap<Class<?>, Binding<?>> bindings;
	private final Binding.Factory factory;

	@Inject
	public BindingBuilderImpl(Binding.Factory factory) {
		this.factory = checkNotNull(factory);
		this.bindings = HashBiMap.create();
	}

	public <T> BindingBuilder add(Class<T> iface, T... objs) {
		// TODO what if one impl is null?
		checkNotNull(iface);
		checkNotNull(objs);
		ImmutableList<T> asList = ImmutableList.copyOf(Arrays.asList(objs));
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

	@Override
	public <T> BindingBuilder add(Class<T> iface, ImmutableList<T> objs) {
		// TODO what if one impl is null?
		checkNotNull(iface);
		checkNotNull(objs);
		if (bindings.containsKey(iface)) {
			// this is guaranteed not to pollute the heap
			@SuppressWarnings("unchecked")
			Binding<T> binding = (Binding<T>) bindings.get(iface);
			this.bindings.put(iface, factory.append(binding, objs));
		} else {
			this.bindings.put(iface, factory.create(iface, objs));
		}
		return this;
	}

	@Override
	public ImmutableSet<Binding<?>> asSet() {
		return ImmutableSet.copyOf(this.bindings.values());
	}
}