/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.entity.ai;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.util.math.MathHelper;

public class EntityAILeapAtTarget
extends EntityAIBase {
    EntityLiving leaper;
    EntityLivingBase leapTarget;
    float leapMotionY;

    public EntityAILeapAtTarget(EntityLiving leapingEntity, float leapMotionYIn) {
        this.leaper = leapingEntity;
        this.leapMotionY = leapMotionYIn;
        this.setMutexBits(5);
    }

    @Override
    public boolean shouldExecute() {
        this.leapTarget = this.leaper.getAttackTarget();
        if (this.leapTarget == null) {
            return false;
        }
        double d0 = this.leaper.getDistanceSqToEntity(this.leapTarget);
        if (d0 >= 4.0 && d0 <= 16.0) {
            if (!this.leaper.onGround) {
                return false;
            }
            return this.leaper.getRNG().nextInt(5) == 0;
        }
        return false;
    }

    @Override
    public boolean continueExecuting() {
        return !this.leaper.onGround;
    }

    @Override
    public void startExecuting() {
        double d0 = this.leapTarget.posX - this.leaper.posX;
        double d1 = this.leapTarget.posZ - this.leaper.posZ;
        float f = MathHelper.sqrt(d0 * d0 + d1 * d1);
        if ((double)f >= 1.0E-4) {
            this.leaper.motionX += d0 / (double)f * 0.5 * (double)0.8f + this.leaper.motionX * (double)0.2f;
            this.leaper.motionZ += d1 / (double)f * 0.5 * (double)0.8f + this.leaper.motionZ * (double)0.2f;
        }
        this.leaper.motionY = this.leapMotionY;
    }
}

