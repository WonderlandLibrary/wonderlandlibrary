package net.minecraft.entity.ai;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.MathHelper;

public class EntityAILeapAtTarget extends EntityAIBase {
  EntityLiving leaper;
  
  EntityLivingBase leapTarget;
  
  float leapMotionY;
  
  public EntityAILeapAtTarget(EntityLiving leapingEntity, float leapMotionYIn) {
    this.leaper = leapingEntity;
    this.leapMotionY = leapMotionYIn;
    setMutexBits(5);
  }
  
  public boolean shouldExecute() {
    this.leapTarget = this.leaper.getAttackTarget();
    if (this.leapTarget == null)
      return false; 
    double d0 = this.leaper.getDistanceSqToEntity((Entity)this.leapTarget);
    return (d0 >= 4.0D && d0 <= 16.0D) ? (!this.leaper.onGround ? false : ((this.leaper.getRNG().nextInt(5) == 0))) : false;
  }
  
  public boolean continueExecuting() {
    return !this.leaper.onGround;
  }
  
  public void startExecuting() {
    double d0 = this.leapTarget.posX - this.leaper.posX;
    double d1 = this.leapTarget.posZ - this.leaper.posZ;
    float f = MathHelper.sqrt_double(d0 * d0 + d1 * d1);
    this.leaper.motionX += d0 / f * 0.5D * 0.800000011920929D + this.leaper.motionX * 0.20000000298023224D;
    this.leaper.motionZ += d1 / f * 0.5D * 0.800000011920929D + this.leaper.motionZ * 0.20000000298023224D;
    this.leaper.motionY = this.leapMotionY;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\net\minecraft\entity\ai\EntityAILeapAtTarget.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */