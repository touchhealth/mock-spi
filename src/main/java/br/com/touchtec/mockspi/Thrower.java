package br.com.touchtec.mockspi;

public interface Thrower<E extends Exception> {

	public void run() throws E;

}
