/*
 * Decompiled with CFR 0.152.
 */
package org.apache.commons.io.output;

import java.io.IOException;
import java.io.OutputStream;

public class NullOutputStream
extends OutputStream {
    public static final NullOutputStream NULL_OUTPUT_STREAM = new NullOutputStream();

    @Override
    public void write(byte[] b2, int off, int len) {
    }

    @Override
    public void write(int b2) {
    }

    @Override
    public void write(byte[] b2) throws IOException {
    }
}

