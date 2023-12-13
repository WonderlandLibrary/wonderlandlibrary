/*
 * Decompiled with CFR 0.150.
 */
package io.netty.handler.codec.spdy;

import io.netty.handler.codec.spdy.SpdyGoAwayFrame;
import io.netty.handler.codec.spdy.SpdySessionStatus;
import io.netty.util.internal.StringUtil;

public class DefaultSpdyGoAwayFrame
implements SpdyGoAwayFrame {
    private int lastGoodStreamId;
    private SpdySessionStatus status;

    public DefaultSpdyGoAwayFrame(int lastGoodStreamId) {
        this(lastGoodStreamId, 0);
    }

    public DefaultSpdyGoAwayFrame(int lastGoodStreamId, int statusCode) {
        this(lastGoodStreamId, SpdySessionStatus.valueOf(statusCode));
    }

    public DefaultSpdyGoAwayFrame(int lastGoodStreamId, SpdySessionStatus status) {
        this.setLastGoodStreamId(lastGoodStreamId);
        this.setStatus(status);
    }

    @Override
    public int getLastGoodStreamId() {
        return this.lastGoodStreamId;
    }

    @Override
    public SpdyGoAwayFrame setLastGoodStreamId(int lastGoodStreamId) {
        if (lastGoodStreamId < 0) {
            throw new IllegalArgumentException("Last-good-stream-ID cannot be negative: " + lastGoodStreamId);
        }
        this.lastGoodStreamId = lastGoodStreamId;
        return this;
    }

    @Override
    public SpdySessionStatus getStatus() {
        return this.status;
    }

    @Override
    public SpdyGoAwayFrame setStatus(SpdySessionStatus status) {
        this.status = status;
        return this;
    }

    public String toString() {
        StringBuilder buf = new StringBuilder();
        buf.append(StringUtil.simpleClassName(this));
        buf.append(StringUtil.NEWLINE);
        buf.append("--> Last-good-stream-ID = ");
        buf.append(this.getLastGoodStreamId());
        buf.append(StringUtil.NEWLINE);
        buf.append("--> Status: ");
        buf.append(this.getStatus().toString());
        return buf.toString();
    }
}

