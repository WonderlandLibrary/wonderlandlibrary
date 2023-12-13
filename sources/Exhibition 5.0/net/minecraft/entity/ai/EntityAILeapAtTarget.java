// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.entity.ai;

import net.minecraft.util.MathHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EntityLiving;

public class EntityAILeapAtTarget extends EntityAIBase
{
    EntityLiving leaper;
    EntityLivingBase leapTarget;
    float leapMotionY;
    private static final String __OBFID = "CL_00001591";
    
    public EntityAILeapAtTarget(final EntityLiving p_i1630_1_, final float p_i1630_2_) {
        this.leaper = p_i1630_1_;
        this.leapMotionY = p_i1630_2_;
        this.setMutexBits(5);
    }
    
    @Override
    public boolean shouldExecute() {
        this.leapTarget = this.leaper.getAttackTarget();
        if (this.leapTarget == null) {
            return false;
        }
        final double var1 = this.leaper.getDistanceSqToEntity(this.leapTarget);
        return var1 >= 4.0 && var1 <= 16.0 && this.leaper.onGround && this.leaper.getRNG().nextInt(5) == 0;
    }
    
    @Override
    public boolean continueExecuting() {
        return !this.leaper.onGround;
    }
    
    @Override
    public void startExecuting() {
        final double var1 = this.leapTarget.posX - this.leaper.posX;
        final double var2 = this.leapTarget.posZ - this.leaper.posZ;
        final float var3 = MathHelper.sqrt_double(var1 * var1 + var2 * var2);
        final EntityLiving leaper = this.leaper;
        leaper.motionX += var1 / var3 * 0.5 * 0.800000011920929 + this.leaper.motionX * 0.20000000298023224;
        final EntityLiving leaper2 = this.leaper;
        leaper2.motionZ += var2 / var3 * 0.5 * 0.800000011920929 + this.leaper.motionZ * 0.20000000298023224;
        this.leaper.motionY = this.leapMotionY;
    }
}
