/*
 * Decompiled with CFR 0.152.
 */
package io.netty.handler.ssl;

public final class SslHandshakeCompletionEvent {
    private final Throwable cause;
    public static final SslHandshakeCompletionEvent SUCCESS = new SslHandshakeCompletionEvent();

    private SslHandshakeCompletionEvent() {
        this.cause = null;
    }

    public boolean isSuccess() {
        return this.cause == null;
    }

    public SslHandshakeCompletionEvent(Throwable throwable) {
        if (throwable == null) {
            throw new NullPointerException("cause");
        }
        this.cause = throwable;
    }

    public Throwable cause() {
        return this.cause;
    }
}

