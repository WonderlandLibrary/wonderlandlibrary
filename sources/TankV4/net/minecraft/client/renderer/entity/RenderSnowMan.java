package net.minecraft.client.renderer.entity;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelSnowMan;
import net.minecraft.client.renderer.entity.layers.LayerSnowmanHead;
import net.minecraft.entity.Entity;
import net.minecraft.entity.monster.EntitySnowman;
import net.minecraft.util.ResourceLocation;

public class RenderSnowMan extends RenderLiving {
   private static final ResourceLocation snowManTextures = new ResourceLocation("textures/entity/snowman.png");

   public ModelSnowMan getMainModel() {
      return (ModelSnowMan)super.getMainModel();
   }

   public RenderSnowMan(RenderManager var1) {
      super(var1, new ModelSnowMan(), 0.5F);
      this.addLayer(new LayerSnowmanHead(this));
   }

   protected ResourceLocation getEntityTexture(EntitySnowman var1) {
      return snowManTextures;
   }

   public ModelBase getMainModel() {
      return this.getMainModel();
   }

   protected ResourceLocation getEntityTexture(Entity var1) {
      return this.getEntityTexture((EntitySnowman)var1);
   }
}
