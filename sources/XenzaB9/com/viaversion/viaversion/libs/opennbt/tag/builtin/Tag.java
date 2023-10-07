// 
// Decompiled by Procyon v0.6.0
// 

package com.viaversion.viaversion.libs.opennbt.tag.builtin;

import java.lang.reflect.Array;
import java.io.DataOutput;
import java.io.IOException;
import com.viaversion.viaversion.libs.opennbt.tag.limiter.TagLimiter;
import java.io.DataInput;

public abstract class Tag implements Cloneable
{
    public abstract Object getValue();
    
    public final void read(final DataInput in) throws IOException {
        this.read(in, TagLimiter.noop(), 0);
    }
    
    public final void read(final DataInput in, final TagLimiter tagLimiter) throws IOException {
        this.read(in, tagLimiter, 0);
    }
    
    public abstract void read(final DataInput p0, final TagLimiter p1, final int p2) throws IOException;
    
    public abstract void write(final DataOutput p0) throws IOException;
    
    public abstract int getTagId();
    
    public abstract Tag clone();
    
    @Override
    public String toString() {
        String value = "";
        if (this.getValue() != null) {
            value = this.getValue().toString();
            if (this.getValue().getClass().isArray()) {
                final StringBuilder build = new StringBuilder();
                build.append("[");
                for (int index = 0; index < Array.getLength(this.getValue()); ++index) {
                    if (index > 0) {
                        build.append(", ");
                    }
                    build.append(Array.get(this.getValue(), index));
                }
                build.append("]");
                value = build.toString();
            }
        }
        return this.getClass().getSimpleName() + " { " + value + " }";
    }
}
