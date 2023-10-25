/*
 * Decompiled with CFR 0.152.
 */
package io.netty.util;

public class IllegalReferenceCountException
extends IllegalStateException {
    private static final long serialVersionUID = -2507492394288153468L;

    public IllegalReferenceCountException(Throwable throwable) {
        super(throwable);
    }

    public IllegalReferenceCountException(String string) {
        super(string);
    }

    public IllegalReferenceCountException(String string, Throwable throwable) {
        super(string, throwable);
    }

    public IllegalReferenceCountException(int n, int n2) {
        this("refCnt: " + n + ", " + (n2 > 0 ? "increment: " + n2 : "decrement: " + -n2));
    }

    public IllegalReferenceCountException() {
    }

    public IllegalReferenceCountException(int n) {
        this("refCnt: " + n);
    }
}

