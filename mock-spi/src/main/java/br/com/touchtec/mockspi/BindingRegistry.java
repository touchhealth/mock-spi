package br.com.touchtec.mockspi;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Map;
import java.util.Set;

import com.google.common.base.Function;
import com.google.common.collect.Maps;

public class BindingRegistry {

	public static final String PREFIX = "META-INF/services/";

	static class ToResourceName implements Function<Binding<?>, String> {

		@Override
		public String apply(Binding<?> binding) {
			return PREFIX + checkNotNull(binding).getInterface().getName();
		}

	}

	static class ToHashcode implements Function<Binding<?>, Integer> {

		@Override
		public Integer apply(Binding<?> binding) {
			return checkNotNull(binding).hashCode();
		}

	}

	static class ToInterface implements Function<Binding<?>, Class<?>> {

		@Override
		public Class<?> apply(Binding<?> binding) {
			return checkNotNull(binding).getInterface();
		}

	}

	private final Map<String, Binding<?>> fromResourceName;
	private final Map<Integer, Binding<?>> fromHashcode;
	private final Map<Class<?>, Binding<?>> fromInterface;

	BindingRegistry(Map<String, Binding<?>> fromResourceName,
			Map<Integer, Binding<?>> fromHashcode,
			Map<Class<?>, Binding<?>> fromInterface) {
		super();
		this.fromResourceName = checkNotNull(fromResourceName);
		this.fromHashcode = checkNotNull(fromHashcode);
		this.fromInterface = checkNotNull(fromInterface);
	}

	public Binding<?> searchByResourceName(String name) {
		checkNotNull(name);
		return fromResourceName.get(name);
	}

	public Binding<?> searchByHashcode(int hashcode) {
		return fromHashcode.get(hashcode);
	}

	public <T> Binding<T> searchByInterface(Class<T> iface) {
		checkNotNull(iface);
		@SuppressWarnings("unchecked")
		Binding<T> binding = (Binding<T>) fromInterface.get(iface);
		return binding;
	}

	public static class Factory {

		public BindingRegistry create(Set<Binding<?>> bindings) {
			checkNotNull(bindings);
			Map<String, Binding<?>> fromResourceName = Maps.uniqueIndex(
					bindings, new ToResourceName());
			Map<Integer, Binding<?>> fromHashcode = Maps.uniqueIndex(bindings,
					new ToHashcode());
			Map<Class<?>, Binding<?>> fromInterface = Maps.uniqueIndex(
					bindings, new ToInterface());
			return new BindingRegistry(fromResourceName, fromHashcode,
					fromInterface);
		}

	}

}
