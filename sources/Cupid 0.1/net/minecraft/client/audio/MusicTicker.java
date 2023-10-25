package net.minecraft.client.audio;

import java.util.Random;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ITickable;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;

public class MusicTicker implements ITickable {
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
      if (!musicticker$musictype.getMusicLocation().equals(this.currentMusic.getSoundLocation())) {
        this.mc.getSoundHandler().stopSound(this.currentMusic);
        this.timeUntilNextMusic = MathHelper.getRandomIntegerInRange(this.rand, 0, musicticker$musictype.getMinDelay() / 2);
      } 
      if (!this.mc.getSoundHandler().isSoundPlaying(this.currentMusic)) {
        this.currentMusic = null;
        this.timeUntilNextMusic = Math.min(MathHelper.getRandomIntegerInRange(this.rand, musicticker$musictype.getMinDelay(), musicticker$musictype.getMaxDelay()), this.timeUntilNextMusic);
      } 
    } 
    if (this.currentMusic == null && this.timeUntilNextMusic-- <= 0)
      func_181558_a(musicticker$musictype); 
  }
  
  public void func_181558_a(MusicType p_181558_1_) {
    this.currentMusic = PositionedSoundRecord.create(p_181558_1_.getMusicLocation());
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
  
  public enum MusicType {
    MENU((String)new ResourceLocation("minecraft:music.menu"), 20, 600),
    GAME((String)new ResourceLocation("minecraft:music.game"), 12000, 24000),
    CREATIVE((String)new ResourceLocation("minecraft:music.game.creative"), 1200, 3600),
    CREDITS((String)new ResourceLocation("minecraft:music.game.end.credits"), 2147483647, 2147483647),
    NETHER((String)new ResourceLocation("minecraft:music.game.nether"), 1200, 3600),
    END_BOSS((String)new ResourceLocation("minecraft:music.game.end.dragon"), 0, 0),
    END((String)new ResourceLocation("minecraft:music.game.end"), 6000, 24000);
    
    private final ResourceLocation musicLocation;
    
    private final int minDelay;
    
    private final int maxDelay;
    
    MusicType(ResourceLocation location, int minDelayIn, int maxDelayIn) {
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
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\net\minecraft\client\audio\MusicTicker.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */