/*
 * Decompiled with CFR 0.150.
 */
package org.apache.commons.compress.compressors.snappy;

import java.io.IOException;
import java.io.InputStream;
import org.apache.commons.compress.compressors.CompressorInputStream;
import org.apache.commons.compress.utils.IOUtils;

public class SnappyCompressorInputStream
extends CompressorInputStream {
    private static final int TAG_MASK = 3;
    public static final int DEFAULT_BLOCK_SIZE = 32768;
    private final byte[] decompressBuf;
    private int writeIndex;
    private int readIndex;
    private final int blockSize;
    private final InputStream in;
    private final int size;
    private int uncompressedBytesRemaining;
    private final byte[] oneByte = new byte[1];
    private boolean endReached = false;

    public SnappyCompressorInputStream(InputStream is) throws IOException {
        this(is, 32768);
    }

    public SnappyCompressorInputStream(InputStream is, int blockSize) throws IOException {
        this.in = is;
        this.blockSize = blockSize;
        this.decompressBuf = new byte[blockSize * 3];
        this.readIndex = 0;
        this.writeIndex = 0;
        this.uncompressedBytesRemaining = this.size = (int)this.readSize();
    }

    public int read() throws IOException {
        return this.read(this.oneByte, 0, 1) == -1 ? -1 : this.oneByte[0] & 0xFF;
    }

    public void close() throws IOException {
        this.in.close();
    }

    public int available() {
        return this.writeIndex - this.readIndex;
    }

    public int read(byte[] b, int off, int len) throws IOException {
        if (this.endReached) {
            return -1;
        }
        int avail = this.available();
        if (len > avail) {
            this.fill(len - avail);
        }
        int readable = Math.min(len, this.available());
        System.arraycopy(this.decompressBuf, this.readIndex, b, off, readable);
        this.readIndex += readable;
        if (this.readIndex > this.blockSize) {
            this.slideBuffer();
        }
        return readable;
    }

    private void fill(int len) throws IOException {
        if (this.uncompressedBytesRemaining == 0) {
            this.endReached = true;
        }
        int readNow = Math.min(len, this.uncompressedBytesRemaining);
        while (readNow > 0) {
            int b = this.readOneByte();
            int length = 0;
            long offset = 0L;
            switch (b & 3) {
                case 0: {
                    length = this.readLiteralLength(b);
                    if (!this.expandLiteral(length)) break;
                    return;
                }
                case 1: {
                    length = 4 + (b >> 2 & 7);
                    offset = (b & 0xE0) << 3;
                    if (!this.expandCopy(offset |= (long)this.readOneByte(), length)) break;
                    return;
                }
                case 2: {
                    length = (b >> 2) + 1;
                    offset = this.readOneByte();
                    if (!this.expandCopy(offset |= (long)(this.readOneByte() << 8), length)) break;
                    return;
                }
                case 3: {
                    length = (b >> 2) + 1;
                    offset = this.readOneByte();
                    offset |= (long)(this.readOneByte() << 8);
                    offset |= (long)(this.readOneByte() << 16);
                    if (!this.expandCopy(offset |= (long)this.readOneByte() << 24, length)) break;
                    return;
                }
            }
            readNow -= length;
            this.uncompressedBytesRemaining -= length;
        }
    }

    private void slideBuffer() {
        System.arraycopy(this.decompressBuf, this.blockSize, this.decompressBuf, 0, this.blockSize * 2);
        this.writeIndex -= this.blockSize;
        this.readIndex -= this.blockSize;
    }

    private int readLiteralLength(int b) throws IOException {
        int length;
        switch (b >> 2) {
            case 60: {
                length = this.readOneByte();
                break;
            }
            case 61: {
                length = this.readOneByte();
                length |= this.readOneByte() << 8;
                break;
            }
            case 62: {
                length = this.readOneByte();
                length |= this.readOneByte() << 8;
                length |= this.readOneByte() << 16;
                break;
            }
            case 63: {
                length = this.readOneByte();
                length |= this.readOneByte() << 8;
                length |= this.readOneByte() << 16;
                length = (int)((long)length | (long)this.readOneByte() << 24);
                break;
            }
            default: {
                length = b >> 2;
            }
        }
        return length + 1;
    }

    private boolean expandLiteral(int length) throws IOException {
        int bytesRead = IOUtils.readFully(this.in, this.decompressBuf, this.writeIndex, length);
        this.count(bytesRead);
        if (length != bytesRead) {
            throw new IOException("Premature end of stream");
        }
        this.writeIndex += length;
        return this.writeIndex >= 2 * this.blockSize;
    }

    private boolean expandCopy(long off, int length) throws IOException {
        if (off > (long)this.blockSize) {
            throw new IOException("Offset is larger than block size");
        }
        int offset = (int)off;
        if (offset == 1) {
            byte lastChar = this.decompressBuf[this.writeIndex - 1];
            for (int i = 0; i < length; ++i) {
                this.decompressBuf[this.writeIndex++] = lastChar;
            }
        } else if (length < offset) {
            System.arraycopy(this.decompressBuf, this.writeIndex - offset, this.decompressBuf, this.writeIndex, length);
            this.writeIndex += length;
        } else {
            int fullRotations = length / offset;
            int pad = length - offset * fullRotations;
            while (fullRotations-- != 0) {
                System.arraycopy(this.decompressBuf, this.writeIndex - offset, this.decompressBuf, this.writeIndex, offset);
                this.writeIndex += offset;
            }
            if (pad > 0) {
                System.arraycopy(this.decompressBuf, this.writeIndex - offset, this.decompressBuf, this.writeIndex, pad);
                this.writeIndex += pad;
            }
        }
        return this.writeIndex >= 2 * this.blockSize;
    }

    private int readOneByte() throws IOException {
        int b = this.in.read();
        if (b == -1) {
            throw new IOException("Premature end of stream");
        }
        this.count(1);
        return b & 0xFF;
    }

    private long readSize() throws IOException {
        int index = 0;
        long sz = 0L;
        int b = 0;
        do {
            b = this.readOneByte();
            sz |= (long)((b & 0x7F) << index++ * 7);
        } while (0 != (b & 0x80));
        return sz;
    }

    public int getSize() {
        return this.size;
    }
}

