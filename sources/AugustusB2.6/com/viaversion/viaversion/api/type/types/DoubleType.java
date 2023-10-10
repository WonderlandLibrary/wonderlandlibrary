// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.api.type.types;

import io.netty.buffer.ByteBuf;
import com.viaversion.viaversion.api.type.TypeConverter;
import com.viaversion.viaversion.api.type.Type;

public class DoubleType extends Type<Double> implements TypeConverter<Double>
{
    public DoubleType() {
        super(Double.class);
    }
    
    @Deprecated
    @Override
    public Double read(final ByteBuf buffer) {
        return buffer.readDouble();
    }
    
    public double readPrimitive(final ByteBuf buffer) {
        return buffer.readDouble();
    }
    
    @Deprecated
    @Override
    public void write(final ByteBuf buffer, final Double object) {
        buffer.writeDouble(object);
    }
    
    public void writePrimitive(final ByteBuf buffer, final double object) {
        buffer.writeDouble(object);
    }
    
    @Override
    public Double from(final Object o) {
        if (o instanceof Number) {
            return ((Number)o).doubleValue();
        }
        if (o instanceof Boolean) {
            return o ? 1.0 : 0.0;
        }
        return (Double)o;
    }
}
