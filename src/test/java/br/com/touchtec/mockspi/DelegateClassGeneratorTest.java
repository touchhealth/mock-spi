package br.com.touchtec.mockspi;

import static org.mockito.Matchers.eq;
import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import org.junit.Before;
import org.junit.Test;

import br.com.touchtec.mockspi.ByteArray;
import br.com.touchtec.mockspi.ClassDefiner;
import br.com.touchtec.mockspi.DelegateClassGenerator;

public class DelegateClassGeneratorTest {

	static class ClassLoaderDefiner extends ClassLoader implements ClassDefiner {

		public ClassLoaderDefiner() {
			super(ClassLoaderDefiner.class.getClassLoader());
		}

		@Override
		public Class<?> defineClass(String name, ByteArray ba) {
			byte[] b = ba.getBytes();
			return defineClass(name, b, 0, b.length);
		}

	}

	private ClassDefiner mockDefiner;

	@Before
	public void before() {
		mockDefiner = mock(ClassDefiner.class);
	}

	@Test(expected = IndexOutOfBoundsException.class)
	public void generatClassShouldFailIfIndexIsOutOfBounds() {
		DelegateClassGenerator generator = new DelegateClassGenerator();
		generator.generateClass("oi",
				AdapterBinding.create(TestIface.class, 2), 123, null);
	}

	@Test
	public void generatedClassShouldBeLoadedByGivenClassDefiner() {
		DelegateClassGenerator generator = new DelegateClassGenerator();
		String binaryName = "oi";
		generator.generateClass(binaryName, AdapterBinding.create(
				TestIface.class, 2), 1, mockDefiner);
		verify(mockDefiner).defineClass(eq(binaryName), isA(ByteArray.class));
	}

}
