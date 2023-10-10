/*
 * Decompiled with CFR 0.152.
 */
package org.apache.commons.io.input;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;

public abstract class ProxyInputStream
extends FilterInputStream {
    public ProxyInputStream(InputStream proxy) {
        super(proxy);
    }

    @Override
    public int read() throws IOException {
        try {
            this.beforeRead(1);
            int b2 = this.in.read();
            this.afterRead(b2 != -1 ? 1 : -1);
            return b2;
        }
        catch (IOException e2) {
            this.handleIOException(e2);
            return -1;
        }
    }

    @Override
    public int read(byte[] bts) throws IOException {
        try {
            this.beforeRead(bts != null ? bts.length : 0);
            int n2 = this.in.read(bts);
            this.afterRead(n2);
            return n2;
        }
        catch (IOException e2) {
            this.handleIOException(e2);
            return -1;
        }
    }

    @Override
    public int read(byte[] bts, int off, int len) throws IOException {
        try {
            this.beforeRead(len);
            int n2 = this.in.read(bts, off, len);
            this.afterRead(n2);
            return n2;
        }
        catch (IOException e2) {
            this.handleIOException(e2);
            return -1;
        }
    }

    @Override
    public long skip(long ln) throws IOException {
        try {
            return this.in.skip(ln);
        }
        catch (IOException e2) {
            this.handleIOException(e2);
            return 0L;
        }
    }

    @Override
    public int available() throws IOException {
        try {
            return super.available();
        }
        catch (IOException e2) {
            this.handleIOException(e2);
            return 0;
        }
    }

    @Override
    public void close() throws IOException {
        try {
            this.in.close();
        }
        catch (IOException e2) {
            this.handleIOException(e2);
        }
    }

    @Override
    public synchronized void mark(int readlimit) {
        this.in.mark(readlimit);
    }

    @Override
    public synchronized void reset() throws IOException {
        try {
            this.in.reset();
        }
        catch (IOException e2) {
            this.handleIOException(e2);
        }
    }

    @Override
    public boolean markSupported() {
        return this.in.markSupported();
    }

    protected void beforeRead(int n2) throws IOException {
    }

    protected void afterRead(int n2) throws IOException {
    }

    protected void handleIOException(IOException e2) throws IOException {
        throw e2;
    }
}

