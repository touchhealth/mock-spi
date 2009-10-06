package br.com.touchtec.mockspi;

import static com.google.common.base.Preconditions.checkNotNull;

import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLStreamHandler;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.google.inject.name.Named;

class MockSPIURLStreamHandler extends URLStreamHandler {

	private final String protocol;
	private final String host;
	private final int port;
	private final Binding<?> binding;
	private final MockSPIURLConnection.Factory factory;

	@Inject
	MockSPIURLStreamHandler(@Assisted Binding<?> binding,
			MockSPIURLConnection.Factory factory,
			@Named("mockspi.url.protocol") String protocol,
			@Named("mockspi.url.host") String host,
			@Named("mockspi.url.port") int port) {
		this.binding = checkNotNull(binding);
		this.factory = checkNotNull(factory);
		this.protocol = checkNotNull(protocol);
		this.host = checkNotNull(host);
		this.port = port;
	}

	private boolean checkSPIURL(URL u) {
		assert (u != null);
		return protocol.equals(u.getProtocol()) && host.equals(u.getHost())
				&& (port == u.getPort());
	}

	private void checkSPIURLAndThrow(URL u) throws IOException {
		assert (u != null);
		if (!checkSPIURL(u)) {
			throw new IOException("not a mock-spi url");
		}
	}

	@Override
	public URLConnection openConnection(URL u) throws IOException {
		checkSPIURLAndThrow(checkNotNull(u));
		return factory.create(u, this.binding);
	}

	static interface Factory {
		MockSPIURLStreamHandler create(Binding<?> binding);
	}
}