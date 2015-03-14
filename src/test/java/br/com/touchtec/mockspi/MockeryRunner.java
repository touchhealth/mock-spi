package br.com.touchtec.mockspi;

import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.InitializationError;

public class MockeryRunner extends BlockJUnit4ClassRunner {

	public MockeryRunner(Class<?> klass) throws InitializationError {
		super(klass);
	}

}
