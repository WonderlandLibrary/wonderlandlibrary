// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.libs.opennbt.tag.builtin;

import java.io.DataOutput;
import java.io.IOException;
import java.io.DataInput;
import com.google.common.base.Preconditions;

public class StringTag extends Tag
{
    public static final int ID = 8;
    private String value;
    
    public StringTag() {
        this("");
    }
    
    public StringTag(final String value) {
        Preconditions.checkNotNull(value);
        this.value = value;
    }
    
    @Override
    public String getValue() {
        return this.value;
    }
    
    public void setValue(final String value) {
        Preconditions.checkNotNull(value);
        this.value = value;
    }
    
    @Override
    public void read(final DataInput in) throws IOException {
        this.value = in.readUTF();
    }
    
    @Override
    public void write(final DataOutput out) throws IOException {
        out.writeUTF(this.value);
    }
    
    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        final StringTag stringTag = (StringTag)o;
        return this.value.equals(stringTag.value);
    }
    
    @Override
    public int hashCode() {
        return this.value.hashCode();
    }
    
    @Override
    public final StringTag clone() {
        return new StringTag(this.value);
    }
    
    @Override
    public int getTagId() {
        return 8;
    }
}
