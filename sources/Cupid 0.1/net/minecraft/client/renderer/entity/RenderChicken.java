package net.minecraft.client.renderer.entity;

import net.minecraft.client.model.ModelBase;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.EntityChicken;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;

public class RenderChicken extends RenderLiving<EntityChicken> {
  private static final ResourceLocation chickenTextures = new ResourceLocation("textures/entity/chicken.png");
  
  public RenderChicken(RenderManager renderManagerIn, ModelBase modelBaseIn, float shadowSizeIn) {
    super(renderManagerIn, modelBaseIn, shadowSizeIn);
  }
  
  protected ResourceLocation getEntityTexture(EntityChicken entity) {
    return chickenTextures;
  }
  
  protected float handleRotationFloat(EntityChicken livingBase, float partialTicks) {
    float f = livingBase.field_70888_h + (livingBase.wingRotation - livingBase.field_70888_h) * partialTicks;
    float f1 = livingBase.field_70884_g + (livingBase.destPos - livingBase.field_70884_g) * partialTicks;
    return (MathHelper.sin(f) + 1.0F) * f1;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\net\minecraft\client\renderer\entity\RenderChicken.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */