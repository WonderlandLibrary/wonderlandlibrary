/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.entity.ai;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAITarget;
import net.minecraft.entity.passive.EntityTameable;

public class EntityAIOwnerHurtByTarget
extends EntityAITarget {
    private int field_142051_e;
    EntityLivingBase theOwnerAttacker;
    EntityTameable theDefendingTameable;

    @Override
    public boolean shouldExecute() {
        if (!this.theDefendingTameable.isTamed()) {
            return false;
        }
        EntityLivingBase entityLivingBase = this.theDefendingTameable.getOwner();
        if (entityLivingBase == null) {
            return false;
        }
        this.theOwnerAttacker = entityLivingBase.getAITarget();
        int n = entityLivingBase.getRevengeTimer();
        return n != this.field_142051_e && this.isSuitableTarget(this.theOwnerAttacker, false) && this.theDefendingTameable.shouldAttackEntity(this.theOwnerAttacker, entityLivingBase);
    }

    @Override
    public void startExecuting() {
        this.taskOwner.setAttackTarget(this.theOwnerAttacker);
        EntityLivingBase entityLivingBase = this.theDefendingTameable.getOwner();
        if (entityLivingBase != null) {
            this.field_142051_e = entityLivingBase.getRevengeTimer();
        }
        super.startExecuting();
    }

    public EntityAIOwnerHurtByTarget(EntityTameable entityTameable) {
        super(entityTameable, false);
        this.theDefendingTameable = entityTameable;
        this.setMutexBits(1);
    }
}

