/*
 * Decompiled with CFR 0.152.
 */
package com.ibm.icu.util;

import com.ibm.icu.impl.Utility;
import java.nio.ByteBuffer;

public class ByteArrayWrapper
implements Comparable<ByteArrayWrapper> {
    public byte[] bytes;
    public int size;

    public ByteArrayWrapper() {
    }

    public ByteArrayWrapper(byte[] bytesToAdopt, int size) {
        if (bytesToAdopt == null && size != 0 || size < 0 || bytesToAdopt != null && size > bytesToAdopt.length) {
            throw new IndexOutOfBoundsException("illegal size: " + size);
        }
        this.bytes = bytesToAdopt;
        this.size = size;
    }

    public ByteArrayWrapper(ByteBuffer source) {
        this.size = source.limit();
        this.bytes = new byte[this.size];
        source.get(this.bytes, 0, this.size);
    }

    public ByteArrayWrapper ensureCapacity(int capacity) {
        if (this.bytes == null || this.bytes.length < capacity) {
            byte[] newbytes = new byte[capacity];
            if (this.bytes != null) {
                ByteArrayWrapper.copyBytes(this.bytes, 0, newbytes, 0, this.size);
            }
            this.bytes = newbytes;
        }
        return this;
    }

    public final ByteArrayWrapper set(byte[] src, int start, int limit) {
        this.size = 0;
        this.append(src, start, limit);
        return this;
    }

    public final ByteArrayWrapper append(byte[] src, int start, int limit) {
        int len = limit - start;
        this.ensureCapacity(this.size + len);
        ByteArrayWrapper.copyBytes(src, start, this.bytes, this.size, len);
        this.size += len;
        return this;
    }

    public final byte[] releaseBytes() {
        byte[] result = this.bytes;
        this.bytes = null;
        this.size = 0;
        return result;
    }

    public String toString() {
        StringBuilder result = new StringBuilder();
        for (int i2 = 0; i2 < this.size; ++i2) {
            if (i2 != 0) {
                result.append(" ");
            }
            result.append(Utility.hex(this.bytes[i2] & 0xFF, 2));
        }
        return result.toString();
    }

    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (other == null) {
            return false;
        }
        try {
            ByteArrayWrapper that = (ByteArrayWrapper)other;
            if (this.size != that.size) {
                return false;
            }
            for (int i2 = 0; i2 < this.size; ++i2) {
                if (this.bytes[i2] == that.bytes[i2]) continue;
                return false;
            }
            return true;
        }
        catch (ClassCastException classCastException) {
            return false;
        }
    }

    public int hashCode() {
        int result = this.bytes.length;
        for (int i2 = 0; i2 < this.size; ++i2) {
            result = 37 * result + this.bytes[i2];
        }
        return result;
    }

    @Override
    public int compareTo(ByteArrayWrapper other) {
        if (this == other) {
            return 0;
        }
        int minSize = this.size < other.size ? this.size : other.size;
        for (int i2 = 0; i2 < minSize; ++i2) {
            if (this.bytes[i2] == other.bytes[i2]) continue;
            return (this.bytes[i2] & 0xFF) - (other.bytes[i2] & 0xFF);
        }
        return this.size - other.size;
    }

    private static final void copyBytes(byte[] src, int srcoff, byte[] tgt, int tgtoff, int length) {
        if (length < 64) {
            int i2 = srcoff;
            int n2 = tgtoff;
            while (--length >= 0) {
                tgt[n2] = src[i2];
                ++i2;
                ++n2;
            }
        } else {
            System.arraycopy(src, srcoff, tgt, tgtoff, length);
        }
    }
}

