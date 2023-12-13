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
    private final ByteBuf[] delimiters;
    private final int maxFrameLength;
    private final boolean stripDelimiter;
    private final boolean failFast;
    private boolean discardingTooLongFrame;
    private int tooLongFrameLength;
    private final LineBasedFrameDecoder lineBasedDecoder;

    public DelimiterBasedFrameDecoder(int maxFrameLength, ByteBuf delimiter) {
        this(maxFrameLength, true, delimiter);
    }

    public DelimiterBasedFrameDecoder(int maxFrameLength, boolean stripDelimiter, ByteBuf delimiter) {
        this(maxFrameLength, stripDelimiter, true, delimiter);
    }

    public DelimiterBasedFrameDecoder(int maxFrameLength, boolean stripDelimiter, boolean failFast, ByteBuf delimiter) {
        this(maxFrameLength, stripDelimiter, failFast, new ByteBuf[]{delimiter.slice(delimiter.readerIndex(), delimiter.readableBytes())});
    }

    public DelimiterBasedFrameDecoder(int maxFrameLength, ByteBuf ... delimiters) {
        this(maxFrameLength, true, delimiters);
    }

    public DelimiterBasedFrameDecoder(int maxFrameLength, boolean stripDelimiter, ByteBuf ... delimiters) {
        this(maxFrameLength, stripDelimiter, true, delimiters);
    }

    public DelimiterBasedFrameDecoder(int maxFrameLength, boolean stripDelimiter, boolean failFast, ByteBuf ... delimiters) {
        DelimiterBasedFrameDecoder.validateMaxFrameLength(maxFrameLength);
        if (delimiters == null) {
            throw new NullPointerException("delimiters");
        }
        if (delimiters.length == 0) {
            throw new IllegalArgumentException("empty delimiters");
        }
        if (DelimiterBasedFrameDecoder.isLineBased(delimiters) && !this.isSubclass()) {
            this.lineBasedDecoder = new LineBasedFrameDecoder(maxFrameLength, stripDelimiter, failFast);
            this.delimiters = null;
        } else {
            this.delimiters = new ByteBuf[delimiters.length];
            for (int i2 = 0; i2 < delimiters.length; ++i2) {
                ByteBuf d2 = delimiters[i2];
                DelimiterBasedFrameDecoder.validateDelimiter(d2);
                this.delimiters[i2] = d2.slice(d2.readerIndex(), d2.readableBytes());
            }
            this.lineBasedDecoder = null;
        }
        this.maxFrameLength = maxFrameLength;
        this.stripDelimiter = stripDelimiter;
        this.failFast = failFast;
    }

    private static boolean isLineBased(ByteBuf[] delimiters) {
        if (delimiters.length != 2) {
            return false;
        }
        ByteBuf a2 = delimiters[0];
        ByteBuf b2 = delimiters[1];
        if (a2.capacity() < b2.capacity()) {
            a2 = delimiters[1];
            b2 = delimiters[0];
        }
        return a2.capacity() == 2 && b2.capacity() == 1 && a2.getByte(0) == 13 && a2.getByte(1) == 10 && b2.getByte(0) == 10;
    }

    private boolean isSubclass() {
        return this.getClass() != DelimiterBasedFrameDecoder.class;
    }

    @Override
    protected final void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        Object decoded = this.decode(ctx, in);
        if (decoded != null) {
            out.add(decoded);
        }
    }

    protected Object decode(ChannelHandlerContext ctx, ByteBuf buffer) throws Exception {
        if (this.lineBasedDecoder != null) {
            return this.lineBasedDecoder.decode(ctx, buffer);
        }
        int minFrameLength = Integer.MAX_VALUE;
        ByteBuf minDelim = null;
        for (ByteBuf delim : this.delimiters) {
            int frameLength = DelimiterBasedFrameDecoder.indexOf(buffer, delim);
            if (frameLength < 0 || frameLength >= minFrameLength) continue;
            minFrameLength = frameLength;
            minDelim = delim;
        }
        if (minDelim != null) {
            ByteBuf frame;
            int minDelimLength = minDelim.capacity();
            if (this.discardingTooLongFrame) {
                this.discardingTooLongFrame = false;
                buffer.skipBytes(minFrameLength + minDelimLength);
                int tooLongFrameLength = this.tooLongFrameLength;
                this.tooLongFrameLength = 0;
                if (!this.failFast) {
                    this.fail(tooLongFrameLength);
                }
                return null;
            }
            if (minFrameLength > this.maxFrameLength) {
                buffer.skipBytes(minFrameLength + minDelimLength);
                this.fail(minFrameLength);
                return null;
            }
            if (this.stripDelimiter) {
                frame = buffer.readSlice(minFrameLength);
                buffer.skipBytes(minDelimLength);
            } else {
                frame = buffer.readSlice(minFrameLength + minDelimLength);
            }
            return frame.retain();
        }
        if (!this.discardingTooLongFrame) {
            if (buffer.readableBytes() > this.maxFrameLength) {
                this.tooLongFrameLength = buffer.readableBytes();
                buffer.skipBytes(buffer.readableBytes());
                this.discardingTooLongFrame = true;
                if (this.failFast) {
                    this.fail(this.tooLongFrameLength);
                }
            }
        } else {
            this.tooLongFrameLength += buffer.readableBytes();
            buffer.skipBytes(buffer.readableBytes());
        }
        return null;
    }

    private void fail(long frameLength) {
        if (frameLength > 0L) {
            throw new TooLongFrameException("frame length exceeds " + this.maxFrameLength + ": " + frameLength + " - discarded");
        }
        throw new TooLongFrameException("frame length exceeds " + this.maxFrameLength + " - discarding");
    }

    private static int indexOf(ByteBuf haystack, ByteBuf needle) {
        for (int i2 = haystack.readerIndex(); i2 < haystack.writerIndex(); ++i2) {
            int needleIndex;
            int haystackIndex = i2;
            for (needleIndex = 0; needleIndex < needle.capacity() && haystack.getByte(haystackIndex) == needle.getByte(needleIndex); ++needleIndex) {
                if (++haystackIndex != haystack.writerIndex() || needleIndex == needle.capacity() - 1) continue;
                return -1;
            }
            if (needleIndex != needle.capacity()) continue;
            return i2 - haystack.readerIndex();
        }
        return -1;
    }

    private static void validateDelimiter(ByteBuf delimiter) {
        if (delimiter == null) {
            throw new NullPointerException("delimiter");
        }
        if (!delimiter.isReadable()) {
            throw new IllegalArgumentException("empty delimiter");
        }
    }

    private static void validateMaxFrameLength(int maxFrameLength) {
        if (maxFrameLength <= 0) {
            throw new IllegalArgumentException("maxFrameLength must be a positive integer: " + maxFrameLength);
        }
    }
}

