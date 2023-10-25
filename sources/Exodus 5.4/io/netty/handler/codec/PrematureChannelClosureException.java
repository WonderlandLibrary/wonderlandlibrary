/*
 * Decompiled with CFR 0.152.
 */
package io.netty.handler.codec;

import io.netty.handler.codec.CodecException;

public class PrematureChannelClosureException
extends CodecException {
    private static final long serialVersionUID = 4907642202594703094L;

    public PrematureChannelClosureException(String string) {
        super(string);
    }

    public PrematureChannelClosureException(Throwable throwable) {
        super(throwable);
    }

    public PrematureChannelClosureException(String string, Throwable throwable) {
        super(string, throwable);
    }

    public PrematureChannelClosureException() {
    }
}

