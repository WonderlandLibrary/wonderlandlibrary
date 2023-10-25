/*
 * Decompiled with CFR 0.152.
 */
package io.netty.channel;

import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelPromise;

public final class ChannelPromiseNotifier
implements ChannelFutureListener {
    private final ChannelPromise[] promises;

    public ChannelPromiseNotifier(ChannelPromise ... channelPromiseArray) {
        if (channelPromiseArray == null) {
            throw new NullPointerException("promises");
        }
        for (ChannelPromise channelPromise : channelPromiseArray) {
            if (channelPromise != null) continue;
            throw new IllegalArgumentException("promises contains null ChannelPromise");
        }
        this.promises = (ChannelPromise[])channelPromiseArray.clone();
    }

    @Override
    public void operationComplete(ChannelFuture channelFuture) throws Exception {
        if (channelFuture.isSuccess()) {
            for (ChannelPromise channelPromise : this.promises) {
                channelPromise.setSuccess();
            }
            return;
        }
        Throwable throwable = channelFuture.cause();
        for (ChannelPromise channelPromise : this.promises) {
            channelPromise.setFailure(throwable);
        }
    }
}

