// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.nbt;

import java.util.Arrays;
import java.io.DataInput;
import java.io.IOException;
import java.io.DataOutput;

public class NBTTagIntArray extends NBTBase
{
    private int[] intArray;
    private static final String __OBFID = "CL_00001221";
    
    NBTTagIntArray() {
    }
    
    public NBTTagIntArray(final int[] p_i45132_1_) {
        this.intArray = p_i45132_1_;
    }
    
    @Override
    void write(final DataOutput output) throws IOException {
        output.writeInt(this.intArray.length);
        for (final int element : this.intArray) {
            output.writeInt(element);
        }
    }
    
    @Override
    void read(final DataInput input, final int depth, final NBTSizeTracker sizeTracker) throws IOException {
        final int var4 = input.readInt();
        sizeTracker.read(32 * var4);
        this.intArray = new int[var4];
        for (int var5 = 0; var5 < var4; ++var5) {
            this.intArray[var5] = input.readInt();
        }
    }
    
    @Override
    public byte getId() {
        return 11;
    }
    
    @Override
    public String toString() {
        String var1 = "[";
        for (final int var5 : this.intArray) {
            var1 = var1 + var5 + ",";
        }
        return var1 + "]";
    }
    
    @Override
    public NBTBase copy() {
        final int[] var1 = new int[this.intArray.length];
        System.arraycopy(this.intArray, 0, var1, 0, this.intArray.length);
        return new NBTTagIntArray(var1);
    }
    
    @Override
    public boolean equals(final Object p_equals_1_) {
        return super.equals(p_equals_1_) && Arrays.equals(this.intArray, ((NBTTagIntArray)p_equals_1_).intArray);
    }
    
    @Override
    public int hashCode() {
        return super.hashCode() ^ Arrays.hashCode(this.intArray);
    }
    
    public int[] getIntArray() {
        return this.intArray;
    }
}
