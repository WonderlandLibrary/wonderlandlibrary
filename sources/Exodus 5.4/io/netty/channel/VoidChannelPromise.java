/*
 * Decompiled with CFR 0.152.
 */
package io.netty.channel;

import io.netty.channel.Channel;
import io.netty.channel.ChannelPromise;
import io.netty.util.concurrent.AbstractFuture;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import java.util.concurrent.TimeUnit;

final class VoidChannelPromise
extends AbstractFuture<Void>
implements ChannelPromise {
    private final Channel channel;
    private final boolean fireException;

    @Override
    public boolean isSuccess() {
        return false;
    }

    @Override
    public VoidChannelPromise setSuccess(Void void_) {
        return this;
    }

    @Override
    public VoidChannelPromise addListeners(GenericFutureListener<? extends Future<? super Void>> ... genericFutureListenerArray) {
        VoidChannelPromise.fail();
        return this;
    }

    @Override
    public Void getNow() {
        return null;
    }

    @Override
    public boolean trySuccess(Void void_) {
        return false;
    }

    @Override
    public boolean isCancelled() {
        return false;
    }

    @Override
    public boolean awaitUninterruptibly(long l, TimeUnit timeUnit) {
        VoidChannelPromise.fail();
        return false;
    }

    private static void fail() {
        throw new IllegalStateException("void future");
    }

    @Override
    public boolean await(long l, TimeUnit timeUnit) {
        VoidChannelPromise.fail();
        return false;
    }

    @Override
    public VoidChannelPromise sync() {
        VoidChannelPromise.fail();
        return this;
    }

    VoidChannelPromise(Channel channel, boolean bl) {
        if (channel == null) {
            throw new NullPointerException("channel");
        }
        this.channel = channel;
        this.fireException = bl;
    }

    @Override
    public VoidChannelPromise setFailure(Throwable throwable) {
        this.fireException(throwable);
        return this;
    }

    @Override
    public boolean trySuccess() {
        return false;
    }

    @Override
    public boolean tryFailure(Throwable throwable) {
        this.fireException(throwable);
        return false;
    }

    @Override
    public boolean isCancellable() {
        return false;
    }

    @Override
    public VoidChannelPromise addListener(GenericFutureListener<? extends Future<? super Void>> genericFutureListener) {
        VoidChannelPromise.fail();
        return this;
    }

    private void fireException(Throwable throwable) {
        if (this.fireException && this.channel.isRegistered()) {
            this.channel.pipeline().fireExceptionCaught(throwable);
        }
    }

    @Override
    public boolean isDone() {
        return false;
    }

    @Override
    public VoidChannelPromise removeListeners(GenericFutureListener<? extends Future<? super Void>> ... genericFutureListenerArray) {
        return this;
    }

    @Override
    public VoidChannelPromise syncUninterruptibly() {
        VoidChannelPromise.fail();
        return this;
    }

    @Override
    public VoidChannelPromise awaitUninterruptibly() {
        VoidChannelPromise.fail();
        return this;
    }

    @Override
    public Throwable cause() {
        return null;
    }

    @Override
    public VoidChannelPromise setSuccess() {
        return this;
    }

    @Override
    public VoidChannelPromise removeListener(GenericFutureListener<? extends Future<? super Void>> genericFutureListener) {
        return this;
    }

    @Override
    public boolean await(long l) {
        VoidChannelPromise.fail();
        return false;
    }

    @Override
    public boolean awaitUninterruptibly(long l) {
        VoidChannelPromise.fail();
        return false;
    }

    @Override
    public VoidChannelPromise await() throws InterruptedException {
        if (Thread.interrupted()) {
            throw new InterruptedException();
        }
        return this;
    }

    @Override
    public Channel channel() {
        return this.channel;
    }

    @Override
    public boolean cancel(boolean bl) {
        return false;
    }

    @Override
    public boolean setUncancellable() {
        return true;
    }
}

