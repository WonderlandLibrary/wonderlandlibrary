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
        if (storageArray.length != 2048) {
            throw new IllegalArgumentException("ChunkNibbleArrays should be 2048 bytes not: " + storageArray.length);
        }
    }

    public int get(int x2, int y2, int z2) {
        return this.getFromIndex(this.getCoordinateIndex(x2, y2, z2));
    }

    public void set(int x2, int y2, int z2, int value) {
        this.setIndex(this.getCoordinateIndex(x2, y2, z2), value);
    }

    private int getCoordinateIndex(int x2, int y2, int z2) {
        return y2 << 8 | z2 << 4 | x2;
    }

    public int getFromIndex(int index) {
        int i2 = this.getNibbleIndex(index);
        return this.isLowerNibble(index) ? this.data[i2] & 0xF : this.data[i2] >> 4 & 0xF;
    }

    public void setIndex(int index, int value) {
        int i2 = this.getNibbleIndex(index);
        this.data[i2] = this.isLowerNibble(index) ? (byte)(this.data[i2] & 0xF0 | value & 0xF) : (byte)(this.data[i2] & 0xF | (value & 0xF) << 4);
    }

    private boolean isLowerNibble(int index) {
        return (index & 1) == 0;
    }

    private int getNibbleIndex(int index) {
        return index >> 1;
    }

    public byte[] getData() {
        return this.data;
    }
}

