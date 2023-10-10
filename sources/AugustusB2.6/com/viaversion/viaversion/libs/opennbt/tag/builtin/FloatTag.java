// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.libs.opennbt.tag.builtin;

import java.io.DataOutput;
import java.io.IOException;
import java.io.DataInput;

public class FloatTag extends NumberTag
{
    public static final int ID = 5;
    private float value;
    
    public FloatTag() {
        this(0.0f);
    }
    
    public FloatTag(final float value) {
        this.value = value;
    }
    
    @Deprecated
    @Override
    public Float getValue() {
        return this.value;
    }
    
    public void setValue(final float value) {
        this.value = value;
    }
    
    @Override
    public void read(final DataInput in) throws IOException {
        this.value = in.readFloat();
    }
    
    @Override
    public void write(final DataOutput out) throws IOException {
        out.writeFloat(this.value);
    }
    
    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        final FloatTag floatTag = (FloatTag)o;
        return this.value == floatTag.value;
    }
    
    @Override
    public int hashCode() {
        return Float.hashCode(this.value);
    }
    
    @Override
    public final FloatTag clone() {
        return new FloatTag(this.value);
    }
    
    @Override
    public byte asByte() {
        return (byte)this.value;
    }
    
    @Override
    public short asShort() {
        return (short)this.value;
    }
    
    @Override
    public int asInt() {
        return (int)this.value;
    }
    
    @Override
    public long asLong() {
        return (long)this.value;
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
        return 5;
    }
}
