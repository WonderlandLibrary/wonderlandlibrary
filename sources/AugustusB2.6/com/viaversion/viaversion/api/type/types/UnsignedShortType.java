// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.api.type.types;

import io.netty.buffer.ByteBuf;
import com.viaversion.viaversion.api.type.TypeConverter;
import com.viaversion.viaversion.api.type.Type;

public class UnsignedShortType extends Type<Integer> implements TypeConverter<Integer>
{
    public UnsignedShortType() {
        super(Integer.class);
    }
    
    @Override
    public Integer read(final ByteBuf buffer) {
        return buffer.readUnsignedShort();
    }
    
    @Override
    public void write(final ByteBuf buffer, final Integer object) {
        buffer.writeShort(object);
    }
    
    @Override
    public Integer from(final Object o) {
        if (o instanceof Number) {
            return ((Number)o).intValue();
        }
        if (o instanceof Boolean) {
            return ((boolean)o) ? 1 : 0;
        }
        return (Integer)o;
    }
}
