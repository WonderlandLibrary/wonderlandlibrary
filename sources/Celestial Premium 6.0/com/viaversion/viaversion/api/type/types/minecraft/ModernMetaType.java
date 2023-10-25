/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  io.netty.buffer.ByteBuf
 */
package com.viaversion.viaversion.api.type.types.minecraft;

import com.viaversion.viaversion.api.minecraft.metadata.MetaType;
import com.viaversion.viaversion.api.minecraft.metadata.Metadata;
import com.viaversion.viaversion.api.type.types.minecraft.MetaTypeTemplate;
import io.netty.buffer.ByteBuf;

public abstract class ModernMetaType
extends MetaTypeTemplate {
    @Override
    public Metadata read(ByteBuf buffer) throws Exception {
        short index = buffer.readUnsignedByte();
        if (index == 255) {
            return null;
        }
        MetaType type = this.getType(buffer.readByte());
        return new Metadata(index, type, type.type().read(buffer));
    }

    protected abstract MetaType getType(int var1);

    @Override
    public void write(ByteBuf buffer, Metadata object) throws Exception {
        if (object == null) {
            buffer.writeByte(255);
        } else {
            buffer.writeByte(object.id());
            MetaType type = object.metaType();
            buffer.writeByte(type.typeId());
            type.type().write(buffer, object.getValue());
        }
    }
}

