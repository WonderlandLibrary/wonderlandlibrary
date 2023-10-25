package net.minecraft.client.renderer.entity;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelEnderMite;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityEndermite;
import net.minecraft.util.ResourceLocation;

public class RenderEndermite extends RenderLiving<EntityEndermite> {
  private static final ResourceLocation ENDERMITE_TEXTURES = new ResourceLocation("textures/entity/endermite.png");
  
  public RenderEndermite(RenderManager renderManagerIn) {
    super(renderManagerIn, (ModelBase)new ModelEnderMite(), 0.3F);
  }
  
  protected float getDeathMaxRotation(EntityEndermite entityLivingBaseIn) {
    return 180.0F;
  }
  
  protected ResourceLocation getEntityTexture(EntityEndermite entity) {
    return ENDERMITE_TEXTURES;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\net\minecraft\client\renderer\entity\RenderEndermite.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */