/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.world;

import net.minecraft.entity.player.PlayerCapabilities;
import net.minecraft.world.WorldType;
import net.minecraft.world.storage.WorldInfo;

public final class WorldSettings {
    private final long seed;
    private final GameType theGameType;
    private final boolean mapFeaturesEnabled;
    private final boolean hardcoreEnabled;
    private final WorldType terrainType;
    private boolean commandsAllowed;
    private boolean bonusChestEnabled;
    private String worldName = "";
    private static final String __OBFID = "CL_00000147";

    public WorldSettings(long seedIn, GameType gameType, boolean enableMapFeatures, boolean hardcoreMode, WorldType worldTypeIn) {
        this.seed = seedIn;
        this.theGameType = gameType;
        this.mapFeaturesEnabled = enableMapFeatures;
        this.hardcoreEnabled = hardcoreMode;
        this.terrainType = worldTypeIn;
    }

    public WorldSettings(WorldInfo info) {
        this(info.getSeed(), info.getGameType(), info.isMapFeaturesEnabled(), info.isHardcoreModeEnabled(), info.getTerrainType());
    }

    public WorldSettings enableBonusChest() {
        this.bonusChestEnabled = true;
        return this;
    }

    public WorldSettings enableCommands() {
        this.commandsAllowed = true;
        return this;
    }

    public WorldSettings setWorldName(String name) {
        this.worldName = name;
        return this;
    }

    public boolean isBonusChestEnabled() {
        return this.bonusChestEnabled;
    }

    public long getSeed() {
        return this.seed;
    }

    public GameType getGameType() {
        return this.theGameType;
    }

    public boolean getHardcoreEnabled() {
        return this.hardcoreEnabled;
    }

    public boolean isMapFeaturesEnabled() {
        return this.mapFeaturesEnabled;
    }

    public WorldType getTerrainType() {
        return this.terrainType;
    }

    public boolean areCommandsAllowed() {
        return this.commandsAllowed;
    }

    public static GameType getGameTypeById(int id) {
        return GameType.getByID(id);
    }

    public String getWorldName() {
        return this.worldName;
    }

    public static enum GameType {
        NOT_SET("NOT_SET", 0, -1, ""),
        SURVIVAL("SURVIVAL", 1, 0, "survival"),
        CREATIVE("CREATIVE", 2, 1, "creative"),
        ADVENTURE("ADVENTURE", 3, 2, "adventure"),
        SPECTATOR("SPECTATOR", 4, 3, "spectator");

        int id;
        String name;
        private static final GameType[] $VALUES;
        private static final String __OBFID = "CL_00000148";

        static {
            $VALUES = new GameType[]{NOT_SET, SURVIVAL, CREATIVE, ADVENTURE, SPECTATOR};
        }

        private GameType(String p_i1956_1_, int p_i1956_2_, int typeId, String nameIn) {
            this.id = typeId;
            this.name = nameIn;
        }

        public int getID() {
            return this.id;
        }

        public String getName() {
            return this.name;
        }

        public void configurePlayerCapabilities(PlayerCapabilities capabilities) {
            if (this == CREATIVE) {
                capabilities.allowFlying = true;
                capabilities.isCreativeMode = true;
                capabilities.disableDamage = true;
            } else if (this == SPECTATOR) {
                capabilities.allowFlying = true;
                capabilities.isCreativeMode = false;
                capabilities.disableDamage = true;
                capabilities.isFlying = true;
            } else {
                capabilities.allowFlying = false;
                capabilities.isCreativeMode = false;
                capabilities.disableDamage = false;
                capabilities.isFlying = false;
            }
            capabilities.allowEdit = !this.isAdventure();
        }

        public boolean isAdventure() {
            return this == ADVENTURE || this == SPECTATOR;
        }

        public boolean isCreative() {
            return this == CREATIVE;
        }

        public boolean isSurvivalOrAdventure() {
            return this == SURVIVAL || this == ADVENTURE;
        }

        public static GameType getByID(int idIn) {
            for (GameType var4 : GameType.values()) {
                if (var4.id != idIn) continue;
                return var4;
            }
            return SURVIVAL;
        }

        public static GameType getByName(String p_77142_0_) {
            for (GameType var4 : GameType.values()) {
                if (!var4.name.equals(p_77142_0_)) continue;
                return var4;
            }
            return SURVIVAL;
        }
    }
}

