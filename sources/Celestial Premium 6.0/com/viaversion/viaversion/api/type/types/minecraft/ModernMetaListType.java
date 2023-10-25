/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  io.netty.buffer.ByteBuf
 */
package com.viaversion.viaversion.api.type.types.minecraft;

import com.viaversion.viaversion.api.minecraft.metadata.Metadata;
import com.viaversion.viaversion.api.type.Type;
import com.viaversion.viaversion.api.type.types.minecraft.AbstractMetaListType;
import io.netty.buffer.ByteBuf;

public abstract class ModernMetaListType
extends AbstractMetaListType {
    @Override
    protected void writeEnd(Type<Metadata> type, ByteBuf buffer) throws Exception {
        type.write(buffer, null);
    }
}

