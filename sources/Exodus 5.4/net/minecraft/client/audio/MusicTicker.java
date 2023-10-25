/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.client.audio;

import java.util.Random;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.ISound;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.util.ITickable;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;

public class MusicTicker
implements ITickable {
    private ISound currentMusic;
    private int timeUntilNextMusic = 100;
    private final Minecraft mc;
    private final Random rand = new Random();

    public void func_181558_a(MusicType musicType) {
        this.currentMusic = PositionedSoundRecord.create(musicType.getMusicLocation());
        this.mc.getSoundHandler().playSound(this.currentMusic);
        this.timeUntilNextMusic = Integer.MAX_VALUE;
    }

    public void func_181557_a() {
        if (this.currentMusic != null) {
            this.mc.getSoundHandler().stopSound(this.currentMusic);
            this.currentMusic = null;
            this.timeUntilNextMusic = 0;
        }
    }

    public MusicTicker(Minecraft minecraft) {
        this.mc = minecraft;
    }

    @Override
    public void update() {
        MusicType musicType = this.mc.getAmbientMusicType();
        if (this.currentMusic != null) {
            if (!musicType.getMusicLocation().equals(this.currentMusic.getSoundLocation())) {
                this.mc.getSoundHandler().stopSound(this.currentMusic);
                this.timeUntilNextMusic = MathHelper.getRandomIntegerInRange(this.rand, 0, musicType.getMinDelay() / 2);
            }
            if (!this.mc.getSoundHandler().isSoundPlaying(this.currentMusic)) {
                this.currentMusic = null;
                this.timeUntilNextMusic = Math.min(MathHelper.getRandomIntegerInRange(this.rand, musicType.getMinDelay(), musicType.getMaxDelay()), this.timeUntilNextMusic);
            }
        }
        if (this.currentMusic == null && this.timeUntilNextMusic-- <= 0) {
            this.func_181558_a(musicType);
        }
    }

    public static enum MusicType {
        MENU(new ResourceLocation("minecraft:music.menu"), 20, 600),
        GAME(new ResourceLocation("minecraft:music.game"), 12000, 24000),
        CREATIVE(new ResourceLocation("minecraft:music.game.creative"), 1200, 3600),
        CREDITS(new ResourceLocation("minecraft:music.game.end.credits"), Integer.MAX_VALUE, Integer.MAX_VALUE),
        NETHER(new ResourceLocation("minecraft:music.game.nether"), 1200, 3600),
        END_BOSS(new ResourceLocation("minecraft:music.game.end.dragon"), 0, 0),
        END(new ResourceLocation("minecraft:music.game.end"), 6000, 24000);

        private final int maxDelay;
        private final ResourceLocation musicLocation;
        private final int minDelay;

        private MusicType(ResourceLocation resourceLocation, int n2, int n3) {
            this.musicLocation = resourceLocation;
            this.minDelay = n2;
            this.maxDelay = n3;
        }

        public int getMaxDelay() {
            return this.maxDelay;
        }

        public ResourceLocation getMusicLocation() {
            return this.musicLocation;
        }

        public int getMinDelay() {
            return this.minDelay;
        }
    }
}

