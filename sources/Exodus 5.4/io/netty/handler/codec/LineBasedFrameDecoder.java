/*
 * Decompiled with CFR 0.152.
 */
package io.netty.handler.codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.handler.codec.TooLongFrameException;
import java.util.List;

public class LineBasedFrameDecoder
extends ByteToMessageDecoder {
    private final boolean stripDelimiter;
    private final int maxLength;
    private final boolean failFast;
    private boolean discarding;
    private int discardedBytes;

    public LineBasedFrameDecoder(int n, boolean bl, boolean bl2) {
        this.maxLength = n;
        this.failFast = bl2;
        this.stripDelimiter = bl;
    }

    private static int findEndOfLine(ByteBuf byteBuf) {
        int n = byteBuf.writerIndex();
        for (int i = byteBuf.readerIndex(); i < n; ++i) {
            byte by = byteBuf.getByte(i);
            if (by == 10) {
                return i;
            }
            if (by != 13 || i >= n - 1 || byteBuf.getByte(i + 1) != 10) continue;
            return i;
        }
        return -1;
    }

    protected Object decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf) throws Exception {
        int n = LineBasedFrameDecoder.findEndOfLine(byteBuf);
        if (!this.discarding) {
            if (n >= 0) {
                ByteBuf byteBuf2;
                int n2;
                int n3 = n - byteBuf.readerIndex();
                int n4 = n2 = byteBuf.getByte(n) == 13 ? 2 : 1;
                if (n3 > this.maxLength) {
                    byteBuf.readerIndex(n + n2);
                    this.fail(channelHandlerContext, n3);
                    return null;
                }
                if (this.stripDelimiter) {
                    byteBuf2 = byteBuf.readSlice(n3);
                    byteBuf.skipBytes(n2);
                } else {
                    byteBuf2 = byteBuf.readSlice(n3 + n2);
                }
                return byteBuf2.retain();
            }
            int n5 = byteBuf.readableBytes();
            if (n5 > this.maxLength) {
                this.discardedBytes = n5;
                byteBuf.readerIndex(byteBuf.writerIndex());
                this.discarding = true;
                if (this.failFast) {
                    this.fail(channelHandlerContext, "over " + this.discardedBytes);
                }
            }
            return null;
        }
        if (n >= 0) {
            int n6 = this.discardedBytes + n - byteBuf.readerIndex();
            int n7 = byteBuf.getByte(n) == 13 ? 2 : 1;
            byteBuf.readerIndex(n + n7);
            this.discardedBytes = 0;
            this.discarding = false;
            if (!this.failFast) {
                this.fail(channelHandlerContext, n6);
            }
        } else {
            this.discardedBytes = byteBuf.readableBytes();
            byteBuf.readerIndex(byteBuf.writerIndex());
        }
        return null;
    }

    private void fail(ChannelHandlerContext channelHandlerContext, String string) {
        channelHandlerContext.fireExceptionCaught(new TooLongFrameException("frame length (" + string + ") exceeds the allowed maximum (" + this.maxLength + ')'));
    }

    @Override
    protected final void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) throws Exception {
        Object object = this.decode(channelHandlerContext, byteBuf);
        if (object != null) {
            list.add(object);
        }
    }

    public LineBasedFrameDecoder(int n) {
        this(n, true, false);
    }

    private void fail(ChannelHandlerContext channelHandlerContext, int n) {
        this.fail(channelHandlerContext, String.valueOf(n));
    }
}

