/*
 * Decompiled with CFR 0.152.
 */
package io.netty.channel.sctp.oio;

import com.sun.nio.sctp.Association;
import com.sun.nio.sctp.MessageInfo;
import com.sun.nio.sctp.NotificationHandler;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelException;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelMetadata;
import io.netty.channel.ChannelOutboundBuffer;
import io.netty.channel.ChannelPromise;
import io.netty.channel.RecvByteBufAllocator;
import io.netty.channel.oio.AbstractOioMessageChannel;
import io.netty.channel.sctp.DefaultSctpChannelConfig;
import io.netty.channel.sctp.SctpChannel;
import io.netty.channel.sctp.SctpChannelConfig;
import io.netty.channel.sctp.SctpMessage;
import io.netty.channel.sctp.SctpNotificationHandler;
import io.netty.channel.sctp.SctpServerChannel;
import io.netty.util.internal.PlatformDependent;
import io.netty.util.internal.StringUtil;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;
import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class OioSctpChannel
extends AbstractOioMessageChannel
implements SctpChannel {
    private final Selector readSelector;
    private final Selector writeSelector;
    private final SctpChannelConfig config;
    private final Selector connectSelector;
    private static final ChannelMetadata METADATA;
    private static final String EXPECTED_TYPE;
    private static final InternalLogger logger;
    private RecvByteBufAllocator.Handle allocHandle;
    private final com.sun.nio.sctp.SctpChannel ch;
    private final NotificationHandler<?> notificationHandler;

    @Override
    protected void doConnect(SocketAddress socketAddress, SocketAddress socketAddress2) throws Exception {
        block3: {
            if (socketAddress2 != null) {
                this.ch.bind(socketAddress2);
            }
            boolean bl = false;
            this.ch.connect(socketAddress);
            boolean bl2 = false;
            while (!bl2) {
                if (this.connectSelector.select(1000L) < 0) continue;
                Set<SelectionKey> set = this.connectSelector.selectedKeys();
                for (SelectionKey selectionKey : set) {
                    if (!selectionKey.isConnectable()) continue;
                    set.clear();
                    bl2 = true;
                    break;
                }
                set.clear();
            }
            bl = this.ch.finishConnect();
            if (bl) break block3;
            this.doClose();
        }
    }

    @Override
    public InetSocketAddress localAddress() {
        return (InetSocketAddress)super.localAddress();
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public OioSctpChannel(Channel channel, com.sun.nio.sctp.SctpChannel sctpChannel) {
        super(channel);
        this.ch = sctpChannel;
        boolean bl = false;
        try {
            sctpChannel.configureBlocking(false);
            this.readSelector = Selector.open();
            this.writeSelector = Selector.open();
            this.connectSelector = Selector.open();
            sctpChannel.register(this.readSelector, 1);
            sctpChannel.register(this.writeSelector, 4);
            sctpChannel.register(this.connectSelector, 8);
            this.config = new OioSctpChannelConfig(this, sctpChannel);
            this.notificationHandler = new SctpNotificationHandler(this);
            return;
        }
        catch (Exception exception) {
            throw new ChannelException("failed to initialize a sctp channel", exception);
        }
    }

    @Override
    protected SocketAddress remoteAddress0() {
        try {
            Iterator<SocketAddress> iterator = this.ch.getRemoteAddresses().iterator();
            if (iterator.hasNext()) {
                return iterator.next();
            }
        }
        catch (IOException iOException) {
            // empty catch block
        }
        return null;
    }

    public OioSctpChannel() {
        this(OioSctpChannel.openChannel());
    }

    @Override
    public InetSocketAddress remoteAddress() {
        return (InetSocketAddress)super.remoteAddress();
    }

    @Override
    public Association association() {
        try {
            return this.ch.association();
        }
        catch (IOException iOException) {
            return null;
        }
    }

    @Override
    public SctpServerChannel parent() {
        return (SctpServerChannel)super.parent();
    }

    private static com.sun.nio.sctp.SctpChannel openChannel() {
        try {
            return com.sun.nio.sctp.SctpChannel.open();
        }
        catch (IOException iOException) {
            throw new ChannelException("Failed to open a sctp channel.", iOException);
        }
    }

    public OioSctpChannel(com.sun.nio.sctp.SctpChannel sctpChannel) {
        this(null, sctpChannel);
    }

    @Override
    public ChannelFuture unbindAddress(InetAddress inetAddress) {
        return this.unbindAddress(inetAddress, this.newPromise());
    }

    @Override
    protected void doDisconnect() throws Exception {
        this.doClose();
    }

    @Override
    public Set<InetSocketAddress> allLocalAddresses() {
        try {
            Set<SocketAddress> set = this.ch.getAllLocalAddresses();
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
    public ChannelFuture unbindAddress(final InetAddress inetAddress, final ChannelPromise channelPromise) {
        if (this.eventLoop().inEventLoop()) {
            try {
                this.ch.unbindAddress(inetAddress);
                channelPromise.setSuccess();
            }
            catch (Throwable throwable) {
                channelPromise.setFailure(throwable);
            }
        } else {
            this.eventLoop().execute(new Runnable(){

                @Override
                public void run() {
                    OioSctpChannel.this.unbindAddress(inetAddress, channelPromise);
                }
            });
        }
        return channelPromise;
    }

    @Override
    public SctpChannelConfig config() {
        return this.config;
    }

    @Override
    public ChannelFuture bindAddress(InetAddress inetAddress) {
        return this.bindAddress(inetAddress, this.newPromise());
    }

    @Override
    public Set<InetSocketAddress> allRemoteAddresses() {
        try {
            Set<SocketAddress> set = this.ch.getRemoteAddresses();
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
    public ChannelMetadata metadata() {
        return METADATA;
    }

    @Override
    protected int doReadMessages(List<Object> list) throws Exception {
        boolean bl;
        if (!this.readSelector.isOpen()) {
            return 0;
        }
        int n = 0;
        int n2 = this.readSelector.select(1000L);
        boolean bl2 = bl = n2 > 0;
        if (!bl) {
            return n;
        }
        Set<SelectionKey> set = this.readSelector.selectedKeys();
        for (SelectionKey selectionKey : set) {
            int n3;
            MessageInfo messageInfo;
            ByteBuffer byteBuffer;
            boolean bl3;
            ByteBuf byteBuf;
            RecvByteBufAllocator.Handle handle;
            block7: {
                int n4;
                block8: {
                    handle = this.allocHandle;
                    if (handle == null) {
                        this.allocHandle = handle = this.config().getRecvByteBufAllocator().newHandle();
                    }
                    byteBuf = handle.allocate(this.config().getAllocator());
                    bl3 = true;
                    try {
                        byteBuffer = byteBuf.nioBuffer(byteBuf.writerIndex(), byteBuf.writableBytes());
                        messageInfo = this.ch.receive(byteBuffer, null, this.notificationHandler);
                        if (messageInfo != null) break block7;
                        n4 = n;
                        int n5 = byteBuf.readableBytes();
                        handle.record(n5);
                        if (!bl3) break block8;
                    }
                    catch (Throwable throwable) {
                        PlatformDependent.throwException(throwable);
                        n3 = byteBuf.readableBytes();
                        handle.record(n3);
                        if (!bl3) continue;
                        byteBuf.release();
                        continue;
                    }
                    byteBuf.release();
                }
                set.clear();
                return n4;
            }
            byteBuffer.flip();
            list.add(new SctpMessage(messageInfo, byteBuf.writerIndex(byteBuf.writerIndex() + byteBuffer.remaining())));
            bl3 = false;
            ++n;
            n3 = byteBuf.readableBytes();
            handle.record(n3);
            if (!bl3) continue;
            byteBuf.release();
        }
        set.clear();
        return n;
    }

    static {
        logger = InternalLoggerFactory.getInstance(OioSctpChannel.class);
        METADATA = new ChannelMetadata(false);
        EXPECTED_TYPE = " (expected: " + StringUtil.simpleClassName(SctpMessage.class) + ')';
    }

    @Override
    public boolean isActive() {
        return this.isOpen() && this.association() != null;
    }

    @Override
    protected SocketAddress localAddress0() {
        try {
            Iterator<SocketAddress> iterator = this.ch.getAllLocalAddresses().iterator();
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
    protected void doBind(SocketAddress socketAddress) throws Exception {
        this.ch.bind(socketAddress);
    }

    @Override
    protected void doWrite(ChannelOutboundBuffer channelOutboundBuffer) throws Exception {
        if (!this.writeSelector.isOpen()) {
            return;
        }
        int n = channelOutboundBuffer.size();
        int n2 = this.writeSelector.select(1000L);
        if (n2 > 0) {
            Set<SelectionKey> set = this.writeSelector.selectedKeys();
            if (set.isEmpty()) {
                return;
            }
            Iterator<SelectionKey> iterator = set.iterator();
            int n3 = 0;
            do {
                ByteBuffer byteBuffer;
                if (n3 == n) {
                    return;
                }
                iterator.next();
                iterator.remove();
                SctpMessage sctpMessage = (SctpMessage)channelOutboundBuffer.current();
                if (sctpMessage == null) {
                    return;
                }
                ByteBuf byteBuf = sctpMessage.content();
                int n4 = byteBuf.readableBytes();
                if (byteBuf.nioBufferCount() != -1) {
                    byteBuffer = byteBuf.nioBuffer();
                } else {
                    byteBuffer = ByteBuffer.allocate(n4);
                    byteBuf.getBytes(byteBuf.readerIndex(), byteBuffer);
                    byteBuffer.flip();
                }
                MessageInfo messageInfo = MessageInfo.createOutgoing(this.association(), null, sctpMessage.streamIdentifier());
                messageInfo.payloadProtocolID(sctpMessage.protocolIdentifier());
                messageInfo.streamNumber(sctpMessage.streamIdentifier());
                this.ch.send(byteBuffer, messageInfo);
                ++n3;
                channelOutboundBuffer.remove();
            } while (iterator.hasNext());
            return;
        }
    }

    @Override
    protected Object filterOutboundMessage(Object object) throws Exception {
        if (object instanceof SctpMessage) {
            return object;
        }
        throw new UnsupportedOperationException("unsupported message type: " + StringUtil.simpleClassName(object) + EXPECTED_TYPE);
    }

    private static void closeSelector(String string, Selector selector) {
        try {
            selector.close();
        }
        catch (IOException iOException) {
            logger.warn("Failed to close a " + string + " selector.", iOException);
        }
    }

    @Override
    public boolean isOpen() {
        return this.ch.isOpen();
    }

    @Override
    protected void doClose() throws Exception {
        OioSctpChannel.closeSelector("read", this.readSelector);
        OioSctpChannel.closeSelector("write", this.writeSelector);
        OioSctpChannel.closeSelector("connect", this.connectSelector);
        this.ch.close();
    }

    @Override
    public ChannelFuture bindAddress(final InetAddress inetAddress, final ChannelPromise channelPromise) {
        if (this.eventLoop().inEventLoop()) {
            try {
                this.ch.bindAddress(inetAddress);
                channelPromise.setSuccess();
            }
            catch (Throwable throwable) {
                channelPromise.setFailure(throwable);
            }
        } else {
            this.eventLoop().execute(new Runnable(){

                @Override
                public void run() {
                    OioSctpChannel.this.bindAddress(inetAddress, channelPromise);
                }
            });
        }
        return channelPromise;
    }

    private final class OioSctpChannelConfig
    extends DefaultSctpChannelConfig {
        private OioSctpChannelConfig(OioSctpChannel oioSctpChannel2, com.sun.nio.sctp.SctpChannel sctpChannel) {
            super(oioSctpChannel2, sctpChannel);
        }

        @Override
        protected void autoReadCleared() {
            OioSctpChannel.this.setReadPending(false);
        }
    }
}

