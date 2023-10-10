// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.libs.opennbt.tag.builtin;

import java.io.DataOutput;
import java.io.IOException;
import java.io.DataInput;

public class ByteTag extends NumberTag
{
    public static final int ID = 1;
    private byte value;
    
    public ByteTag() {
        this((byte)0);
    }
    
    public ByteTag(final byte value) {
        this.value = value;
    }
    
    @Deprecated
    @Override
    public Byte getValue() {
        return this.value;
    }
    
    public void setValue(final byte value) {
        this.value = value;
    }
    
    @Override
    public void read(final DataInput in) throws IOException {
        this.value = in.readByte();
    }
    
    @Override
    public void write(final DataOutput out) throws IOException {
        out.writeByte(this.value);
    }
    
    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        final ByteTag byteTag = (ByteTag)o;
        return this.value == byteTag.value;
    }
    
    @Override
    public int hashCode() {
        return this.value;
    }
    
    @Override
    public final ByteTag clone() {
        return new ByteTag(this.value);
    }
    
    @Override
    public byte asByte() {
        return this.value;
    }
    
    @Override
    public short asShort() {
        return this.value;
    }
    
    @Override
    public int asInt() {
        return this.value;
    }
    
    @Override
    public long asLong() {
        return this.value;
    }
    
    @Override
    public float asFloat() {
        return this.value;
    }
    
    @Override
    public double asDouble() {
        return this.value;
    }
    
    @Override
    public int getTagId() {
        return 1;
    }
}
