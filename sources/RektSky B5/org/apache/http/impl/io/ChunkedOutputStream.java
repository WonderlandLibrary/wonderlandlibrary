/*
 * Decompiled with CFR 0.152.
 */
package org.apache.http.impl.io;

import java.io.IOException;
import java.io.OutputStream;
import org.apache.http.io.SessionOutputBuffer;

public class ChunkedOutputStream
extends OutputStream {
    private final SessionOutputBuffer out;
    private final byte[] cache;
    private int cachePosition = 0;
    private boolean wroteLastChunk = false;
    private boolean closed = false;

    @Deprecated
    public ChunkedOutputStream(SessionOutputBuffer out, int bufferSize) throws IOException {
        this(bufferSize, out);
    }

    @Deprecated
    public ChunkedOutputStream(SessionOutputBuffer out) throws IOException {
        this(2048, out);
    }

    public ChunkedOutputStream(int bufferSize, SessionOutputBuffer out) {
        this.cache = new byte[bufferSize];
        this.out = out;
    }

    protected void flushCache() throws IOException {
        if (this.cachePosition > 0) {
            this.out.writeLine(Integer.toHexString(this.cachePosition));
            this.out.write(this.cache, 0, this.cachePosition);
            this.out.writeLine("");
            this.cachePosition = 0;
        }
    }

    protected void flushCacheWithAppend(byte[] bufferToAppend, int off, int len) throws IOException {
        this.out.writeLine(Integer.toHexString(this.cachePosition + len));
        this.out.write(this.cache, 0, this.cachePosition);
        this.out.write(bufferToAppend, off, len);
        this.out.writeLine("");
        this.cachePosition = 0;
    }

    protected void writeClosingChunk() throws IOException {
        this.out.writeLine("0");
        this.out.writeLine("");
    }

    public void finish() throws IOException {
        if (!this.wroteLastChunk) {
            this.flushCache();
            this.writeClosingChunk();
            this.wroteLastChunk = true;
        }
    }

    @Override
    public void write(int b2) throws IOException {
        if (this.closed) {
            throw new IOException("Attempted write to closed stream.");
        }
        this.cache[this.cachePosition] = (byte)b2;
        ++this.cachePosition;
        if (this.cachePosition == this.cache.length) {
            this.flushCache();
        }
    }

    @Override
    public void write(byte[] b2) throws IOException {
        this.write(b2, 0, b2.length);
    }

    @Override
    public void write(byte[] src, int off, int len) throws IOException {
        if (this.closed) {
            throw new IOException("Attempted write to closed stream.");
        }
        if (len >= this.cache.length - this.cachePosition) {
            this.flushCacheWithAppend(src, off, len);
        } else {
            System.arraycopy(src, off, this.cache, this.cachePosition, len);
            this.cachePosition += len;
        }
    }

    @Override
    public void flush() throws IOException {
        this.flushCache();
        this.out.flush();
    }

    @Override
    public void close() throws IOException {
        if (!this.closed) {
            this.closed = true;
            this.finish();
            this.out.flush();
        }
    }
}

