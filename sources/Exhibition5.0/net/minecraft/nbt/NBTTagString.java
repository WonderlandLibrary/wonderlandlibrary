// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.nbt;

import java.io.DataInput;
import java.io.IOException;
import java.io.DataOutput;

public class NBTTagString extends NBTBase
{
    private String data;
    private static final String __OBFID = "CL_00001228";
    
    public NBTTagString() {
        this.data = "";
    }
    
    public NBTTagString(final String data) {
        this.data = data;
        if (data == null) {
            throw new IllegalArgumentException("Empty string not allowed");
        }
    }
    
    @Override
    void write(final DataOutput output) throws IOException {
        output.writeUTF(this.data);
    }
    
    @Override
    void read(final DataInput input, final int depth, final NBTSizeTracker sizeTracker) throws IOException {
        this.data = input.readUTF();
        sizeTracker.read(16 * this.data.length());
    }
    
    @Override
    public byte getId() {
        return 8;
    }
    
    @Override
    public String toString() {
        return "\"" + this.data.replace("\"", "\\\"") + "\"";
    }
    
    @Override
    public NBTBase copy() {
        return new NBTTagString(this.data);
    }
    
    @Override
    public boolean hasNoTags() {
        return this.data.isEmpty();
    }
    
    @Override
    public boolean equals(final Object p_equals_1_) {
        if (!super.equals(p_equals_1_)) {
            return false;
        }
        final NBTTagString var2 = (NBTTagString)p_equals_1_;
        return (this.data == null && var2.data == null) || (this.data != null && this.data.equals(var2.data));
    }
    
    @Override
    public int hashCode() {
        return super.hashCode() ^ this.data.hashCode();
    }
    
    public String getString() {
        return this.data;
    }
}
