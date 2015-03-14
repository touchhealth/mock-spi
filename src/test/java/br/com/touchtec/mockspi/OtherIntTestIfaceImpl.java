package br.com.touchtec.mockspi;

public class OtherIntTestIfaceImpl implements OtherIntTestIface {

	@Override
	public String greet(String name) {
		return "Hello, again, " + name + "!";
	}

}
