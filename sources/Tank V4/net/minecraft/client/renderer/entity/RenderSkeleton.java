package net.minecraft.client.renderer.entity;

import net.minecraft.client.model.ModelSkeleton;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.layers.LayerBipedArmor;
import net.minecraft.client.renderer.entity.layers.LayerHeldItem;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.util.ResourceLocation;

public class RenderSkeleton extends RenderBiped {
   private static final ResourceLocation skeletonTextures = new ResourceLocation("textures/entity/skeleton/skeleton.png");
   private static final ResourceLocation witherSkeletonTextures = new ResourceLocation("textures/entity/skeleton/wither_skeleton.png");

   protected ResourceLocation getEntityTexture(EntityLiving var1) {
      return this.getEntityTexture((EntitySkeleton)var1);
   }

   protected void preRenderCallback(EntitySkeleton var1, float var2) {
      if (var1.getSkeletonType() == 1) {
         GlStateManager.scale(1.2F, 1.2F, 1.2F);
      }

   }

   public void transformHeldFull3DItemLayer() {
      GlStateManager.translate(0.09375F, 0.1875F, 0.0F);
   }

   public RenderSkeleton(RenderManager var1) {
      super(var1, new ModelSkeleton(), 0.5F);
      this.addLayer(new LayerHeldItem(this));
      this.addLayer(new LayerBipedArmor(this, this) {
         final RenderSkeleton this$0;

         protected void initArmor() {
            this.field_177189_c = new ModelSkeleton(0.5F, true);
            this.field_177186_d = new ModelSkeleton(1.0F, true);
         }

         {
            this.this$0 = var1;
         }
      });
   }

   protected ResourceLocation getEntityTexture(EntitySkeleton var1) {
      return var1.getSkeletonType() == 1 ? witherSkeletonTextures : skeletonTextures;
   }

   protected void preRenderCallback(EntityLivingBase var1, float var2) {
      this.preRenderCallback((EntitySkeleton)var1, var2);
   }
}
