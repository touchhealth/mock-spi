package br.com.touchtec.mockspi;

public class IntTestIfaceImpl implements IntTestIface {

	@Override
	public String greet(String name) {
		return "Hello, " + name + "!";
	}

}
