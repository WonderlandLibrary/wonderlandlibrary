/*
 * Decompiled with CFR 0.152.
 */
package io.netty.channel.epoll;

import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.ChannelOption;
import io.netty.channel.DefaultChannelConfig;
import io.netty.channel.MessageSizeEstimator;
import io.netty.channel.RecvByteBufAllocator;
import io.netty.channel.epoll.EpollChannelOption;
import io.netty.channel.epoll.EpollServerSocketChannel;
import io.netty.channel.epoll.Native;
import io.netty.channel.socket.ServerSocketChannelConfig;
import io.netty.util.NetUtil;
import java.util.Map;

public final class EpollServerSocketChannelConfig
extends DefaultChannelConfig
implements ServerSocketChannelConfig {
    private volatile int backlog = NetUtil.SOMAXCONN;
    private final EpollServerSocketChannel channel;

    @Override
    public EpollServerSocketChannelConfig setWriteSpinCount(int n) {
        super.setWriteSpinCount(n);
        return this;
    }

    @Override
    protected void autoReadCleared() {
        this.channel.clearEpollIn();
    }

    @Override
    public EpollServerSocketChannelConfig setConnectTimeoutMillis(int n) {
        super.setConnectTimeoutMillis(n);
        return this;
    }

    @Override
    public EpollServerSocketChannelConfig setPerformancePreferences(int n, int n2, int n3) {
        return this;
    }

    EpollServerSocketChannelConfig(EpollServerSocketChannel epollServerSocketChannel) {
        super(epollServerSocketChannel);
        this.channel = epollServerSocketChannel;
        this.setReuseAddress(true);
    }

    @Override
    public EpollServerSocketChannelConfig setWriteBufferLowWaterMark(int n) {
        super.setWriteBufferLowWaterMark(n);
        return this;
    }

    @Override
    public EpollServerSocketChannelConfig setReceiveBufferSize(int n) {
        Native.setReceiveBufferSize(this.channel.fd, n);
        return this;
    }

    @Override
    public int getReceiveBufferSize() {
        return Native.getReceiveBufferSize(this.channel.fd);
    }

    @Override
    public EpollServerSocketChannelConfig setAllocator(ByteBufAllocator byteBufAllocator) {
        super.setAllocator(byteBufAllocator);
        return this;
    }

    @Override
    public boolean isReuseAddress() {
        return Native.isReuseAddress(this.channel.fd) == 1;
    }

    @Override
    public EpollServerSocketChannelConfig setBacklog(int n) {
        if (n < 0) {
            throw new IllegalArgumentException("backlog: " + n);
        }
        this.backlog = n;
        return this;
    }

    @Override
    public EpollServerSocketChannelConfig setWriteBufferHighWaterMark(int n) {
        super.setWriteBufferHighWaterMark(n);
        return this;
    }

    @Override
    public EpollServerSocketChannelConfig setMaxMessagesPerRead(int n) {
        super.setMaxMessagesPerRead(n);
        return this;
    }

    @Override
    public EpollServerSocketChannelConfig setAutoRead(boolean bl) {
        super.setAutoRead(bl);
        return this;
    }

    @Override
    public EpollServerSocketChannelConfig setReuseAddress(boolean bl) {
        Native.setReuseAddress(this.channel.fd, bl ? 1 : 0);
        return this;
    }

    @Override
    public <T> T getOption(ChannelOption<T> channelOption) {
        if (channelOption == ChannelOption.SO_RCVBUF) {
            return (T)Integer.valueOf(this.getReceiveBufferSize());
        }
        if (channelOption == ChannelOption.SO_REUSEADDR) {
            return (T)Boolean.valueOf(this.isReuseAddress());
        }
        if (channelOption == ChannelOption.SO_BACKLOG) {
            return (T)Integer.valueOf(this.getBacklog());
        }
        if (channelOption == EpollChannelOption.SO_REUSEPORT) {
            return (T)Boolean.valueOf(this.isReusePort());
        }
        return super.getOption(channelOption);
    }

    @Override
    public <T> boolean setOption(ChannelOption<T> channelOption, T t) {
        this.validate(channelOption, t);
        if (channelOption == ChannelOption.SO_RCVBUF) {
            this.setReceiveBufferSize((Integer)t);
        } else if (channelOption == ChannelOption.SO_REUSEADDR) {
            this.setReuseAddress((Boolean)t);
        } else if (channelOption == ChannelOption.SO_BACKLOG) {
            this.setBacklog((Integer)t);
        } else if (channelOption == EpollChannelOption.SO_REUSEPORT) {
            this.setReusePort((Boolean)t);
        } else {
            return super.setOption(channelOption, t);
        }
        return true;
    }

    @Override
    public int getBacklog() {
        return this.backlog;
    }

    @Override
    public EpollServerSocketChannelConfig setRecvByteBufAllocator(RecvByteBufAllocator recvByteBufAllocator) {
        super.setRecvByteBufAllocator(recvByteBufAllocator);
        return this;
    }

    public boolean isReusePort() {
        return Native.isReusePort(this.channel.fd) == 1;
    }

    public EpollServerSocketChannelConfig setReusePort(boolean bl) {
        Native.setReusePort(this.channel.fd, bl ? 1 : 0);
        return this;
    }

    @Override
    public EpollServerSocketChannelConfig setMessageSizeEstimator(MessageSizeEstimator messageSizeEstimator) {
        super.setMessageSizeEstimator(messageSizeEstimator);
        return this;
    }

    @Override
    public Map<ChannelOption<?>, Object> getOptions() {
        return this.getOptions(super.getOptions(), ChannelOption.SO_RCVBUF, ChannelOption.SO_REUSEADDR, ChannelOption.SO_BACKLOG, EpollChannelOption.SO_REUSEPORT);
    }
}

