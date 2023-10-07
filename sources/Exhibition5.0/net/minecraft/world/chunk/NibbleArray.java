// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.world.chunk;

public class NibbleArray
{
    private final byte[] data;
    private static final String __OBFID = "CL_00000371";
    
    public NibbleArray() {
        this.data = new byte[2048];
    }
    
    public NibbleArray(final byte[] storageArray) {
        this.data = storageArray;
        if (storageArray.length != 2048) {
            throw new IllegalArgumentException("ChunkNibbleArrays should be 2048 bytes not: " + storageArray.length);
        }
    }
    
    public int get(final int x, final int y, final int z) {
        return this.getFromIndex(this.getCoordinateIndex(x, y, z));
    }
    
    public void set(final int x, final int y, final int z, final int value) {
        this.setIndex(this.getCoordinateIndex(x, y, z), value);
    }
    
    private int getCoordinateIndex(final int x, final int y, final int z) {
        return y << 8 | z << 4 | x;
    }
    
    public int getFromIndex(final int index) {
        final int var2 = this.func_177478_c(index);
        return this.func_177479_b(index) ? (this.data[var2] & 0xF) : (this.data[var2] >> 4 & 0xF);
    }
    
    public void setIndex(final int index, final int value) {
        final int var3 = this.func_177478_c(index);
        if (this.func_177479_b(index)) {
            this.data[var3] = (byte)((this.data[var3] & 0xF0) | (value & 0xF));
        }
        else {
            this.data[var3] = (byte)((this.data[var3] & 0xF) | (value & 0xF) << 4);
        }
    }
    
    private boolean func_177479_b(final int p_177479_1_) {
        return (p_177479_1_ & 0x1) == 0x0;
    }
    
    private int func_177478_c(final int p_177478_1_) {
        return p_177478_1_ >> 1;
    }
    
    public byte[] getData() {
        return this.data;
    }
}
