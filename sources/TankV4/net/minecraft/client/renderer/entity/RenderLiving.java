package net.minecraft.client.renderer.entity;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.culling.ICamera;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityHanging;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import optifine.Config;
import shadersmod.client.Shaders;

public abstract class RenderLiving extends RendererLivingEntity {
   private static final String __OBFID = "CL_00001015";

   public RenderLiving(RenderManager var1, ModelBase var2, float var3) {
      super(var1, var2, var3);
   }

   private double interpolateValue(double var1, double var3, double var5) {
      return var1 + (var3 - var1) * var5;
   }

   public boolean shouldRender(Entity var1, ICamera var2, double var3, double var5, double var7) {
      return this.shouldRender((EntityLiving)var1, var2, var3, var5, var7);
   }

   public void func_177105_a(EntityLiving var1, float var2) {
      int var3 = var1.getBrightnessForRender(var2);
      int var4 = var3 % 65536;
      int var5 = var3 / 65536;
      OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, (float)var4 / 1.0F, (float)var5 / 1.0F);
   }

   public boolean shouldRender(EntityLiving var1, ICamera var2, double var3, double var5, double var7) {
      if (super.shouldRender(var1, var2, var3, var5, var7)) {
         return true;
      } else if (var1.getLeashed() && var1.getLeashedToEntity() != null) {
         Entity var9 = var1.getLeashedToEntity();
         return var2.isBoundingBoxInFrustum(var9.getEntityBoundingBox());
      } else {
         return false;
      }
   }

   public void doRender(EntityLiving var1, double var2, double var4, double var6, float var8, float var9) {
      super.doRender((EntityLivingBase)var1, var2, var4, var6, var8, var9);
      this.renderLeash(var1, var2, var4, var6, var8, var9);
   }

   protected boolean canRenderName(EntityLiving var1) {
      return super.canRenderName(var1) && (var1.getAlwaysRenderNameTagForRender() || var1.hasCustomName() && var1 == this.renderManager.pointedEntity);
   }

   public void doRender(EntityLivingBase var1, double var2, double var4, double var6, float var8, float var9) {
      this.doRender((EntityLiving)var1, var2, var4, var6, var8, var9);
   }

   protected boolean canRenderName(EntityLivingBase var1) {
      return this.canRenderName((EntityLiving)var1);
   }

   protected void renderLeash(EntityLiving var1, double var2, double var4, double var6, float var8, float var9) {
      if (!Config.isShaders() || !Shaders.isShadowPass) {
         Entity var10 = var1.getLeashedToEntity();
         if (var10 != null) {
            var4 -= (1.6D - (double)var1.height) * 0.5D;
            Tessellator var11 = Tessellator.getInstance();
            WorldRenderer var12 = var11.getWorldRenderer();
            double var13 = this.interpolateValue((double)var10.prevRotationYaw, (double)var10.rotationYaw, (double)(var9 * 0.5F)) * 0.01745329238474369D;
            double var15 = this.interpolateValue((double)var10.prevRotationPitch, (double)var10.rotationPitch, (double)(var9 * 0.5F)) * 0.01745329238474369D;
            double var17 = Math.cos(var13);
            double var19 = Math.sin(var13);
            double var21 = Math.sin(var15);
            if (var10 instanceof EntityHanging) {
               var17 = 0.0D;
               var19 = 0.0D;
               var21 = -1.0D;
            }

            double var23 = Math.cos(var15);
            double var25 = this.interpolateValue(var10.prevPosX, var10.posX, (double)var9) - var17 * 0.7D - var19 * 0.5D * var23;
            double var27 = this.interpolateValue(var10.prevPosY + (double)var10.getEyeHeight() * 0.7D, var10.posY + (double)var10.getEyeHeight() * 0.7D, (double)var9) - var21 * 0.5D - 0.25D;
            double var29 = this.interpolateValue(var10.prevPosZ, var10.posZ, (double)var9) - var19 * 0.7D + var17 * 0.5D * var23;
            double var31 = this.interpolateValue((double)var1.prevRenderYawOffset, (double)var1.renderYawOffset, (double)var9) * 0.01745329238474369D + 1.5707963267948966D;
            var17 = Math.cos(var31) * (double)var1.width * 0.4D;
            var19 = Math.sin(var31) * (double)var1.width * 0.4D;
            double var33 = this.interpolateValue(var1.prevPosX, var1.posX, (double)var9) + var17;
            double var35 = this.interpolateValue(var1.prevPosY, var1.posY, (double)var9);
            double var37 = this.interpolateValue(var1.prevPosZ, var1.posZ, (double)var9) + var19;
            var2 += var17;
            var6 += var19;
            double var39 = (double)((float)(var25 - var33));
            double var41 = (double)((float)(var27 - var35));
            double var43 = (double)((float)(var29 - var37));
            GlStateManager.disableTexture2D();
            GlStateManager.disableLighting();
            GlStateManager.disableCull();
            if (Config.isShaders()) {
               Shaders.beginLeash();
            }

            boolean var45 = true;
            double var46 = 0.025D;
            var12.begin(5, DefaultVertexFormats.POSITION_COLOR);

            int var48;
            float var49;
            float var50;
            float var51;
            float var52;
            for(var48 = 0; var48 <= 24; ++var48) {
               var49 = 0.5F;
               var50 = 0.4F;
               var51 = 0.3F;
               if (var48 % 2 == 0) {
                  var49 *= 0.7F;
                  var50 *= 0.7F;
                  var51 *= 0.7F;
               }

               var52 = (float)var48 / 24.0F;
               var12.pos(var2 + var39 * (double)var52 + 0.0D, var4 + var41 * (double)(var52 * var52 + var52) * 0.5D + (double)((24.0F - (float)var48) / 18.0F + 0.125F), var6 + var43 * (double)var52).color(var49, var50, var51, 1.0F).endVertex();
               var12.pos(var2 + var39 * (double)var52 + 0.025D, var4 + var41 * (double)(var52 * var52 + var52) * 0.5D + (double)((24.0F - (float)var48) / 18.0F + 0.125F) + 0.025D, var6 + var43 * (double)var52).color(var49, var50, var51, 1.0F).endVertex();
            }

            var11.draw();
            var12.begin(5, DefaultVertexFormats.POSITION_COLOR);

            for(var48 = 0; var48 <= 24; ++var48) {
               var49 = 0.5F;
               var50 = 0.4F;
               var51 = 0.3F;
               if (var48 % 2 == 0) {
                  var49 *= 0.7F;
                  var50 *= 0.7F;
                  var51 *= 0.7F;
               }

               var52 = (float)var48 / 24.0F;
               var12.pos(var2 + var39 * (double)var52 + 0.0D, var4 + var41 * (double)(var52 * var52 + var52) * 0.5D + (double)((24.0F - (float)var48) / 18.0F + 0.125F) + 0.025D, var6 + var43 * (double)var52).color(var49, var50, var51, 1.0F).endVertex();
               var12.pos(var2 + var39 * (double)var52 + 0.025D, var4 + var41 * (double)(var52 * var52 + var52) * 0.5D + (double)((24.0F - (float)var48) / 18.0F + 0.125F), var6 + var43 * (double)var52 + 0.025D).color(var49, var50, var51, 1.0F).endVertex();
            }

            var11.draw();
            if (Config.isShaders()) {
               Shaders.endLeash();
            }

            GlStateManager.enableLighting();
            GlStateManager.enableTexture2D();
            GlStateManager.enableCull();
         }
      }

   }
}
