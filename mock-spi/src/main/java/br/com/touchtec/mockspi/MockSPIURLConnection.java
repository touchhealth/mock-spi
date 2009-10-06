package br.com.touchtec.mockspi;

import static com.google.common.base.Preconditions.checkNotNull;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;

class MockSPIURLConnection extends URLConnection {

	private final Binding<?> binding;
	private final BindingConverter converter;

	@Inject
	public MockSPIURLConnection(@Assisted URL url,
			@Assisted Binding<?> binding, BindingConverter converter) {
		super(checkNotNull(url));
		this.binding = checkNotNull(binding);
		this.converter = checkNotNull(converter);
	}

	@Override
	public void connect() throws IOException {
		// empty method
	}

	private InputStream createInputStream(Binding<?> binding) {
		checkNotNull(binding);
		StringBuilder builder = new StringBuilder();
		for (int i = 0; i < binding.size(); i++) {
			builder.append(converter.toBinaryName(binding, i));
			builder.append('\n');
		}
		return new ByteArrayInputStream(builder.toString().getBytes());
	}

	@Override
	public InputStream getInputStream() throws IOException {
		return createInputStream(this.binding);
	}

	static interface Factory {
		MockSPIURLConnection create(URL url, Binding<?> binding);
	}
}