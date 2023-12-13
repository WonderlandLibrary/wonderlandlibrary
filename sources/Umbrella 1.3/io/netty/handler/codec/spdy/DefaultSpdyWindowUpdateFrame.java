/*
 * Decompiled with CFR 0.150.
 */
package io.netty.handler.codec.spdy;

import io.netty.handler.codec.spdy.SpdyWindowUpdateFrame;
import io.netty.util.internal.StringUtil;

public class DefaultSpdyWindowUpdateFrame
implements SpdyWindowUpdateFrame {
    private int streamId;
    private int deltaWindowSize;

    public DefaultSpdyWindowUpdateFrame(int streamId, int deltaWindowSize) {
        this.setStreamId(streamId);
        this.setDeltaWindowSize(deltaWindowSize);
    }

    @Override
    public int getStreamId() {
        return this.streamId;
    }

    @Override
    public SpdyWindowUpdateFrame setStreamId(int streamId) {
        if (streamId < 0) {
            throw new IllegalArgumentException("Stream-ID cannot be negative: " + streamId);
        }
        this.streamId = streamId;
        return this;
    }

    @Override
    public int getDeltaWindowSize() {
        return this.deltaWindowSize;
    }

    @Override
    public SpdyWindowUpdateFrame setDeltaWindowSize(int deltaWindowSize) {
        if (deltaWindowSize <= 0) {
            throw new IllegalArgumentException("Delta-Window-Size must be positive: " + deltaWindowSize);
        }
        this.deltaWindowSize = deltaWindowSize;
        return this;
    }

    public String toString() {
        StringBuilder buf = new StringBuilder();
        buf.append(StringUtil.simpleClassName(this));
        buf.append(StringUtil.NEWLINE);
        buf.append("--> Stream-ID = ");
        buf.append(this.getStreamId());
        buf.append(StringUtil.NEWLINE);
        buf.append("--> Delta-Window-Size = ");
        buf.append(this.getDeltaWindowSize());
        return buf.toString();
    }
}

