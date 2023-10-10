/*
 * Decompiled with CFR 0.152.
 */
package org.apache.http.impl.io;

import java.io.IOException;
import java.io.OutputStream;
import org.apache.http.io.SessionOutputBuffer;
import org.apache.http.util.Args;

public class IdentityOutputStream
extends OutputStream {
    private final SessionOutputBuffer out;
    private boolean closed = false;

    public IdentityOutputStream(SessionOutputBuffer out) {
        this.out = Args.notNull(out, "Session output buffer");
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
        this.out.write(b2, off, len);
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
        this.out.write(b2);
    }
}

