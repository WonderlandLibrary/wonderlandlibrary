/*
 * Decompiled with CFR 0.152.
 */
package io.netty.handler.codec;

public class CodecException
extends RuntimeException {
    private static final long serialVersionUID = -1464830400709348473L;

    public CodecException(String string, Throwable throwable) {
        super(string, throwable);
    }

    public CodecException(Throwable throwable) {
        super(throwable);
    }

    public CodecException() {
    }

    public CodecException(String string) {
        super(string);
    }
}

