/*
 * Decompiled with CFR 0.152.
 */
package io.netty.channel;

import io.netty.channel.Channel;
import io.netty.channel.ChannelFlushPromiseNotifier;
import io.netty.channel.ChannelProgressivePromise;
import io.netty.util.concurrent.DefaultProgressivePromise;
import io.netty.util.concurrent.EventExecutor;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;

public class DefaultChannelProgressivePromise
extends DefaultProgressivePromise<Void>
implements ChannelProgressivePromise,
ChannelFlushPromiseNotifier.FlushCheckpoint {
    private long checkpoint;
    private final Channel channel;

    @Override
    public ChannelProgressivePromise sync() throws InterruptedException {
        super.sync();
        return this;
    }

    @Override
    public ChannelProgressivePromise setSuccess(Void void_) {
        super.setSuccess(void_);
        return this;
    }

    @Override
    public ChannelProgressivePromise promise() {
        return this;
    }

    public DefaultChannelProgressivePromise(Channel channel) {
        this.channel = channel;
    }

    public DefaultChannelProgressivePromise(Channel channel, EventExecutor eventExecutor) {
        super(eventExecutor);
        this.channel = channel;
    }

    @Override
    protected void checkDeadLock() {
        if (this.channel().isRegistered()) {
            super.checkDeadLock();
        }
    }

    @Override
    public ChannelProgressivePromise removeListener(GenericFutureListener<? extends Future<? super Void>> genericFutureListener) {
        super.removeListener(genericFutureListener);
        return this;
    }

    @Override
    public ChannelProgressivePromise await() throws InterruptedException {
        super.await();
        return this;
    }

    @Override
    public ChannelProgressivePromise syncUninterruptibly() {
        super.syncUninterruptibly();
        return this;
    }

    @Override
    public Channel channel() {
        return this.channel;
    }

    @Override
    public ChannelProgressivePromise addListeners(GenericFutureListener<? extends Future<? super Void>> ... genericFutureListenerArray) {
        super.addListeners(genericFutureListenerArray);
        return this;
    }

    @Override
    protected EventExecutor executor() {
        EventExecutor eventExecutor = super.executor();
        if (eventExecutor == null) {
            return this.channel().eventLoop();
        }
        return eventExecutor;
    }

    @Override
    public ChannelProgressivePromise setSuccess() {
        return this.setSuccess(null);
    }

    @Override
    public ChannelProgressivePromise awaitUninterruptibly() {
        super.awaitUninterruptibly();
        return this;
    }

    @Override
    public ChannelProgressivePromise removeListeners(GenericFutureListener<? extends Future<? super Void>> ... genericFutureListenerArray) {
        super.removeListeners(genericFutureListenerArray);
        return this;
    }

    @Override
    public ChannelProgressivePromise setProgress(long l, long l2) {
        super.setProgress(l, l2);
        return this;
    }

    @Override
    public void flushCheckpoint(long l) {
        this.checkpoint = l;
    }

    @Override
    public boolean trySuccess() {
        return this.trySuccess(null);
    }

    @Override
    public ChannelProgressivePromise setFailure(Throwable throwable) {
        super.setFailure(throwable);
        return this;
    }

    @Override
    public ChannelProgressivePromise addListener(GenericFutureListener<? extends Future<? super Void>> genericFutureListener) {
        super.addListener(genericFutureListener);
        return this;
    }

    @Override
    public long flushCheckpoint() {
        return this.checkpoint;
    }
}

