package net.minecraft.client.renderer.texture;

import net.minecraft.client.Minecraft;
import net.minecraft.util.MathHelper;
import optifine.Config;
import shadersmod.client.ShadersTex;

public class TextureClock extends TextureAtlasSprite {
   private double field_94239_h;
   private static final String __OBFID = "CL_00001070";
   private double field_94240_i;

   public TextureClock(String var1) {
      super(var1);
   }

   public void updateAnimation() {
      if (!this.framesTextureData.isEmpty()) {
         Minecraft var1 = Minecraft.getMinecraft();
         double var2 = 0.0D;
         if (Minecraft.theWorld != null && Minecraft.thePlayer != null) {
            var2 = (double)Minecraft.theWorld.getCelestialAngle(1.0F);
            if (!Minecraft.theWorld.provider.isSurfaceWorld()) {
               var2 = Math.random();
            }
         }

         double var4;
         for(var4 = var2 - this.field_94239_h; var4 < -0.5D; ++var4) {
         }

         while(var4 >= 0.5D) {
            --var4;
         }

         var4 = MathHelper.clamp_double(var4, -1.0D, 1.0D);
         this.field_94240_i += var4 * 0.1D;
         this.field_94240_i *= 0.8D;
         this.field_94239_h += this.field_94240_i;

         int var6;
         for(var6 = (int)((this.field_94239_h + 1.0D) * (double)this.framesTextureData.size()) % this.framesTextureData.size(); var6 < 0; var6 = (var6 + this.framesTextureData.size()) % this.framesTextureData.size()) {
         }

         if (var6 != this.frameCounter) {
            this.frameCounter = var6;
            if (Config.isShaders()) {
               ShadersTex.uploadTexSub((int[][])this.framesTextureData.get(this.frameCounter), this.width, this.height, this.originX, this.originY, false, false);
            } else {
               TextureUtil.uploadTextureMipmap((int[][])this.framesTextureData.get(this.frameCounter), this.width, this.height, this.originX, this.originY, false, false);
            }
         }
      }

   }
}
