/*
 * Decompiled with CFR 0.152.
 */
package io.netty.channel.socket.nio;

import io.netty.buffer.ByteBuf;
import io.netty.channel.AddressedEnvelope;
import io.netty.channel.ChannelException;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelMetadata;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelOutboundBuffer;
import io.netty.channel.ChannelPromise;
import io.netty.channel.DefaultAddressedEnvelope;
import io.netty.channel.RecvByteBufAllocator;
import io.netty.channel.nio.AbstractNioMessageChannel;
import io.netty.channel.socket.DatagramChannel;
import io.netty.channel.socket.DatagramChannelConfig;
import io.netty.channel.socket.DatagramPacket;
import io.netty.channel.socket.InternetProtocolFamily;
import io.netty.channel.socket.nio.NioDatagramChannelConfig;
import io.netty.channel.socket.nio.ProtocolFamilyConverter;
import io.netty.util.internal.PlatformDependent;
import io.netty.util.internal.StringUtil;
import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.NetworkInterface;
import java.net.SocketAddress;
import java.net.SocketException;
import java.nio.ByteBuffer;
import java.nio.channels.MembershipKey;
import java.nio.channels.SelectableChannel;
import java.nio.channels.spi.SelectorProvider;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public final class NioDatagramChannel
extends AbstractNioMessageChannel
implements DatagramChannel {
    private static final ChannelMetadata METADATA = new ChannelMetadata(true);
    private static final String EXPECTED_TYPES;
    private RecvByteBufAllocator.Handle allocHandle;
    private Map<InetAddress, List<MembershipKey>> memberships;
    private final DatagramChannelConfig config;
    private static final SelectorProvider DEFAULT_SELECTOR_PROVIDER;

    @Override
    public ChannelFuture leaveGroup(InetAddress inetAddress, NetworkInterface networkInterface, InetAddress inetAddress2, ChannelPromise channelPromise) {
        NioDatagramChannel.checkJavaVersion();
        if (inetAddress == null) {
            throw new NullPointerException("multicastAddress");
        }
        if (networkInterface == null) {
            throw new NullPointerException("networkInterface");
        }
        NioDatagramChannel nioDatagramChannel = this;
        synchronized (nioDatagramChannel) {
            List<MembershipKey> list;
            if (this.memberships != null && (list = this.memberships.get(inetAddress)) != null) {
                Iterator<MembershipKey> iterator = list.iterator();
                while (iterator.hasNext()) {
                    MembershipKey membershipKey = iterator.next();
                    if (!networkInterface.equals(membershipKey.networkInterface()) || (inetAddress2 != null || membershipKey.sourceAddress() != null) && (inetAddress2 == null || !inetAddress2.equals(membershipKey.sourceAddress()))) continue;
                    membershipKey.drop();
                    iterator.remove();
                }
                if (list.isEmpty()) {
                    this.memberships.remove(inetAddress);
                }
            }
        }
        channelPromise.setSuccess();
        return channelPromise;
    }

    @Override
    public ChannelFuture leaveGroup(InetSocketAddress inetSocketAddress, NetworkInterface networkInterface) {
        return this.leaveGroup(inetSocketAddress, networkInterface, this.newPromise());
    }

    @Override
    public DatagramChannelConfig config() {
        return this.config;
    }

    @Override
    public ChannelFuture joinGroup(InetSocketAddress inetSocketAddress, NetworkInterface networkInterface, ChannelPromise channelPromise) {
        return this.joinGroup(inetSocketAddress.getAddress(), networkInterface, null, channelPromise);
    }

    @Override
    public ChannelFuture joinGroup(InetAddress inetAddress, ChannelPromise channelPromise) {
        try {
            return this.joinGroup(inetAddress, NetworkInterface.getByInetAddress(((InetSocketAddress)this.localAddress()).getAddress()), null, channelPromise);
        }
        catch (SocketException socketException) {
            channelPromise.setFailure(socketException);
            return channelPromise;
        }
    }

    @Override
    public ChannelFuture joinGroup(InetAddress inetAddress, NetworkInterface networkInterface, InetAddress inetAddress2) {
        return this.joinGroup(inetAddress, networkInterface, inetAddress2, this.newPromise());
    }

    @Override
    public InetSocketAddress remoteAddress() {
        return (InetSocketAddress)super.remoteAddress();
    }

    @Override
    public ChannelMetadata metadata() {
        return METADATA;
    }

    private static java.nio.channels.DatagramChannel newSocket(SelectorProvider selectorProvider, InternetProtocolFamily internetProtocolFamily) {
        if (internetProtocolFamily == null) {
            return NioDatagramChannel.newSocket(selectorProvider);
        }
        NioDatagramChannel.checkJavaVersion();
        try {
            return selectorProvider.openDatagramChannel(ProtocolFamilyConverter.convert(internetProtocolFamily));
        }
        catch (IOException iOException) {
            throw new ChannelException("Failed to open a socket.", iOException);
        }
    }

    @Override
    public ChannelFuture joinGroup(InetAddress inetAddress) {
        return this.joinGroup(inetAddress, this.newPromise());
    }

    @Override
    protected boolean continueOnWriteError() {
        return true;
    }

    public NioDatagramChannel(java.nio.channels.DatagramChannel datagramChannel) {
        super(null, datagramChannel, 1);
        this.config = new NioDatagramChannelConfig(this, datagramChannel);
    }

    @Override
    public ChannelFuture leaveGroup(InetAddress inetAddress, NetworkInterface networkInterface, InetAddress inetAddress2) {
        return this.leaveGroup(inetAddress, networkInterface, inetAddress2, this.newPromise());
    }

    @Override
    public ChannelFuture block(InetAddress inetAddress, InetAddress inetAddress2) {
        return this.block(inetAddress, inetAddress2, this.newPromise());
    }

    private static void checkJavaVersion() {
        if (PlatformDependent.javaVersion() < 7) {
            throw new UnsupportedOperationException("Only supported on java 7+.");
        }
    }

    @Override
    public ChannelFuture joinGroup(InetAddress inetAddress, NetworkInterface networkInterface, InetAddress inetAddress2, ChannelPromise channelPromise) {
        NioDatagramChannel.checkJavaVersion();
        if (inetAddress == null) {
            throw new NullPointerException("multicastAddress");
        }
        if (networkInterface == null) {
            throw new NullPointerException("networkInterface");
        }
        try {
            MembershipKey membershipKey = inetAddress2 == null ? this.javaChannel().join(inetAddress, networkInterface) : this.javaChannel().join(inetAddress, networkInterface, inetAddress2);
            NioDatagramChannel nioDatagramChannel = this;
            synchronized (nioDatagramChannel) {
                List<MembershipKey> list = null;
                if (this.memberships == null) {
                    this.memberships = new HashMap<InetAddress, List<MembershipKey>>();
                } else {
                    list = this.memberships.get(inetAddress);
                }
                if (list == null) {
                    list = new ArrayList<MembershipKey>();
                    this.memberships.put(inetAddress, list);
                }
                list.add(membershipKey);
            }
            channelPromise.setSuccess();
        }
        catch (Throwable throwable) {
            channelPromise.setFailure(throwable);
        }
        return channelPromise;
    }

    @Override
    public ChannelFuture leaveGroup(InetAddress inetAddress) {
        return this.leaveGroup(inetAddress, this.newPromise());
    }

    private static java.nio.channels.DatagramChannel newSocket(SelectorProvider selectorProvider) {
        try {
            return selectorProvider.openDatagramChannel();
        }
        catch (IOException iOException) {
            throw new ChannelException("Failed to open a socket.", iOException);
        }
    }

    @Override
    public ChannelFuture leaveGroup(InetAddress inetAddress, ChannelPromise channelPromise) {
        try {
            return this.leaveGroup(inetAddress, NetworkInterface.getByInetAddress(((InetSocketAddress)this.localAddress()).getAddress()), null, channelPromise);
        }
        catch (SocketException socketException) {
            channelPromise.setFailure(socketException);
            return channelPromise;
        }
    }

    @Override
    protected void doClose() throws Exception {
        this.javaChannel().close();
    }

    @Override
    public ChannelFuture joinGroup(InetSocketAddress inetSocketAddress, NetworkInterface networkInterface) {
        return this.joinGroup(inetSocketAddress, networkInterface, this.newPromise());
    }

    @Override
    public boolean isConnected() {
        return ((java.nio.channels.DatagramChannel)this.javaChannel()).isConnected();
    }

    public NioDatagramChannel(SelectorProvider selectorProvider) {
        this(NioDatagramChannel.newSocket(selectorProvider));
    }

    private static boolean isSingleDirectBuffer(ByteBuf byteBuf) {
        return byteBuf.isDirect() && byteBuf.nioBufferCount() == 1;
    }

    @Override
    protected void doDisconnect() throws Exception {
        ((java.nio.channels.DatagramChannel)this.javaChannel()).disconnect();
    }

    @Override
    protected void doBind(SocketAddress socketAddress) throws Exception {
        ((java.nio.channels.DatagramChannel)this.javaChannel()).socket().bind(socketAddress);
    }

    public NioDatagramChannel(InternetProtocolFamily internetProtocolFamily) {
        this(NioDatagramChannel.newSocket(DEFAULT_SELECTOR_PROVIDER, internetProtocolFamily));
    }

    @Override
    public ChannelFuture block(InetAddress inetAddress, InetAddress inetAddress2, ChannelPromise channelPromise) {
        try {
            return this.block(inetAddress, NetworkInterface.getByInetAddress(((InetSocketAddress)this.localAddress()).getAddress()), inetAddress2, channelPromise);
        }
        catch (SocketException socketException) {
            channelPromise.setFailure(socketException);
            return channelPromise;
        }
    }

    @Override
    protected SocketAddress localAddress0() {
        return ((java.nio.channels.DatagramChannel)this.javaChannel()).socket().getLocalSocketAddress();
    }

    @Override
    public ChannelFuture leaveGroup(InetSocketAddress inetSocketAddress, NetworkInterface networkInterface, ChannelPromise channelPromise) {
        return this.leaveGroup(inetSocketAddress.getAddress(), networkInterface, null, channelPromise);
    }

    public NioDatagramChannel(SelectorProvider selectorProvider, InternetProtocolFamily internetProtocolFamily) {
        this(NioDatagramChannel.newSocket(selectorProvider, internetProtocolFamily));
    }

    @Override
    protected Object filterOutboundMessage(Object object) {
        AddressedEnvelope addressedEnvelope;
        if (object instanceof DatagramPacket) {
            DatagramPacket datagramPacket = (DatagramPacket)object;
            ByteBuf byteBuf = (ByteBuf)datagramPacket.content();
            if (NioDatagramChannel.isSingleDirectBuffer(byteBuf)) {
                return datagramPacket;
            }
            return new DatagramPacket(this.newDirectBuffer(datagramPacket, byteBuf), (InetSocketAddress)datagramPacket.recipient());
        }
        if (object instanceof ByteBuf) {
            ByteBuf byteBuf = (ByteBuf)object;
            if (NioDatagramChannel.isSingleDirectBuffer(byteBuf)) {
                return byteBuf;
            }
            return this.newDirectBuffer(byteBuf);
        }
        if (object instanceof AddressedEnvelope && (addressedEnvelope = (AddressedEnvelope)object).content() instanceof ByteBuf) {
            ByteBuf byteBuf = (ByteBuf)addressedEnvelope.content();
            if (NioDatagramChannel.isSingleDirectBuffer(byteBuf)) {
                return addressedEnvelope;
            }
            return new DefaultAddressedEnvelope(this.newDirectBuffer(addressedEnvelope, byteBuf), addressedEnvelope.recipient());
        }
        throw new UnsupportedOperationException("unsupported message type: " + StringUtil.simpleClassName(object) + EXPECTED_TYPES);
    }

    @Override
    protected boolean doWriteMessage(Object object, ChannelOutboundBuffer channelOutboundBuffer) throws Exception {
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
        if (n == 0) {
            return true;
        }
        ByteBuffer byteBuffer = byteBuf.internalNioBuffer(byteBuf.readerIndex(), n);
        int n2 = socketAddress != null ? ((java.nio.channels.DatagramChannel)this.javaChannel()).send(byteBuffer, socketAddress) : ((java.nio.channels.DatagramChannel)this.javaChannel()).write(byteBuffer);
        return n2 > 0;
    }

    @Override
    protected boolean doConnect(SocketAddress socketAddress, SocketAddress socketAddress2) throws Exception {
        if (socketAddress2 != null) {
            ((java.nio.channels.DatagramChannel)this.javaChannel()).socket().bind(socketAddress2);
        }
        boolean bl = false;
        ((java.nio.channels.DatagramChannel)this.javaChannel()).connect(socketAddress);
        bl = true;
        boolean bl2 = true;
        if (!bl) {
            this.doClose();
        }
        return bl2;
    }

    @Override
    public ChannelFuture block(InetAddress inetAddress, NetworkInterface networkInterface, InetAddress inetAddress2, ChannelPromise channelPromise) {
        NioDatagramChannel.checkJavaVersion();
        if (inetAddress == null) {
            throw new NullPointerException("multicastAddress");
        }
        if (inetAddress2 == null) {
            throw new NullPointerException("sourceToBlock");
        }
        if (networkInterface == null) {
            throw new NullPointerException("networkInterface");
        }
        NioDatagramChannel nioDatagramChannel = this;
        synchronized (nioDatagramChannel) {
            if (this.memberships != null) {
                List<MembershipKey> list = this.memberships.get(inetAddress);
                for (MembershipKey membershipKey : list) {
                    if (!networkInterface.equals(membershipKey.networkInterface())) continue;
                    try {
                        membershipKey.block(inetAddress2);
                    }
                    catch (IOException iOException) {
                        channelPromise.setFailure(iOException);
                    }
                }
            }
        }
        channelPromise.setSuccess();
        return channelPromise;
    }

    static {
        DEFAULT_SELECTOR_PROVIDER = SelectorProvider.provider();
        EXPECTED_TYPES = " (expected: " + StringUtil.simpleClassName(DatagramPacket.class) + ", " + StringUtil.simpleClassName(AddressedEnvelope.class) + '<' + StringUtil.simpleClassName(ByteBuf.class) + ", " + StringUtil.simpleClassName(SocketAddress.class) + ">, " + StringUtil.simpleClassName(ByteBuf.class) + ')';
    }

    @Override
    protected void setReadPending(boolean bl) {
        super.setReadPending(bl);
    }

    @Override
    protected java.nio.channels.DatagramChannel javaChannel() {
        return (java.nio.channels.DatagramChannel)super.javaChannel();
    }

    @Override
    protected void doFinishConnect() throws Exception {
        throw new Error();
    }

    @Override
    public ChannelFuture block(InetAddress inetAddress, NetworkInterface networkInterface, InetAddress inetAddress2) {
        return this.block(inetAddress, networkInterface, inetAddress2, this.newPromise());
    }

    @Override
    public boolean isActive() {
        SelectableChannel selectableChannel = this.javaChannel();
        return selectableChannel.isOpen() && (this.config.getOption(ChannelOption.DATAGRAM_CHANNEL_ACTIVE_ON_REGISTRATION) != false && this.isRegistered() || ((java.nio.channels.DatagramChannel)selectableChannel).socket().isBound());
    }

    @Override
    protected int doReadMessages(List<Object> list) throws Exception {
        int n;
        block7: {
            InetSocketAddress inetSocketAddress;
            int n2;
            ByteBuffer byteBuffer;
            boolean bl;
            ByteBuf byteBuf;
            RecvByteBufAllocator.Handle handle;
            block5: {
                int n3;
                block6: {
                    SelectableChannel selectableChannel = this.javaChannel();
                    DatagramChannelConfig datagramChannelConfig = this.config();
                    handle = this.allocHandle;
                    if (handle == null) {
                        this.allocHandle = handle = datagramChannelConfig.getRecvByteBufAllocator().newHandle();
                    }
                    byteBuf = handle.allocate(datagramChannelConfig.getAllocator());
                    bl = true;
                    try {
                        byteBuffer = byteBuf.internalNioBuffer(byteBuf.writerIndex(), byteBuf.writableBytes());
                        n2 = byteBuffer.position();
                        inetSocketAddress = (InetSocketAddress)((java.nio.channels.DatagramChannel)selectableChannel).receive(byteBuffer);
                        if (inetSocketAddress != null) break block5;
                        n3 = 0;
                        if (!bl) break block6;
                    }
                    catch (Throwable throwable) {
                        PlatformDependent.throwException(throwable);
                        n2 = -1;
                        if (bl) {
                            byteBuf.release();
                        }
                        return n2;
                    }
                    byteBuf.release();
                }
                return n3;
            }
            int n4 = byteBuffer.position() - n2;
            byteBuf.writerIndex(byteBuf.writerIndex() + n4);
            handle.record(n4);
            list.add(new DatagramPacket(byteBuf, (InetSocketAddress)this.localAddress(), inetSocketAddress));
            bl = false;
            n = 1;
            if (!bl) break block7;
            byteBuf.release();
        }
        return n;
    }

    @Override
    protected SocketAddress remoteAddress0() {
        return ((java.nio.channels.DatagramChannel)this.javaChannel()).socket().getRemoteSocketAddress();
    }

    public NioDatagramChannel() {
        this(NioDatagramChannel.newSocket(DEFAULT_SELECTOR_PROVIDER));
    }

    @Override
    public InetSocketAddress localAddress() {
        return (InetSocketAddress)super.localAddress();
    }
}

