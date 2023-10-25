/*
 * Decompiled with CFR 0.152.
 */
package io.netty.channel.socket;

import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelPromise;
import io.netty.channel.socket.ServerSocketChannel;
import io.netty.channel.socket.SocketChannelConfig;
import java.net.InetSocketAddress;

public interface SocketChannel
extends Channel {
    public boolean isOutputShutdown();

    @Override
    public ServerSocketChannel parent();

    @Override
    public InetSocketAddress remoteAddress();

    @Override
    public InetSocketAddress localAddress();

    public ChannelFuture shutdownOutput(ChannelPromise var1);

    public boolean isInputShutdown();

    @Override
    public SocketChannelConfig config();

    public ChannelFuture shutdownOutput();
}

