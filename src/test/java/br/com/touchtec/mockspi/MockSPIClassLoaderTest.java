package br.com.touchtec.mockspi;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.io.Serializable;
import java.net.URL;

import org.junit.Before;
import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import br.com.touchtec.mockspi.Binding;
import br.com.touchtec.mockspi.BindingConverter;
import br.com.touchtec.mockspi.BindingRegistry;
import br.com.touchtec.mockspi.DelegateClassGenerator;
import br.com.touchtec.mockspi.MockSPIClassLoader;
import br.com.touchtec.mockspi.MockSPIURLFactory;

public class MockSPIClassLoaderTest {

	private Answer<Binding<?>> answerBinding(final Class<?> iface) {
		return new Answer<Binding<?>>() {

			@Override
			public Binding<?> answer(InvocationOnMock invocation)
					throws Throwable {
				return AdapterBinding.create(iface);
			}
		};
	}

	private ClassLoader mockLoader;
	private BindingRegistry mockRegistry;
	private BindingConverter mockConverter;
	private MockSPIURLFactory mockFactory;
	private DelegateClassGenerator mockGenerator;

	@Before
	public void before() {
		this.mockLoader = mock(ClassLoader.class);
		this.mockRegistry = mock(BindingRegistry.class);
		this.mockConverter = mock(BindingConverter.class);
		this.mockFactory = mock(MockSPIURLFactory.class);
		this.mockGenerator = mock(DelegateClassGenerator.class);
	}

	private MockSPIClassLoader create() {
		return new MockSPIClassLoader(mockLoader, mockRegistry, mockConverter,
				mockFactory, mockGenerator);
	}

	@Test
	public void shouldNotCallParentGetResourcesIfNameMatches()
			throws IOException {
		String name = "oi";
		when(mockRegistry.searchByResourceName(name)).thenAnswer(
				answerBinding(Runnable.class));
		MockSPIClassLoader loader = create();
		loader.getResources(name);
		verifyZeroInteractions(mockLoader);
	}

	@Test
	public void shouldCallParentGetResourcesIfNameDoesNotMatch()
			throws IOException {
		String name = "oi";
		MockSPIClassLoader loader = create();
		loader.getResources(name);
		verify(mockLoader).getResources(name);
	}

	@Test
	public void getResourcesShouldReturnURLIfNameMatches() throws IOException {
		String name = "oi";
		when(mockRegistry.searchByResourceName(name)).thenAnswer(
				answerBinding(Runnable.class));
		when(mockFactory.createURL(isA(Binding.class))).thenReturn(
				new URL("http://localhost"));
		MockSPIClassLoader loader = create();
		assertNotNull(loader.getResources(name));
	}

	@Test
	public void getResourcesShouldAllowNullName() throws IOException {
		MockSPIClassLoader loader = create();
		loader.getResources(null);
	}

	@Test
	public void shouldNotCallParentLoadClassIfBinaryNameMatches()
			throws ClassNotFoundException {
		String binaryName = "oi";
		when(mockConverter.binaryNameMatches(binaryName)).thenReturn(true);
		when(mockRegistry.searchByHashcode(anyInt())).thenAnswer(
				answerBinding(Serializable.class));
		MockSPIClassLoader loader = create();
		loader.loadClass(binaryName);
		verifyZeroInteractions(mockLoader);
	}

	@Test
	public void shouldCallParentLoadClassIfBinaryNameDoesNotMatch()
			throws ClassNotFoundException {
		// TODO this is the only test that fails if not use delegatecl
		String binaryName = "oi";
		when(mockConverter.binaryNameMatches(binaryName)).thenReturn(false);
		MockSPIClassLoader loader = create();
		loader.loadClass(binaryName);
		verify(mockLoader).loadClass(binaryName);
	}

	@Test
	public void loadClassShouldReturnClassIfBinaryNameMatches()
			throws ClassNotFoundException {
		MockSPIClassLoader loader = create();
		String binaryName = "oi";
		when(mockConverter.binaryNameMatches(binaryName)).thenReturn(true);
		when(mockRegistry.searchByHashcode(anyInt())).thenAnswer(
				answerBinding(Serializable.class));
		when(
				mockGenerator.generateClass(anyString(), isA(Binding.class),
						anyInt(), eq(loader))).thenAnswer(
				Answers.answerClass(String.class));
		assertNotNull(loader.loadClass(binaryName));
	}
}
