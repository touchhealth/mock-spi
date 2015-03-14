package br.com.touchtec.mockspi;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Test;

import br.com.touchtec.mockspi.Binding;
import br.com.touchtec.mockspi.BindingImpl;
import br.com.touchtec.mockspi.BindingImpl.Factory;

import com.google.common.collect.Iterables;
import com.google.common.collect.Iterators;

public class BindingImplTest {

	@Test
	public void bindingImplShouldReturnCLThatLoadedTheInterface() {
		Factory factory = new BindingImpl.Factory();
		Binding<String> binding = factory.create(String.class, Collections
				.<String> emptyList());
		assertSame(String.class.getClassLoader(), binding
				.getInterfaceClassLoader());
	}

	@Test
	public void factoryShouldCreateRight() {
		Factory factory = new BindingImpl.Factory();
		List<String> list = Arrays.asList("oi", "mamae");
		Binding<String> binding = factory.create(String.class, list);
		assertEquals(String.class, binding.getInterface());
		assertTrue(Iterables.elementsEqual(list, binding));
	}

	@Test
	public void factoryShouldAppendImpls() {
		@SuppressWarnings("unchecked")
		Binding<String> binding = mock(Binding.class);
		when(binding.getInterface()).thenAnswer(
				Answers.answerClass(String.class));
		when(binding.size()).thenReturn(1);
		when(binding.iterator()).thenReturn(Iterators.forArray("um"));
		List<String> impls = Arrays.asList("dois", "tres");
		Factory factory = new BindingImpl.Factory();
		Binding<String> actual = factory.append(binding, impls);
		assertTrue(Iterables.elementsEqual(Arrays.asList("um", "dois", "tres"),
				actual));
	}
}
