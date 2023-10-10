// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.api.type.types.version;

import com.viaversion.viaversion.api.type.types.minecraft.MetaListType;
import com.viaversion.viaversion.api.minecraft.chunks.ChunkSection;
import java.util.List;
import com.viaversion.viaversion.api.minecraft.metadata.Metadata;
import com.viaversion.viaversion.api.type.Type;

public class Types1_8
{
    public static final Type<Metadata> METADATA;
    public static final Type<List<Metadata>> METADATA_LIST;
    public static final Type<ChunkSection> CHUNK_SECTION;
    
    static {
        METADATA = new Metadata1_8Type();
        METADATA_LIST = new MetaListType(Types1_8.METADATA);
        CHUNK_SECTION = new ChunkSectionType1_8();
    }
}
