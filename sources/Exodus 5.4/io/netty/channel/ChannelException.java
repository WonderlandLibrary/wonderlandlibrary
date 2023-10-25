/*
 * Decompiled with CFR 0.152.
 */
package io.netty.channel;

public class ChannelException
extends RuntimeException {
    private static final long serialVersionUID = 2908618315971075004L;

    public ChannelException(String string, Throwable throwable) {
        super(string, throwable);
    }

    public ChannelException(String string) {
        super(string);
    }

    public ChannelException() {
    }

    public ChannelException(Throwable throwable) {
        super(throwable);
    }
}

