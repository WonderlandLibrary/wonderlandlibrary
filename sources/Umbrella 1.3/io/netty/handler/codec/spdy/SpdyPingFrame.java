/*
 * Decompiled with CFR 0.150.
 */
package io.netty.handler.codec.spdy;

import io.netty.handler.codec.spdy.SpdyFrame;

public interface SpdyPingFrame
extends SpdyFrame {
    public int getId();

    public SpdyPingFrame setId(int var1);
}

