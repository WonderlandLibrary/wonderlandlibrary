/*
 * Decompiled with CFR 0.152.
 */
package org.apache.commons.io.output;

import java.io.IOException;
import java.io.OutputStream;

public class ClosedOutputStream
extends OutputStream {
    public static final ClosedOutputStream CLOSED_OUTPUT_STREAM = new ClosedOutputStream();

    @Override
    public void write(int b2) throws IOException {
        throw new IOException("write(" + b2 + ") failed: stream is closed");
    }

    @Override
    public void flush() throws IOException {
        throw new IOException("flush() failed: stream is closed");
    }
}

