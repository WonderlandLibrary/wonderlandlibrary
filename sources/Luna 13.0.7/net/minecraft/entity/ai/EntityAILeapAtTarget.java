package net.minecraft.entity.ai;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.MathHelper;

public class EntityAILeapAtTarget
  extends EntityAIBase
{
  EntityLiving leaper;
  EntityLivingBase leapTarget;
  float leapMotionY;
  private static final String __OBFID = "CL_00001591";
  
  public EntityAILeapAtTarget(EntityLiving p_i1630_1_, float p_i1630_2_)
  {
    this.leaper = p_i1630_1_;
    this.leapMotionY = p_i1630_2_;
    setMutexBits(5);
  }
  
  public boolean shouldExecute()
  {
    this.leapTarget = this.leaper.getAttackTarget();
    if (this.leapTarget == null) {
      return false;
    }
    double var1 = this.leaper.getDistanceSqToEntity(this.leapTarget);
    return this.leaper.onGround;
  }
  
  public boolean continueExecuting()
  {
    return !this.leaper.onGround;
  }
  
  public void startExecuting()
  {
    double var1 = this.leapTarget.posX - this.leaper.posX;
    double var3 = this.leapTarget.posZ - this.leaper.posZ;
    float var5 = MathHelper.sqrt_double(var1 * var1 + var3 * var3);
    this.leaper.motionX += var1 / var5 * 0.5D * 0.800000011920929D + this.leaper.motionX * 0.20000000298023224D;
    this.leaper.motionZ += var3 / var5 * 0.5D * 0.800000011920929D + this.leaper.motionZ * 0.20000000298023224D;
    this.leaper.motionY = this.leapMotionY;
  }
}
