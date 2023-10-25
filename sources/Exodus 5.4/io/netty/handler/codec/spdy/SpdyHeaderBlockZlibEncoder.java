/*
 * Decompiled with CFR 0.152.
 */
package io.netty.handler.codec.spdy;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.handler.codec.spdy.SpdyCodecUtil;
import io.netty.handler.codec.spdy.SpdyHeaderBlockRawEncoder;
import io.netty.handler.codec.spdy.SpdyHeadersFrame;
import io.netty.handler.codec.spdy.SpdyVersion;
import java.util.zip.Deflater;

class SpdyHeaderBlockZlibEncoder
extends SpdyHeaderBlockRawEncoder {
    private final Deflater compressor;
    private boolean finished;

    SpdyHeaderBlockZlibEncoder(SpdyVersion spdyVersion, int n) {
        super(spdyVersion);
        if (n < 0 || n > 9) {
            throw new IllegalArgumentException("compressionLevel: " + n + " (expected: 0-9)");
        }
        this.compressor = new Deflater(n);
        this.compressor.setDictionary(SpdyCodecUtil.SPDY_DICT);
    }

    private boolean compressInto(ByteBuf byteBuf) {
        byte[] byArray = byteBuf.array();
        int n = byteBuf.arrayOffset() + byteBuf.writerIndex();
        int n2 = byteBuf.writableBytes();
        int n3 = this.compressor.deflate(byArray, n, n2, 2);
        byteBuf.writerIndex(byteBuf.writerIndex() + n3);
        return n3 == n2;
    }

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
        ByteBuf byteBuf2 = byteBuf.alloc().heapBuffer(byteBuf.readableBytes());
        int n = this.setInput(byteBuf);
        this.encode(byteBuf2);
        byteBuf.skipBytes(n);
        return byteBuf2;
    }

    private int setInput(ByteBuf byteBuf) {
        int n = byteBuf.readableBytes();
        if (byteBuf.hasArray()) {
            this.compressor.setInput(byteBuf.array(), byteBuf.arrayOffset() + byteBuf.readerIndex(), n);
        } else {
            byte[] byArray = new byte[n];
            byteBuf.getBytes(byteBuf.readerIndex(), byArray);
            this.compressor.setInput(byArray, 0, byArray.length);
        }
        return n;
    }

    private void encode(ByteBuf byteBuf) {
        while (this.compressInto(byteBuf)) {
            byteBuf.ensureWritable(byteBuf.capacity() << 1);
        }
    }

    @Override
    public void end() {
        if (this.finished) {
            return;
        }
        this.finished = true;
        this.compressor.end();
        super.end();
    }
}

