// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.api.type.types;

import io.netty.buffer.ByteBuf;
import com.viaversion.viaversion.api.type.TypeConverter;
import com.viaversion.viaversion.api.type.Type;

public class BooleanType extends Type<Boolean> implements TypeConverter<Boolean>
{
    public BooleanType() {
        super(Boolean.class);
    }
    
    @Override
    public Boolean read(final ByteBuf buffer) {
        return buffer.readBoolean();
    }
    
    @Override
    public void write(final ByteBuf buffer, final Boolean object) {
        buffer.writeBoolean(object);
    }
    
    @Override
    public Boolean from(final Object o) {
        if (o instanceof Number) {
            return ((Number)o).intValue() == 1;
        }
        return (Boolean)o;
    }
}
