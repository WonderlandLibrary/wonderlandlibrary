/*
 * Decompiled with CFR 0.152.
 */
package io.netty.handler.codec.spdy;

public class SpdyProtocolException
extends Exception {
    private static final long serialVersionUID = 7870000537743847264L;

    public SpdyProtocolException(String string, Throwable throwable) {
        super(string, throwable);
    }

    public SpdyProtocolException() {
    }

    public SpdyProtocolException(Throwable throwable) {
        super(throwable);
    }

    public SpdyProtocolException(String string) {
        super(string);
    }
}

