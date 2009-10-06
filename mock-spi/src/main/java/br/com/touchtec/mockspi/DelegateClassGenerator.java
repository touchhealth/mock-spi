package br.com.touchtec.mockspi;

import static com.google.common.base.Preconditions.checkElementIndex;
import static com.google.common.base.Preconditions.checkNotNull;

import java.io.IOException;
import java.io.InputStream;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;

public class DelegateClassGenerator {

	/**
	 * @param binaryName
	 * @param binding
	 * @param index
	 *            of the binding instance
	 * @param definer
	 * @return class loaded by the the given class definer
	 */
	public Class<?> generateClass(String binaryName, Binding<?> binding,
			int index, ClassDefiner definer) {
		checkNotNull(binding);
		checkElementIndex(index, binding.size());
		checkNotNull(definer);

		ClassLoader loader = binding.getInterfaceClassLoader();
		InputStream stream = loader.getResourceAsStream(binding.getInterface()
				.getName().replace('.', '/')
				+ ".class");
		ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_MAXS);
		DelegateClassAdapter adapter = new DelegateClassAdapter(index,
				binaryName, writer, binding.getInterface());
		ClassReader reader;
		try {
			reader = new ClassReader(stream);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		reader.accept(adapter, 0);
		return definer.defineClass(binaryName, new ByteArray(writer
				.toByteArray()));
	}

}
