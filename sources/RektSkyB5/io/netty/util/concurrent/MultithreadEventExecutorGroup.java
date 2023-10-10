/*
 * Decompiled with CFR 0.152.
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
    private final EventExecutorChooser chooser;

    protected MultithreadEventExecutorGroup(int nThreads, ThreadFactory threadFactory, Object ... args) {
        if (nThreads <= 0) {
            throw new IllegalArgumentException(String.format("nThreads: %d (expected: > 0)", nThreads));
        }
        if (threadFactory == null) {
            threadFactory = this.newDefaultThreadFactory();
        }
        this.children = new SingleThreadEventExecutor[nThreads];
        this.chooser = MultithreadEventExecutorGroup.isPowerOfTwo(this.children.length) ? new PowerOfTwoEventExecutorChooser() : new GenericEventExecutorChooser();
        for (int i2 = 0; i2 < nThreads; ++i2) {
            boolean success = false;
            try {
                this.children[i2] = this.newChild(threadFactory, args);
                success = true;
                continue;
            }
            catch (Exception e2) {
                throw new IllegalStateException("failed to create a child event loop", e2);
            }
            finally {
                if (!success) {
                    int j2;
                    for (j2 = 0; j2 < i2; ++j2) {
                        this.children[j2].shutdownGracefully();
                    }
                    for (j2 = 0; j2 < i2; ++j2) {
                        EventExecutor e3 = this.children[j2];
                        try {
                            while (!e3.isTerminated()) {
                                e3.awaitTermination(Integer.MAX_VALUE, TimeUnit.SECONDS);
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
        for (EventExecutor e4 : this.children) {
            e4.terminationFuture().addListener(terminationListener);
        }
    }

    protected ThreadFactory newDefaultThreadFactory() {
        return new DefaultThreadFactory(this.getClass());
    }

    @Override
    public EventExecutor next() {
        return this.chooser.next();
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
        for (EventExecutor l2 : this.children) {
            l2.shutdownGracefully(quietPeriod, timeout, unit);
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
        for (EventExecutor l2 : this.children) {
            l2.shutdown();
        }
    }

    @Override
    public boolean isShuttingDown() {
        for (EventExecutor l2 : this.children) {
            if (l2.isShuttingDown()) continue;
            return false;
        }
        return true;
    }

    @Override
    public boolean isShutdown() {
        for (EventExecutor l2 : this.children) {
            if (l2.isShutdown()) continue;
            return false;
        }
        return true;
    }

    @Override
    public boolean isTerminated() {
        for (EventExecutor l2 : this.children) {
            if (l2.isTerminated()) continue;
            return false;
        }
        return true;
    }

    @Override
    public boolean awaitTermination(long timeout, TimeUnit unit) throws InterruptedException {
        long deadline = System.nanoTime() + unit.toNanos(timeout);
        block0: for (EventExecutor l2 : this.children) {
            long timeLeft;
            while ((timeLeft = deadline - System.nanoTime()) > 0L) {
                if (!l2.awaitTermination(timeLeft, TimeUnit.NANOSECONDS)) continue;
                continue block0;
            }
            break block0;
        }
        return this.isTerminated();
    }

    private static boolean isPowerOfTwo(int val2) {
        return (val2 & -val2) == val2;
    }

    private final class GenericEventExecutorChooser
    implements EventExecutorChooser {
        private GenericEventExecutorChooser() {
        }

        @Override
        public EventExecutor next() {
            return MultithreadEventExecutorGroup.this.children[Math.abs(MultithreadEventExecutorGroup.this.childIndex.getAndIncrement() % MultithreadEventExecutorGroup.this.children.length)];
        }
    }

    private final class PowerOfTwoEventExecutorChooser
    implements EventExecutorChooser {
        private PowerOfTwoEventExecutorChooser() {
        }

        @Override
        public EventExecutor next() {
            return MultithreadEventExecutorGroup.this.children[MultithreadEventExecutorGroup.this.childIndex.getAndIncrement() & MultithreadEventExecutorGroup.this.children.length - 1];
        }
    }

    private static interface EventExecutorChooser {
        public EventExecutor next();
    }
}

