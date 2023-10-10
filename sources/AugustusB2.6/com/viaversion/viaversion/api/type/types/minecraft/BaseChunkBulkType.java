// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.api.type.types.minecraft;

import com.viaversion.viaversion.api.minecraft.chunks.Chunk;
import com.viaversion.viaversion.api.type.Type;

public abstract class BaseChunkBulkType extends Type<Chunk[]>
{
    protected BaseChunkBulkType() {
        super(Chunk[].class);
    }
    
    protected BaseChunkBulkType(final String typeName) {
        super(typeName, Chunk[].class);
    }
    
    @Override
    public Class<? extends Type> getBaseClass() {
        return BaseChunkBulkType.class;
    }
}
