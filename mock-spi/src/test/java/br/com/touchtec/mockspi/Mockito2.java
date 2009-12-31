package br.com.touchtec.mockspi;

import org.mockito.Mockito;

import com.google.inject.TypeLiteral;

public class Mockito2 {

	@SuppressWarnings("unchecked")
	public static <T> T mock(TypeLiteral<T> typeLiteral) {
		return (T) Mockito.mock(typeLiteral.getRawType());
	}

}
