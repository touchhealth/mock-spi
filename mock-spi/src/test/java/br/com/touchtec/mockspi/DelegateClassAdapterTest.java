package br.com.touchtec.mockspi;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Constructor;

import org.junit.Test;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;

import br.com.touchtec.mockspi.DelegateClassAdapter;

public class DelegateClassAdapterTest {

	static class ClassLoaderDefiner extends ClassLoader {

		public ClassLoaderDefiner() {
			super(Thread.currentThread().getContextClassLoader());
		}

		public Class<?> defineClass(String name, byte[] b) {
			return defineClass(name, b, 0, b.length);
		}

	}

	@SuppressWarnings("unchecked")
	private <T> Class<T> create(Class<T> iface) throws IOException {
		ClassLoader loader = Thread.currentThread().getContextClassLoader();
		InputStream stream = loader.getResourceAsStream(iface.getName()
				.replace('.', '/')
				+ ".class");
		ClassReader reader = new ClassReader(stream);
		ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_MAXS);
		String implName = iface.getName() + "$OIE";
		DelegateClassAdapter adapter = new DelegateClassAdapter(0, implName,
				writer, iface);
		reader.accept(adapter, 0);
		return (Class<T>) new ClassLoaderDefiner().defineClass(implName, writer
				.toByteArray());
	}

	@Test
	public void generatedClassShouldBeLoadable() throws IOException {
		create(EmptyTestIface.class);
	}

	@Test
	public void generatedClassShouldImplementGivenInterface()
			throws IOException {
		Class<EmptyTestIface> impl = create(EmptyTestIface.class);
		assertTrue(EmptyTestIface.class.isAssignableFrom(impl));
	}

	@Test
	public void generatedClassShouldHaveDefaultConstructor()
			throws IOException, SecurityException, NoSuchMethodException {
		Class<EmptyTestIface> impl = create(EmptyTestIface.class);
		impl.getConstructor();
	}

	@Test
	public void generatedClassShouldHaveDelegateConstructor()
			throws IOException, SecurityException, NoSuchMethodException {
		Class<EmptyTestIface> impl = create(EmptyTestIface.class);
		impl.getConstructor(EmptyTestIface.class);
	}

	private <T> T delegate(Class<T> clazz, T target) {
		try {
			Class<?> generatedClass = create(clazz);
			@SuppressWarnings("unchecked")
			Constructor<T> constructor = (Constructor<T>) generatedClass
					.getConstructor(clazz);
			return constructor.newInstance(target);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Test
	public void generatedClassShouldDelegateVoidMethods() throws Exception {
		TestIface mockIface = mock(TestIface.class);
		TestIface delegate = delegate(TestIface.class, mockIface);
		delegate.voidMethod();
		verify(mockIface).voidMethod();
	}

	@Test
	public void generatedClassShouldDelegatePrimitiveMethods() throws Exception {
		TestIface mockIface = mock(TestIface.class);
		TestIface delegate = delegate(TestIface.class, mockIface);
		delegate.intMethod();
		verify(mockIface).intMethod();
	}

	@Test
	public void generatedClassShouldDelegateGenericMethods() throws Exception {
		TestIface mockIface = mock(TestIface.class);
		TestIface delegate = delegate(TestIface.class, mockIface);
		delegate.genericMethod(RuntimeException.class);
		verify(mockIface).genericMethod(RuntimeException.class);
	}

	@Test
	public void generatedClassShouldReturnFromGenericMethods() throws Exception {
		TestIface mockIface = mock(TestIface.class);
		RuntimeException expected = new RuntimeException();
		when(mockIface.genericMethod(RuntimeException.class)).thenReturn(
				expected);
		TestIface delegate = delegate(TestIface.class, mockIface);
		assertSame(expected, delegate.genericMethod(RuntimeException.class));
	}

	@Test(expected = RuntimeException.class)
	public void generatedClassShouldThrowFromGenericMethods() throws Exception {
		TestIface mockIface = mock(TestIface.class);
		when(mockIface.genericMethod(RuntimeException.class)).thenThrow(
				new RuntimeException());
		TestIface delegate = delegate(TestIface.class, mockIface);
		delegate.genericMethod(RuntimeException.class);
	}

	@Test
	public void generatedClassShouldDelegateMultiArgMethods() throws Exception {
		TestIface mockIface = mock(TestIface.class);
		TestIface delegate = delegate(TestIface.class, mockIface);
		delegate.multiArgs(01, "lala", 0.24d);
		verify(mockIface).multiArgs(01, "lala", 0.24d);
	}

	@Test
	public void generatedClassShouldReturnFromMultiArgMethods()
			throws Exception {
		TestIface mockIface = mock(TestIface.class);
		when(mockIface.multiArgs(01, "lala", 0.24d)).thenReturn(123);
		TestIface delegate = delegate(TestIface.class, mockIface);
		assertEquals(123, delegate.multiArgs(01, "lala", 0.24d));
	}

	@Test(expected = NullPointerException.class)
	public void whatIfGeneratedClassGetsANullDelegate() {
		// TODO this is what happens if delegate is null. should be treated at
		// the constructor
		TestIface delegate = delegate(TestIface.class, null);
		delegate.voidMethod();
	}
}
