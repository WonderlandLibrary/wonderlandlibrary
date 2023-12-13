// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.client.audio;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.MathHelper;
import net.minecraft.client.Minecraft;
import java.util.Random;
import net.minecraft.server.gui.IUpdatePlayerListBox;

public class MusicTicker implements IUpdatePlayerListBox
{
    private final Random rand;
    private final Minecraft mc;
    private ISound currentMusic;
    private int timeUntilNextMusic;
    private static final String __OBFID = "CL_00001138";
    
    public MusicTicker(final Minecraft mcIn) {
        this.rand = new Random();
        this.timeUntilNextMusic = 100;
        this.mc = mcIn;
    }
    
    @Override
    public void update() {
        final MusicType var1 = this.mc.getAmbientMusicType();
        if (this.currentMusic != null) {
            if (!var1.getMusicLocation().equals(this.currentMusic.getSoundLocation())) {
                this.mc.getSoundHandler().stopSound(this.currentMusic);
                this.timeUntilNextMusic = MathHelper.getRandomIntegerInRange(this.rand, 0, var1.getMinDelay() / 2);
            }
            if (!this.mc.getSoundHandler().isSoundPlaying(this.currentMusic)) {
                this.currentMusic = null;
                this.timeUntilNextMusic = Math.min(MathHelper.getRandomIntegerInRange(this.rand, var1.getMinDelay(), var1.getMaxDelay()), this.timeUntilNextMusic);
            }
        }
        if (this.currentMusic == null && this.timeUntilNextMusic-- <= 0) {
            this.currentMusic = PositionedSoundRecord.createPositionedSoundRecord(var1.getMusicLocation());
            this.mc.getSoundHandler().playSound(this.currentMusic);
            this.timeUntilNextMusic = Integer.MAX_VALUE;
        }
    }
    
    public enum MusicType
    {
        MENU("MENU", 0, new ResourceLocation("minecraft:music.menu"), 20, 600), 
        GAME("GAME", 1, new ResourceLocation("minecraft:music.game"), 12000, 24000), 
        CREATIVE("CREATIVE", 2, new ResourceLocation("minecraft:music.game.creative"), 1200, 3600), 
        CREDITS("CREDITS", 3, new ResourceLocation("minecraft:music.game.end.credits"), Integer.MAX_VALUE, Integer.MAX_VALUE), 
        NETHER("NETHER", 4, new ResourceLocation("minecraft:music.game.nether"), 1200, 3600), 
        END_BOSS("END_BOSS", 5, new ResourceLocation("minecraft:music.game.end.dragon"), 0, 0), 
        END("END", 6, new ResourceLocation("minecraft:music.game.end"), 6000, 24000);
        
        private final ResourceLocation musicLocation;
        private final int minDelay;
        private final int maxDelay;
        private static final MusicType[] $VALUES;
        private static final String __OBFID = "CL_00001139";
        
        private MusicType(final String p_i45111_1_, final int p_i45111_2_, final ResourceLocation location, final int p_i45111_4_, final int p_i45111_5_) {
            this.musicLocation = location;
            this.minDelay = p_i45111_4_;
            this.maxDelay = p_i45111_5_;
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
        
        static {
            $VALUES = new MusicType[] { MusicType.MENU, MusicType.GAME, MusicType.CREATIVE, MusicType.CREDITS, MusicType.NETHER, MusicType.END_BOSS, MusicType.END };
        }
    }
}
