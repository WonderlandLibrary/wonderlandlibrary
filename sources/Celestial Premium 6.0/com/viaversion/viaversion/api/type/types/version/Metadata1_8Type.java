/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  io.netty.buffer.ByteBuf
 */
package com.viaversion.viaversion.api.type.types.version;

import com.viaversion.viaversion.api.minecraft.metadata.Metadata;
import com.viaversion.viaversion.api.minecraft.metadata.types.MetaType1_8;
import com.viaversion.viaversion.api.type.types.minecraft.MetaTypeTemplate;
import io.netty.buffer.ByteBuf;

public class Metadata1_8Type
extends MetaTypeTemplate {
    @Override
    public Metadata read(ByteBuf buffer) throws Exception {
        byte item = buffer.readByte();
        if (item == 127) {
            return null;
        }
        int typeID = (item & 0xE0) >> 5;
        MetaType1_8 type = MetaType1_8.byId(typeID);
        int id = item & 0x1F;
        return new Metadata(id, type, type.type().read(buffer));
    }

    @Override
    public void write(ByteBuf buffer, Metadata meta) throws Exception {
        byte item = (byte)(meta.metaType().typeId() << 5 | meta.id() & 0x1F);
        buffer.writeByte((int)item);
        meta.metaType().type().write(buffer, meta.getValue());
    }
}

