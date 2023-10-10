/*
 * Decompiled with CFR 0.152.
 */
package joptsimple.internal;

public final class Objects {
    private Objects() {
        throw new UnsupportedOperationException();
    }

    public static void ensureNotNull(Object target) {
        if (target == null) {
            throw new NullPointerException();
        }
    }
}

