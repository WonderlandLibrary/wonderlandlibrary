/*
 * Decompiled with CFR 0.152.
 */
package io.netty.channel;

import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.ChannelProgressivePromise;
import io.netty.channel.ChannelPromise;
import io.netty.util.AttributeMap;
import io.netty.util.concurrent.EventExecutor;
import java.net.SocketAddress;

public interface ChannelHandlerContext
extends AttributeMap {
    public Channel channel();

    public ChannelHandlerContext fireChannelUnregistered();

    public ChannelFuture connect(SocketAddress var1, ChannelPromise var2);

    public ChannelHandlerContext fireChannelRegistered();

    public ChannelHandlerContext fireChannelActive();

    public ChannelFuture close(ChannelPromise var1);

    public ChannelFuture write(Object var1, ChannelPromise var2);

    public ChannelHandlerContext fireExceptionCaught(Throwable var1);

    public ChannelHandlerContext fireChannelWritabilityChanged();

    public ChannelFuture connect(SocketAddress var1, SocketAddress var2, ChannelPromise var3);

    public ChannelHandlerContext fireChannelInactive();

    public ChannelFuture newFailedFuture(Throwable var1);

    public ChannelHandlerContext fireUserEventTriggered(Object var1);

    public ChannelFuture connect(SocketAddress var1, SocketAddress var2);

    public ChannelFuture deregister();

    public ChannelFuture disconnect();

    public ChannelHandlerContext flush();

    public EventExecutor executor();

    public ChannelPromise newPromise();

    public ChannelHandlerContext read();

    public ChannelHandlerContext fireChannelReadComplete();

    public ChannelProgressivePromise newProgressivePromise();

    public ChannelFuture disconnect(ChannelPromise var1);

    public ChannelFuture bind(SocketAddress var1, ChannelPromise var2);

    public ChannelFuture newSucceededFuture();

    public boolean isRemoved();

    public ChannelFuture writeAndFlush(Object var1, ChannelPromise var2);

    public ChannelFuture write(Object var1);

    public String name();

    public ChannelFuture connect(SocketAddress var1);

    public ByteBufAllocator alloc();

    public ChannelHandlerContext fireChannelRead(Object var1);

    public ChannelFuture close();

    public ChannelFuture bind(SocketAddress var1);

    public ChannelPipeline pipeline();

    public ChannelFuture deregister(ChannelPromise var1);

    public ChannelHandler handler();

    public ChannelPromise voidPromise();

    public ChannelFuture writeAndFlush(Object var1);
}

