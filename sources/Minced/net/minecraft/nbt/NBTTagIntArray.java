// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.nbt;

import java.util.Arrays;
import java.io.DataInput;
import java.io.IOException;
import java.io.DataOutput;
import java.util.List;

public class NBTTagIntArray extends NBTBase
{
    private int[] intArray;
    
    NBTTagIntArray() {
    }
    
    public NBTTagIntArray(final int[] p_i45132_1_) {
        this.intArray = p_i45132_1_;
    }
    
    public NBTTagIntArray(final List<Integer> p_i47528_1_) {
        this(toArray(p_i47528_1_));
    }
    
    private static int[] toArray(final List<Integer> p_193584_0_) {
        final int[] aint = new int[p_193584_0_.size()];
        for (int i = 0; i < p_193584_0_.size(); ++i) {
            final Integer integer = p_193584_0_.get(i);
            aint[i] = ((integer == null) ? 0 : integer);
        }
        return aint;
    }
    
    @Override
    void write(final DataOutput output) throws IOException {
        output.writeInt(this.intArray.length);
        for (final int i : this.intArray) {
            output.writeInt(i);
        }
    }
    
    @Override
    void read(final DataInput input, final int depth, final NBTSizeTracker sizeTracker) throws IOException {
        sizeTracker.read(192L);
        final int i = input.readInt();
        sizeTracker.read(32 * i);
        this.intArray = new int[i];
        for (int j = 0; j < i; ++j) {
            this.intArray[j] = input.readInt();
        }
    }
    
    @Override
    public byte getId() {
        return 11;
    }
    
    @Override
    public String toString() {
        final StringBuilder stringbuilder = new StringBuilder("[I;");
        for (int i = 0; i < this.intArray.length; ++i) {
            if (i != 0) {
                stringbuilder.append(',');
            }
            stringbuilder.append(this.intArray[i]);
        }
        return stringbuilder.append(']').toString();
    }
    
    @Override
    public NBTTagIntArray copy() {
        final int[] aint = new int[this.intArray.length];
        System.arraycopy(this.intArray, 0, aint, 0, this.intArray.length);
        return new NBTTagIntArray(aint);
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
