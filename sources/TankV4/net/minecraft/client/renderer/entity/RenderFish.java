package net.minecraft.client.renderer.entity;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityFishHook;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Vec3;

public class RenderFish extends Render {
   private static final ResourceLocation FISH_PARTICLES = new ResourceLocation("textures/particle/particles.png");

   public void doRender(EntityFishHook var1, double var2, double var4, double var6, float var8, float var9) {
      GlStateManager.pushMatrix();
      GlStateManager.translate((float)var2, (float)var4, (float)var6);
      GlStateManager.enableRescaleNormal();
      GlStateManager.scale(0.5F, 0.5F, 0.5F);
      this.bindEntityTexture(var1);
      Tessellator var10 = Tessellator.getInstance();
      WorldRenderer var11 = var10.getWorldRenderer();
      boolean var12 = true;
      boolean var13 = true;
      float var14 = 0.0625F;
      float var15 = 0.125F;
      float var16 = 0.125F;
      float var17 = 0.1875F;
      float var18 = 1.0F;
      float var19 = 0.5F;
      float var20 = 0.5F;
      GlStateManager.rotate(180.0F - RenderManager.playerViewY, 0.0F, 1.0F, 0.0F);
      GlStateManager.rotate(-this.renderManager.playerViewX, 1.0F, 0.0F, 0.0F);
      var11.begin(7, DefaultVertexFormats.POSITION_TEX_NORMAL);
      var11.pos(-0.5D, -0.5D, 0.0D).tex(0.0625D, 0.1875D).normal(0.0F, 1.0F, 0.0F).endVertex();
      var11.pos(0.5D, -0.5D, 0.0D).tex(0.125D, 0.1875D).normal(0.0F, 1.0F, 0.0F).endVertex();
      var11.pos(0.5D, 0.5D, 0.0D).tex(0.125D, 0.125D).normal(0.0F, 1.0F, 0.0F).endVertex();
      var11.pos(-0.5D, 0.5D, 0.0D).tex(0.0625D, 0.125D).normal(0.0F, 1.0F, 0.0F).endVertex();
      var10.draw();
      GlStateManager.disableRescaleNormal();
      GlStateManager.popMatrix();
      if (var1.angler != null) {
         double var24;
         double var26;
         double var28;
         double var30;
         label39: {
            float var21 = var1.angler.getSwingProgress(var9);
            float var22 = MathHelper.sin(MathHelper.sqrt_float(var21) * 3.1415927F);
            Vec3 var23 = new Vec3(-0.36D, 0.03D, 0.35D);
            var23 = var23.rotatePitch(-(var1.angler.prevRotationPitch + (var1.angler.rotationPitch - var1.angler.prevRotationPitch) * var9) * 3.1415927F / 180.0F);
            var23 = var23.rotateYaw(-(var1.angler.prevRotationYaw + (var1.angler.rotationYaw - var1.angler.prevRotationYaw) * var9) * 3.1415927F / 180.0F);
            var23 = var23.rotateYaw(var22 * 0.5F);
            var23 = var23.rotatePitch(-var22 * 0.7F);
            var24 = var1.angler.prevPosX + (var1.angler.posX - var1.angler.prevPosX) * (double)var9 + var23.xCoord;
            var26 = var1.angler.prevPosY + (var1.angler.posY - var1.angler.prevPosY) * (double)var9 + var23.yCoord;
            var28 = var1.angler.prevPosZ + (var1.angler.posZ - var1.angler.prevPosZ) * (double)var9 + var23.zCoord;
            var30 = (double)var1.angler.getEyeHeight();
            if (this.renderManager.options == null || this.renderManager.options.thirdPersonView <= 0) {
               EntityPlayer var10000 = var1.angler;
               Minecraft.getMinecraft();
               if (var10000 == Minecraft.thePlayer) {
                  break label39;
               }
            }

            float var32 = (var1.angler.prevRenderYawOffset + (var1.angler.renderYawOffset - var1.angler.prevRenderYawOffset) * var9) * 3.1415927F / 180.0F;
            double var33 = (double)MathHelper.sin(var32);
            double var35 = (double)MathHelper.cos(var32);
            double var37 = 0.35D;
            double var39 = 0.8D;
            var24 = var1.angler.prevPosX + (var1.angler.posX - var1.angler.prevPosX) * (double)var9 - var35 * 0.35D - var33 * 0.8D;
            var26 = var1.angler.prevPosY + var30 + (var1.angler.posY - var1.angler.prevPosY) * (double)var9 - 0.45D;
            var28 = var1.angler.prevPosZ + (var1.angler.posZ - var1.angler.prevPosZ) * (double)var9 - var33 * 0.35D + var35 * 0.8D;
            var30 = var1.angler.isSneaking() ? -0.1875D : 0.0D;
         }

         double var47 = var1.prevPosX + (var1.posX - var1.prevPosX) * (double)var9;
         double var34 = var1.prevPosY + (var1.posY - var1.prevPosY) * (double)var9 + 0.25D;
         double var36 = var1.prevPosZ + (var1.posZ - var1.prevPosZ) * (double)var9;
         double var38 = (double)((float)(var24 - var47));
         double var40 = (double)((float)(var26 - var34)) + var30;
         double var42 = (double)((float)(var28 - var36));
         GlStateManager.disableTexture2D();
         GlStateManager.disableLighting();
         var11.begin(3, DefaultVertexFormats.POSITION_COLOR);
         boolean var44 = true;

         for(int var45 = 0; var45 <= 16; ++var45) {
            float var46 = (float)var45 / 16.0F;
            var11.pos(var2 + var38 * (double)var46, var4 + var40 * (double)(var46 * var46 + var46) * 0.5D + 0.25D, var6 + var42 * (double)var46).color(0, 0, 0, 255).endVertex();
         }

         var10.draw();
         GlStateManager.enableLighting();
         GlStateManager.enableTexture2D();
         super.doRender(var1, var2, var4, var6, var8, var9);
      }

   }

   public RenderFish(RenderManager var1) {
      super(var1);
   }

   protected ResourceLocation getEntityTexture(Entity var1) {
      return this.getEntityTexture((EntityFishHook)var1);
   }

   protected ResourceLocation getEntityTexture(EntityFishHook var1) {
      return FISH_PARTICLES;
   }

   public void doRender(Entity var1, double var2, double var4, double var6, float var8, float var9) {
      this.doRender((EntityFishHook)var1, var2, var4, var6, var8, var9);
   }
}
