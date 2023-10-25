/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  io.netty.buffer.ByteBuf
 */
package com.viaversion.viaversion.api.type.types.minecraft;

import com.viaversion.viaversion.api.type.Type;
import io.netty.buffer.ByteBuf;
import java.util.UUID;

public class OptUUIDType
extends Type<UUID> {
    public OptUUIDType() {
        super(UUID.class);
    }

    @Override
    public UUID read(ByteBuf buffer) {
        boolean present = buffer.readBoolean();
        if (!present) {
            return null;
        }
        return new UUID(buffer.readLong(), buffer.readLong());
    }

    @Override
    public void write(ByteBuf buffer, UUID object) {
        if (object == null) {
            buffer.writeBoolean(false);
        } else {
            buffer.writeBoolean(true);
            buffer.writeLong(object.getMostSignificantBits());
            buffer.writeLong(object.getLeastSignificantBits());
        }
    }
}

