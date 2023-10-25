/*
 * Decompiled with CFR 0.152.
 */
package io.netty.handler.codec.compression;

import io.netty.handler.codec.EncoderException;

public class CompressionException
extends EncoderException {
    private static final long serialVersionUID = 5603413481274811897L;

    public CompressionException(Throwable throwable) {
        super(throwable);
    }

    public CompressionException(String string) {
        super(string);
    }

    public CompressionException(String string, Throwable throwable) {
        super(string, throwable);
    }

    public CompressionException() {
    }
}

