/*
 * Decompiled with CFR 0.152.
 */
package io.netty.channel;

import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.ChannelConfig;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelMetadata;
import io.netty.channel.ChannelOutboundBuffer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.ChannelProgressivePromise;
import io.netty.channel.ChannelPromise;
import io.netty.channel.EventLoop;
import io.netty.util.AttributeMap;
import java.net.SocketAddress;

public interface Channel
extends AttributeMap,
Comparable<Channel> {
    public ChannelFuture connect(SocketAddress var1);

    public ChannelFuture disconnect(ChannelPromise var1);

    public EventLoop eventLoop();

    public ChannelFuture bind(SocketAddress var1, ChannelPromise var2);

    public ChannelFuture connect(SocketAddress var1, ChannelPromise var2);

    public ChannelFuture writeAndFlush(Object var1);

    public Channel parent();

    public ChannelFuture connect(SocketAddress var1, SocketAddress var2);

    public ChannelFuture deregister();

    public ChannelFuture newSucceededFuture();

    public Channel read();

    public ChannelFuture close(ChannelPromise var1);

    public boolean isOpen();

    public ChannelPromise newPromise();

    public ByteBufAllocator alloc();

    public ChannelPipeline pipeline();

    public ChannelFuture write(Object var1, ChannelPromise var2);

    public ChannelFuture newFailedFuture(Throwable var1);

    public ChannelConfig config();

    public ChannelFuture close();

    public ChannelMetadata metadata();

    public SocketAddress localAddress();

    public ChannelFuture connect(SocketAddress var1, SocketAddress var2, ChannelPromise var3);

    public ChannelProgressivePromise newProgressivePromise();

    public ChannelFuture write(Object var1);

    public ChannelPromise voidPromise();

    public Channel flush();

    public ChannelFuture disconnect();

    public Unsafe unsafe();

    public ChannelFuture writeAndFlush(Object var1, ChannelPromise var2);

    public boolean isWritable();

    public SocketAddress remoteAddress();

    public ChannelFuture closeFuture();

    public ChannelFuture deregister(ChannelPromise var1);

    public boolean isActive();

    public ChannelFuture bind(SocketAddress var1);

    public boolean isRegistered();

    public static interface Unsafe {
        public void connect(SocketAddress var1, SocketAddress var2, ChannelPromise var3);

        public void disconnect(ChannelPromise var1);

        public ChannelOutboundBuffer outboundBuffer();

        public void register(EventLoop var1, ChannelPromise var2);

        public void close(ChannelPromise var1);

        public void flush();

        public void bind(SocketAddress var1, ChannelPromise var2);

        public SocketAddress remoteAddress();

        public void beginRead();

        public void write(Object var1, ChannelPromise var2);

        public SocketAddress localAddress();

        public ChannelPromise voidPromise();

        public void closeForcibly();

        public void deregister(ChannelPromise var1);
    }
}

