/*
 * Decompiled with CFR 0.152.
 */
package io.netty.channel;

import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.AbstractChannel;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
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
import io.netty.channel.SingleThreadEventLoop;
import io.netty.channel.SucceededChannelFuture;
import io.netty.channel.VoidChannelPromise;
import io.netty.util.DefaultAttributeMap;
import io.netty.util.Recycler;
import io.netty.util.ReferenceCountUtil;
import io.netty.util.concurrent.EventExecutor;
import io.netty.util.concurrent.EventExecutorGroup;
import io.netty.util.internal.OneTimeTask;
import io.netty.util.internal.RecyclableMpscLinkedQueueNode;
import io.netty.util.internal.StringUtil;
import java.net.SocketAddress;

abstract class AbstractChannelHandlerContext
extends DefaultAttributeMap
implements ChannelHandlerContext {
    private volatile Runnable invokeReadTask;
    private volatile Runnable invokeChannelWritableStateChangedTask;
    volatile AbstractChannelHandlerContext next;
    private final AbstractChannel channel;
    volatile AbstractChannelHandlerContext prev;
    private boolean removed;
    private volatile Runnable invokeFlushTask;
    private final boolean outbound;
    private volatile Runnable invokeChannelReadCompleteTask;
    private final boolean inbound;
    private final String name;
    private ChannelFuture succeededFuture;
    final EventExecutor executor;
    private final DefaultChannelPipeline pipeline;

    private void invokeChannelUnregistered() {
        try {
            ((ChannelInboundHandler)this.handler()).channelUnregistered(this);
        }
        catch (Throwable throwable) {
            this.notifyHandlerException(throwable);
        }
    }

    @Override
    public ChannelProgressivePromise newProgressivePromise() {
        return new DefaultChannelProgressivePromise(this.channel(), this.executor());
    }

    @Override
    public ChannelFuture close() {
        return this.close(this.newPromise());
    }

    private void invokeClose(ChannelPromise channelPromise) {
        try {
            ((ChannelOutboundHandler)this.handler()).close(this, channelPromise);
        }
        catch (Throwable throwable) {
            AbstractChannelHandlerContext.notifyOutboundHandlerException(throwable, channelPromise);
        }
    }

    @Override
    public Channel channel() {
        return this.channel;
    }

    @Override
    public ChannelPromise voidPromise() {
        return this.channel.voidPromise();
    }

    @Override
    public ChannelHandlerContext fireExceptionCaught(final Throwable throwable) {
        block5: {
            if (throwable == null) {
                throw new NullPointerException("cause");
            }
            final AbstractChannelHandlerContext abstractChannelHandlerContext = this.next;
            EventExecutor eventExecutor = abstractChannelHandlerContext.executor();
            if (eventExecutor.inEventLoop()) {
                abstractChannelHandlerContext.invokeExceptionCaught(throwable);
            } else {
                try {
                    eventExecutor.execute(new OneTimeTask(){

                        @Override
                        public void run() {
                            abstractChannelHandlerContext.invokeExceptionCaught(throwable);
                        }
                    });
                }
                catch (Throwable throwable2) {
                    if (!DefaultChannelPipeline.logger.isWarnEnabled()) break block5;
                    DefaultChannelPipeline.logger.warn("Failed to submit an exceptionCaught() event.", throwable2);
                    DefaultChannelPipeline.logger.warn("The exceptionCaught() event that was failed to submit was:", throwable);
                }
            }
        }
        return this;
    }

    void teardown() {
        EventExecutor eventExecutor = this.executor();
        if (eventExecutor.inEventLoop()) {
            this.teardown0();
        } else {
            eventExecutor.execute(new Runnable(){

                @Override
                public void run() {
                    AbstractChannelHandlerContext.this.teardown0();
                }
            });
        }
    }

    private void invokeChannelWritabilityChanged() {
        try {
            ((ChannelInboundHandler)this.handler()).channelWritabilityChanged(this);
        }
        catch (Throwable throwable) {
            this.notifyHandlerException(throwable);
        }
    }

    @Override
    public ChannelHandlerContext fireChannelWritabilityChanged() {
        final AbstractChannelHandlerContext abstractChannelHandlerContext = this.findContextInbound();
        EventExecutor eventExecutor = abstractChannelHandlerContext.executor();
        if (eventExecutor.inEventLoop()) {
            abstractChannelHandlerContext.invokeChannelWritabilityChanged();
        } else {
            Runnable runnable = abstractChannelHandlerContext.invokeChannelWritableStateChangedTask;
            if (runnable == null) {
                abstractChannelHandlerContext.invokeChannelWritableStateChangedTask = runnable = new Runnable(){

                    @Override
                    public void run() {
                        abstractChannelHandlerContext.invokeChannelWritabilityChanged();
                    }
                };
            }
            eventExecutor.execute(runnable);
        }
        return this;
    }

    private AbstractChannelHandlerContext findContextInbound() {
        AbstractChannelHandlerContext abstractChannelHandlerContext = this;
        do {
            abstractChannelHandlerContext = abstractChannelHandlerContext.next;
        } while (!abstractChannelHandlerContext.inbound);
        return abstractChannelHandlerContext;
    }

    private static void safeExecute(EventExecutor eventExecutor, Runnable runnable, ChannelPromise channelPromise, Object object) {
        block2: {
            try {
                eventExecutor.execute(runnable);
            }
            catch (Throwable throwable) {
                channelPromise.setFailure(throwable);
                if (object == null) break block2;
                ReferenceCountUtil.release(object);
            }
        }
    }

    @Override
    public ChannelFuture bind(SocketAddress socketAddress) {
        return this.bind(socketAddress, this.newPromise());
    }

    private void invokeBind(SocketAddress socketAddress, ChannelPromise channelPromise) {
        try {
            ((ChannelOutboundHandler)this.handler()).bind(this, socketAddress, channelPromise);
        }
        catch (Throwable throwable) {
            AbstractChannelHandlerContext.notifyOutboundHandlerException(throwable, channelPromise);
        }
    }

    private void notifyHandlerException(Throwable throwable) {
        if (AbstractChannelHandlerContext.inExceptionCaught(throwable)) {
            if (DefaultChannelPipeline.logger.isWarnEnabled()) {
                DefaultChannelPipeline.logger.warn("An exception was thrown by a user handler while handling an exceptionCaught event", throwable);
            }
            return;
        }
        this.invokeExceptionCaught(throwable);
    }

    private void invokeDeregister(ChannelPromise channelPromise) {
        try {
            ((ChannelOutboundHandler)this.handler()).deregister(this, channelPromise);
        }
        catch (Throwable throwable) {
            AbstractChannelHandlerContext.notifyOutboundHandlerException(throwable, channelPromise);
        }
    }

    @Override
    public ChannelFuture close(final ChannelPromise channelPromise) {
        if (!this.validatePromise(channelPromise, false)) {
            return channelPromise;
        }
        final AbstractChannelHandlerContext abstractChannelHandlerContext = this.findContextOutbound();
        EventExecutor eventExecutor = abstractChannelHandlerContext.executor();
        if (eventExecutor.inEventLoop()) {
            abstractChannelHandlerContext.invokeClose(channelPromise);
        } else {
            AbstractChannelHandlerContext.safeExecute(eventExecutor, new OneTimeTask(){

                @Override
                public void run() {
                    abstractChannelHandlerContext.invokeClose(channelPromise);
                }
            }, channelPromise, null);
        }
        return channelPromise;
    }

    @Override
    public ChannelHandlerContext fireChannelRead(final Object object) {
        if (object == null) {
            throw new NullPointerException("msg");
        }
        final AbstractChannelHandlerContext abstractChannelHandlerContext = this.findContextInbound();
        EventExecutor eventExecutor = abstractChannelHandlerContext.executor();
        if (eventExecutor.inEventLoop()) {
            abstractChannelHandlerContext.invokeChannelRead(object);
        } else {
            eventExecutor.execute(new OneTimeTask(){

                @Override
                public void run() {
                    abstractChannelHandlerContext.invokeChannelRead(object);
                }
            });
        }
        return this;
    }

    @Override
    public ChannelFuture write(Object object) {
        return this.write(object, this.newPromise());
    }

    @Override
    public ChannelFuture deregister() {
        return this.deregister(this.newPromise());
    }

    @Override
    public ChannelFuture writeAndFlush(Object object) {
        return this.writeAndFlush(object, this.newPromise());
    }

    private static void notifyOutboundHandlerException(Throwable throwable, ChannelPromise channelPromise) {
        if (channelPromise instanceof VoidChannelPromise) {
            return;
        }
        if (!channelPromise.tryFailure(throwable) && DefaultChannelPipeline.logger.isWarnEnabled()) {
            DefaultChannelPipeline.logger.warn("Failed to fail the promise because it's done already: {}", (Object)channelPromise, (Object)throwable);
        }
    }

    @Override
    public ChannelHandlerContext fireChannelRegistered() {
        final AbstractChannelHandlerContext abstractChannelHandlerContext = this.findContextInbound();
        EventExecutor eventExecutor = abstractChannelHandlerContext.executor();
        if (eventExecutor.inEventLoop()) {
            abstractChannelHandlerContext.invokeChannelRegistered();
        } else {
            eventExecutor.execute(new OneTimeTask(){

                @Override
                public void run() {
                    abstractChannelHandlerContext.invokeChannelRegistered();
                }
            });
        }
        return this;
    }

    private void invokeDisconnect(ChannelPromise channelPromise) {
        try {
            ((ChannelOutboundHandler)this.handler()).disconnect(this, channelPromise);
        }
        catch (Throwable throwable) {
            AbstractChannelHandlerContext.notifyOutboundHandlerException(throwable, channelPromise);
        }
    }

    @Override
    public ChannelFuture connect(SocketAddress socketAddress, ChannelPromise channelPromise) {
        return this.connect(socketAddress, null, channelPromise);
    }

    @Override
    public ChannelFuture writeAndFlush(Object object, ChannelPromise channelPromise) {
        if (object == null) {
            throw new NullPointerException("msg");
        }
        if (!this.validatePromise(channelPromise, true)) {
            ReferenceCountUtil.release(object);
            return channelPromise;
        }
        this.write(object, true, channelPromise);
        return channelPromise;
    }

    @Override
    public ChannelHandlerContext fireChannelReadComplete() {
        final AbstractChannelHandlerContext abstractChannelHandlerContext = this.findContextInbound();
        EventExecutor eventExecutor = abstractChannelHandlerContext.executor();
        if (eventExecutor.inEventLoop()) {
            abstractChannelHandlerContext.invokeChannelReadComplete();
        } else {
            Runnable runnable = abstractChannelHandlerContext.invokeChannelReadCompleteTask;
            if (runnable == null) {
                abstractChannelHandlerContext.invokeChannelReadCompleteTask = runnable = new Runnable(){

                    @Override
                    public void run() {
                        abstractChannelHandlerContext.invokeChannelReadComplete();
                    }
                };
            }
            eventExecutor.execute(runnable);
        }
        return this;
    }

    @Override
    public ChannelFuture connect(final SocketAddress socketAddress, final SocketAddress socketAddress2, final ChannelPromise channelPromise) {
        if (socketAddress == null) {
            throw new NullPointerException("remoteAddress");
        }
        if (!this.validatePromise(channelPromise, false)) {
            return channelPromise;
        }
        final AbstractChannelHandlerContext abstractChannelHandlerContext = this.findContextOutbound();
        EventExecutor eventExecutor = abstractChannelHandlerContext.executor();
        if (eventExecutor.inEventLoop()) {
            abstractChannelHandlerContext.invokeConnect(socketAddress, socketAddress2, channelPromise);
        } else {
            AbstractChannelHandlerContext.safeExecute(eventExecutor, new OneTimeTask(){

                @Override
                public void run() {
                    abstractChannelHandlerContext.invokeConnect(socketAddress, socketAddress2, channelPromise);
                }
            }, channelPromise, null);
        }
        return channelPromise;
    }

    @Override
    public ChannelFuture bind(final SocketAddress socketAddress, final ChannelPromise channelPromise) {
        if (socketAddress == null) {
            throw new NullPointerException("localAddress");
        }
        if (!this.validatePromise(channelPromise, false)) {
            return channelPromise;
        }
        final AbstractChannelHandlerContext abstractChannelHandlerContext = this.findContextOutbound();
        EventExecutor eventExecutor = abstractChannelHandlerContext.executor();
        if (eventExecutor.inEventLoop()) {
            abstractChannelHandlerContext.invokeBind(socketAddress, channelPromise);
        } else {
            AbstractChannelHandlerContext.safeExecute(eventExecutor, new OneTimeTask(){

                @Override
                public void run() {
                    abstractChannelHandlerContext.invokeBind(socketAddress, channelPromise);
                }
            }, channelPromise, null);
        }
        return channelPromise;
    }

    private static boolean inExceptionCaught(Throwable throwable) {
        do {
            StackTraceElement[] stackTraceElementArray;
            if ((stackTraceElementArray = throwable.getStackTrace()) == null) continue;
            for (StackTraceElement stackTraceElement : stackTraceElementArray) {
                if (stackTraceElement == null) break;
                if (!"exceptionCaught".equals(stackTraceElement.getMethodName())) continue;
                return true;
            }
        } while ((throwable = throwable.getCause()) != null);
        return false;
    }

    @Override
    public ByteBufAllocator alloc() {
        return this.channel().config().getAllocator();
    }

    private void teardown0() {
        AbstractChannelHandlerContext abstractChannelHandlerContext = this.prev;
        if (abstractChannelHandlerContext != null) {
            DefaultChannelPipeline defaultChannelPipeline = this.pipeline;
            synchronized (defaultChannelPipeline) {
                this.pipeline.remove0(this);
            }
            abstractChannelHandlerContext.teardown();
        }
    }

    @Override
    public ChannelHandlerContext fireUserEventTriggered(final Object object) {
        if (object == null) {
            throw new NullPointerException("event");
        }
        final AbstractChannelHandlerContext abstractChannelHandlerContext = this.findContextInbound();
        EventExecutor eventExecutor = abstractChannelHandlerContext.executor();
        if (eventExecutor.inEventLoop()) {
            abstractChannelHandlerContext.invokeUserEventTriggered(object);
        } else {
            eventExecutor.execute(new OneTimeTask(){

                @Override
                public void run() {
                    abstractChannelHandlerContext.invokeUserEventTriggered(object);
                }
            });
        }
        return this;
    }

    private boolean validatePromise(ChannelPromise channelPromise, boolean bl) {
        if (channelPromise == null) {
            throw new NullPointerException("promise");
        }
        if (channelPromise.isDone()) {
            if (channelPromise.isCancelled()) {
                return false;
            }
            throw new IllegalArgumentException("promise already done: " + channelPromise);
        }
        if (channelPromise.channel() != this.channel()) {
            throw new IllegalArgumentException(String.format("promise.channel does not match: %s (expected: %s)", channelPromise.channel(), this.channel()));
        }
        if (channelPromise.getClass() == DefaultChannelPromise.class) {
            return true;
        }
        if (!bl && channelPromise instanceof VoidChannelPromise) {
            throw new IllegalArgumentException(StringUtil.simpleClassName(VoidChannelPromise.class) + " not allowed for this operation");
        }
        if (channelPromise instanceof AbstractChannel.CloseFuture) {
            throw new IllegalArgumentException(StringUtil.simpleClassName(AbstractChannel.CloseFuture.class) + " not allowed in a pipeline");
        }
        return true;
    }

    @Override
    public ChannelFuture newFailedFuture(Throwable throwable) {
        return new FailedChannelFuture(this.channel(), this.executor(), throwable);
    }

    private void invokeUserEventTriggered(Object object) {
        try {
            ((ChannelInboundHandler)this.handler()).userEventTriggered(this, object);
        }
        catch (Throwable throwable) {
            this.notifyHandlerException(throwable);
        }
    }

    private void invokeFlush() {
        try {
            ((ChannelOutboundHandler)this.handler()).flush(this);
        }
        catch (Throwable throwable) {
            this.notifyHandlerException(throwable);
        }
    }

    private void invokeRead() {
        try {
            ((ChannelOutboundHandler)this.handler()).read(this);
        }
        catch (Throwable throwable) {
            this.notifyHandlerException(throwable);
        }
    }

    private void invokeExceptionCaught(Throwable throwable) {
        block2: {
            try {
                this.handler().exceptionCaught(this, throwable);
            }
            catch (Throwable throwable2) {
                if (!DefaultChannelPipeline.logger.isWarnEnabled()) break block2;
                DefaultChannelPipeline.logger.warn("An exception was thrown by a user handler's exceptionCaught() method while handling the following exception:", throwable);
            }
        }
    }

    private void invokeChannelRead(Object object) {
        try {
            ((ChannelInboundHandler)this.handler()).channelRead(this, object);
        }
        catch (Throwable throwable) {
            this.notifyHandlerException(throwable);
        }
    }

    @Override
    public ChannelHandlerContext fireChannelActive() {
        final AbstractChannelHandlerContext abstractChannelHandlerContext = this.findContextInbound();
        EventExecutor eventExecutor = abstractChannelHandlerContext.executor();
        if (eventExecutor.inEventLoop()) {
            abstractChannelHandlerContext.invokeChannelActive();
        } else {
            eventExecutor.execute(new OneTimeTask(){

                @Override
                public void run() {
                    abstractChannelHandlerContext.invokeChannelActive();
                }
            });
        }
        return this;
    }

    @Override
    public ChannelHandlerContext read() {
        final AbstractChannelHandlerContext abstractChannelHandlerContext = this.findContextOutbound();
        EventExecutor eventExecutor = abstractChannelHandlerContext.executor();
        if (eventExecutor.inEventLoop()) {
            abstractChannelHandlerContext.invokeRead();
        } else {
            Runnable runnable = abstractChannelHandlerContext.invokeReadTask;
            if (runnable == null) {
                abstractChannelHandlerContext.invokeReadTask = runnable = new Runnable(){

                    @Override
                    public void run() {
                        abstractChannelHandlerContext.invokeRead();
                    }
                };
            }
            eventExecutor.execute(runnable);
        }
        return this;
    }

    private void invokeConnect(SocketAddress socketAddress, SocketAddress socketAddress2, ChannelPromise channelPromise) {
        try {
            ((ChannelOutboundHandler)this.handler()).connect(this, socketAddress, socketAddress2, channelPromise);
        }
        catch (Throwable throwable) {
            AbstractChannelHandlerContext.notifyOutboundHandlerException(throwable, channelPromise);
        }
    }

    private void write(Object object, boolean bl, ChannelPromise channelPromise) {
        AbstractChannelHandlerContext abstractChannelHandlerContext = this.findContextOutbound();
        EventExecutor eventExecutor = abstractChannelHandlerContext.executor();
        if (eventExecutor.inEventLoop()) {
            abstractChannelHandlerContext.invokeWrite(object, channelPromise);
            if (bl) {
                abstractChannelHandlerContext.invokeFlush();
            }
        } else {
            Object object2;
            int n = this.channel.estimatorHandle().size(object);
            if (n > 0 && (object2 = this.channel.unsafe().outboundBuffer()) != null) {
                ((ChannelOutboundBuffer)object2).incrementPendingOutboundBytes(n);
            }
            object2 = bl ? WriteAndFlushTask.newInstance(abstractChannelHandlerContext, object, n, channelPromise) : WriteTask.newInstance(abstractChannelHandlerContext, object, n, channelPromise);
            AbstractChannelHandlerContext.safeExecute(eventExecutor, (Runnable)object2, channelPromise, object);
        }
    }

    @Override
    public ChannelFuture connect(SocketAddress socketAddress, SocketAddress socketAddress2) {
        return this.connect(socketAddress, socketAddress2, this.newPromise());
    }

    @Override
    public ChannelPromise newPromise() {
        return new DefaultChannelPromise(this.channel(), this.executor());
    }

    private void invokeWrite(Object object, ChannelPromise channelPromise) {
        try {
            ((ChannelOutboundHandler)this.handler()).write(this, object, channelPromise);
        }
        catch (Throwable throwable) {
            AbstractChannelHandlerContext.notifyOutboundHandlerException(throwable, channelPromise);
        }
    }

    AbstractChannelHandlerContext(DefaultChannelPipeline defaultChannelPipeline, EventExecutorGroup eventExecutorGroup, String string, boolean bl, boolean bl2) {
        if (string == null) {
            throw new NullPointerException("name");
        }
        this.channel = defaultChannelPipeline.channel;
        this.pipeline = defaultChannelPipeline;
        this.name = string;
        if (eventExecutorGroup != null) {
            EventExecutor eventExecutor = defaultChannelPipeline.childExecutors.get(eventExecutorGroup);
            if (eventExecutor == null) {
                eventExecutor = eventExecutorGroup.next();
                defaultChannelPipeline.childExecutors.put(eventExecutorGroup, eventExecutor);
            }
            this.executor = eventExecutor;
        } else {
            this.executor = null;
        }
        this.inbound = bl;
        this.outbound = bl2;
    }

    @Override
    public ChannelFuture newSucceededFuture() {
        ChannelFuture channelFuture = this.succeededFuture;
        if (channelFuture == null) {
            this.succeededFuture = channelFuture = new SucceededChannelFuture(this.channel(), this.executor());
        }
        return channelFuture;
    }

    @Override
    public ChannelFuture disconnect(final ChannelPromise channelPromise) {
        if (!this.validatePromise(channelPromise, false)) {
            return channelPromise;
        }
        final AbstractChannelHandlerContext abstractChannelHandlerContext = this.findContextOutbound();
        EventExecutor eventExecutor = abstractChannelHandlerContext.executor();
        if (eventExecutor.inEventLoop()) {
            if (!this.channel().metadata().hasDisconnect()) {
                abstractChannelHandlerContext.invokeClose(channelPromise);
            } else {
                abstractChannelHandlerContext.invokeDisconnect(channelPromise);
            }
        } else {
            AbstractChannelHandlerContext.safeExecute(eventExecutor, new OneTimeTask(){

                @Override
                public void run() {
                    if (!AbstractChannelHandlerContext.this.channel().metadata().hasDisconnect()) {
                        abstractChannelHandlerContext.invokeClose(channelPromise);
                    } else {
                        abstractChannelHandlerContext.invokeDisconnect(channelPromise);
                    }
                }
            }, channelPromise, null);
        }
        return channelPromise;
    }

    private void invokeChannelRegistered() {
        try {
            ((ChannelInboundHandler)this.handler()).channelRegistered(this);
        }
        catch (Throwable throwable) {
            this.notifyHandlerException(throwable);
        }
    }

    @Override
    public EventExecutor executor() {
        if (this.executor == null) {
            return this.channel().eventLoop();
        }
        return this.executor;
    }

    @Override
    public ChannelHandlerContext fireChannelInactive() {
        final AbstractChannelHandlerContext abstractChannelHandlerContext = this.findContextInbound();
        EventExecutor eventExecutor = abstractChannelHandlerContext.executor();
        if (eventExecutor.inEventLoop()) {
            abstractChannelHandlerContext.invokeChannelInactive();
        } else {
            eventExecutor.execute(new OneTimeTask(){

                @Override
                public void run() {
                    abstractChannelHandlerContext.invokeChannelInactive();
                }
            });
        }
        return this;
    }

    @Override
    public ChannelHandlerContext fireChannelUnregistered() {
        final AbstractChannelHandlerContext abstractChannelHandlerContext = this.findContextInbound();
        EventExecutor eventExecutor = abstractChannelHandlerContext.executor();
        if (eventExecutor.inEventLoop()) {
            abstractChannelHandlerContext.invokeChannelUnregistered();
        } else {
            eventExecutor.execute(new OneTimeTask(){

                @Override
                public void run() {
                    abstractChannelHandlerContext.invokeChannelUnregistered();
                }
            });
        }
        return this;
    }

    private AbstractChannelHandlerContext findContextOutbound() {
        AbstractChannelHandlerContext abstractChannelHandlerContext = this;
        do {
            abstractChannelHandlerContext = abstractChannelHandlerContext.prev;
        } while (!abstractChannelHandlerContext.outbound);
        return abstractChannelHandlerContext;
    }

    @Override
    public ChannelPipeline pipeline() {
        return this.pipeline;
    }

    @Override
    public ChannelFuture disconnect() {
        return this.disconnect(this.newPromise());
    }

    @Override
    public ChannelFuture connect(SocketAddress socketAddress) {
        return this.connect(socketAddress, this.newPromise());
    }

    private void invokeChannelReadComplete() {
        try {
            ((ChannelInboundHandler)this.handler()).channelReadComplete(this);
        }
        catch (Throwable throwable) {
            this.notifyHandlerException(throwable);
        }
    }

    @Override
    public String name() {
        return this.name;
    }

    @Override
    public boolean isRemoved() {
        return this.removed;
    }

    @Override
    public ChannelFuture write(Object object, ChannelPromise channelPromise) {
        if (object == null) {
            throw new NullPointerException("msg");
        }
        if (!this.validatePromise(channelPromise, true)) {
            ReferenceCountUtil.release(object);
            return channelPromise;
        }
        this.write(object, false, channelPromise);
        return channelPromise;
    }

    @Override
    public ChannelFuture deregister(final ChannelPromise channelPromise) {
        if (!this.validatePromise(channelPromise, false)) {
            return channelPromise;
        }
        final AbstractChannelHandlerContext abstractChannelHandlerContext = this.findContextOutbound();
        EventExecutor eventExecutor = abstractChannelHandlerContext.executor();
        if (eventExecutor.inEventLoop()) {
            abstractChannelHandlerContext.invokeDeregister(channelPromise);
        } else {
            AbstractChannelHandlerContext.safeExecute(eventExecutor, new OneTimeTask(){

                @Override
                public void run() {
                    abstractChannelHandlerContext.invokeDeregister(channelPromise);
                }
            }, channelPromise, null);
        }
        return channelPromise;
    }

    @Override
    public ChannelHandlerContext flush() {
        final AbstractChannelHandlerContext abstractChannelHandlerContext = this.findContextOutbound();
        EventExecutor eventExecutor = abstractChannelHandlerContext.executor();
        if (eventExecutor.inEventLoop()) {
            abstractChannelHandlerContext.invokeFlush();
        } else {
            Runnable runnable = abstractChannelHandlerContext.invokeFlushTask;
            if (runnable == null) {
                abstractChannelHandlerContext.invokeFlushTask = runnable = new Runnable(){

                    @Override
                    public void run() {
                        abstractChannelHandlerContext.invokeFlush();
                    }
                };
            }
            AbstractChannelHandlerContext.safeExecute(eventExecutor, runnable, this.channel.voidPromise(), null);
        }
        return this;
    }

    private void invokeChannelInactive() {
        try {
            ((ChannelInboundHandler)this.handler()).channelInactive(this);
        }
        catch (Throwable throwable) {
            this.notifyHandlerException(throwable);
        }
    }

    private void invokeChannelActive() {
        try {
            ((ChannelInboundHandler)this.handler()).channelActive(this);
        }
        catch (Throwable throwable) {
            this.notifyHandlerException(throwable);
        }
    }

    void setRemoved() {
        this.removed = true;
    }

    static abstract class AbstractWriteTask
    extends RecyclableMpscLinkedQueueNode<Runnable>
    implements Runnable {
        private ChannelPromise promise;
        private AbstractChannelHandlerContext ctx;
        private Object msg;
        private int size;

        private AbstractWriteTask(Recycler.Handle handle) {
            super(handle);
        }

        protected static void init(AbstractWriteTask abstractWriteTask, AbstractChannelHandlerContext abstractChannelHandlerContext, Object object, int n, ChannelPromise channelPromise) {
            abstractWriteTask.ctx = abstractChannelHandlerContext;
            abstractWriteTask.msg = object;
            abstractWriteTask.promise = channelPromise;
            abstractWriteTask.size = n;
        }

        @Override
        public final void run() {
            ChannelOutboundBuffer channelOutboundBuffer;
            if (this.size > 0 && (channelOutboundBuffer = this.ctx.channel.unsafe().outboundBuffer()) != null) {
                channelOutboundBuffer.decrementPendingOutboundBytes(this.size);
            }
            this.write(this.ctx, this.msg, this.promise);
            this.ctx = null;
            this.msg = null;
            this.promise = null;
        }

        protected void write(AbstractChannelHandlerContext abstractChannelHandlerContext, Object object, ChannelPromise channelPromise) {
            abstractChannelHandlerContext.invokeWrite(object, channelPromise);
        }

        @Override
        public Runnable value() {
            return this;
        }
    }

    static final class WriteTask
    extends AbstractWriteTask
    implements SingleThreadEventLoop.NonWakeupRunnable {
        private static final Recycler<WriteTask> RECYCLER = new Recycler<WriteTask>(){

            @Override
            protected WriteTask newObject(Recycler.Handle handle) {
                return new WriteTask(handle);
            }
        };

        @Override
        protected void recycle(Recycler.Handle handle) {
            RECYCLER.recycle(this, handle);
        }

        private static WriteTask newInstance(AbstractChannelHandlerContext abstractChannelHandlerContext, Object object, int n, ChannelPromise channelPromise) {
            WriteTask writeTask = RECYCLER.get();
            WriteTask.init(writeTask, abstractChannelHandlerContext, object, n, channelPromise);
            return writeTask;
        }

        private WriteTask(Recycler.Handle handle) {
            super(handle);
        }
    }

    static final class WriteAndFlushTask
    extends AbstractWriteTask {
        private static final Recycler<WriteAndFlushTask> RECYCLER = new Recycler<WriteAndFlushTask>(){

            @Override
            protected WriteAndFlushTask newObject(Recycler.Handle handle) {
                return new WriteAndFlushTask(handle);
            }
        };

        @Override
        protected void recycle(Recycler.Handle handle) {
            RECYCLER.recycle(this, handle);
        }

        @Override
        public void write(AbstractChannelHandlerContext abstractChannelHandlerContext, Object object, ChannelPromise channelPromise) {
            super.write(abstractChannelHandlerContext, object, channelPromise);
            abstractChannelHandlerContext.invokeFlush();
        }

        private static WriteAndFlushTask newInstance(AbstractChannelHandlerContext abstractChannelHandlerContext, Object object, int n, ChannelPromise channelPromise) {
            WriteAndFlushTask writeAndFlushTask = RECYCLER.get();
            WriteAndFlushTask.init(writeAndFlushTask, abstractChannelHandlerContext, object, n, channelPromise);
            return writeAndFlushTask;
        }

        private WriteAndFlushTask(Recycler.Handle handle) {
            super(handle);
        }
    }
}

