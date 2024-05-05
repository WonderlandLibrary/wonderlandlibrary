package net.minecraft.entity.ai;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.ai.EntityAIBase;

public class EntityAILookIdle
extends EntityAIBase {
    private EntityLiving idleEntity;
    private double lookX;
    private double lookZ;
    private int idleTime;

    public EntityAILookIdle(EntityLiving entitylivingIn) {
        this.idleEntity = entitylivingIn;
        this.setMutexBits(3);
    }

    public boolean shouldExecute() {
        return this.idleEntity.getRNG().nextFloat() < 0.02f;
    }

    public boolean continueExecuting() {
        return this.idleTime >= 0;
    }

    public void startExecuting() {
        double d0 = Math.PI * 2 * this.idleEntity.getRNG().nextDouble();
        this.lookX = Math.cos((double)d0);
        this.lookZ = Math.sin((double)d0);
        this.idleTime = 20 + this.idleEntity.getRNG().nextInt(20);
    }

    public void updateTask() {
        --this.idleTime;
        this.idleEntity.getLookHelper().setLookPosition(this.idleEntity.posX + this.lookX, this.idleEntity.posY + (double)this.idleEntity.getEyeHeight(), this.idleEntity.posZ + this.lookZ, 10.0f, (float)this.idleEntity.getVerticalFaceSpeed());
    }
}
