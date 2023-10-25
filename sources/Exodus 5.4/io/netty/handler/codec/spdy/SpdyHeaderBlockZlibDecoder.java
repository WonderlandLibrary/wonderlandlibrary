/*
 * Decompiled with CFR 0.152.
 */
package io.netty.handler.codec.spdy;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.handler.codec.spdy.SpdyCodecUtil;
import io.netty.handler.codec.spdy.SpdyHeaderBlockRawDecoder;
import io.netty.handler.codec.spdy.SpdyHeadersFrame;
import io.netty.handler.codec.spdy.SpdyProtocolException;
import io.netty.handler.codec.spdy.SpdyVersion;
import java.util.zip.DataFormatException;
import java.util.zip.Inflater;

final class SpdyHeaderBlockZlibDecoder
extends SpdyHeaderBlockRawDecoder {
    private static final SpdyProtocolException INVALID_HEADER_BLOCK = new SpdyProtocolException("Invalid Header Block");
    private ByteBuf decompressed;
    private static final int DEFAULT_BUFFER_CAPACITY = 4096;
    private final Inflater decompressor = new Inflater();

    private void ensureBuffer(ByteBufAllocator byteBufAllocator) {
        if (this.decompressed == null) {
            this.decompressed = byteBufAllocator.heapBuffer(4096);
        }
        this.decompressed.ensureWritable(1);
    }

    private int setInput(ByteBuf byteBuf) {
        int n = byteBuf.readableBytes();
        if (byteBuf.hasArray()) {
            this.decompressor.setInput(byteBuf.array(), byteBuf.arrayOffset() + byteBuf.readerIndex(), n);
        } else {
            byte[] byArray = new byte[n];
            byteBuf.getBytes(byteBuf.readerIndex(), byArray);
            this.decompressor.setInput(byArray, 0, byArray.length);
        }
        return n;
    }

    @Override
    void decode(ByteBuf byteBuf, SpdyHeadersFrame spdyHeadersFrame) throws Exception {
        int n;
        int n2 = this.setInput(byteBuf);
        while ((n = this.decompress(byteBuf.alloc(), spdyHeadersFrame)) > 0) {
        }
        if (this.decompressor.getRemaining() != 0) {
            throw INVALID_HEADER_BLOCK;
        }
        byteBuf.skipBytes(n2);
    }

    @Override
    public void end() {
        super.end();
        this.releaseBuffer();
        this.decompressor.end();
    }

    private int decompress(ByteBufAllocator byteBufAllocator, SpdyHeadersFrame spdyHeadersFrame) throws Exception {
        this.ensureBuffer(byteBufAllocator);
        byte[] byArray = this.decompressed.array();
        int n = this.decompressed.arrayOffset() + this.decompressed.writerIndex();
        try {
            int n2 = this.decompressor.inflate(byArray, n, this.decompressed.writableBytes());
            if (n2 == 0 && this.decompressor.needsDictionary()) {
                try {
                    this.decompressor.setDictionary(SpdyCodecUtil.SPDY_DICT);
                }
                catch (IllegalArgumentException illegalArgumentException) {
                    throw INVALID_HEADER_BLOCK;
                }
                n2 = this.decompressor.inflate(byArray, n, this.decompressed.writableBytes());
            }
            if (spdyHeadersFrame != null) {
                this.decompressed.writerIndex(this.decompressed.writerIndex() + n2);
                this.decodeHeaderBlock(this.decompressed, spdyHeadersFrame);
                this.decompressed.discardReadBytes();
            }
            return n2;
        }
        catch (DataFormatException dataFormatException) {
            throw new SpdyProtocolException("Received invalid header block", dataFormatException);
        }
    }

    SpdyHeaderBlockZlibDecoder(SpdyVersion spdyVersion, int n) {
        super(spdyVersion, n);
    }

    private void releaseBuffer() {
        if (this.decompressed != null) {
            this.decompressed.release();
            this.decompressed = null;
        }
    }

    @Override
    void endHeaderBlock(SpdyHeadersFrame spdyHeadersFrame) throws Exception {
        super.endHeaderBlock(spdyHeadersFrame);
        this.releaseBuffer();
    }
}

