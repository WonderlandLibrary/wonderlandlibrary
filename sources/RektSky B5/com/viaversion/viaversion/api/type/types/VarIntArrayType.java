/*
 * Decompiled with CFR 0.152.
 */
package com.viaversion.viaversion.api.type.types;

import com.google.common.base.Preconditions;
import com.viaversion.viaversion.api.type.Type;
import io.netty.buffer.ByteBuf;

public class VarIntArrayType
extends Type<int[]> {
    public VarIntArrayType() {
        super(int[].class);
    }

    @Override
    public int[] read(ByteBuf buffer) throws Exception {
        int length = Type.VAR_INT.readPrimitive(buffer);
        Preconditions.checkArgument(buffer.isReadable(length));
        int[] array = new int[length];
        for (int i2 = 0; i2 < array.length; ++i2) {
            array[i2] = Type.VAR_INT.readPrimitive(buffer);
        }
        return array;
    }

    @Override
    public void write(ByteBuf buffer, int[] object) throws Exception {
        Type.VAR_INT.writePrimitive(buffer, object.length);
        for (int i2 : object) {
            Type.VAR_INT.writePrimitive(buffer, i2);
        }
    }
}

