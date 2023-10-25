/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.jcraft.jzlib.Deflater
 *  com.jcraft.jzlib.JZlib
 */
package io.netty.handler.codec.spdy;

import com.jcraft.jzlib.Deflater;
import com.jcraft.jzlib.JZlib;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.handler.codec.compression.CompressionException;
import io.netty.handler.codec.spdy.SpdyCodecUtil;
import io.netty.handler.codec.spdy.SpdyHeaderBlockRawEncoder;
import io.netty.handler.codec.spdy.SpdyHeadersFrame;
import io.netty.handler.codec.spdy.SpdyVersion;

class SpdyHeaderBlockJZlibEncoder
extends SpdyHeaderBlockRawEncoder {
    private boolean finished;
    private final Deflater z = new Deflater();

    @Override
    public ByteBuf encode(SpdyHeadersFrame spdyHeadersFrame) throws Exception {
        if (spdyHeadersFrame == null) {
            throw new IllegalArgumentException("frame");
        }
        if (this.finished) {
            return Unpooled.EMPTY_BUFFER;
        }
        ByteBuf byteBuf = super.encode(spdyHeadersFrame);
        if (byteBuf.readableBytes() == 0) {
            return Unpooled.EMPTY_BUFFER;
        }
        ByteBuf byteBuf2 = byteBuf.alloc().buffer();
        this.setInput(byteBuf);
        this.encode(byteBuf2);
        return byteBuf2;
    }

    private void setInput(ByteBuf byteBuf) {
        byte[] byArray = new byte[byteBuf.readableBytes()];
        byteBuf.readBytes(byArray);
        this.z.next_in = byArray;
        this.z.next_in_index = 0;
        this.z.avail_in = byArray.length;
    }

    SpdyHeaderBlockJZlibEncoder(SpdyVersion spdyVersion, int n, int n2, int n3) {
        super(spdyVersion);
        if (n < 0 || n > 9) {
            throw new IllegalArgumentException("compressionLevel: " + n + " (expected: 0-9)");
        }
        if (n2 < 9 || n2 > 15) {
            throw new IllegalArgumentException("windowBits: " + n2 + " (expected: 9-15)");
        }
        if (n3 < 1 || n3 > 9) {
            throw new IllegalArgumentException("memLevel: " + n3 + " (expected: 1-9)");
        }
        int n4 = this.z.deflateInit(n, n2, n3, JZlib.W_ZLIB);
        if (n4 != 0) {
            throw new CompressionException("failed to initialize an SPDY header block deflater: " + n4);
        }
        n4 = this.z.deflateSetDictionary(SpdyCodecUtil.SPDY_DICT, SpdyCodecUtil.SPDY_DICT.length);
        if (n4 != 0) {
            throw new CompressionException("failed to set the SPDY dictionary: " + n4);
        }
    }

    @Override
    public void end() {
        if (this.finished) {
            return;
        }
        this.finished = true;
        this.z.deflateEnd();
        this.z.next_in = null;
        this.z.next_out = null;
    }

    private void encode(ByteBuf byteBuf) {
        byte[] byArray = new byte[(int)Math.ceil((double)this.z.next_in.length * 1.001) + 12];
        this.z.next_out = byArray;
        this.z.next_out_index = 0;
        this.z.avail_out = byArray.length;
        int n = this.z.deflate(2);
        if (n != 0) {
            throw new CompressionException("compression failure: " + n);
        }
        if (this.z.next_out_index != 0) {
            byteBuf.writeBytes(byArray, 0, this.z.next_out_index);
        }
        this.z.next_in = null;
        this.z.next_out = null;
    }
}

