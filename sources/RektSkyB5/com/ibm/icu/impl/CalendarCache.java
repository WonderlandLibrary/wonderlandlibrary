/*
 * Decompiled with CFR 0.152.
 */
package com.ibm.icu.impl;

public class CalendarCache {
    private static final int[] primes = new int[]{61, 127, 509, 1021, 2039, 4093, 8191, 16381, 32749, 65521, 131071, 262139};
    private int pIndex = 0;
    private int size = 0;
    private int arraySize = primes[this.pIndex];
    private int threshold = this.arraySize * 3 / 4;
    private long[] keys = new long[this.arraySize];
    private long[] values = new long[this.arraySize];
    public static long EMPTY = Long.MIN_VALUE;

    public CalendarCache() {
        this.makeArrays(this.arraySize);
    }

    private void makeArrays(int newSize) {
        this.keys = new long[newSize];
        this.values = new long[newSize];
        for (int i2 = 0; i2 < newSize; ++i2) {
            this.values[i2] = EMPTY;
        }
        this.arraySize = newSize;
        this.threshold = (int)((double)this.arraySize * 0.75);
        this.size = 0;
    }

    public synchronized long get(long key) {
        return this.values[this.findIndex(key)];
    }

    public synchronized void put(long key, long value) {
        if (this.size >= this.threshold) {
            this.rehash();
        }
        int index = this.findIndex(key);
        this.keys[index] = key;
        this.values[index] = value;
        ++this.size;
    }

    private final int findIndex(long key) {
        int index = this.hash(key);
        int delta = 0;
        while (this.values[index] != EMPTY && this.keys[index] != key) {
            if (delta == 0) {
                delta = this.hash2(key);
            }
            index = (index + delta) % this.arraySize;
        }
        return index;
    }

    private void rehash() {
        int oldSize = this.arraySize;
        long[] oldKeys = this.keys;
        long[] oldValues = this.values;
        this.arraySize = this.pIndex < primes.length - 1 ? primes[++this.pIndex] : this.arraySize * 2 + 1;
        this.size = 0;
        this.makeArrays(this.arraySize);
        for (int i2 = 0; i2 < oldSize; ++i2) {
            if (oldValues[i2] == EMPTY) continue;
            this.put(oldKeys[i2], oldValues[i2]);
        }
    }

    private final int hash(long key) {
        int h2 = (int)((key * 15821L + 1L) % (long)this.arraySize);
        if (h2 < 0) {
            h2 += this.arraySize;
        }
        return h2;
    }

    private final int hash2(long key) {
        return this.arraySize - 2 - (int)(key % (long)(this.arraySize - 2));
    }
}

