package net.minecraft.client.renderer.entity.layers;

import net.minecraft.entity.EntityLivingBase;

public abstract interface LayerRenderer
{
  public abstract void doRenderLayer(EntityLivingBase paramEntityLivingBase, float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4, float paramFloat5, float paramFloat6, float paramFloat7);
  
  public abstract boolean shouldCombineTextures();
}
