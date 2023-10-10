package net.minecraft.client.particle;

import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class EntityLavaFX extends EntityFX {
   private float lavaParticleScale;

   public void onUpdate() {
      this.prevPosX = this.posX;
      this.prevPosY = this.posY;
      this.prevPosZ = this.posZ;
      if (this.particleAge++ >= this.particleMaxAge) {
         this.setDead();
      }

      float var1 = (float)this.particleAge / (float)this.particleMaxAge;
      if (this.rand.nextFloat() > var1) {
         this.worldObj.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, this.posX, this.posY, this.posZ, this.motionX, this.motionY, this.motionZ);
      }

      this.motionY -= 0.03D;
      this.moveEntity(this.motionX, this.motionY, this.motionZ);
      this.motionX *= 0.9990000128746033D;
      this.motionY *= 0.9990000128746033D;
      this.motionZ *= 0.9990000128746033D;
      if (this.onGround) {
         this.motionX *= 0.699999988079071D;
         this.motionZ *= 0.699999988079071D;
      }

   }

   public void renderParticle(WorldRenderer var1, Entity var2, float var3, float var4, float var5, float var6, float var7, float var8) {
      float var9 = ((float)this.particleAge + var3) / (float)this.particleMaxAge;
      this.particleScale = this.lavaParticleScale * (1.0F - var9 * var9);
      super.renderParticle(var1, var2, var3, var4, var5, var6, var7, var8);
   }

   protected EntityLavaFX(World var1, double var2, double var4, double var6) {
      super(var1, var2, var4, var6, 0.0D, 0.0D, 0.0D);
      this.motionX *= 0.800000011920929D;
      this.motionY *= 0.800000011920929D;
      this.motionZ *= 0.800000011920929D;
      this.motionY = (double)(this.rand.nextFloat() * 0.4F + 0.05F);
      this.particleRed = this.particleGreen = this.particleBlue = 1.0F;
      this.particleScale *= this.rand.nextFloat() * 2.0F + 0.2F;
      this.lavaParticleScale = this.particleScale;
      this.particleMaxAge = (int)(16.0D / (Math.random() * 0.8D + 0.2D));
      this.noClip = false;
      this.setParticleTextureIndex(49);
   }

   public float getBrightness(float var1) {
      return 1.0F;
   }

   public int getBrightnessForRender(float var1) {
      float var2 = ((float)this.particleAge + var1) / (float)this.particleMaxAge;
      var2 = MathHelper.clamp_float(var2, 0.0F, 1.0F);
      int var3 = super.getBrightnessForRender(var1);
      short var4 = 240;
      int var5 = var3 >> 16 & 255;
      return var4 | var5 << 16;
   }

   public static class Factory implements IParticleFactory {
      public EntityFX getEntityFX(int var1, World var2, double var3, double var5, double var7, double var9, double var11, double var13, int... var15) {
         return new EntityLavaFX(var2, var3, var5, var7);
      }
   }
}
