package br.com.touchtec.mockspi;

import static org.mockito.Mockito.mock;

import java.io.IOException;
import java.util.concurrent.ThreadFactory;

import org.junit.Test;

import com.google.inject.TypeLiteral;

public class AdapterThreadFactoryTest extends
		MockeryTestCase<AdapterThreadFactory> {

	@Override
	protected TypeLiteral<AdapterThreadFactory> getType() {
		return new TypeLiteral<AdapterThreadFactory>() {};
	}

	@Test
	public void createThrowerShouldCreateAdapter() {
		Thrower<IOException> thrower = Mockito2
				.mock(new TypeLiteral<Thrower<IOException>>() {});
		get().create(IOException.class, thrower);
		verify(ThrowerAdapter.Factory.class).create(thrower, IOException.class);
	}

	@Test
	public void createThrowerShouldCreateThread() {
		Thrower<IOException> thrower = Mockito2
				.mock(new TypeLiteral<Thrower<IOException>>() {});
		ThrowerAdapter<?> runnable = mock(ThrowerAdapter.class);
		doReturn(runnable).when(ThrowerAdapter.Factory.class)
				.<IOException> create(thrower, IOException.class);
		get().create(IOException.class, thrower);
		verify(ThreadFactory.class).newThread(runnable);
	}
}
