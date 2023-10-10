// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.api.type.types;

import com.google.common.base.Preconditions;
import io.netty.buffer.ByteBuf;
import com.viaversion.viaversion.api.type.Type;

public class VarIntArrayType extends Type<int[]>
{
    public VarIntArrayType() {
        super(int[].class);
    }
    
    @Override
    public int[] read(final ByteBuf buffer) throws Exception {
        final int length = Type.VAR_INT.readPrimitive(buffer);
        Preconditions.checkArgument(buffer.isReadable(length));
        final int[] array = new int[length];
        for (int i = 0; i < array.length; ++i) {
            array[i] = Type.VAR_INT.readPrimitive(buffer);
        }
        return array;
    }
    
    @Override
    public void write(final ByteBuf buffer, final int[] object) throws Exception {
        Type.VAR_INT.writePrimitive(buffer, object.length);
        for (final int i : object) {
            Type.VAR_INT.writePrimitive(buffer, i);
        }
    }
}
