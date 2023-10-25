/*
 * Decompiled with CFR 0.152.
 */
package io.netty.channel;

import io.netty.channel.Channel;
import io.netty.channel.ChannelException;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelPromise;
import io.netty.channel.DefaultChannelPromise;
import io.netty.channel.EventLoop;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.FailedChannelFuture;
import io.netty.channel.ThreadPerChannelEventLoop;
import io.netty.util.concurrent.AbstractEventExecutorGroup;
import io.netty.util.concurrent.DefaultPromise;
import io.netty.util.concurrent.EventExecutor;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.FutureListener;
import io.netty.util.concurrent.GlobalEventExecutor;
import io.netty.util.concurrent.Promise;
import io.netty.util.internal.EmptyArrays;
import io.netty.util.internal.PlatformDependent;
import io.netty.util.internal.ReadOnlyIterator;
import java.util.Collections;
import java.util.Iterator;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

public class ThreadPerChannelEventLoopGroup
extends AbstractEventExecutorGroup
implements EventLoopGroup {
    final ThreadFactory threadFactory;
    private final FutureListener<Object> childTerminationListener;
    private final int maxChannels;
    private volatile boolean shuttingDown;
    private final Promise<?> terminationFuture;
    final Queue<ThreadPerChannelEventLoop> idleChildren;
    private final ChannelException tooManyChannels;
    final Set<ThreadPerChannelEventLoop> activeChildren = Collections.newSetFromMap(PlatformDependent.newConcurrentHashMap());
    private final Object[] childArgs;

    protected ThreadPerChannelEventLoopGroup(int n, ThreadFactory threadFactory, Object ... objectArray) {
        this.idleChildren = new ConcurrentLinkedQueue<ThreadPerChannelEventLoop>();
        this.terminationFuture = new DefaultPromise(GlobalEventExecutor.INSTANCE);
        this.childTerminationListener = new FutureListener<Object>(){

            @Override
            public void operationComplete(Future<Object> future) throws Exception {
                if (ThreadPerChannelEventLoopGroup.this.isTerminated()) {
                    ThreadPerChannelEventLoopGroup.this.terminationFuture.trySuccess(null);
                }
            }
        };
        if (n < 0) {
            throw new IllegalArgumentException(String.format("maxChannels: %d (expected: >= 0)", n));
        }
        if (threadFactory == null) {
            throw new NullPointerException("threadFactory");
        }
        this.childArgs = objectArray == null ? EmptyArrays.EMPTY_OBJECTS : (Object[])objectArray.clone();
        this.maxChannels = n;
        this.threadFactory = threadFactory;
        this.tooManyChannels = new ChannelException("too many channels (max: " + n + ')');
        this.tooManyChannels.setStackTrace(EmptyArrays.EMPTY_STACK_TRACE);
    }

    @Override
    public Iterator<EventExecutor> iterator() {
        return new ReadOnlyIterator<EventExecutor>(this.activeChildren.iterator());
    }

    @Override
    public EventLoop next() {
        throw new UnsupportedOperationException();
    }

    @Override
    public ChannelFuture register(Channel channel) {
        if (channel == null) {
            throw new NullPointerException("channel");
        }
        try {
            EventLoop eventLoop = this.nextChild();
            return eventLoop.register(channel, new DefaultChannelPromise(channel, eventLoop));
        }
        catch (Throwable throwable) {
            return new FailedChannelFuture(channel, GlobalEventExecutor.INSTANCE, throwable);
        }
    }

    protected ThreadPerChannelEventLoopGroup() {
        this(0);
    }

    protected ThreadPerChannelEventLoopGroup(int n) {
        this(n, Executors.defaultThreadFactory(), new Object[0]);
    }

    @Override
    public boolean isShuttingDown() {
        for (EventLoop eventLoop : this.activeChildren) {
            if (eventLoop.isShuttingDown()) continue;
            return false;
        }
        for (EventLoop eventLoop : this.idleChildren) {
            if (eventLoop.isShuttingDown()) continue;
            return false;
        }
        return true;
    }

    @Override
    public boolean isShutdown() {
        for (EventLoop eventLoop : this.activeChildren) {
            if (eventLoop.isShutdown()) continue;
            return false;
        }
        for (EventLoop eventLoop : this.idleChildren) {
            if (eventLoop.isShutdown()) continue;
            return false;
        }
        return true;
    }

    @Override
    @Deprecated
    public void shutdown() {
        this.shuttingDown = true;
        for (EventLoop eventLoop : this.activeChildren) {
            eventLoop.shutdown();
        }
        for (EventLoop eventLoop : this.idleChildren) {
            eventLoop.shutdown();
        }
        if (this.isTerminated()) {
            this.terminationFuture.trySuccess(null);
        }
    }

    @Override
    public Future<?> terminationFuture() {
        return this.terminationFuture;
    }

    @Override
    public ChannelFuture register(Channel channel, ChannelPromise channelPromise) {
        if (channel == null) {
            throw new NullPointerException("channel");
        }
        try {
            return this.nextChild().register(channel, channelPromise);
        }
        catch (Throwable throwable) {
            channelPromise.setFailure(throwable);
            return channelPromise;
        }
    }

    private EventLoop nextChild() throws Exception {
        if (this.shuttingDown) {
            throw new RejectedExecutionException("shutting down");
        }
        ThreadPerChannelEventLoop threadPerChannelEventLoop = this.idleChildren.poll();
        if (threadPerChannelEventLoop == null) {
            if (this.maxChannels > 0 && this.activeChildren.size() >= this.maxChannels) {
                throw this.tooManyChannels;
            }
            threadPerChannelEventLoop = this.newChild(this.childArgs);
            threadPerChannelEventLoop.terminationFuture().addListener(this.childTerminationListener);
        }
        this.activeChildren.add(threadPerChannelEventLoop);
        return threadPerChannelEventLoop;
    }

    protected ThreadPerChannelEventLoop newChild(Object ... objectArray) throws Exception {
        return new ThreadPerChannelEventLoop(this);
    }

    @Override
    public Future<?> shutdownGracefully(long l, long l2, TimeUnit timeUnit) {
        this.shuttingDown = true;
        for (EventLoop eventLoop : this.activeChildren) {
            eventLoop.shutdownGracefully(l, l2, timeUnit);
        }
        for (EventLoop eventLoop : this.idleChildren) {
            eventLoop.shutdownGracefully(l, l2, timeUnit);
        }
        if (this.isTerminated()) {
            this.terminationFuture.trySuccess(null);
        }
        return this.terminationFuture();
    }

    @Override
    public boolean isTerminated() {
        for (EventLoop eventLoop : this.activeChildren) {
            if (eventLoop.isTerminated()) continue;
            return false;
        }
        for (EventLoop eventLoop : this.idleChildren) {
            if (eventLoop.isTerminated()) continue;
            return false;
        }
        return true;
    }

    @Override
    public boolean awaitTermination(long l, TimeUnit timeUnit) throws InterruptedException {
        long l2;
        long l3 = System.nanoTime() + timeUnit.toNanos(l);
        for (EventLoop eventLoop : this.activeChildren) {
            do {
                if ((l2 = l3 - System.nanoTime()) > 0L) continue;
                return this.isTerminated();
            } while (!eventLoop.awaitTermination(l2, TimeUnit.NANOSECONDS));
        }
        for (EventLoop eventLoop : this.idleChildren) {
            do {
                if ((l2 = l3 - System.nanoTime()) > 0L) continue;
                return this.isTerminated();
            } while (!eventLoop.awaitTermination(l2, TimeUnit.NANOSECONDS));
        }
        return this.isTerminated();
    }
}

