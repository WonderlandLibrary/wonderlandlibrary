/*
 * Decompiled with CFR 0.152.
 */
package io.netty.channel.socket;

import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.ChannelException;
import io.netty.channel.ChannelOption;
import io.netty.channel.DefaultChannelConfig;
import io.netty.channel.FixedRecvByteBufAllocator;
import io.netty.channel.MessageSizeEstimator;
import io.netty.channel.RecvByteBufAllocator;
import io.netty.channel.socket.DatagramChannel;
import io.netty.channel.socket.DatagramChannelConfig;
import io.netty.util.internal.PlatformDependent;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;
import java.io.IOException;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Map;

public class DefaultDatagramChannelConfig
extends DefaultChannelConfig
implements DatagramChannelConfig {
    private volatile boolean activeOnOpen;
    private final DatagramSocket javaSocket;
    private static final RecvByteBufAllocator DEFAULT_RCVBUF_ALLOCATOR;
    private static final InternalLogger logger;

    private void setActiveOnOpen(boolean bl) {
        if (this.channel.isRegistered()) {
            throw new IllegalStateException("Can only changed before channel was registered");
        }
        this.activeOnOpen = bl;
    }

    @Override
    public int getTimeToLive() {
        if (this.javaSocket instanceof MulticastSocket) {
            try {
                return ((MulticastSocket)this.javaSocket).getTimeToLive();
            }
            catch (IOException iOException) {
                throw new ChannelException(iOException);
            }
        }
        throw new UnsupportedOperationException();
    }

    @Override
    public DatagramChannelConfig setTrafficClass(int n) {
        try {
            this.javaSocket.setTrafficClass(n);
        }
        catch (SocketException socketException) {
            throw new ChannelException(socketException);
        }
        return this;
    }

    @Override
    public DatagramChannelConfig setSendBufferSize(int n) {
        try {
            this.javaSocket.setSendBufferSize(n);
        }
        catch (SocketException socketException) {
            throw new ChannelException(socketException);
        }
        return this;
    }

    @Override
    public boolean isBroadcast() {
        try {
            return this.javaSocket.getBroadcast();
        }
        catch (SocketException socketException) {
            throw new ChannelException(socketException);
        }
    }

    @Override
    public DatagramChannelConfig setAllocator(ByteBufAllocator byteBufAllocator) {
        super.setAllocator(byteBufAllocator);
        return this;
    }

    @Override
    public Map<ChannelOption<?>, Object> getOptions() {
        return this.getOptions(super.getOptions(), ChannelOption.SO_BROADCAST, ChannelOption.SO_RCVBUF, ChannelOption.SO_SNDBUF, ChannelOption.SO_REUSEADDR, ChannelOption.IP_MULTICAST_LOOP_DISABLED, ChannelOption.IP_MULTICAST_ADDR, ChannelOption.IP_MULTICAST_IF, ChannelOption.IP_MULTICAST_TTL, ChannelOption.IP_TOS, ChannelOption.DATAGRAM_CHANNEL_ACTIVE_ON_REGISTRATION);
    }

    @Override
    public NetworkInterface getNetworkInterface() {
        if (this.javaSocket instanceof MulticastSocket) {
            try {
                return ((MulticastSocket)this.javaSocket).getNetworkInterface();
            }
            catch (SocketException socketException) {
                throw new ChannelException(socketException);
            }
        }
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isLoopbackModeDisabled() {
        if (this.javaSocket instanceof MulticastSocket) {
            try {
                return ((MulticastSocket)this.javaSocket).getLoopbackMode();
            }
            catch (SocketException socketException) {
                throw new ChannelException(socketException);
            }
        }
        throw new UnsupportedOperationException();
    }

    @Override
    public int getTrafficClass() {
        try {
            return this.javaSocket.getTrafficClass();
        }
        catch (SocketException socketException) {
            throw new ChannelException(socketException);
        }
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
        return super.getOption(channelOption);
    }

    @Override
    public DatagramChannelConfig setMessageSizeEstimator(MessageSizeEstimator messageSizeEstimator) {
        super.setMessageSizeEstimator(messageSizeEstimator);
        return this;
    }

    public DefaultDatagramChannelConfig(DatagramChannel datagramChannel, DatagramSocket datagramSocket) {
        super(datagramChannel);
        if (datagramSocket == null) {
            throw new NullPointerException("javaSocket");
        }
        this.javaSocket = datagramSocket;
        this.setRecvByteBufAllocator(DEFAULT_RCVBUF_ALLOCATOR);
    }

    @Override
    public DatagramChannelConfig setLoopbackModeDisabled(boolean bl) {
        if (this.javaSocket instanceof MulticastSocket) {
            try {
                ((MulticastSocket)this.javaSocket).setLoopbackMode(bl);
            }
            catch (SocketException socketException) {
                throw new ChannelException(socketException);
            }
        } else {
            throw new UnsupportedOperationException();
        }
        return this;
    }

    @Override
    public DatagramChannelConfig setConnectTimeoutMillis(int n) {
        super.setConnectTimeoutMillis(n);
        return this;
    }

    @Override
    public DatagramChannelConfig setTimeToLive(int n) {
        if (this.javaSocket instanceof MulticastSocket) {
            try {
                ((MulticastSocket)this.javaSocket).setTimeToLive(n);
            }
            catch (IOException iOException) {
                throw new ChannelException(iOException);
            }
        } else {
            throw new UnsupportedOperationException();
        }
        return this;
    }

    @Override
    public DatagramChannelConfig setWriteBufferLowWaterMark(int n) {
        super.setWriteBufferLowWaterMark(n);
        return this;
    }

    @Override
    public DatagramChannelConfig setReceiveBufferSize(int n) {
        try {
            this.javaSocket.setReceiveBufferSize(n);
        }
        catch (SocketException socketException) {
            throw new ChannelException(socketException);
        }
        return this;
    }

    @Override
    public InetAddress getInterface() {
        if (this.javaSocket instanceof MulticastSocket) {
            try {
                return ((MulticastSocket)this.javaSocket).getInterface();
            }
            catch (SocketException socketException) {
                throw new ChannelException(socketException);
            }
        }
        throw new UnsupportedOperationException();
    }

    @Override
    public DatagramChannelConfig setBroadcast(boolean bl) {
        try {
            if (bl && !PlatformDependent.isWindows() && !PlatformDependent.isRoot() && !this.javaSocket.getLocalAddress().isAnyLocalAddress()) {
                logger.warn("A non-root user can't receive a broadcast packet if the socket is not bound to a wildcard address; setting the SO_BROADCAST flag anyway as requested on the socket which is bound to " + this.javaSocket.getLocalSocketAddress() + '.');
            }
            this.javaSocket.setBroadcast(bl);
        }
        catch (SocketException socketException) {
            throw new ChannelException(socketException);
        }
        return this;
    }

    @Override
    public boolean isReuseAddress() {
        try {
            return this.javaSocket.getReuseAddress();
        }
        catch (SocketException socketException) {
            throw new ChannelException(socketException);
        }
    }

    @Override
    public DatagramChannelConfig setNetworkInterface(NetworkInterface networkInterface) {
        if (this.javaSocket instanceof MulticastSocket) {
            try {
                ((MulticastSocket)this.javaSocket).setNetworkInterface(networkInterface);
            }
            catch (SocketException socketException) {
                throw new ChannelException(socketException);
            }
        } else {
            throw new UnsupportedOperationException();
        }
        return this;
    }

    static {
        logger = InternalLoggerFactory.getInstance(DefaultDatagramChannelConfig.class);
        DEFAULT_RCVBUF_ALLOCATOR = new FixedRecvByteBufAllocator(2048);
    }

    @Override
    public int getReceiveBufferSize() {
        try {
            return this.javaSocket.getReceiveBufferSize();
        }
        catch (SocketException socketException) {
            throw new ChannelException(socketException);
        }
    }

    @Override
    public DatagramChannelConfig setWriteSpinCount(int n) {
        super.setWriteSpinCount(n);
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
        } else {
            return super.setOption(channelOption, t);
        }
        return true;
    }

    @Override
    public DatagramChannelConfig setAutoClose(boolean bl) {
        super.setAutoClose(bl);
        return this;
    }

    @Override
    public int getSendBufferSize() {
        try {
            return this.javaSocket.getSendBufferSize();
        }
        catch (SocketException socketException) {
            throw new ChannelException(socketException);
        }
    }

    @Override
    public DatagramChannelConfig setAutoRead(boolean bl) {
        super.setAutoRead(bl);
        return this;
    }

    @Override
    public DatagramChannelConfig setReuseAddress(boolean bl) {
        try {
            this.javaSocket.setReuseAddress(bl);
        }
        catch (SocketException socketException) {
            throw new ChannelException(socketException);
        }
        return this;
    }

    @Override
    public DatagramChannelConfig setRecvByteBufAllocator(RecvByteBufAllocator recvByteBufAllocator) {
        super.setRecvByteBufAllocator(recvByteBufAllocator);
        return this;
    }

    @Override
    public DatagramChannelConfig setInterface(InetAddress inetAddress) {
        if (this.javaSocket instanceof MulticastSocket) {
            try {
                ((MulticastSocket)this.javaSocket).setInterface(inetAddress);
            }
            catch (SocketException socketException) {
                throw new ChannelException(socketException);
            }
        } else {
            throw new UnsupportedOperationException();
        }
        return this;
    }

    @Override
    public DatagramChannelConfig setMaxMessagesPerRead(int n) {
        super.setMaxMessagesPerRead(n);
        return this;
    }

    @Override
    public DatagramChannelConfig setWriteBufferHighWaterMark(int n) {
        super.setWriteBufferHighWaterMark(n);
        return this;
    }
}

