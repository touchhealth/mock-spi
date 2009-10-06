package br.com.touchtec.mockspi;

public interface TestIface {

	public void voidMethod();

	public int intMethod();

	public <T extends Exception> T genericMethod(Class<T> c) throws T;

	public int multiArgs(int oi, String lala, double lele);
}