// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.libs.opennbt.tag.builtin;

import java.util.Arrays;
import java.io.DataOutput;
import java.io.IOException;
import java.io.DataInput;
import com.google.common.base.Preconditions;

public class IntArrayTag extends Tag
{
    public static final int ID = 11;
    private int[] value;
    
    public IntArrayTag() {
        this(new int[0]);
    }
    
    public IntArrayTag(final int[] value) {
        Preconditions.checkNotNull(value);
        this.value = value;
    }
    
    @Override
    public int[] getValue() {
        return this.value;
    }
    
    public void setValue(final int[] value) {
        Preconditions.checkNotNull(value);
        this.value = value;
    }
    
    public int getValue(final int index) {
        return this.value[index];
    }
    
    public void setValue(final int index, final int value) {
        this.value[index] = value;
    }
    
    public int length() {
        return this.value.length;
    }
    
    @Override
    public void read(final DataInput in) throws IOException {
        this.value = new int[in.readInt()];
        for (int index = 0; index < this.value.length; ++index) {
            this.value[index] = in.readInt();
        }
    }
    
    @Override
    public void write(final DataOutput out) throws IOException {
        out.writeInt(this.value.length);
        for (final int i : this.value) {
            out.writeInt(i);
        }
    }
    
    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        final IntArrayTag that = (IntArrayTag)o;
        return Arrays.equals(this.value, that.value);
    }
    
    @Override
    public int hashCode() {
        return Arrays.hashCode(this.value);
    }
    
    @Override
    public final IntArrayTag clone() {
        return new IntArrayTag(this.value.clone());
    }
    
    @Override
    public int getTagId() {
        return 11;
    }
}
