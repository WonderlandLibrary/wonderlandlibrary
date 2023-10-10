// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.libs.opennbt.tag.builtin;

import java.io.DataOutput;
import java.io.IOException;
import java.io.DataInput;

public class DoubleTag extends NumberTag
{
    public static final int ID = 6;
    private double value;
    
    public DoubleTag() {
        this(0.0);
    }
    
    public DoubleTag(final double value) {
        this.value = value;
    }
    
    @Deprecated
    @Override
    public Double getValue() {
        return this.value;
    }
    
    public void setValue(final double value) {
        this.value = value;
    }
    
    @Override
    public void read(final DataInput in) throws IOException {
        this.value = in.readDouble();
    }
    
    @Override
    public void write(final DataOutput out) throws IOException {
        out.writeDouble(this.value);
    }
    
    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        final DoubleTag doubleTag = (DoubleTag)o;
        return this.value == doubleTag.value;
    }
    
    @Override
    public int hashCode() {
        return Double.hashCode(this.value);
    }
    
    @Override
    public final DoubleTag clone() {
        return new DoubleTag(this.value);
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
        return (float)this.value;
    }
    
    @Override
    public double asDouble() {
        return this.value;
    }
    
    @Override
    public int getTagId() {
        return 6;
    }
}
