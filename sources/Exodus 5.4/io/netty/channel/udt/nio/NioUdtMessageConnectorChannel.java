/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.barchart.udt.TypeUDT
 *  com.barchart.udt.nio.ChannelUDT
 *  com.barchart.udt.nio.SocketChannelUDT
 */
package io.netty.channel.udt.nio;

import com.barchart.udt.TypeUDT;
import com.barchart.udt.nio.ChannelUDT;
import com.barchart.udt.nio.SocketChannelUDT;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelException;
import io.netty.channel.ChannelMetadata;
import io.netty.channel.ChannelOutboundBuffer;
import io.netty.channel.nio.AbstractNioMessageChannel;
import io.netty.channel.udt.DefaultUdtChannelConfig;
import io.netty.channel.udt.UdtChannel;
import io.netty.channel.udt.UdtChannelConfig;
import io.netty.channel.udt.UdtMessage;
import io.netty.channel.udt.nio.NioUdtProvider;
import io.netty.util.internal.StringUtil;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.channels.ScatteringByteChannel;
import java.nio.channels.SelectableChannel;
import java.util.List;

public class NioUdtMessageConnectorChannel
extends AbstractNioMessageChannel
implements UdtChannel {
    private static final String EXPECTED_TYPE;
    private static final ChannelMetadata METADATA;
    private static final InternalLogger logger;
    private final UdtChannelConfig config;

    @Override
    public InetSocketAddress remoteAddress() {
        return (InetSocketAddress)super.remoteAddress();
    }

    @Override
    public InetSocketAddress localAddress() {
        return (InetSocketAddress)super.localAddress();
    }

    public NioUdtMessageConnectorChannel() {
        this(TypeUDT.DATAGRAM);
    }

    @Override
    protected boolean doWriteMessage(Object object, ChannelOutboundBuffer channelOutboundBuffer) throws Exception {
        UdtMessage udtMessage = (UdtMessage)object;
        ByteBuf byteBuf = udtMessage.content();
        int n = byteBuf.readableBytes();
        long l = byteBuf.nioBufferCount() == 1 ? (long)this.javaChannel().write(byteBuf.nioBuffer()) : this.javaChannel().write(byteBuf.nioBuffers());
        if (l <= 0L && n > 0) {
            return false;
        }
        if (l != (long)n) {
            throw new Error("Provider error: failed to write message. Provider library should be upgraded.");
        }
        return true;
    }

    @Override
    protected void doFinishConnect() throws Exception {
        if (!this.javaChannel().finishConnect()) {
            throw new Error("Provider error: failed to finish connect. Provider library should be upgraded.");
        }
        this.selectionKey().interestOps(this.selectionKey().interestOps() & 0xFFFFFFF7);
    }

    @Override
    protected void doBind(SocketAddress socketAddress) throws Exception {
        this.javaChannel().bind(socketAddress);
    }

    public NioUdtMessageConnectorChannel(SocketChannelUDT socketChannelUDT) {
        this(null, socketChannelUDT);
    }

    public NioUdtMessageConnectorChannel(Channel channel, SocketChannelUDT socketChannelUDT) {
        super(channel, (SelectableChannel)socketChannelUDT, 1);
        try {
            socketChannelUDT.configureBlocking(false);
            switch (socketChannelUDT.socketUDT().status()) {
                case INIT: 
                case OPENED: {
                    this.config = new DefaultUdtChannelConfig(this, (ChannelUDT)socketChannelUDT, true);
                    break;
                }
                default: {
                    this.config = new DefaultUdtChannelConfig(this, (ChannelUDT)socketChannelUDT, false);
                    break;
                }
            }
        }
        catch (Exception exception) {
            block7: {
                try {
                    socketChannelUDT.close();
                }
                catch (Exception exception2) {
                    if (!logger.isWarnEnabled()) break block7;
                    logger.warn("Failed to close channel.", exception2);
                }
            }
            throw new ChannelException("Failed to configure channel.", exception);
        }
    }

    public NioUdtMessageConnectorChannel(TypeUDT typeUDT) {
        this(NioUdtProvider.newConnectorChannelUDT(typeUDT));
    }

    @Override
    protected void doDisconnect() throws Exception {
        this.doClose();
    }

    @Override
    public boolean isActive() {
        SocketChannelUDT socketChannelUDT = this.javaChannel();
        return socketChannelUDT.isOpen() && socketChannelUDT.isConnectFinished();
    }

    @Override
    protected void doClose() throws Exception {
        this.javaChannel().close();
    }

    @Override
    protected SocketAddress remoteAddress0() {
        return this.javaChannel().socket().getRemoteSocketAddress();
    }

    @Override
    protected boolean doConnect(SocketAddress socketAddress, SocketAddress socketAddress2) throws Exception {
        this.doBind(socketAddress2 != null ? socketAddress2 : new InetSocketAddress(0));
        boolean bl = false;
        boolean bl2 = this.javaChannel().connect(socketAddress);
        if (!bl2) {
            this.selectionKey().interestOps(this.selectionKey().interestOps() | 8);
        }
        bl = true;
        boolean bl3 = bl2;
        if (!bl) {
            this.doClose();
        }
        return bl3;
    }

    @Override
    public ChannelMetadata metadata() {
        return METADATA;
    }

    @Override
    protected SocketAddress localAddress0() {
        return this.javaChannel().socket().getLocalSocketAddress();
    }

    static {
        logger = InternalLoggerFactory.getInstance(NioUdtMessageConnectorChannel.class);
        METADATA = new ChannelMetadata(false);
        EXPECTED_TYPE = " (expected: " + StringUtil.simpleClassName(UdtMessage.class) + ')';
    }

    @Override
    protected final Object filterOutboundMessage(Object object) throws Exception {
        if (object instanceof UdtMessage) {
            return object;
        }
        throw new UnsupportedOperationException("unsupported message type: " + StringUtil.simpleClassName(object) + EXPECTED_TYPE);
    }

    @Override
    public UdtChannelConfig config() {
        return this.config;
    }

    protected SocketChannelUDT javaChannel() {
        return (SocketChannelUDT)super.javaChannel();
    }

    @Override
    protected int doReadMessages(List<Object> list) throws Exception {
        int n = this.config.getReceiveBufferSize();
        ByteBuf byteBuf = this.config.getAllocator().directBuffer(n);
        int n2 = byteBuf.writeBytes((ScatteringByteChannel)this.javaChannel(), n);
        if (n2 <= 0) {
            byteBuf.release();
            return 0;
        }
        if (n2 >= n) {
            this.javaChannel().close();
            throw new ChannelException("Invalid config : increase receive buffer size to avoid message truncation");
        }
        list.add(new UdtMessage(byteBuf));
        return 1;
    }
}

