/*
 * Decompiled with CFR 0.152.
 */
package io.netty.channel.socket;

import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelPromise;
import io.netty.channel.socket.DatagramChannelConfig;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.NetworkInterface;

public interface DatagramChannel
extends Channel {
    public ChannelFuture block(InetAddress var1, NetworkInterface var2, InetAddress var3);

    public boolean isConnected();

    public ChannelFuture leaveGroup(InetSocketAddress var1, NetworkInterface var2, ChannelPromise var3);

    @Override
    public InetSocketAddress localAddress();

    @Override
    public InetSocketAddress remoteAddress();

    public ChannelFuture joinGroup(InetAddress var1);

    public ChannelFuture joinGroup(InetSocketAddress var1, NetworkInterface var2, ChannelPromise var3);

    public ChannelFuture leaveGroup(InetAddress var1, ChannelPromise var2);

    public ChannelFuture block(InetAddress var1, InetAddress var2);

    public ChannelFuture block(InetAddress var1, NetworkInterface var2, InetAddress var3, ChannelPromise var4);

    public ChannelFuture leaveGroup(InetAddress var1);

    public ChannelFuture joinGroup(InetSocketAddress var1, NetworkInterface var2);

    @Override
    public DatagramChannelConfig config();

    public ChannelFuture joinGroup(InetAddress var1, NetworkInterface var2, InetAddress var3);

    public ChannelFuture leaveGroup(InetAddress var1, NetworkInterface var2, InetAddress var3);

    public ChannelFuture leaveGroup(InetAddress var1, NetworkInterface var2, InetAddress var3, ChannelPromise var4);

    public ChannelFuture leaveGroup(InetSocketAddress var1, NetworkInterface var2);

    public ChannelFuture joinGroup(InetAddress var1, ChannelPromise var2);

    public ChannelFuture joinGroup(InetAddress var1, NetworkInterface var2, InetAddress var3, ChannelPromise var4);

    public ChannelFuture block(InetAddress var1, InetAddress var2, ChannelPromise var3);
}

