// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.api.type.types;

import io.netty.buffer.ByteBuf;
import com.viaversion.viaversion.api.type.TypeConverter;
import com.viaversion.viaversion.api.type.Type;

public class ByteType extends Type<Byte> implements TypeConverter<Byte>
{
    public ByteType() {
        super(Byte.class);
    }
    
    public byte readPrimitive(final ByteBuf buffer) {
        return buffer.readByte();
    }
    
    public void writePrimitive(final ByteBuf buffer, final byte object) {
        buffer.writeByte(object);
    }
    
    @Deprecated
    @Override
    public Byte read(final ByteBuf buffer) {
        return buffer.readByte();
    }
    
    @Deprecated
    @Override
    public void write(final ByteBuf buffer, final Byte object) {
        buffer.writeByte(object);
    }
    
    @Override
    public Byte from(final Object o) {
        if (o instanceof Number) {
            return ((Number)o).byteValue();
        }
        if (o instanceof Boolean) {
            return (byte)(((boolean)o) ? 1 : 0);
        }
        return (Byte)o;
    }
}
