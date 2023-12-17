// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.world.chunk.storage;

public class NibbleArrayReader
{
    public final byte[] data;
    private final int depthBits;
    private final int depthBitsPlusFour;
    
    public NibbleArrayReader(final byte[] dataIn, final int depthBitsIn) {
        this.data = dataIn;
        this.depthBits = depthBitsIn;
        this.depthBitsPlusFour = depthBitsIn + 4;
    }
    
    public int get(final int x, final int y, final int z) {
        final int i = x << this.depthBitsPlusFour | z << this.depthBits | y;
        final int j = i >> 1;
        final int k = i & 0x1;
        return (k == 0) ? (this.data[j] & 0xF) : (this.data[j] >> 4 & 0xF);
    }
}
