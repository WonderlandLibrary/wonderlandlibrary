/*
 * Decompiled with CFR 0.152.
 */
package io.netty.handler.codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.handler.codec.LineBasedFrameDecoder;
import io.netty.handler.codec.TooLongFrameException;
import java.util.List;

public class DelimiterBasedFrameDecoder
extends ByteToMessageDecoder {
    private boolean discardingTooLongFrame;
    private final int maxFrameLength;
    private final boolean failFast;
    private final boolean stripDelimiter;
    private final ByteBuf[] delimiters;
    private int tooLongFrameLength;
    private final LineBasedFrameDecoder lineBasedDecoder;

    private static void validateMaxFrameLength(int n) {
        if (n <= 0) {
            throw new IllegalArgumentException("maxFrameLength must be a positive integer: " + n);
        }
    }

    private boolean isSubclass() {
        return this.getClass() != DelimiterBasedFrameDecoder.class;
    }

    public DelimiterBasedFrameDecoder(int n, boolean bl, ByteBuf ... byteBufArray) {
        this(n, bl, true, byteBufArray);
    }

    public DelimiterBasedFrameDecoder(int n, ByteBuf byteBuf) {
        this(n, true, byteBuf);
    }

    @Override
    protected final void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) throws Exception {
        Object object = this.decode(channelHandlerContext, byteBuf);
        if (object != null) {
            list.add(object);
        }
    }

    public DelimiterBasedFrameDecoder(int n, boolean bl, ByteBuf byteBuf) {
        this(n, bl, true, byteBuf);
    }

    private void fail(long l) {
        if (l > 0L) {
            throw new TooLongFrameException("frame length exceeds " + this.maxFrameLength + ": " + l + " - discarded");
        }
        throw new TooLongFrameException("frame length exceeds " + this.maxFrameLength + " - discarding");
    }

    public DelimiterBasedFrameDecoder(int n, boolean bl, boolean bl2, ByteBuf byteBuf) {
        this(n, bl, bl2, new ByteBuf[]{byteBuf.slice(byteBuf.readerIndex(), byteBuf.readableBytes())});
    }

    private static void validateDelimiter(ByteBuf byteBuf) {
        if (byteBuf == null) {
            throw new NullPointerException("delimiter");
        }
        if (!byteBuf.isReadable()) {
            throw new IllegalArgumentException("empty delimiter");
        }
    }

    private static int indexOf(ByteBuf byteBuf, ByteBuf byteBuf2) {
        for (int i = byteBuf.readerIndex(); i < byteBuf.writerIndex(); ++i) {
            int n;
            int n2 = i;
            for (n = 0; n < byteBuf2.capacity() && byteBuf.getByte(n2) == byteBuf2.getByte(n); ++n) {
                if (++n2 != byteBuf.writerIndex() || n == byteBuf2.capacity() - 1) continue;
                return -1;
            }
            if (n != byteBuf2.capacity()) continue;
            return i - byteBuf.readerIndex();
        }
        return -1;
    }

    private static boolean isLineBased(ByteBuf[] byteBufArray) {
        if (byteBufArray.length != 2) {
            return false;
        }
        ByteBuf byteBuf = byteBufArray[0];
        ByteBuf byteBuf2 = byteBufArray[1];
        if (byteBuf.capacity() < byteBuf2.capacity()) {
            byteBuf = byteBufArray[1];
            byteBuf2 = byteBufArray[0];
        }
        return byteBuf.capacity() == 2 && byteBuf2.capacity() == 1 && byteBuf.getByte(0) == 13 && byteBuf.getByte(1) == 10 && byteBuf2.getByte(0) == 10;
    }

    public DelimiterBasedFrameDecoder(int n, ByteBuf ... byteBufArray) {
        this(n, true, byteBufArray);
    }

    protected Object decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf) throws Exception {
        if (this.lineBasedDecoder != null) {
            return this.lineBasedDecoder.decode(channelHandlerContext, byteBuf);
        }
        int n = Integer.MAX_VALUE;
        ByteBuf byteBuf2 = null;
        for (ByteBuf byteBuf3 : this.delimiters) {
            int n2 = DelimiterBasedFrameDecoder.indexOf(byteBuf, byteBuf3);
            if (n2 < 0 || n2 >= n) continue;
            n = n2;
            byteBuf2 = byteBuf3;
        }
        if (byteBuf2 != null) {
            ByteBuf byteBuf4;
            int n3 = byteBuf2.capacity();
            if (this.discardingTooLongFrame) {
                this.discardingTooLongFrame = false;
                byteBuf.skipBytes(n + n3);
                int n4 = this.tooLongFrameLength;
                this.tooLongFrameLength = 0;
                if (!this.failFast) {
                    this.fail(n4);
                }
                return null;
            }
            if (n > this.maxFrameLength) {
                byteBuf.skipBytes(n + n3);
                this.fail(n);
                return null;
            }
            if (this.stripDelimiter) {
                byteBuf4 = byteBuf.readSlice(n);
                byteBuf.skipBytes(n3);
            } else {
                byteBuf4 = byteBuf.readSlice(n + n3);
            }
            return byteBuf4.retain();
        }
        if (!this.discardingTooLongFrame) {
            if (byteBuf.readableBytes() > this.maxFrameLength) {
                this.tooLongFrameLength = byteBuf.readableBytes();
                byteBuf.skipBytes(byteBuf.readableBytes());
                this.discardingTooLongFrame = true;
                if (this.failFast) {
                    this.fail(this.tooLongFrameLength);
                }
            }
        } else {
            this.tooLongFrameLength += byteBuf.readableBytes();
            byteBuf.skipBytes(byteBuf.readableBytes());
        }
        return null;
    }

    public DelimiterBasedFrameDecoder(int n, boolean bl, boolean bl2, ByteBuf ... byteBufArray) {
        DelimiterBasedFrameDecoder.validateMaxFrameLength(n);
        if (byteBufArray == null) {
            throw new NullPointerException("delimiters");
        }
        if (byteBufArray.length == 0) {
            throw new IllegalArgumentException("empty delimiters");
        }
        if (DelimiterBasedFrameDecoder.isLineBased(byteBufArray) && !this.isSubclass()) {
            this.lineBasedDecoder = new LineBasedFrameDecoder(n, bl, bl2);
            this.delimiters = null;
        } else {
            this.delimiters = new ByteBuf[byteBufArray.length];
            for (int i = 0; i < byteBufArray.length; ++i) {
                ByteBuf byteBuf = byteBufArray[i];
                DelimiterBasedFrameDecoder.validateDelimiter(byteBuf);
                this.delimiters[i] = byteBuf.slice(byteBuf.readerIndex(), byteBuf.readableBytes());
            }
            this.lineBasedDecoder = null;
        }
        this.maxFrameLength = n;
        this.stripDelimiter = bl;
        this.failFast = bl2;
    }
}

