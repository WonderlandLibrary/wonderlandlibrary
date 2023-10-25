/*
 * Decompiled with CFR 0.152.
 */
package io.netty.handler.codec.spdy;

import io.netty.handler.codec.spdy.DefaultSpdyStreamFrame;
import io.netty.handler.codec.spdy.SpdyRstStreamFrame;
import io.netty.handler.codec.spdy.SpdyStreamStatus;
import io.netty.util.internal.StringUtil;

public class DefaultSpdyRstStreamFrame
extends DefaultSpdyStreamFrame
implements SpdyRstStreamFrame {
    private SpdyStreamStatus status;

    @Override
    public SpdyRstStreamFrame setStreamId(int n) {
        super.setStreamId(n);
        return this;
    }

    public DefaultSpdyRstStreamFrame(int n, int n2) {
        this(n, SpdyStreamStatus.valueOf(n2));
    }

    @Override
    public SpdyRstStreamFrame setLast(boolean bl) {
        super.setLast(bl);
        return this;
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(StringUtil.simpleClassName(this));
        stringBuilder.append(StringUtil.NEWLINE);
        stringBuilder.append("--> Stream-ID = ");
        stringBuilder.append(this.streamId());
        stringBuilder.append(StringUtil.NEWLINE);
        stringBuilder.append("--> Status: ");
        stringBuilder.append(this.status());
        return stringBuilder.toString();
    }

    public DefaultSpdyRstStreamFrame(int n, SpdyStreamStatus spdyStreamStatus) {
        super(n);
        this.setStatus(spdyStreamStatus);
    }

    @Override
    public SpdyRstStreamFrame setStatus(SpdyStreamStatus spdyStreamStatus) {
        this.status = spdyStreamStatus;
        return this;
    }

    @Override
    public SpdyStreamStatus status() {
        return this.status;
    }
}

