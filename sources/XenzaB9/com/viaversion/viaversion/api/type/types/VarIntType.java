// 
// Decompiled by Procyon v0.6.0
// 

package com.viaversion.viaversion.api.type.types;

import io.netty.buffer.ByteBuf;
import com.viaversion.viaversion.api.type.TypeConverter;
import com.viaversion.viaversion.api.type.Type;

public class VarIntType extends Type<Integer> implements TypeConverter<Integer>
{
    private static final int CONTINUE_BIT = 128;
    private static final int VALUE_BITS = 127;
    private static final int MULTI_BYTE_BITS = -128;
    private static final int MAX_BYTES = 5;
    
    public VarIntType() {
        super("VarInt", Integer.class);
    }
    
    public int readPrimitive(final ByteBuf buffer) {
        int value = 0;
        int bytes = 0;
        byte in;
        do {
            in = buffer.readByte();
            value |= (in & 0x7F) << bytes++ * 7;
            if (bytes > 5) {
                throw new RuntimeException("VarInt too big");
            }
        } while ((in & 0x80) == 0x80);
        return value;
    }
    
    public void writePrimitive(final ByteBuf buffer, int value) {
        while ((value & 0xFFFFFF80) != 0x0) {
            buffer.writeByte((value & 0x7F) | 0x80);
            value >>>= 7;
        }
        buffer.writeByte(value);
    }
    
    @Deprecated
    @Override
    public Integer read(final ByteBuf buffer) {
        return this.readPrimitive(buffer);
    }
    
    @Deprecated
    @Override
    public void write(final ByteBuf buffer, final Integer object) {
        this.writePrimitive(buffer, object);
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
