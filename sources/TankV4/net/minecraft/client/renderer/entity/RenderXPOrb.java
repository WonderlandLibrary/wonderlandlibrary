package net.minecraft.client.renderer.entity;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import optifine.Config;
import optifine.CustomColors;

public class RenderXPOrb extends Render {
   private static final ResourceLocation experienceOrbTextures = new ResourceLocation("textures/entity/experience_orb.png");
   private static final String __OBFID = "CL_00000993";

   public void doRender(Entity var1, double var2, double var4, double var6, float var8, float var9) {
      this.doRender((EntityXPOrb)var1, var2, var4, var6, var8, var9);
   }

   public RenderXPOrb(RenderManager var1) {
      super(var1);
      this.shadowSize = 0.15F;
      this.shadowOpaque = 0.75F;
   }

   public void doRender(EntityXPOrb var1, double var2, double var4, double var6, float var8, float var9) {
      GlStateManager.pushMatrix();
      GlStateManager.translate((float)var2, (float)var4, (float)var6);
      this.bindEntityTexture(var1);
      int var10 = var1.getTextureByXP();
      float var11 = (float)(var10 % 4 * 16 + 0) / 64.0F;
      float var12 = (float)(var10 % 4 * 16 + 16) / 64.0F;
      float var13 = (float)(var10 / 4 * 16 + 0) / 64.0F;
      float var14 = (float)(var10 / 4 * 16 + 16) / 64.0F;
      float var15 = 1.0F;
      float var16 = 0.5F;
      float var17 = 0.25F;
      int var18 = var1.getBrightnessForRender(var9);
      int var19 = var18 % 65536;
      int var20 = var18 / 65536;
      OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, (float)var19 / 1.0F, (float)var20 / 1.0F);
      GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
      float var21 = 255.0F;
      float var22 = ((float)var1.xpColor + var9) / 2.0F;
      var20 = (int)((MathHelper.sin(var22 + 0.0F) + 1.0F) * 0.5F * 255.0F);
      boolean var23 = true;
      int var24 = (int)((MathHelper.sin(var22 + 4.1887903F) + 1.0F) * 0.1F * 255.0F);
      GlStateManager.rotate(180.0F - RenderManager.playerViewY, 0.0F, 1.0F, 0.0F);
      GlStateManager.rotate(-this.renderManager.playerViewX, 1.0F, 0.0F, 0.0F);
      float var25 = 0.3F;
      GlStateManager.scale(0.3F, 0.3F, 0.3F);
      Tessellator var26 = Tessellator.getInstance();
      WorldRenderer var27 = var26.getWorldRenderer();
      var27.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR_NORMAL);
      int var28 = var20;
      int var29 = 255;
      int var30 = var24;
      if (Config.isCustomColors()) {
         int var31 = CustomColors.getXpOrbColor(var22);
         if (var31 >= 0) {
            var28 = var31 >> 16 & 255;
            var29 = var31 >> 8 & 255;
            var30 = var31 >> 0 & 255;
         }
      }

      var27.pos((double)(0.0F - var16), (double)(0.0F - var17), 0.0D).tex((double)var11, (double)var14).color(var28, var29, var30, 128).normal(0.0F, 1.0F, 0.0F).endVertex();
      var27.pos((double)(var15 - var16), (double)(0.0F - var17), 0.0D).tex((double)var12, (double)var14).color(var28, var29, var30, 128).normal(0.0F, 1.0F, 0.0F).endVertex();
      var27.pos((double)(var15 - var16), (double)(1.0F - var17), 0.0D).tex((double)var12, (double)var13).color(var28, var29, var30, 128).normal(0.0F, 1.0F, 0.0F).endVertex();
      var27.pos((double)(0.0F - var16), (double)(1.0F - var17), 0.0D).tex((double)var11, (double)var13).color(var28, var29, var30, 128).normal(0.0F, 1.0F, 0.0F).endVertex();
      var26.draw();
      GlStateManager.disableBlend();
      GlStateManager.disableRescaleNormal();
      GlStateManager.popMatrix();
      super.doRender(var1, var2, var4, var6, var8, var9);
   }

   protected ResourceLocation getEntityTexture(Entity var1) {
      return this.getEntityTexture((EntityXPOrb)var1);
   }

   protected ResourceLocation getEntityTexture(EntityXPOrb var1) {
      return experienceOrbTextures;
   }
}
