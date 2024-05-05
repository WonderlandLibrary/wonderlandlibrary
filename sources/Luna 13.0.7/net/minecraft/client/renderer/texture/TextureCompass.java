package net.minecraft.client.renderer.texture;

import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.WorldProvider;
import optifine.Config;
import shadersmod.client.ShadersTex;

public class TextureCompass
  extends TextureAtlasSprite
{
  public double currentAngle;
  public double angleDelta;
  public static String field_176608_l;
  private static final String __OBFID = "CL_00001071";
  
  public TextureCompass(String p_i1286_1_)
  {
    super(p_i1286_1_);
    field_176608_l = p_i1286_1_;
  }
  
  public void updateAnimation()
  {
    Minecraft var1 = Minecraft.getMinecraft();
    if ((Minecraft.theWorld != null) && (Minecraft.thePlayer != null)) {
      updateCompass(Minecraft.theWorld, Minecraft.thePlayer.posX, Minecraft.thePlayer.posZ, Minecraft.thePlayer.rotationYaw, false, false);
    } else {
      updateCompass(null, 0.0D, 0.0D, 0.0D, true, false);
    }
  }
  
  public void updateCompass(World worldIn, double p_94241_2_, double p_94241_4_, double p_94241_6_, boolean p_94241_8_, boolean p_94241_9_)
  {
    if (!this.framesTextureData.isEmpty())
    {
      double var10 = 0.0D;
      if ((worldIn != null) && (!p_94241_8_))
      {
        BlockPos var18 = worldIn.getSpawnPoint();
        double var13 = var18.getX() - p_94241_2_;
        double var15 = var18.getZ() - p_94241_4_;
        p_94241_6_ %= 360.0D;
        var10 = -((p_94241_6_ - 90.0D) * 3.141592653589793D / 180.0D - Math.atan2(var15, var13));
        if (!worldIn.provider.isSurfaceWorld()) {
          var10 = Math.random() * 3.141592653589793D * 2.0D;
        }
      }
      if (p_94241_9_)
      {
        this.currentAngle = var10;
      }
      else
      {
        for (double var181 = var10 - this.currentAngle; var181 < -3.141592653589793D; var181 += 6.283185307179586D) {}
        while (var181 >= 3.141592653589793D) {
          var181 -= 6.283185307179586D;
        }
        var181 = MathHelper.clamp_double(var181, -1.0D, 1.0D);
        this.angleDelta += var181 * 0.1D;
        this.angleDelta *= 0.8D;
        this.currentAngle += this.angleDelta;
      }
      for (int var182 = (int)((this.currentAngle / 6.283185307179586D + 1.0D) * this.framesTextureData.size()) % this.framesTextureData.size(); var182 < 0; var182 = (var182 + this.framesTextureData.size()) % this.framesTextureData.size()) {}
      if (var182 != this.frameCounter)
      {
        this.frameCounter = var182;
        if (Config.isShaders()) {
          ShadersTex.uploadTexSub((int[][])this.framesTextureData.get(this.frameCounter), this.width, this.height, this.originX, this.originY, false, false);
        } else {
          TextureUtil.uploadTextureMipmap((int[][])this.framesTextureData.get(this.frameCounter), this.width, this.height, this.originX, this.originY, false, false);
        }
      }
    }
  }
}
