/*
 * Decompiled with CFR 0.152.
 */
package com.viaversion.viaversion.api.type.types;

import com.viaversion.viaversion.api.type.Type;
import com.viaversion.viaversion.api.type.TypeConverter;
import io.netty.buffer.ByteBuf;

public class ShortType
extends Type<Short>
implements TypeConverter<Short> {
    public ShortType() {
        super(Short.class);
    }

    public short readPrimitive(ByteBuf buffer) {
        return buffer.readShort();
    }

    public void writePrimitive(ByteBuf buffer, short object) {
        buffer.writeShort(object);
    }

    @Override
    @Deprecated
    public Short read(ByteBuf buffer) {
        return buffer.readShort();
    }

    @Override
    @Deprecated
    public void write(ByteBuf buffer, Short object) {
        buffer.writeShort(object.shortValue());
    }

    @Override
    public Short from(Object o2) {
        if (o2 instanceof Number) {
            return ((Number)o2).shortValue();
        }
        if (o2 instanceof Boolean) {
            return (Boolean)o2 != false ? (short)1 : 0;
        }
        return (Short)o2;
    }
}

