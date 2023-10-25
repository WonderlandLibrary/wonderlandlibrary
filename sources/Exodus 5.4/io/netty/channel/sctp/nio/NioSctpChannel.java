/*
 * Decompiled with CFR 0.152.
 */
package io.netty.channel.sctp.nio;

import com.sun.nio.sctp.Association;
import com.sun.nio.sctp.MessageInfo;
import com.sun.nio.sctp.NotificationHandler;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.Channel;
import io.netty.channel.ChannelException;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelMetadata;
import io.netty.channel.ChannelOutboundBuffer;
import io.netty.channel.ChannelPromise;
import io.netty.channel.RecvByteBufAllocator;
import io.netty.channel.nio.AbstractNioMessageChannel;
import io.netty.channel.sctp.DefaultSctpChannelConfig;
import io.netty.channel.sctp.SctpChannel;
import io.netty.channel.sctp.SctpChannelConfig;
import io.netty.channel.sctp.SctpMessage;
import io.netty.channel.sctp.SctpNotificationHandler;
import io.netty.channel.sctp.SctpServerChannel;
import io.netty.util.concurrent.AbstractEventExecutor;
import io.netty.util.concurrent.SingleThreadEventExecutor;
import io.netty.util.internal.PlatformDependent;
import io.netty.util.internal.StringUtil;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;
import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectableChannel;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class NioSctpChannel
extends AbstractNioMessageChannel
implements SctpChannel {
    private final NotificationHandler<?> notificationHandler;
    private RecvByteBufAllocator.Handle allocHandle;
    private final SctpChannelConfig config;
    private static final InternalLogger logger;
    private static final ChannelMetadata METADATA;

    public NioSctpChannel(com.sun.nio.sctp.SctpChannel sctpChannel) {
        this(null, sctpChannel);
    }

    @Override
    public ChannelFuture unbindAddress(final InetAddress inetAddress, final ChannelPromise channelPromise) {
        if (((AbstractEventExecutor)((Object)this.eventLoop())).inEventLoop()) {
            try {
                ((com.sun.nio.sctp.SctpChannel)this.javaChannel()).unbindAddress(inetAddress);
                channelPromise.setSuccess();
            }
            catch (Throwable throwable) {
                channelPromise.setFailure(throwable);
            }
        } else {
            ((SingleThreadEventExecutor)((Object)this.eventLoop())).execute(new Runnable(){

                @Override
                public void run() {
                    NioSctpChannel.this.unbindAddress(inetAddress, channelPromise);
                }
            });
        }
        return channelPromise;
    }

    static {
        METADATA = new ChannelMetadata(false);
        logger = InternalLoggerFactory.getInstance(NioSctpChannel.class);
    }

    @Override
    protected void doClose() throws Exception {
        this.javaChannel().close();
    }

    @Override
    public ChannelFuture unbindAddress(InetAddress inetAddress) {
        return this.unbindAddress(inetAddress, this.newPromise());
    }

    @Override
    public SctpServerChannel parent() {
        return (SctpServerChannel)super.parent();
    }

    @Override
    public boolean isActive() {
        SelectableChannel selectableChannel = this.javaChannel();
        return selectableChannel.isOpen() && this.association() != null;
    }

    @Override
    protected void doDisconnect() throws Exception {
        this.doClose();
    }

    public NioSctpChannel(Channel channel, com.sun.nio.sctp.SctpChannel sctpChannel) {
        super(channel, sctpChannel, 1);
        try {
            sctpChannel.configureBlocking(false);
            this.config = new NioSctpChannelConfig(this, sctpChannel);
            this.notificationHandler = new SctpNotificationHandler(this);
        }
        catch (IOException iOException) {
            block4: {
                try {
                    sctpChannel.close();
                }
                catch (IOException iOException2) {
                    if (!logger.isWarnEnabled()) break block4;
                    logger.warn("Failed to close a partially initialized sctp channel.", iOException2);
                }
            }
            throw new ChannelException("Failed to enter non-blocking mode.", iOException);
        }
    }

    @Override
    public SctpChannelConfig config() {
        return this.config;
    }

    @Override
    public InetSocketAddress remoteAddress() {
        return (InetSocketAddress)super.remoteAddress();
    }

    @Override
    public ChannelFuture bindAddress(InetAddress inetAddress) {
        return this.bindAddress(inetAddress, this.newPromise());
    }

    @Override
    public Association association() {
        try {
            return ((com.sun.nio.sctp.SctpChannel)this.javaChannel()).association();
        }
        catch (IOException iOException) {
            return null;
        }
    }

    @Override
    protected void doBind(SocketAddress socketAddress) throws Exception {
        ((com.sun.nio.sctp.SctpChannel)this.javaChannel()).bind(socketAddress);
    }

    @Override
    protected final Object filterOutboundMessage(Object object) throws Exception {
        if (object instanceof SctpMessage) {
            SctpMessage sctpMessage = (SctpMessage)object;
            ByteBuf byteBuf = sctpMessage.content();
            if (byteBuf.isDirect() && byteBuf.nioBufferCount() == 1) {
                return sctpMessage;
            }
            return new SctpMessage(sctpMessage.protocolIdentifier(), sctpMessage.streamIdentifier(), this.newDirectBuffer(sctpMessage, byteBuf));
        }
        throw new UnsupportedOperationException("unsupported message type: " + StringUtil.simpleClassName(object) + " (expected: " + StringUtil.simpleClassName(SctpMessage.class));
    }

    @Override
    protected void doFinishConnect() throws Exception {
        if (!((com.sun.nio.sctp.SctpChannel)this.javaChannel()).finishConnect()) {
            throw new Error();
        }
    }

    public NioSctpChannel() {
        this(NioSctpChannel.newSctpChannel());
    }

    @Override
    public Set<InetSocketAddress> allRemoteAddresses() {
        try {
            Set<SocketAddress> set = ((com.sun.nio.sctp.SctpChannel)this.javaChannel()).getRemoteAddresses();
            HashSet<InetSocketAddress> hashSet = new HashSet<InetSocketAddress>(set.size());
            for (SocketAddress socketAddress : set) {
                hashSet.add((InetSocketAddress)socketAddress);
            }
            return hashSet;
        }
        catch (Throwable throwable) {
            return Collections.emptySet();
        }
    }

    @Override
    protected SocketAddress remoteAddress0() {
        try {
            Iterator<SocketAddress> iterator = ((com.sun.nio.sctp.SctpChannel)this.javaChannel()).getRemoteAddresses().iterator();
            if (iterator.hasNext()) {
                return iterator.next();
            }
        }
        catch (IOException iOException) {
            // empty catch block
        }
        return null;
    }

    @Override
    public ChannelMetadata metadata() {
        return METADATA;
    }

    @Override
    public InetSocketAddress localAddress() {
        return (InetSocketAddress)super.localAddress();
    }

    private static com.sun.nio.sctp.SctpChannel newSctpChannel() {
        try {
            return com.sun.nio.sctp.SctpChannel.open();
        }
        catch (IOException iOException) {
            throw new ChannelException("Failed to open a sctp channel.", iOException);
        }
    }

    @Override
    public Set<InetSocketAddress> allLocalAddresses() {
        try {
            Set<SocketAddress> set = ((com.sun.nio.sctp.SctpChannel)this.javaChannel()).getAllLocalAddresses();
            LinkedHashSet<InetSocketAddress> linkedHashSet = new LinkedHashSet<InetSocketAddress>(set.size());
            for (SocketAddress socketAddress : set) {
                linkedHashSet.add((InetSocketAddress)socketAddress);
            }
            return linkedHashSet;
        }
        catch (Throwable throwable) {
            return Collections.emptySet();
        }
    }

    @Override
    protected com.sun.nio.sctp.SctpChannel javaChannel() {
        return (com.sun.nio.sctp.SctpChannel)super.javaChannel();
    }

    @Override
    public ChannelFuture bindAddress(final InetAddress inetAddress, final ChannelPromise channelPromise) {
        if (((AbstractEventExecutor)((Object)this.eventLoop())).inEventLoop()) {
            try {
                ((com.sun.nio.sctp.SctpChannel)this.javaChannel()).bindAddress(inetAddress);
                channelPromise.setSuccess();
            }
            catch (Throwable throwable) {
                channelPromise.setFailure(throwable);
            }
        } else {
            ((SingleThreadEventExecutor)((Object)this.eventLoop())).execute(new Runnable(){

                @Override
                public void run() {
                    NioSctpChannel.this.bindAddress(inetAddress, channelPromise);
                }
            });
        }
        return channelPromise;
    }

    @Override
    protected SocketAddress localAddress0() {
        try {
            Iterator<SocketAddress> iterator = ((com.sun.nio.sctp.SctpChannel)this.javaChannel()).getAllLocalAddresses().iterator();
            if (iterator.hasNext()) {
                return iterator.next();
            }
        }
        catch (IOException iOException) {
            // empty catch block
        }
        return null;
    }

    @Override
    protected boolean doConnect(SocketAddress socketAddress, SocketAddress socketAddress2) throws Exception {
        if (socketAddress2 != null) {
            ((com.sun.nio.sctp.SctpChannel)this.javaChannel()).bind(socketAddress2);
        }
        boolean bl = false;
        boolean bl2 = ((com.sun.nio.sctp.SctpChannel)this.javaChannel()).connect(socketAddress);
        if (!bl2) {
            this.selectionKey().interestOps(8);
        }
        bl = true;
        boolean bl3 = bl2;
        if (!bl) {
            this.doClose();
        }
        return bl3;
    }

    @Override
    protected int doReadMessages(List<Object> list) throws Exception {
        int n;
        block7: {
            MessageInfo messageInfo;
            int n2;
            ByteBuffer byteBuffer;
            boolean bl;
            ByteBuf byteBuf;
            RecvByteBufAllocator.Handle handle;
            block5: {
                int n3;
                block6: {
                    SelectableChannel selectableChannel = this.javaChannel();
                    handle = this.allocHandle;
                    if (handle == null) {
                        this.allocHandle = handle = this.config().getRecvByteBufAllocator().newHandle();
                    }
                    byteBuf = handle.allocate(this.config().getAllocator());
                    bl = true;
                    try {
                        byteBuffer = byteBuf.internalNioBuffer(byteBuf.writerIndex(), byteBuf.writableBytes());
                        n2 = byteBuffer.position();
                        messageInfo = ((com.sun.nio.sctp.SctpChannel)selectableChannel).receive(byteBuffer, null, this.notificationHandler);
                        if (messageInfo != null) break block5;
                        n3 = 0;
                        int n4 = byteBuf.readableBytes();
                        handle.record(n4);
                        if (!bl) break block6;
                    }
                    catch (Throwable throwable) {
                        PlatformDependent.throwException(throwable);
                        n2 = -1;
                        int n5 = byteBuf.readableBytes();
                        handle.record(n5);
                        if (bl) {
                            byteBuf.release();
                        }
                        return n2;
                    }
                    byteBuf.release();
                }
                return n3;
            }
            list.add(new SctpMessage(messageInfo, byteBuf.writerIndex(byteBuf.writerIndex() + byteBuffer.position() - n2)));
            bl = false;
            n = 1;
            int n6 = byteBuf.readableBytes();
            handle.record(n6);
            if (!bl) break block7;
            byteBuf.release();
        }
        return n;
    }

    @Override
    protected boolean doWriteMessage(Object object, ChannelOutboundBuffer channelOutboundBuffer) throws Exception {
        ByteBuffer byteBuffer;
        boolean bl;
        SctpMessage sctpMessage = (SctpMessage)object;
        ByteBuf byteBuf = sctpMessage.content();
        int n = byteBuf.readableBytes();
        if (n == 0) {
            return true;
        }
        ByteBufAllocator byteBufAllocator = this.alloc();
        boolean bl2 = bl = byteBuf.nioBufferCount() != 1;
        if (!bl && !byteBuf.isDirect() && byteBufAllocator.isDirectBufferPooled()) {
            bl = true;
        }
        if (!bl) {
            byteBuffer = byteBuf.nioBuffer();
        } else {
            byteBuf = byteBufAllocator.directBuffer(n).writeBytes(byteBuf);
            byteBuffer = byteBuf.nioBuffer();
        }
        MessageInfo messageInfo = MessageInfo.createOutgoing(this.association(), null, sctpMessage.streamIdentifier());
        messageInfo.payloadProtocolID(sctpMessage.protocolIdentifier());
        messageInfo.streamNumber(sctpMessage.streamIdentifier());
        int n2 = ((com.sun.nio.sctp.SctpChannel)this.javaChannel()).send(byteBuffer, messageInfo);
        return n2 > 0;
    }

    private final class NioSctpChannelConfig
    extends DefaultSctpChannelConfig {
        @Override
        protected void autoReadCleared() {
            NioSctpChannel.this.setReadPending(false);
        }

        private NioSctpChannelConfig(NioSctpChannel nioSctpChannel2, com.sun.nio.sctp.SctpChannel sctpChannel) {
            super(nioSctpChannel2, sctpChannel);
        }
    }
}

