package net.minecraft.client.renderer.entity;

import net.minecraft.client.model.ModelBlaze;
import net.minecraft.entity.Entity;
import net.minecraft.entity.monster.EntityBlaze;
import net.minecraft.util.ResourceLocation;

public class RenderBlaze extends RenderLiving {
   private static final ResourceLocation blazeTextures = new ResourceLocation("textures/entity/blaze.png");

   public RenderBlaze(RenderManager var1) {
      super(var1, new ModelBlaze(), 0.5F);
   }

   protected ResourceLocation getEntityTexture(EntityBlaze var1) {
      return blazeTextures;
   }

   protected ResourceLocation getEntityTexture(Entity var1) {
      return this.getEntityTexture((EntityBlaze)var1);
   }
}
