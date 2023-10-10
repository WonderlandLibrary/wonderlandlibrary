// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.api.type.types.version;

import com.viaversion.viaversion.api.minecraft.metadata.types.MetaType1_8;
import com.viaversion.viaversion.api.minecraft.metadata.MetaType;
import com.viaversion.viaversion.api.type.types.minecraft.OldMetaType;

public class Metadata1_8Type extends OldMetaType
{
    @Override
    protected MetaType getType(final int index) {
        return MetaType1_8.byId(index);
    }
}
