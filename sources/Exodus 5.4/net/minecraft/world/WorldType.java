/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.world;

public class WorldType {
    public static final WorldType[] worldTypes = new WorldType[16];
    public static final WorldType LARGE_BIOMES;
    private boolean hasNotificationData;
    private final int worldTypeId;
    public static final WorldType AMPLIFIED;
    public static final WorldType DEFAULT;
    private final int generatorVersion;
    public static final WorldType FLAT;
    private final String worldType;
    private boolean isWorldTypeVersioned;
    public static final WorldType CUSTOMIZED;
    public static final WorldType DEBUG_WORLD;
    public static final WorldType DEFAULT_1_1;
    private boolean canBeCreated;

    private WorldType setNotificationData() {
        this.hasNotificationData = true;
        return this;
    }

    private WorldType(int n, String string, int n2) {
        this.worldType = string;
        this.generatorVersion = n2;
        this.canBeCreated = true;
        this.worldTypeId = n;
        WorldType.worldTypes[n] = this;
    }

    public boolean showWorldInfoNotice() {
        return this.hasNotificationData;
    }

    public int getWorldTypeID() {
        return this.worldTypeId;
    }

    private WorldType(int n, String string) {
        this(n, string, 0);
    }

    public String getWorldTypeName() {
        return this.worldType;
    }

    public boolean isVersioned() {
        return this.isWorldTypeVersioned;
    }

    public static WorldType parseWorldType(String string) {
        int n = 0;
        while (n < worldTypes.length) {
            if (worldTypes[n] != null && WorldType.worldTypes[n].worldType.equalsIgnoreCase(string)) {
                return worldTypes[n];
            }
            ++n;
        }
        return null;
    }

    static {
        DEFAULT = new WorldType(0, "default", 1).setVersioned();
        FLAT = new WorldType(1, "flat");
        LARGE_BIOMES = new WorldType(2, "largeBiomes");
        AMPLIFIED = new WorldType(3, "amplified").setNotificationData();
        CUSTOMIZED = new WorldType(4, "customized");
        DEBUG_WORLD = new WorldType(5, "debug_all_block_states");
        DEFAULT_1_1 = new WorldType(8, "default_1_1", 0).setCanBeCreated(false);
    }

    public boolean getCanBeCreated() {
        return this.canBeCreated;
    }

    public int getGeneratorVersion() {
        return this.generatorVersion;
    }

    public String getTranslateName() {
        return "generator." + this.worldType;
    }

    public String func_151359_c() {
        return String.valueOf(this.getTranslateName()) + ".info";
    }

    public WorldType getWorldTypeForGeneratorVersion(int n) {
        return this == DEFAULT && n == 0 ? DEFAULT_1_1 : this;
    }

    private WorldType setVersioned() {
        this.isWorldTypeVersioned = true;
        return this;
    }

    private WorldType setCanBeCreated(boolean bl) {
        this.canBeCreated = bl;
        return this;
    }
}

