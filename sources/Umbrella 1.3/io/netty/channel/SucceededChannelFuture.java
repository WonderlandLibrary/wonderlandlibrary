/*
 * Decompiled with CFR 0.150.
 */
package io.netty.channel;

import io.netty.channel.Channel;
import io.netty.channel.CompleteChannelFuture;
import io.netty.util.concurrent.EventExecutor;

final class SucceededChannelFuture
extends CompleteChannelFuture {
    public SucceededChannelFuture(Channel channel, EventExecutor executor) {
        super(channel, executor);
    }

    @Override
    public Throwable cause() {
        return null;
    }

    @Override
    public boolean isSuccess() {
        return true;
    }
}

