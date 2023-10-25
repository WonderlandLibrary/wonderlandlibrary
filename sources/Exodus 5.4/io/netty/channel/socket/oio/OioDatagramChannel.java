/*
 * Decompiled with CFR 0.152.
 */
package io.netty.channel.socket.oio;

import io.netty.buffer.ByteBuf;
import io.netty.channel.AddressedEnvelope;
import io.netty.channel.ChannelConfig;
import io.netty.channel.ChannelException;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelMetadata;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelOutboundBuffer;
import io.netty.channel.ChannelPromise;
import io.netty.channel.RecvByteBufAllocator;
import io.netty.channel.oio.AbstractOioMessageChannel;
import io.netty.channel.socket.DatagramChannel;
import io.netty.channel.socket.DatagramChannelConfig;
import io.netty.channel.socket.DatagramPacket;
import io.netty.channel.socket.DefaultDatagramChannelConfig;
import io.netty.util.internal.EmptyArrays;
import io.netty.util.internal.PlatformDependent;
import io.netty.util.internal.StringUtil;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;
import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.MulticastSocket;
import java.net.NetworkInterface;
import java.net.SocketAddress;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.List;
import java.util.Locale;

public class OioDatagramChannel
extends AbstractOioMessageChannel
implements DatagramChannel {
    private final java.net.DatagramPacket tmpPacket;
    private final DatagramChannelConfig config;
    private final MulticastSocket socket;
    private static final ChannelMetadata METADATA;
    private static final InternalLogger logger;
    private RecvByteBufAllocator.Handle allocHandle;
    private static final String EXPECTED_TYPES;

    @Override
    protected void doConnect(SocketAddress socketAddress, SocketAddress socketAddress2) throws Exception {
        block4: {
            if (socketAddress2 != null) {
                this.socket.bind(socketAddress2);
            }
            boolean bl = false;
            this.socket.connect(socketAddress);
            bl = true;
            if (bl) break block4;
            try {
                this.socket.close();
            }
            catch (Throwable throwable) {
                logger.warn("Failed to close a socket.", throwable);
            }
        }
    }

    @Override
    public ChannelFuture joinGroup(InetSocketAddress inetSocketAddress, NetworkInterface networkInterface, ChannelPromise channelPromise) {
        this.ensureBound();
        try {
            this.socket.joinGroup(inetSocketAddress, networkInterface);
            channelPromise.setSuccess();
        }
        catch (IOException iOException) {
            channelPromise.setFailure(iOException);
        }
        return channelPromise;
    }

    @Override
    protected int doReadMessages(List<Object> list) throws Exception {
        int n;
        block9: {
            ChannelConfig channelConfig = this.config();
            RecvByteBufAllocator.Handle handle = this.allocHandle;
            if (handle == null) {
                this.allocHandle = handle = channelConfig.getRecvByteBufAllocator().newHandle();
            }
            ByteBuf byteBuf = channelConfig.getAllocator().heapBuffer(handle.guess());
            boolean bl = true;
            try {
                this.tmpPacket.setData(byteBuf.array(), byteBuf.arrayOffset(), byteBuf.capacity());
                this.socket.receive(this.tmpPacket);
                InetSocketAddress inetSocketAddress = (InetSocketAddress)this.tmpPacket.getSocketAddress();
                int n2 = this.tmpPacket.getLength();
                handle.record(n2);
                list.add(new DatagramPacket(byteBuf.writerIndex(n2), (InetSocketAddress)this.localAddress(), inetSocketAddress));
                bl = false;
                n = 1;
                if (!bl) break block9;
            }
            catch (SocketTimeoutException socketTimeoutException) {
                int n3 = 0;
                if (bl) {
                    byteBuf.release();
                }
                return n3;
            }
            catch (SocketException socketException) {
                if (!socketException.getMessage().toLowerCase(Locale.US).contains("socket closed")) {
                    throw socketException;
                }
                int n4 = -1;
                if (bl) {
                    byteBuf.release();
                }
                return n4;
            }
            catch (Throwable throwable) {
                PlatformDependent.throwException(throwable);
                int n5 = -1;
                if (bl) {
                    byteBuf.release();
                }
                return n5;
            }
            byteBuf.release();
        }
        return n;
    }

    @Override
    protected void doDisconnect() throws Exception {
        this.socket.disconnect();
    }

    @Override
    public ChannelFuture joinGroup(InetAddress inetAddress) {
        return this.joinGroup(inetAddress, this.newPromise());
    }

    @Override
    public ChannelFuture leaveGroup(InetAddress inetAddress, NetworkInterface networkInterface, InetAddress inetAddress2) {
        return this.newFailedFuture(new UnsupportedOperationException());
    }

    @Override
    public ChannelFuture block(InetAddress inetAddress, InetAddress inetAddress2, ChannelPromise channelPromise) {
        channelPromise.setFailure(new UnsupportedOperationException());
        return channelPromise;
    }

    @Override
    public boolean isActive() {
        return this.isOpen() && (this.config.getOption(ChannelOption.DATAGRAM_CHANNEL_ACTIVE_ON_REGISTRATION) != false && this.isRegistered() || this.socket.isBound());
    }

    @Override
    public ChannelFuture leaveGroup(InetSocketAddress inetSocketAddress, NetworkInterface networkInterface) {
        return this.leaveGroup(inetSocketAddress, networkInterface, this.newPromise());
    }

    private void ensureBound() {
        if (!this.isActive()) {
            throw new IllegalStateException(DatagramChannel.class.getName() + " must be bound to join a group.");
        }
    }

    @Override
    public ChannelFuture leaveGroup(InetAddress inetAddress, ChannelPromise channelPromise) {
        try {
            this.socket.leaveGroup(inetAddress);
            channelPromise.setSuccess();
        }
        catch (IOException iOException) {
            channelPromise.setFailure(iOException);
        }
        return channelPromise;
    }

    @Override
    public ChannelFuture block(InetAddress inetAddress, NetworkInterface networkInterface, InetAddress inetAddress2, ChannelPromise channelPromise) {
        channelPromise.setFailure(new UnsupportedOperationException());
        return channelPromise;
    }

    @Override
    public boolean isConnected() {
        return this.socket.isConnected();
    }

    @Override
    public ChannelFuture joinGroup(InetAddress inetAddress, NetworkInterface networkInterface, InetAddress inetAddress2) {
        return this.newFailedFuture(new UnsupportedOperationException());
    }

    @Override
    protected void doBind(SocketAddress socketAddress) throws Exception {
        this.socket.bind(socketAddress);
    }

    private static MulticastSocket newSocket() {
        try {
            return new MulticastSocket(null);
        }
        catch (Exception exception) {
            throw new ChannelException("failed to create a new socket", exception);
        }
    }

    @Override
    public ChannelFuture leaveGroup(InetAddress inetAddress, NetworkInterface networkInterface, InetAddress inetAddress2, ChannelPromise channelPromise) {
        channelPromise.setFailure(new UnsupportedOperationException());
        return channelPromise;
    }

    @Override
    public ChannelFuture block(InetAddress inetAddress, NetworkInterface networkInterface, InetAddress inetAddress2) {
        return this.newFailedFuture(new UnsupportedOperationException());
    }

    @Override
    public ChannelFuture leaveGroup(InetSocketAddress inetSocketAddress, NetworkInterface networkInterface, ChannelPromise channelPromise) {
        try {
            this.socket.leaveGroup(inetSocketAddress, networkInterface);
            channelPromise.setSuccess();
        }
        catch (IOException iOException) {
            channelPromise.setFailure(iOException);
        }
        return channelPromise;
    }

    public OioDatagramChannel() {
        this(OioDatagramChannel.newSocket());
    }

    @Override
    public InetSocketAddress remoteAddress() {
        return (InetSocketAddress)super.remoteAddress();
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public OioDatagramChannel(MulticastSocket multicastSocket) {
        block2: {
            super(null);
            this.tmpPacket = new java.net.DatagramPacket(EmptyArrays.EMPTY_BYTES, 0);
            boolean bl = false;
            try {
                multicastSocket.setSoTimeout(1000);
                multicastSocket.setBroadcast(false);
                bl = true;
                if (bl) break block2;
            }
            catch (SocketException socketException) {
                throw new ChannelException("Failed to configure the datagram socket timeout.", socketException);
            }
            multicastSocket.close();
        }
        this.socket = multicastSocket;
        this.config = new DefaultDatagramChannelConfig(this, multicastSocket);
    }

    @Override
    public DatagramChannelConfig config() {
        return this.config;
    }

    @Override
    public boolean isOpen() {
        return !this.socket.isClosed();
    }

    @Override
    protected SocketAddress remoteAddress0() {
        return this.socket.getRemoteSocketAddress();
    }

    @Override
    protected Object filterOutboundMessage(Object object) {
        AddressedEnvelope addressedEnvelope;
        if (object instanceof DatagramPacket || object instanceof ByteBuf) {
            return object;
        }
        if (object instanceof AddressedEnvelope && (addressedEnvelope = (AddressedEnvelope)object).content() instanceof ByteBuf) {
            return object;
        }
        throw new UnsupportedOperationException("unsupported message type: " + StringUtil.simpleClassName(object) + EXPECTED_TYPES);
    }

    @Override
    protected SocketAddress localAddress0() {
        return this.socket.getLocalSocketAddress();
    }

    @Override
    public ChannelFuture joinGroup(InetAddress inetAddress, NetworkInterface networkInterface, InetAddress inetAddress2, ChannelPromise channelPromise) {
        channelPromise.setFailure(new UnsupportedOperationException());
        return channelPromise;
    }

    @Override
    public ChannelFuture joinGroup(InetAddress inetAddress, ChannelPromise channelPromise) {
        this.ensureBound();
        try {
            this.socket.joinGroup(inetAddress);
            channelPromise.setSuccess();
        }
        catch (IOException iOException) {
            channelPromise.setFailure(iOException);
        }
        return channelPromise;
    }

    static {
        logger = InternalLoggerFactory.getInstance(OioDatagramChannel.class);
        METADATA = new ChannelMetadata(true);
        EXPECTED_TYPES = " (expected: " + StringUtil.simpleClassName(DatagramPacket.class) + ", " + StringUtil.simpleClassName(AddressedEnvelope.class) + '<' + StringUtil.simpleClassName(ByteBuf.class) + ", " + StringUtil.simpleClassName(SocketAddress.class) + ">, " + StringUtil.simpleClassName(ByteBuf.class) + ')';
    }

    @Override
    public InetSocketAddress localAddress() {
        return (InetSocketAddress)super.localAddress();
    }

    @Override
    public ChannelFuture leaveGroup(InetAddress inetAddress) {
        return this.leaveGroup(inetAddress, this.newPromise());
    }

    @Override
    public ChannelFuture block(InetAddress inetAddress, InetAddress inetAddress2) {
        return this.newFailedFuture(new UnsupportedOperationException());
    }

    @Override
    public ChannelMetadata metadata() {
        return METADATA;
    }

    @Override
    protected void doWrite(ChannelOutboundBuffer channelOutboundBuffer) throws Exception {
        Object object;
        while ((object = channelOutboundBuffer.current()) != null) {
            ByteBuf byteBuf;
            SocketAddress socketAddress;
            if (object instanceof AddressedEnvelope) {
                AddressedEnvelope addressedEnvelope = (AddressedEnvelope)object;
                socketAddress = (SocketAddress)addressedEnvelope.recipient();
                byteBuf = (ByteBuf)addressedEnvelope.content();
            } else {
                byteBuf = (ByteBuf)object;
                socketAddress = null;
            }
            int n = byteBuf.readableBytes();
            if (socketAddress != null) {
                this.tmpPacket.setSocketAddress(socketAddress);
            }
            if (byteBuf.hasArray()) {
                this.tmpPacket.setData(byteBuf.array(), byteBuf.arrayOffset() + byteBuf.readerIndex(), n);
            } else {
                byte[] byArray = new byte[n];
                byteBuf.getBytes(byteBuf.readerIndex(), byArray);
                this.tmpPacket.setData(byArray);
            }
            try {
                this.socket.send(this.tmpPacket);
                channelOutboundBuffer.remove();
            }
            catch (IOException iOException) {
                channelOutboundBuffer.remove(iOException);
            }
        }
    }

    @Override
    protected void doClose() throws Exception {
        this.socket.close();
    }

    @Override
    public ChannelFuture joinGroup(InetSocketAddress inetSocketAddress, NetworkInterface networkInterface) {
        return this.joinGroup(inetSocketAddress, networkInterface, this.newPromise());
    }
}

