// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.libs.opennbt.tag.builtin;

import java.io.DataOutput;
import java.io.IOException;
import java.io.DataInput;

public class ShortTag extends NumberTag
{
    public static final int ID = 2;
    private short value;
    
    public ShortTag() {
        this((short)0);
    }
    
    public ShortTag(final short value) {
        this.value = value;
    }
    
    @Deprecated
    @Override
    public Short getValue() {
        return this.value;
    }
    
    public void setValue(final short value) {
        this.value = value;
    }
    
    @Override
    public void read(final DataInput in) throws IOException {
        this.value = in.readShort();
    }
    
    @Override
    public void write(final DataOutput out) throws IOException {
        out.writeShort(this.value);
    }
    
    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        final ShortTag shortTag = (ShortTag)o;
        return this.value == shortTag.value;
    }
    
    @Override
    public int hashCode() {
        return this.value;
    }
    
    @Override
    public final ShortTag clone() {
        return new ShortTag(this.value);
    }
    
    @Override
    public byte asByte() {
        return (byte)this.value;
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
        return 2;
    }
}
