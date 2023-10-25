package net.minecraft.client.renderer.entity;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelCreeper;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.layers.LayerCreeperCharge;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;

public class RenderCreeper extends RenderLiving<EntityCreeper> {
  private static final ResourceLocation creeperTextures = new ResourceLocation("textures/entity/creeper/creeper.png");
  
  public RenderCreeper(RenderManager renderManagerIn) {
    super(renderManagerIn, (ModelBase)new ModelCreeper(), 0.5F);
    addLayer(new LayerCreeperCharge(this));
  }
  
  protected void preRenderCallback(EntityCreeper entitylivingbaseIn, float partialTickTime) {
    float f = entitylivingbaseIn.getCreeperFlashIntensity(partialTickTime);
    float f1 = 1.0F + MathHelper.sin(f * 100.0F) * f * 0.01F;
    f = MathHelper.clamp_float(f, 0.0F, 1.0F);
    f *= f;
    f *= f;
    float f2 = (1.0F + f * 0.4F) * f1;
    float f3 = (1.0F + f * 0.1F) / f1;
    GlStateManager.scale(f2, f3, f2);
  }
  
  protected int getColorMultiplier(EntityCreeper entitylivingbaseIn, float lightBrightness, float partialTickTime) {
    float f = entitylivingbaseIn.getCreeperFlashIntensity(partialTickTime);
    if ((int)(f * 10.0F) % 2 == 0)
      return 0; 
    int i = (int)(f * 0.2F * 255.0F);
    i = MathHelper.clamp_int(i, 0, 255);
    return i << 24 | 0xFFFFFF;
  }
  
  protected ResourceLocation getEntityTexture(EntityCreeper entity) {
    return creeperTextures;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\net\minecraft\client\renderer\entity\RenderCreeper.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */