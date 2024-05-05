/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.entity.ai;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAITarget;
import net.minecraft.entity.passive.EntityTameable;

public class EntityAIOwnerHurtByTarget
extends EntityAITarget {
    EntityTameable theDefendingTameable;
    EntityLivingBase theOwnerAttacker;
    private int field_142051_e;

    public EntityAIOwnerHurtByTarget(EntityTameable theDefendingTameableIn) {
        super(theDefendingTameableIn, false);
        this.theDefendingTameable = theDefendingTameableIn;
        this.setMutexBits(1);
    }

    @Override
    public boolean shouldExecute() {
        if (!this.theDefendingTameable.isTamed()) {
            return false;
        }
        EntityLivingBase entitylivingbase = this.theDefendingTameable.getOwner();
        if (entitylivingbase == null) {
            return false;
        }
        this.theOwnerAttacker = entitylivingbase.getAITarget();
        int i = entitylivingbase.getRevengeTimer();
        if (i == this.field_142051_e) return false;
        if (!this.isSuitableTarget(this.theOwnerAttacker, false)) return false;
        if (!this.theDefendingTameable.shouldAttackEntity(this.theOwnerAttacker, entitylivingbase)) return false;
        return true;
    }

    @Override
    public void startExecuting() {
        this.taskOwner.setAttackTarget(this.theOwnerAttacker);
        EntityLivingBase entitylivingbase = this.theDefendingTameable.getOwner();
        if (entitylivingbase != null) {
            this.field_142051_e = entitylivingbase.getRevengeTimer();
        }
        super.startExecuting();
    }
}

