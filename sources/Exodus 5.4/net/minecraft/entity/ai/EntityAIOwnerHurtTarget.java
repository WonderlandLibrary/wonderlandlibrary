/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.entity.ai;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAITarget;
import net.minecraft.entity.passive.EntityTameable;

public class EntityAIOwnerHurtTarget
extends EntityAITarget {
    EntityTameable theEntityTameable;
    EntityLivingBase theTarget;
    private int field_142050_e;

    @Override
    public void startExecuting() {
        this.taskOwner.setAttackTarget(this.theTarget);
        EntityLivingBase entityLivingBase = this.theEntityTameable.getOwner();
        if (entityLivingBase != null) {
            this.field_142050_e = entityLivingBase.getLastAttackerTime();
        }
        super.startExecuting();
    }

    @Override
    public boolean shouldExecute() {
        if (!this.theEntityTameable.isTamed()) {
            return false;
        }
        EntityLivingBase entityLivingBase = this.theEntityTameable.getOwner();
        if (entityLivingBase == null) {
            return false;
        }
        this.theTarget = entityLivingBase.getLastAttacker();
        int n = entityLivingBase.getLastAttackerTime();
        return n != this.field_142050_e && this.isSuitableTarget(this.theTarget, false) && this.theEntityTameable.shouldAttackEntity(this.theTarget, entityLivingBase);
    }

    public EntityAIOwnerHurtTarget(EntityTameable entityTameable) {
        super(entityTameable, false);
        this.theEntityTameable = entityTameable;
        this.setMutexBits(1);
    }
}

