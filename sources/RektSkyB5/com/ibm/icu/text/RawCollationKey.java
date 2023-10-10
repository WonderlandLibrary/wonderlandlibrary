/*
 * Decompiled with CFR 0.152.
 */
package com.ibm.icu.text;

import com.ibm.icu.util.ByteArrayWrapper;

public final class RawCollationKey
extends ByteArrayWrapper {
    public RawCollationKey() {
    }

    public RawCollationKey(int capacity) {
        this.bytes = new byte[capacity];
    }

    public RawCollationKey(byte[] bytes) {
        this.bytes = bytes;
    }

    public RawCollationKey(byte[] bytesToAdopt, int size) {
        super(bytesToAdopt, size);
    }

    @Override
    public int compareTo(RawCollationKey rhs) {
        int result = super.compareTo(rhs);
        return result < 0 ? -1 : (result == 0 ? 0 : 1);
    }
}

