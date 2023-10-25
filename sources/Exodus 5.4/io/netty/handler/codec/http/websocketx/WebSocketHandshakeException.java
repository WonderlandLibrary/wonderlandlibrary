/*
 * Decompiled with CFR 0.152.
 */
package io.netty.handler.codec.http.websocketx;

public class WebSocketHandshakeException
extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public WebSocketHandshakeException(String string) {
        super(string);
    }

    public WebSocketHandshakeException(String string, Throwable throwable) {
        super(string, throwable);
    }
}

