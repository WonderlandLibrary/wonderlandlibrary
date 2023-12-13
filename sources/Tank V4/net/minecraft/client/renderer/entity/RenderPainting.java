package net.minecraft.client.renderer.entity;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityPainting;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;

public class RenderPainting extends Render {
   private static final ResourceLocation KRISTOFFER_PAINTING_TEXTURE = new ResourceLocation("textures/painting/paintings_kristoffer_zetterstrand.png");

   protected ResourceLocation getEntityTexture(Entity var1) {
      return this.getEntityTexture((EntityPainting)var1);
   }

   private void setLightmap(EntityPainting var1, float var2, float var3) {
      int var4 = MathHelper.floor_double(var1.posX);
      int var5 = MathHelper.floor_double(var1.posY + (double)(var3 / 16.0F));
      int var6 = MathHelper.floor_double(var1.posZ);
      EnumFacing var7 = var1.facingDirection;
      if (var7 == EnumFacing.NORTH) {
         var4 = MathHelper.floor_double(var1.posX + (double)(var2 / 16.0F));
      }

      if (var7 == EnumFacing.WEST) {
         var6 = MathHelper.floor_double(var1.posZ - (double)(var2 / 16.0F));
      }

      if (var7 == EnumFacing.SOUTH) {
         var4 = MathHelper.floor_double(var1.posX - (double)(var2 / 16.0F));
      }

      if (var7 == EnumFacing.EAST) {
         var6 = MathHelper.floor_double(var1.posZ + (double)(var2 / 16.0F));
      }

      int var8 = this.renderManager.worldObj.getCombinedLight(new BlockPos(var4, var5, var6), 0);
      int var9 = var8 % 65536;
      int var10 = var8 / 65536;
      OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, (float)var9, (float)var10);
      GlStateManager.color(1.0F, 1.0F, 1.0F);
   }

   protected ResourceLocation getEntityTexture(EntityPainting var1) {
      return KRISTOFFER_PAINTING_TEXTURE;
   }

   public void doRender(Entity var1, double var2, double var4, double var6, float var8, float var9) {
      this.doRender((EntityPainting)var1, var2, var4, var6, var8, var9);
   }

   private void renderPainting(EntityPainting var1, int var2, int var3, int var4, int var5) {
      float var6 = (float)(-var2) / 2.0F;
      float var7 = (float)(-var3) / 2.0F;
      float var8 = 0.5F;
      float var9 = 0.75F;
      float var10 = 0.8125F;
      float var11 = 0.0F;
      float var12 = 0.0625F;
      float var13 = 0.75F;
      float var14 = 0.8125F;
      float var15 = 0.001953125F;
      float var16 = 0.001953125F;
      float var17 = 0.7519531F;
      float var18 = 0.7519531F;
      float var19 = 0.0F;
      float var20 = 0.0625F;

      for(int var21 = 0; var21 < var2 / 16; ++var21) {
         for(int var22 = 0; var22 < var3 / 16; ++var22) {
            float var23 = var6 + (float)((var21 + 1) * 16);
            float var24 = var6 + (float)(var21 * 16);
            float var25 = var7 + (float)((var22 + 1) * 16);
            float var26 = var7 + (float)(var22 * 16);
            this.setLightmap(var1, (var23 + var24) / 2.0F, (var25 + var26) / 2.0F);
            float var27 = (float)(var4 + var2 - var21 * 16) / 256.0F;
            float var28 = (float)(var4 + var2 - (var21 + 1) * 16) / 256.0F;
            float var29 = (float)(var5 + var3 - var22 * 16) / 256.0F;
            float var30 = (float)(var5 + var3 - (var22 + 1) * 16) / 256.0F;
            Tessellator var31 = Tessellator.getInstance();
            WorldRenderer var32 = var31.getWorldRenderer();
            var32.begin(7, DefaultVertexFormats.POSITION_TEX_NORMAL);
            var32.pos((double)var23, (double)var26, (double)(-var8)).tex((double)var28, (double)var29).normal(0.0F, 0.0F, -1.0F).endVertex();
            var32.pos((double)var24, (double)var26, (double)(-var8)).tex((double)var27, (double)var29).normal(0.0F, 0.0F, -1.0F).endVertex();
            var32.pos((double)var24, (double)var25, (double)(-var8)).tex((double)var27, (double)var30).normal(0.0F, 0.0F, -1.0F).endVertex();
            var32.pos((double)var23, (double)var25, (double)(-var8)).tex((double)var28, (double)var30).normal(0.0F, 0.0F, -1.0F).endVertex();
            var32.pos((double)var23, (double)var25, (double)var8).tex((double)var9, (double)var11).normal(0.0F, 0.0F, 1.0F).endVertex();
            var32.pos((double)var24, (double)var25, (double)var8).tex((double)var10, (double)var11).normal(0.0F, 0.0F, 1.0F).endVertex();
            var32.pos((double)var24, (double)var26, (double)var8).tex((double)var10, (double)var12).normal(0.0F, 0.0F, 1.0F).endVertex();
            var32.pos((double)var23, (double)var26, (double)var8).tex((double)var9, (double)var12).normal(0.0F, 0.0F, 1.0F).endVertex();
            var32.pos((double)var23, (double)var25, (double)(-var8)).tex((double)var13, (double)var15).normal(0.0F, 1.0F, 0.0F).endVertex();
            var32.pos((double)var24, (double)var25, (double)(-var8)).tex((double)var14, (double)var15).normal(0.0F, 1.0F, 0.0F).endVertex();
            var32.pos((double)var24, (double)var25, (double)var8).tex((double)var14, (double)var16).normal(0.0F, 1.0F, 0.0F).endVertex();
            var32.pos((double)var23, (double)var25, (double)var8).tex((double)var13, (double)var16).normal(0.0F, 1.0F, 0.0F).endVertex();
            var32.pos((double)var23, (double)var26, (double)var8).tex((double)var13, (double)var15).normal(0.0F, -1.0F, 0.0F).endVertex();
            var32.pos((double)var24, (double)var26, (double)var8).tex((double)var14, (double)var15).normal(0.0F, -1.0F, 0.0F).endVertex();
            var32.pos((double)var24, (double)var26, (double)(-var8)).tex((double)var14, (double)var16).normal(0.0F, -1.0F, 0.0F).endVertex();
            var32.pos((double)var23, (double)var26, (double)(-var8)).tex((double)var13, (double)var16).normal(0.0F, -1.0F, 0.0F).endVertex();
            var32.pos((double)var23, (double)var25, (double)var8).tex((double)var18, (double)var19).normal(-1.0F, 0.0F, 0.0F).endVertex();
            var32.pos((double)var23, (double)var26, (double)var8).tex((double)var18, (double)var20).normal(-1.0F, 0.0F, 0.0F).endVertex();
            var32.pos((double)var23, (double)var26, (double)(-var8)).tex((double)var17, (double)var20).normal(-1.0F, 0.0F, 0.0F).endVertex();
            var32.pos((double)var23, (double)var25, (double)(-var8)).tex((double)var17, (double)var19).normal(-1.0F, 0.0F, 0.0F).endVertex();
            var32.pos((double)var24, (double)var25, (double)(-var8)).tex((double)var18, (double)var19).normal(1.0F, 0.0F, 0.0F).endVertex();
            var32.pos((double)var24, (double)var26, (double)(-var8)).tex((double)var18, (double)var20).normal(1.0F, 0.0F, 0.0F).endVertex();
            var32.pos((double)var24, (double)var26, (double)var8).tex((double)var17, (double)var20).normal(1.0F, 0.0F, 0.0F).endVertex();
            var32.pos((double)var24, (double)var25, (double)var8).tex((double)var17, (double)var19).normal(1.0F, 0.0F, 0.0F).endVertex();
            var31.draw();
         }
      }

   }

   public void doRender(EntityPainting var1, double var2, double var4, double var6, float var8, float var9) {
      GlStateManager.pushMatrix();
      GlStateManager.translate(var2, var4, var6);
      GlStateManager.rotate(180.0F - var8, 0.0F, 1.0F, 0.0F);
      GlStateManager.enableRescaleNormal();
      this.bindEntityTexture(var1);
      EntityPainting.EnumArt var10 = var1.art;
      float var11 = 0.0625F;
      GlStateManager.scale(var11, var11, var11);
      this.renderPainting(var1, var10.sizeX, var10.sizeY, var10.offsetX, var10.offsetY);
      GlStateManager.disableRescaleNormal();
      GlStateManager.popMatrix();
      super.doRender(var1, var2, var4, var6, var8, var9);
   }

   public RenderPainting(RenderManager var1) {
      super(var1);
   }
}
