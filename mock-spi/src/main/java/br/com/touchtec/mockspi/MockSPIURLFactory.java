package br.com.touchtec.mockspi;

import static com.google.common.base.Preconditions.checkNotNull;

import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLStreamHandler;

import com.google.inject.Inject;
import com.google.inject.name.Named;

public class MockSPIURLFactory {

	private final URLFactory urlFactory;
	private final MockSPIURLStreamHandler.Factory factory;
	private final String protocol;
	private final String host;
	private final int port;

	@Inject
	public MockSPIURLFactory(URLFactory urlFactory,
			MockSPIURLStreamHandler.Factory factory,
			@Named("mockspi.url.protocol") String protocol,
			@Named("mockspi.url.host") String host,
			@Named("mockspi.url.port") int port) {
		this.urlFactory = checkNotNull(urlFactory);
		this.factory = checkNotNull(factory);
		this.protocol = checkNotNull(protocol);
		this.host = checkNotNull(host);
		this.port = port;
	}

	public URL createURL(Binding<?> binding) {
		checkNotNull(binding);
		try {
			return urlFactory.create(protocol, host, port,
					createFileName(binding), factory.create(binding));
		} catch (MalformedURLException e) {
			throw new RuntimeException(e);
		}
	}

	private String createFileName(Binding<?> binding) {
		assert (binding != null);
		return "/" + binding.hashCode();
	}

	static class URLFactory {
		public URL create(String protocol, String host, int port, String file,
				URLStreamHandler handler) throws MalformedURLException {
			return new URL(checkNotNull(protocol), checkNotNull(host), port,
					checkNotNull(file), checkNotNull(handler));
		}
	}

}