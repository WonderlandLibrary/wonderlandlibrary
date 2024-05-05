package net.minecraft.client.audio;

import net.minecraft.util.ResourceLocation;

public static enum MusicTicker.MusicType {
    MENU(new ResourceLocation("minecraft:music.menu"), 20, 600),
    GAME(new ResourceLocation("minecraft:music.game"), 12000, 24000),
    CREATIVE(new ResourceLocation("minecraft:music.game.creative"), 1200, 3600),
    CREDITS(new ResourceLocation("minecraft:music.game.end.credits"), Integer.MAX_VALUE, Integer.MAX_VALUE),
    NETHER(new ResourceLocation("minecraft:music.game.nether"), 1200, 3600),
    END_BOSS(new ResourceLocation("minecraft:music.game.end.dragon"), 0, 0),
    END(new ResourceLocation("minecraft:music.game.end"), 6000, 24000);

    private final ResourceLocation musicLocation;
    private final int minDelay;
    private final int maxDelay;

    private MusicTicker.MusicType(ResourceLocation location, int minDelayIn, int maxDelayIn) {
        this.musicLocation = location;
        this.minDelay = minDelayIn;
        this.maxDelay = maxDelayIn;
    }

    public ResourceLocation getMusicLocation() {
        return this.musicLocation;
    }

    public int getMinDelay() {
        return this.minDelay;
    }

    public int getMaxDelay() {
        return this.maxDelay;
    }
}
