/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.entity.ai;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.world.World;

public class EntityAIOcelotAttack
extends EntityAIBase {
    EntityLiving theEntity;
    EntityLivingBase theVictim;
    int attackCountdown;
    World theWorld;

    @Override
    public boolean shouldExecute() {
        EntityLivingBase entityLivingBase = this.theEntity.getAttackTarget();
        if (entityLivingBase == null) {
            return false;
        }
        this.theVictim = entityLivingBase;
        return true;
    }

    public EntityAIOcelotAttack(EntityLiving entityLiving) {
        this.theEntity = entityLiving;
        this.theWorld = entityLiving.worldObj;
        this.setMutexBits(3);
    }

    @Override
    public void resetTask() {
        this.theVictim = null;
        this.theEntity.getNavigator().clearPathEntity();
    }

    @Override
    public void updateTask() {
        this.theEntity.getLookHelper().setLookPositionWithEntity(this.theVictim, 30.0f, 30.0f);
        double d = this.theEntity.width * 2.0f * this.theEntity.width * 2.0f;
        double d2 = this.theEntity.getDistanceSq(this.theVictim.posX, this.theVictim.getEntityBoundingBox().minY, this.theVictim.posZ);
        double d3 = 0.8;
        if (d2 > d && d2 < 16.0) {
            d3 = 1.33;
        } else if (d2 < 225.0) {
            d3 = 0.6;
        }
        this.theEntity.getNavigator().tryMoveToEntityLiving(this.theVictim, d3);
        this.attackCountdown = Math.max(this.attackCountdown - 1, 0);
        if (d2 <= d && this.attackCountdown <= 0) {
            this.attackCountdown = 20;
            this.theEntity.attackEntityAsMob(this.theVictim);
        }
    }

    @Override
    public boolean continueExecuting() {
        return !this.theVictim.isEntityAlive() ? false : (this.theEntity.getDistanceSqToEntity(this.theVictim) > 225.0 ? false : !this.theEntity.getNavigator().noPath() || this.shouldExecute());
    }
}

