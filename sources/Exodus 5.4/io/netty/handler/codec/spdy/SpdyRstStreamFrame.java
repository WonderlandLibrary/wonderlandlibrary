/*
 * Decompiled with CFR 0.152.
 */
package io.netty.handler.codec.spdy;

import io.netty.handler.codec.spdy.SpdyStreamFrame;
import io.netty.handler.codec.spdy.SpdyStreamStatus;

public interface SpdyRstStreamFrame
extends SpdyStreamFrame {
    @Override
    public SpdyRstStreamFrame setStreamId(int var1);

    public SpdyRstStreamFrame setStatus(SpdyStreamStatus var1);

    public SpdyStreamStatus status();

    @Override
    public SpdyRstStreamFrame setLast(boolean var1);
}

