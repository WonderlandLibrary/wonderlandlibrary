/*
 * Decompiled with CFR 0.152.
 */
package com.viaversion.viaversion.api.type.types;

import com.viaversion.viaversion.api.type.Type;
import com.viaversion.viaversion.api.type.TypeConverter;
import io.netty.buffer.ByteBuf;

public class DoubleType
extends Type<Double>
implements TypeConverter<Double> {
    public DoubleType() {
        super(Double.class);
    }

    @Override
    @Deprecated
    public Double read(ByteBuf buffer) {
        return buffer.readDouble();
    }

    public double readPrimitive(ByteBuf buffer) {
        return buffer.readDouble();
    }

    @Override
    @Deprecated
    public void write(ByteBuf buffer, Double object) {
        buffer.writeDouble(object);
    }

    public void writePrimitive(ByteBuf buffer, double object) {
        buffer.writeDouble(object);
    }

    @Override
    public Double from(Object o2) {
        if (o2 instanceof Number) {
            return ((Number)o2).doubleValue();
        }
        if (o2 instanceof Boolean) {
            return (Boolean)o2 != false ? 1.0 : 0.0;
        }
        return (Double)o2;
    }
}

