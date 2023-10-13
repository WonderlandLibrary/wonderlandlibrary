/*
 * Decompiled with CFR 0.150.
 */
package io.netty.channel;

import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.AbstractChannel;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandler;
import io.netty.channel.ChannelOutboundBuffer;
import io.netty.channel.ChannelOutboundHandler;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.ChannelProgressivePromise;
import io.netty.channel.ChannelPromise;
import io.netty.channel.DefaultChannelPipeline;
import io.netty.channel.DefaultChannelProgressivePromise;
import io.netty.channel.DefaultChannelPromise;
import io.netty.channel.FailedChannelFuture;
import io.netty.channel.SucceededChannelFuture;
import io.netty.channel.VoidChannelPromise;
import io.netty.util.DefaultAttributeMap;
import io.netty.util.Recycler;
import io.netty.util.ReferenceCountUtil;
import io.netty.util.concurrent.EventExecutor;
import io.netty.util.concurrent.EventExecutorGroup;
import io.netty.util.internal.StringUtil;
import java.net.SocketAddress;

final class DefaultChannelHandlerContext
extends DefaultAttributeMap
implements ChannelHandlerContext {
    volatile DefaultChannelHandlerContext next;
    volatile DefaultChannelHandlerContext prev;
    private final boolean inbound;
    private final boolean outbound;
    private final AbstractChannel channel;
    private final DefaultChannelPipeline pipeline;
    private final String name;
    private final ChannelHandler handler;
    private boolean removed;
    final EventExecutor executor;
    private ChannelFuture succeededFuture;
    private Runnable invokeChannelReadCompleteTask;
    private Runnable invokeReadTask;
    private Runnable invokeFlushTask;
    private Runnable invokeChannelWritableStateChangedTask;

    DefaultChannelHandlerContext(DefaultChannelPipeline pipeline, EventExecutorGroup group, String name, ChannelHandler handler) {
        if (name == null) {
            throw new NullPointerException("name");
        }
        if (handler == null) {
            throw new NullPointerException("handler");
        }
        this.channel = pipeline.channel;
        this.pipeline = pipeline;
        this.name = name;
        this.handler = handler;
        if (group != null) {
            EventExecutor childExecutor = pipeline.childExecutors.get(group);
            if (childExecutor == null) {
                childExecutor = group.next();
                pipeline.childExecutors.put(group, childExecutor);
            }
            this.executor = childExecutor;
        } else {
            this.executor = null;
        }
        this.inbound = handler instanceof ChannelInboundHandler;
        this.outbound = handler instanceof ChannelOutboundHandler;
    }

    void teardown() {
        EventExecutor executor = this.executor();
        if (executor.inEventLoop()) {
            this.teardown0();
        } else {
            executor.execute(new Runnable(){

                @Override
                public void run() {
                    DefaultChannelHandlerContext.this.teardown0();
                }
            });
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    private void teardown0() {
        DefaultChannelHandlerContext prev = this.prev;
        if (prev != null) {
            DefaultChannelPipeline defaultChannelPipeline = this.pipeline;
            synchronized (defaultChannelPipeline) {
                this.pipeline.remove0(this);
            }
            prev.teardown();
        }
    }

    @Override
    public Channel channel() {
        return this.channel;
    }

    @Override
    public ChannelPipeline pipeline() {
        return this.pipeline;
    }

    @Override
    public ByteBufAllocator alloc() {
        return this.channel().config().getAllocator();
    }

    @Override
    public EventExecutor executor() {
        if (this.executor == null) {
            return this.channel().eventLoop();
        }
        return this.executor;
    }

    @Override
    public ChannelHandler handler() {
        return this.handler;
    }

    @Override
    public String name() {
        return this.name;
    }

    @Override
    public ChannelHandlerContext fireChannelRegistered() {
        final DefaultChannelHandlerContext next = this.findContextInbound();
        EventExecutor executor = next.executor();
        if (executor.inEventLoop()) {
            next.invokeChannelRegistered();
        } else {
            executor.execute(new Runnable(){

                @Override
                public void run() {
                    next.invokeChannelRegistered();
                }
            });
        }
        return this;
    }

    private void invokeChannelRegistered() {
        try {
            ((ChannelInboundHandler)this.handler).channelRegistered(this);
        }
        catch (Throwable t) {
            this.notifyHandlerException(t);
        }
    }

    @Override
    public ChannelHandlerContext fireChannelUnregistered() {
        final DefaultChannelHandlerContext next = this.findContextInbound();
        EventExecutor executor = next.executor();
        if (executor.inEventLoop()) {
            next.invokeChannelUnregistered();
        } else {
            executor.execute(new Runnable(){

                @Override
                public void run() {
                    next.invokeChannelUnregistered();
                }
            });
        }
        return this;
    }

    private void invokeChannelUnregistered() {
        try {
            ((ChannelInboundHandler)this.handler).channelUnregistered(this);
        }
        catch (Throwable t) {
            this.notifyHandlerException(t);
        }
    }

    @Override
    public ChannelHandlerContext fireChannelActive() {
        final DefaultChannelHandlerContext next = this.findContextInbound();
        EventExecutor executor = next.executor();
        if (executor.inEventLoop()) {
            next.invokeChannelActive();
        } else {
            executor.execute(new Runnable(){

                @Override
                public void run() {
                    next.invokeChannelActive();
                }
            });
        }
        return this;
    }

    private void invokeChannelActive() {
        try {
            ((ChannelInboundHandler)this.handler).channelActive(this);
        }
        catch (Throwable t) {
            this.notifyHandlerException(t);
        }
    }

    @Override
    public ChannelHandlerContext fireChannelInactive() {
        final DefaultChannelHandlerContext next = this.findContextInbound();
        EventExecutor executor = next.executor();
        if (executor.inEventLoop()) {
            next.invokeChannelInactive();
        } else {
            executor.execute(new Runnable(){

                @Override
                public void run() {
                    next.invokeChannelInactive();
                }
            });
        }
        return this;
    }

    private void invokeChannelInactive() {
        try {
            ((ChannelInboundHandler)this.handler).channelInactive(this);
        }
        catch (Throwable t) {
            this.notifyHandlerException(t);
        }
    }

    @Override
    public ChannelHandlerContext fireExceptionCaught(final Throwable cause) {
        block5: {
            if (cause == null) {
                throw new NullPointerException("cause");
            }
            final DefaultChannelHandlerContext next = this.next;
            EventExecutor executor = next.executor();
            if (executor.inEventLoop()) {
                next.invokeExceptionCaught(cause);
            } else {
                try {
                    executor.execute(new Runnable(){

                        @Override
                        public void run() {
                            next.invokeExceptionCaught(cause);
                        }
                    });
                }
                catch (Throwable t) {
                    if (!DefaultChannelPipeline.logger.isWarnEnabled()) break block5;
                    DefaultChannelPipeline.logger.warn("Failed to submit an exceptionCaught() event.", t);
                    DefaultChannelPipeline.logger.warn("The exceptionCaught() event that was failed to submit was:", cause);
                }
            }
        }
        return this;
    }

    private void invokeExceptionCaught(Throwable cause) {
        block2: {
            try {
                this.handler.exceptionCaught(this, cause);
            }
            catch (Throwable t) {
                if (!DefaultChannelPipeline.logger.isWarnEnabled()) break block2;
                DefaultChannelPipeline.logger.warn("An exception was thrown by a user handler's exceptionCaught() method while handling the following exception:", cause);
            }
        }
    }

    @Override
    public ChannelHandlerContext fireUserEventTriggered(final Object event) {
        if (event == null) {
            throw new NullPointerException("event");
        }
        final DefaultChannelHandlerContext next = this.findContextInbound();
        EventExecutor executor = next.executor();
        if (executor.inEventLoop()) {
            next.invokeUserEventTriggered(event);
        } else {
            executor.execute(new Runnable(){

                @Override
                public void run() {
                    next.invokeUserEventTriggered(event);
                }
            });
        }
        return this;
    }

    private void invokeUserEventTriggered(Object event) {
        try {
            ((ChannelInboundHandler)this.handler).userEventTriggered(this, event);
        }
        catch (Throwable t) {
            this.notifyHandlerException(t);
        }
    }

    @Override
    public ChannelHandlerContext fireChannelRead(final Object msg) {
        if (msg == null) {
            throw new NullPointerException("msg");
        }
        final DefaultChannelHandlerContext next = this.findContextInbound();
        EventExecutor executor = next.executor();
        if (executor.inEventLoop()) {
            next.invokeChannelRead(msg);
        } else {
            executor.execute(new Runnable(){

                @Override
                public void run() {
                    next.invokeChannelRead(msg);
                }
            });
        }
        return this;
    }

    private void invokeChannelRead(Object msg) {
        try {
            ((ChannelInboundHandler)this.handler).channelRead(this, msg);
        }
        catch (Throwable t) {
            this.notifyHandlerException(t);
        }
    }

    @Override
    public ChannelHandlerContext fireChannelReadComplete() {
        final DefaultChannelHandlerContext next = this.findContextInbound();
        EventExecutor executor = next.executor();
        if (executor.inEventLoop()) {
            next.invokeChannelReadComplete();
        } else {
            Runnable task = next.invokeChannelReadCompleteTask;
            if (task == null) {
                next.invokeChannelReadCompleteTask = task = new Runnable(){

                    @Override
                    public void run() {
                        next.invokeChannelReadComplete();
                    }
                };
            }
            executor.execute(task);
        }
        return this;
    }

    private void invokeChannelReadComplete() {
        try {
            ((ChannelInboundHandler)this.handler).channelReadComplete(this);
        }
        catch (Throwable t) {
            this.notifyHandlerException(t);
        }
    }

    @Override
    public ChannelHandlerContext fireChannelWritabilityChanged() {
        final DefaultChannelHandlerContext next = this.findContextInbound();
        EventExecutor executor = next.executor();
        if (executor.inEventLoop()) {
            next.invokeChannelWritabilityChanged();
        } else {
            Runnable task = next.invokeChannelWritableStateChangedTask;
            if (task == null) {
                next.invokeChannelWritableStateChangedTask = task = new Runnable(){

                    @Override
                    public void run() {
                        next.invokeChannelWritabilityChanged();
                    }
                };
            }
            executor.execute(task);
        }
        return this;
    }

    private void invokeChannelWritabilityChanged() {
        try {
            ((ChannelInboundHandler)this.handler).channelWritabilityChanged(this);
        }
        catch (Throwable t) {
            this.notifyHandlerException(t);
        }
    }

    @Override
    public ChannelFuture bind(SocketAddress localAddress) {
        return this.bind(localAddress, this.newPromise());
    }

    @Override
    public ChannelFuture connect(SocketAddress remoteAddress) {
        return this.connect(remoteAddress, this.newPromise());
    }

    @Override
    public ChannelFuture connect(SocketAddress remoteAddress, SocketAddress localAddress) {
        return this.connect(remoteAddress, localAddress, this.newPromise());
    }

    @Override
    public ChannelFuture disconnect() {
        return this.disconnect(this.newPromise());
    }

    @Override
    public ChannelFuture close() {
        return this.close(this.newPromise());
    }

    @Override
    public ChannelFuture deregister() {
        return this.deregister(this.newPromise());
    }

    @Override
    public ChannelFuture bind(final SocketAddress localAddress, final ChannelPromise promise) {
        if (localAddress == null) {
            throw new NullPointerException("localAddress");
        }
        this.validatePromise(promise, false);
        final DefaultChannelHandlerContext next = this.findContextOutbound();
        EventExecutor executor = next.executor();
        if (executor.inEventLoop()) {
            next.invokeBind(localAddress, promise);
        } else {
            DefaultChannelHandlerContext.safeExecute(executor, new Runnable(){

                @Override
                public void run() {
                    next.invokeBind(localAddress, promise);
                }
            }, promise, null);
        }
        return promise;
    }

    private void invokeBind(SocketAddress localAddress, ChannelPromise promise) {
        try {
            ((ChannelOutboundHandler)this.handler).bind(this, localAddress, promise);
        }
        catch (Throwable t) {
            DefaultChannelHandlerContext.notifyOutboundHandlerException(t, promise);
        }
    }

    @Override
    public ChannelFuture connect(SocketAddress remoteAddress, ChannelPromise promise) {
        return this.connect(remoteAddress, null, promise);
    }

    @Override
    public ChannelFuture connect(final SocketAddress remoteAddress, final SocketAddress localAddress, final ChannelPromise promise) {
        if (remoteAddress == null) {
            throw new NullPointerException("remoteAddress");
        }
        this.validatePromise(promise, false);
        final DefaultChannelHandlerContext next = this.findContextOutbound();
        EventExecutor executor = next.executor();
        if (executor.inEventLoop()) {
            next.invokeConnect(remoteAddress, localAddress, promise);
        } else {
            DefaultChannelHandlerContext.safeExecute(executor, new Runnable(){

                @Override
                public void run() {
                    next.invokeConnect(remoteAddress, localAddress, promise);
                }
            }, promise, null);
        }
        return promise;
    }

    private void invokeConnect(SocketAddress remoteAddress, SocketAddress localAddress, ChannelPromise promise) {
        try {
            ((ChannelOutboundHandler)this.handler).connect(this, remoteAddress, localAddress, promise);
        }
        catch (Throwable t) {
            DefaultChannelHandlerContext.notifyOutboundHandlerException(t, promise);
        }
    }

    @Override
    public ChannelFuture disconnect(final ChannelPromise promise) {
        this.validatePromise(promise, false);
        final DefaultChannelHandlerContext next = this.findContextOutbound();
        EventExecutor executor = next.executor();
        if (executor.inEventLoop()) {
            if (!this.channel().metadata().hasDisconnect()) {
                next.invokeClose(promise);
            } else {
                next.invokeDisconnect(promise);
            }
        } else {
            DefaultChannelHandlerContext.safeExecute(executor, new Runnable(){

                @Override
                public void run() {
                    if (!DefaultChannelHandlerContext.this.channel().metadata().hasDisconnect()) {
                        next.invokeClose(promise);
                    } else {
                        next.invokeDisconnect(promise);
                    }
                }
            }, promise, null);
        }
        return promise;
    }

    private void invokeDisconnect(ChannelPromise promise) {
        try {
            ((ChannelOutboundHandler)this.handler).disconnect(this, promise);
        }
        catch (Throwable t) {
            DefaultChannelHandlerContext.notifyOutboundHandlerException(t, promise);
        }
    }

    @Override
    public ChannelFuture close(final ChannelPromise promise) {
        this.validatePromise(promise, false);
        final DefaultChannelHandlerContext next = this.findContextOutbound();
        EventExecutor executor = next.executor();
        if (executor.inEventLoop()) {
            next.invokeClose(promise);
        } else {
            DefaultChannelHandlerContext.safeExecute(executor, new Runnable(){

                @Override
                public void run() {
                    next.invokeClose(promise);
                }
            }, promise, null);
        }
        return promise;
    }

    private void invokeClose(ChannelPromise promise) {
        try {
            ((ChannelOutboundHandler)this.handler).close(this, promise);
        }
        catch (Throwable t) {
            DefaultChannelHandlerContext.notifyOutboundHandlerException(t, promise);
        }
    }

    @Override
    public ChannelFuture deregister(final ChannelPromise promise) {
        this.validatePromise(promise, false);
        final DefaultChannelHandlerContext next = this.findContextOutbound();
        EventExecutor executor = next.executor();
        if (executor.inEventLoop()) {
            next.invokeDeregister(promise);
        } else {
            DefaultChannelHandlerContext.safeExecute(executor, new Runnable(){

                @Override
                public void run() {
                    next.invokeDeregister(promise);
                }
            }, promise, null);
        }
        return promise;
    }

    private void invokeDeregister(ChannelPromise promise) {
        try {
            ((ChannelOutboundHandler)this.handler).deregister(this, promise);
        }
        catch (Throwable t) {
            DefaultChannelHandlerContext.notifyOutboundHandlerException(t, promise);
        }
    }

    @Override
    public ChannelHandlerContext read() {
        final DefaultChannelHandlerContext next = this.findContextOutbound();
        EventExecutor executor = next.executor();
        if (executor.inEventLoop()) {
            next.invokeRead();
        } else {
            Runnable task = next.invokeReadTask;
            if (task == null) {
                next.invokeReadTask = task = new Runnable(){

                    @Override
                    public void run() {
                        next.invokeRead();
                    }
                };
            }
            executor.execute(task);
        }
        return this;
    }

    private void invokeRead() {
        try {
            ((ChannelOutboundHandler)this.handler).read(this);
        }
        catch (Throwable t) {
            this.notifyHandlerException(t);
        }
    }

    @Override
    public ChannelFuture write(Object msg) {
        return this.write(msg, this.newPromise());
    }

    @Override
    public ChannelFuture write(Object msg, ChannelPromise promise) {
        if (msg == null) {
            throw new NullPointerException("msg");
        }
        this.validatePromise(promise, true);
        this.write(msg, false, promise);
        return promise;
    }

    private void invokeWrite(Object msg, ChannelPromise promise) {
        try {
            ((ChannelOutboundHandler)this.handler).write(this, msg, promise);
        }
        catch (Throwable t) {
            DefaultChannelHandlerContext.notifyOutboundHandlerException(t, promise);
        }
    }

    @Override
    public ChannelHandlerContext flush() {
        final DefaultChannelHandlerContext next = this.findContextOutbound();
        EventExecutor executor = next.executor();
        if (executor.inEventLoop()) {
            next.invokeFlush();
        } else {
            Runnable task = next.invokeFlushTask;
            if (task == null) {
                next.invokeFlushTask = task = new Runnable(){

                    @Override
                    public void run() {
                        next.invokeFlush();
                    }
                };
            }
            DefaultChannelHandlerContext.safeExecute(executor, task, this.channel.voidPromise(), null);
        }
        return this;
    }

    private void invokeFlush() {
        try {
            ((ChannelOutboundHandler)this.handler).flush(this);
        }
        catch (Throwable t) {
            this.notifyHandlerException(t);
        }
    }

    @Override
    public ChannelFuture writeAndFlush(Object msg, ChannelPromise promise) {
        if (msg == null) {
            throw new NullPointerException("msg");
        }
        this.validatePromise(promise, true);
        this.write(msg, true, promise);
        return promise;
    }

    private void write(Object msg, boolean flush, ChannelPromise promise) {
        DefaultChannelHandlerContext next = this.findContextOutbound();
        EventExecutor executor = next.executor();
        if (executor.inEventLoop()) {
            next.invokeWrite(msg, promise);
            if (flush) {
                next.invokeFlush();
            }
        } else {
            ChannelOutboundBuffer buffer;
            int size = this.channel.estimatorHandle().size(msg);
            if (size > 0 && (buffer = this.channel.unsafe().outboundBuffer()) != null) {
                buffer.incrementPendingOutboundBytes(size);
            }
            DefaultChannelHandlerContext.safeExecute(executor, WriteTask.newInstance(next, msg, size, flush, promise), promise, msg);
        }
    }

    @Override
    public ChannelFuture writeAndFlush(Object msg) {
        return this.writeAndFlush(msg, this.newPromise());
    }

    private static void notifyOutboundHandlerException(Throwable cause, ChannelPromise promise) {
        if (promise instanceof VoidChannelPromise) {
            return;
        }
        if (!promise.tryFailure(cause) && DefaultChannelPipeline.logger.isWarnEnabled()) {
            DefaultChannelPipeline.logger.warn("Failed to fail the promise because it's done already: {}", (Object)promise, (Object)cause);
        }
    }

    private void notifyHandlerException(Throwable cause) {
        if (DefaultChannelHandlerContext.inExceptionCaught(cause)) {
            if (DefaultChannelPipeline.logger.isWarnEnabled()) {
                DefaultChannelPipeline.logger.warn("An exception was thrown by a user handler while handling an exceptionCaught event", cause);
            }
            return;
        }
        this.invokeExceptionCaught(cause);
    }

    private static boolean inExceptionCaught(Throwable cause) {
        do {
            StackTraceElement[] trace;
            if ((trace = cause.getStackTrace()) == null) continue;
            for (StackTraceElement t : trace) {
                if (t == null) break;
                if (!"exceptionCaught".equals(t.getMethodName())) continue;
                return true;
            }
        } while ((cause = cause.getCause()) != null);
        return false;
    }

    @Override
    public ChannelPromise newPromise() {
        return new DefaultChannelPromise(this.channel(), this.executor());
    }

    @Override
    public ChannelProgressivePromise newProgressivePromise() {
        return new DefaultChannelProgressivePromise(this.channel(), this.executor());
    }

    @Override
    public ChannelFuture newSucceededFuture() {
        ChannelFuture succeededFuture = this.succeededFuture;
        if (succeededFuture == null) {
            this.succeededFuture = succeededFuture = new SucceededChannelFuture(this.channel(), this.executor());
        }
        return succeededFuture;
    }

    @Override
    public ChannelFuture newFailedFuture(Throwable cause) {
        return new FailedChannelFuture(this.channel(), this.executor(), cause);
    }

    private void validatePromise(ChannelPromise promise, boolean allowVoidPromise) {
        if (promise == null) {
            throw new NullPointerException("promise");
        }
        if (promise.isDone()) {
            throw new IllegalArgumentException("promise already done: " + promise);
        }
        if (promise.channel() != this.channel()) {
            throw new IllegalArgumentException(String.format("promise.channel does not match: %s (expected: %s)", promise.channel(), this.channel()));
        }
        if (promise.getClass() == DefaultChannelPromise.class) {
            return;
        }
        if (!allowVoidPromise && promise instanceof VoidChannelPromise) {
            throw new IllegalArgumentException(StringUtil.simpleClassName(VoidChannelPromise.class) + " not allowed for this operation");
        }
        if (promise instanceof AbstractChannel.CloseFuture) {
            throw new IllegalArgumentException(StringUtil.simpleClassName(AbstractChannel.CloseFuture.class) + " not allowed in a pipeline");
        }
    }

    private DefaultChannelHandlerContext findContextInbound() {
        DefaultChannelHandlerContext ctx = this;
        do {
            ctx = ctx.next;
        } while (!ctx.inbound);
        return ctx;
    }

    private DefaultChannelHandlerContext findContextOutbound() {
        DefaultChannelHandlerContext ctx = this;
        do {
            ctx = ctx.prev;
        } while (!ctx.outbound);
        return ctx;
    }

    @Override
    public ChannelPromise voidPromise() {
        return this.channel.voidPromise();
    }

    void setRemoved() {
        this.removed = true;
    }

    @Override
    public boolean isRemoved() {
        return this.removed;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    private static void safeExecute(EventExecutor executor, Runnable runnable, ChannelPromise promise, Object msg) {
        try {
            executor.execute(runnable);
        }
        catch (Throwable cause) {
            try {
                promise.setFailure(cause);
            }
            finally {
                if (msg != null) {
                    ReferenceCountUtil.release(msg);
                }
            }
        }
    }

    static final class WriteTask
    implements Runnable {
        private DefaultChannelHandlerContext ctx;
        private Object msg;
        private ChannelPromise promise;
        private int size;
        private boolean flush;
        private static final Recycler<WriteTask> RECYCLER = new Recycler<WriteTask>(){

            @Override
            protected WriteTask newObject(Recycler.Handle handle) {
                return new WriteTask(handle);
            }
        };
        private final Recycler.Handle handle;

        private static WriteTask newInstance(DefaultChannelHandlerContext ctx, Object msg, int size, boolean flush, ChannelPromise promise) {
            WriteTask task = RECYCLER.get();
            task.ctx = ctx;
            task.msg = msg;
            task.promise = promise;
            task.size = size;
            task.flush = flush;
            return task;
        }

        private WriteTask(Recycler.Handle handle) {
            this.handle = handle;
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        @Override
        public void run() {
            try {
                ChannelOutboundBuffer buffer;
                if (this.size > 0 && (buffer = this.ctx.channel.unsafe().outboundBuffer()) != null) {
                    buffer.decrementPendingOutboundBytes(this.size);
                }
                this.ctx.invokeWrite(this.msg, this.promise);
                if (this.flush) {
                    this.ctx.invokeFlush();
                }
            }
            finally {
                this.ctx = null;
                this.msg = null;
                this.promise = null;
                RECYCLER.recycle(this, this.handle);
            }
        }
    }
}

