/*
 * Decompiled with CFR 0.152.
 */
package org.apache.http.impl.io;

import java.io.IOException;
import java.io.OutputStream;
import org.apache.http.io.SessionOutputBuffer;
import org.apache.http.util.Args;

public class ContentLengthOutputStream
extends OutputStream {
    private final SessionOutputBuffer out;
    private final long contentLength;
    private long total = 0L;
    private boolean closed = false;

    public ContentLengthOutputStream(SessionOutputBuffer out, long contentLength) {
        this.out = Args.notNull(out, "Session output buffer");
        this.contentLength = Args.notNegative(contentLength, "Content length");
    }

    @Override
    public void close() throws IOException {
        if (!this.closed) {
            this.closed = true;
            this.out.flush();
        }
    }

    @Override
    public void flush() throws IOException {
        this.out.flush();
    }

    @Override
    public void write(byte[] b2, int off, int len) throws IOException {
        if (this.closed) {
            throw new IOException("Attempted write to closed stream.");
        }
        if (this.total < this.contentLength) {
            int chunk = len;
            long max = this.contentLength - this.total;
            if ((long)chunk > max) {
                chunk = (int)max;
            }
            this.out.write(b2, off, chunk);
            this.total += (long)chunk;
        }
    }

    @Override
    public void write(byte[] b2) throws IOException {
        this.write(b2, 0, b2.length);
    }

    @Override
    public void write(int b2) throws IOException {
        if (this.closed) {
            throw new IOException("Attempted write to closed stream.");
        }
        if (this.total < this.contentLength) {
            this.out.write(b2);
            ++this.total;
        }
    }
}

