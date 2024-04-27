/*
 * Decompiled with CFR 0.152.
 */
package net.ccbluex.liquidbounce.utils;

public class RollingArrayLongBuffer {
    private final long[] contents;
    private int currentIndex = 0;

    public long[] getContents() {
        return this.contents;
    }

    public int getTimestampsSince(long l) {
        for (int i = 0; i < this.contents.length; ++i) {
            if (this.contents[this.currentIndex < i ? this.contents.length - i + this.currentIndex : this.currentIndex - i] >= l) continue;
            return i;
        }
        return this.contents.length;
    }

    public void add(long l) {
        this.currentIndex = (this.currentIndex + 1) % this.contents.length;
        this.contents[this.currentIndex] = l;
    }

    public RollingArrayLongBuffer(int n) {
        this.contents = new long[n];
    }
}

