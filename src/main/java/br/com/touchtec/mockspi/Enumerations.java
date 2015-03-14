package br.com.touchtec.mockspi;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Arrays;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * @author Rodrigo Couto
 * @since 2.1
 */
public class Enumerations {

	public static <T> Enumeration<T> newEnumeration(final Iterator<T> iterator) {
		checkNotNull(iterator);
		return new Enumeration<T>() {

			@Override
			public boolean hasMoreElements() {
				return iterator.hasNext();
			}

			@Override
			public T nextElement() {
				if (!iterator.hasNext()) {
					throw new NoSuchElementException();
				}
				return iterator.next();
			}

		};
	}

	public static <T> Enumeration<T> newEnumeration(final Iterable<T> iterable) {
		return newEnumeration(iterable.iterator());
	}

	public static <T> Enumeration<T> newEnumeration(final T... itens) {
		return newEnumeration(Arrays.asList(itens));
	}

}
