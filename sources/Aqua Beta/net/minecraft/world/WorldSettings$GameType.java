package net.minecraft.world;

import net.minecraft.entity.player.PlayerCapabilities;

public static enum WorldSettings.GameType {
    NOT_SET(-1, ""),
    SURVIVAL(0, "survival"),
    CREATIVE(1, "creative"),
    ADVENTURE(2, "adventure"),
    SPECTATOR(3, "spectator");

    int id;
    String name;

    private WorldSettings.GameType(int typeId, String nameIn) {
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

    public static WorldSettings.GameType getByID(int idIn) {
        for (WorldSettings.GameType worldsettings$gametype : WorldSettings.GameType.values()) {
            if (worldsettings$gametype.id != idIn) continue;
            return worldsettings$gametype;
        }
        return SURVIVAL;
    }

    public static WorldSettings.GameType getByName(String gamemodeName) {
        for (WorldSettings.GameType worldsettings$gametype : WorldSettings.GameType.values()) {
            if (!worldsettings$gametype.name.equals((Object)gamemodeName)) continue;
            return worldsettings$gametype;
        }
        return SURVIVAL;
    }
}
