package br.com.touchtec.mockspi;

import java.lang.annotation.Annotation;

import com.google.inject.assistedinject.Assisted;

public class Annotations {

	public static Assisted assisted() {
		return assisted("");
	}

	public static Assisted assisted(final String value) {
		return new Assisted() {

			@Override
			public String value() {
				return value;
			}

			public int hashCode() {
				// This is specified in java.lang.Annotation.
				return (127 * "value".hashCode()) ^ value.hashCode();
			}

			public boolean equals(Object o) {
				if (!(o instanceof Assisted)) {
					return false;
				}

				Assisted other = (Assisted) o;
				return value.equals(other.value());
			}

			public String toString() {
				return "@" + Assisted.class.getName() + "(value=" + value + ")";
			}

			public Class<? extends Annotation> annotationType() {
				return Assisted.class;
			}

			private static final long serialVersionUID = 0;
		};
	}

}
