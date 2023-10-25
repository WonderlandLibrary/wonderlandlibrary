/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  io.netty.buffer.ByteBuf
 */
package com.viaversion.viaversion.api.type.types.version;

import com.viaversion.viaversion.api.minecraft.metadata.Metadata;
import com.viaversion.viaversion.api.type.Type;
import com.viaversion.viaversion.api.type.types.minecraft.AbstractMetaListType;
import com.viaversion.viaversion.api.type.types.version.Types1_8;
import io.netty.buffer.ByteBuf;

public class MetadataList1_8Type
extends AbstractMetaListType {
    @Override
    protected Type<Metadata> getType() {
        return Types1_8.METADATA;
    }

    @Override
    protected void writeEnd(Type<Metadata> type, ByteBuf buffer) throws Exception {
        buffer.writeByte(127);
    }
}

