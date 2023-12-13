/*
 * Decompiled with CFR 0.152.
 */
package io.netty.handler.codec.http.websocketx;

public class WebSocketHandshakeException
extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public WebSocketHandshakeException(String s2) {
        super(s2);
    }

    public WebSocketHandshakeException(String s2, Throwable throwable) {
        super(s2, throwable);
    }
}

