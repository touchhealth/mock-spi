package br.com.touchtec.mockspi;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.anyList;
import static org.mockito.Matchers.eq;
import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import br.com.touchtec.mockspi.Binding;
import br.com.touchtec.mockspi.BindingBuilder;
import br.com.touchtec.mockspi.Binding.Factory;

public class BindingBuilderTest {

	private Factory mockFactory;
	private IntTestIface mockImpl1;
	private IntTestIface mockImpl2;
	private BindingBuilder builder;

	@Before
	public void before() {
		mockFactory = mock(Binding.Factory.class);
		mockImpl1 = mock(IntTestIface.class);
		mockImpl2 = mock(IntTestIface.class);
		builder = new BindingBuilder(mockFactory);
	}

	@Test
	public void builderShouldMergeSameIfaceBoundMoreThanOnce() {
		builder.add(IntTestIface.class, mockImpl1);
		builder.add(IntTestIface.class, mockImpl2);
		Set<Binding<?>> set = builder.build();
		assertEquals(1, set.size());
	}

	@SuppressWarnings("unchecked")
	@Test
	public void builderShouldCallAppendIfIfaceBoundMoreThanOnce() {
		when(mockFactory.create(eq(IntTestIface.class), anyList())).thenReturn(
				mock(Binding.class));
		builder.add(IntTestIface.class, mockImpl1);
		builder.add(IntTestIface.class, mockImpl2);
		verify(mockFactory).append(isA(Binding.class), anyList());
	}
}
