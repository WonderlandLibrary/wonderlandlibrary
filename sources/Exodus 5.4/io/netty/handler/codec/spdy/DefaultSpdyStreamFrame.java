/*
 * Decompiled with CFR 0.152.
 */
package io.netty.handler.codec.spdy;

import io.netty.handler.codec.spdy.SpdyStreamFrame;

public abstract class DefaultSpdyStreamFrame
implements SpdyStreamFrame {
    private int streamId;
    private boolean last;

    @Override
    public int streamId() {
        return this.streamId;
    }

    protected DefaultSpdyStreamFrame(int n) {
        this.setStreamId(n);
    }

    @Override
    public SpdyStreamFrame setLast(boolean bl) {
        this.last = bl;
        return this;
    }

    @Override
    public SpdyStreamFrame setStreamId(int n) {
        if (n <= 0) {
            throw new IllegalArgumentException("Stream-ID must be positive: " + n);
        }
        this.streamId = n;
        return this;
    }

    @Override
    public boolean isLast() {
        return this.last;
    }
}

