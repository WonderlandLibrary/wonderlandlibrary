package net.minecraft.client.renderer.entity;

import net.minecraft.client.model.ModelSilverfish;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntitySilverfish;
import net.minecraft.util.ResourceLocation;

public class RenderSilverfish extends RenderLiving {
   private static final ResourceLocation silverfishTextures = new ResourceLocation("textures/entity/silverfish.png");

   protected float getDeathMaxRotation(EntityLivingBase var1) {
      return this.getDeathMaxRotation((EntitySilverfish)var1);
   }

   protected ResourceLocation getEntityTexture(EntitySilverfish var1) {
      return silverfishTextures;
   }

   public RenderSilverfish(RenderManager var1) {
      super(var1, new ModelSilverfish(), 0.3F);
   }

   protected float getDeathMaxRotation(EntitySilverfish var1) {
      return 180.0F;
   }

   protected ResourceLocation getEntityTexture(Entity var1) {
      return this.getEntityTexture((EntitySilverfish)var1);
   }
}
