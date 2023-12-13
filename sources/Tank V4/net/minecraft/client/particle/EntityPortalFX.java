package net.minecraft.client.particle;

import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.world.World;

public class EntityPortalFX extends EntityFX {
   private double portalPosZ;
   private float portalParticleScale;
   private double portalPosX;
   private double portalPosY;

   public float getBrightness(float var1) {
      float var2 = super.getBrightness(var1);
      float var3 = (float)this.particleAge / (float)this.particleMaxAge;
      var3 = var3 * var3 * var3 * var3;
      return var2 * (1.0F - var3) + var3;
   }

   public void renderParticle(WorldRenderer var1, Entity var2, float var3, float var4, float var5, float var6, float var7, float var8) {
      float var9 = ((float)this.particleAge + var3) / (float)this.particleMaxAge;
      var9 = 1.0F - var9;
      var9 *= var9;
      var9 = 1.0F - var9;
      this.particleScale = this.portalParticleScale * var9;
      super.renderParticle(var1, var2, var3, var4, var5, var6, var7, var8);
   }

   protected EntityPortalFX(World var1, double var2, double var4, double var6, double var8, double var10, double var12) {
      super(var1, var2, var4, var6, var8, var10, var12);
      this.motionX = var8;
      this.motionY = var10;
      this.motionZ = var12;
      this.portalPosX = this.posX = var2;
      this.portalPosY = this.posY = var4;
      this.portalPosZ = this.posZ = var6;
      float var14 = this.rand.nextFloat() * 0.6F + 0.4F;
      this.portalParticleScale = this.particleScale = this.rand.nextFloat() * 0.2F + 0.5F;
      this.particleRed = this.particleGreen = this.particleBlue = 1.0F * var14;
      this.particleGreen *= 0.3F;
      this.particleRed *= 0.9F;
      this.particleMaxAge = (int)(Math.random() * 10.0D) + 40;
      this.noClip = true;
      this.setParticleTextureIndex((int)(Math.random() * 8.0D));
   }

   public void onUpdate() {
      this.prevPosX = this.posX;
      this.prevPosY = this.posY;
      this.prevPosZ = this.posZ;
      float var1 = (float)this.particleAge / (float)this.particleMaxAge;
      var1 = -var1 + var1 * var1 * 2.0F;
      var1 = 1.0F - var1;
      this.posX = this.portalPosX + this.motionX * (double)var1;
      this.posY = this.portalPosY + this.motionY * (double)var1 + (double)(1.0F - var1);
      this.posZ = this.portalPosZ + this.motionZ * (double)var1;
      if (this.particleAge++ >= this.particleMaxAge) {
         this.setDead();
      }

   }

   public int getBrightnessForRender(float var1) {
      int var2 = super.getBrightnessForRender(var1);
      float var3 = (float)this.particleAge / (float)this.particleMaxAge;
      var3 *= var3;
      var3 *= var3;
      int var4 = var2 & 255;
      int var5 = var2 >> 16 & 255;
      var5 += (int)(var3 * 15.0F * 16.0F);
      if (var5 > 240) {
         var5 = 240;
      }

      return var4 | var5 << 16;
   }

   public static class Factory implements IParticleFactory {
      public EntityFX getEntityFX(int var1, World var2, double var3, double var5, double var7, double var9, double var11, double var13, int... var15) {
         return new EntityPortalFX(var2, var3, var5, var7, var9, var11, var13);
      }
   }
}
