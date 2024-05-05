package net.minecraft.client.audio;

import java.util.Random;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.ISound;
import net.minecraft.client.audio.MusicTicker;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.util.ITickable;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;

public class MusicTicker
implements ITickable {
    private final Random rand = new Random();
    private final Minecraft mc;
    private ISound currentMusic;
    private int timeUntilNextMusic = 100;

    public MusicTicker(Minecraft mcIn) {
        this.mc = mcIn;
    }

    public void update() {
        MusicType musicticker$musictype = this.mc.getAmbientMusicType();
        if (this.currentMusic != null) {
            if (!musicticker$musictype.getMusicLocation().equals((Object)this.currentMusic.getSoundLocation())) {
                this.mc.getSoundHandler().stopSound(this.currentMusic);
                this.timeUntilNextMusic = MathHelper.getRandomIntegerInRange((Random)this.rand, (int)0, (int)(musicticker$musictype.getMinDelay() / 2));
            }
            if (!this.mc.getSoundHandler().isSoundPlaying(this.currentMusic)) {
                this.currentMusic = null;
                this.timeUntilNextMusic = Math.min((int)MathHelper.getRandomIntegerInRange((Random)this.rand, (int)musicticker$musictype.getMinDelay(), (int)musicticker$musictype.getMaxDelay()), (int)this.timeUntilNextMusic);
            }
        }
        if (this.currentMusic == null && this.timeUntilNextMusic-- <= 0) {
            this.func_181558_a(musicticker$musictype);
        }
    }

    public void func_181558_a(MusicType p_181558_1_) {
        this.currentMusic = PositionedSoundRecord.create((ResourceLocation)p_181558_1_.getMusicLocation());
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
}
