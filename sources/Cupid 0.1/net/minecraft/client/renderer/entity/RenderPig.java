package net.minecraft.client.renderer.entity;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.entity.layers.LayerSaddle;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.EntityPig;
import net.minecraft.util.ResourceLocation;

public class RenderPig extends RenderLiving<EntityPig> {
  private static final ResourceLocation pigTextures = new ResourceLocation("textures/entity/pig/pig.png");
  
  public RenderPig(RenderManager renderManagerIn, ModelBase modelBaseIn, float shadowSizeIn) {
    super(renderManagerIn, modelBaseIn, shadowSizeIn);
    addLayer(new LayerSaddle(this));
  }
  
  protected ResourceLocation getEntityTexture(EntityPig entity) {
    return pigTextures;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\net\minecraft\client\renderer\entity\RenderPig.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */