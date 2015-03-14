package br.com.touchtec.mockspi;

import java.util.Set;

public class RefactoryingSandbox {

	// fica fora adaptação de thrower e building de bindings
	// cria classloader
	// cria thread
	// troca cl da thread e manda rodar
	public static interface Runner {
		public void run(Runnable runnable, Set<Binding<?>> bindings);
	}

	// fica fora building de bindings
	// adapta thrower em formato de runnable
	// delega para runner
	// tenta pegar exceções lançadas
	public static interface ThrowerRunner {
		public <E extends Exception> void run(Thrower<E> thrower,
				Set<Binding<?>> bindings) throws E;
	}
}
