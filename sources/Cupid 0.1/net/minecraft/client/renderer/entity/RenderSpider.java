package net.minecraft.client.renderer.entity;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelSpider;
import net.minecraft.client.renderer.entity.layers.LayerSpiderEyes;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntitySpider;
import net.minecraft.util.ResourceLocation;

public class RenderSpider<T extends EntitySpider> extends RenderLiving<T> {
  private static final ResourceLocation spiderTextures = new ResourceLocation("textures/entity/spider/spider.png");
  
  public RenderSpider(RenderManager renderManagerIn) {
    super(renderManagerIn, (ModelBase)new ModelSpider(), 1.0F);
    addLayer(new LayerSpiderEyes(this));
  }
  
  protected float getDeathMaxRotation(T entityLivingBaseIn) {
    return 180.0F;
  }
  
  protected ResourceLocation getEntityTexture(T entity) {
    return spiderTextures;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\net\minecraft\client\renderer\entity\RenderSpider.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */