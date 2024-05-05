package net.minecraft.client.audio;

import java.util.Random;
import net.minecraft.client.Minecraft;
import net.minecraft.server.gui.IUpdatePlayerListBox;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;

public class MusicTicker
  implements IUpdatePlayerListBox
{
  private final Random rand = new Random();
  private final Minecraft mc;
  private ISound currentMusic;
  private int timeUntilNextMusic = 100;
  private static final String __OBFID = "CL_00001138";
  
  public MusicTicker(Minecraft mcIn)
  {
    this.mc = mcIn;
  }
  
  public void update()
  {
    MusicType var1 = this.mc.getAmbientMusicType();
    if (this.currentMusic != null)
    {
      if (!var1.getMusicLocation().equals(this.currentMusic.getSoundLocation()))
      {
        this.mc.getSoundHandler().stopSound(this.currentMusic);
        this.timeUntilNextMusic = MathHelper.getRandomIntegerInRange(this.rand, 0, var1.getMinDelay() / 2);
      }
      if (!this.mc.getSoundHandler().isSoundPlaying(this.currentMusic))
      {
        this.currentMusic = null;
        this.timeUntilNextMusic = Math.min(MathHelper.getRandomIntegerInRange(this.rand, var1.getMinDelay(), var1.getMaxDelay()), this.timeUntilNextMusic);
      }
    }
    if ((this.currentMusic == null) && (this.timeUntilNextMusic-- <= 0))
    {
      this.currentMusic = PositionedSoundRecord.createPositionedSoundRecord(var1.getMusicLocation());
      this.mc.getSoundHandler().playSound(this.currentMusic);
      this.timeUntilNextMusic = Integer.MAX_VALUE;
    }
  }
  
  public static enum MusicType
  {
    private final ResourceLocation musicLocation;
    private final int minDelay;
    private final int maxDelay;
    private static final MusicType[] $VALUES = { MENU, GAME, CREATIVE, CREDITS, NETHER, END_BOSS, END };
    private static final String __OBFID = "CL_00001139";
    
    private MusicType(String p_i45111_1_, int p_i45111_2_, ResourceLocation location, int p_i45111_4_, int p_i45111_5_)
    {
      this.musicLocation = location;
      this.minDelay = p_i45111_4_;
      this.maxDelay = p_i45111_5_;
    }
    
    public ResourceLocation getMusicLocation()
    {
      return this.musicLocation;
    }
    
    public int getMinDelay()
    {
      return this.minDelay;
    }
    
    public int getMaxDelay()
    {
      return this.maxDelay;
    }
  }
}
