/*
 * Decompiled with CFR 0.152.
 */
package org.apache.commons.io.input;

import java.io.Reader;
import java.io.Serializable;

public class CharSequenceReader
extends Reader
implements Serializable {
    private static final long serialVersionUID = 3724187752191401220L;
    private final CharSequence charSequence;
    private int idx;
    private int mark;

    public CharSequenceReader(CharSequence charSequence) {
        this.charSequence = charSequence != null ? charSequence : "";
    }

    @Override
    public void close() {
        this.idx = 0;
        this.mark = 0;
    }

    @Override
    public void mark(int readAheadLimit) {
        this.mark = this.idx;
    }

    @Override
    public boolean markSupported() {
        return true;
    }

    @Override
    public int read() {
        if (this.idx >= this.charSequence.length()) {
            return -1;
        }
        return this.charSequence.charAt(this.idx++);
    }

    @Override
    public int read(char[] array, int offset, int length) {
        if (this.idx >= this.charSequence.length()) {
            return -1;
        }
        if (array == null) {
            throw new NullPointerException("Character array is missing");
        }
        if (length < 0 || offset < 0 || offset + length > array.length) {
            throw new IndexOutOfBoundsException("Array Size=" + array.length + ", offset=" + offset + ", length=" + length);
        }
        int count = 0;
        for (int i2 = 0; i2 < length; ++i2) {
            int c2 = this.read();
            if (c2 == -1) {
                return count;
            }
            array[offset + i2] = (char)c2;
            ++count;
        }
        return count;
    }

    @Override
    public void reset() {
        this.idx = this.mark;
    }

    @Override
    public long skip(long n2) {
        if (n2 < 0L) {
            throw new IllegalArgumentException("Number of characters to skip is less than zero: " + n2);
        }
        if (this.idx >= this.charSequence.length()) {
            return -1L;
        }
        int dest = (int)Math.min((long)this.charSequence.length(), (long)this.idx + n2);
        int count = dest - this.idx;
        this.idx = dest;
        return count;
    }

    public String toString() {
        return this.charSequence.toString();
    }
}

