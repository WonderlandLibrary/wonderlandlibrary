/*
 * Decompiled with CFR 0.150.
 */
package io.netty.handler.codec.spdy;

import io.netty.handler.codec.spdy.DefaultSpdyHeadersFrame;
import io.netty.handler.codec.spdy.SpdySynStreamFrame;
import io.netty.util.internal.StringUtil;

public class DefaultSpdySynStreamFrame
extends DefaultSpdyHeadersFrame
implements SpdySynStreamFrame {
    private int associatedToStreamId;
    private byte priority;
    private boolean unidirectional;

    public DefaultSpdySynStreamFrame(int streamId, int associatedToStreamId, byte priority) {
        super(streamId);
        this.setAssociatedToStreamId(associatedToStreamId);
        this.setPriority(priority);
    }

    @Override
    public SpdySynStreamFrame setStreamId(int streamId) {
        super.setStreamId(streamId);
        return this;
    }

    @Override
    public SpdySynStreamFrame setLast(boolean last) {
        super.setLast(last);
        return this;
    }

    @Override
    public SpdySynStreamFrame setInvalid() {
        super.setInvalid();
        return this;
    }

    @Override
    public int getAssociatedToStreamId() {
        return this.associatedToStreamId;
    }

    @Override
    public SpdySynStreamFrame setAssociatedToStreamId(int associatedToStreamId) {
        if (associatedToStreamId < 0) {
            throw new IllegalArgumentException("Associated-To-Stream-ID cannot be negative: " + associatedToStreamId);
        }
        this.associatedToStreamId = associatedToStreamId;
        return this;
    }

    @Override
    public byte getPriority() {
        return this.priority;
    }

    @Override
    public SpdySynStreamFrame setPriority(byte priority) {
        if (priority < 0 || priority > 7) {
            throw new IllegalArgumentException("Priority must be between 0 and 7 inclusive: " + priority);
        }
        this.priority = priority;
        return this;
    }

    @Override
    public boolean isUnidirectional() {
        return this.unidirectional;
    }

    @Override
    public SpdySynStreamFrame setUnidirectional(boolean unidirectional) {
        this.unidirectional = unidirectional;
        return this;
    }

    @Override
    public String toString() {
        StringBuilder buf = new StringBuilder();
        buf.append(StringUtil.simpleClassName(this));
        buf.append("(last: ");
        buf.append(this.isLast());
        buf.append("; unidirectional: ");
        buf.append(this.isUnidirectional());
        buf.append(')');
        buf.append(StringUtil.NEWLINE);
        buf.append("--> Stream-ID = ");
        buf.append(this.getStreamId());
        buf.append(StringUtil.NEWLINE);
        if (this.associatedToStreamId != 0) {
            buf.append("--> Associated-To-Stream-ID = ");
            buf.append(this.getAssociatedToStreamId());
            buf.append(StringUtil.NEWLINE);
        }
        buf.append("--> Priority = ");
        buf.append(this.getPriority());
        buf.append(StringUtil.NEWLINE);
        buf.append("--> Headers:");
        buf.append(StringUtil.NEWLINE);
        this.appendHeaders(buf);
        buf.setLength(buf.length() - StringUtil.NEWLINE.length());
        return buf.toString();
    }
}

