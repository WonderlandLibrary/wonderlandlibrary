/*
 * Decompiled with CFR 0.152.
 */
package com.viaversion.viaversion.api.type.types.minecraft;

import com.google.common.base.Preconditions;
import com.viaversion.viaversion.api.type.Type;
import com.viaversion.viaversion.libs.opennbt.NBTIO;
import com.viaversion.viaversion.libs.opennbt.tag.builtin.CompoundTag;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufInputStream;
import io.netty.buffer.ByteBufOutputStream;

public class NBTType
extends Type<CompoundTag> {
    public NBTType() {
        super(CompoundTag.class);
    }

    @Override
    public CompoundTag read(ByteBuf buffer) throws Exception {
        Preconditions.checkArgument(buffer.readableBytes() <= 0x200000, "Cannot read NBT (got %s bytes)", buffer.readableBytes());
        int readerIndex = buffer.readerIndex();
        byte b2 = buffer.readByte();
        if (b2 == 0) {
            return null;
        }
        buffer.readerIndex(readerIndex);
        return NBTIO.readTag(new ByteBufInputStream(buffer));
    }

    @Override
    public void write(ByteBuf buffer, CompoundTag object) throws Exception {
        if (object == null) {
            buffer.writeByte(0);
        } else {
            ByteBufOutputStream bytebufStream = new ByteBufOutputStream(buffer);
            NBTIO.writeTag(bytebufStream, object);
        }
    }
}

