// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.api.type.types.version;

import com.viaversion.viaversion.api.minecraft.metadata.MetaType;
import com.viaversion.viaversion.api.minecraft.metadata.types.MetaTypes;
import com.viaversion.viaversion.api.type.types.minecraft.ModernMetaType;

public final class MetadataType extends ModernMetaType
{
    private final MetaTypes metaTypes;
    
    public MetadataType(final MetaTypes metaTypes) {
        this.metaTypes = metaTypes;
    }
    
    @Override
    protected MetaType getType(final int index) {
        return this.metaTypes.byId(index);
    }
}
