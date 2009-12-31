package br.com.touchtec.mockspi;

import static com.google.common.base.Preconditions.checkNotNull;

import com.google.inject.Inject;

public class ExceptionExpectatorBuilder<E extends Exception> {

	private final AdapterThreadFactory adapterFactory;
	private final Class<E> excClass;
	private final MockSPIRunner runner;

	public ExceptionExpectatorBuilder(AdapterThreadFactory adapterFactory,
			Class<E> excClass, MockSPIRunner runner) {
		this.adapterFactory = checkNotNull(adapterFactory);
		this.excClass = checkNotNull(excClass);
		this.runner = checkNotNull(runner);
	}

	public void andRun(Thrower<E> thrower) throws E {
		checkNotNull(thrower);
		try {
			this.runner.run(adapterFactory.create(excClass, thrower));
		} catch (RuntimeWrapperException rwe) {
			rwe.rethrow(excClass);
		}
	}

	public static class Factory {
		private final AdapterThreadFactory factory;

		@Inject
		public Factory(AdapterThreadFactory factory) {
			this.factory = factory;
		}

		public <S extends Exception> ExceptionExpectatorBuilder<S> create(
				Class<S> excClass, MockSPIRunner runner) {
			return new ExceptionExpectatorBuilder<S>(factory, excClass, runner);
		}
	}

}
