/*
 * Decompiled with CFR 0.152.
 */
package io.netty.channel;

import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import io.netty.util.concurrent.EventExecutorGroup;
import java.net.SocketAddress;
import java.util.List;
import java.util.Map;

public interface ChannelPipeline
extends Iterable<Map.Entry<String, ChannelHandler>> {
    public ChannelHandler first();

    public ChannelHandlerContext context(String var1);

    public ChannelFuture connect(SocketAddress var1, SocketAddress var2, ChannelPromise var3);

    public ChannelPipeline fireChannelActive();

    public ChannelFuture writeAndFlush(Object var1, ChannelPromise var2);

    public ChannelPipeline addLast(String var1, ChannelHandler var2);

    public ChannelPipeline remove(ChannelHandler var1);

    public ChannelHandlerContext context(Class<? extends ChannelHandler> var1);

    public ChannelHandler replace(String var1, String var2, ChannelHandler var3);

    public ChannelHandlerContext context(ChannelHandler var1);

    public ChannelFuture connect(SocketAddress var1, SocketAddress var2);

    public ChannelPipeline addBefore(String var1, String var2, ChannelHandler var3);

    public ChannelHandler get(String var1);

    public ChannelPipeline fireChannelRead(Object var1);

    public ChannelFuture write(Object var1, ChannelPromise var2);

    public ChannelFuture close();

    public <T extends ChannelHandler> T get(Class<T> var1);

    public <T extends ChannelHandler> T remove(Class<T> var1);

    public ChannelFuture write(Object var1);

    public ChannelFuture connect(SocketAddress var1);

    public ChannelPipeline addFirst(ChannelHandler ... var1);

    public ChannelPipeline fireChannelUnregistered();

    public ChannelPipeline fireChannelReadComplete();

    public ChannelFuture connect(SocketAddress var1, ChannelPromise var2);

    public ChannelPipeline fireUserEventTriggered(Object var1);

    public ChannelHandlerContext lastContext();

    public ChannelPipeline addFirst(String var1, ChannelHandler var2);

    public ChannelFuture writeAndFlush(Object var1);

    public Map<String, ChannelHandler> toMap();

    public ChannelFuture deregister();

    public ChannelFuture close(ChannelPromise var1);

    public ChannelPipeline addAfter(EventExecutorGroup var1, String var2, String var3, ChannelHandler var4);

    public ChannelPipeline addBefore(EventExecutorGroup var1, String var2, String var3, ChannelHandler var4);

    public ChannelHandler remove(String var1);

    public ChannelPipeline fireChannelWritabilityChanged();

    public ChannelFuture deregister(ChannelPromise var1);

    public ChannelPipeline addAfter(String var1, String var2, ChannelHandler var3);

    public ChannelPipeline fireChannelInactive();

    public ChannelHandlerContext firstContext();

    public ChannelFuture bind(SocketAddress var1, ChannelPromise var2);

    public ChannelFuture disconnect(ChannelPromise var1);

    public ChannelPipeline flush();

    public ChannelPipeline addLast(ChannelHandler ... var1);

    public ChannelHandler removeFirst();

    public ChannelPipeline replace(ChannelHandler var1, String var2, ChannelHandler var3);

    public ChannelHandler last();

    public ChannelPipeline addFirst(EventExecutorGroup var1, String var2, ChannelHandler var3);

    public ChannelFuture bind(SocketAddress var1);

    public ChannelPipeline fireChannelRegistered();

    public ChannelPipeline addLast(EventExecutorGroup var1, String var2, ChannelHandler var3);

    public ChannelFuture disconnect();

    public ChannelPipeline fireExceptionCaught(Throwable var1);

    public <T extends ChannelHandler> T replace(Class<T> var1, String var2, ChannelHandler var3);

    public ChannelPipeline addFirst(EventExecutorGroup var1, ChannelHandler ... var2);

    public Channel channel();

    public ChannelPipeline addLast(EventExecutorGroup var1, ChannelHandler ... var2);

    public ChannelPipeline read();

    public List<String> names();

    public ChannelHandler removeLast();
}

