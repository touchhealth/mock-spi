package br.com.touchtec.mockspi;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.ServiceLoader;
import java.util.Set;

import org.junit.Test;

import com.google.common.base.Function;
import com.google.common.collect.Iterables;
import com.google.common.collect.Sets;

public class MockSPIIntTest {

	@Test
	public void runnableShouldBeInMockCL() throws Throwable {
		final IntTestIface iface = mock(IntTestIface.class);
		MockSPI.bind(IntTestIface.class, iface).andMock(new Runnable() {
			@Override
			public void run() {
				assertTrue(Thread.currentThread().getContextClassLoader() instanceof MockSPIClassLoader);
			}
		});
	}

	@Test
	public void runnableShouldFindOneURLForServiceFile() throws Throwable {
		final IntTestIface iface = mock(IntTestIface.class);
		MockSPI.bind(IntTestIface.class, iface).andMock(new Runnable() {
			@Override
			public void run() {
				ClassLoader loader = Thread.currentThread()
						.getContextClassLoader();
				Enumeration<URL> resources;
				try {
					resources = loader.getResources("META-INF/services/"
							+ IntTestIface.class.getName());
				} catch (IOException e) {
					throw new RuntimeException(e);
				}
				// lazyness sucks
				assertTrue(resources.hasMoreElements());
				assertNotNull(resources.nextElement());
				assertFalse(resources.hasMoreElements());
			}
		});
	}

	private void copy(InputStream in, OutputStream out) throws IOException {
		byte[] buf = new byte[1024];
		int len;
		while ((len = in.read(buf)) > 0) {
			out.write(buf, 0, len);
		}
	}

	@Test
	public void runnableShouldBeAbleToReadServiceFile() throws Throwable {
		final IntTestIface iface = mock(IntTestIface.class);
		MockSPI.bind(IntTestIface.class, iface).andMock(new Runnable() {
			@Override
			public void run() {
				ClassLoader loader = Thread.currentThread()
						.getContextClassLoader();
				Enumeration<URL> resources;
				try {
					resources = loader.getResources("META-INF/services/"
							+ IntTestIface.class.getName());
				} catch (IOException e) {
					throw new RuntimeException(e);
				}
				URL url = resources.nextElement();
				try {
					InputStream in = url.openStream();
					ByteArrayOutputStream out = new ByteArrayOutputStream();
					copy(in, out);
					String providerClassName = out.toString().trim();
					assertNotNull(providerClassName);
					assertTrue(providerClassName.startsWith("M"));
					assertTrue(providerClassName.endsWith("_0"));
				} catch (IOException e) {
					throw new RuntimeException(e);
				}

			}
		});
	}

	@Test
	public void runnableShouldBeAbleToLoadProvider() throws Throwable {
		final IntTestIface iface = mock(IntTestIface.class);
		MockSPI.bind(IntTestIface.class, iface).andMock(new Runnable() {
			@Override
			public void run() {
				ClassLoader loader = Thread.currentThread()
						.getContextClassLoader();
				Enumeration<URL> resources;
				try {
					resources = loader.getResources("META-INF/services/"
							+ IntTestIface.class.getName());
				} catch (IOException e) {
					throw new RuntimeException(e);
				}
				URL url = resources.nextElement();
				try {
					InputStream in = url.openStream();
					ByteArrayOutputStream out = new ByteArrayOutputStream();
					copy(in, out);
					String providerClassName = out.toString().trim();
					Class.forName(providerClassName, true, loader);
				} catch (IOException e) {
					throw new RuntimeException(e);
				} catch (ClassNotFoundException e) {
					throw new RuntimeException(e);
				}

			}
		});
	}

	@Test
	public void without() {
		IntTestIface actual = ServiceLoader.load(IntTestIface.class).iterator()
				.next();
		assertEquals("Hello, Rodrigo!", actual.greet("Rodrigo"));
	}

