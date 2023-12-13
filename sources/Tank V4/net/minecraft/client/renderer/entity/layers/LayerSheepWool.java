package net.minecraft.client.renderer.entity.layers;

import net.minecraft.client.model.ModelSheep1;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderSheep;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.util.ResourceLocation;
import optifine.Config;
import optifine.CustomColors;

public class LayerSheepWool implements LayerRenderer {
   private final RenderSheep sheepRenderer;
   private static final ResourceLocation TEXTURE = new ResourceLocation("textures/entity/sheep/sheep_fur.png");
   private final ModelSheep1 sheepModel = new ModelSheep1();
   private static final String __OBFID = "CL_00002413";

   public boolean shouldCombineTextures() {
      return true;
   }

   public LayerSheepWool(RenderSheep var1) {
      this.sheepRenderer = var1;
   }

   public void doRenderLayer(EntitySheep var1, float var2, float var3, float var4, float var5, float var6, float var7, float var8) {
      if (!var1.getSheared() && !var1.isInvisible()) {
         this.sheepRenderer.bindTexture(TEXTURE);
         if (var1.hasCustomName() && "jeb_".equals(var1.getCustomNameTag())) {
            boolean var17 = true;
            int var10 = var1.ticksExisted / 25 + var1.getEntityId();
            int var11 = EnumDyeColor.values().length;
            int var12 = var10 % var11;
            int var13 = (var10 + 1) % var11;
            float var14 = ((float)(var1.ticksExisted % 25) + var4) / 25.0F;
            float[] var15 = EntitySheep.func_175513_a(EnumDyeColor.byMetadata(var12));
            float[] var16 = EntitySheep.func_175513_a(EnumDyeColor.byMetadata(var13));
            if (Config.isCustomColors()) {
               var15 = CustomColors.getSheepColors(EnumDyeColor.byMetadata(var12), var15);
               var16 = CustomColors.getSheepColors(EnumDyeColor.byMetadata(var13), var16);
            }

            GlStateManager.color(var15[0] * (1.0F - var14) + var16[0] * var14, var15[1] * (1.0F - var14) + var16[1] * var14, var15[2] * (1.0F - var14) + var16[2] * var14);
         } else {
            float[] var9 = EntitySheep.func_175513_a(var1.getFleeceColor());
            if (Config.isCustomColors()) {
               var9 = CustomColors.getSheepColors(var1.getFleeceColor(), var9);
            }

            GlStateManager.color(var9[0], var9[1], var9[2]);
         }

         this.sheepModel.setModelAttributes(this.sheepRenderer.getMainModel());
         this.sheepModel.setLivingAnimations(var1, var2, var3, var4);
         this.sheepModel.render(var1, var2, var3, var5, var6, var7, var8);
      }

   }

   public void doRenderLayer(EntityLivingBase var1, float var2, float var3, float var4, float var5, float var6, float var7, float var8) {
      this.doRenderLayer((EntitySheep)var1, var2, var3, var4, var5, var6, var7, var8);
   }
}
