/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.world;

import net.minecraft.entity.player.PlayerCapabilities;
import net.minecraft.world.WorldType;
import net.minecraft.world.storage.WorldInfo;

public final class WorldSettings {
    private String worldName = "";
    private final long seed;
    private final boolean mapFeaturesEnabled;
    private final GameType theGameType;
    private final WorldType terrainType;
    private boolean bonusChestEnabled;
    private boolean commandsAllowed;
    private final boolean hardcoreEnabled;

    public WorldSettings(WorldInfo worldInfo) {
        this(worldInfo.getSeed(), worldInfo.getGameType(), worldInfo.isMapFeaturesEnabled(), worldInfo.isHardcoreModeEnabled(), worldInfo.getTerrainType());
    }

    public WorldSettings(long l, GameType gameType, boolean bl, boolean bl2, WorldType worldType) {
        this.seed = l;
        this.theGameType = gameType;
        this.mapFeaturesEnabled = bl;
        this.hardcoreEnabled = bl2;
        this.terrainType = worldType;
    }

    public WorldType getTerrainType() {
        return this.terrainType;
    }

    public boolean isMapFeaturesEnabled() {
        return this.mapFeaturesEnabled;
    }

    public WorldSettings enableBonusChest() {
        this.bonusChestEnabled = true;
        return this;
    }

    public boolean areCommandsAllowed() {
        return this.commandsAllowed;
    }

    public String getWorldName() {
        return this.worldName;
    }

    public GameType getGameType() {
        return this.theGameType;
    }

    public WorldSettings enableCommands() {
        this.commandsAllowed = true;
        return this;
    }

    public long getSeed() {
        return this.seed;
    }

    public boolean isBonusChestEnabled() {
        return this.bonusChestEnabled;
    }

    public boolean getHardcoreEnabled() {
        return this.hardcoreEnabled;
    }

    public static GameType getGameTypeById(int n) {
        return GameType.getByID(n);
    }

    public WorldSettings setWorldName(String string) {
        this.worldName = string;
        return this;
    }

    public static enum GameType {
        NOT_SET(-1, ""),
        SURVIVAL(0, "survival"),
        CREATIVE(1, "creative"),
        ADVENTURE(2, "adventure"),
        SPECTATOR(3, "spectator");

        int id;
        String name;

        public String getName() {
            return this.name;
        }

        public boolean isSurvivalOrAdventure() {
            return this == SURVIVAL || this == ADVENTURE;
        }

        public static GameType getByID(int n) {
            GameType[] gameTypeArray = GameType.values();
            int n2 = gameTypeArray.length;
            int n3 = 0;
            while (n3 < n2) {
                GameType gameType = gameTypeArray[n3];
                if (gameType.id == n) {
                    return gameType;
                }
                ++n3;
            }
            return SURVIVAL;
        }

        public int getID() {
            return this.id;
        }

        public static GameType getByName(String string) {
            GameType[] gameTypeArray = GameType.values();
            int n = gameTypeArray.length;
            int n2 = 0;
            while (n2 < n) {
                GameType gameType = gameTypeArray[n2];
                if (gameType.name.equals(string)) {
                    return gameType;
                }
                ++n2;
            }
            return SURVIVAL;
        }

        public boolean isCreative() {
            return this == CREATIVE;
        }

        public boolean isAdventure() {
            return this == ADVENTURE || this == SPECTATOR;
        }

        private GameType(int n2, String string2) {
            this.id = n2;
            this.name = string2;
        }

        public void configurePlayerCapabilities(PlayerCapabilities playerCapabilities) {
            if (this == CREATIVE) {
                playerCapabilities.allowFlying = true;
                playerCapabilities.isCreativeMode = true;
                playerCapabilities.disableDamage = true;
            } else if (this == SPECTATOR) {
                playerCapabilities.allowFlying = true;
                playerCapabilities.isCreativeMode = false;
                playerCapabilities.disableDamage = true;
                playerCapabilities.isFlying = true;
            } else {
                playerCapabilities.allowFlying = false;
                playerCapabilities.isCreativeMode = false;
                playerCapabilities.disableDamage = false;
                playerCapabilities.isFlying = false;
            }
            playerCapabilities.allowEdit = !this.isAdventure();
        }
    }
}

