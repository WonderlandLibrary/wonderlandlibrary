/*
 * Decompiled with CFR 0.150.
 */
package io.netty.handler.codec.spdy;

import io.netty.buffer.ByteBuf;
import io.netty.handler.codec.spdy.SpdyCodecUtil;
import io.netty.handler.codec.spdy.SpdyHeaderBlockDecoder;
import io.netty.handler.codec.spdy.SpdyHeadersFrame;
import io.netty.handler.codec.spdy.SpdyVersion;

public class SpdyHeaderBlockRawDecoder
extends SpdyHeaderBlockDecoder {
    private static final int LENGTH_FIELD_SIZE = 4;
    private final int version;
    private final int maxHeaderSize;
    private int headerSize;
    private int numHeaders = -1;

    public SpdyHeaderBlockRawDecoder(SpdyVersion spdyVersion, int maxHeaderSize) {
        if (spdyVersion == null) {
            throw new NullPointerException("spdyVersion");
        }
        this.version = spdyVersion.getVersion();
        this.maxHeaderSize = maxHeaderSize;
    }

    private static int readLengthField(ByteBuf buffer) {
        int length = SpdyCodecUtil.getSignedInt(buffer, buffer.readerIndex());
        buffer.skipBytes(4);
        return length;
    }

    @Override
    void decode(ByteBuf encoded, SpdyHeadersFrame frame) throws Exception {
        if (encoded == null) {
            throw new NullPointerException("encoded");
        }
        if (frame == null) {
            throw new NullPointerException("frame");
        }
        if (this.numHeaders == -1) {
            if (encoded.readableBytes() < 4) {
                return;
            }
            this.numHeaders = SpdyHeaderBlockRawDecoder.readLengthField(encoded);
            if (this.numHeaders < 0) {
                frame.setInvalid();
                return;
            }
        }
        while (this.numHeaders > 0) {
            int headerSize = this.headerSize;
            encoded.markReaderIndex();
            if (encoded.readableBytes() < 4) {
                encoded.resetReaderIndex();
                return;
            }
            int nameLength = SpdyHeaderBlockRawDecoder.readLengthField(encoded);
            if (nameLength <= 0) {
                frame.setInvalid();
                return;
            }
            if ((headerSize += nameLength) > this.maxHeaderSize) {
                frame.setTruncated();
                return;
            }
            if (encoded.readableBytes() < nameLength) {
                encoded.resetReaderIndex();
                return;
            }
            byte[] nameBytes = new byte[nameLength];
            encoded.readBytes(nameBytes);
            String name = new String(nameBytes, "UTF-8");
            if (frame.headers().contains(name)) {
                frame.setInvalid();
                return;
            }
            if (encoded.readableBytes() < 4) {
                encoded.resetReaderIndex();
                return;
            }
            int valueLength = SpdyHeaderBlockRawDecoder.readLengthField(encoded);
            if (valueLength < 0) {
                frame.setInvalid();
                return;
            }
            if (valueLength == 0) {
                frame.headers().add(name, "");
                --this.numHeaders;
                this.headerSize = headerSize;
                continue;
            }
            if ((headerSize += valueLength) > this.maxHeaderSize) {
                frame.setTruncated();
                return;
            }
            if (encoded.readableBytes() < valueLength) {
                encoded.resetReaderIndex();
                return;
            }
            byte[] valueBytes = new byte[valueLength];
            encoded.readBytes(valueBytes);
            int index = 0;
            int offset = 0;
            while (index < valueLength) {
                while (index < valueBytes.length && valueBytes[index] != 0) {
                    ++index;
                }
                if (index < valueBytes.length && valueBytes[index + 1] == 0) {
                    frame.setInvalid();
                    return;
                }
                String value = new String(valueBytes, offset, index - offset, "UTF-8");
                try {
                    frame.headers().add(name, value);
                }
                catch (IllegalArgumentException e) {
                    frame.setInvalid();
                    return;
                }
                offset = ++index;
            }
            --this.numHeaders;
            this.headerSize = headerSize;
        }
    }

    @Override
    void reset() {
        this.headerSize = 0;
        this.numHeaders = -1;
    }

    @Override
    void end() {
    }
}

