// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.api.type.types;

import io.netty.buffer.ByteBuf;
import com.viaversion.viaversion.api.type.TypeConverter;
import com.viaversion.viaversion.api.type.Type;

public class LongType extends Type<Long> implements TypeConverter<Long>
{
    public LongType() {
        super(Long.class);
    }
    
    @Deprecated
    @Override
    public Long read(final ByteBuf buffer) {
        return buffer.readLong();
    }
    
    @Deprecated
    @Override
    public void write(final ByteBuf buffer, final Long object) {
        buffer.writeLong(object);
    }
    
    @Override
    public Long from(final Object o) {
        if (o instanceof Number) {
            return ((Number)o).longValue();
        }
        if (o instanceof Boolean) {
            return (long)(((boolean)o) ? 1 : 0);
        }
        return (Long)o;
    }
    
    public long readPrimitive(final ByteBuf buffer) {
        return buffer.readLong();
    }
    
    public void writePrimitive(final ByteBuf buffer, final long object) {
        buffer.writeLong(object);
    }
}
