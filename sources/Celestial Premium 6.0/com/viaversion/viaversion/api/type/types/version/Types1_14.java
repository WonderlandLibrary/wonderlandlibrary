/*
 * Decompiled with CFR 0.150.
 */
package com.viaversion.viaversion.api.type.types.version;

import com.viaversion.viaversion.api.minecraft.metadata.Metadata;
import com.viaversion.viaversion.api.type.Type;
import com.viaversion.viaversion.api.type.types.Particle;
import com.viaversion.viaversion.api.type.types.minecraft.MetaListType;
import com.viaversion.viaversion.api.type.types.minecraft.Particle1_14Type;
import com.viaversion.viaversion.api.type.types.version.Metadata1_14Type;
import java.util.List;

public final class Types1_14 {
    public static final Type<Metadata> METADATA = new Metadata1_14Type();
    public static final Type<List<Metadata>> METADATA_LIST = new MetaListType(METADATA);
    public static final Type<Particle> PARTICLE = new Particle1_14Type();
}

