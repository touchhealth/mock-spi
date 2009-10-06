package br.com.touchtec.mockspi;

import static com.google.common.base.Preconditions.checkNotNull;

import java.io.ByteArrayInputStream;

public class ByteArray extends ByteArrayInputStream {

	public ByteArray(byte[] buf) {
		super(checkNotNull(buf));
	}

	public byte[] getBytes() {
		return this.buf;
	}
}
