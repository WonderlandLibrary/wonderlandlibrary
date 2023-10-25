/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  io.netty.buffer.ByteBuf
 */
package com.viaversion.viaversion.api.type.types.minecraft;

import com.viaversion.viaversion.api.minecraft.Vector;
import com.viaversion.viaversion.api.type.Type;
import io.netty.buffer.ByteBuf;

public class VectorType
extends Type<Vector> {
    public VectorType() {
        super(Vector.class);
    }

    @Override
    public Vector read(ByteBuf buffer) throws Exception {
        int x = Type.INT.read(buffer);
        int y = Type.INT.read(buffer);
        int z = Type.INT.read(buffer);
        return new Vector(x, y, z);
    }

    @Override
    public void write(ByteBuf buffer, Vector object) throws Exception {
        Type.INT.write(buffer, object.getBlockX());
        Type.INT.write(buffer, object.getBlockY());
        Type.INT.write(buffer, object.getBlockZ());
    }
}

