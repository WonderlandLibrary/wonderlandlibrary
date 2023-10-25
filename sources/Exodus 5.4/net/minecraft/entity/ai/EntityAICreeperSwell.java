/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.entity.ai;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.monster.EntityCreeper;

public class EntityAICreeperSwell
extends EntityAIBase {
    EntityLivingBase creeperAttackTarget;
    EntityCreeper swellingCreeper;

    @Override
    public void startExecuting() {
        this.swellingCreeper.getNavigator().clearPathEntity();
        this.creeperAttackTarget = this.swellingCreeper.getAttackTarget();
    }

    @Override
    public boolean shouldExecute() {
        EntityLivingBase entityLivingBase = this.swellingCreeper.getAttackTarget();
        return this.swellingCreeper.getCreeperState() > 0 || entityLivingBase != null && this.swellingCreeper.getDistanceSqToEntity(entityLivingBase) < 9.0;
    }

    @Override
    public void updateTask() {
        if (this.creeperAttackTarget == null) {
            this.swellingCreeper.setCreeperState(-1);
        } else if (this.swellingCreeper.getDistanceSqToEntity(this.creeperAttackTarget) > 49.0) {
            this.swellingCreeper.setCreeperState(-1);
        } else if (!this.swellingCreeper.getEntitySenses().canSee(this.creeperAttackTarget)) {
            this.swellingCreeper.setCreeperState(-1);
        } else {
            this.swellingCreeper.setCreeperState(1);
        }
    }

    public EntityAICreeperSwell(EntityCreeper entityCreeper) {
        this.swellingCreeper = entityCreeper;
        this.setMutexBits(1);
    }

    @Override
    public void resetTask() {
        this.creeperAttackTarget = null;
    }
}

