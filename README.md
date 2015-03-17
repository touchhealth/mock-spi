![Build status](https://travis-ci.org/rcouto/mock-spi.svg?branch=master "Build status") [![Coverage Status](https://coveralls.io/repos/rcouto/mock-spi/badge.svg)](https://coveralls.io/r/rcouto/mock-spi)

This library helps you create integration tests without worrying about `META-INF/services` files and without having visibility problems between single tests.

Here is a little snippet of how to use it:

```java
final IntTestIface iface = mock(IntTestIface.class);
when(iface.greet(eq("Mars"))).thenReturn("Hello, World!");
MockSPI.bind(IntTestIface.class, iface).andMock(new Runnable() {
    @Override public void run() {
        IntTestIface actual = ServiceLoader.load(IntTestIface.class).iterator().next();
        assertEquals("Hello, World!", actual.greet("Mars"));
    }
});
```

More examples [here](mock-spi/src/test/java/br/com/touchtec/mockspi/MockSPIIntTest.java).
