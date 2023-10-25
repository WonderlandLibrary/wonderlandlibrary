/*
 * Decompiled with CFR 0.152.
 */
package io.netty.channel.epoll;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelOutboundBuffer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.ChannelPromise;
import io.netty.channel.ConnectTimeoutException;
import io.netty.channel.DefaultChannelConfig;
import io.netty.channel.DefaultFileRegion;
import io.netty.channel.EventLoop;
import io.netty.channel.RecvByteBufAllocator;
import io.netty.channel.epoll.AbstractEpollChannel;
import io.netty.channel.epoll.EpollSocketChannelConfig;
import io.netty.channel.epoll.IovArray;
import io.netty.channel.epoll.Native;
import io.netty.channel.socket.ChannelInputShutdownEvent;
import io.netty.channel.socket.ServerSocketChannel;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.SocketChannelConfig;
import io.netty.util.internal.PlatformDependent;
import io.netty.util.internal.StringUtil;
import java.io.IOException;
import java.net.ConnectException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public final class EpollSocketChannel
extends AbstractEpollChannel
implements SocketChannel {
    private final EpollSocketChannelConfig config = new EpollSocketChannelConfig(this);
    private SocketAddress requestedRemoteAddress;
    private volatile InetSocketAddress remote;
    private volatile boolean outputShutdown;
    private ScheduledFuture<?> connectTimeoutFuture;
    private static final String EXPECTED_TYPES = " (expected: " + StringUtil.simpleClassName(ByteBuf.class) + ", " + StringUtil.simpleClassName(DefaultFileRegion.class) + ')';
    private volatile InetSocketAddress local;
    private volatile boolean inputShutdown;
    private ChannelPromise connectPromise;

    private boolean doWriteSingle(ChannelOutboundBuffer channelOutboundBuffer) throws Exception {
        Object object = channelOutboundBuffer.current();
        if (object instanceof ByteBuf) {
            ByteBuf byteBuf = (ByteBuf)object;
            if (!this.writeBytes(channelOutboundBuffer, byteBuf)) {
                return false;
            }
        } else if (object instanceof DefaultFileRegion) {
            DefaultFileRegion defaultFileRegion = (DefaultFileRegion)object;
            if (!this.writeFileRegion(channelOutboundBuffer, defaultFileRegion)) {
                return false;
            }
        } else {
            throw new Error();
        }
        return true;
    }

    private boolean writeBytes(ChannelOutboundBuffer channelOutboundBuffer, ByteBuf byteBuf) throws Exception {
        int n = byteBuf.readableBytes();
        if (n == 0) {
            channelOutboundBuffer.remove();
            return true;
        }
        boolean bl = false;
        long l = 0L;
        if (byteBuf.hasMemoryAddress()) {
            block6: {
                int n2;
                long l2 = byteBuf.memoryAddress();
                int n3 = byteBuf.readerIndex();
                int n4 = byteBuf.writerIndex();
                while ((n2 = Native.writeAddress(this.fd, l2, n3, n4)) > 0) {
                    if ((l += (long)n2) == (long)n) {
                        bl = true;
                        break block6;
                    }
                    n3 += n2;
                }
                this.setEpollOut();
            }
            channelOutboundBuffer.removeBytes(l);
            return bl;
        }
        if (byteBuf.nioBufferCount() == 1) {
            block7: {
                int n5;
                int n6;
                int n7;
                int n8 = byteBuf.readerIndex();
                ByteBuffer byteBuffer = byteBuf.internalNioBuffer(n8, byteBuf.readableBytes());
                while ((n7 = Native.write(this.fd, byteBuffer, n6 = byteBuffer.position(), n5 = byteBuffer.limit())) > 0) {
                    byteBuffer.position(n6 + n7);
                    if ((l += (long)n7) != (long)n) continue;
                    bl = true;
                    break block7;
                }
                this.setEpollOut();
            }
            channelOutboundBuffer.removeBytes(l);
            return bl;
        }
        ByteBuffer[] byteBufferArray = byteBuf.nioBuffers();
        return this.writeBytesMultiple(channelOutboundBuffer, byteBufferArray, byteBufferArray.length, n);
    }

    @Override
    public boolean isOutputShutdown() {
        return this.outputShutdown || !this.isActive();
    }

    @Override
    protected AbstractEpollChannel.AbstractEpollUnsafe newUnsafe() {
        return new EpollSocketUnsafe();
    }

    public EpollSocketChannel() {
        super(Native.socketStreamFd(), 1);
    }

    @Override
    protected void doBind(SocketAddress socketAddress) throws Exception {
        InetSocketAddress inetSocketAddress = (InetSocketAddress)socketAddress;
        Native.bind(this.fd, inetSocketAddress.getAddress(), inetSocketAddress.getPort());
        this.local = Native.localAddress(this.fd);
    }

    @Override
    public ChannelFuture shutdownOutput() {
        return this.shutdownOutput(this.newPromise());
    }

    @Override
    public boolean isInputShutdown() {
        return this.inputShutdown;
    }

    private boolean writeBytesMultiple(ChannelOutboundBuffer channelOutboundBuffer, IovArray iovArray) throws IOException {
        long l = iovArray.size();
        int n = iovArray.count();
        assert (l != 0L);
        assert (n != 0);
        boolean bl = false;
        long l2 = 0L;
        int n2 = 0;
        int n3 = n2 + n;
        block0: while (true) {
            long l3;
            long l4;
            if ((l4 = Native.writevAddresses(this.fd, iovArray.memoryAddress(n2), n)) == 0L) {
                this.setEpollOut();
                break;
            }
            l2 += l4;
            if ((l -= l4) == 0L) {
                bl = true;
                break;
            }
            do {
                if ((l3 = iovArray.processWritten(n2, l4)) == -1L) continue block0;
                --n;
            } while (++n2 < n3 && (l4 -= l3) > 0L);
        }
        channelOutboundBuffer.removeBytes(l2);
        return bl;
    }

    private boolean doWriteMultiple(ChannelOutboundBuffer channelOutboundBuffer) throws Exception {
        if (PlatformDependent.hasUnsafe()) {
            IovArray iovArray = IovArray.get(channelOutboundBuffer);
            int n = iovArray.count();
            if (n >= 1) {
                if (!this.writeBytesMultiple(channelOutboundBuffer, iovArray)) {
                    return false;
                }
            } else {
                channelOutboundBuffer.removeBytes(0L);
            }
        } else {
            ByteBuffer[] byteBufferArray = channelOutboundBuffer.nioBuffers();
            int n = channelOutboundBuffer.nioBufferCount();
            if (n >= 1) {
                if (!this.writeBytesMultiple(channelOutboundBuffer, byteBufferArray, n, channelOutboundBuffer.nioBufferSize())) {
                    return false;
                }
            } else {
                channelOutboundBuffer.removeBytes(0L);
            }
        }
        return true;
    }

    private boolean writeBytesMultiple(ChannelOutboundBuffer channelOutboundBuffer, ByteBuffer[] byteBufferArray, int n, long l) throws IOException {
        assert (l != 0L);
        boolean bl = false;
        long l2 = 0L;
        int n2 = 0;
        int n3 = n2 + n;
        block0: while (true) {
            int n4;
            long l3;
            if ((l3 = Native.writev(this.fd, byteBufferArray, n2, n)) == 0L) {
                this.setEpollOut();
                break;
            }
            l2 += l3;
            if ((l -= l3) == 0L) {
                bl = true;
                break;
            }
            do {
                ByteBuffer byteBuffer = byteBufferArray[n2];
                int n5 = byteBuffer.position();
                n4 = byteBuffer.limit() - n5;
                if ((long)n4 > l3) {
                    byteBuffer.position(n5 + (int)l3);
                    continue block0;
                }
                --n;
            } while (++n2 < n3 && (l3 -= (long)n4) > 0L);
        }
        channelOutboundBuffer.removeBytes(l2);
        return bl;
    }

    @Override
    protected Object filterOutboundMessage(Object object) {
        if (object instanceof ByteBuf) {
            ByteBuf byteBuf = (ByteBuf)object;
            if (!(byteBuf.hasMemoryAddress() || !PlatformDependent.hasUnsafe() && byteBuf.isDirect())) {
                byteBuf = this.newDirectBuffer(byteBuf);
                assert (byteBuf.hasMemoryAddress());
            }
            return byteBuf;
        }
        if (object instanceof DefaultFileRegion) {
            return object;
        }
        throw new UnsupportedOperationException("unsupported message type: " + StringUtil.simpleClassName(object) + EXPECTED_TYPES);
    }

    @Override
    protected SocketAddress remoteAddress0() {
        return this.remote;
    }

    @Override
    protected SocketAddress localAddress0() {
        return this.local;
    }

    EpollSocketChannel(Channel channel, int n) {
        super(channel, n, 1, true);
        this.remote = Native.remoteAddress(n);
        this.local = Native.localAddress(n);
    }

    private boolean writeFileRegion(ChannelOutboundBuffer channelOutboundBuffer, DefaultFileRegion defaultFileRegion) throws Exception {
        long l = defaultFileRegion.count();
        if (defaultFileRegion.transfered() >= l) {
            channelOutboundBuffer.remove();
            return true;
        }
        long l2 = defaultFileRegion.position();
        boolean bl = false;
        long l3 = 0L;
        for (int i = ((DefaultChannelConfig)((Object)this.config())).getWriteSpinCount() - 1; i >= 0; --i) {
            long l4 = defaultFileRegion.transfered();
            long l5 = Native.sendfile(this.fd, defaultFileRegion, l2, l4, l - l4);
            if (l5 == 0L) {
                this.setEpollOut();
                break;
            }
            l3 += l5;
            if (defaultFileRegion.transfered() < l) continue;
            bl = true;
            break;
        }
        if (l3 > 0L) {
            channelOutboundBuffer.progress(l3);
        }
        if (bl) {
            channelOutboundBuffer.remove();
        }
        return bl;
    }

    @Override
    public EpollSocketChannelConfig config() {
        return this.config;
    }

    @Override
    protected void doWrite(ChannelOutboundBuffer channelOutboundBuffer) throws Exception {
        int n;
        do {
            if ((n = channelOutboundBuffer.size()) != 0) continue;
            this.clearEpollOut();
            break;
        } while (!(n > 1 && channelOutboundBuffer.current() instanceof ByteBuf ? !this.doWriteMultiple(channelOutboundBuffer) : !this.doWriteSingle(channelOutboundBuffer)));
    }

    @Override
    public ChannelFuture shutdownOutput(final ChannelPromise channelPromise) {
        EventLoop eventLoop = this.eventLoop();
        if (eventLoop.inEventLoop()) {
            try {
                Native.shutdown(this.fd, false, true);
                this.outputShutdown = true;
                channelPromise.setSuccess();
            }
            catch (Throwable throwable) {
                channelPromise.setFailure(throwable);
            }
        } else {
            eventLoop.execute(new Runnable(){

                @Override
                public void run() {
                    EpollSocketChannel.this.shutdownOutput(channelPromise);
                }
            });
        }
        return channelPromise;
    }

    @Override
    public ServerSocketChannel parent() {
        return (ServerSocketChannel)super.parent();
    }

    final class EpollSocketUnsafe
    extends AbstractEpollChannel.AbstractEpollUnsafe {
        private RecvByteBufAllocator.Handle allocHandle;

        EpollSocketUnsafe() {
        }

        @Override
        public void connect(final SocketAddress socketAddress, SocketAddress socketAddress2, ChannelPromise channelPromise) {
            if (!channelPromise.setUncancellable() || !this.ensureOpen(channelPromise)) {
                return;
            }
            try {
                if (EpollSocketChannel.this.connectPromise != null) {
                    throw new IllegalStateException("connection attempt already made");
                }
                boolean bl = EpollSocketChannel.this.isActive();
                if (this.doConnect((InetSocketAddress)socketAddress, (InetSocketAddress)socketAddress2)) {
                    this.fulfillConnectPromise(channelPromise, bl);
                } else {
                    EpollSocketChannel.this.connectPromise = channelPromise;
                    EpollSocketChannel.this.requestedRemoteAddress = socketAddress;
                    int n = ((DefaultChannelConfig)((Object)EpollSocketChannel.this.config())).getConnectTimeoutMillis();
                    if (n > 0) {
                        EpollSocketChannel.this.connectTimeoutFuture = EpollSocketChannel.this.eventLoop().schedule(new Runnable(){

                            @Override
                            public void run() {
                                ChannelPromise channelPromise = EpollSocketChannel.this.connectPromise;
                                ConnectTimeoutException connectTimeoutException = new ConnectTimeoutException("connection timed out: " + socketAddress);
                                if (channelPromise != null && channelPromise.tryFailure(connectTimeoutException)) {
                                    EpollSocketUnsafe.this.close(EpollSocketUnsafe.this.voidPromise());
                                }
                            }
                        }, (long)n, TimeUnit.MILLISECONDS);
                    }
                    channelPromise.addListener(new ChannelFutureListener(){

                        @Override
                        public void operationComplete(ChannelFuture channelFuture) throws Exception {
                            if (channelFuture.isCancelled()) {
                                if (EpollSocketChannel.this.connectTimeoutFuture != null) {
                                    EpollSocketChannel.this.connectTimeoutFuture.cancel(false);
                                }
                                EpollSocketChannel.this.connectPromise = null;
                                EpollSocketUnsafe.this.close(EpollSocketUnsafe.this.voidPromise());
                            }
                        }
                    });
                }
            }
            catch (Throwable throwable) {
                ConnectException connectException;
                if (throwable instanceof ConnectException) {
                    ConnectException connectException2 = new ConnectException(throwable.getMessage() + ": " + socketAddress);
                    connectException2.setStackTrace(throwable.getStackTrace());
                    connectException = connectException2;
                }
                this.closeIfClosed();
                channelPromise.tryFailure(connectException);
            }
        }

        private boolean handleReadException(ChannelPipeline channelPipeline, ByteBuf byteBuf, Throwable throwable, boolean bl) {
            if (byteBuf != null) {
                if (byteBuf.isReadable()) {
                    this.readPending = false;
                    channelPipeline.fireChannelRead(byteBuf);
                } else {
                    byteBuf.release();
                }
            }
            channelPipeline.fireChannelReadComplete();
            channelPipeline.fireExceptionCaught(throwable);
            if (bl || throwable instanceof IOException) {
                this.closeOnRead(channelPipeline);
                return true;
            }
            return false;
        }

        private boolean doConnect(InetSocketAddress inetSocketAddress, InetSocketAddress inetSocketAddress2) throws Exception {
            if (inetSocketAddress2 != null) {
                AbstractEpollChannel.checkResolvable(inetSocketAddress2);
                Native.bind(EpollSocketChannel.this.fd, inetSocketAddress2.getAddress(), inetSocketAddress2.getPort());
            }
            boolean bl = false;
            AbstractEpollChannel.checkResolvable(inetSocketAddress);
            boolean bl2 = Native.connect(EpollSocketChannel.this.fd, inetSocketAddress.getAddress(), inetSocketAddress.getPort());
            EpollSocketChannel.this.remote = inetSocketAddress;
            EpollSocketChannel.this.local = Native.localAddress(EpollSocketChannel.this.fd);
            if (!bl2) {
                EpollSocketChannel.this.setEpollOut();
            }
            bl = true;
            boolean bl3 = bl2;
            if (!bl) {
                EpollSocketChannel.this.doClose();
            }
            return bl3;
        }

        private void finishConnect() {
            block10: {
                boolean bl;
                boolean bl2;
                block8: {
                    block9: {
                        assert (EpollSocketChannel.this.eventLoop().inEventLoop());
                        bl2 = false;
                        bl = EpollSocketChannel.this.isActive();
                        if (this.doFinishConnect()) break block8;
                        bl2 = true;
                        if (bl2) break block9;
                        if (EpollSocketChannel.this.connectTimeoutFuture != null) {
                            EpollSocketChannel.this.connectTimeoutFuture.cancel(false);
                        }
                        EpollSocketChannel.this.connectPromise = null;
                    }
                    return;
                }
                try {
                    this.fulfillConnectPromise(EpollSocketChannel.this.connectPromise, bl);
                    if (bl2) break block10;
                }
                catch (Throwable throwable) {
                    ConnectException connectException;
                    if (throwable instanceof ConnectException) {
                        ConnectException connectException2 = new ConnectException(throwable.getMessage() + ": " + EpollSocketChannel.this.requestedRemoteAddress);
                        connectException2.setStackTrace(throwable.getStackTrace());
                        connectException = connectException2;
                    }
                    this.fulfillConnectPromise(EpollSocketChannel.this.connectPromise, connectException);
                    if (bl2) break block10;
                    if (EpollSocketChannel.this.connectTimeoutFuture != null) {
                        EpollSocketChannel.this.connectTimeoutFuture.cancel(false);
                    }
                    EpollSocketChannel.this.connectPromise = null;
                }
                if (EpollSocketChannel.this.connectTimeoutFuture != null) {
                    EpollSocketChannel.this.connectTimeoutFuture.cancel(false);
                }
                EpollSocketChannel.this.connectPromise = null;
            }
        }

        @Override
        void epollRdHupReady() {
            if (EpollSocketChannel.this.isActive()) {
                this.epollInReady();
            } else {
                this.closeOnRead(EpollSocketChannel.this.pipeline());
            }
        }

        private void fulfillConnectPromise(ChannelPromise channelPromise, Throwable throwable) {
            if (channelPromise == null) {
                return;
            }
            channelPromise.tryFailure(throwable);
            this.closeIfClosed();
        }

        @Override
        void epollInReady() {
            block8: {
                SocketChannelConfig socketChannelConfig = EpollSocketChannel.this.config();
                ChannelPipeline channelPipeline = EpollSocketChannel.this.pipeline();
                ByteBufAllocator byteBufAllocator = socketChannelConfig.getAllocator();
                RecvByteBufAllocator.Handle handle = this.allocHandle;
                if (handle == null) {
                    this.allocHandle = handle = socketChannelConfig.getRecvByteBufAllocator().newHandle();
                }
                ByteBuf byteBuf = null;
                boolean bl = false;
                try {
                    int n;
                    int n2;
                    int n3 = 0;
                    do {
                        byteBuf = handle.allocate(byteBufAllocator);
                        n = byteBuf.writableBytes();
                        n2 = this.doReadBytes(byteBuf);
                        if (n2 <= 0) {
                            byteBuf.release();
                            bl = n2 < 0;
                            break;
                        }
                        this.readPending = false;
                        channelPipeline.fireChannelRead(byteBuf);
                        byteBuf = null;
                        if (n3 >= Integer.MAX_VALUE - n2) {
                            handle.record(n3);
                            n3 = n2;
                            continue;
                        }
                        n3 += n2;
                    } while (n2 >= n);
                    channelPipeline.fireChannelReadComplete();
                    handle.record(n3);
                    if (bl) {
                        this.closeOnRead(channelPipeline);
                        bl = false;
                    }
                    if (socketChannelConfig.isAutoRead() || this.readPending) break block8;
                }
                catch (Throwable throwable) {
                    boolean bl2 = this.handleReadException(channelPipeline, byteBuf, throwable, bl);
                    if (!bl2) {
                        EpollSocketChannel.this.eventLoop().execute(new Runnable(){

                            @Override
                            public void run() {
                                EpollSocketUnsafe.this.epollInReady();
                            }
                        });
                    }
                    if (socketChannelConfig.isAutoRead() || this.readPending) break block8;
                    this.clearEpollIn0();
                }
                this.clearEpollIn0();
            }
        }

        private boolean doFinishConnect() throws Exception {
            if (Native.finishConnect(EpollSocketChannel.this.fd)) {
                EpollSocketChannel.this.clearEpollOut();
                return true;
            }
            EpollSocketChannel.this.setEpollOut();
            return false;
        }

        private void fulfillConnectPromise(ChannelPromise channelPromise, boolean bl) {
            if (channelPromise == null) {
                return;
            }
            EpollSocketChannel.this.active = true;
            boolean bl2 = channelPromise.trySuccess();
            if (!bl && EpollSocketChannel.this.isActive()) {
                EpollSocketChannel.this.pipeline().fireChannelActive();
            }
            if (!bl2) {
                this.close(this.voidPromise());
            }
        }

        @Override
        void epollOutReady() {
            if (EpollSocketChannel.this.connectPromise != null) {
                this.finishConnect();
            } else {
                super.epollOutReady();
            }
        }

        private int doReadBytes(ByteBuf byteBuf) throws Exception {
            int n;
            int n2 = byteBuf.writerIndex();
            if (byteBuf.hasMemoryAddress()) {
                n = Native.readAddress(EpollSocketChannel.this.fd, byteBuf.memoryAddress(), n2, byteBuf.capacity());
            } else {
                ByteBuffer byteBuffer = byteBuf.internalNioBuffer(n2, byteBuf.writableBytes());
                n = Native.read(EpollSocketChannel.this.fd, byteBuffer, byteBuffer.position(), byteBuffer.limit());
            }
            if (n > 0) {
                byteBuf.writerIndex(n2 + n);
            }
            return n;
        }

        private void closeOnRead(ChannelPipeline channelPipeline) {
            EpollSocketChannel.this.inputShutdown = true;
            if (EpollSocketChannel.this.isOpen()) {
                if (Boolean.TRUE.equals(((EpollSocketChannelConfig)EpollSocketChannel.this.config()).getOption(ChannelOption.ALLOW_HALF_CLOSURE))) {
                    this.clearEpollIn0();
                    channelPipeline.fireUserEventTriggered(ChannelInputShutdownEvent.INSTANCE);
                } else {
                    this.close(this.voidPromise());
                }
            }
        }
    }
}

