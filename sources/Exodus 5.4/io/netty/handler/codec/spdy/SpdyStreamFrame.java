/*
 * Decompiled with CFR 0.152.
 */
package io.netty.handler.codec.spdy;

import io.netty.handler.codec.spdy.SpdyFrame;

public interface SpdyStreamFrame
extends SpdyFrame {
    public SpdyStreamFrame setLast(boolean var1);

    public SpdyStreamFrame setStreamId(int var1);

    public boolean isLast();

    public int streamId();
}

