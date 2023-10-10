// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.api.type.types.minecraft;

import com.viaversion.viaversion.api.minecraft.metadata.Metadata;
import java.util.List;
import com.viaversion.viaversion.api.type.Type;

public abstract class MetaListTypeTemplate extends Type<List<Metadata>>
{
    protected MetaListTypeTemplate() {
        super("MetaData List", List.class);
    }
    
    @Override
    public Class<? extends Type> getBaseClass() {
        return MetaListTypeTemplate.class;
    }
}
