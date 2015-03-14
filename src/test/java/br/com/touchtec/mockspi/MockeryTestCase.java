package br.com.touchtec.mockspi;

import java.lang.annotation.Annotation;

import org.junit.Before;
import org.mockito.Mockito;

import com.google.inject.TypeLiteral;

public abstract class MockeryTestCase<S> {

	private Mockery<S> mockery;

	protected abstract TypeLiteral<S> getType();

	@Before
	public void before() {
		mockery = new Mockery.Factory().create(getType());
	}

	public S get() {
		return mockery.get();
	}

	public <T> T recall(Annotation ann, Class<T> class1) {
		return mockery.recall(ann, class1);
	}

	public <T> T recall(Class<T> class1) {
		return mockery.recall(class1);
	}

	// Stubbing

	public static interface StubberHolder {
		public <T> T when(Class<T> clazz);
	}

	public StubberHolder doReturn(final Object obj) {
		return new StubberHolder() {

			@Override
			public <T> T when(Class<T> clazz) {
				return Mockito.doReturn(obj).when(recall(clazz));
			}
		};
	}

	// Verification

	public <T> T verify(T obj) {
		return Mockito.verify(obj);
	}

	public <T> T verify(Class<T> clazz) {
		return Mockito.verify(recall(clazz));
	}

}
