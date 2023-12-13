/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.entity.ai;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.world.World;

public class EntityAIOcelotAttack
extends EntityAIBase {
    World theWorld;
    EntityLiving theEntity;
    EntityLivingBase theVictim;
    int attackCountdown;
    private static final String __OBFID = "CL_00001600";

    public EntityAIOcelotAttack(EntityLiving p_i1641_1_) {
        this.theEntity = p_i1641_1_;
        this.theWorld = p_i1641_1_.worldObj;
        this.setMutexBits(3);
    }

    @Override
    public boolean shouldExecute() {
        EntityLivingBase var1 = this.theEntity.getAttackTarget();
        if (var1 == null) {
            return false;
        }
        this.theVictim = var1;
        return true;
    }

    @Override
    public boolean continueExecuting() {
        return !this.theVictim.isEntityAlive() ? false : (this.theEntity.getDistanceSqToEntity(this.theVictim) > 225.0 ? false : !this.theEntity.getNavigator().noPath() || this.shouldExecute());
    }

    @Override
    public void resetTask() {
        this.theVictim = null;
        this.theEntity.getNavigator().clearPathEntity();
    }

    @Override
    public void updateTask() {
        this.theEntity.getLookHelper().setLookPositionWithEntity(this.theVictim, 30.0f, 30.0f);
        double var1 = this.theEntity.width * 2.0f * this.theEntity.width * 2.0f;
        double var3 = this.theEntity.getDistanceSq(this.theVictim.posX, this.theVictim.getEntityBoundingBox().minY, this.theVictim.posZ);
        double var5 = 0.8;
        if (var3 > var1 && var3 < 16.0) {
            var5 = 1.33;
        } else if (var3 < 225.0) {
            var5 = 0.6;
        }
        this.theEntity.getNavigator().tryMoveToEntityLiving(this.theVictim, var5);
        this.attackCountdown = Math.max(this.attackCountdown - 1, 0);
        if (var3 <= var1 && this.attackCountdown <= 0) {
            this.attackCountdown = 20;
            this.theEntity.attackEntityAsMob(this.theVictim);
        }
    }
}

