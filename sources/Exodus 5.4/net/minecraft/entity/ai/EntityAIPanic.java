/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.entity.ai;

import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.RandomPositionGenerator;
import net.minecraft.util.Vec3;

public class EntityAIPanic
extends EntityAIBase {
    protected double speed;
    private EntityCreature theEntityCreature;
    private double randPosZ;
    private double randPosY;
    private double randPosX;

    @Override
    public boolean continueExecuting() {
        return !this.theEntityCreature.getNavigator().noPath();
    }

    public EntityAIPanic(EntityCreature entityCreature, double d) {
        this.theEntityCreature = entityCreature;
        this.speed = d;
        this.setMutexBits(1);
    }

    @Override
    public boolean shouldExecute() {
        if (this.theEntityCreature.getAITarget() == null && !this.theEntityCreature.isBurning()) {
            return false;
        }
        Vec3 vec3 = RandomPositionGenerator.findRandomTarget(this.theEntityCreature, 5, 4);
        if (vec3 == null) {
            return false;
        }
        this.randPosX = vec3.xCoord;
        this.randPosY = vec3.yCoord;
        this.randPosZ = vec3.zCoord;
        return true;
    }

    @Override
    public void startExecuting() {
        this.theEntityCreature.getNavigator().tryMoveToXYZ(this.randPosX, this.randPosY, this.randPosZ, this.speed);
    }
}

