/*
 * Decompiled with CFR 0.152.
 */
package io.netty.util.concurrent;

public class BlockingOperationException
extends IllegalStateException {
    private static final long serialVersionUID = 2462223247762460301L;

    public BlockingOperationException(String string, Throwable throwable) {
        super(string, throwable);
    }

    public BlockingOperationException(String string) {
        super(string);
    }

    public BlockingOperationException() {
    }

    public BlockingOperationException(Throwable throwable) {
        super(throwable);
    }
}

