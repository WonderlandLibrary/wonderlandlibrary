// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.world;

import net.minecraft.entity.player.PlayerCapabilities;

public enum GameType
{
    NOT_SET(-1, "", ""), 
    SURVIVAL(0, "survival", "s"), 
    CREATIVE(1, "creative", "c"), 
    ADVENTURE(2, "adventure", "a"), 
    SPECTATOR(3, "spectator", "sp");
    
    int id;
    String name;
    String shortName;
    
    private GameType(final int idIn, final String nameIn, final String shortNameIn) {
        this.id = idIn;
        this.name = nameIn;
        this.shortName = shortNameIn;
    }
    
    public int getID() {
        return this.id;
    }
    
    public String getName() {
        return this.name;
    }
    
    public void configurePlayerCapabilities(final PlayerCapabilities capabilities) {
        if (this == GameType.CREATIVE) {
            capabilities.allowFlying = true;
            capabilities.isCreativeMode = true;
            capabilities.disableDamage = true;
        }
        else if (this == GameType.SPECTATOR) {
            capabilities.allowFlying = true;
            capabilities.isCreativeMode = false;
            capabilities.disableDamage = true;
            capabilities.isFlying = true;
        }
        else {
            capabilities.allowFlying = false;
            capabilities.isCreativeMode = false;
            capabilities.disableDamage = false;
            capabilities.isFlying = false;
        }
        capabilities.allowEdit = !this.hasLimitedInteractions();
    }
    
    public boolean hasLimitedInteractions() {
        return this == GameType.ADVENTURE || this == GameType.SPECTATOR;
    }
    
    public boolean isCreative() {
        return this == GameType.CREATIVE;
    }
    
    public boolean isSurvivalOrAdventure() {
        return this == GameType.SURVIVAL || this == GameType.ADVENTURE;
    }
    
    public static GameType getByID(final int idIn) {
        return parseGameTypeWithDefault(idIn, GameType.SURVIVAL);
    }
    
    public static GameType parseGameTypeWithDefault(final int targetId, final GameType fallback) {
        for (final GameType gametype : values()) {
            if (gametype.id == targetId) {
                return gametype;
            }
        }
        return fallback;
    }
    
    public static GameType getByName(final String gamemodeName) {
        return parseGameTypeWithDefault(gamemodeName, GameType.SURVIVAL);
    }
    
    public static GameType parseGameTypeWithDefault(final String targetName, final GameType fallback) {
        for (final GameType gametype : values()) {
            if (gametype.name.equals(targetName) || gametype.shortName.equals(targetName)) {
                return gametype;
            }
        }
        return fallback;
    }
}
