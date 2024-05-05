/*
 * Decompiled with CFR 0.152.
 */
package jx.utils.novoline.util;

public final class SneakyThrowing {
    public static RuntimeException sneakyThrow(Throwable throwable) {
        return (RuntimeException)SneakyThrowing.sneakyThrow0(throwable);
    }

    private static <T extends Throwable> T sneakyThrow0(Throwable throwable) throws T {
        throw throwable;
    }

    private SneakyThrowing() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }
}

