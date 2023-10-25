/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.world.chunk.storage;

public class NibbleArrayReader {
    private final int depthBits;
    private final int depthBitsPlusFour;
    public final byte[] data;

    public int get(int n, int n2, int n3) {
        int n4 = n << this.depthBitsPlusFour | n3 << this.depthBits | n2;
        int n5 = n4 >> 1;
        int n6 = n4 & 1;
        return n6 == 0 ? this.data[n5] & 0xF : this.data[n5] >> 4 & 0xF;
    }

    public NibbleArrayReader(byte[] byArray, int n) {
        this.data = byArray;
        this.depthBits = n;
        this.depthBitsPlusFour = n + 4;
    }
}

