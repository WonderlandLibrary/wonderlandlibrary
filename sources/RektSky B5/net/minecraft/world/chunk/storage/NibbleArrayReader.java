/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.world.chunk.storage;

public class NibbleArrayReader {
    public final byte[] data;
    private final int depthBits;
    private final int depthBitsPlusFour;

    public NibbleArrayReader(byte[] dataIn, int depthBitsIn) {
        this.data = dataIn;
        this.depthBits = depthBitsIn;
        this.depthBitsPlusFour = depthBitsIn + 4;
    }

    public int get(int p_76686_1_, int p_76686_2_, int p_76686_3_) {
        int i2 = p_76686_1_ << this.depthBitsPlusFour | p_76686_3_ << this.depthBits | p_76686_2_;
        int j2 = i2 >> 1;
        int k2 = i2 & 1;
        return k2 == 0 ? this.data[j2] & 0xF : this.data[j2] >> 4 & 0xF;
    }
}

