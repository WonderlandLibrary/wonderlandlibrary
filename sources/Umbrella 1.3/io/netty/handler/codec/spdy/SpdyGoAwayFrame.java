/*
 * Decompiled with CFR 0.150.
 */
package io.netty.handler.codec.spdy;

import io.netty.handler.codec.spdy.SpdyFrame;
import io.netty.handler.codec.spdy.SpdySessionStatus;

public interface SpdyGoAwayFrame
extends SpdyFrame {
    public int getLastGoodStreamId();

    public SpdyGoAwayFrame setLastGoodStreamId(int var1);

    public SpdySessionStatus getStatus();

    public SpdyGoAwayFrame setStatus(SpdySessionStatus var1);
}

