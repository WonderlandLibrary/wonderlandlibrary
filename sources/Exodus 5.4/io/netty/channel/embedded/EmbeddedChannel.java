/*
 * Decompiled with CFR 0.152.
 */
package io.netty.channel.embedded;

import io.netty.channel.AbstractChannel;
import io.netty.channel.ChannelConfig;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelMetadata;
import io.netty.channel.ChannelOutboundBuffer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.ChannelPromise;
import io.netty.channel.DefaultChannelConfig;
import io.netty.channel.EventLoop;
import io.netty.channel.embedded.EmbeddedEventLoop;
import io.netty.channel.embedded.EmbeddedSocketAddress;
import io.netty.util.ReferenceCountUtil;
import io.netty.util.internal.PlatformDependent;
import io.netty.util.internal.RecyclableArrayList;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;
import java.net.SocketAddress;
import java.nio.channels.ClosedChannelException;
import java.util.ArrayDeque;
import java.util.Queue;

public class EmbeddedChannel
extends AbstractChannel {
    private final SocketAddress remoteAddress;
    private final EmbeddedEventLoop loop = new EmbeddedEventLoop();
    private static final InternalLogger logger = InternalLoggerFactory.getInstance(EmbeddedChannel.class);
    private static final ChannelMetadata METADATA = new ChannelMetadata(false);
    private final Queue<Object> inboundMessages;
    private Throwable lastException;
    private final SocketAddress localAddress;
    private final Queue<Object> outboundMessages;
    private final ChannelConfig config = new DefaultChannelConfig(this);
    private int state;

    @Override
    protected AbstractChannel.AbstractUnsafe newUnsafe() {
        return new DefaultUnsafe();
    }

    public EmbeddedChannel(ChannelHandler ... channelHandlerArray) {
        super(null);
        this.localAddress = new EmbeddedSocketAddress();
        this.remoteAddress = new EmbeddedSocketAddress();
        this.inboundMessages = new ArrayDeque<Object>();
        this.outboundMessages = new ArrayDeque<Object>();
        if (channelHandlerArray == null) {
            throw new NullPointerException("handlers");
        }
        int n = 0;
        ChannelPipeline channelPipeline = this.pipeline();
        for (ChannelHandler channelHandler : channelHandlerArray) {
            if (channelHandler == null) break;
            ++n;
            channelPipeline.addLast(channelHandler);
        }
        if (n == 0) {
            throw new IllegalArgumentException("handlers is empty.");
        }
        this.loop.register(this);
        channelPipeline.addLast(new LastInboundHandler());
    }

    @Override
    protected SocketAddress remoteAddress0() {
        return this.isActive() ? this.remoteAddress : null;
    }

    public boolean writeInbound(Object ... objectArray) {
        this.ensureOpen();
        if (objectArray.length == 0) {
            return !this.inboundMessages.isEmpty();
        }
        ChannelPipeline channelPipeline = this.pipeline();
        for (Object object : objectArray) {
            channelPipeline.fireChannelRead(object);
        }
        channelPipeline.fireChannelReadComplete();
        this.runPendingTasks();
        this.checkException();
        return !this.inboundMessages.isEmpty();
    }

    @Deprecated
    public Queue<Object> lastInboundBuffer() {
        return this.inboundMessages();
    }

    @Override
    protected void doDisconnect() throws Exception {
        this.doClose();
    }

    @Deprecated
    public Queue<Object> lastOutboundBuffer() {
        return this.outboundMessages();
    }

    @Override
    public boolean isOpen() {
        return this.state < 2;
    }

    @Override
    protected boolean isCompatible(EventLoop eventLoop) {
        return eventLoop instanceof EmbeddedEventLoop;
    }

    @Override
    protected void doBind(SocketAddress socketAddress) throws Exception {
    }

    public Queue<Object> inboundMessages() {
        return this.inboundMessages;
    }

    @Override
    protected void doWrite(ChannelOutboundBuffer channelOutboundBuffer) throws Exception {
        Object object;
        while ((object = channelOutboundBuffer.current()) != null) {
            ReferenceCountUtil.retain(object);
            this.outboundMessages.add(object);
            channelOutboundBuffer.remove();
        }
    }

    public boolean finish() {
        this.close();
        this.runPendingTasks();
        this.checkException();
        return !this.inboundMessages.isEmpty() || !this.outboundMessages.isEmpty();
    }

    public Object readOutbound() {
        return this.outboundMessages.poll();
    }

    @Override
    protected SocketAddress localAddress0() {
        return this.isActive() ? this.localAddress : null;
    }

    public Object readInbound() {
        return this.inboundMessages.poll();
    }

    @Override
    protected void doClose() throws Exception {
        this.state = 2;
    }

    public void runPendingTasks() {
        try {
            this.loop.runTasks();
        }
        catch (Exception exception) {
            this.recordException(exception);
        }
    }

    @Override
    public ChannelMetadata metadata() {
        return METADATA;
    }

    @Override
    public boolean isActive() {
        return this.state == 1;
    }

    private void recordException(Throwable throwable) {
        if (this.lastException == null) {
            this.lastException = throwable;
        } else {
            logger.warn("More than one exception was raised. Will report only the first one and log others.", throwable);
        }
    }

    @Override
    protected void doBeginRead() throws Exception {
    }

    protected final void ensureOpen() {
        if (!this.isOpen()) {
            this.recordException(new ClosedChannelException());
            this.checkException();
        }
    }

    @Override
    protected void doRegister() throws Exception {
        this.state = 1;
    }

    public void checkException() {
        Throwable throwable = this.lastException;
        if (throwable == null) {
            return;
        }
        this.lastException = null;
        PlatformDependent.throwException(throwable);
    }

    @Override
    public ChannelConfig config() {
        return this.config;
    }

    public Queue<Object> outboundMessages() {
        return this.outboundMessages;
    }

    public boolean writeOutbound(Object ... objectArray) {
        Object object;
        this.ensureOpen();
        if (objectArray.length == 0) {
            return !this.outboundMessages.isEmpty();
        }
        RecyclableArrayList recyclableArrayList = RecyclableArrayList.newInstance(objectArray.length);
        Object[] objectArray2 = objectArray;
        int n = objectArray2.length;
        for (int i = 0; i < n && (object = objectArray2[i]) != null; i += 1) {
            recyclableArrayList.add(this.write(object));
        }
        this.flush();
        int n2 = recyclableArrayList.size();
        for (n = 0; n < n2; n += 1) {
            ChannelFuture channelFuture = (ChannelFuture)recyclableArrayList.get(n);
            assert (channelFuture.isDone());
            if (channelFuture.cause() == null) continue;
            this.recordException(channelFuture.cause());
        }
        this.runPendingTasks();
        this.checkException();
        n = !this.outboundMessages.isEmpty() ? 1 : 0;
        recyclableArrayList.recycle();
        return n != 0;
    }

    private class DefaultUnsafe
    extends AbstractChannel.AbstractUnsafe {
        @Override
        public void connect(SocketAddress socketAddress, SocketAddress socketAddress2, ChannelPromise channelPromise) {
            this.safeSetSuccess(channelPromise);
        }

        private DefaultUnsafe() {
        }
    }

    private final class LastInboundHandler
    extends ChannelInboundHandlerAdapter {
        @Override
        public void channelRead(ChannelHandlerContext channelHandlerContext, Object object) throws Exception {
            EmbeddedChannel.this.inboundMessages.add(object);
        }

        private LastInboundHandler() {
        }

        @Override
        public void exceptionCaught(ChannelHandlerContext channelHandlerContext, Throwable throwable) throws Exception {
            EmbeddedChannel.this.recordException(throwable);
        }
    }
}

