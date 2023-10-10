// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.api.type.types.version;

import com.viaversion.viaversion.api.type.types.minecraft.MetaListType;
import com.viaversion.viaversion.api.minecraft.metadata.types.MetaTypes;
import java.util.List;
import com.viaversion.viaversion.api.minecraft.metadata.Metadata;
import com.viaversion.viaversion.api.minecraft.metadata.types.MetaTypes1_13;
import com.viaversion.viaversion.api.type.types.minecraft.ParticleType;
import com.viaversion.viaversion.api.minecraft.chunks.ChunkSection;
import com.viaversion.viaversion.api.type.Type;

public final class Types1_13
{
    public static final Type<ChunkSection> CHUNK_SECTION;
    public static final ParticleType PARTICLE;
    public static final MetaTypes1_13 META_TYPES;
    public static final Type<Metadata> METADATA;
    public static final Type<List<Metadata>> METADATA_LIST;
    
    static {
        CHUNK_SECTION = new ChunkSectionType1_13();
        PARTICLE = new ParticleType();
        META_TYPES = new MetaTypes1_13(Types1_13.PARTICLE);
        METADATA = new MetadataType(Types1_13.META_TYPES);
        METADATA_LIST = new MetaListType(Types1_13.METADATA);
    }
}
