package net.minecraft.client.renderer.entity;

import net.minecraft.client.model.ModelBase;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.EntityChicken;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;

public class RenderChicken extends RenderLiving {
   private static final ResourceLocation chickenTextures = new ResourceLocation("textures/entity/chicken.png");

   protected float handleRotationFloat(EntityChicken var1, float var2) {
      float var3 = var1.field_70888_h + (var1.wingRotation - var1.field_70888_h) * var2;
      float var4 = var1.field_70884_g + (var1.destPos - var1.field_70884_g) * var2;
      return (MathHelper.sin(var3) + 1.0F) * var4;
   }

   protected ResourceLocation getEntityTexture(EntityChicken var1) {
      return chickenTextures;
   }

   public RenderChicken(RenderManager var1, ModelBase var2, float var3) {
      super(var1, var2, var3);
   }

   protected float handleRotationFloat(EntityLivingBase var1, float var2) {
      return this.handleRotationFloat((EntityChicken)var1, var2);
   }

   protected ResourceLocation getEntityTexture(Entity var1) {
      return this.getEntityTexture((EntityChicken)var1);
   }
}
