/*
 * Decompiled with CFR 0.152.
 */
package io.netty.handler.codec.compression;

import io.netty.handler.codec.DecoderException;

public class DecompressionException
extends DecoderException {
    private static final long serialVersionUID = 3546272712208105199L;

    public DecompressionException(String string) {
        super(string);
    }

    public DecompressionException(Throwable throwable) {
        super(throwable);
    }

    public DecompressionException(String string, Throwable throwable) {
        super(string, throwable);
    }

    public DecompressionException() {
    }
}

