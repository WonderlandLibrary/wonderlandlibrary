/*
 * Decompiled with CFR 0.152.
 */
package io.netty.handler.codec.spdy;

import io.netty.handler.codec.spdy.SpdyHeaders;
import io.netty.handler.codec.spdy.SpdyStreamFrame;

public interface SpdyHeadersFrame
extends SpdyStreamFrame {
    public boolean isTruncated();

    public SpdyHeadersFrame setInvalid();

    @Override
    public SpdyHeadersFrame setLast(boolean var1);

    public SpdyHeaders headers();

    public boolean isInvalid();

    @Override
    public SpdyHeadersFrame setStreamId(int var1);

    public SpdyHeadersFrame setTruncated();
}

