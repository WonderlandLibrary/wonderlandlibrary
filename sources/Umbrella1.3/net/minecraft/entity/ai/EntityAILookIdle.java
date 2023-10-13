/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.entity.ai;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.ai.EntityAIBase;

public class EntityAILookIdle
extends EntityAIBase {
    private EntityLiving idleEntity;
    private double lookX;
    private double lookZ;
    private int idleTime;
    private static final String __OBFID = "CL_00001607";

    public EntityAILookIdle(EntityLiving p_i1647_1_) {
        this.idleEntity = p_i1647_1_;
        this.setMutexBits(3);
    }

    @Override
    public boolean shouldExecute() {
        return this.idleEntity.getRNG().nextFloat() < 0.02f;
    }

    @Override
    public boolean continueExecuting() {
        return this.idleTime >= 0;
    }

    @Override
    public void startExecuting() {
        double var1 = Math.PI * 2 * this.idleEntity.getRNG().nextDouble();
        this.lookX = Math.cos(var1);
        this.lookZ = Math.sin(var1);
        this.idleTime = 20 + this.idleEntity.getRNG().nextInt(20);
    }

    @Override
    public void updateTask() {
        --this.idleTime;
        this.idleEntity.getLookHelper().setLookPosition(this.idleEntity.posX + this.lookX, this.idleEntity.posY + (double)this.idleEntity.getEyeHeight(), this.idleEntity.posZ + this.lookZ, 10.0f, this.idleEntity.getVerticalFaceSpeed());
    }
}

