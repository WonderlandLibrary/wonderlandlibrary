/*
 * Decompiled with CFR 0.152.
 */
package io.netty.channel.embedded;

import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelPromise;
import io.netty.channel.DefaultChannelPromise;
import io.netty.channel.EventLoop;
import io.netty.channel.EventLoopGroup;
import io.netty.util.concurrent.AbstractEventExecutor;
import io.netty.util.concurrent.Future;
import java.util.ArrayDeque;
import java.util.Queue;
import java.util.concurrent.TimeUnit;

final class EmbeddedEventLoop
extends AbstractEventExecutor
implements EventLoop {
    private final Queue<Runnable> tasks = new ArrayDeque<Runnable>(2);

    @Override
    public boolean inEventLoop(Thread thread) {
        return true;
    }

    @Override
    public boolean isShutdown() {
        return false;
    }

    @Override
    public EventLoop next() {
        return this;
    }

    @Override
    public ChannelFuture register(Channel channel, ChannelPromise channelPromise) {
        channel.unsafe().register(this, channelPromise);
        return channelPromise;
    }

    void runTasks() {
        Runnable runnable;
        while ((runnable = this.tasks.poll()) != null) {
            runnable.run();
        }
    }

    @Override
    public void execute(Runnable runnable) {
        if (runnable == null) {
            throw new NullPointerException("command");
        }
        this.tasks.add(runnable);
    }

    @Override
    public Future<?> terminationFuture() {
        throw new UnsupportedOperationException();
    }

    @Override
    public ChannelFuture register(Channel channel) {
        return this.register(channel, new DefaultChannelPromise(channel, this));
    }

    EmbeddedEventLoop() {
    }

    @Override
    public EventLoopGroup parent() {
        return this;
    }

    @Override
    public boolean isShuttingDown() {
        return false;
    }

    @Override
    @Deprecated
    public void shutdown() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Future<?> shutdownGracefully(long l, long l2, TimeUnit timeUnit) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean awaitTermination(long l, TimeUnit timeUnit) throws InterruptedException {
        Thread.sleep(timeUnit.toMillis(l));
        return false;
    }

    @Override
    public boolean isTerminated() {
        return false;
    }

    @Override
    public boolean inEventLoop() {
        return true;
    }
}

