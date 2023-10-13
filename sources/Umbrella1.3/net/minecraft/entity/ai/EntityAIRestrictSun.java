/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.entity.ai;

import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.pathfinding.PathNavigateGround;

public class EntityAIRestrictSun
extends EntityAIBase {
    private EntityCreature theEntity;
    private static final String __OBFID = "CL_00001611";

    public EntityAIRestrictSun(EntityCreature p_i1652_1_) {
        this.theEntity = p_i1652_1_;
    }

    @Override
    public boolean shouldExecute() {
        return this.theEntity.worldObj.isDaytime();
    }

    @Override
    public void startExecuting() {
        ((PathNavigateGround)this.theEntity.getNavigator()).func_179685_e(true);
    }

    @Override
    public void resetTask() {
        ((PathNavigateGround)this.theEntity.getNavigator()).func_179685_e(false);
    }
}

