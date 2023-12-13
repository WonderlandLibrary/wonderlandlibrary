/*
 * Decompiled with CFR 0.152.
 */
package com.viaversion.viaversion.api.type.types;

import com.viaversion.viaversion.api.type.Type;
import com.viaversion.viaversion.api.type.TypeConverter;
import io.netty.buffer.ByteBuf;

public class IntType
extends Type<Integer>
implements TypeConverter<Integer> {
    public IntType() {
        super(Integer.class);
    }

    @Override
    public Integer read(ByteBuf buffer) {
        return buffer.readInt();
    }

    @Override
    public void write(ByteBuf buffer, Integer object) {
        buffer.writeInt(object);
    }

    @Override
    public Integer from(Object o2) {
        if (o2 instanceof Number) {
            return ((Number)o2).intValue();
        }
        if (o2 instanceof Boolean) {
            return (Boolean)o2 != false ? 1 : 0;
        }
        return (Integer)o2;
    }
}

