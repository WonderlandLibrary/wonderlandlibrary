package net.minecraft.client.renderer.entity;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelMinecart;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Vec3;

public class RenderMinecart extends Render {
   private static final ResourceLocation minecartTextures = new ResourceLocation("textures/entity/minecart.png");
   protected ModelBase modelMinecart = new ModelMinecart();

   public void doRender(Entity var1, double var2, double var4, double var6, float var8, float var9) {
      this.doRender((EntityMinecart)var1, var2, var4, var6, var8, var9);
   }

   public void doRender(EntityMinecart var1, double var2, double var4, double var6, float var8, float var9) {
      GlStateManager.pushMatrix();
      this.bindEntityTexture(var1);
      long var10 = (long)var1.getEntityId() * 493286711L;
      var10 = var10 * var10 * 4392167121L + var10 * 98761L;
      float var12 = (((float)(var10 >> 16 & 7L) + 0.5F) / 8.0F - 0.5F) * 0.004F;
      float var13 = (((float)(var10 >> 20 & 7L) + 0.5F) / 8.0F - 0.5F) * 0.004F;
      float var14 = (((float)(var10 >> 24 & 7L) + 0.5F) / 8.0F - 0.5F) * 0.004F;
      GlStateManager.translate(var12, var13, var14);
      double var15 = var1.lastTickPosX + (var1.posX - var1.lastTickPosX) * (double)var9;
      double var17 = var1.lastTickPosY + (var1.posY - var1.lastTickPosY) * (double)var9;
      double var19 = var1.lastTickPosZ + (var1.posZ - var1.lastTickPosZ) * (double)var9;
      double var21 = 0.30000001192092896D;
      Vec3 var23 = var1.func_70489_a(var15, var17, var19);
      float var24 = var1.prevRotationPitch + (var1.rotationPitch - var1.prevRotationPitch) * var9;
      if (var23 != null) {
         Vec3 var25 = var1.func_70495_a(var15, var17, var19, var21);
         Vec3 var26 = var1.func_70495_a(var15, var17, var19, -var21);
         if (var25 == null) {
            var25 = var23;
         }

         if (var26 == null) {
            var26 = var23;
         }

         var2 += var23.xCoord - var15;
         var4 += (var25.yCoord + var26.yCoord) / 2.0D - var17;
         var6 += var23.zCoord - var19;
         Vec3 var27 = var26.addVector(-var25.xCoord, -var25.yCoord, -var25.zCoord);
         if (var27.lengthVector() != 0.0D) {
            var27 = var27.normalize();
            var8 = (float)(Math.atan2(var27.zCoord, var27.xCoord) * 180.0D / 3.141592653589793D);
            var24 = (float)(Math.atan(var27.yCoord) * 73.0D);
         }
      }

      GlStateManager.translate((float)var2, (float)var4 + 0.375F, (float)var6);
      GlStateManager.rotate(180.0F - var8, 0.0F, 1.0F, 0.0F);
      GlStateManager.rotate(-var24, 0.0F, 0.0F, 1.0F);
      float var30 = (float)var1.getRollingAmplitude() - var9;
      float var31 = var1.getDamage() - var9;
      if (var31 < 0.0F) {
         var31 = 0.0F;
      }

      if (var30 > 0.0F) {
         GlStateManager.rotate(MathHelper.sin(var30) * var30 * var31 / 10.0F * (float)var1.getRollingDirection(), 1.0F, 0.0F, 0.0F);
      }

      int var32 = var1.getDisplayTileOffset();
      IBlockState var28 = var1.getDisplayTile();
      if (var28.getBlock().getRenderType() != -1) {
         GlStateManager.pushMatrix();
         this.bindTexture(TextureMap.locationBlocksTexture);
         float var29 = 0.75F;
         GlStateManager.scale(var29, var29, var29);
         GlStateManager.translate(-0.5F, (float)(var32 - 8) / 16.0F, 0.5F);
         this.func_180560_a(var1, var9, var28);
         GlStateManager.popMatrix();
         GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
         this.bindEntityTexture(var1);
      }

      GlStateManager.scale(-1.0F, -1.0F, 1.0F);
      this.modelMinecart.render(var1, 0.0F, 0.0F, -0.1F, 0.0F, 0.0F, 0.0625F);
      GlStateManager.popMatrix();
      super.doRender(var1, var2, var4, var6, var8, var9);
   }

   public RenderMinecart(RenderManager var1) {
      super(var1);
      this.shadowSize = 0.5F;
   }

   protected ResourceLocation getEntityTexture(Entity var1) {
      return this.getEntityTexture((EntityMinecart)var1);
   }

   protected ResourceLocation getEntityTexture(EntityMinecart var1) {
      return minecartTextures;
   }

   protected void func_180560_a(EntityMinecart var1, float var2, IBlockState var3) {
      GlStateManager.pushMatrix();
      Minecraft.getMinecraft().getBlockRendererDispatcher().renderBlockBrightness(var3, var1.getBrightness(var2));
      GlStateManager.popMatrix();
   }
}
