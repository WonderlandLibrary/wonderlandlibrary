// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.nbt;

import java.io.DataInput;
import java.io.IOException;
import java.io.DataOutput;
import java.util.Objects;

public class NBTTagString extends NBTBase
{
    private String data;
    
    public NBTTagString() {
        this("");
    }
    
    public NBTTagString(final String data) {
        Objects.requireNonNull(data, "Null string not allowed");
        this.data = data;
    }
    
    @Override
    void write(final DataOutput output) throws IOException {
        output.writeUTF(this.data);
    }
    
    @Override
    void read(final DataInput input, final int depth, final NBTSizeTracker sizeTracker) throws IOException {
        sizeTracker.read(288L);
        this.data = input.readUTF();
        sizeTracker.read(16 * this.data.length());
    }
    
    @Override
    public byte getId() {
        return 8;
    }
    
    @Override
    public String toString() {
        return quoteAndEscape(this.data);
    }
    
    @Override
    public NBTTagString copy() {
        return new NBTTagString(this.data);
    }
    
    @Override
    public boolean isEmpty() {
        return this.data.isEmpty();
    }
    
    @Override
    public boolean equals(final Object p_equals_1_) {
        if (!super.equals(p_equals_1_)) {
            return false;
        }
        final NBTTagString nbttagstring = (NBTTagString)p_equals_1_;
        return (this.data == null && nbttagstring.data == null) || Objects.equals(this.data, nbttagstring.data);
    }
    
    @Override
    public int hashCode() {
        return super.hashCode() ^ this.data.hashCode();
    }
    
    public String getString() {
        return this.data;
    }
    
    public static String quoteAndEscape(final String p_193588_0_) {
        final StringBuilder stringbuilder = new StringBuilder("\"");
        for (int i = 0; i < p_193588_0_.length(); ++i) {
            final char c0 = p_193588_0_.charAt(i);
            if (c0 == '\\' || c0 == '\"') {
                stringbuilder.append('\\');
            }
            stringbuilder.append(c0);
        }
        return stringbuilder.append('\"').toString();
    }
}
