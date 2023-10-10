package net.minecraft.client.renderer.entity;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.entity.layers.LayerSheepWool;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.util.ResourceLocation;

public class RenderSheep extends RenderLiving {
   private static final ResourceLocation shearedSheepTextures = new ResourceLocation("textures/entity/sheep/sheep.png");

   public RenderSheep(RenderManager var1, ModelBase var2, float var3) {
      super(var1, var2, var3);
      this.addLayer(new LayerSheepWool(this));
   }

   protected ResourceLocation getEntityTexture(Entity var1) {
      return this.getEntityTexture((EntitySheep)var1);
   }

   protected ResourceLocation getEntityTexture(EntitySheep var1) {
      return shearedSheepTextures;
   }
}
