package br.com.touchtec.mockspi;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

public class RuntimeWrapperException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	private final Exception exc;
	private final Class<?> clazz;

	public <E extends Exception> RuntimeWrapperException(E exc, Class<E> clazz) {
		this.exc = checkNotNull(exc);
		this.clazz = checkNotNull(clazz);
		checkArgument(clazz.isInstance(exc));
	}

	public <E extends Exception> void rethrow(Class<E> c) throws E {
		if (this.clazz.equals(c)) {
			throw c.cast(this.exc);
		}
	}
}
