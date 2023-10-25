/*
 * Decompiled with CFR 0.152.
 */
package io.netty.util.concurrent;

import io.netty.util.concurrent.AbstractEventExecutor;
import io.netty.util.concurrent.DefaultProgressivePromise;
import io.netty.util.concurrent.DefaultPromise;
import io.netty.util.concurrent.EventExecutor;
import io.netty.util.concurrent.EventExecutorGroup;
import io.netty.util.concurrent.FailedFuture;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GlobalEventExecutor;
import io.netty.util.concurrent.ProgressivePromise;
import io.netty.util.concurrent.Promise;
import java.util.concurrent.TimeUnit;

public final class ImmediateEventExecutor
extends AbstractEventExecutor {
    public static final ImmediateEventExecutor INSTANCE = new ImmediateEventExecutor();
    private final Future<?> terminationFuture = new FailedFuture(GlobalEventExecutor.INSTANCE, new UnsupportedOperationException());

    @Override
    public void execute(Runnable runnable) {
        if (runnable == null) {
            throw new NullPointerException("command");
        }
        runnable.run();
    }

    @Override
    public boolean awaitTermination(long l, TimeUnit timeUnit) {
        return false;
    }

    @Override
    public boolean inEventLoop(Thread thread) {
        return true;
    }

    @Override
    public Future<?> shutdownGracefully(long l, long l2, TimeUnit timeUnit) {
        return this.terminationFuture();
    }

    @Override
    @Deprecated
    public void shutdown() {
    }

    @Override
    public boolean isTerminated() {
        return false;
    }

    @Override
    public <V> Promise<V> newPromise() {
        return new ImmediatePromise(this);
    }

    @Override
    public boolean inEventLoop() {
        return true;
    }

    private ImmediateEventExecutor() {
    }

    @Override
    public Future<?> terminationFuture() {
        return this.terminationFuture;
    }

    @Override
    public boolean isShuttingDown() {
        return false;
    }

    @Override
    public EventExecutorGroup parent() {
        return null;
    }

    @Override
    public <V> ProgressivePromise<V> newProgressivePromise() {
        return new ImmediateProgressivePromise(this);
    }

    @Override
    public boolean isShutdown() {
        return false;
    }

    static class ImmediatePromise<V>
    extends DefaultPromise<V> {
        @Override
        protected void checkDeadLock() {
        }

        ImmediatePromise(EventExecutor eventExecutor) {
            super(eventExecutor);
        }
    }

    static class ImmediateProgressivePromise<V>
    extends DefaultProgressivePromise<V> {
        ImmediateProgressivePromise(EventExecutor eventExecutor) {
            super(eventExecutor);
        }

        @Override
        protected void checkDeadLock() {
        }
    }
}

