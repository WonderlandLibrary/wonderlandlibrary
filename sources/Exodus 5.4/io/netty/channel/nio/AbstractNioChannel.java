/*
 * Decompiled with CFR 0.152.
 */
package io.netty.channel.nio;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.buffer.ByteBufUtil;
import io.netty.buffer.Unpooled;
import io.netty.channel.AbstractChannel;
import io.netty.channel.Channel;
import io.netty.channel.ChannelException;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelPromise;
import io.netty.channel.ConnectTimeoutException;
import io.netty.channel.EventLoop;
import io.netty.channel.nio.NioEventLoop;
import io.netty.util.ReferenceCountUtil;
import io.netty.util.ReferenceCounted;
import io.netty.util.concurrent.AbstractEventExecutor;
import io.netty.util.concurrent.SingleThreadEventExecutor;
import io.netty.util.internal.OneTimeTask;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;
import java.io.IOException;
import java.net.ConnectException;
import java.net.SocketAddress;
import java.nio.channels.CancelledKeyException;
import java.nio.channels.SelectableChannel;
import java.nio.channels.SelectionKey;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public abstract class AbstractNioChannel
extends AbstractChannel {
    private ScheduledFuture<?> connectTimeoutFuture;
    private ChannelPromise connectPromise;
    private final SelectableChannel ch;
    private volatile boolean inputShutdown;
    private SocketAddress requestedRemoteAddress;
    protected final int readInterestOp;
    private volatile boolean readPending;
    private static final InternalLogger logger = InternalLoggerFactory.getInstance(AbstractNioChannel.class);
    volatile SelectionKey selectionKey;

    protected void setReadPending(boolean bl) {
        this.readPending = bl;
    }

    @Override
    protected void doRegister() throws Exception {
        boolean bl = false;
        while (true) {
            try {
                this.selectionKey = this.javaChannel().register(((NioEventLoop)this.eventLoop()).selector, 0, this);
                return;
            }
            catch (CancelledKeyException cancelledKeyException) {
                if (!bl) {
                    ((NioEventLoop)this.eventLoop()).selectNow();
                    bl = true;
                    continue;
                }
                throw cancelledKeyException;
            }
            break;
        }
    }

    protected SelectableChannel javaChannel() {
        return this.ch;
    }

    @Override
    public NioEventLoop eventLoop() {
        return (NioEventLoop)super.eventLoop();
    }

    protected final ByteBuf newDirectBuffer(ByteBuf byteBuf) {
        int n = byteBuf.readableBytes();
        if (n == 0) {
            ReferenceCountUtil.safeRelease(byteBuf);
            return Unpooled.EMPTY_BUFFER;
        }
        ByteBufAllocator byteBufAllocator = this.alloc();
        if (byteBufAllocator.isDirectBufferPooled()) {
            ByteBuf byteBuf2 = byteBufAllocator.directBuffer(n);
            byteBuf2.writeBytes(byteBuf, byteBuf.readerIndex(), n);
            ReferenceCountUtil.safeRelease(byteBuf);
            return byteBuf2;
        }
        ByteBuf byteBuf3 = ByteBufUtil.threadLocalDirectBuffer();
        if (byteBuf3 != null) {
            byteBuf3.writeBytes(byteBuf, byteBuf.readerIndex(), n);
            ReferenceCountUtil.safeRelease(byteBuf);
            return byteBuf3;
        }
        return byteBuf;
    }

    protected AbstractNioChannel(Channel channel, SelectableChannel selectableChannel, int n) {
        super(channel);
        this.ch = selectableChannel;
        this.readInterestOp = n;
        try {
            selectableChannel.configureBlocking(false);
        }
        catch (IOException iOException) {
            block4: {
                try {
                    selectableChannel.close();
                }
                catch (IOException iOException2) {
                    if (!logger.isWarnEnabled()) break block4;
                    logger.warn("Failed to close a partially initialized socket.", iOException2);
                }
            }
            throw new ChannelException("Failed to enter non-blocking mode.", iOException);
        }
    }

    @Override
    protected void doBeginRead() throws Exception {
        if (this.inputShutdown) {
            return;
        }
        SelectionKey selectionKey = this.selectionKey;
        if (!selectionKey.isValid()) {
            return;
        }
        this.readPending = true;
        int n = selectionKey.interestOps();
        if ((n & this.readInterestOp) == 0) {
            selectionKey.interestOps(n | this.readInterestOp);
        }
    }

    void setInputShutdown() {
        this.inputShutdown = true;
    }

    protected SelectionKey selectionKey() {
        assert (this.selectionKey != null);
        return this.selectionKey;
    }

    protected boolean isReadPending() {
        return this.readPending;
    }

    protected final ByteBuf newDirectBuffer(ReferenceCounted referenceCounted, ByteBuf byteBuf) {
        int n = byteBuf.readableBytes();
        if (n == 0) {
            ReferenceCountUtil.safeRelease(referenceCounted);
            return Unpooled.EMPTY_BUFFER;
        }
        ByteBufAllocator byteBufAllocator = this.alloc();
        if (byteBufAllocator.isDirectBufferPooled()) {
            ByteBuf byteBuf2 = byteBufAllocator.directBuffer(n);
            byteBuf2.writeBytes(byteBuf, byteBuf.readerIndex(), n);
            ReferenceCountUtil.safeRelease(referenceCounted);
            return byteBuf2;
        }
        ByteBuf byteBuf3 = ByteBufUtil.threadLocalDirectBuffer();
        if (byteBuf3 != null) {
            byteBuf3.writeBytes(byteBuf, byteBuf.readerIndex(), n);
            ReferenceCountUtil.safeRelease(referenceCounted);
            return byteBuf3;
        }
        if (referenceCounted != byteBuf) {
            byteBuf.retain();
            ReferenceCountUtil.safeRelease(referenceCounted);
        }
        return byteBuf;
    }

    protected abstract boolean doConnect(SocketAddress var1, SocketAddress var2) throws Exception;

    @Override
    public NioUnsafe unsafe() {
        return (NioUnsafe)super.unsafe();
    }

    protected boolean isInputShutdown() {
        return this.inputShutdown;
    }

    protected abstract void doFinishConnect() throws Exception;

    @Override
    protected void doDeregister() throws Exception {
        ((NioEventLoop)this.eventLoop()).cancel(this.selectionKey());
    }

    @Override
    protected boolean isCompatible(EventLoop eventLoop) {
        return eventLoop instanceof NioEventLoop;
    }

    @Override
    public boolean isOpen() {
        return this.ch.isOpen();
    }

    protected abstract class AbstractNioUnsafe
    extends AbstractChannel.AbstractUnsafe
    implements NioUnsafe {
        @Override
        public final SelectableChannel ch() {
            return AbstractNioChannel.this.javaChannel();
        }

        protected final void removeReadOp() {
            SelectionKey selectionKey = AbstractNioChannel.this.selectionKey();
            if (!selectionKey.isValid()) {
                return;
            }
            int n = selectionKey.interestOps();
            if ((n & AbstractNioChannel.this.readInterestOp) != 0) {
                selectionKey.interestOps(n & ~AbstractNioChannel.this.readInterestOp);
            }
        }

        private void fulfillConnectPromise(ChannelPromise channelPromise, boolean bl) {
            if (channelPromise == null) {
                return;
            }
            boolean bl2 = channelPromise.trySuccess();
            if (!bl && AbstractNioChannel.this.isActive()) {
                AbstractNioChannel.this.pipeline().fireChannelActive();
            }
            if (!bl2) {
                this.close(this.voidPromise());
            }
        }

        @Override
        public final void forceFlush() {
            super.flush0();
        }

        @Override
        public final void connect(final SocketAddress socketAddress, SocketAddress socketAddress2, ChannelPromise channelPromise) {
            if (!channelPromise.setUncancellable() || !this.ensureOpen(channelPromise)) {
                return;
            }
            try {
                if (AbstractNioChannel.this.connectPromise != null) {
                    throw new IllegalStateException("connection attempt already made");
                }
                boolean bl = AbstractNioChannel.this.isActive();
                if (AbstractNioChannel.this.doConnect(socketAddress, socketAddress2)) {
                    this.fulfillConnectPromise(channelPromise, bl);
                } else {
                    AbstractNioChannel.this.connectPromise = channelPromise;
                    AbstractNioChannel.this.requestedRemoteAddress = socketAddress;
                    int n = AbstractNioChannel.this.config().getConnectTimeoutMillis();
                    if (n > 0) {
                        AbstractNioChannel.this.connectTimeoutFuture = ((SingleThreadEventExecutor)((Object)AbstractNioChannel.this.eventLoop())).schedule(new OneTimeTask(){

                            @Override
                            public void run() {
                                ChannelPromise channelPromise = AbstractNioChannel.this.connectPromise;
                                ConnectTimeoutException connectTimeoutException = new ConnectTimeoutException("connection timed out: " + socketAddress);
                                if (channelPromise != null && channelPromise.tryFailure(connectTimeoutException)) {
                                    AbstractNioUnsafe.this.close(AbstractNioUnsafe.this.voidPromise());
                                }
                            }
                        }, (long)n, TimeUnit.MILLISECONDS);
                    }
                    channelPromise.addListener(new ChannelFutureListener(){

                        @Override
                        public void operationComplete(ChannelFuture channelFuture) throws Exception {
                            if (channelFuture.isCancelled()) {
                                if (AbstractNioChannel.this.connectTimeoutFuture != null) {
                                    AbstractNioChannel.this.connectTimeoutFuture.cancel(false);
                                }
                                AbstractNioChannel.this.connectPromise = null;
                                AbstractNioUnsafe.this.close(AbstractNioUnsafe.this.voidPromise());
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
                channelPromise.tryFailure(connectException);
                this.closeIfClosed();
            }
        }

        private boolean isFlushPending() {
            SelectionKey selectionKey = AbstractNioChannel.this.selectionKey();
            return selectionKey.isValid() && (selectionKey.interestOps() & 4) != 0;
        }

        @Override
        public final void finishConnect() {
            assert (((AbstractEventExecutor)((Object)AbstractNioChannel.this.eventLoop())).inEventLoop());
            try {
                boolean bl = AbstractNioChannel.this.isActive();
                AbstractNioChannel.this.doFinishConnect();
                this.fulfillConnectPromise(AbstractNioChannel.this.connectPromise, bl);
            }
            catch (Throwable throwable) {
                ConnectException connectException;
                if (throwable instanceof ConnectException) {
                    ConnectException connectException2 = new ConnectException(throwable.getMessage() + ": " + AbstractNioChannel.this.requestedRemoteAddress);
                    connectException2.setStackTrace(throwable.getStackTrace());
                    connectException = connectException2;
                }
                this.fulfillConnectPromise(AbstractNioChannel.this.connectPromise, connectException);
                if (AbstractNioChannel.this.connectTimeoutFuture != null) {
                    AbstractNioChannel.this.connectTimeoutFuture.cancel(false);
                }
                AbstractNioChannel.this.connectPromise = null;
            }
            if (AbstractNioChannel.this.connectTimeoutFuture != null) {
                AbstractNioChannel.this.connectTimeoutFuture.cancel(false);
            }
            AbstractNioChannel.this.connectPromise = null;
        }

        @Override
        protected final void flush0() {
            if (this.isFlushPending()) {
                return;
            }
            super.flush0();
        }

        protected AbstractNioUnsafe() {
            super(AbstractNioChannel.this);
        }

        private void fulfillConnectPromise(ChannelPromise channelPromise, Throwable throwable) {
            if (channelPromise == null) {
                return;
            }
            channelPromise.tryFailure(throwable);
            this.closeIfClosed();
        }
    }

    public static interface NioUnsafe
    extends Channel.Unsafe {
        public void forceFlush();

        public void read();

        public void finishConnect();

        public SelectableChannel ch();
    }
}

