package net.minecraft.client.renderer.entity;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class RenderArrow extends Render {
   private static final ResourceLocation arrowTextures = new ResourceLocation("textures/entity/arrow.png");

   protected ResourceLocation getEntityTexture(Entity var1) {
      return this.getEntityTexture((EntityArrow)var1);
   }

   public void doRender(Entity var1, double var2, double var4, double var6, float var8, float var9) {
      this.doRender((EntityArrow)var1, var2, var4, var6, var8, var9);
   }

   protected ResourceLocation getEntityTexture(EntityArrow var1) {
      return arrowTextures;
   }

   public RenderArrow(RenderManager var1) {
      super(var1);
   }

   public void doRender(EntityArrow var1, double var2, double var4, double var6, float var8, float var9) {
      this.bindEntityTexture(var1);
      GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
      GlStateManager.pushMatrix();
      GlStateManager.translate((float)var2, (float)var4, (float)var6);
      GlStateManager.rotate(var1.prevRotationYaw + (var1.rotationYaw - var1.prevRotationYaw) * var9 - 90.0F, 0.0F, 1.0F, 0.0F);
      GlStateManager.rotate(var1.prevRotationPitch + (var1.rotationPitch - var1.prevRotationPitch) * var9, 0.0F, 0.0F, 1.0F);
      Tessellator var10 = Tessellator.getInstance();
      WorldRenderer var11 = var10.getWorldRenderer();
      byte var12 = 0;
      float var13 = 0.0F;
      float var14 = 0.5F;
      float var15 = (float)(0 + var12 * 10) / 32.0F;
      float var16 = (float)(5 + var12 * 10) / 32.0F;
      float var17 = 0.0F;
      float var18 = 0.15625F;
      float var19 = (float)(5 + var12 * 10) / 32.0F;
      float var20 = (float)(10 + var12 * 10) / 32.0F;
      float var21 = 0.05625F;
      GlStateManager.enableRescaleNormal();
      float var22 = (float)var1.arrowShake - var9;
      if (var22 > 0.0F) {
         float var23 = -MathHelper.sin(var22 * 3.0F) * var22;
         GlStateManager.rotate(var23, 0.0F, 0.0F, 1.0F);
      }

      GlStateManager.rotate(45.0F, 1.0F, 0.0F, 0.0F);
      GlStateManager.scale(var21, var21, var21);
      GlStateManager.translate(-4.0F, 0.0F, 0.0F);
      GL11.glNormal3f(var21, 0.0F, 0.0F);
      var11.begin(7, DefaultVertexFormats.POSITION_TEX);
      var11.pos(-7.0D, -2.0D, -2.0D).tex((double)var17, (double)var19).endVertex();
      var11.pos(-7.0D, -2.0D, 2.0D).tex((double)var18, (double)var19).endVertex();
      var11.pos(-7.0D, 2.0D, 2.0D).tex((double)var18, (double)var20).endVertex();
      var11.pos(-7.0D, 2.0D, -2.0D).tex((double)var17, (double)var20).endVertex();
      var10.draw();
      GL11.glNormal3f(-var21, 0.0F, 0.0F);
      var11.begin(7, DefaultVertexFormats.POSITION_TEX);
      var11.pos(-7.0D, 2.0D, -2.0D).tex((double)var17, (double)var19).endVertex();
      var11.pos(-7.0D, 2.0D, 2.0D).tex((double)var18, (double)var19).endVertex();
      var11.pos(-7.0D, -2.0D, 2.0D).tex((double)var18, (double)var20).endVertex();
      var11.pos(-7.0D, -2.0D, -2.0D).tex((double)var17, (double)var20).endVertex();
      var10.draw();

      for(int var24 = 0; var24 < 4; ++var24) {
         GlStateManager.rotate(90.0F, 1.0F, 0.0F, 0.0F);
         GL11.glNormal3f(0.0F, 0.0F, var21);
         var11.begin(7, DefaultVertexFormats.POSITION_TEX);
         var11.pos(-8.0D, -2.0D, 0.0D).tex((double)var13, (double)var15).endVertex();
         var11.pos(8.0D, -2.0D, 0.0D).tex((double)var14, (double)var15).endVertex();
         var11.pos(8.0D, 2.0D, 0.0D).tex((double)var14, (double)var16).endVertex();
         var11.pos(-8.0D, 2.0D, 0.0D).tex((double)var13, (double)var16).endVertex();
         var10.draw();
      }

      GlStateManager.disableRescaleNormal();
      GlStateManager.popMatrix();
      super.doRender(var1, var2, var4, var6, var8, var9);
   }
}
