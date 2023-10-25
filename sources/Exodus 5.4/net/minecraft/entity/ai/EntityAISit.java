/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.entity.ai;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.passive.EntityTameable;

public class EntityAISit
extends EntityAIBase {
    private EntityTameable theEntity;
    private boolean isSitting;

    @Override
    public void startExecuting() {
        this.theEntity.getNavigator().clearPathEntity();
        this.theEntity.setSitting(true);
    }

    @Override
    public void resetTask() {
        this.theEntity.setSitting(false);
    }

    public EntityAISit(EntityTameable entityTameable) {
        this.theEntity = entityTameable;
        this.setMutexBits(5);
    }

    public void setSitting(boolean bl) {
        this.isSitting = bl;
    }

    @Override
    public boolean shouldExecute() {
        if (!this.theEntity.isTamed()) {
            return false;
        }
        if (this.theEntity.isInWater()) {
            return false;
        }
        if (!this.theEntity.onGround) {
            return false;
        }
        EntityLivingBase entityLivingBase = this.theEntity.getOwner();
        return entityLivingBase == null ? true : (this.theEntity.getDistanceSqToEntity(entityLivingBase) < 144.0 && entityLivingBase.getAITarget() != null ? false : this.isSitting);
    }
}

