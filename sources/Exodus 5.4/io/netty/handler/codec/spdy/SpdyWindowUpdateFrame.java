/*
 * Decompiled with CFR 0.152.
 */
package io.netty.handler.codec.spdy;

import io.netty.handler.codec.spdy.SpdyFrame;

public interface SpdyWindowUpdateFrame
extends SpdyFrame {
    public SpdyWindowUpdateFrame setDeltaWindowSize(int var1);

    public int deltaWindowSize();

    public int streamId();

    public SpdyWindowUpdateFrame setStreamId(int var1);
}

