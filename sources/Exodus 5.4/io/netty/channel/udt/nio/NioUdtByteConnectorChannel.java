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
import io.netty.channel.FileRegion;
import io.netty.channel.nio.AbstractNioByteChannel;
import io.netty.channel.udt.DefaultUdtChannelConfig;
import io.netty.channel.udt.UdtChannel;
import io.netty.channel.udt.UdtChannelConfig;
import io.netty.channel.udt.nio.NioUdtProvider;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.channels.GatheringByteChannel;
import java.nio.channels.ScatteringByteChannel;
import java.nio.channels.SelectableChannel;

public class NioUdtByteConnectorChannel
extends AbstractNioByteChannel
implements UdtChannel {
    private static final InternalLogger logger = InternalLoggerFactory.getInstance(NioUdtByteConnectorChannel.class);
    private static final ChannelMetadata METADATA = new ChannelMetadata(false);
    private final UdtChannelConfig config;

    @Override
    public UdtChannelConfig config() {
        return this.config;
    }

    protected SocketChannelUDT javaChannel() {
        return (SocketChannelUDT)super.javaChannel();
    }

    @Override
    public boolean isActive() {
        SocketChannelUDT socketChannelUDT = this.javaChannel();
        return socketChannelUDT.isOpen() && socketChannelUDT.isConnectFinished();
    }

    public NioUdtByteConnectorChannel(SocketChannelUDT socketChannelUDT) {
        this(null, socketChannelUDT);
    }

    @Override
    protected void doFinishConnect() throws Exception {
        if (!this.javaChannel().finishConnect()) {
            throw new Error("Provider error: failed to finish connect. Provider library should be upgraded.");
        }
        this.selectionKey().interestOps(this.selectionKey().interestOps() & 0xFFFFFFF7);
    }

    public NioUdtByteConnectorChannel(Channel channel, SocketChannelUDT socketChannelUDT) {
        super(channel, (SelectableChannel)socketChannelUDT);
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

    public NioUdtByteConnectorChannel() {
        this(TypeUDT.STREAM);
    }

    @Override
    protected void doDisconnect() throws Exception {
        this.doClose();
    }

    @Override
    public InetSocketAddress localAddress() {
        return (InetSocketAddress)super.localAddress();
    }

    public NioUdtByteConnectorChannel(TypeUDT typeUDT) {
        this(NioUdtProvider.newConnectorChannelUDT(typeUDT));
    }

    @Override
    protected int doWriteBytes(ByteBuf byteBuf) throws Exception {
        int n = byteBuf.readableBytes();
        return byteBuf.readBytes((GatheringByteChannel)this.javaChannel(), n);
    }

    @Override
    public InetSocketAddress remoteAddress() {
        return (InetSocketAddress)super.remoteAddress();
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
    protected int doReadBytes(ByteBuf byteBuf) throws Exception {
        return byteBuf.writeBytes((ScatteringByteChannel)this.javaChannel(), byteBuf.writableBytes());
    }

    @Override
    protected long doWriteFileRegion(FileRegion fileRegion) throws Exception {
        throw new UnsupportedOperationException();
    }

    @Override
    protected SocketAddress remoteAddress0() {
        return this.javaChannel().socket().getRemoteSocketAddress();
    }

    @Override
    protected SocketAddress localAddress0() {
        return this.javaChannel().socket().getLocalSocketAddress();
    }

    @Override
    protected void doBind(SocketAddress socketAddress) throws Exception {
        this.javaChannel().bind(socketAddress);
    }

    @Override
    protected void doClose() throws Exception {
        this.javaChannel().close();
    }
}

