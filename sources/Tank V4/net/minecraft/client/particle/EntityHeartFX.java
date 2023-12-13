package net.minecraft.client.particle;

import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class EntityHeartFX extends EntityFX {
   float particleScaleOverTime;

   protected EntityHeartFX(World var1, double var2, double var4, double var6, double var8, double var10, double var12, float var14) {
      super(var1, var2, var4, var6, 0.0D, 0.0D, 0.0D);
      this.motionX *= 0.009999999776482582D;
      this.motionY *= 0.009999999776482582D;
      this.motionZ *= 0.009999999776482582D;
      this.motionY += 0.1D;
      this.particleScale *= 0.75F;
      this.particleScale *= var14;
      this.particleScaleOverTime = this.particleScale;
      this.particleMaxAge = 16;
      this.noClip = false;
      this.setParticleTextureIndex(80);
   }

   public void renderParticle(WorldRenderer var1, Entity var2, float var3, float var4, float var5, float var6, float var7, float var8) {
      float var9 = ((float)this.particleAge + var3) / (float)this.particleMaxAge * 32.0F;
      var9 = MathHelper.clamp_float(var9, 0.0F, 1.0F);
      this.particleScale = this.particleScaleOverTime * var9;
      super.renderParticle(var1, var2, var3, var4, var5, var6, var7, var8);
   }

   protected EntityHeartFX(World var1, double var2, double var4, double var6, double var8, double var10, double var12) {
      this(var1, var2, var4, var6, var8, var10, var12, 2.0F);
   }

   public void onUpdate() {
      this.prevPosX = this.posX;
      this.prevPosY = this.posY;
      this.prevPosZ = this.posZ;
      if (this.particleAge++ >= this.particleMaxAge) {
         this.setDead();
      }

      this.moveEntity(this.motionX, this.motionY, this.motionZ);
      if (this.posY == this.prevPosY) {
         this.motionX *= 1.1D;
         this.motionZ *= 1.1D;
      }

      this.motionX *= 0.8600000143051147D;
      this.motionY *= 0.8600000143051147D;
      this.motionZ *= 0.8600000143051147D;
      if (this.onGround) {
         this.motionX *= 0.699999988079071D;
         this.motionZ *= 0.699999988079071D;
      }

   }

   public static class Factory implements IParticleFactory {
      public EntityFX getEntityFX(int var1, World var2, double var3, double var5, double var7, double var9, double var11, double var13, int... var15) {
         return new EntityHeartFX(var2, var3, var5, var7, var9, var11, var13);
      }
   }

   public static class AngryVillagerFactory implements IParticleFactory {
      public EntityFX getEntityFX(int var1, World var2, double var3, double var5, double var7, double var9, double var11, double var13, int... var15) {
         EntityHeartFX var16 = new EntityHeartFX(var2, var3, var5 + 0.5D, var7, var9, var11, var13);
         var16.setParticleTextureIndex(81);
         var16.setRBGColorF(1.0F, 1.0F, 1.0F);
         return var16;
      }
   }
}
