/*
 * Decompiled with CFR 0.152.
 */
package io.netty.handler.codec.http;

import io.netty.channel.embedded.EmbeddedChannel;
import io.netty.handler.codec.compression.ZlibCodecFactory;
import io.netty.handler.codec.compression.ZlibWrapper;
import io.netty.handler.codec.http.HttpContentDecoder;

public class HttpContentDecompressor
extends HttpContentDecoder {
    private final boolean strict;

    public HttpContentDecompressor() {
        this(false);
    }

    public HttpContentDecompressor(boolean bl) {
        this.strict = bl;
    }

    @Override
    protected EmbeddedChannel newContentDecoder(String string) throws Exception {
        if ("gzip".equalsIgnoreCase(string) || "x-gzip".equalsIgnoreCase(string)) {
            return new EmbeddedChannel(ZlibCodecFactory.newZlibDecoder(ZlibWrapper.GZIP));
        }
        if ("deflate".equalsIgnoreCase(string) || "x-deflate".equalsIgnoreCase(string)) {
            ZlibWrapper zlibWrapper = this.strict ? ZlibWrapper.ZLIB : ZlibWrapper.ZLIB_OR_NONE;
            return new EmbeddedChannel(ZlibCodecFactory.newZlibDecoder(zlibWrapper));
        }
        return null;
    }
}

