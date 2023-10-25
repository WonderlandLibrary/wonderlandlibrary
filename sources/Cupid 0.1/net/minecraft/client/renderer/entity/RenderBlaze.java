package net.minecraft.client.renderer.entity;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelBlaze;
import net.minecraft.entity.Entity;
import net.minecraft.entity.monster.EntityBlaze;
import net.minecraft.util.ResourceLocation;

public class RenderBlaze extends RenderLiving<EntityBlaze> {
  private static final ResourceLocation blazeTextures = new ResourceLocation("textures/entity/blaze.png");
  
  public RenderBlaze(RenderManager renderManagerIn) {
    super(renderManagerIn, (ModelBase)new ModelBlaze(), 0.5F);
  }
  
  protected ResourceLocation getEntityTexture(EntityBlaze entity) {
    return blazeTextures;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\net\minecraft\client\renderer\entity\RenderBlaze.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */