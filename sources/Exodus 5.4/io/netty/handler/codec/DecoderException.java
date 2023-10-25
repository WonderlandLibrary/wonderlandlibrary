/*
 * Decompiled with CFR 0.152.
 */
package io.netty.handler.codec;

import io.netty.handler.codec.CodecException;

public class DecoderException
extends CodecException {
    private static final long serialVersionUID = 6926716840699621852L;

    public DecoderException() {
    }

    public DecoderException(String string) {
        super(string);
    }

    public DecoderException(Throwable throwable) {
        super(throwable);
    }

    public DecoderException(String string, Throwable throwable) {
        super(string, throwable);
    }
}

