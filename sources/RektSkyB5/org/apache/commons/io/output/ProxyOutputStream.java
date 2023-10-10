/*
 * Decompiled with CFR 0.152.
 */
package org.apache.commons.io.output;

import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class ProxyOutputStream
extends FilterOutputStream {
    public ProxyOutputStream(OutputStream proxy) {
        super(proxy);
    }

    @Override
    public void write(int idx) throws IOException {
        try {
            this.beforeWrite(1);
            this.out.write(idx);
            this.afterWrite(1);
        }
        catch (IOException e2) {
            this.handleIOException(e2);
        }
    }

    @Override
    public void write(byte[] bts) throws IOException {
        try {
            int len = bts != null ? bts.length : 0;
            this.beforeWrite(len);
            this.out.write(bts);
            this.afterWrite(len);
        }
        catch (IOException e2) {
            this.handleIOException(e2);
        }
    }

    @Override
    public void write(byte[] bts, int st, int end) throws IOException {
        try {
            this.beforeWrite(end);
            this.out.write(bts, st, end);
            this.afterWrite(end);
        }
        catch (IOException e2) {
            this.handleIOException(e2);
        }
    }

    @Override
    public void flush() throws IOException {
        try {
            this.out.flush();
        }
        catch (IOException e2) {
            this.handleIOException(e2);
        }
    }

    @Override
    public void close() throws IOException {
        try {
            this.out.close();
        }
        catch (IOException e2) {
            this.handleIOException(e2);
        }
    }

    protected void beforeWrite(int n2) throws IOException {
    }

    protected void afterWrite(int n2) throws IOException {
    }

    protected void handleIOException(IOException e2) throws IOException {
        throw e2;
    }
}

