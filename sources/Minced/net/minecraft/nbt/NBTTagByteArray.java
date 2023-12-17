// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.nbt;

import java.util.Arrays;
import java.io.DataInput;
import java.io.IOException;
import java.io.DataOutput;
import java.util.List;

public class NBTTagByteArray extends NBTBase
{
    private byte[] data;
    
    NBTTagByteArray() {
    }
    
    public NBTTagByteArray(final byte[] data) {
        this.data = data;
    }
    
    public NBTTagByteArray(final List<Byte> p_i47529_1_) {
        this(toArray(p_i47529_1_));
    }
    
    private static byte[] toArray(final List<Byte> p_193589_0_) {
        final byte[] abyte = new byte[p_193589_0_.size()];
        for (int i = 0; i < p_193589_0_.size(); ++i) {
            final Byte obyte = p_193589_0_.get(i);
            abyte[i] = (byte)((obyte == null) ? 0 : ((byte)obyte));
        }
        return abyte;
    }
    
    @Override
    void write(final DataOutput output) throws IOException {
        output.writeInt(this.data.length);
        output.write(this.data);
    }
    
    @Override
    void read(final DataInput input, final int depth, final NBTSizeTracker sizeTracker) throws IOException {
        sizeTracker.read(192L);
        final int i = input.readInt();
        sizeTracker.read(8 * i);
        input.readFully(this.data = new byte[i]);
    }
    
    @Override
    public byte getId() {
        return 7;
    }
    
    @Override
    public String toString() {
        final StringBuilder stringbuilder = new StringBuilder("[B;");
        for (int i = 0; i < this.data.length; ++i) {
            if (i != 0) {
                stringbuilder.append(',');
            }
            stringbuilder.append(this.data[i]).append('B');
        }
        return stringbuilder.append(']').toString();
    }
    
    @Override
    public NBTBase copy() {
        final byte[] abyte = new byte[this.data.length];
        System.arraycopy(this.data, 0, abyte, 0, this.data.length);
        return new NBTTagByteArray(abyte);
    }
    
    @Override
    public boolean equals(final Object p_equals_1_) {
        return super.equals(p_equals_1_) && Arrays.equals(this.data, ((NBTTagByteArray)p_equals_1_).data);
    }
    
    @Override
    public int hashCode() {
        return super.hashCode() ^ Arrays.hashCode(this.data);
    }
    
    public byte[] getByteArray() {
        return this.data;
    }
}
