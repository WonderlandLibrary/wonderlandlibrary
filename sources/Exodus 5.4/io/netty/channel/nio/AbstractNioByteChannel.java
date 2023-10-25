/*
 * Decompiled with CFR 0.152.
 */
package io.netty.channel.nio;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.Channel;
import io.netty.channel.ChannelConfig;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelOutboundBuffer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.FileRegion;
import io.netty.channel.RecvByteBufAllocator;
import io.netty.channel.nio.AbstractNioChannel;
import io.netty.channel.socket.ChannelInputShutdownEvent;
import io.netty.util.ReferenceCounted;
import io.netty.util.concurrent.SingleThreadEventExecutor;
import io.netty.util.internal.StringUtil;
import java.io.IOException;
import java.nio.channels.SelectableChannel;
import java.nio.channels.SelectionKey;

public abstract class AbstractNioByteChannel
extends AbstractNioChannel {
    private static final String EXPECTED_TYPES = " (expected: " + StringUtil.simpleClassName(ByteBuf.class) + ", " + StringUtil.simpleClassName(FileRegion.class) + ')';
    private Runnable flushTask;

    @Override
    protected AbstractNioChannel.AbstractNioUnsafe newUnsafe() {
        return new NioByteUnsafe();
    }

    protected final void setOpWrite() {
        SelectionKey selectionKey = this.selectionKey();
        if (!selectionKey.isValid()) {
            return;
        }
        int n = selectionKey.interestOps();
        if ((n & 4) == 0) {
            selectionKey.interestOps(n | 4);
        }
    }

    @Override
    protected final Object filterOutboundMessage(Object object) {
        if (object instanceof ByteBuf) {
            ByteBuf byteBuf = (ByteBuf)object;
            if (byteBuf.isDirect()) {
                return object;
            }
            return this.newDirectBuffer(byteBuf);
        }
        if (object instanceof FileRegion) {
            return object;
        }
        throw new UnsupportedOperationException("unsupported message type: " + StringUtil.simpleClassName(object) + EXPECTED_TYPES);
    }

    protected abstract int doReadBytes(ByteBuf var1) throws Exception;

    @Override
    protected void doWrite(ChannelOutboundBuffer channelOutboundBuffer) throws Exception {
        block11: {
            block12: {
                int n;
                int n2 = -1;
                while (true) {
                    boolean bl;
                    ReferenceCounted referenceCounted;
                    Object object;
                    if ((object = channelOutboundBuffer.current()) == null) {
                        this.clearOpWrite();
                        break block11;
                    }
                    if (object instanceof ByteBuf) {
                        referenceCounted = (ByteBuf)object;
                        n = ((ByteBuf)referenceCounted).readableBytes();
                        if (!n) {
                            channelOutboundBuffer.remove();
                            continue;
                        }
                        bl = false;
                        boolean bl2 = false;
                        long l = 0L;
                        if (n2 == -1) {
                            n2 = this.config().getWriteSpinCount();
                        }
                        for (int i = n2 - 1; i >= 0; --i) {
                            int n3 = this.doWriteBytes((ByteBuf)referenceCounted);
                            if (n3 == 0) {
                                bl = true;
                                break;
                            }
                            l += (long)n3;
                            if (((ByteBuf)referenceCounted).isReadable()) continue;
                            bl2 = true;
                            break;
                        }
                        channelOutboundBuffer.progress(l);
                        if (bl2) {
                            channelOutboundBuffer.remove();
                            continue;
                        }
                        this.incompleteWrite(bl);
                        break block11;
                    }
                    if (!(object instanceof FileRegion)) break block12;
                    referenceCounted = (FileRegion)object;
                    n = 0;
                    bl = false;
                    long l = 0L;
                    if (n2 == -1) {
                        n2 = this.config().getWriteSpinCount();
                    }
                    for (int i = n2 - 1; i >= 0; --i) {
                        long l2 = this.doWriteFileRegion((FileRegion)referenceCounted);
                        if (l2 == 0L) {
                            n = 1;
                            break;
                        }
                        l += l2;
                        if (referenceCounted.transfered() < referenceCounted.count()) continue;
                        bl = true;
                        break;
                    }
                    channelOutboundBuffer.progress(l);
                    if (!bl) break;
                    channelOutboundBuffer.remove();
                }
                this.incompleteWrite(n != 0);
                break block11;
            }
            throw new Error();
        }
    }

    protected abstract long doWriteFileRegion(FileRegion var1) throws Exception;

    protected final void incompleteWrite(boolean bl) {
        if (bl) {
            this.setOpWrite();
        } else {
            Runnable runnable = this.flushTask;
            if (runnable == null) {
                runnable = this.flushTask = new Runnable(){

                    @Override
                    public void run() {
                        AbstractNioByteChannel.this.flush();
                    }
                };
            }
            ((SingleThreadEventExecutor)((Object)this.eventLoop())).execute(runnable);
        }
    }

    protected abstract int doWriteBytes(ByteBuf var1) throws Exception;

    protected AbstractNioByteChannel(Channel channel, SelectableChannel selectableChannel) {
        super(channel, selectableChannel, 1);
    }

    protected final void clearOpWrite() {
        SelectionKey selectionKey = this.selectionKey();
        if (!selectionKey.isValid()) {
            return;
        }
        int n = selectionKey.interestOps();
        if ((n & 4) != 0) {
            selectionKey.interestOps(n & 0xFFFFFFFB);
        }
    }

    private final class NioByteUnsafe
    extends AbstractNioChannel.AbstractNioUnsafe {
        private RecvByteBufAllocator.Handle allocHandle;

        private NioByteUnsafe() {
        }

        private void handleReadException(ChannelPipeline channelPipeline, ByteBuf byteBuf, Throwable throwable, boolean bl) {
            if (byteBuf != null) {
                if (byteBuf.isReadable()) {
                    AbstractNioByteChannel.this.setReadPending(false);
                    channelPipeline.fireChannelRead(byteBuf);
                } else {
                    byteBuf.release();
                }
            }
            channelPipeline.fireChannelReadComplete();
            channelPipeline.fireExceptionCaught(throwable);
            if (bl || throwable instanceof IOException) {
                this.closeOnRead(channelPipeline);
            }
        }

        private void closeOnRead(ChannelPipeline channelPipeline) {
            SelectionKey selectionKey = AbstractNioByteChannel.this.selectionKey();
            AbstractNioByteChannel.this.setInputShutdown();
            if (AbstractNioByteChannel.this.isOpen()) {
                if (Boolean.TRUE.equals(AbstractNioByteChannel.this.config().getOption(ChannelOption.ALLOW_HALF_CLOSURE))) {
                    selectionKey.interestOps(selectionKey.interestOps() & ~AbstractNioByteChannel.this.readInterestOp);
                    channelPipeline.fireUserEventTriggered(ChannelInputShutdownEvent.INSTANCE);
                } else {
                    this.close(this.voidPromise());
                }
            }
        }

        @Override
        public void read() {
            block9: {
                ChannelConfig channelConfig;
                block8: {
                    channelConfig = AbstractNioByteChannel.this.config();
                    if (!channelConfig.isAutoRead() && !AbstractNioByteChannel.this.isReadPending()) {
                        this.removeReadOp();
                        return;
                    }
                    ChannelPipeline channelPipeline = AbstractNioByteChannel.this.pipeline();
                    ByteBufAllocator byteBufAllocator = channelConfig.getAllocator();
                    int n = channelConfig.getMaxMessagesPerRead();
                    RecvByteBufAllocator.Handle handle = this.allocHandle;
                    if (handle == null) {
                        this.allocHandle = handle = channelConfig.getRecvByteBufAllocator().newHandle();
                    }
                    ByteBuf byteBuf = null;
                    int n2 = 0;
                    boolean bl = false;
                    try {
                        int n3;
                        int n4;
                        int n5 = 0;
                        boolean bl2 = false;
                        do {
                            byteBuf = handle.allocate(byteBufAllocator);
                            n3 = byteBuf.writableBytes();
                            n4 = AbstractNioByteChannel.this.doReadBytes(byteBuf);
                            if (n4 <= 0) {
                                byteBuf.release();
                                bl = n4 < 0;
                                break;
                            }
                            if (!bl2) {
                                bl2 = true;
                                AbstractNioByteChannel.this.setReadPending(false);
                            }
                            channelPipeline.fireChannelRead(byteBuf);
                            byteBuf = null;
                            if (n5 >= Integer.MAX_VALUE - n4) {
                                n5 = Integer.MAX_VALUE;
                                break;
                            }
                            n5 += n4;
                        } while (channelConfig.isAutoRead() && n4 >= n3 && ++n2 < n);
                        channelPipeline.fireChannelReadComplete();
                        handle.record(n5);
                        if (!bl) break block8;
                        this.closeOnRead(channelPipeline);
                        bl = false;
                    }
                    catch (Throwable throwable) {
                        this.handleReadException(channelPipeline, byteBuf, throwable, bl);
                        if (channelConfig.isAutoRead() || AbstractNioByteChannel.this.isReadPending()) break block9;
                        this.removeReadOp();
                    }
                }
                if (channelConfig.isAutoRead() || AbstractNioByteChannel.this.isReadPending()) break block9;
                this.removeReadOp();
            }
        }
    }
}

