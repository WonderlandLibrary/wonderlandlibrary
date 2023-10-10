// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.libs.opennbt.tag.builtin;

import java.io.DataOutput;
import java.io.IOException;
import java.io.DataInput;

public class IntTag extends NumberTag
{
    public static final int ID = 3;
    private int value;
    
    public IntTag() {
        this(0);
    }
    
    public IntTag(final int value) {
        this.value = value;
    }
    
    @Deprecated
    @Override
    public Integer getValue() {
        return this.value;
    }
    
    public void setValue(final int value) {
        this.value = value;
    }
    
    @Override
    public void read(final DataInput in) throws IOException {
        this.value = in.readInt();
    }
    
    @Override
    public void write(final DataOutput out) throws IOException {
        out.writeInt(this.value);
    }
    
    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        final IntTag intTag = (IntTag)o;
        return this.value == intTag.value;
    }
    
    @Override
    public int hashCode() {
        return this.value;
    }
    
    @Override
    public final IntTag clone() {
        return new IntTag(this.value);
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
        return this.value;
    }
    
    @Override
    public long asLong() {
        return this.value;
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
        return 3;
    }
}
