/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.entity.ai;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.player.EntityPlayer;

public class EntityAIWatchClosest
extends EntityAIBase {
    protected EntityLiving theWatcher;
    protected Class<? extends Entity> watchedClass;
    protected float maxDistanceForPlayer;
    protected Entity closestEntity;
    private float chance;
    private int lookTime;

    public EntityAIWatchClosest(EntityLiving entityLiving, Class<? extends Entity> clazz, float f) {
        this.theWatcher = entityLiving;
        this.watchedClass = clazz;
        this.maxDistanceForPlayer = f;
        this.chance = 0.02f;
        this.setMutexBits(2);
    }

    @Override
    public boolean shouldExecute() {
        if (this.theWatcher.getRNG().nextFloat() >= this.chance) {
            return false;
        }
        if (this.theWatcher.getAttackTarget() != null) {
            this.closestEntity = this.theWatcher.getAttackTarget();
        }
        this.closestEntity = this.watchedClass == EntityPlayer.class ? this.theWatcher.worldObj.getClosestPlayerToEntity(this.theWatcher, this.maxDistanceForPlayer) : this.theWatcher.worldObj.findNearestEntityWithinAABB(this.watchedClass, this.theWatcher.getEntityBoundingBox().expand(this.maxDistanceForPlayer, 3.0, this.maxDistanceForPlayer), this.theWatcher);
        return this.closestEntity != null;
    }

    @Override
    public void updateTask() {
        this.theWatcher.getLookHelper().setLookPosition(this.closestEntity.posX, this.closestEntity.posY + (double)this.closestEntity.getEyeHeight(), this.closestEntity.posZ, 10.0f, this.theWatcher.getVerticalFaceSpeed());
        --this.lookTime;
    }

    @Override
    public boolean continueExecuting() {
        return !this.closestEntity.isEntityAlive() ? false : (this.theWatcher.getDistanceSqToEntity(this.closestEntity) > (double)(this.maxDistanceForPlayer * this.maxDistanceForPlayer) ? false : this.lookTime > 0);
    }

    @Override
    public void startExecuting() {
        this.lookTime = 40 + this.theWatcher.getRNG().nextInt(40);
    }

    @Override
    public void resetTask() {
        this.closestEntity = null;
    }

    public EntityAIWatchClosest(EntityLiving entityLiving, Class<? extends Entity> clazz, float f, float f2) {
        this.theWatcher = entityLiving;
        this.watchedClass = clazz;
        this.maxDistanceForPlayer = f;
        this.chance = f2;
        this.setMutexBits(2);
    }
}

