package br.com.touchtec.mockspi;

import static com.google.common.base.Preconditions.checkNotNull;

public class BlockerThreadRunner implements ThreadRunner {

	@Override
	public void run(Thread t) {
		checkNotNull(t);
		t.start();
		try {
			// TODO join is final, therefore one cannot use a mock
			t.join();
		} catch (InterruptedException e) {
			// TODO find a way to test this branch
			throw new RuntimeException(e);
		}
	}
}
