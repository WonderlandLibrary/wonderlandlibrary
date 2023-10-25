/*
 * Decompiled with CFR 0.152.
 */
package io.netty.channel.oio;

import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelConfig;
import io.netty.channel.ChannelMetadata;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelOutboundBuffer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.FileRegion;
import io.netty.channel.RecvByteBufAllocator;
import io.netty.channel.oio.AbstractOioChannel;
import io.netty.channel.socket.ChannelInputShutdownEvent;
import io.netty.util.ReferenceCounted;
import io.netty.util.internal.StringUtil;
import java.io.IOException;

public abstract class AbstractOioByteChannel
extends AbstractOioChannel {
    private static final String EXPECTED_TYPES;
    private static final ChannelMetadata METADATA;
    private RecvByteBufAllocator.Handle allocHandle;
    private volatile boolean inputShutdown;

    @Override
    protected final Object filterOutboundMessage(Object object) throws Exception {
        if (object instanceof ByteBuf || object instanceof FileRegion) {
            return object;
        }
        throw new UnsupportedOperationException("unsupported message type: " + StringUtil.simpleClassName(object) + EXPECTED_TYPES);
    }

    @Override
    protected void doWrite(ChannelOutboundBuffer channelOutboundBuffer) throws Exception {
        Object object;
        while ((object = channelOutboundBuffer.current()) != null) {
            ReferenceCounted referenceCounted;
            if (object instanceof ByteBuf) {
                referenceCounted = (ByteBuf)object;
                int n = ((ByteBuf)referenceCounted).readableBytes();
                while (n > 0) {
                    this.doWriteBytes((ByteBuf)referenceCounted);
                    int n2 = ((ByteBuf)referenceCounted).readableBytes();
                    channelOutboundBuffer.progress(n - n2);
                    n = n2;
                }
                channelOutboundBuffer.remove();
                continue;
            }
            if (object instanceof FileRegion) {
                referenceCounted = (FileRegion)object;
                long l = referenceCounted.transfered();
                this.doWriteFileRegion((FileRegion)referenceCounted);
                channelOutboundBuffer.progress(referenceCounted.transfered() - l);
                channelOutboundBuffer.remove();
                continue;
            }
            channelOutboundBuffer.remove(new UnsupportedOperationException("unsupported message type: " + StringUtil.simpleClassName(object)));
        }
    }

    protected abstract int doReadBytes(ByteBuf var1) throws Exception;

    protected abstract int available();

    static {
        METADATA = new ChannelMetadata(false);
        EXPECTED_TYPES = " (expected: " + StringUtil.simpleClassName(ByteBuf.class) + ", " + StringUtil.simpleClassName(FileRegion.class) + ')';
    }

    protected abstract void doWriteFileRegion(FileRegion var1) throws Exception;

    protected AbstractOioByteChannel(Channel channel) {
        super(channel);
    }

    protected boolean checkInputShutdown() {
        if (this.inputShutdown) {
            try {
                Thread.sleep(1000L);
            }
            catch (InterruptedException interruptedException) {
                // empty catch block
            }
            return true;
        }
        return false;
    }

    protected abstract void doWriteBytes(ByteBuf var1) throws Exception;

    @Override
    public ChannelMetadata metadata() {
        return METADATA;
    }

    protected boolean isInputShutdown() {
        return this.inputShutdown;
    }

    @Override
    protected void doRead() {
        block32: {
            int n;
            Throwable throwable;
            boolean bl;
            ChannelPipeline channelPipeline;
            block33: {
                ByteBuf byteBuf;
                block31: {
                    if (this.checkInputShutdown()) {
                        return;
                    }
                    ChannelConfig channelConfig = this.config();
                    channelPipeline = this.pipeline();
                    RecvByteBufAllocator.Handle handle = this.allocHandle;
                    if (handle == null) {
                        this.allocHandle = handle = channelConfig.getRecvByteBufAllocator().newHandle();
                    }
                    byteBuf = handle.allocate(this.alloc());
                    bl = false;
                    boolean bl2 = false;
                    throwable = null;
                    n = 0;
                    try {
                        int n2 = 0;
                        do {
                            if ((n = this.doReadBytes(byteBuf)) > 0) {
                                bl2 = true;
                            } else if (n < 0) {
                                bl = true;
                            }
                            int n3 = this.available();
                            if (n3 <= 0) break;
                            if (!byteBuf.isWritable()) {
                                int n4;
                                int n5 = byteBuf.capacity();
                                if (n5 == (n4 = byteBuf.maxCapacity())) {
                                    if (bl2) {
                                        bl2 = false;
                                        channelPipeline.fireChannelRead(byteBuf);
                                        byteBuf = this.alloc().buffer();
                                    }
                                } else {
                                    int n6 = byteBuf.writerIndex();
                                    if (n6 + n3 > n4) {
                                        byteBuf.capacity(n4);
                                    } else {
                                        byteBuf.ensureWritable(n3);
                                    }
                                }
                            }
                            if (n2 >= Integer.MAX_VALUE - n) {
                                n2 = Integer.MAX_VALUE;
                                break;
                            }
                            n2 += n;
                        } while (channelConfig.isAutoRead());
                        handle.record(n2);
                        if (!bl2) break block31;
                        channelPipeline.fireChannelRead(byteBuf);
                    }
                    catch (Throwable throwable2) {
                        throwable = throwable2;
                        if (bl2) {
                            channelPipeline.fireChannelRead(byteBuf);
                        } else {
                            byteBuf.release();
                        }
                        channelPipeline.fireChannelReadComplete();
                        if (throwable != null) {
                            if (throwable instanceof IOException) {
                                bl = true;
                                this.pipeline().fireExceptionCaught(throwable);
                            } else {
                                channelPipeline.fireExceptionCaught(throwable);
                                this.unsafe().close(this.voidPromise());
                            }
                        }
                        if (bl) {
                            this.inputShutdown = true;
                            if (this.isOpen()) {
                                if (Boolean.TRUE.equals(this.config().getOption(ChannelOption.ALLOW_HALF_CLOSURE))) {
                                    channelPipeline.fireUserEventTriggered(ChannelInputShutdownEvent.INSTANCE);
                                } else {
                                    this.unsafe().close(this.unsafe().voidPromise());
                                }
                            }
                        }
                        if (n != 0 || !this.isActive()) break block32;
                        this.read();
                    }
                    break block33;
                }
                byteBuf.release();
            }
            channelPipeline.fireChannelReadComplete();
            if (throwable != null) {
                if (throwable instanceof IOException) {
                    bl = true;
                    this.pipeline().fireExceptionCaught(throwable);
                } else {
                    channelPipeline.fireExceptionCaught(throwable);
                    this.unsafe().close(this.voidPromise());
                }
            }
            if (bl) {
                this.inputShutdown = true;
                if (this.isOpen()) {
                    if (Boolean.TRUE.equals(this.config().getOption(ChannelOption.ALLOW_HALF_CLOSURE))) {
                        channelPipeline.fireUserEventTriggered(ChannelInputShutdownEvent.INSTANCE);
                    } else {
                        this.unsafe().close(this.unsafe().voidPromise());
                    }
                }
            }
            if (n != 0 || !this.isActive()) break block32;
            this.read();
        }
    }
}

