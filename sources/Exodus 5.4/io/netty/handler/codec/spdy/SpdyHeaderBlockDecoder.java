/*
 * Decompiled with CFR 0.152.
 */
package io.netty.handler.codec.spdy;

import io.netty.buffer.ByteBuf;
import io.netty.handler.codec.spdy.SpdyHeaderBlockZlibDecoder;
import io.netty.handler.codec.spdy.SpdyHeadersFrame;
import io.netty.handler.codec.spdy.SpdyVersion;

abstract class SpdyHeaderBlockDecoder {
    abstract void decode(ByteBuf var1, SpdyHeadersFrame var2) throws Exception;

    static SpdyHeaderBlockDecoder newInstance(SpdyVersion spdyVersion, int n) {
        return new SpdyHeaderBlockZlibDecoder(spdyVersion, n);
    }

    abstract void endHeaderBlock(SpdyHeadersFrame var1) throws Exception;

    SpdyHeaderBlockDecoder() {
    }

    abstract void end();
}

