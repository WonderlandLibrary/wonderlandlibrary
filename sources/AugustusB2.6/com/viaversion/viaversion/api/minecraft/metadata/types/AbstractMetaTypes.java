// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.api.minecraft.metadata.types;

import com.viaversion.viaversion.api.type.Type;
import com.viaversion.viaversion.api.minecraft.metadata.MetaType;

public abstract class AbstractMetaTypes implements MetaTypes
{
    private final MetaType[] values;
    
    protected AbstractMetaTypes(final int values) {
        this.values = new MetaType[values];
    }
    
    @Override
    public MetaType byId(final int id) {
        return this.values[id];
    }
    
    @Override
    public MetaType[] values() {
        return this.values;
    }
    
    protected MetaType add(final int typeId, final Type<?> type) {
        final MetaType metaType = MetaType.create(typeId, type);
        return this.values[typeId] = metaType;
    }
}
