package br.com.touchtec.mockspi;

import java.util.List;

public interface Binding<T> extends Iterable<T> {

	public Class<T> getInterface();

	public int size();

	public T getInstance(int i);

	public ClassLoader getInterfaceClassLoader();

	static interface Factory {
		<E> Binding<E> create(Class<E> iface, List<E> impls);

		<E> Binding<E> append(Binding<E> binding, List<E> impls);
	}
}