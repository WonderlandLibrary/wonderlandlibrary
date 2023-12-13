/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.entity.ai;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.player.EntityPlayer;

public class EntityAIWatchClosest
extends EntityAIBase {
    protected EntityLiving theWatcher;
    protected Entity closestEntity;
    protected float maxDistanceForPlayer;
    private int lookTime;
    private float field_75331_e;
    protected Class watchedClass;
    private static final String __OBFID = "CL_00001592";

    public EntityAIWatchClosest(EntityLiving p_i1631_1_, Class p_i1631_2_, float p_i1631_3_) {
        this.theWatcher = p_i1631_1_;
        this.watchedClass = p_i1631_2_;
        this.maxDistanceForPlayer = p_i1631_3_;
        this.field_75331_e = 0.02f;
        this.setMutexBits(2);
    }

    public EntityAIWatchClosest(EntityLiving p_i1632_1_, Class p_i1632_2_, float p_i1632_3_, float p_i1632_4_) {
        this.theWatcher = p_i1632_1_;
        this.watchedClass = p_i1632_2_;
        this.maxDistanceForPlayer = p_i1632_3_;
        this.field_75331_e = p_i1632_4_;
        this.setMutexBits(2);
    }

    @Override
    public boolean shouldExecute() {
        if (this.theWatcher.getRNG().nextFloat() >= this.field_75331_e) {
            return false;
        }
        if (this.theWatcher.getAttackTarget() != null) {
            this.closestEntity = this.theWatcher.getAttackTarget();
        }
        this.closestEntity = this.watchedClass == EntityPlayer.class ? this.theWatcher.worldObj.getClosestPlayerToEntity(this.theWatcher, this.maxDistanceForPlayer) : this.theWatcher.worldObj.findNearestEntityWithinAABB(this.watchedClass, this.theWatcher.getEntityBoundingBox().expand(this.maxDistanceForPlayer, 3.0, this.maxDistanceForPlayer), this.theWatcher);
        return this.closestEntity != null;
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

    @Override
    public void updateTask() {
        this.theWatcher.getLookHelper().setLookPosition(this.closestEntity.posX, this.closestEntity.posY + (double)this.closestEntity.getEyeHeight(), this.closestEntity.posZ, 10.0f, this.theWatcher.getVerticalFaceSpeed());
        --this.lookTime;
    }
}

