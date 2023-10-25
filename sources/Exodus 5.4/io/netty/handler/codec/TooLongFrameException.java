/*
 * Decompiled with CFR 0.152.
 */
package io.netty.handler.codec;

import io.netty.handler.codec.DecoderException;

public class TooLongFrameException
extends DecoderException {
    private static final long serialVersionUID = -1995801950698951640L;

    public TooLongFrameException(String string, Throwable throwable) {
        super(string, throwable);
    }

    public TooLongFrameException(Throwable throwable) {
        super(throwable);
    }

    public TooLongFrameException() {
    }

    public TooLongFrameException(String string) {
        super(string);
    }
}

