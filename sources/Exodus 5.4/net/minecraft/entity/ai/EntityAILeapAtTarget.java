/*
 * Decompiled with CFR 0.152.
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

    @Override
    public void startExecuting() {
        double d = this.leapTarget.posX - this.leaper.posX;
        double d2 = this.leapTarget.posZ - this.leaper.posZ;
        float f = MathHelper.sqrt_double(d * d + d2 * d2);
        this.leaper.motionX += d / (double)f * 0.5 * (double)0.8f + this.leaper.motionX * (double)0.2f;
        this.leaper.motionZ += d2 / (double)f * 0.5 * (double)0.8f + this.leaper.motionZ * (double)0.2f;
        this.leaper.motionY = this.leapMotionY;
    }

    @Override
    public boolean shouldExecute() {
        this.leapTarget = this.leaper.getAttackTarget();
        if (this.leapTarget == null) {
            return false;
        }
        double d = this.leaper.getDistanceSqToEntity(this.leapTarget);
        return d >= 4.0 && d <= 16.0 ? (!this.leaper.onGround ? false : this.leaper.getRNG().nextInt(5) == 0) : false;
    }

    @Override
    public boolean continueExecuting() {
        return !this.leaper.onGround;
    }

    public EntityAILeapAtTarget(EntityLiving entityLiving, float f) {
        this.leaper = entityLiving;
        this.leapMotionY = f;
        this.setMutexBits(5);
    }
}

