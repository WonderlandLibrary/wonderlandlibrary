package net.minecraft.client.renderer.tileentity;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelEnderCrystal;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;

public class RenderEnderCrystal extends Render {
   private static final ResourceLocation enderCrystalTextures = new ResourceLocation("textures/entity/endercrystal/endercrystal.png");
   private ModelBase modelEnderCrystal = new ModelEnderCrystal(0.0F, true);

   public RenderEnderCrystal(RenderManager var1) {
      super(var1);
      this.shadowSize = 0.5F;
   }

   protected ResourceLocation getEntityTexture(Entity var1) {
      return this.getEntityTexture((EntityEnderCrystal)var1);
   }

   protected ResourceLocation getEntityTexture(EntityEnderCrystal var1) {
      return enderCrystalTextures;
   }

   public void doRender(EntityEnderCrystal var1, double var2, double var4, double var6, float var8, float var9) {
      float var10 = (float)var1.innerRotation + var9;
      GlStateManager.pushMatrix();
      GlStateManager.translate((float)var2, (float)var4, (float)var6);
      this.bindTexture(enderCrystalTextures);
      float var11 = MathHelper.sin(var10 * 0.2F) / 2.0F + 0.5F;
      var11 += var11 * var11;
      this.modelEnderCrystal.render(var1, 0.0F, var10 * 3.0F, var11 * 0.2F, 0.0F, 0.0F, 0.0625F);
      GlStateManager.popMatrix();
      super.doRender(var1, var2, var4, var6, var8, var9);
   }

   public void doRender(Entity var1, double var2, double var4, double var6, float var8, float var9) {
      this.doRender((EntityEnderCrystal)var1, var2, var4, var6, var8, var9);
   }
}
