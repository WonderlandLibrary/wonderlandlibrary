/*
 * Decompiled with CFR 0.150.
 */
package io.netty.handler.codec.spdy;

import io.netty.handler.codec.spdy.SpdyFrame;

public interface SpdyWindowUpdateFrame
extends SpdyFrame {
    public int getStreamId();

    public SpdyWindowUpdateFrame setStreamId(int var1);

    public int getDeltaWindowSize();

    public SpdyWindowUpdateFrame setDeltaWindowSize(int var1);
}

