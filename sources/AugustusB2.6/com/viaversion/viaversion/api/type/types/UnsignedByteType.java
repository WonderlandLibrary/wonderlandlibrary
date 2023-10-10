// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.api.type.types;

import io.netty.buffer.ByteBuf;
import com.viaversion.viaversion.api.type.TypeConverter;
import com.viaversion.viaversion.api.type.Type;

public class UnsignedByteType extends Type<Short> implements TypeConverter<Short>
{
    public UnsignedByteType() {
        super("Unsigned Byte", Short.class);
    }
    
    @Override
    public Short read(final ByteBuf buffer) {
        return buffer.readUnsignedByte();
    }
    
    @Override
    public void write(final ByteBuf buffer, final Short object) {
        buffer.writeByte(object);
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
