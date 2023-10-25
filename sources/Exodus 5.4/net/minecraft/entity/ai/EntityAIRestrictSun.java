/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.entity.ai;

import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.pathfinding.PathNavigateGround;

public class EntityAIRestrictSun
extends EntityAIBase {
    private EntityCreature theEntity;

    @Override
    public void startExecuting() {
        ((PathNavigateGround)this.theEntity.getNavigator()).setAvoidSun(true);
    }

    @Override
    public void resetTask() {
        ((PathNavigateGround)this.theEntity.getNavigator()).setAvoidSun(false);
    }

    @Override
    public boolean shouldExecute() {
        return this.theEntity.worldObj.isDaytime();
    }

    public EntityAIRestrictSun(EntityCreature entityCreature) {
        this.theEntity = entityCreature;
    }
}

