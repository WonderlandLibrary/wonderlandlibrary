/*
 * Decompiled with CFR 0.150.
 */
package io.netty.channel.oio;

import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelMetadata;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelOutboundBuffer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.FileRegion;
import io.netty.channel.oio.AbstractOioChannel;
import io.netty.channel.socket.ChannelInputShutdownEvent;
import io.netty.util.internal.StringUtil;
import java.io.IOException;

public abstract class AbstractOioByteChannel
extends AbstractOioChannel {
    private volatile boolean inputShutdown;
    private static final ChannelMetadata METADATA = new ChannelMetadata(false);

    protected AbstractOioByteChannel(Channel parent) {
        super(parent);
    }

    protected boolean isInputShutdown() {
        return this.inputShutdown;
    }

    @Override
    public ChannelMetadata metadata() {
        return METADATA;
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

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    protected void doRead() {
        if (this.checkInputShutdown()) {
            return;
        }
        ChannelPipeline pipeline = this.pipeline();
        ByteBuf byteBuf = this.alloc().buffer();
        boolean closed = false;
        boolean read = false;
        Throwable exception = null;
        try {
            do {
                int maxCapacity;
                int localReadAmount;
                if ((localReadAmount = this.doReadBytes(byteBuf)) > 0) {
                    read = true;
                } else if (localReadAmount < 0) {
                    closed = true;
                }
                int available = this.available();
                if (available <= 0) {
                    break;
                }
                if (byteBuf.isWritable()) continue;
                int capacity = byteBuf.capacity();
                if (capacity == (maxCapacity = byteBuf.maxCapacity())) {
                    if (!read) continue;
                    read = false;
                    pipeline.fireChannelRead(byteBuf);
                    byteBuf = this.alloc().buffer();
                    continue;
                }
                int writerIndex = byteBuf.writerIndex();
                if (writerIndex + available > maxCapacity) {
                    byteBuf.capacity(maxCapacity);
                    continue;
                }
                byteBuf.ensureWritable(available);
            } while (this.config().isAutoRead());
        }
        catch (Throwable t) {
            exception = t;
        }
        finally {
            if (read) {
                pipeline.fireChannelRead(byteBuf);
            } else {
                byteBuf.release();
            }
            pipeline.fireChannelReadComplete();
            if (exception != null) {
                if (exception instanceof IOException) {
                    closed = true;
                    this.pipeline().fireExceptionCaught(exception);
                } else {
                    pipeline.fireExceptionCaught(exception);
                    this.unsafe().close(this.voidPromise());
                }
            }
            if (closed) {
                this.inputShutdown = true;
                if (this.isOpen()) {
                    if (Boolean.TRUE.equals(this.config().getOption(ChannelOption.ALLOW_HALF_CLOSURE))) {
                        pipeline.fireUserEventTriggered(ChannelInputShutdownEvent.INSTANCE);
                    } else {
                        this.unsafe().close(this.unsafe().voidPromise());
                    }
                }
            }
        }
    }

    @Override
    protected void doWrite(ChannelOutboundBuffer in) throws Exception {
        Object msg;
        while ((msg = in.current()) != null) {
            if (msg instanceof ByteBuf) {
                ByteBuf buf = (ByteBuf)msg;
                while (buf.isReadable()) {
                    this.doWriteBytes(buf);
                }
                in.remove();
                continue;
            }
            if (msg instanceof FileRegion) {
                this.doWriteFileRegion((FileRegion)msg);
                in.remove();
                continue;
            }
            in.remove(new UnsupportedOperationException("unsupported message type: " + StringUtil.simpleClassName(msg)));
        }
    }

    protected abstract int available();

    protected abstract int doReadBytes(ByteBuf var1) throws Exception;

    protected abstract void doWriteBytes(ByteBuf var1) throws Exception;

    protected abstract void doWriteFileRegion(FileRegion var1) throws Exception;
}

