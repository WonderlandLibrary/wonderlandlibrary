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
    private final EventExecutorChooser chooser;
    private final Promise<?> terminationFuture;
    private final AtomicInteger childIndex = new AtomicInteger();
    private final AtomicInteger terminatedChildren = new AtomicInteger();
    private final EventExecutor[] children;

    @Override
    public Iterator<EventExecutor> iterator() {
        return this.children().iterator();
    }

    private static boolean isPowerOfTwo(int n) {
        return (n & -n) == n;
    }

    protected Set<EventExecutor> children() {
        Set<EventExecutor> set = Collections.newSetFromMap(new LinkedHashMap());
        Collections.addAll(set, this.children);
        return set;
    }

    @Override
    public boolean isTerminated() {
        for (EventExecutor eventExecutor : this.children) {
            if (eventExecutor.isTerminated()) continue;
            return false;
        }
        return true;
    }

    @Override
    public boolean isShutdown() {
        for (EventExecutor eventExecutor : this.children) {
            if (eventExecutor.isShutdown()) continue;
            return false;
        }
        return true;
    }

    @Override
    public EventExecutor next() {
        return this.chooser.next();
    }

    @Override
    public boolean awaitTermination(long l, TimeUnit timeUnit) throws InterruptedException {
        long l2 = System.nanoTime() + timeUnit.toNanos(l);
        block0: for (EventExecutor eventExecutor : this.children) {
            long l3;
            while ((l3 = l2 - System.nanoTime()) > 0L) {
                if (!eventExecutor.awaitTermination(l3, TimeUnit.NANOSECONDS)) continue;
                continue block0;
            }
            break block0;
        }
        return this.isTerminated();
    }

    @Override
    public Future<?> shutdownGracefully(long l, long l2, TimeUnit timeUnit) {
        for (EventExecutor eventExecutor : this.children) {
            eventExecutor.shutdownGracefully(l, l2, timeUnit);
        }
        return this.terminationFuture();
    }

    @Override
    @Deprecated
    public void shutdown() {
        for (EventExecutor eventExecutor : this.children) {
            eventExecutor.shutdown();
        }
    }

    protected abstract EventExecutor newChild(ThreadFactory var1, Object ... var2) throws Exception;

    protected ThreadFactory newDefaultThreadFactory() {
        return new DefaultThreadFactory(this.getClass());
    }

    @Override
    public boolean isShuttingDown() {
        for (EventExecutor eventExecutor : this.children) {
            if (eventExecutor.isShuttingDown()) continue;
            return false;
        }
        return true;
    }

    @Override
    public Future<?> terminationFuture() {
        return this.terminationFuture;
    }

    public final int executorCount() {
        return this.children.length;
    }

    protected MultithreadEventExecutorGroup(int n, ThreadFactory threadFactory, Object ... objectArray) {
        this.terminationFuture = new DefaultPromise(GlobalEventExecutor.INSTANCE);
        if (n <= 0) {
            throw new IllegalArgumentException(String.format("nThreads: %d (expected: > 0)", n));
        }
        if (threadFactory == null) {
            threadFactory = this.newDefaultThreadFactory();
        }
        this.children = new SingleThreadEventExecutor[n];
        this.chooser = MultithreadEventExecutorGroup.isPowerOfTwo(this.children.length) ? new PowerOfTwoEventExecutorChooser() : new GenericEventExecutorChooser();
        block5: for (int i = 0; i < n; ++i) {
            int n2;
            boolean bl = false;
            try {
                this.children[i] = this.newChild(threadFactory, objectArray);
                bl = true;
                if (bl) continue;
            }
            catch (Exception exception) {
                throw new IllegalStateException("failed to create a child event loop", exception);
            }
            for (n2 = 0; n2 < i; ++n2) {
                this.children[n2].shutdownGracefully();
            }
            for (n2 = 0; n2 < i; ++n2) {
                EventExecutor eventExecutor = this.children[n2];
                try {
                    while (!eventExecutor.isTerminated()) {
                        eventExecutor.awaitTermination(Integer.MAX_VALUE, TimeUnit.SECONDS);
                    }
                    continue;
                }
                catch (InterruptedException interruptedException) {
                    Thread.currentThread().interrupt();
                    continue block5;
                }
            }
            continue;
        }
        FutureListener<Object> futureListener = new FutureListener<Object>(){

            @Override
            public void operationComplete(Future<Object> future) throws Exception {
                if (MultithreadEventExecutorGroup.this.terminatedChildren.incrementAndGet() == MultithreadEventExecutorGroup.this.children.length) {
                    MultithreadEventExecutorGroup.this.terminationFuture.setSuccess(null);
                }
            }
        };
        for (EventExecutor eventExecutor : this.children) {
            eventExecutor.terminationFuture().addListener(futureListener);
        }
    }

    private static interface EventExecutorChooser {
        public EventExecutor next();
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

    private final class GenericEventExecutorChooser
    implements EventExecutorChooser {
        @Override
        public EventExecutor next() {
            return MultithreadEventExecutorGroup.this.children[Math.abs(MultithreadEventExecutorGroup.this.childIndex.getAndIncrement() % MultithreadEventExecutorGroup.this.children.length)];
        }

        private GenericEventExecutorChooser() {
        }
    }
}

