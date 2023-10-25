/*
 * Decompiled with CFR 0.152.
 */
package io.netty.handler.codec;

import io.netty.handler.codec.CodecException;

public class EncoderException
extends CodecException {
    private static final long serialVersionUID = -5086121160476476774L;

    public EncoderException(Throwable throwable) {
        super(throwable);
    }

    public EncoderException(String string) {
        super(string);
    }

    public EncoderException() {
    }

    public EncoderException(String string, Throwable throwable) {
        super(string, throwable);
    }
}

