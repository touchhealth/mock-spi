package br.com.touchtec.mockspi;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

import java.lang.Thread.UncaughtExceptionHandler;

import javax.annotation.Nullable;

import com.google.inject.Inject;

public class CtxtClassLoaderSwitcher {

	private final class RuntimeExceptionLogger
			implements
				UncaughtExceptionHandler {
		private RuntimeException throwable;

		@Override
		public void uncaughtException(Thread t, Throwable e) {
			checkArgument(e instanceof RuntimeException);
			throwable = (RuntimeException) e;
		}

		public RuntimeException getThrowable() {
			return throwable;
		}
	}

	private final ClassLoader loader;

	@Inject
	public CtxtClassLoaderSwitcher(ClassLoader loader) {
		this.loader = checkNotNull(loader);
	}

	private void throwIfNotNull(@Nullable RuntimeException t) {
		if (t != null) {
			throw t;
		}
	}

	public void switchLoader(Runnable runnable) {
		checkNotNull(runnable);
		Thread thread = new Thread(runnable);
		thread.setContextClassLoader(loader);
		RuntimeExceptionLogger logger = new RuntimeExceptionLogger();
		thread.setUncaughtExceptionHandler(logger);
		thread.start();
		try {
			thread.join();
		} catch (InterruptedException e) {
			// TODO find a way to test if this catch will ever happen
			throw new RuntimeException(e);
		}
		throwIfNotNull(logger.getThrowable());
	}

}
