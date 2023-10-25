/*
 * Decompiled with CFR 0.152.
 */
package io.netty.channel;

import io.netty.channel.ChannelException;

public class EventLoopException
extends ChannelException {
    private static final long serialVersionUID = -8969100344583703616L;

    public EventLoopException(Throwable throwable) {
        super(throwable);
    }

    public EventLoopException(String string, Throwable throwable) {
        super(string, throwable);
    }

    public EventLoopException(String string) {
        super(string);
    }

    public EventLoopException() {
    }
}

