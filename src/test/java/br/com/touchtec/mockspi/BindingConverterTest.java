package br.com.touchtec.mockspi;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import br.com.touchtec.mockspi.Binding;
import br.com.touchtec.mockspi.BindingConverter;

public class BindingConverterTest {

	private BindingConverter converter;
	private String inPattern;
	private String outOfPattern;

	@Before
	public void before() {
		converter = new BindingConverter();
		inPattern = "M12345_9231";
		outOfPattern = "oi";
	}

	@Test
	public void outOfPatternShouldNotMatch() {
		assertFalse(converter.binaryNameMatches(outOfPattern));
	}

	@Test
	public void inPatternShouldMatch() {
		assertTrue(converter.binaryNameMatches(inPattern));
	}

	@Test
	public void bindingToBinaryNameShouldFollowThePattern() {
		Binding<String> binding = AdapterBinding.create(String.class);
		String binaryName = converter.toBinaryName(binding, 123);
		assertTrue(BindingConverter.PATTERN.matcher(binaryName).matches());
	}

	@Test(expected = IllegalArgumentException.class)
	public void binaryNameToHashcodeShouldFailIfNotFollowThePattern() {
		converter.binaryNameToHashcode(outOfPattern);
	}

	@Test
	public void binaryNameToHashcodeShouldReturnTheHashcodeOtherwise() {
		assertEquals(12345, converter.binaryNameToHashcode(inPattern));
	}

	@Test(expected = IllegalArgumentException.class)
	public void binaryNameToIndexShouldFailIfNotFollowThePattern() {
		converter.binaryNameToIndex(outOfPattern);
	}

	@Test
	public void binaryNameToIndexShouldReturnTheHashcodeOtherwise() {
		assertEquals(9231, converter.binaryNameToIndex(inPattern));
	}

	@Test
	public void convertionsShouldWorkTogether() {
		int index = 123;
		Binding<String> binding = AdapterBinding.create(String.class);
		String binaryName = converter.toBinaryName(binding, index);
		assertEquals(binding.hashCode(), converter
				.binaryNameToHashcode(binaryName));
		assertEquals(index, converter.binaryNameToIndex(binaryName));
	}

}
