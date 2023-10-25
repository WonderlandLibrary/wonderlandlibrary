/*
 * Decompiled with CFR 0.152.
 */
package io.netty.channel.epoll;

import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.ChannelOption;
import io.netty.channel.DefaultChannelConfig;
import io.netty.channel.FixedRecvByteBufAllocator;
import io.netty.channel.MessageSizeEstimator;
import io.netty.channel.RecvByteBufAllocator;
import io.netty.channel.epoll.EpollChannelOption;
import io.netty.channel.epoll.EpollDatagramChannel;
import io.netty.channel.epoll.Native;
import io.netty.channel.socket.DatagramChannelConfig;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Map;

public final class EpollDatagramChannelConfig
extends DefaultChannelConfig
implements DatagramChannelConfig {
    private static final RecvByteBufAllocator DEFAULT_RCVBUF_ALLOCATOR = new FixedRecvByteBufAllocator(2048);
    private boolean activeOnOpen;
    private final EpollDatagramChannel datagramChannel;

    @Override
    public EpollDatagramChannelConfig setRecvByteBufAllocator(RecvByteBufAllocator recvByteBufAllocator) {
        super.setRecvByteBufAllocator(recvByteBufAllocator);
        return this;
    }

    public boolean isReusePort() {
        return Native.isReusePort(this.datagramChannel.fd) == 1;
    }

    @Override
    public int getTrafficClass() {
        return Native.getTrafficClass(this.datagramChannel.fd);
    }

    private void setActiveOnOpen(boolean bl) {
        if (this.channel.isRegistered()) {
            throw new IllegalStateException("Can only changed before channel was registered");
        }
        this.activeOnOpen = bl;
    }

    @Override
    public DatagramChannelConfig setLoopbackModeDisabled(boolean bl) {
        throw new UnsupportedOperationException("Multicast not supported");
    }

    EpollDatagramChannelConfig(EpollDatagramChannel epollDatagramChannel) {
        super(epollDatagramChannel);
        this.datagramChannel = epollDatagramChannel;
        this.setRecvByteBufAllocator(DEFAULT_RCVBUF_ALLOCATOR);
    }

    @Override
    public EpollDatagramChannelConfig setReuseAddress(boolean bl) {
        Native.setReuseAddress(this.datagramChannel.fd, bl ? 1 : 0);
        return this;
    }

    @Override
    public boolean isReuseAddress() {
        return Native.isReuseAddress(this.datagramChannel.fd) == 1;
    }

    @Override
    public EpollDatagramChannelConfig setMessageSizeEstimator(MessageSizeEstimator messageSizeEstimator) {
        super.setMessageSizeEstimator(messageSizeEstimator);
        return this;
    }

    @Override
    public EpollDatagramChannelConfig setTimeToLive(int n) {
        throw new UnsupportedOperationException("Multicast not supported");
    }

    @Override
    public EpollDatagramChannelConfig setInterface(InetAddress inetAddress) {
        throw new UnsupportedOperationException("Multicast not supported");
    }

    @Override
    public EpollDatagramChannelConfig setBroadcast(boolean bl) {
        Native.setBroadcast(this.datagramChannel.fd, bl ? 1 : 0);
        return this;
    }

    @Override
    public boolean isBroadcast() {
        return Native.isBroadcast(this.datagramChannel.fd) == 1;
    }

    @Override
    public int getTimeToLive() {
        return -1;
    }

    @Override
    public InetAddress getInterface() {
        return null;
    }

    @Override
    public int getSendBufferSize() {
        return Native.getSendBufferSize(this.datagramChannel.fd);
    }

    @Override
    public NetworkInterface getNetworkInterface() {
        return null;
    }

    @Override
    public <T> T getOption(ChannelOption<T> channelOption) {
        if (channelOption == ChannelOption.SO_BROADCAST) {
            return (T)Boolean.valueOf(this.isBroadcast());
        }
        if (channelOption == ChannelOption.SO_RCVBUF) {
            return (T)Integer.valueOf(this.getReceiveBufferSize());
        }
        if (channelOption == ChannelOption.SO_SNDBUF) {
            return (T)Integer.valueOf(this.getSendBufferSize());
        }
        if (channelOption == ChannelOption.SO_REUSEADDR) {
            return (T)Boolean.valueOf(this.isReuseAddress());
        }
        if (channelOption == ChannelOption.IP_MULTICAST_LOOP_DISABLED) {
            return (T)Boolean.valueOf(this.isLoopbackModeDisabled());
        }
        if (channelOption == ChannelOption.IP_MULTICAST_ADDR) {
            return (T)this.getInterface();
        }
        if (channelOption == ChannelOption.IP_MULTICAST_IF) {
            return (T)this.getNetworkInterface();
        }
        if (channelOption == ChannelOption.IP_MULTICAST_TTL) {
            return (T)Integer.valueOf(this.getTimeToLive());
        }
        if (channelOption == ChannelOption.IP_TOS) {
            return (T)Integer.valueOf(this.getTrafficClass());
        }
        if (channelOption == ChannelOption.DATAGRAM_CHANNEL_ACTIVE_ON_REGISTRATION) {
            return (T)Boolean.valueOf(this.activeOnOpen);
        }
        if (channelOption == EpollChannelOption.SO_REUSEPORT) {
            return (T)Boolean.valueOf(this.isReusePort());
        }
        return super.getOption(channelOption);
    }

    @Override
    public EpollDatagramChannelConfig setSendBufferSize(int n) {
        Native.setSendBufferSize(this.datagramChannel.fd, n);
        return this;
    }

    @Override
    public EpollDatagramChannelConfig setAutoRead(boolean bl) {
        super.setAutoRead(bl);
        return this;
    }

    @Override
    public <T> boolean setOption(ChannelOption<T> channelOption, T t) {
        this.validate(channelOption, t);
        if (channelOption == ChannelOption.SO_BROADCAST) {
            this.setBroadcast((Boolean)t);
        } else if (channelOption == ChannelOption.SO_RCVBUF) {
            this.setReceiveBufferSize((Integer)t);
        } else if (channelOption == ChannelOption.SO_SNDBUF) {
            this.setSendBufferSize((Integer)t);
        } else if (channelOption == ChannelOption.SO_REUSEADDR) {
            this.setReuseAddress((Boolean)t);
        } else if (channelOption == ChannelOption.IP_MULTICAST_LOOP_DISABLED) {
            this.setLoopbackModeDisabled((Boolean)t);
        } else if (channelOption == ChannelOption.IP_MULTICAST_ADDR) {
            this.setInterface((InetAddress)t);
        } else if (channelOption == ChannelOption.IP_MULTICAST_IF) {
            this.setNetworkInterface((NetworkInterface)t);
        } else if (channelOption == ChannelOption.IP_MULTICAST_TTL) {
            this.setTimeToLive((Integer)t);
        } else if (channelOption == ChannelOption.IP_TOS) {
            this.setTrafficClass((Integer)t);
        } else if (channelOption == ChannelOption.DATAGRAM_CHANNEL_ACTIVE_ON_REGISTRATION) {
            this.setActiveOnOpen((Boolean)t);
        } else if (channelOption == EpollChannelOption.SO_REUSEPORT) {
            this.setReusePort((Boolean)t);
        } else {
            return super.setOption(channelOption, t);
        }
        return true;
    }

    @Override
    public EpollDatagramChannelConfig setMaxMessagesPerRead(int n) {
        super.setMaxMessagesPerRead(n);
        return this;
    }

    @Override
    public EpollDatagramChannelConfig setTrafficClass(int n) {
        Native.setTrafficClass(this.datagramChannel.fd, n);
        return this;
    }

    @Override
    public EpollDatagramChannelConfig setConnectTimeoutMillis(int n) {
        super.setConnectTimeoutMillis(n);
        return this;
    }

    @Override
    public EpollDatagramChannelConfig setReceiveBufferSize(int n) {
        Native.setReceiveBufferSize(this.datagramChannel.fd, n);
        return this;
    }

    @Override
    public EpollDatagramChannelConfig setNetworkInterface(NetworkInterface networkInterface) {
        throw new UnsupportedOperationException("Multicast not supported");
    }

    @Override
    public EpollDatagramChannelConfig setWriteBufferLowWaterMark(int n) {
        super.setWriteBufferLowWaterMark(n);
        return this;
    }

    @Override
    public EpollDatagramChannelConfig setAutoClose(boolean bl) {
        super.setAutoClose(bl);
        return this;
    }

    @Override
    public int getReceiveBufferSize() {
        return Native.getReceiveBufferSize(this.datagramChannel.fd);
    }

    @Override
    public boolean isLoopbackModeDisabled() {
        return false;
    }

    public EpollDatagramChannelConfig setReusePort(boolean bl) {
        Native.setReusePort(this.datagramChannel.fd, bl ? 1 : 0);
        return this;
    }

    @Override
    public EpollDatagramChannelConfig setWriteSpinCount(int n) {
        super.setWriteSpinCount(n);
        return this;
    }

    @Override
    public EpollDatagramChannelConfig setWriteBufferHighWaterMark(int n) {
        super.setWriteBufferHighWaterMark(n);
        return this;
    }

    @Override
    public EpollDatagramChannelConfig setAllocator(ByteBufAllocator byteBufAllocator) {
        super.setAllocator(byteBufAllocator);
        return this;
    }

    @Override
    public Map<ChannelOption<?>, Object> getOptions() {
        return this.getOptions(super.getOptions(), ChannelOption.SO_BROADCAST, ChannelOption.SO_RCVBUF, ChannelOption.SO_SNDBUF, ChannelOption.SO_REUSEADDR, ChannelOption.IP_MULTICAST_LOOP_DISABLED, ChannelOption.IP_MULTICAST_ADDR, ChannelOption.IP_MULTICAST_IF, ChannelOption.IP_MULTICAST_TTL, ChannelOption.IP_TOS, ChannelOption.DATAGRAM_CHANNEL_ACTIVE_ON_REGISTRATION, EpollChannelOption.SO_REUSEPORT);
    }

    @Override
    protected void autoReadCleared() {
        this.datagramChannel.clearEpollIn();
    }
}

