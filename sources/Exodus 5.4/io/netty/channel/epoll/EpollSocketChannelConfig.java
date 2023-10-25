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
import io.netty.channel.epoll.EpollSocketChannel;
import io.netty.channel.epoll.Native;
import io.netty.channel.socket.SocketChannelConfig;
import io.netty.util.internal.PlatformDependent;
import java.util.Map;

public final class EpollSocketChannelConfig
extends DefaultChannelConfig
implements SocketChannelConfig {
    private final EpollSocketChannel channel;
    private volatile boolean allowHalfClosure;

    @Override
    public EpollSocketChannelConfig setWriteSpinCount(int n) {
        super.setWriteSpinCount(n);
        return this;
    }

    public int getTcpKeepCnt() {
        return Native.getTcpKeepCnt(this.channel.fd);
    }

    public int getTcpKeepIdle() {
        return Native.getTcpKeepIdle(this.channel.fd);
    }

    @Override
    public EpollSocketChannelConfig setWriteBufferLowWaterMark(int n) {
        super.setWriteBufferLowWaterMark(n);
        return this;
    }

    @Override
    public EpollSocketChannelConfig setAllowHalfClosure(boolean bl) {
        this.allowHalfClosure = bl;
        return this;
    }

    @Override
    public EpollSocketChannelConfig setSendBufferSize(int n) {
        Native.setSendBufferSize(this.channel.fd, n);
        return this;
    }

    @Override
    public boolean isKeepAlive() {
        return Native.isKeepAlive(this.channel.fd) == 1;
    }

    @Override
    public boolean isAllowHalfClosure() {
        return this.allowHalfClosure;
    }

    public EpollSocketChannelConfig setTcpKeepIdle(int n) {
        Native.setTcpKeepIdle(this.channel.fd, n);
        return this;
    }

    @Override
    public EpollSocketChannelConfig setMessageSizeEstimator(MessageSizeEstimator messageSizeEstimator) {
        super.setMessageSizeEstimator(messageSizeEstimator);
        return this;
    }

    @Override
    public EpollSocketChannelConfig setWriteBufferHighWaterMark(int n) {
        super.setWriteBufferHighWaterMark(n);
        return this;
    }

    @Override
    public EpollSocketChannelConfig setMaxMessagesPerRead(int n) {
        super.setMaxMessagesPerRead(n);
        return this;
    }

    @Override
    public int getTrafficClass() {
        return Native.getTrafficClass(this.channel.fd);
    }

    public EpollSocketChannelConfig setTcpKeepCntl(int n) {
        Native.setTcpKeepCnt(this.channel.fd, n);
        return this;
    }

    @Override
    public Map<ChannelOption<?>, Object> getOptions() {
        return this.getOptions(super.getOptions(), ChannelOption.SO_RCVBUF, ChannelOption.SO_SNDBUF, ChannelOption.TCP_NODELAY, ChannelOption.SO_KEEPALIVE, ChannelOption.SO_REUSEADDR, ChannelOption.SO_LINGER, ChannelOption.IP_TOS, ChannelOption.ALLOW_HALF_CLOSURE, EpollChannelOption.TCP_CORK, EpollChannelOption.TCP_KEEPCNT, EpollChannelOption.TCP_KEEPIDLE, EpollChannelOption.TCP_KEEPINTVL);
    }

    @Override
    public EpollSocketChannelConfig setPerformancePreferences(int n, int n2, int n3) {
        return this;
    }

    @Override
    public EpollSocketChannelConfig setAutoRead(boolean bl) {
        super.setAutoRead(bl);
        return this;
    }

    public boolean isTcpCork() {
        return Native.isTcpCork(this.channel.fd) == 1;
    }

    @Override
    public EpollSocketChannelConfig setReuseAddress(boolean bl) {
        Native.setReuseAddress(this.channel.fd, bl ? 1 : 0);
        return this;
    }

    @Override
    public boolean isReuseAddress() {
        return Native.isReuseAddress(this.channel.fd) == 1;
    }

    @Override
    public boolean isTcpNoDelay() {
        return Native.isTcpNoDelay(this.channel.fd) == 1;
    }

    @Override
    public EpollSocketChannelConfig setTrafficClass(int n) {
        Native.setTrafficClass(this.channel.fd, n);
        return this;
    }

    @Override
    public EpollSocketChannelConfig setSoLinger(int n) {
        Native.setSoLinger(this.channel.fd, n);
        return this;
    }

    @Override
    protected void autoReadCleared() {
        this.channel.clearEpollIn();
    }

    @Override
    public int getSendBufferSize() {
        return Native.getSendBufferSize(this.channel.fd);
    }

    @Override
    public EpollSocketChannelConfig setTcpNoDelay(boolean bl) {
        Native.setTcpNoDelay(this.channel.fd, bl ? 1 : 0);
        return this;
    }

    @Override
    public int getReceiveBufferSize() {
        return Native.getReceiveBufferSize(this.channel.fd);
    }

    @Override
    public <T> T getOption(ChannelOption<T> channelOption) {
        if (channelOption == ChannelOption.SO_RCVBUF) {
            return (T)Integer.valueOf(this.getReceiveBufferSize());
        }
        if (channelOption == ChannelOption.SO_SNDBUF) {
            return (T)Integer.valueOf(this.getSendBufferSize());
        }
        if (channelOption == ChannelOption.TCP_NODELAY) {
            return (T)Boolean.valueOf(this.isTcpNoDelay());
        }
        if (channelOption == ChannelOption.SO_KEEPALIVE) {
            return (T)Boolean.valueOf(this.isKeepAlive());
        }
        if (channelOption == ChannelOption.SO_REUSEADDR) {
            return (T)Boolean.valueOf(this.isReuseAddress());
        }
        if (channelOption == ChannelOption.SO_LINGER) {
            return (T)Integer.valueOf(this.getSoLinger());
        }
        if (channelOption == ChannelOption.IP_TOS) {
            return (T)Integer.valueOf(this.getTrafficClass());
        }
        if (channelOption == ChannelOption.ALLOW_HALF_CLOSURE) {
            return (T)Boolean.valueOf(this.isAllowHalfClosure());
        }
        if (channelOption == EpollChannelOption.TCP_CORK) {
            return (T)Boolean.valueOf(this.isTcpCork());
        }
        if (channelOption == EpollChannelOption.TCP_KEEPIDLE) {
            return (T)Integer.valueOf(this.getTcpKeepIdle());
        }
        if (channelOption == EpollChannelOption.TCP_KEEPINTVL) {
            return (T)Integer.valueOf(this.getTcpKeepIntvl());
        }
        if (channelOption == EpollChannelOption.TCP_KEEPCNT) {
            return (T)Integer.valueOf(this.getTcpKeepCnt());
        }
        return super.getOption(channelOption);
    }

    @Override
    public EpollSocketChannelConfig setReceiveBufferSize(int n) {
        Native.setReceiveBufferSize(this.channel.fd, n);
        return this;
    }

    @Override
    public EpollSocketChannelConfig setAutoClose(boolean bl) {
        super.setAutoClose(bl);
        return this;
    }

    EpollSocketChannelConfig(EpollSocketChannel epollSocketChannel) {
        super(epollSocketChannel);
        this.channel = epollSocketChannel;
        if (PlatformDependent.canEnableTcpNoDelayByDefault()) {
            this.setTcpNoDelay(true);
        }
    }

    @Override
    public EpollSocketChannelConfig setRecvByteBufAllocator(RecvByteBufAllocator recvByteBufAllocator) {
        super.setRecvByteBufAllocator(recvByteBufAllocator);
        return this;
    }

    @Override
    public EpollSocketChannelConfig setKeepAlive(boolean bl) {
        Native.setKeepAlive(this.channel.fd, bl ? 1 : 0);
        return this;
    }

    @Override
    public int getSoLinger() {
        return Native.getSoLinger(this.channel.fd);
    }

    @Override
    public EpollSocketChannelConfig setConnectTimeoutMillis(int n) {
        super.setConnectTimeoutMillis(n);
        return this;
    }

    public EpollSocketChannelConfig setTcpCork(boolean bl) {
        Native.setTcpCork(this.channel.fd, bl ? 1 : 0);
        return this;
    }

    public int getTcpKeepIntvl() {
        return Native.getTcpKeepIntvl(this.channel.fd);
    }

    @Override
    public <T> boolean setOption(ChannelOption<T> channelOption, T t) {
        this.validate(channelOption, t);
        if (channelOption == ChannelOption.SO_RCVBUF) {
            this.setReceiveBufferSize((Integer)t);
        } else if (channelOption == ChannelOption.SO_SNDBUF) {
            this.setSendBufferSize((Integer)t);
        } else if (channelOption == ChannelOption.TCP_NODELAY) {
            this.setTcpNoDelay((Boolean)t);
        } else if (channelOption == ChannelOption.SO_KEEPALIVE) {
            this.setKeepAlive((Boolean)t);
        } else if (channelOption == ChannelOption.SO_REUSEADDR) {
            this.setReuseAddress((Boolean)t);
        } else if (channelOption == ChannelOption.SO_LINGER) {
            this.setSoLinger((Integer)t);
        } else if (channelOption == ChannelOption.IP_TOS) {
            this.setTrafficClass((Integer)t);
        } else if (channelOption == ChannelOption.ALLOW_HALF_CLOSURE) {
            this.setAllowHalfClosure((Boolean)t);
        } else if (channelOption == EpollChannelOption.TCP_CORK) {
            this.setTcpCork((Boolean)t);
        } else if (channelOption == EpollChannelOption.TCP_KEEPIDLE) {
            this.setTcpKeepIdle((Integer)t);
        } else if (channelOption == EpollChannelOption.TCP_KEEPCNT) {
            this.setTcpKeepCntl((Integer)t);
        } else if (channelOption == EpollChannelOption.TCP_KEEPINTVL) {
            this.setTcpKeepIntvl((Integer)t);
        } else {
            return super.setOption(channelOption, t);
        }
        return true;
    }

    @Override
    public EpollSocketChannelConfig setAllocator(ByteBufAllocator byteBufAllocator) {
        super.setAllocator(byteBufAllocator);
        return this;
    }

    public EpollSocketChannelConfig setTcpKeepIntvl(int n) {
        Native.setTcpKeepIntvl(this.channel.fd, n);
        return this;
    }
}

