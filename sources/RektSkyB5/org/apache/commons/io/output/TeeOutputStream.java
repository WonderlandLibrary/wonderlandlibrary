/*
 * Decompiled with CFR 0.152.
 */
package org.apache.commons.io.output;

import java.io.IOException;
import java.io.OutputStream;
import org.apache.commons.io.output.ProxyOutputStream;

public class TeeOutputStream
extends ProxyOutputStream {
    protected OutputStream branch;

    public TeeOutputStream(OutputStream out, OutputStream branch) {
        super(out);
        this.branch = branch;
    }

    @Override
    public synchronized void write(byte[] b2) throws IOException {
        super.write(b2);
        this.branch.write(b2);
    }

    @Override
    public synchronized void write(byte[] b2, int off, int len) throws IOException {
        super.write(b2, off, len);
        this.branch.write(b2, off, len);
    }

    @Override
    public synchronized void write(int b2) throws IOException {
        super.write(b2);
        this.branch.write(b2);
    }

    @Override
    public void flush() throws IOException {
        super.flush();
        this.branch.flush();
    }

    @Override
    public void close() throws IOException {
        try {
            super.close();
        }
        finally {
            this.branch.close();
        }
    }
}

