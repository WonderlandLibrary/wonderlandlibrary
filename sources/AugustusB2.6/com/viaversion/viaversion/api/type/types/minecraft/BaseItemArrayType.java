// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.api.type.types.minecraft;

import com.viaversion.viaversion.api.minecraft.item.Item;
import com.viaversion.viaversion.api.type.Type;

public abstract class BaseItemArrayType extends Type<Item[]>
{
    protected BaseItemArrayType() {
        super(Item[].class);
    }
    
    protected BaseItemArrayType(final String typeName) {
        super(typeName, Item[].class);
    }
    
    @Override
    public Class<? extends Type> getBaseClass() {
        return BaseItemArrayType.class;
    }
}
