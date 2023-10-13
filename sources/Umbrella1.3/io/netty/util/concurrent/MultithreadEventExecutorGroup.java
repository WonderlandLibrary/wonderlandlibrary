/*
 * Decompiled with CFR 0.150.
 */
package io.netty.util.concurrent;

import io.netty.util.concurrent.AbstractEventExecutorGroup;
import io.netty.util.concurrent.DefaultPromise;
import io.netty.util.concurrent.DefaultThreadFactory;
import io.netty.util.concurrent.EventExecutor;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.FutureListener;
import io.netty.util.concurrent.GlobalEventExecutor;
import io.netty.util.concurrent.Promise;
import io.netty.util.concurrent.SingleThreadEventExecutor;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Set;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public abstract class MultithreadEventExecutorGroup
extends AbstractEventExecutorGroup {
    private final EventExecutor[] children;
    private final AtomicInteger childIndex = new AtomicInteger();
    private final AtomicInteger terminatedChildren = new AtomicInteger();
    private final Promise<?> terminationFuture = new DefaultPromise(GlobalEventExecutor.INSTANCE);

    protected MultithreadEventExecutorGroup(int nThreads, ThreadFactory threadFactory, Object ... args) {
        if (nThreads <= 0) {
            throw new IllegalArgumentException(String.format("nThreads: %d (expected: > 0)", nThreads));
        }
        if (threadFactory == null) {
            threadFactory = this.newDefaultThreadFactory();
        }
        this.children = new SingleThreadEventExecutor[nThreads];
        for (int i = 0; i < nThreads; ++i) {
            boolean success = false;
            try {
                this.children[i] = this.newChild(threadFactory, args);
                success = true;
                continue;
            }
            catch (Exception e) {
                throw new IllegalStateException("failed to create a child event loop", e);
            }
            finally {
                if (!success) {
                    int j;
                    for (j = 0; j < i; ++j) {
                        this.children[j].shutdownGracefully();
                    }
                    for (j = 0; j < i; ++j) {
                        EventExecutor e = this.children[j];
                        try {
                            while (!e.isTerminated()) {
                                e.awaitTermination(Integer.MAX_VALUE, TimeUnit.SECONDS);
                            }
                            continue;
                        }
                        catch (InterruptedException interrupted) {
                            Thread.currentThread().interrupt();
                            break;
                        }
                    }
                }
            }
        }
        FutureListener<Object> terminationListener = new FutureListener<Object>(){

            @Override
            public void operationComplete(Future<Object> future) throws Exception {
                if (MultithreadEventExecutorGroup.this.terminatedChildren.incrementAndGet() == MultithreadEventExecutorGroup.this.children.length) {
                    MultithreadEventExecutorGroup.this.terminationFuture.setSuccess(null);
                }
            }
        };
        for (EventExecutor e : this.children) {
            e.terminationFuture().addListener(terminationListener);
        }
    }

    protected ThreadFactory newDefaultThreadFactory() {
        return new DefaultThreadFactory(this.getClass());
    }

    @Override
    public EventExecutor next() {
        return this.children[Math.abs(this.childIndex.getAndIncrement() % this.children.length)];
    }

    @Override
    public Iterator<EventExecutor> iterator() {
        return this.children().iterator();
    }

    public final int executorCount() {
        return this.children.length;
    }

    protected Set<EventExecutor> children() {
        Set<EventExecutor> children = Collections.newSetFromMap(new LinkedHashMap());
        Collections.addAll(children, this.children);
        return children;
    }

    protected abstract EventExecutor newChild(ThreadFactory var1, Object ... var2) throws Exception;

    @Override
    public Future<?> shutdownGracefully(long quietPeriod, long timeout, TimeUnit unit) {
        for (EventExecutor l : this.children) {
            l.shutdownGracefully(quietPeriod, timeout, unit);
        }
        return this.terminationFuture();
    }

    @Override
    public Future<?> terminationFuture() {
        return this.terminationFuture;
    }

    @Override
    @Deprecated
    public void shutdown() {
        for (EventExecutor l : this.children) {
            l.shutdown();
        }
    }

    @Override
    public boolean isShuttingDown() {
        for (EventExecutor l : this.children) {
            if (l.isShuttingDown()) continue;
            return false;
        }
        return true;
    }

    @Override
    public boolean isShutdown() {
        for (EventExecutor l : this.children) {
            if (l.isShutdown()) continue;
            return false;
        }
        return true;
    }

    @Override
    public boolean isTerminated() {
        for (EventExecutor l : this.children) {
            if (l.isTerminated()) continue;
            return false;
        }
        return true;
    }

    @Override
    public boolean awaitTermination(long timeout, TimeUnit unit) throws InterruptedException {
        long deadline = System.nanoTime() + unit.toNanos(timeout);
        block0: for (EventExecutor l : this.children) {
            long timeLeft;
            while ((timeLeft = deadline - System.nanoTime()) > 0L) {
                if (!l.awaitTermination(timeLeft, TimeUnit.NANOSECONDS)) continue;
                continue block0;
            }
            break block0;
        }
        return this.isTerminated();
    }
}

