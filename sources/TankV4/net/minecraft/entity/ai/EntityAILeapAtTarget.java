package net.minecraft.entity.ai;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.MathHelper;

public class EntityAILeapAtTarget extends EntityAIBase {
   EntityLivingBase leapTarget;
   float leapMotionY;
   EntityLiving leaper;

   public boolean continueExecuting() {
      return !this.leaper.onGround;
   }

   public boolean shouldExecute() {
      this.leapTarget = this.leaper.getAttackTarget();
      if (this.leapTarget == null) {
         return false;
      } else {
         double var1 = this.leaper.getDistanceSqToEntity(this.leapTarget);
         return var1 >= 4.0D && var1 <= 16.0D ? (!this.leaper.onGround ? false : this.leaper.getRNG().nextInt(5) == 0) : false;
      }
   }

   public void startExecuting() {
      double var1 = this.leapTarget.posX - this.leaper.posX;
      double var3 = this.leapTarget.posZ - this.leaper.posZ;
      float var5 = MathHelper.sqrt_double(var1 * var1 + var3 * var3);
      EntityLiving var10000 = this.leaper;
      var10000.motionX += var1 / (double)var5 * 0.5D * 0.800000011920929D + this.leaper.motionX * 0.20000000298023224D;
      var10000 = this.leaper;
      var10000.motionZ += var3 / (double)var5 * 0.5D * 0.800000011920929D + this.leaper.motionZ * 0.20000000298023224D;
      this.leaper.motionY = (double)this.leapMotionY;
   }

   public EntityAILeapAtTarget(EntityLiving var1, float var2) {
      this.leaper = var1;
      this.leapMotionY = var2;
      this.setMutexBits(5);
   }
}
