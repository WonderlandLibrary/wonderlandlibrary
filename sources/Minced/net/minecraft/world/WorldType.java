// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.world;

public class WorldType
{
    public static final WorldType[] WORLD_TYPES;
    public static final WorldType DEFAULT;
    public static final WorldType FLAT;
    public static final WorldType LARGE_BIOMES;
    public static final WorldType AMPLIFIED;
    public static final WorldType CUSTOMIZED;
    public static final WorldType DEBUG_ALL_BLOCK_STATES;
    public static final WorldType DEFAULT_1_1;
    private final int id;
    private final String name;
    private final int version;
    private boolean canBeCreated;
    private boolean versioned;
    private boolean hasInfoNotice;
    
    private WorldType(final int id, final String name) {
        this(id, name, 0);
    }
    
    private WorldType(final int id, final String name, final int version) {
        this.name = name;
        this.version = version;
        this.canBeCreated = true;
        this.id = id;
        WorldType.WORLD_TYPES[id] = this;
    }
    
    public String getName() {
        return this.name;
    }
    
    public String getTranslationKey() {
        return "generator." + this.name;
    }
    
    public String getInfoTranslationKey() {
        return this.getTranslationKey() + ".info";
    }
    
    public int getVersion() {
        return this.version;
    }
    
    public WorldType getWorldTypeForGeneratorVersion(final int version) {
        return (this == WorldType.DEFAULT && version == 0) ? WorldType.DEFAULT_1_1 : this;
    }
    
    private WorldType setCanBeCreated(final boolean enable) {
        this.canBeCreated = enable;
        return this;
    }
    
    public boolean canBeCreated() {
        return this.canBeCreated;
    }
    
    private WorldType setVersioned() {
        this.versioned = true;
        return this;
    }
    
    public boolean isVersioned() {
        return this.versioned;
    }
    
    public static WorldType byName(final String type) {
        for (final WorldType worldtype : WorldType.WORLD_TYPES) {
            if (worldtype != null && worldtype.name.equalsIgnoreCase(type)) {
                return worldtype;
            }
        }
        return null;
    }
    
    public int getId() {
        return this.id;
    }
    
    public boolean hasInfoNotice() {
        return this.hasInfoNotice;
    }
    
    private WorldType enableInfoNotice() {
        this.hasInfoNotice = true;
        return this;
    }
    
    static {
        WORLD_TYPES = new WorldType[16];
        DEFAULT = new WorldType(0, "default", 1).setVersioned();
        FLAT = new WorldType(1, "flat");
        LARGE_BIOMES = new WorldType(2, "largeBiomes");
        AMPLIFIED = new WorldType(3, "amplified").enableInfoNotice();
        CUSTOMIZED = new WorldType(4, "customized");
        DEBUG_ALL_BLOCK_STATES = new WorldType(5, "debug_all_block_states");
        DEFAULT_1_1 = new WorldType(8, "default_1_1", 0).setCanBeCreated(false);
    }
}
