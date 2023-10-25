/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.entity.ai;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.ai.EntityAIBase;

public class EntityAILookIdle
extends EntityAIBase {
    private EntityLiving idleEntity;
    private int idleTime;
    private double lookZ;
    private double lookX;

    @Override
    public boolean continueExecuting() {
        return this.idleTime >= 0;
    }

    public EntityAILookIdle(EntityLiving entityLiving) {
        this.idleEntity = entityLiving;
        this.setMutexBits(3);
    }

    @Override
    public void updateTask() {
        --this.idleTime;
        this.idleEntity.getLookHelper().setLookPosition(this.idleEntity.posX + this.lookX, this.idleEntity.posY + (double)this.idleEntity.getEyeHeight(), this.idleEntity.posZ + this.lookZ, 10.0f, this.idleEntity.getVerticalFaceSpeed());
    }

    @Override
    public void startExecuting() {
        double d = Math.PI * 2 * this.idleEntity.getRNG().nextDouble();
        this.lookX = Math.cos(d);
        this.lookZ = Math.sin(d);
        this.idleTime = 20 + this.idleEntity.getRNG().nextInt(20);
    }

    @Override
    public boolean shouldExecute() {
        return this.idleEntity.getRNG().nextFloat() < 0.02f;
    }
}

