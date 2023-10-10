/*
 * Decompiled with CFR 0.152.
 */
package com.viaversion.viaversion.libs.opennbt.tag.builtin;

import com.google.common.base.Preconditions;
import com.viaversion.viaversion.libs.opennbt.tag.builtin.Tag;
import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Arrays;

public class LongArrayTag
extends Tag {
    public static final int ID = 12;
    private long[] value;

    public LongArrayTag() {
        this(new long[0]);
    }

    public LongArrayTag(long[] value) {
        Preconditions.checkNotNull(value);
        this.value = value;
    }

    public long[] getValue() {
        return this.value;
    }

    public void setValue(long[] value) {
        Preconditions.checkNotNull(value);
        this.value = value;
    }

    public long getValue(int index) {
        return this.value[index];
    }

    public void setValue(int index, long value) {
        this.value[index] = value;
    }

    public int length() {
        return this.value.length;
    }

    @Override
    public void read(DataInput in) throws IOException {
        this.value = new long[in.readInt()];
        for (int index = 0; index < this.value.length; ++index) {
            this.value[index] = in.readLong();
        }
    }

    @Override
    public void write(DataOutput out) throws IOException {
        out.writeInt(this.value.length);
        for (long l2 : this.value) {
            out.writeLong(l2);
        }
    }

    public boolean equals(Object o2) {
        if (this == o2) {
            return true;
        }
        if (o2 == null || this.getClass() != o2.getClass()) {
            return false;
        }
        LongArrayTag that = (LongArrayTag)o2;
        return Arrays.equals(this.value, that.value);
    }

    public int hashCode() {
        return Arrays.hashCode(this.value);
    }

    @Override
    public final LongArrayTag clone() {
        return new LongArrayTag((long[])this.value.clone());
    }

    @Override
    public int getTagId() {
        return 12;
    }
}

