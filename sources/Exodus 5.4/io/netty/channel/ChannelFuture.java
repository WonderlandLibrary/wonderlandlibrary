/*
 * Decompiled with CFR 0.152.
 */
package io.netty.channel;

import io.netty.channel.Channel;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;

public interface ChannelFuture
extends Future<Void> {
    public ChannelFuture addListener(GenericFutureListener<? extends Future<? super Void>> var1);

    public ChannelFuture awaitUninterruptibly();

    public Channel channel();

    public ChannelFuture addListeners(GenericFutureListener<? extends Future<? super Void>> ... var1);

    public ChannelFuture await() throws InterruptedException;

    public ChannelFuture syncUninterruptibly();

    public ChannelFuture removeListeners(GenericFutureListener<? extends Future<? super Void>> ... var1);

    public ChannelFuture removeListener(GenericFutureListener<? extends Future<? super Void>> var1);

    public ChannelFuture sync() throws InterruptedException;
}

