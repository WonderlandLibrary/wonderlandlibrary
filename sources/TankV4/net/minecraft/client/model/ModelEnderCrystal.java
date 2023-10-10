package net.minecraft.client.model;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;

public class ModelEnderCrystal extends ModelBase {
   private ModelRenderer glass = new ModelRenderer(this, "glass");
   private ModelRenderer base;
   private ModelRenderer cube;

   public void render(Entity var1, float var2, float var3, float var4, float var5, float var6, float var7) {
      GlStateManager.pushMatrix();
      GlStateManager.scale(2.0F, 2.0F, 2.0F);
      GlStateManager.translate(0.0F, -0.5F, 0.0F);
      if (this.base != null) {
         this.base.render(var7);
      }

      GlStateManager.rotate(var3, 0.0F, 1.0F, 0.0F);
      GlStateManager.translate(0.0F, 0.8F + var4, 0.0F);
      GlStateManager.rotate(60.0F, 0.7071F, 0.0F, 0.7071F);
      this.glass.render(var7);
      float var8 = 0.875F;
      GlStateManager.scale(var8, var8, var8);
      GlStateManager.rotate(60.0F, 0.7071F, 0.0F, 0.7071F);
      GlStateManager.rotate(var3, 0.0F, 1.0F, 0.0F);
      this.glass.render(var7);
      GlStateManager.scale(var8, var8, var8);
      GlStateManager.rotate(60.0F, 0.7071F, 0.0F, 0.7071F);
      GlStateManager.rotate(var3, 0.0F, 1.0F, 0.0F);
      this.cube.render(var7);
      GlStateManager.popMatrix();
   }

   public ModelEnderCrystal(float var1, boolean var2) {
      this.glass.setTextureOffset(0, 0).addBox(-4.0F, -4.0F, -4.0F, 8, 8, 8);
      this.cube = new ModelRenderer(this, "cube");
      this.cube.setTextureOffset(32, 0).addBox(-4.0F, -4.0F, -4.0F, 8, 8, 8);
      if (var2) {
         this.base = new ModelRenderer(this, "base");
         this.base.setTextureOffset(0, 16).addBox(-6.0F, 0.0F, -6.0F, 12, 4, 12);
      }

   }
}
