package net.minecraft.client.particle;

import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class EntityCrit2FX extends EntityFX {
   float field_174839_a;

   public void renderParticle(WorldRenderer var1, Entity var2, float var3, float var4, float var5, float var6, float var7, float var8) {
      float var9 = ((float)this.particleAge + var3) / (float)this.particleMaxAge * 32.0F;
      var9 = MathHelper.clamp_float(var9, 0.0F, 1.0F);
      this.particleScale = this.field_174839_a * var9;
      super.renderParticle(var1, var2, var3, var4, var5, var6, var7, var8);
   }

   public void onUpdate() {
      this.prevPosX = this.posX;
      this.prevPosY = this.posY;
      this.prevPosZ = this.posZ;
      if (this.particleAge++ >= this.particleMaxAge) {
         this.setDead();
      }

      this.moveEntity(this.motionX, this.motionY, this.motionZ);
      this.particleGreen = (float)((double)this.particleGreen * 0.96D);
      this.particleBlue = (float)((double)this.particleBlue * 0.9D);
      this.motionX *= 0.699999988079071D;
      this.motionY *= 0.699999988079071D;
      this.motionZ *= 0.699999988079071D;
      this.motionY -= 0.019999999552965164D;
      if (this.onGround) {
         this.motionX *= 0.699999988079071D;
         this.motionZ *= 0.699999988079071D;
      }

   }

   protected EntityCrit2FX(World var1, double var2, double var4, double var6, double var8, double var10, double var12) {
      this(var1, var2, var4, var6, var8, var10, var12, 1.0F);
   }

   protected EntityCrit2FX(World var1, double var2, double var4, double var6, double var8, double var10, double var12, float var14) {
      super(var1, var2, var4, var6, 0.0D, 0.0D, 0.0D);
      this.motionX *= 0.10000000149011612D;
      this.motionY *= 0.10000000149011612D;
      this.motionZ *= 0.10000000149011612D;
      this.motionX += var8 * 0.4D;
      this.motionY += var10 * 0.4D;
      this.motionZ += var12 * 0.4D;
      this.particleRed = this.particleGreen = this.particleBlue = (float)(Math.random() * 0.30000001192092896D + 0.6000000238418579D);
      this.particleScale *= 0.75F;
      this.particleScale *= var14;
      this.field_174839_a = this.particleScale;
      this.particleMaxAge = (int)(6.0D / (Math.random() * 0.8D + 0.6D));
      this.particleMaxAge = (int)((float)this.particleMaxAge * var14);
      this.noClip = false;
      this.setParticleTextureIndex(65);
      this.onUpdate();
   }

   public static class MagicFactory implements IParticleFactory {
      public EntityFX getEntityFX(int var1, World var2, double var3, double var5, double var7, double var9, double var11, double var13, int... var15) {
         EntityCrit2FX var16 = new EntityCrit2FX(var2, var3, var5, var7, var9, var11, var13);
         var16.setRBGColorF(var16.getRedColorF() * 0.3F, var16.getGreenColorF() * 0.8F, var16.getBlueColorF());
         var16.nextTextureIndexX();
         return var16;
      }
   }

   public static class Factory implements IParticleFactory {
      public EntityFX getEntityFX(int var1, World var2, double var3, double var5, double var7, double var9, double var11, double var13, int... var15) {
         return new EntityCrit2FX(var2, var3, var5, var7, var9, var11, var13);
      }
   }
}
