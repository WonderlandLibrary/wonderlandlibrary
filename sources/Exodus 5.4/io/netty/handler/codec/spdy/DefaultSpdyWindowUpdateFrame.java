/*
 * Decompiled with CFR 0.152.
 */
package io.netty.handler.codec.spdy;

import io.netty.handler.codec.spdy.SpdyWindowUpdateFrame;
import io.netty.util.internal.StringUtil;

public class DefaultSpdyWindowUpdateFrame
implements SpdyWindowUpdateFrame {
    private int deltaWindowSize;
    private int streamId;

    @Override
    public SpdyWindowUpdateFrame setDeltaWindowSize(int n) {
        if (n <= 0) {
            throw new IllegalArgumentException("Delta-Window-Size must be positive: " + n);
        }
        this.deltaWindowSize = n;
        return this;
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(StringUtil.simpleClassName(this));
        stringBuilder.append(StringUtil.NEWLINE);
        stringBuilder.append("--> Stream-ID = ");
        stringBuilder.append(this.streamId());
        stringBuilder.append(StringUtil.NEWLINE);
        stringBuilder.append("--> Delta-Window-Size = ");
        stringBuilder.append(this.deltaWindowSize());
        return stringBuilder.toString();
    }

    public DefaultSpdyWindowUpdateFrame(int n, int n2) {
        this.setStreamId(n);
        this.setDeltaWindowSize(n2);
    }

    @Override
    public int deltaWindowSize() {
        return this.deltaWindowSize;
    }

    @Override
    public SpdyWindowUpdateFrame setStreamId(int n) {
        if (n < 0) {
            throw new IllegalArgumentException("Stream-ID cannot be negative: " + n);
        }
        this.streamId = n;
        return this;
    }

    @Override
    public int streamId() {
        return this.streamId;
    }
}

