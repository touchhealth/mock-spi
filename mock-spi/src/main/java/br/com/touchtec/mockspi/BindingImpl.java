package br.com.touchtec.mockspi;

import static com.google.common.base.Preconditions.checkElementIndex;
import static com.google.common.base.Preconditions.checkNotNull;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.google.common.collect.AbstractIterator;
import com.google.inject.internal.Lists;

class BindingImpl<T> implements Binding<T> {

	private final Class<T> iface;
	private final List<T> objs;

	BindingImpl(Class<T> iface, List<T> objs) {
		this.iface = checkNotNull(iface);
		this.objs = checkNotNull(objs);
	}

	public T getInstance(int i) {
		checkElementIndex(i, objs.size());
		return this.objs.get(i);
	}

	public Class<T> getInterface() {
		return this.iface;
	}

	public int size() {
		return this.objs.size();
	}

	@Override
	public ClassLoader getInterfaceClassLoader() {
		return this.iface.getClassLoader();
	}

	@Override
	public Iterator<T> iterator() {
		return new AbstractIterator<T>() {

			private int index = 0;

			@Override
			protected T computeNext() {
				if (index < BindingImpl.this.size()) {
					return BindingImpl.this.getInstance(index++);
				}
				return endOfData();
			}
		};
	}

	public static class Factory implements Binding.Factory {

		@Override
		public <E> Binding<E> append(Binding<E> binding, List<E> impls) {
			ArrayList<E> list = Lists.newArrayList(checkNotNull(binding));
			list.addAll(checkNotNull(impls));
			return new BindingImpl<E>(binding.getInterface(), list);
		}

		@Override
		public <E> Binding<E> create(Class<E> iface, List<E> impls) {
			return new BindingImpl<E>(checkNotNull(iface), checkNotNull(impls));
		}

	}

}