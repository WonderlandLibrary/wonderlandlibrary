// 
// Decompiled by Procyon v0.6.0
// 

package com.viaversion.viaversion.libs.opennbt.tag.builtin;

import java.util.Arrays;
import java.io.DataOutput;
import java.io.IOException;
import com.viaversion.viaversion.libs.opennbt.tag.limiter.TagLimiter;
import java.io.DataInput;
import com.google.common.base.Preconditions;

public class LongArrayTag extends Tag
{
    public static final int ID = 12;
    private static final long[] EMPTY_ARRAY;
    private long[] value;
    
    public LongArrayTag() {
        this(LongArrayTag.EMPTY_ARRAY);
    }
    
    public LongArrayTag(final long[] value) {
        Preconditions.checkNotNull(value);
        this.value = value;
    }
    
    @Override
    public long[] getValue() {
        return this.value;
    }
    
    public void setValue(final long[] value) {
        Preconditions.checkNotNull(value);
        this.value = value;
    }
    
    public long getValue(final int index) {
        return this.value[index];
    }
    
    public void setValue(final int index, final long value) {
        this.value[index] = value;
    }
    
    public int length() {
        return this.value.length;
    }
    
    @Override
    public void read(final DataInput in, final TagLimiter tagLimiter, final int nestingLevel) throws IOException {
        tagLimiter.countInt();
        this.value = new long[in.readInt()];
        tagLimiter.countBytes(8 * this.value.length);
        for (int index = 0; index < this.value.length; ++index) {
            this.value[index] = in.readLong();
        }
    }
    
    @Override
    public void write(final DataOutput out) throws IOException {
        out.writeInt(this.value.length);
        for (final long l : this.value) {
            out.writeLong(l);
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
        final LongArrayTag that = (LongArrayTag)o;
        return Arrays.equals(this.value, that.value);
    }
    
    @Override
    public int hashCode() {
        return Arrays.hashCode(this.value);
    }
    
    @Override
    public final LongArrayTag clone() {
        return new LongArrayTag(this.value.clone());
    }
    
    @Override
    public int getTagId() {
        return 12;
    }
    
    static {
        EMPTY_ARRAY = new long[0];
    }
}
