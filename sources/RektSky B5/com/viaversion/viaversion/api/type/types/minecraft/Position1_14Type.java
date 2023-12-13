/*
 * Decompiled with CFR 0.152.
 */
package com.viaversion.viaversion.api.type.types.minecraft;

import com.viaversion.viaversion.api.minecraft.Position;
import com.viaversion.viaversion.api.type.Type;
import io.netty.buffer.ByteBuf;

public class Position1_14Type
extends Type<Position> {
    public Position1_14Type() {
        super(Position.class);
    }

    @Override
    public Position read(ByteBuf buffer) {
        long val2 = buffer.readLong();
        long x2 = val2 >> 38;
        long y2 = val2 << 52 >> 52;
        long z2 = val2 << 26 >> 38;
        return new Position((int)x2, (int)y2, (int)z2);
    }

    @Override
    public void write(ByteBuf buffer, Position object) {
        buffer.writeLong(((long)object.x() & 0x3FFFFFFL) << 38 | (long)(object.y() & 0xFFF) | ((long)object.z() & 0x3FFFFFFL) << 12);
    }
}

