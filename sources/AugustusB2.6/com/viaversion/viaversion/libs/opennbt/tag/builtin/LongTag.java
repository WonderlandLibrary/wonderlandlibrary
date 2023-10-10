// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.libs.opennbt.tag.builtin;

import java.io.DataOutput;
import java.io.IOException;
import java.io.DataInput;

public class LongTag extends NumberTag
{
    public static final int ID = 4;
    private long value;
    
    public LongTag() {
        this(0L);
    }
    
    public LongTag(final long value) {
        this.value = value;
    }
    
    @Deprecated
    @Override
    public Long getValue() {
        return this.value;
    }
    
    public void setValue(final long value) {
        this.value = value;
    }
    
    @Override
    public void read(final DataInput in) throws IOException {
        this.value = in.readLong();
    }
    
    @Override
    public void write(final DataOutput out) throws IOException {
        out.writeLong(this.value);
    }
    
    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        final LongTag longTag = (LongTag)o;
        return this.value == longTag.value;
    }
    
    @Override
    public int hashCode() {
        return Long.hashCode(this.value);
    }
    
    @Override
    public final LongTag clone() {
        return new LongTag(this.value);
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
        return this.value;
    }
    
    @Override
    public float asFloat() {
        return (float)this.value;
    }
    
    @Override
    public double asDouble() {
        return (double)this.value;
    }
    
    @Override
    public int getTagId() {
        return 4;
    }
}
