/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.entity.ai;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.pathfinding.PathNavigateGround;

public class EntityAISwimming
extends EntityAIBase {
    private EntityLiving theEntity;

    public EntityAISwimming(EntityLiving entitylivingIn) {
        this.theEntity = entitylivingIn;
        this.setMutexBits(4);
        ((PathNavigateGround)entitylivingIn.getNavigator()).setCanSwim(true);
    }

    @Override
    public boolean shouldExecute() {
        if (this.theEntity.isInWater()) return true;
        if (this.theEntity.isInLava()) return true;
        return false;
    }

    @Override
    public void updateTask() {
        if (!(this.theEntity.getRNG().nextFloat() < 0.8f)) return;
        this.theEntity.getJumpHelper().setJumping();
    }
}

