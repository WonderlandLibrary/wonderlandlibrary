package net.minecraft.client.renderer.entity;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelSnowMan;
import net.minecraft.client.renderer.entity.layers.LayerSnowmanHead;
import net.minecraft.entity.Entity;
import net.minecraft.entity.monster.EntitySnowman;
import net.minecraft.util.ResourceLocation;

public class RenderSnowMan extends RenderLiving<EntitySnowman> {
  private static final ResourceLocation snowManTextures = new ResourceLocation("textures/entity/snowman.png");
  
  public RenderSnowMan(RenderManager renderManagerIn) {
    super(renderManagerIn, (ModelBase)new ModelSnowMan(), 0.5F);
    addLayer(new LayerSnowmanHead(this));
  }
  
  protected ResourceLocation getEntityTexture(EntitySnowman entity) {
    return snowManTextures;
  }
  
  public ModelSnowMan getMainModel() {
    return (ModelSnowMan)super.getMainModel();
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\net\minecraft\client\renderer\entity\RenderSnowMan.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */