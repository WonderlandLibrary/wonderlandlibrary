/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  io.netty.buffer.ByteBuf
 */
package com.viaversion.viaversion.api.type.types;

import com.viaversion.viaversion.api.type.Type;
import com.viaversion.viaversion.api.type.TypeConverter;
import io.netty.buffer.ByteBuf;

public class ByteType
extends Type<Byte>
implements TypeConverter<Byte> {
    public ByteType() {
        super(Byte.class);
    }

    @Override
    public Byte read(ByteBuf buffer) {
        return buffer.readByte();
    }

    @Override
    public void write(ByteBuf buffer, Byte object) {
        buffer.writeByte((int)object.byteValue());
    }

    @Override
    public Byte from(Object o) {
        if (o instanceof Number) {
            return ((Number)o).byteValue();
        }
        if (o instanceof Boolean) {
            return (Boolean)o != false ? (byte)1 : 0;
        }
        return (Byte)o;
    }
}

