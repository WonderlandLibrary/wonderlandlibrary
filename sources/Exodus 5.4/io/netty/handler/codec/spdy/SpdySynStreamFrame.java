/*
 * Decompiled with CFR 0.152.
 */
package io.netty.handler.codec.spdy;

import io.netty.handler.codec.spdy.SpdyHeadersFrame;

public interface SpdySynStreamFrame
extends SpdyHeadersFrame {
    public int associatedStreamId();

    public SpdySynStreamFrame setUnidirectional(boolean var1);

    @Override
    public SpdySynStreamFrame setStreamId(int var1);

    @Override
    public SpdySynStreamFrame setInvalid();

    public boolean isUnidirectional();

    public SpdySynStreamFrame setPriority(byte var1);

    public SpdySynStreamFrame setAssociatedStreamId(int var1);

    public byte priority();

    @Override
    public SpdySynStreamFrame setLast(boolean var1);
}

