package br.com.touchtec.mockspi;

public class ASMTest implements TestIface {

	private final TestIface delegate;

	public ASMTest() {
		this(ASMTestDebuger.getInstance(TestIface.class, 321));
	}

	public ASMTest(TestIface delegate) {
		super();
		this.delegate = delegate;
	}

	@Override
	public void voidMethod() {
		this.delegate.voidMethod();
	}

	@Override
	public int intMethod() {
		return this.delegate.intMethod();
	}

	@Override
	public <T extends Exception> T genericMethod(Class<T> c) throws T {
		throw this.delegate.genericMethod(c);
	}

	@Override
	public int multiArgs(int oi, String lala, double lele) {
		return this.delegate.multiArgs(oi, lala, lele);
	}

}
