package br.com.touchtec.mockspi;

import static com.google.common.base.Preconditions.checkNotNull;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Enumeration;

/**
 * Delegates every <code>public</code> method of {@link ClassLoader}. Parameter
 * checking will be done by the delegate loader.
 */
public class DelegateClassLoader extends ClassLoader {

	private final ClassLoader delegate;

	/**
	 * @param delegate
	 */
	public DelegateClassLoader(ClassLoader delegate) {
		// TODO check if not having a direct parent sets the behavior as
		// expected
		super();
		this.delegate = checkNotNull(delegate);
	}

	@Override
	public synchronized void clearAssertionStatus() {
		this.delegate.clearAssertionStatus();
	}

	@Override
	public URL getResource(String name) {
		return this.delegate.getResource(name);
	}

	@Override
	public InputStream getResourceAsStream(String name) {
		return this.delegate.getResourceAsStream(name);
	}

	@Override
	public Enumeration<URL> getResources(String name) throws IOException {
		return this.delegate.getResources(name);
	}

	@Override
	public Class<?> loadClass(String name) throws ClassNotFoundException {
		return this.delegate.loadClass(name);
	}

	@Override
	public synchronized void setClassAssertionStatus(String className,
			boolean enabled) {
		this.delegate.setClassAssertionStatus(className, enabled);
	}

	@Override
	public synchronized void setDefaultAssertionStatus(boolean enabled) {
		this.delegate.setDefaultAssertionStatus(enabled);
	}

	@Override
	public synchronized void setPackageAssertionStatus(String packageName,
			boolean enabled) {
		this.delegate.setPackageAssertionStatus(packageName, enabled);
	}

}
