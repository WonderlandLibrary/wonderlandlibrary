/*
 * Decompiled with CFR 0.152.
 */
package io.netty.channel.epoll;

import io.netty.buffer.ByteBuf;
import io.netty.channel.AddressedEnvelope;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelMetadata;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelOutboundBuffer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.ChannelPromise;
import io.netty.channel.DefaultAddressedEnvelope;
import io.netty.channel.RecvByteBufAllocator;
import io.netty.channel.epoll.AbstractEpollChannel;
import io.netty.channel.epoll.EpollDatagramChannelConfig;
import io.netty.channel.epoll.Native;
import io.netty.channel.socket.DatagramChannel;
import io.netty.channel.socket.DatagramPacket;
import io.netty.util.internal.StringUtil;
import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.NetworkInterface;
import java.net.SocketAddress;
import java.net.SocketException;
import java.nio.ByteBuffer;
import java.nio.channels.NotYetConnectedException;

public final class EpollDatagramChannel
extends AbstractEpollChannel
implements DatagramChannel {
    private static final String EXPECTED_TYPES;
    private volatile InetSocketAddress remote;
    private volatile InetSocketAddress local;
    private static final ChannelMetadata METADATA;
    private final EpollDatagramChannelConfig config = new EpollDatagramChannelConfig(this);
    private volatile boolean connected;

    @Override
    public ChannelFuture joinGroup(InetAddress inetAddress, ChannelPromise channelPromise) {
        try {
            return this.joinGroup(inetAddress, NetworkInterface.getByInetAddress(this.localAddress().getAddress()), null, channelPromise);
        }
        catch (SocketException socketException) {
            channelPromise.setFailure(socketException);
            return channelPromise;
        }
    }

    @Override
    public ChannelFuture joinGroup(InetAddress inetAddress, NetworkInterface networkInterface, InetAddress inetAddress2, ChannelPromise channelPromise) {
        if (inetAddress == null) {
            throw new NullPointerException("multicastAddress");
        }
        if (networkInterface == null) {
            throw new NullPointerException("networkInterface");
        }
        channelPromise.setFailure(new UnsupportedOperationException("Multicast not supported"));
        return channelPromise;
    }

    @Override
    public EpollDatagramChannelConfig config() {
        return this.config;
    }

    @Override
    public boolean isActive() {
        return this.fd != -1 && (this.config.getOption(ChannelOption.DATAGRAM_CHANNEL_ACTIVE_ON_REGISTRATION) != false && this.isRegistered() || this.active);
    }

    @Override
    protected void doWrite(ChannelOutboundBuffer channelOutboundBuffer) throws Exception {
        while (true) {
            Object object;
            if ((object = channelOutboundBuffer.current()) == null) {
                this.clearEpollOut();
                break;
            }
            try {
                boolean bl = false;
                for (int i = this.config().getWriteSpinCount() - 1; i >= 0; --i) {
                    if (!this.doWriteMessage(object)) continue;
                    bl = true;
                    break;
                }
                if (bl) {
                    channelOutboundBuffer.remove();
                    continue;
                }
                this.setEpollOut();
            }
            catch (IOException iOException) {
                channelOutboundBuffer.remove(iOException);
                continue;
            }
            break;
        }
    }

    @Override
    public ChannelFuture leaveGroup(InetSocketAddress inetSocketAddress, NetworkInterface networkInterface) {
        return this.leaveGroup(inetSocketAddress, networkInterface, this.newPromise());
    }

    @Override
    protected Object filterOutboundMessage(Object object) {
        AddressedEnvelope addressedEnvelope;
        if (object instanceof DatagramPacket) {
            DatagramPacket datagramPacket = (DatagramPacket)object;
            ByteBuf byteBuf = (ByteBuf)datagramPacket.content();
            if (byteBuf.hasMemoryAddress()) {
                return object;
            }
            return new DatagramPacket(this.newDirectBuffer(datagramPacket, byteBuf), (InetSocketAddress)datagramPacket.recipient());
        }
        if (object instanceof ByteBuf) {
            ByteBuf byteBuf = (ByteBuf)object;
            if (byteBuf.hasMemoryAddress()) {
                return object;
            }
            return this.newDirectBuffer(byteBuf);
        }
        if (object instanceof AddressedEnvelope && (addressedEnvelope = (AddressedEnvelope)object).content() instanceof ByteBuf && (addressedEnvelope.recipient() == null || addressedEnvelope.recipient() instanceof InetSocketAddress)) {
            ByteBuf byteBuf = (ByteBuf)addressedEnvelope.content();
            if (byteBuf.hasMemoryAddress()) {
                return addressedEnvelope;
            }
            return new DefaultAddressedEnvelope<ByteBuf, InetSocketAddress>(this.newDirectBuffer(addressedEnvelope, byteBuf), (InetSocketAddress)addressedEnvelope.recipient());
        }
        throw new UnsupportedOperationException("unsupported message type: " + StringUtil.simpleClassName(object) + EXPECTED_TYPES);
    }

    @Override
    protected void doBind(SocketAddress socketAddress) throws Exception {
        InetSocketAddress inetSocketAddress = (InetSocketAddress)socketAddress;
        EpollDatagramChannel.checkResolvable(inetSocketAddress);
        Native.bind(this.fd, inetSocketAddress.getAddress(), inetSocketAddress.getPort());
        this.local = Native.localAddress(this.fd);
        this.active = true;
    }

    @Override
    protected InetSocketAddress remoteAddress0() {
        return this.remote;
    }

    @Override
    public ChannelFuture block(InetAddress inetAddress, InetAddress inetAddress2) {
        return this.block(inetAddress, inetAddress2, this.newPromise());
    }

    @Override
    protected void doDisconnect() throws Exception {
        this.connected = false;
    }

    @Override
    public ChannelFuture block(InetAddress inetAddress, NetworkInterface networkInterface, InetAddress inetAddress2) {
        return this.block(inetAddress, networkInterface, inetAddress2, this.newPromise());
    }

    @Override
    public ChannelFuture leaveGroup(InetAddress inetAddress, ChannelPromise channelPromise) {
        try {
            return this.leaveGroup(inetAddress, NetworkInterface.getByInetAddress(this.localAddress().getAddress()), null, channelPromise);
        }
        catch (SocketException socketException) {
            channelPromise.setFailure(socketException);
            return channelPromise;
        }
    }

    @Override
    public ChannelFuture leaveGroup(InetAddress inetAddress, NetworkInterface networkInterface, InetAddress inetAddress2, ChannelPromise channelPromise) {
        if (inetAddress == null) {
            throw new NullPointerException("multicastAddress");
        }
        if (networkInterface == null) {
            throw new NullPointerException("networkInterface");
        }
        channelPromise.setFailure(new UnsupportedOperationException("Multicast not supported"));
        return channelPromise;
    }

    @Override
    protected InetSocketAddress localAddress0() {
        return this.local;
    }

    @Override
    public ChannelFuture leaveGroup(InetSocketAddress inetSocketAddress, NetworkInterface networkInterface, ChannelPromise channelPromise) {
        return this.leaveGroup(inetSocketAddress.getAddress(), networkInterface, null, channelPromise);
    }

    @Override
    public ChannelFuture joinGroup(InetAddress inetAddress, NetworkInterface networkInterface, InetAddress inetAddress2) {
        return this.joinGroup(inetAddress, networkInterface, inetAddress2, this.newPromise());
    }

    @Override
    protected AbstractEpollChannel.AbstractEpollUnsafe newUnsafe() {
        return new EpollDatagramChannelUnsafe();
    }

    @Override
    public boolean isConnected() {
        return this.connected;
    }

    @Override
    public ChannelFuture joinGroup(InetSocketAddress inetSocketAddress, NetworkInterface networkInterface) {
        return this.joinGroup(inetSocketAddress, networkInterface, this.newPromise());
    }

    @Override
    public ChannelMetadata metadata() {
        return METADATA;
    }

    @Override
    public ChannelFuture joinGroup(InetSocketAddress inetSocketAddress, NetworkInterface networkInterface, ChannelPromise channelPromise) {
        return this.joinGroup(inetSocketAddress.getAddress(), networkInterface, null, channelPromise);
    }

    @Override
    public ChannelFuture block(InetAddress inetAddress, NetworkInterface networkInterface, InetAddress inetAddress2, ChannelPromise channelPromise) {
        if (inetAddress == null) {
            throw new NullPointerException("multicastAddress");
        }
        if (inetAddress2 == null) {
            throw new NullPointerException("sourceToBlock");
        }
        if (networkInterface == null) {
            throw new NullPointerException("networkInterface");
        }
        channelPromise.setFailure(new UnsupportedOperationException("Multicast not supported"));
        return channelPromise;
    }

    static {
        METADATA = new ChannelMetadata(true);
        EXPECTED_TYPES = " (expected: " + StringUtil.simpleClassName(DatagramPacket.class) + ", " + StringUtil.simpleClassName(AddressedEnvelope.class) + '<' + StringUtil.simpleClassName(ByteBuf.class) + ", " + StringUtil.simpleClassName(InetSocketAddress.class) + ">, " + StringUtil.simpleClassName(ByteBuf.class) + ')';
    }

    private boolean doWriteMessage(Object object) throws IOException {
        int n;
        InetSocketAddress inetSocketAddress;
        ByteBuf byteBuf;
        if (object instanceof AddressedEnvelope) {
            AddressedEnvelope addressedEnvelope = (AddressedEnvelope)object;
            byteBuf = (ByteBuf)addressedEnvelope.content();
            inetSocketAddress = (InetSocketAddress)addressedEnvelope.recipient();
        } else {
            byteBuf = (ByteBuf)object;
            inetSocketAddress = null;
        }
        int n2 = byteBuf.readableBytes();
        if (n2 == 0) {
            return true;
        }
        if (inetSocketAddress == null && (inetSocketAddress = this.remote) == null) {
            throw new NotYetConnectedException();
        }
        if (byteBuf.hasMemoryAddress()) {
            long l = byteBuf.memoryAddress();
            n = Native.sendToAddress(this.fd, l, byteBuf.readerIndex(), byteBuf.writerIndex(), inetSocketAddress.getAddress(), inetSocketAddress.getPort());
        } else {
            ByteBuffer byteBuffer = byteBuf.internalNioBuffer(byteBuf.readerIndex(), byteBuf.readableBytes());
            n = Native.sendTo(this.fd, byteBuffer, byteBuffer.position(), byteBuffer.limit(), inetSocketAddress.getAddress(), inetSocketAddress.getPort());
        }
        return n > 0;
    }

    @Override
    public ChannelFuture leaveGroup(InetAddress inetAddress) {
        return this.leaveGroup(inetAddress, this.newPromise());
    }

    @Override
    public ChannelFuture block(InetAddress inetAddress, InetAddress inetAddress2, ChannelPromise channelPromise) {
        try {
            return this.block(inetAddress, NetworkInterface.getByInetAddress(this.localAddress().getAddress()), inetAddress2, channelPromise);
        }
        catch (Throwable throwable) {
            channelPromise.setFailure(throwable);
            return channelPromise;
        }
    }

    @Override
    public ChannelFuture joinGroup(InetAddress inetAddress) {
        return this.joinGroup(inetAddress, this.newPromise());
    }

    @Override
    public ChannelFuture leaveGroup(InetAddress inetAddress, NetworkInterface networkInterface, InetAddress inetAddress2) {
        return this.leaveGroup(inetAddress, networkInterface, inetAddress2, this.newPromise());
    }

    public EpollDatagramChannel() {
        super(Native.socketDgramFd(), 1);
    }

    final class EpollDatagramChannelUnsafe
    extends AbstractEpollChannel.AbstractEpollUnsafe {
        private RecvByteBufAllocator.Handle allocHandle;

        @Override
        public void connect(SocketAddress socketAddress, SocketAddress socketAddress2, ChannelPromise channelPromise) {
            boolean bl = false;
            try {
                InetSocketAddress inetSocketAddress = (InetSocketAddress)socketAddress;
                if (socketAddress2 != null) {
                    InetSocketAddress inetSocketAddress2 = (InetSocketAddress)socketAddress2;
                    EpollDatagramChannel.this.doBind(inetSocketAddress2);
                }
                AbstractEpollChannel.checkResolvable(inetSocketAddress);
                EpollDatagramChannel.this.remote = inetSocketAddress;
                EpollDatagramChannel.this.local = Native.localAddress(EpollDatagramChannel.this.fd);
                bl = true;
                if (!bl) {
                    EpollDatagramChannel.this.doClose();
                } else {
                    channelPromise.setSuccess();
                    EpollDatagramChannel.this.connected = true;
                }
            }
            catch (Throwable throwable) {
                channelPromise.setFailure(throwable);
            }
        }

        EpollDatagramChannelUnsafe() {
            super(EpollDatagramChannel.this);
        }

        @Override
        void epollInReady() {
            block9: {
                EpollDatagramChannelConfig epollDatagramChannelConfig = EpollDatagramChannel.this.config();
                RecvByteBufAllocator.Handle handle = this.allocHandle;
                if (handle == null) {
                    this.allocHandle = handle = epollDatagramChannelConfig.getRecvByteBufAllocator().newHandle();
                }
                assert (EpollDatagramChannel.this.eventLoop().inEventLoop());
                ChannelPipeline channelPipeline = EpollDatagramChannel.this.pipeline();
                while (true) {
                    DatagramSocketAddress datagramSocketAddress;
                    ByteBuf byteBuf;
                    block8: {
                        byteBuf = null;
                        try {
                            byteBuf = handle.allocate(epollDatagramChannelConfig.getAllocator());
                            int n = byteBuf.writerIndex();
                            if (byteBuf.hasMemoryAddress()) {
                                datagramSocketAddress = Native.recvFromAddress(EpollDatagramChannel.this.fd, byteBuf.memoryAddress(), n, byteBuf.capacity());
                            } else {
                                ByteBuffer byteBuffer = byteBuf.internalNioBuffer(n, byteBuf.writableBytes());
                                datagramSocketAddress = Native.recvFrom(EpollDatagramChannel.this.fd, byteBuffer, byteBuffer.position(), byteBuffer.limit());
                            }
                            if (datagramSocketAddress != null) break block8;
                            if (byteBuf == null) break;
                        }
                        catch (Throwable throwable) {
                            channelPipeline.fireChannelReadComplete();
                            channelPipeline.fireExceptionCaught(throwable);
                            if (byteBuf == null) continue;
                            byteBuf.release();
                            continue;
                        }
                        byteBuf.release();
                        break;
                    }
                    int n = datagramSocketAddress.receivedAmount;
                    byteBuf.writerIndex(byteBuf.writerIndex() + n);
                    handle.record(n);
                    this.readPending = false;
                    channelPipeline.fireChannelRead(new DatagramPacket(byteBuf, (InetSocketAddress)this.localAddress(), datagramSocketAddress));
                    byteBuf = null;
                    if (byteBuf == null) continue;
                    byteBuf.release();
                    continue;
                    break;
                }
                if (EpollDatagramChannel.this.config().isAutoRead() || this.readPending) break block9;
                EpollDatagramChannel.this.clearEpollIn();
            }
        }
    }

    static final class DatagramSocketAddress
    extends InetSocketAddress {
        final int receivedAmount;
        private static final long serialVersionUID = 1348596211215015739L;

        DatagramSocketAddress(String string, int n, int n2) {
            super(string, n);
            this.receivedAmount = n2;
        }
    }
}

