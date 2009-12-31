package br.com.touchtec.mockspi;

import com.google.inject.TypeLiteral;

public class MockSPIThreadRunnerTest extends
		MockeryTestCase<MockSPIThreadRunner> {

	@Override
	protected TypeLiteral<MockSPIThreadRunner> getType() {
		return new TypeLiteral<MockSPIThreadRunner>() {};
	}

}
