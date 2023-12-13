/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.entity.ai;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.util.MathHelper;

public class EntityAILeapAtTarget
extends EntityAIBase {
    EntityLiving leaper;
    EntityLivingBase leapTarget;
    float leapMotionY;
    private static final String __OBFID = "CL_00001591";

    public EntityAILeapAtTarget(EntityLiving p_i1630_1_, float p_i1630_2_) {
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
        double var1 = this.leaper.getDistanceSqToEntity(this.leapTarget);
        return var1 >= 4.0 && var1 <= 16.0 ? (!this.leaper.onGround ? false : this.leaper.getRNG().nextInt(5) == 0) : false;
    }

    @Override
    public boolean continueExecuting() {
        return !this.leaper.onGround;
    }

    @Override
    public void startExecuting() {
        double var1 = this.leapTarget.posX - this.leaper.posX;
        double var3 = this.leapTarget.posZ - this.leaper.posZ;
        float var5 = MathHelper.sqrt_double(var1 * var1 + var3 * var3);
        this.leaper.motionX += var1 / (double)var5 * 0.5 * (double)0.8f + this.leaper.motionX * (double)0.2f;
        this.leaper.motionZ += var3 / (double)var5 * 0.5 * (double)0.8f + this.leaper.motionZ * (double)0.2f;
        this.leaper.motionY = this.leapMotionY;
    }
}

