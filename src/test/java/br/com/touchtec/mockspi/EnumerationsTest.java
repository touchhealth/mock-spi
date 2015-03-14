package br.com.touchtec.mockspi;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Collections;
import java.util.Enumeration;
import java.util.NoSuchElementException;

import org.junit.Test;

import br.com.touchtec.mockspi.Enumerations;

public class EnumerationsTest {

	@Test
	public void newEnumFromEmptyItShouldHaveNoMoreElems() {
		Enumeration<String> enume = Enumerations.newEnumeration(Collections
				.<String> emptySet().iterator());
		assertFalse(enume.hasMoreElements());
	}

	@Test(expected = NoSuchElementException.class)
	public void newEnumFromEmptyItShouldThrowNoSuchElem() {
		Enumeration<String> enume = Enumerations.newEnumeration(Collections
				.<String> emptySet().iterator());
		enume.nextElement();
	}

	@Test
	public void newEnumFromNonEmptyItShouldHaveMoreElems() {
		Enumeration<String> enume = Enumerations.newEnumeration(Arrays.asList(
				"oi", "mamae").iterator());
		assertTrue(enume.hasMoreElements());
	}

}
