package viaversion.viaversion.api.type.types.version;

import viaversion.viaversion.api.minecraft.metadata.Metadata;
import viaversion.viaversion.api.type.Type;
import viaversion.viaversion.api.type.types.Particle;
import viaversion.viaversion.protocols.protocol1_13_2to1_13_1.types.Particle1_13_2Type;

import java.util.List;

public class Types1_13_2 {
    /**
     * Metadata list type for 1.13
     */
    public static final Type<List<Metadata>> METADATA_LIST = new MetadataList1_13_2Type();

    /**
     * Metadata type for 1.13
     */
    public static final Type<Metadata> METADATA = new Metadata1_13_2Type();

    /**
     * Particle type for 1.13.2
     */
    public static Type<Particle> PARTICLE = new Particle1_13_2Type();
}
