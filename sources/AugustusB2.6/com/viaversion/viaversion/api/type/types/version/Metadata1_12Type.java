// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.api.type.types.version;

import com.viaversion.viaversion.api.minecraft.metadata.types.MetaType1_12;
import com.viaversion.viaversion.api.minecraft.metadata.MetaType;
import com.viaversion.viaversion.api.type.types.minecraft.ModernMetaType;

public class Metadata1_12Type extends ModernMetaType
{
    @Override
    protected MetaType getType(final int index) {
        return MetaType1_12.byId(index);
    }
}
