// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.nbt;

import java.util.Arrays;
import java.io.DataInput;
import java.io.IOException;
import java.io.DataOutput;
import java.util.List;

public class NBTTagLongArray extends NBTBase
{
    private long[] data;
    
    NBTTagLongArray() {
    }
    
    public NBTTagLongArray(final long[] p_i47524_1_) {
        this.data = p_i47524_1_;
    }
    
    public NBTTagLongArray(final List<Long> p_i47525_1_) {
        this(toArray(p_i47525_1_));
    }
    
    private static long[] toArray(final List<Long> p_193586_0_) {
        final long[] along = new long[p_193586_0_.size()];
        for (int i = 0; i < p_193586_0_.size(); ++i) {
            final Long olong = p_193586_0_.get(i);
            along[i] = ((olong == null) ? 0L : olong);
        }
        return along;
    }
    
    @Override
    void write(final DataOutput output) throws IOException {
        output.writeInt(this.data.length);
        for (final long i : this.data) {
            output.writeLong(i);
        }
    }
    
    @Override
    void read(final DataInput input, final int depth, final NBTSizeTracker sizeTracker) throws IOException {
        sizeTracker.read(192L);
        final int i = input.readInt();
        sizeTracker.read(64 * i);
        this.data = new long[i];
        for (int j = 0; j < i; ++j) {
            this.data[j] = input.readLong();
        }
    }
    
    @Override
    public byte getId() {
        return 12;
    }
    
    @Override
    public String toString() {
        final StringBuilder stringbuilder = new StringBuilder("[L;");
        for (int i = 0; i < this.data.length; ++i) {
            if (i != 0) {
                stringbuilder.append(',');
            }
            stringbuilder.append(this.data[i]).append('L');
        }
        return stringbuilder.append(']').toString();
    }
    
    @Override
    public NBTTagLongArray copy() {
        final long[] along = new long[this.data.length];
        System.arraycopy(this.data, 0, along, 0, this.data.length);
        return new NBTTagLongArray(along);
    }
    
    @Override
    public boolean equals(final Object p_equals_1_) {
        return super.equals(p_equals_1_) && Arrays.equals(this.data, ((NBTTagLongArray)p_equals_1_).data);
    }
    
    @Override
    public int hashCode() {
        return super.hashCode() ^ Arrays.hashCode(this.data);
    }
}
