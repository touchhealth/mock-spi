package br.com.touchtec.mockspi;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.concurrent.ThreadFactory;

import com.google.inject.Inject;

public class AdapterThreadFactory {

	private final ThreadFactory threadFactory;
	private final ThrowerAdapter.Factory adapterFactory;

	@Inject
	public AdapterThreadFactory(ThreadFactory threadFactory,
			ThrowerAdapter.Factory adapterFactory) {
		this.threadFactory = checkNotNull(threadFactory);
		this.adapterFactory = checkNotNull(adapterFactory);
	}

	public <E extends Exception> Thread create(Class<E> clazz,
			Thrower<E> thrower) {
		checkNotNull(clazz);
		checkNotNull(thrower);
		return this.create(adapterFactory.create(thrower, clazz));
	}

	@Untested
	public Thread create(Runnable runnable) {
		return threadFactory.newThread(runnable);
	}

}
