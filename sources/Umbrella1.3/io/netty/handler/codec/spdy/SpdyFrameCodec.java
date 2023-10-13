/*
 * Decompiled with CFR 0.150.
 */
package io.netty.handler.codec.spdy;

import io.netty.channel.CombinedChannelDuplexHandler;
import io.netty.handler.codec.spdy.SpdyFrameDecoder;
import io.netty.handler.codec.spdy.SpdyFrameEncoder;
import io.netty.handler.codec.spdy.SpdyVersion;

public final class SpdyFrameCodec
extends CombinedChannelDuplexHandler<SpdyFrameDecoder, SpdyFrameEncoder> {
    public SpdyFrameCodec(SpdyVersion version) {
        this(version, 8192, 16384, 6, 15, 8);
    }

    public SpdyFrameCodec(SpdyVersion version, int maxChunkSize, int maxHeaderSize, int compressionLevel, int windowBits, int memLevel) {
        super(new SpdyFrameDecoder(version, maxChunkSize, maxHeaderSize), new SpdyFrameEncoder(version, compressionLevel, windowBits, memLevel));
    }
}

