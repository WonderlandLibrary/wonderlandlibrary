/*
 * Decompiled with CFR 0.152.
 */
package liying.fonts.util;

public final class SneakyThrowing {
    private static Throwable sneakyThrow0(Throwable throwable) throws Throwable {
        throw throwable;
    }

    public static RuntimeException sneakyThrow(Throwable throwable) {
        return (RuntimeException)SneakyThrowing.sneakyThrow0(throwable);
    }

    private SneakyThrowing() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }
}

