// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.api.type.types.minecraft;

import java.util.Iterator;
import java.util.ArrayList;
import java.util.List;
import io.netty.buffer.ByteBuf;
import com.google.common.base.Preconditions;
import com.viaversion.viaversion.api.minecraft.metadata.Metadata;
import com.viaversion.viaversion.api.type.Type;

public final class MetaListType extends MetaListTypeTemplate
{
    private final Type<Metadata> type;
    
    public MetaListType(final Type<Metadata> type) {
        Preconditions.checkNotNull(type);
        this.type = type;
    }
    
    @Override
    public List<Metadata> read(final ByteBuf buffer) throws Exception {
        final List<Metadata> list = new ArrayList<Metadata>();
        Metadata meta;
        do {
            meta = this.type.read(buffer);
            if (meta != null) {
                list.add(meta);
            }
        } while (meta != null);
        return list;
    }
    
    @Override
    public void write(final ByteBuf buffer, final List<Metadata> object) throws Exception {
        for (final Metadata metadata : object) {
            this.type.write(buffer, metadata);
        }
        this.type.write(buffer, null);
    }
}
