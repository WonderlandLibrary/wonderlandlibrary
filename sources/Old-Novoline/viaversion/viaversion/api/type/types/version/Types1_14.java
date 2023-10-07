package viaversion.viaversion.api.type.types.version;

import viaversion.viaversion.api.minecraft.metadata.Metadata;
import viaversion.viaversion.api.type.Type;
import viaversion.viaversion.api.type.types.Particle;
import viaversion.viaversion.api.type.types.minecraft.Particle1_14Type;

import java.util.List;

public class Types1_14 {
    /**
     * Metadata list type for 1.14
     */
    public static final Type<List<Metadata>> METADATA_LIST = new MetadataList1_14Type();

    /**
     * Metadata type for 1.14
     */
    public static final Type<Metadata> METADATA = new Metadata1_14Type();

    /**
     * Particle type for 1.14
     */
    public static final Type<Particle> PARTICLE = new Particle1_14Type();
}
