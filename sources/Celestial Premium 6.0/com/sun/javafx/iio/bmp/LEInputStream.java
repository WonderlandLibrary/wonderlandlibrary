/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.iio.bmp;

import com.sun.javafx.iio.common.ImageTools;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;

final class LEInputStream {
    public final InputStream in;

    LEInputStream(InputStream inputStream) {
        this.in = inputStream;
    }

    public final short readShort() throws IOException {
        int n;
        int n2 = this.in.read();
        if ((n2 | (n = this.in.read())) < 0) {
            throw new EOFException();
        }
        return (short)((n << 8) + n2);
    }

    public final int readInt() throws IOException {
        int n;
        int n2;
        int n3;
        int n4 = this.in.read();
        if ((n4 | (n3 = this.in.read()) | (n2 = this.in.read()) | (n = this.in.read())) < 0) {
            throw new EOFException();
        }
        return (n << 24) + (n2 << 16) + (n3 << 8) + n4;
    }

    public final void skipBytes(int n) throws IOException {
        ImageTools.skipFully(this.in, n);
    }
}

