/*
 * Decompiled with CFR 0.150.
 */
package org.apache.logging.log4j.core.util;

import java.io.IOException;
import java.io.OutputStream;

public class NullOutputStream
extends OutputStream {
    private static final NullOutputStream INSTANCE;
    @Deprecated
    public static final NullOutputStream NULL_OUTPUT_STREAM;

    public static NullOutputStream getInstance() {
        return INSTANCE;
    }

    private NullOutputStream() {
    }

    @Override
    public void write(byte[] b, int off, int len) {
    }

    @Override
    public void write(int b) {
    }

    @Override
    public void write(byte[] b) throws IOException {
    }

    static {
        NULL_OUTPUT_STREAM = INSTANCE = new NullOutputStream();
    }
}

