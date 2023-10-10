// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.libs.opennbt.tag.builtin;

import java.util.Arrays;
import java.io.DataOutput;
import java.io.IOException;
import java.io.DataInput;

public class ByteArrayTag extends Tag
{
    public static final int ID = 7;
    private byte[] value;
    
    public ByteArrayTag() {
        this(new byte[0]);
    }
    
    public ByteArrayTag(final byte[] value) {
        this.value = value;
    }
    
    @Override
    public byte[] getValue() {
        return this.value;
    }
    
    public void setValue(final byte[] value) {
        if (value == null) {
            return;
        }
        this.value = value;
    }
    
    public byte getValue(final int index) {
        return this.value[index];
    }
    
    public void setValue(final int index, final byte value) {
        this.value[index] = value;
    }
    
    public int length() {
        return this.value.length;
    }
    
    @Override
    public void read(final DataInput in) throws IOException {
        in.readFully(this.value = new byte[in.readInt()]);
    }
    
    @Override
    public void write(final DataOutput out) throws IOException {
        out.writeInt(this.value.length);
        out.write(this.value);
    }
    
    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        final ByteArrayTag that = (ByteArrayTag)o;
        return Arrays.equals(this.value, that.value);
    }
    
    @Override
    public int hashCode() {
        return Arrays.hashCode(this.value);
    }
    
    @Override
    public final ByteArrayTag clone() {
        return new ByteArrayTag(this.value);
    }
    
    @Override
    public int getTagId() {
        return 7;
    }
}
