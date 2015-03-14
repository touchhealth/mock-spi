package br.com.touchtec.mockspi;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BindingConverter {

	public static final Pattern PATTERN = Pattern.compile("M([0-9]+)_([0-9]+)");

	public String toBinaryName(Binding<?> binding, int index) {
		checkNotNull(binding);
		checkArgument(index >= 0);
		return new StringBuilder("M").append(binding.hashCode()).append('_')
				.append(index).toString();
	}

	private int matchAndGet(String binaryName, int group) {
		assert (binaryName != null);
		assert (group >= 0);
		Matcher matcher = PATTERN.matcher(binaryName);
		if (!matcher.matches()) {
			throw new IllegalArgumentException();
		}
		return new Integer(matcher.group(group));
	}

	public int binaryNameToHashcode(String binaryName) {
		return matchAndGet(checkNotNull(binaryName), 1);
	}

	public int binaryNameToIndex(String binaryName) {
		return matchAndGet(checkNotNull(binaryName), 2);
	}

	public boolean binaryNameMatches(String binaryName) {
		return PATTERN.matcher(checkNotNull(binaryName)).matches();
	}

}
