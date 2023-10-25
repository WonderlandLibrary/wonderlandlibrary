/*
 * Decompiled with CFR 0.152.
 */
package io.netty.handler.codec.spdy;

import io.netty.handler.codec.spdy.DefaultSpdyHeadersFrame;
import io.netty.handler.codec.spdy.SpdySynStreamFrame;
import io.netty.util.internal.StringUtil;

public class DefaultSpdySynStreamFrame
extends DefaultSpdyHeadersFrame
implements SpdySynStreamFrame {
    private byte priority;
    private boolean unidirectional;
    private int associatedStreamId;

    @Override
    public SpdySynStreamFrame setInvalid() {
        super.setInvalid();
        return this;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(StringUtil.simpleClassName(this));
        stringBuilder.append("(last: ");
        stringBuilder.append(this.isLast());
        stringBuilder.append("; unidirectional: ");
        stringBuilder.append(this.isUnidirectional());
        stringBuilder.append(')');
        stringBuilder.append(StringUtil.NEWLINE);
        stringBuilder.append("--> Stream-ID = ");
        stringBuilder.append(this.streamId());
        stringBuilder.append(StringUtil.NEWLINE);
        if (this.associatedStreamId != 0) {
            stringBuilder.append("--> Associated-To-Stream-ID = ");
            stringBuilder.append(this.associatedStreamId());
            stringBuilder.append(StringUtil.NEWLINE);
        }
        stringBuilder.append("--> Priority = ");
        stringBuilder.append(this.priority());
        stringBuilder.append(StringUtil.NEWLINE);
        stringBuilder.append("--> Headers:");
        stringBuilder.append(StringUtil.NEWLINE);
        this.appendHeaders(stringBuilder);
        stringBuilder.setLength(stringBuilder.length() - StringUtil.NEWLINE.length());
        return stringBuilder.toString();
    }

    @Override
    public SpdySynStreamFrame setAssociatedStreamId(int n) {
        if (n < 0) {
            throw new IllegalArgumentException("Associated-To-Stream-ID cannot be negative: " + n);
        }
        this.associatedStreamId = n;
        return this;
    }

    @Override
    public boolean isUnidirectional() {
        return this.unidirectional;
    }

    @Override
    public SpdySynStreamFrame setLast(boolean bl) {
        super.setLast(bl);
        return this;
    }

    @Override
    public byte priority() {
        return this.priority;
    }

    public DefaultSpdySynStreamFrame(int n, int n2, byte by) {
        super(n);
        this.setAssociatedStreamId(n2);
        this.setPriority(by);
    }

    @Override
    public SpdySynStreamFrame setUnidirectional(boolean bl) {
        this.unidirectional = bl;
        return this;
    }

    @Override
    public SpdySynStreamFrame setPriority(byte by) {
        if (by < 0 || by > 7) {
            throw new IllegalArgumentException("Priority must be between 0 and 7 inclusive: " + by);
        }
        this.priority = by;
        return this;
    }

    @Override
    public int associatedStreamId() {
        return this.associatedStreamId;
    }

    @Override
    public SpdySynStreamFrame setStreamId(int n) {
        super.setStreamId(n);
        return this;
    }
}

