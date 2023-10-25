/*
 * Decompiled with CFR 0.152.
 */
package io.netty.handler.codec.spdy;

import io.netty.handler.codec.spdy.SpdyGoAwayFrame;
import io.netty.handler.codec.spdy.SpdySessionStatus;
import io.netty.util.internal.StringUtil;

public class DefaultSpdyGoAwayFrame
implements SpdyGoAwayFrame {
    private int lastGoodStreamId;
    private SpdySessionStatus status;

    @Override
    public SpdyGoAwayFrame setLastGoodStreamId(int n) {
        if (n < 0) {
            throw new IllegalArgumentException("Last-good-stream-ID cannot be negative: " + n);
        }
        this.lastGoodStreamId = n;
        return this;
    }

    public DefaultSpdyGoAwayFrame(int n, int n2) {
        this(n, SpdySessionStatus.valueOf(n2));
    }

    @Override
    public int lastGoodStreamId() {
        return this.lastGoodStreamId;
    }

    public DefaultSpdyGoAwayFrame(int n, SpdySessionStatus spdySessionStatus) {
        this.setLastGoodStreamId(n);
        this.setStatus(spdySessionStatus);
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(StringUtil.simpleClassName(this));
        stringBuilder.append(StringUtil.NEWLINE);
        stringBuilder.append("--> Last-good-stream-ID = ");
        stringBuilder.append(this.lastGoodStreamId());
        stringBuilder.append(StringUtil.NEWLINE);
        stringBuilder.append("--> Status: ");
        stringBuilder.append(this.status());
        return stringBuilder.toString();
    }

    public DefaultSpdyGoAwayFrame(int n) {
        this(n, 0);
    }

    @Override
    public SpdyGoAwayFrame setStatus(SpdySessionStatus spdySessionStatus) {
        this.status = spdySessionStatus;
        return this;
    }

    @Override
    public SpdySessionStatus status() {
        return this.status;
    }
}

