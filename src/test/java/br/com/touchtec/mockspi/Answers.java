package br.com.touchtec.mockspi;

import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

public class Answers {

	public static <T> Answer<Class<T>> answerClass(final Class<T> c) {
		return new Answer<Class<T>>() {

			@Override
			public Class<T> answer(InvocationOnMock invocation)
					throws Throwable {
				return c;
			}
		};
	}

}
