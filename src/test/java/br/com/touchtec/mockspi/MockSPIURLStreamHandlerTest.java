package br.com.touchtec.mockspi;

import static org.mockito.Mockito.mock;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLStreamHandler;

import org.junit.Test;

import br.com.touchtec.mockspi.Binding;
import br.com.touchtec.mockspi.MockSPIURLConnection;
import br.com.touchtec.mockspi.MockSPIURLStreamHandler;
import br.com.touchtec.mockspi.MockSPIURLConnection.Factory;

public class MockSPIURLStreamHandlerTest {

	private MockSPIURLStreamHandler create(String proto, String host, int port) {
		Binding<?> mockBinding = mock(Binding.class);
		Factory mockFactory = mock(MockSPIURLConnection.Factory.class);
		MockSPIURLStreamHandler handler = new MockSPIURLStreamHandler(
				mockBinding, mockFactory, proto, host, port);
		return handler;
	}

	private URL createURL(String proto, String host, int port)
			throws MalformedURLException {
		URLStreamHandler mockHandler = mock(URLStreamHandler.class);
		URL u = new URL(proto, host, port, "file", mockHandler);
		return u;
	}

	@Test(expected = IOException.class)
	public void urlShouldMatchProtocol() throws MalformedURLException,
			IOException {
		MockSPIURLStreamHandler handler = create("proto", "host", 0);
		handler.openConnection(createURL("zuado", "host", 0));
	}

	@Test(expected = IOException.class)
	public void urlShouldMatchHost() throws MalformedURLException, IOException {
		MockSPIURLStreamHandler handler = create("proto", "host", 0);
		handler.openConnection(createURL("proto", "zuado", 0));
	}

	@Test(expected = IOException.class)
	public void urlShouldMatchPort() throws MalformedURLException, IOException {
		MockSPIURLStreamHandler handler = create("proto", "host", 0);
		handler.openConnection(createURL("proto", "host", 666));
	}

}
