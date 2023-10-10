// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.api.type.types;

import io.netty.buffer.ByteBuf;
import com.viaversion.viaversion.api.type.TypeConverter;
import com.viaversion.viaversion.api.type.Type;

public class VarIntType extends Type<Integer> implements TypeConverter<Integer>
{
    public VarIntType() {
        super("VarInt", Integer.class);
    }
    
    public int readPrimitive(final ByteBuf buffer) {
        int out = 0;
        int bytes = 0;
        byte in;
        do {
            in = buffer.readByte();
            out |= (in & 0x7F) << bytes++ * 7;
            if (bytes > 5) {
                throw new RuntimeException("VarInt too big");
            }
        } while ((in & 0x80) == 0x80);
        return out;
    }
    
    public void writePrimitive(final ByteBuf buffer, int object) {
        do {
            int part = object & 0x7F;
            object >>>= 7;
            if (object != 0) {
                part |= 0x80;
            }
            buffer.writeByte(part);
        } while (object != 0);
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
