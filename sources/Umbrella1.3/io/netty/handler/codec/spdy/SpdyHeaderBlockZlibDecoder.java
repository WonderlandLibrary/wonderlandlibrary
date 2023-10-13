/*
 * Decompiled with CFR 0.150.
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
    private static final int DEFAULT_BUFFER_CAPACITY = 4096;
    private final Inflater decompressor = new Inflater();
    private ByteBuf decompressed;

    SpdyHeaderBlockZlibDecoder(SpdyVersion spdyVersion, int maxHeaderSize) {
        super(spdyVersion, maxHeaderSize);
    }

    @Override
    void decode(ByteBuf encoded, SpdyHeadersFrame frame) throws Exception {
        int numBytes;
        int len = this.setInput(encoded);
        while ((numBytes = this.decompress(encoded.alloc(), frame)) > 0) {
        }
        if (this.decompressor.getRemaining() != 0) {
            throw new SpdyProtocolException("client sent extra data beyond headers");
        }
        encoded.skipBytes(len);
    }

    private int setInput(ByteBuf compressed) {
        int len = compressed.readableBytes();
        if (compressed.hasArray()) {
            this.decompressor.setInput(compressed.array(), compressed.arrayOffset() + compressed.readerIndex(), len);
        } else {
            byte[] in = new byte[len];
            compressed.getBytes(compressed.readerIndex(), in);
            this.decompressor.setInput(in, 0, in.length);
        }
        return len;
    }

    private int decompress(ByteBufAllocator alloc, SpdyHeadersFrame frame) throws Exception {
        this.ensureBuffer(alloc);
        byte[] out = this.decompressed.array();
        int off = this.decompressed.arrayOffset() + this.decompressed.writerIndex();
        try {
            int numBytes = this.decompressor.inflate(out, off, this.decompressed.writableBytes());
            if (numBytes == 0 && this.decompressor.needsDictionary()) {
                this.decompressor.setDictionary(SpdyCodecUtil.SPDY_DICT);
                numBytes = this.decompressor.inflate(out, off, this.decompressed.writableBytes());
            }
            if (frame != null) {
                this.decompressed.writerIndex(this.decompressed.writerIndex() + numBytes);
                super.decode(this.decompressed, frame);
                this.decompressed.discardReadBytes();
            }
            return numBytes;
        }
        catch (DataFormatException e) {
            throw new SpdyProtocolException("Received invalid header block", e);
        }
    }

    private void ensureBuffer(ByteBufAllocator alloc) {
        if (this.decompressed == null) {
            this.decompressed = alloc.heapBuffer(4096);
        }
        this.decompressed.ensureWritable(1);
    }

    @Override
    void reset() {
        this.releaseBuffer();
        super.reset();
    }

    @Override
    public void end() {
        this.releaseBuffer();
        this.decompressor.end();
        super.end();
    }

    private void releaseBuffer() {
        if (this.decompressed != null) {
            this.decompressed.release();
            this.decompressed = null;
        }
    }
}

