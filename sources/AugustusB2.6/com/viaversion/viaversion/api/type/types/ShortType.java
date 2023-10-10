// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.api.type.types;

import io.netty.buffer.ByteBuf;
import com.viaversion.viaversion.api.type.TypeConverter;
import com.viaversion.viaversion.api.type.Type;

public class ShortType extends Type<Short> implements TypeConverter<Short>
{
    public ShortType() {
        super(Short.class);
    }
    
    public short readPrimitive(final ByteBuf buffer) {
        return buffer.readShort();
    }
    
    public void writePrimitive(final ByteBuf buffer, final short object) {
        buffer.writeShort(object);
    }
    
    @Deprecated
    @Override
    public Short read(final ByteBuf buffer) {
        return buffer.readShort();
    }
    
    @Deprecated
    @Override
    public void write(final ByteBuf buffer, final Short object) {
        buffer.writeShort(object);
    }
    
    @Override
    public Short from(final Object o) {
        if (o instanceof Number) {
            return ((Number)o).shortValue();
        }
        if (o instanceof Boolean) {
            return (short)(((boolean)o) ? 1 : 0);
        }
        return (Short)o;
    }
}
