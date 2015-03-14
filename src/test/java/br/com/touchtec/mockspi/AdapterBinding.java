package br.com.touchtec.mockspi;

import java.util.Iterator;

import br.com.touchtec.mockspi.Binding;

public class AdapterBinding<T> implements Binding<T> {

	private final Class<T> iface;
	private final int size;

	AdapterBinding(Class<T> iface, int size) {
		super();
		this.iface = iface;
		this.size = size;
	}

	@Override
	public T getInstance(int i) {
		return null;
	}

	@Override
	public Class<T> getInterface() {
		return iface;
	}

	@Override
	public int size() {
		return size;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((iface == null) ? 0 : iface.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		AdapterBinding<?> other = (AdapterBinding<?>) obj;
		if (iface == null) {
			if (other.iface != null)
				return false;
		} else if (!iface.equals(other.iface))
			return false;
		return true;
	}

	public static <E> Binding<E> create(Class<E> c) {
		return create(c, 0);
	}

	public static <E> Binding<E> create(Class<E> c, int i) {
		return new AdapterBinding<E>(c, i);
	}

	@Override
	public ClassLoader getInterfaceClassLoader() {
		return this.iface.getClassLoader();
	}

	@Override
	public Iterator<T> iterator() {
		return null;
	}

}
