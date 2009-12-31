package br.com.touchtec.mockspi;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;

public interface BindingBuilder {

	public abstract <T> BindingBuilder add(Class<T> iface, ImmutableList<T> objs);

	public abstract ImmutableSet<Binding<?>> asSet();

}