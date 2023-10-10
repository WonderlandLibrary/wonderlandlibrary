// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.api.type.types.version;

import com.viaversion.viaversion.api.type.types.minecraft.MetaListType;
import java.util.List;
import com.viaversion.viaversion.api.minecraft.metadata.Metadata;
import com.viaversion.viaversion.api.type.Type;

public class Types1_12
{
    public static final Type<Metadata> METADATA;
    public static final Type<List<Metadata>> METADATA_LIST;
    
    static {
        METADATA = new Metadata1_12Type();
        METADATA_LIST = new MetaListType(Types1_12.METADATA);
    }
}
