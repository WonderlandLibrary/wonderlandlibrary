/*
 * Decompiled with CFR 0.150.
 */
package org.apache.commons.compress.archivers.zip;

import java.io.IOException;
import java.io.InputStream;

class BitStream {
    private final InputStream in;
    private long bitCache;
    private int bitCacheSize;
    private static final int[] MASKS = new int[]{0, 1, 3, 7, 15, 31, 63, 127, 255};

    BitStream(InputStream in) {
        this.in = in;
    }

    private boolean fillCache() throws IOException {
        long nextByte;
        boolean filled = false;
        while (this.bitCacheSize <= 56 && (nextByte = (long)this.in.read()) != -1L) {
            filled = true;
            this.bitCache |= nextByte << this.bitCacheSize;
            this.bitCacheSize += 8;
        }
        return filled;
    }

    int nextBit() throws IOException {
        if (this.bitCacheSize == 0 && !this.fillCache()) {
            return -1;
        }
        int bit = (int)(this.bitCache & 1L);
        this.bitCache >>>= 1;
        --this.bitCacheSize;
        return bit;
    }

    int nextBits(int n) throws IOException {
        if (this.bitCacheSize < n && !this.fillCache()) {
            return -1;
        }
        int bits = (int)(this.bitCache & (long)MASKS[n]);
        this.bitCache >>>= n;
        this.bitCacheSize -= n;
        return bits;
    }

    int nextByte() throws IOException {
        return this.nextBits(8);
    }
}

