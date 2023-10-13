/*
 * Decompiled with CFR 0.150.
 */
package io.netty.handler.codec.spdy;

import io.netty.buffer.ByteBuf;
import io.netty.handler.codec.spdy.SpdyHeaderBlockZlibDecoder;
import io.netty.handler.codec.spdy.SpdyHeadersFrame;
import io.netty.handler.codec.spdy.SpdyVersion;

abstract class SpdyHeaderBlockDecoder {
    SpdyHeaderBlockDecoder() {
    }

    static SpdyHeaderBlockDecoder newInstance(SpdyVersion version, int maxHeaderSize) {
        return new SpdyHeaderBlockZlibDecoder(version, maxHeaderSize);
    }

    abstract void decode(ByteBuf var1, SpdyHeadersFrame var2) throws Exception;

    abstract void reset();

    abstract void end();
}

