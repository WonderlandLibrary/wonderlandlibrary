// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.api.type.types;

import io.netty.buffer.ByteBuf;
import com.viaversion.viaversion.api.type.Type;

public class LongArrayType extends Type<long[]>
{
    public LongArrayType() {
        super(long[].class);
    }
    
    @Override
    public long[] read(final ByteBuf buffer) throws Exception {
        final int length = Type.VAR_INT.readPrimitive(buffer);
        final long[] array = new long[length];
        for (int i = 0; i < array.length; ++i) {
            array[i] = Type.LONG.readPrimitive(buffer);
        }
        return array;
    }
    
    @Override
    public void write(final ByteBuf buffer, final long[] object) throws Exception {
        Type.VAR_INT.writePrimitive(buffer, object.length);
        for (final long l : object) {
            Type.LONG.writePrimitive(buffer, l);
        }
    }
}
