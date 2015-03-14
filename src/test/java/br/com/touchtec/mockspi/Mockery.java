package br.com.touchtec.mockspi;

import java.lang.annotation.Annotation;

import org.mockito.Mockito;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.Module;
import com.google.inject.TypeLiteral;
import com.google.inject.spi.Dependency;
import com.google.inject.spi.InjectionPoint;

public class Mockery<M> {

	private final Injector injector;
	private final TypeLiteral<M> typeLiteral;

	public Mockery(Injector injector, TypeLiteral<M> typeLiteral) {
		this.injector = injector;
		this.typeLiteral = typeLiteral;
	}

	public M get() {
		return injector.getInstance(Key.get(typeLiteral));
	}

	public <T> T recall(Annotation ann, Class<T> class1) {
		Key<T> key = Key.get(class1, ann);
		return injector.getInstance(key);
	}

	public <T> T recall(Class<T> class1) {
		return injector.getInstance(class1);
	}

	public static class Factory {

		public <T> Mockery<T> create(TypeLiteral<T> typeLiteral) {
			final InjectionPoint point = InjectionPoint
					.forConstructorOf(typeLiteral);
			Module module = new AbstractModule() {

				@SuppressWarnings("unchecked")
				private <S> void addBind(Key<S> key) {
					TypeLiteral<S> type = key.getTypeLiteral();
					Class<? super S> rawType = type.getRawType();
					S mock = (S) Mockito.mock(rawType);
					bind(key).toInstance(mock);
				}

				@Override
				protected void configure() {
					for (Dependency<?> dependency : point.getDependencies()) {
						addBind(dependency.getKey());
					}
				}
			};
			return new Mockery<T>(Guice.createInjector(module), typeLiteral);
		}

	}

}
