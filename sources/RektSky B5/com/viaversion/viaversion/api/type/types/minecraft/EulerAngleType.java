/*
 * Decompiled with CFR 0.152.
 */
package com.viaversion.viaversion.api.type.types.minecraft;

import com.viaversion.viaversion.api.minecraft.EulerAngle;
import com.viaversion.viaversion.api.type.Type;
import io.netty.buffer.ByteBuf;

public class EulerAngleType
extends Type<EulerAngle> {
    public EulerAngleType() {
        super(EulerAngle.class);
    }

    @Override
    public EulerAngle read(ByteBuf buffer) throws Exception {
        float x2 = Type.FLOAT.readPrimitive(buffer);
        float y2 = Type.FLOAT.readPrimitive(buffer);
        float z2 = Type.FLOAT.readPrimitive(buffer);
        return new EulerAngle(x2, y2, z2);
    }

    @Override
    public void write(ByteBuf buffer, EulerAngle object) throws Exception {
        Type.FLOAT.writePrimitive(buffer, object.x());
        Type.FLOAT.writePrimitive(buffer, object.y());
        Type.FLOAT.writePrimitive(buffer, object.z());
    }
}

