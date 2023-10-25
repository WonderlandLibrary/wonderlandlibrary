/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.iio.png;

import com.sun.javafx.iio.common.ImageTools;
import java.io.DataInputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;

public class PNGIDATChunkInputStream
extends InputStream {
    static final int IDAT_TYPE = 1229209940;
    private DataInputStream source;
    private int numBytesAvailable = 0;
    private boolean foundAllIDATChunks = false;
    private int nextChunkLength = 0;
    private int nextChunkType = 0;

    PNGIDATChunkInputStream(DataInputStream dataInputStream, int n) throws IOException {
        if (n < 0) {
            throw new IOException("Invalid chunk length");
        }
        this.source = dataInputStream;
        this.numBytesAvailable = n;
    }

    private void nextChunk() throws IOException {
        if (!this.foundAllIDATChunks) {
            ImageTools.skipFully(this.source, 4L);
            int n = this.source.readInt();
            if (n < 0) {
                throw new IOException("Invalid chunk length");
            }
            int n2 = this.source.readInt();
            if (n2 == 1229209940) {
                this.numBytesAvailable += n;
            } else {
                this.foundAllIDATChunks = true;
                this.nextChunkLength = n;
                this.nextChunkType = n2;
            }
        }
    }

    boolean isFoundAllIDATChunks() {
        return this.foundAllIDATChunks;
    }

    int getNextChunkLength() {
        return this.nextChunkLength;
    }

    int getNextChunkType() {
        return this.nextChunkType;
    }

    @Override
    public int read() throws IOException {
        if (this.numBytesAvailable == 0) {
            this.nextChunk();
        }
        if (this.numBytesAvailable == 0) {
            return -1;
        }
        --this.numBytesAvailable;
        return this.source.read();
    }

    @Override
    public int read(byte[] arrby, int n, int n2) throws IOException {
        if (this.numBytesAvailable == 0) {
            this.nextChunk();
            if (this.numBytesAvailable == 0) {
                return -1;
            }
        }
        int n3 = 0;
        while (this.numBytesAvailable > 0 && n2 > 0) {
            int n4 = n2 < this.numBytesAvailable ? n2 : this.numBytesAvailable;
            int n5 = this.source.read(arrby, n, n4);
            if (n5 == -1) {
                throw new EOFException();
            }
            this.numBytesAvailable -= n5;
            n += n5;
            n3 += n5;
            if (this.numBytesAvailable != 0 || (n2 -= n5) <= 0) continue;
            this.nextChunk();
        }
        return n3;
    }
}

