package net.minecraft.client.particle;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityGuardian;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class MobAppearance extends EntityFX {
   private EntityLivingBase entity;

   protected MobAppearance(World var1, double var2, double var4, double var6) {
      super(var1, var2, var4, var6, 0.0D, 0.0D, 0.0D);
      this.particleRed = this.particleGreen = this.particleBlue = 1.0F;
      this.motionX = this.motionY = this.motionZ = 0.0D;
      this.particleGravity = 0.0F;
      this.particleMaxAge = 30;
   }

   public int getFXLayer() {
      return 3;
   }

   public void renderParticle(WorldRenderer var1, Entity var2, float var3, float var4, float var5, float var6, float var7, float var8) {
      if (this.entity != null) {
         RenderManager var9 = Minecraft.getMinecraft().getRenderManager();
         var9.setRenderPosition(EntityFX.interpPosX, EntityFX.interpPosY, EntityFX.interpPosZ);
         float var10 = 0.42553192F;
         float var11 = ((float)this.particleAge + var3) / (float)this.particleMaxAge;
         GlStateManager.depthMask(true);
         GlStateManager.enableBlend();
         GlStateManager.enableDepth();
         GlStateManager.blendFunc(770, 771);
         float var12 = 240.0F;
         OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, var12, var12);
         GlStateManager.pushMatrix();
         float var13 = 0.05F + 0.5F * MathHelper.sin(var11 * 3.1415927F);
         GlStateManager.color(1.0F, 1.0F, 1.0F, var13);
         GlStateManager.translate(0.0F, 1.8F, 0.0F);
         GlStateManager.rotate(180.0F - var2.rotationYaw, 0.0F, 1.0F, 0.0F);
         GlStateManager.rotate(60.0F - 150.0F * var11 - var2.rotationPitch, 1.0F, 0.0F, 0.0F);
         GlStateManager.translate(0.0F, -0.4F, -1.5F);
         GlStateManager.scale(var10, var10, var10);
         this.entity.rotationYaw = this.entity.prevRotationYaw = 0.0F;
         this.entity.rotationYawHead = this.entity.prevRotationYawHead = 0.0F;
         var9.renderEntityWithPosYaw(this.entity, 0.0D, 0.0D, 0.0D, 0.0F, var3);
         GlStateManager.popMatrix();
         GlStateManager.enableDepth();
      }

   }

   public void onUpdate() {
      super.onUpdate();
      if (this.entity == null) {
         EntityGuardian var1 = new EntityGuardian(this.worldObj);
         var1.setElder();
         this.entity = var1;
      }

   }

   public static class Factory implements IParticleFactory {
      public EntityFX getEntityFX(int var1, World var2, double var3, double var5, double var7, double var9, double var11, double var13, int... var15) {
         return new MobAppearance(var2, var3, var5, var7);
      }
   }
}
