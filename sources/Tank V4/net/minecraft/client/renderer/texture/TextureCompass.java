package net.minecraft.client.renderer.texture;

import net.minecraft.client.Minecraft;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import optifine.Config;
import shadersmod.client.ShadersTex;

public class TextureCompass extends TextureAtlasSprite {
   public double angleDelta;
   public static String field_176608_l;
   public double currentAngle;
   private static final String __OBFID = "CL_00001071";

   public TextureCompass(String var1) {
      super(var1);
      field_176608_l = var1;
   }

   public void updateCompass(World var1, double var2, double var4, double var6, boolean var8, boolean var9) {
      if (!this.framesTextureData.isEmpty()) {
         double var10 = 0.0D;
         if (var1 != null && !var8) {
            BlockPos var12 = var1.getSpawnPoint();
            double var13 = (double)var12.getX() - var2;
            double var15 = (double)var12.getZ() - var4;
            var6 %= 360.0D;
            var10 = -((var6 - 90.0D) * 3.141592653589793D / 180.0D - Math.atan2(var15, var13));
            if (!var1.provider.isSurfaceWorld()) {
               var10 = Math.random() * 3.141592653589793D * 2.0D;
            }
         }

         if (var9) {
            this.currentAngle = var10;
         } else {
            double var17;
            for(var17 = var10 - this.currentAngle; var17 < -3.141592653589793D; var17 += 6.283185307179586D) {
            }

            while(var17 >= 3.141592653589793D) {
               var17 -= 6.283185307179586D;
            }

            var17 = MathHelper.clamp_double(var17, -1.0D, 1.0D);
            this.angleDelta += var17 * 0.1D;
            this.angleDelta *= 0.8D;
            this.currentAngle += this.angleDelta;
         }

         int var18;
         for(var18 = (int)((this.currentAngle / 6.283185307179586D + 1.0D) * (double)this.framesTextureData.size()) % this.framesTextureData.size(); var18 < 0; var18 = (var18 + this.framesTextureData.size()) % this.framesTextureData.size()) {
         }

         if (var18 != this.frameCounter) {
            this.frameCounter = var18;
            if (Config.isShaders()) {
               ShadersTex.uploadTexSub((int[][])this.framesTextureData.get(this.frameCounter), this.width, this.height, this.originX, this.originY, false, false);
            } else {
               TextureUtil.uploadTextureMipmap((int[][])this.framesTextureData.get(this.frameCounter), this.width, this.height, this.originX, this.originY, false, false);
            }
         }
      }

   }

   public void updateAnimation() {
      Minecraft var1 = Minecraft.getMinecraft();
      if (Minecraft.theWorld != null && Minecraft.thePlayer != null) {
         this.updateCompass(Minecraft.theWorld, Minecraft.thePlayer.posX, Minecraft.thePlayer.posZ, (double)Minecraft.thePlayer.rotationYaw, false, false);
      } else {
         this.updateCompass((World)null, 0.0D, 0.0D, 0.0D, true, false);
      }

   }
}
