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

    public ChannelPromiseNotifier(ChannelPromise ... promises) {
        if (promises == null) {
            throw new NullPointerException("promises");
        }
        for (ChannelPromise promise : promises) {
            if (promise != null) continue;
            throw new IllegalArgumentException("promises contains null ChannelPromise");
        }
        this.promises = (ChannelPromise[])promises.clone();
    }

    @Override
    public void operationComplete(ChannelFuture cf) throws Exception {
        if (cf.isSuccess()) {
            for (ChannelPromise p2 : this.promises) {
                p2.setSuccess();
            }
            return;
        }
        Throwable cause = cf.cause();
        for (ChannelPromise p3 : this.promises) {
            p3.setFailure(cause);
        }
    }
}

