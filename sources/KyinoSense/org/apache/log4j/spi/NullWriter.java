/*
 * Decompiled with CFR 0.152.
 */
package org.apache.log4j.spi;

import java.io.Writer;

class NullWriter
extends Writer {
    NullWriter() {
    }

    public void close() {
    }

    public void flush() {
    }

    public void write(char[] cbuf, int off, int len) {
    }
}

