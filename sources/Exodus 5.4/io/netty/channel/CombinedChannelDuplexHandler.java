/*
 * Decompiled with CFR 0.152.
 */
package io.netty.channel;

import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandler;
import io.netty.channel.ChannelOutboundHandler;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.ChannelPromise;
import java.net.SocketAddress;

public class CombinedChannelDuplexHandler<I extends ChannelInboundHandler, O extends ChannelOutboundHandler>
extends ChannelDuplexHandler {
    private O outboundHandler;
    private I inboundHandler;

    @Override
    public void channelRegistered(ChannelHandlerContext channelHandlerContext) throws Exception {
        this.inboundHandler.channelRegistered(channelHandlerContext);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext channelHandlerContext, Throwable throwable) throws Exception {
        this.inboundHandler.exceptionCaught(channelHandlerContext, throwable);
    }

    @Override
    public void handlerAdded(ChannelHandlerContext channelHandlerContext) throws Exception {
        if (this.inboundHandler == null) {
            throw new IllegalStateException("init() must be invoked before being added to a " + ChannelPipeline.class.getSimpleName() + " if " + CombinedChannelDuplexHandler.class.getSimpleName() + " was constructed with the default constructor.");
        }
        this.inboundHandler.handlerAdded(channelHandlerContext);
        this.outboundHandler.handlerAdded(channelHandlerContext);
    }

    @Override
    public void write(ChannelHandlerContext channelHandlerContext, Object object, ChannelPromise channelPromise) throws Exception {
        this.outboundHandler.write(channelHandlerContext, object, channelPromise);
    }

    protected final O outboundHandler() {
        return this.outboundHandler;
    }

    @Override
    public void read(ChannelHandlerContext channelHandlerContext) throws Exception {
        this.outboundHandler.read(channelHandlerContext);
    }

    @Override
    public void channelUnregistered(ChannelHandlerContext channelHandlerContext) throws Exception {
        this.inboundHandler.channelUnregistered(channelHandlerContext);
    }

    @Override
    public void channelActive(ChannelHandlerContext channelHandlerContext) throws Exception {
        this.inboundHandler.channelActive(channelHandlerContext);
    }

    @Override
    public void bind(ChannelHandlerContext channelHandlerContext, SocketAddress socketAddress, ChannelPromise channelPromise) throws Exception {
        this.outboundHandler.bind(channelHandlerContext, socketAddress, channelPromise);
    }

    protected CombinedChannelDuplexHandler() {
    }

    @Override
    public void deregister(ChannelHandlerContext channelHandlerContext, ChannelPromise channelPromise) throws Exception {
        this.outboundHandler.deregister(channelHandlerContext, channelPromise);
    }

    @Override
    public void flush(ChannelHandlerContext channelHandlerContext) throws Exception {
        this.outboundHandler.flush(channelHandlerContext);
    }

    public CombinedChannelDuplexHandler(I i, O o) {
        this.init(i, o);
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext channelHandlerContext, Object object) throws Exception {
        this.inboundHandler.userEventTriggered(channelHandlerContext, object);
    }

    @Override
    public void channelInactive(ChannelHandlerContext channelHandlerContext) throws Exception {
        this.inboundHandler.channelInactive(channelHandlerContext);
    }

    @Override
    public void close(ChannelHandlerContext channelHandlerContext, ChannelPromise channelPromise) throws Exception {
        this.outboundHandler.close(channelHandlerContext, channelPromise);
    }

    @Override
    public void channelRead(ChannelHandlerContext channelHandlerContext, Object object) throws Exception {
        this.inboundHandler.channelRead(channelHandlerContext, object);
    }

    @Override
    public void channelWritabilityChanged(ChannelHandlerContext channelHandlerContext) throws Exception {
        this.inboundHandler.channelWritabilityChanged(channelHandlerContext);
    }

    protected final I inboundHandler() {
        return this.inboundHandler;
    }

    @Override
    public void connect(ChannelHandlerContext channelHandlerContext, SocketAddress socketAddress, SocketAddress socketAddress2, ChannelPromise channelPromise) throws Exception {
        this.outboundHandler.connect(channelHandlerContext, socketAddress, socketAddress2, channelPromise);
    }

    protected final void init(I i, O o) {
        this.validate(i, o);
        this.inboundHandler = i;
        this.outboundHandler = o;
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext channelHandlerContext) throws Exception {
        this.inboundHandler.channelReadComplete(channelHandlerContext);
    }

    private void validate(I i, O o) {
        if (this.inboundHandler != null) {
            throw new IllegalStateException("init() can not be invoked if " + CombinedChannelDuplexHandler.class.getSimpleName() + " was constructed with non-default constructor.");
        }
        if (i == null) {
            throw new NullPointerException("inboundHandler");
        }
        if (o == null) {
            throw new NullPointerException("outboundHandler");
        }
        if (i instanceof ChannelOutboundHandler) {
            throw new IllegalArgumentException("inboundHandler must not implement " + ChannelOutboundHandler.class.getSimpleName() + " to get combined.");
        }
        if (o instanceof ChannelInboundHandler) {
            throw new IllegalArgumentException("outboundHandler must not implement " + ChannelInboundHandler.class.getSimpleName() + " to get combined.");
        }
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext channelHandlerContext) throws Exception {
        this.inboundHandler.handlerRemoved(channelHandlerContext);
        this.outboundHandler.handlerRemoved(channelHandlerContext);
    }

    @Override
    public void disconnect(ChannelHandlerContext channelHandlerContext, ChannelPromise channelPromise) throws Exception {
        this.outboundHandler.disconnect(channelHandlerContext, channelPromise);
    }
}

