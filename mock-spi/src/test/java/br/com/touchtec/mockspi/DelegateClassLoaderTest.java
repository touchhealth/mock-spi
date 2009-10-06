package br.com.touchtec.mockspi;

import static org.junit.Assert.assertSame;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Enumeration;

import org.junit.Before;
import org.junit.Test;

import br.com.touchtec.mockspi.DelegateClassLoader;
import br.com.touchtec.mockspi.Enumerations;

public class DelegateClassLoaderTest {

	private ClassLoader delegate;
	private DelegateClassLoader loader;

	@Before
	public void before() {
		delegate = mock(ClassLoader.class);
		loader = new DelegateClassLoader(delegate);
	}

	@Test
	public void setDefaultAssertionStatusShouldDelegate() {
		loader.setDefaultAssertionStatus(true);
		verify(delegate).setDefaultAssertionStatus(true);
	}

	@Test
	public void clearAssertionStatusShouldDelegate() {
		loader.clearAssertionStatus();
		verify(delegate).clearAssertionStatus();
	}

	@Test
	public void getResourceStringShouldDelegate() throws MalformedURLException {
		String name = "nomeBizarro";
		URL url = new URL("http://oie");
		when(loader.getResource(name)).thenReturn(url);
		assertSame(url, loader.getResource(name));
		verify(delegate).getResource(eq(name));
	}

	@Test
	public void getResourceAsStreamStringShouldDelegate() {
		String name = "nomeBizarro";
		ByteArrayInputStream stream = new ByteArrayInputStream("oie".getBytes());
		when(loader.getResourceAsStream(name)).thenReturn(stream);
		assertSame(stream, loader.getResourceAsStream(name));
		verify(delegate).getResourceAsStream(eq(name));
	}

	@Test
	public void getResourcesStringShouldDelegate() throws IOException {
		String name = "nomeBizarro";
		URL url = new URL("http://oie");
		Enumeration<URL> enumeration = Enumerations.newEnumeration(url);
		when(loader.getResources(name)).thenReturn(enumeration);
		assertSame(enumeration, loader.getResources(name));
		verify(delegate).getResources(eq(name));
	}

	@Test
	public void loadClassStringShouldDelegate() throws ClassNotFoundException {
		String name = "nomeBizarro";
		when(loader.loadClass(name)).thenAnswer(
				Answers.answerClass(String.class));
		assertSame(String.class, loader.loadClass(name));
		verify(delegate).loadClass(eq(name));
	}

	@Test
	public void setClassAssertionStatusStringBooleanShouldDelegate() {
		String className = "oie";
		boolean enabled = true;
		loader.setClassAssertionStatus(className, enabled);
		verify(delegate).setClassAssertionStatus(className, enabled);
	}

	@Test
	public void setPackageAssertionStatusStringBooleanShouldDelegate() {
		String packageName = "asdf";
		boolean enabled = true;
		loader.setPackageAssertionStatus(packageName, enabled);
		verify(delegate).setPackageAssertionStatus(packageName, enabled);
	}

}
