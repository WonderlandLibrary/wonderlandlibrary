package net.minecraft.client.renderer.entity;

import com.google.common.collect.Maps;
import java.util.Map;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelHorse;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.LayeredTexture;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.EntityHorse;
import net.minecraft.util.ResourceLocation;

public class RenderHorse extends RenderLiving {
   private static final ResourceLocation muleTextures = new ResourceLocation("textures/entity/horse/mule.png");
   private static final ResourceLocation whiteHorseTextures = new ResourceLocation("textures/entity/horse/horse_white.png");
   private static final ResourceLocation donkeyTextures = new ResourceLocation("textures/entity/horse/donkey.png");
   private static final Map field_110852_a = Maps.newHashMap();
   private static final ResourceLocation zombieHorseTextures = new ResourceLocation("textures/entity/horse/horse_zombie.png");
   private static final ResourceLocation skeletonHorseTextures = new ResourceLocation("textures/entity/horse/horse_skeleton.png");

   public RenderHorse(RenderManager var1, ModelHorse var2, float var3) {
      super(var1, var2, var3);
   }

   protected void preRenderCallback(EntityHorse var1, float var2) {
      float var3 = 1.0F;
      int var4 = var1.getHorseType();
      if (var4 == 1) {
         var3 *= 0.87F;
      } else if (var4 == 2) {
         var3 *= 0.92F;
      }

      GlStateManager.scale(var3, var3, var3);
      super.preRenderCallback(var1, var2);
   }

   private ResourceLocation func_110848_b(EntityHorse var1) {
      String var2 = var1.getHorseTexture();
      if (!var1.func_175507_cI()) {
         return null;
      } else {
         ResourceLocation var3 = (ResourceLocation)field_110852_a.get(var2);
         if (var3 == null) {
            var3 = new ResourceLocation(var2);
            Minecraft.getMinecraft().getTextureManager().loadTexture(var3, new LayeredTexture(var1.getVariantTexturePaths()));
            field_110852_a.put(var2, var3);
         }

         return var3;
      }
   }

   protected void preRenderCallback(EntityLivingBase var1, float var2) {
      this.preRenderCallback((EntityHorse)var1, var2);
   }

   protected ResourceLocation getEntityTexture(Entity var1) {
      return this.getEntityTexture((EntityHorse)var1);
   }

   protected ResourceLocation getEntityTexture(EntityHorse var1) {
      if (!var1.func_110239_cn()) {
         switch(var1.getHorseType()) {
         case 0:
         default:
            return whiteHorseTextures;
         case 1:
            return donkeyTextures;
         case 2:
            return muleTextures;
         case 3:
            return zombieHorseTextures;
         case 4:
            return skeletonHorseTextures;
         }
      } else {
         return this.func_110848_b(var1);
      }
   }
}
