/*
 * Decompiled with CFR 0.152.
 */
package org.apache.http.impl.io;

import java.io.IOException;
import java.io.InputStream;
import org.apache.http.io.BufferInfo;
import org.apache.http.io.SessionInputBuffer;
import org.apache.http.util.Args;

public class IdentityInputStream
extends InputStream {
    private final SessionInputBuffer in;
    private boolean closed = false;

    public IdentityInputStream(SessionInputBuffer in) {
        this.in = Args.notNull(in, "Session input buffer");
    }

    @Override
    public int available() throws IOException {
        if (this.in instanceof BufferInfo) {
            return ((BufferInfo)((Object)this.in)).length();
        }
        return 0;
    }

    @Override
    public void close() throws IOException {
        this.closed = true;
    }

    @Override
    public int read() throws IOException {
        if (this.closed) {
            return -1;
        }
        return this.in.read();
    }

    @Override
    public int read(byte[] b2, int off, int len) throws IOException {
        if (this.closed) {
            return -1;
        }
        return this.in.read(b2, off, len);
    }
}

