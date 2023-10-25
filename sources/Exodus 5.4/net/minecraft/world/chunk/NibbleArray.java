/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.world.chunk;

public class NibbleArray {
    private final byte[] data;

    public int get(int n, int n2, int n3) {
        return this.getFromIndex(this.getCoordinateIndex(n, n2, n3));
    }

    public NibbleArray(byte[] byArray) {
        this.data = byArray;
        if (byArray.length != 2048) {
            throw new IllegalArgumentException("ChunkNibbleArrays should be 2048 bytes not: " + byArray.length);
        }
    }

    public void set(int n, int n2, int n3, int n4) {
        this.setIndex(this.getCoordinateIndex(n, n2, n3), n4);
    }

    private boolean isLowerNibble(int n) {
        return (n & 1) == 0;
    }

    private int getNibbleIndex(int n) {
        return n >> 1;
    }

    private int getCoordinateIndex(int n, int n2, int n3) {
        return n2 << 8 | n3 << 4 | n;
    }

    public byte[] getData() {
        return this.data;
    }

    public void setIndex(int n, int n2) {
        int n3 = this.getNibbleIndex(n);
        this.data[n3] = this.isLowerNibble(n) ? (byte)(this.data[n3] & 0xF0 | n2 & 0xF) : (byte)(this.data[n3] & 0xF | (n2 & 0xF) << 4);
    }

    public int getFromIndex(int n) {
        int n2 = this.getNibbleIndex(n);
        return this.isLowerNibble(n) ? this.data[n2] & 0xF : this.data[n2] >> 4 & 0xF;
    }

    public NibbleArray() {
        this.data = new byte[2048];
    }
}

