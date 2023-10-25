/*
 * Decompiled with CFR 0.150.
 */
package org.apache.logging.log4j.core.util;

import java.io.IOException;
import java.io.OutputStream;

public class CloseShieldOutputStream
extends OutputStream {
    private final OutputStream delegate;

    public CloseShieldOutputStream(OutputStream delegate) {
        this.delegate = delegate;
    }

    @Override
    public void close() {
    }

    @Override
    public void flush() throws IOException {
        this.delegate.flush();
    }

    @Override
    public void write(byte[] b) throws IOException {
        this.delegate.write(b);
    }

    @Override
    public void write(byte[] b, int off, int len) throws IOException {
        this.delegate.write(b, off, len);
    }

    @Override
    public void write(int b) throws IOException {
        this.delegate.write(b);
    }
}

