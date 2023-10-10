/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.world.chunk;

public class NibbleArray {
    private final byte[] data;

    public NibbleArray() {
        this.data = new byte[2048];
    }

    public NibbleArray(byte[] storageArray) {
        this.data = storageArray;
        if (storageArray.length == 2048) return;
        throw new IllegalArgumentException("ChunkNibbleArrays should be 2048 bytes not: " + storageArray.length);
    }

    public int get(int x, int y, int z) {
        return this.getFromIndex(this.getCoordinateIndex(x, y, z));
    }

    public void set(int x, int y, int z, int value) {
        this.setIndex(this.getCoordinateIndex(x, y, z), value);
    }

    private int getCoordinateIndex(int x, int y, int z) {
        return y << 8 | z << 4 | x;
    }

    public int getFromIndex(int index) {
        int n;
        int i = this.getNibbleIndex(index);
        if (this.isLowerNibble(index)) {
            n = this.data[i] & 0xF;
            return n;
        }
        n = this.data[i] >> 4 & 0xF;
        return n;
    }

    public void setIndex(int index, int value) {
        int i = this.getNibbleIndex(index);
        if (this.isLowerNibble(index)) {
            this.data[i] = (byte)(this.data[i] & 0xF0 | value & 0xF);
            return;
        }
        this.data[i] = (byte)(this.data[i] & 0xF | (value & 0xF) << 4);
    }

    private boolean isLowerNibble(int index) {
        if ((index & 1) != 0) return false;
        return true;
    }

    private int getNibbleIndex(int index) {
        return index >> 1;
    }

    public byte[] getData() {
        return this.data;
    }
}

