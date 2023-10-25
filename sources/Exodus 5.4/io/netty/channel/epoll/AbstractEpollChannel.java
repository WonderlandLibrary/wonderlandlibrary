/*
 * Decompiled with CFR 0.152.
 */
package io.netty.channel.epoll;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.buffer.ByteBufUtil;
import io.netty.buffer.Unpooled;
import io.netty.channel.AbstractChannel;
import io.netty.channel.Channel;
import io.netty.channel.ChannelMetadata;
import io.netty.channel.EventLoop;
import io.netty.channel.epoll.EpollEventLoop;
import io.netty.channel.epoll.Native;
import io.netty.util.ReferenceCountUtil;
import io.netty.util.internal.OneTimeTask;
import java.net.InetSocketAddress;
import java.nio.channels.UnresolvedAddressException;

abstract class AbstractEpollChannel
extends AbstractChannel {
    private static final ChannelMetadata DATA = new ChannelMetadata(false);
    private final int readFlag;
    protected int flags;
    volatile int fd;
    int id;
    protected volatile boolean active;

    AbstractEpollChannel(Channel channel, int n, int n2, boolean bl) {
        super(channel);
        this.fd = n;
        this.readFlag = n2;
        this.flags |= n2;
        this.active = bl;
    }

    protected final ByteBuf newDirectBuffer(ByteBuf byteBuf) {
        return this.newDirectBuffer(byteBuf, byteBuf);
    }

    @Override
    protected void doDisconnect() throws Exception {
        this.doClose();
    }

    @Override
    public InetSocketAddress localAddress() {
        return (InetSocketAddress)super.localAddress();
    }

    protected static void checkResolvable(InetSocketAddress inetSocketAddress) {
        if (inetSocketAddress.isUnresolved()) {
            throw new UnresolvedAddressException();
        }
    }

    AbstractEpollChannel(int n, int n2) {
        this(null, n, n2, false);
    }

    private void modifyEvents() {
        if (this.isOpen()) {
            ((EpollEventLoop)this.eventLoop()).modify(this);
        }
    }

    @Override
    protected boolean isCompatible(EventLoop eventLoop) {
        return eventLoop instanceof EpollEventLoop;
    }

    protected final void setEpollOut() {
        if ((this.flags & 2) == 0) {
            this.flags |= 2;
            this.modifyEvents();
        }
    }

    @Override
    protected void doRegister() throws Exception {
        EpollEventLoop epollEventLoop = (EpollEventLoop)this.eventLoop();
        epollEventLoop.add(this);
    }

    @Override
    protected abstract AbstractEpollUnsafe newUnsafe();

    @Override
    public boolean isOpen() {
        return this.fd != -1;
    }

    @Override
    public ChannelMetadata metadata() {
        return DATA;
    }

    @Override
    public boolean isActive() {
        return this.active;
    }

    private static ByteBuf newDirectBuffer0(Object object, ByteBuf byteBuf, ByteBufAllocator byteBufAllocator, int n) {
        ByteBuf byteBuf2 = byteBufAllocator.directBuffer(n);
        byteBuf2.writeBytes(byteBuf, byteBuf.readerIndex(), n);
        ReferenceCountUtil.safeRelease(object);
        return byteBuf2;
    }

    protected final void clearEpollOut() {
        if ((this.flags & 2) != 0) {
            this.flags &= 0xFFFFFFFD;
            this.modifyEvents();
        }
    }

    final void clearEpollIn() {
        if (this.isRegistered()) {
            EventLoop eventLoop = this.eventLoop();
            final AbstractEpollUnsafe abstractEpollUnsafe = (AbstractEpollUnsafe)this.unsafe();
            if (eventLoop.inEventLoop()) {
                abstractEpollUnsafe.clearEpollIn0();
            } else {
                eventLoop.execute(new OneTimeTask(){

                    @Override
                    public void run() {
                        if (!AbstractEpollChannel.this.config().isAutoRead() && !abstractEpollUnsafe.readPending) {
                            abstractEpollUnsafe.clearEpollIn0();
                        }
                    }
                });
            }
        } else {
            this.flags &= ~this.readFlag;
        }
    }

    protected final ByteBuf newDirectBuffer(Object object, ByteBuf byteBuf) {
        int n = byteBuf.readableBytes();
        if (n == 0) {
            ReferenceCountUtil.safeRelease(object);
            return Unpooled.EMPTY_BUFFER;
        }
        ByteBufAllocator byteBufAllocator = this.alloc();
        if (byteBufAllocator.isDirectBufferPooled()) {
            return AbstractEpollChannel.newDirectBuffer0(object, byteBuf, byteBufAllocator, n);
        }
        ByteBuf byteBuf2 = ByteBufUtil.threadLocalDirectBuffer();
        if (byteBuf2 == null) {
            return AbstractEpollChannel.newDirectBuffer0(object, byteBuf, byteBufAllocator, n);
        }
        byteBuf2.writeBytes(byteBuf, byteBuf.readerIndex(), n);
        ReferenceCountUtil.safeRelease(object);
        return byteBuf2;
    }

    @Override
    public InetSocketAddress remoteAddress() {
        return (InetSocketAddress)super.remoteAddress();
    }

    @Override
    protected void doDeregister() throws Exception {
        ((EpollEventLoop)this.eventLoop()).remove(this);
    }

    @Override
    protected void doBeginRead() throws Exception {
        ((AbstractEpollUnsafe)this.unsafe()).readPending = true;
        if ((this.flags & this.readFlag) == 0) {
            this.flags |= this.readFlag;
            this.modifyEvents();
        }
    }

    @Override
    protected void doClose() throws Exception {
        this.active = false;
        this.doDeregister();
        int n = this.fd;
        this.fd = -1;
        Native.close(n);
    }

    protected abstract class AbstractEpollUnsafe
    extends AbstractChannel.AbstractUnsafe {
        protected boolean readPending;

        void epollOutReady() {
            super.flush0();
        }

        void epollRdHupReady() {
        }

        protected AbstractEpollUnsafe() {
            super(AbstractEpollChannel.this);
        }

        abstract void epollInReady();

        private boolean isFlushPending() {
            return (AbstractEpollChannel.this.flags & 2) != 0;
        }

        protected final void clearEpollIn0() {
            if ((AbstractEpollChannel.this.flags & AbstractEpollChannel.this.readFlag) != 0) {
                AbstractEpollChannel.this.flags &= ~AbstractEpollChannel.this.readFlag;
                AbstractEpollChannel.this.modifyEvents();
            }
        }

        @Override
        protected void flush0() {
            if (this.isFlushPending()) {
                return;
            }
            super.flush0();
        }
    }
}

