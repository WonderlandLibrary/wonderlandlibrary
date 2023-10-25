/*
 * Decompiled with CFR 0.152.
 */
package io.netty.handler.codec.spdy;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.handler.codec.spdy.DefaultSpdyStreamFrame;
import io.netty.handler.codec.spdy.SpdyDataFrame;
import io.netty.util.IllegalReferenceCountException;
import io.netty.util.internal.StringUtil;

public class DefaultSpdyDataFrame
extends DefaultSpdyStreamFrame
implements SpdyDataFrame {
    private final ByteBuf data;

    @Override
    public ByteBuf content() {
        if (this.data.refCnt() <= 0) {
            throw new IllegalReferenceCountException(this.data.refCnt());
        }
        return this.data;
    }

    public DefaultSpdyDataFrame(int n, ByteBuf byteBuf) {
        super(n);
        if (byteBuf == null) {
            throw new NullPointerException("data");
        }
        this.data = DefaultSpdyDataFrame.validate(byteBuf);
    }

    @Override
    public SpdyDataFrame copy() {
        DefaultSpdyDataFrame defaultSpdyDataFrame = new DefaultSpdyDataFrame(this.streamId(), this.content().copy());
        defaultSpdyDataFrame.setLast(this.isLast());
        return defaultSpdyDataFrame;
    }

    private static ByteBuf validate(ByteBuf byteBuf) {
        if (byteBuf.readableBytes() > 0xFFFFFF) {
            throw new IllegalArgumentException("data payload cannot exceed 16777215 bytes");
        }
        return byteBuf;
    }

    @Override
    public SpdyDataFrame setLast(boolean bl) {
        super.setLast(bl);
        return this;
    }

    @Override
    public SpdyDataFrame duplicate() {
        DefaultSpdyDataFrame defaultSpdyDataFrame = new DefaultSpdyDataFrame(this.streamId(), this.content().duplicate());
        defaultSpdyDataFrame.setLast(this.isLast());
        return defaultSpdyDataFrame;
    }

    @Override
    public SpdyDataFrame setStreamId(int n) {
        super.setStreamId(n);
        return this;
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(StringUtil.simpleClassName(this));
        stringBuilder.append("(last: ");
        stringBuilder.append(this.isLast());
        stringBuilder.append(')');
        stringBuilder.append(StringUtil.NEWLINE);
        stringBuilder.append("--> Stream-ID = ");
        stringBuilder.append(this.streamId());
        stringBuilder.append(StringUtil.NEWLINE);
        stringBuilder.append("--> Size = ");
        if (this.refCnt() == 0) {
            stringBuilder.append("(freed)");
        } else {
            stringBuilder.append(this.content().readableBytes());
        }
        return stringBuilder.toString();
    }

    @Override
    public SpdyDataFrame retain() {
        this.data.retain();
        return this;
    }

    @Override
    public SpdyDataFrame retain(int n) {
        this.data.retain(n);
        return this;
    }

    public DefaultSpdyDataFrame(int n) {
        this(n, Unpooled.buffer(0));
    }

    @Override
    public boolean release(int n) {
        return this.data.release(n);
    }

    @Override
    public boolean release() {
        return this.data.release();
    }

    @Override
    public int refCnt() {
        return this.data.refCnt();
    }
}