	@Test
	public void runnableShouldBeAbleToUseServiceLoader() throws Throwable {
		final IntTestIface iface = mock(IntTestIface.class);
		when(iface.greet(eq("Rodrigo"))).thenReturn("Ola, Oltra!");
		MockSPI.bind(IntTestIface.class, iface).andMock(new Runnable() {
			@Override
			public void run() {
				IntTestIface actual = ServiceLoader.load(IntTestIface.class)
						.iterator().next();
				assertEquals("Ola, Oltra!", actual.greet("Rodrigo"));
			}
		});
	}

	@Test
	public void runnableShouldBeAbleToUseServiceLoaderWithTwoInstances()
			throws Throwable {
		final IntTestIface iface1 = mock(IntTestIface.class);
		final IntTestIface iface2 = mock(IntTestIface.class);
		String[] outs = new String[] { "Ola, Oltra!", "Ola, de novo, Oltra!" };
		final Set<String> expected = Sets.newHashSet(Arrays.asList(outs));
		when(iface1.greet(eq("Rodrigo"))).thenReturn(outs[0]);
		when(iface2.greet(eq("Rodrigo"))).thenReturn(outs[1]);
		MockSPI.bind(IntTestIface.class, iface1, iface2).andMock(
				new Runnable() {
					@Override
					public void run() {
						Set<String> actual = Sets.newHashSet(Iterables
								.transform(ServiceLoader
										.load(IntTestIface.class),
										new Function<IntTestIface, String>() {

											@Override
											public String apply(
													IntTestIface iface) {
												return iface.greet("Rodrigo");
											}
										}));
						assertEquals(expected, actual);
					}
				});
	}

	@Test
	public void runnableShouldBeAbleToUseServiceLoaderWithTwoBindings()
			throws Throwable {
		final IntTestIface iface1 = mock(IntTestIface.class);
		final OtherIntTestIface iface2 = mock(OtherIntTestIface.class);
		String[] outs = new String[] { "Ola, Oltra!", "Ola, de novo, Oltra!" };
		final Set<String> expected = Sets.newHashSet(Arrays.asList(outs));
		when(iface1.greet(eq("Rodrigo"))).thenReturn(outs[0]);
		when(iface2.greet(eq("Rodrigo"))).thenReturn(outs[1]);
		MockSPI.bind(IntTestIface.class, iface1).andBind(
				OtherIntTestIface.class, iface2).andMock(new Runnable() {
			@Override
			public void run() {
				Set<String> actual = Sets.newHashSet();
				actual.add(ServiceLoader.load(IntTestIface.class).iterator()
						.next().greet("Rodrigo"));
				actual.add(ServiceLoader.load(OtherIntTestIface.class)
						.iterator().next().greet("Rodrigo"));
				assertEquals(expected, actual);
			}
		});
	}

	@Test
	public void throwerShouldBeInMockCL() throws IOException {
		final IntTestIface iface = mock(IntTestIface.class);
		MockSPI.bind(IntTestIface.class, iface).andExpect(IOException.class)
				.andRun(new Thrower<IOException>() {
					@Override
					public void run() throws IOException {
						assertTrue(Thread.currentThread()
								.getContextClassLoader() instanceof MockSPIClassLoader);
					}
				});
	}

	@Test
	public void throwerExceptionShouldBeThrownToClient() {
		final IntTestIface iface = mock(IntTestIface.class);
		final IOException ioException = new IOException();
		try {
			MockSPI.bind(IntTestIface.class, iface)
					.andExpect(IOException.class).andRun(
							new Thrower<IOException>() {
								@Override
								public void run() throws IOException {
									throw ioException;
								}
							});
			fail();
		} catch (IOException e) {
			assertSame(ioException, e);
		}
	}

	@Test
	public void throwerRuntimeExceptionShouldBeThrownToClient()
			throws IOException {
		final IntTestIface iface = mock(IntTestIface.class);
		final RuntimeException runtimeException = new RuntimeException();
		try {
			MockSPI.bind(IntTestIface.class, iface)
					.andExpect(IOException.class).andRun(
							new Thrower<IOException>() {
								@Override
								public void run() throws IOException {
									throw runtimeException;
								}
							});
			fail();
		} catch (RuntimeException e) {
			assertSame(runtimeException, e);
		}
	}

}
